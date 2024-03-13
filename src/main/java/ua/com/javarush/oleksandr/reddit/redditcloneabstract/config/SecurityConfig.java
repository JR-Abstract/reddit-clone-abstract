package ua.com.javarush.oleksandr.reddit.redditcloneabstract.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        log.debug("Configuring security filter chain");

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(API_AUTH_PATHS).permitAll()
                        .requestMatchers(ADMIN_PATHS).hasRole("ADMIN")
                        .requestMatchers(SUBREDDIT_PATHS).hasRole("ADMIN")
                        .requestMatchers(POSTS_PATHS).hasRole("USER")
                        .requestMatchers(VOTE_PATHS).hasRole("USER")
                )
                .anonymous(anon -> anon.authorities("ROLE_USER"))
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

    private void initRole(DataSource dataSource) throws SQLException {

        log.debug("Initializing roles");
        final String INIT_ROLE_SQL = " INSERT INTO role (name) VALUES  ('ROLE_ADMIN'), ('ROLE_USER'); ";

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