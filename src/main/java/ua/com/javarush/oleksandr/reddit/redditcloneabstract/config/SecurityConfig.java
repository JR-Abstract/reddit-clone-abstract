package ua.com.javarush.oleksandr.reddit.redditcloneabstract.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                                .anyRequest().permitAll()
//                        .requestMatchers("/api/v1/auth/login", "/api/v1/auth/signup").permitAll()
//                        .requestMatchers("/api/v1/posts/**").hasAuthority("ROLE_USER")
//                        .requestMatchers("/api/v1/subreddit/**").hasAuthority("ROLE_USER")
//                        .requestMatchers("/api/v1/vote/**").hasAuthority("ROLE_USER")
//                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        // .anyRequest().authenticated()
                )
                .build();
    }
}
