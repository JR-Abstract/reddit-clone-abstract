package ua.com.javarush.oleksandr.reddit.redditcloneabstract.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.security.JwtAuthenticationEntryPoint;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.security.JwtAuthenticationFilter;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.security.JwtTokenProvider;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.UserService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
@Slf4j
public class SecurityConfig {

    private static final String API_AUTH_PATHS = "/api/v1/auth/**";
    private static final String ADMIN_PATHS = "/admin/**";
    private static final String POSTS_PATHS = "/api/v1/posts/**";
    private static final String SUBREDDIT_PATHS = "/api/v1/subreddit/**";
    private static final String VOTE_PATHS = "/api/v1/vote/**";
    private static final String COMMENT_PATHS = "/api/v1/comments/**";

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final JwtAuthenticationEntryPoint jwtAuthEntryPoint;

    public SecurityConfig(
            JwtTokenProvider jwtTokenProvider,
            UserService userService,
            JwtAuthenticationEntryPoint jwtAuthEntryPoint) {

        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        log.debug("Configuring security filter chain");

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .userDetailsService(userService)
                .exceptionHandling(eh -> eh.authenticationEntryPoint(jwtAuthEntryPoint))
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(API_AUTH_PATHS).permitAll()
                        .requestMatchers(ADMIN_PATHS).hasRole("ADMIN")
                        .requestMatchers(SUBREDDIT_PATHS).hasRole("USER")
                        .requestMatchers(POSTS_PATHS).hasRole("USER")
                        .requestMatchers(VOTE_PATHS).hasRole("USER")
                        .requestMatchers(COMMENT_PATHS).hasRole("USER")
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    CommandLineRunner initRoleIfNotExistsRunner(DataSource dataSource) {
        return args -> {

            log.debug("Initializing roles if not exists");
            if (!roleExists(dataSource)) {
                initRole(dataSource);
            }
        };
    }

    private boolean roleExists(DataSource dataSource) throws SQLException {

        log.debug("Checking if roles exist");
        final String CHECK_ROLE_SQL = "SELECT COUNT(*) FROM role";

        try (Connection connection = dataSource.getConnection();

             Statement checkStmt = connection.createStatement();
             ResultSet rs = checkStmt.executeQuery(CHECK_ROLE_SQL)) {

            return rs.next() && rs.getLong(1) > 0;
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private void initRole(DataSource dataSource) throws SQLException {

        log.debug("Initializing roles");
        final String INIT_ROLE_SQL =
                "INSERT INTO role (name, created_at) VALUES  ('ROLE_ADMIN', NOW()), ('ROLE_USER', NOW());";

        try (Connection connection = dataSource.getConnection();

             Statement insertStmt = connection.createStatement()) {

            int affectedRows = insertStmt.executeUpdate(INIT_ROLE_SQL);
            if (affectedRows != 2) {
                throw new SQLException(
                        "Could not initialize roles, expected to affect 2 rows, but affected " + affectedRows);
            }
        }
    }
}