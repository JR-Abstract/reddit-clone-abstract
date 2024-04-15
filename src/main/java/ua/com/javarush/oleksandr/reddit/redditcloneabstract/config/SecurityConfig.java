package ua.com.javarush.oleksandr.reddit.redditcloneabstract.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.security.JwtAuthenticationFilter;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.security.JwtBlacklist;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.security.JwtTokenProvider;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.UserService;

@Configuration
@Slf4j
@EnableWebSecurity
public class SecurityConfig {

    private static final String API_AUTH_PATHS = "/api/v1/auth/**";
    private static final String ACTIVATION = "/activation/**";
    private static final String ADMIN_PATHS = "/admin/**";
    private static final String POSTS_PATHS = "/api/v1/posts/**";
    private static final String SUBREDDIT_PATHS = "/api/v1/subreddit/**";
    private static final String VOTE_PATHS = "/api/v1/vote/**";
    private static final String COMMENT_PATHS = "/api/v1/comments/**";

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final JwtBlacklist jwtBlacklist;

    public SecurityConfig(
            JwtTokenProvider jwtTokenProvider,
            UserService userService,
            JwtBlacklist jwtBlacklist) {

        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.jwtBlacklist = jwtBlacklist;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        log.debug("Configuring security filter chain");

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .userDetailsService(userService)
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider, jwtBlacklist),
                        UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(API_AUTH_PATHS, ACTIVATION).permitAll()
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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}