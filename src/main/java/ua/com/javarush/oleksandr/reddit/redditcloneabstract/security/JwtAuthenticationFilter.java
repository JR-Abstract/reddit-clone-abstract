package ua.com.javarush.oleksandr.reddit.redditcloneabstract.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/api/**");

    private static final AntPathRequestMatcher EXCLUDE_AUTH_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/api/v1/auth/**");

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.validation.token.is.invalid}")
    private String invalidJwtToken;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {

        if (!EXCLUDE_AUTH_PATH_REQUEST_MATCHER.matches(request)) {

            return super.requiresAuthentication(request, response)
                   && Objects.isNull(SecurityContextHolder.getContext().getAuthentication());
        }
        return false;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        var header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            String jwtToken = header.substring(7);

            if (isTokenValid(jwtToken)) {

                var authRequest = new JwtAuthenticationToken(
                        jwtToken,
                        getUserEmail(jwtToken),
                        getGrandedAuthorities(jwtToken));

                authRequest.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                return authRequest;
            }

            throw new AuthenticationServiceException(invalidJwtToken);
        }

        return null;
    }

    private boolean isTokenValid(String token) {
        return jwtTokenProvider.validate(token);
    }

    private String getUserEmail(String token) {
        return jwtTokenProvider.extractSubjectFromToken(token);
    }

    private Set<? extends GrantedAuthority> getGrandedAuthorities(String jwtToken) {

        List<?> rawAuthorities = jwtTokenProvider.extractClaim(
                jwtToken,
                claims -> claims.get("authorities", List.class));

        return rawAuthorities.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authResult);

        if (this.logger.isDebugEnabled()) {
            this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s", authResult));
        }

        getRememberMeServices().loginSuccess(request, response, authResult);

        if (this.eventPublisher != null) {
            this.eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
        }

        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {


        logger.debug("Failed to authenticate user via JwtToken", failed);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Unauthorized - " + failed.getMessage() + "\"}");
    }
}
