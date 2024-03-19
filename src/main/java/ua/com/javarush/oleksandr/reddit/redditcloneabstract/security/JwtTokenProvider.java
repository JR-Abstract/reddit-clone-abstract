package ua.com.javarush.oleksandr.reddit.redditcloneabstract.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.JwtTokenExpiredException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    public static final String JWT_VALIDATION_EXPIRED_MESSAGE = "jwt.validation.token.is.expired";

    public static final String JWT_VALIDATION_FAILED_MESSAGE = "jwt.validation.failure";

    public static final String JWT_AUTHORITY = "authorities";

    private SecretKey secretKey;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationInMilliseconds;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshExpirationInMilliseconds;

    @Value("${user.role.by.default}")
    private String defaultRole;

    private final MessageSource messageSource;

    public String generateToken(User user) {
        return generateToken(user, new HashMap<>());
    }

    public String generateToken(@NonNull User user, @NonNull Map<String, Object> extraClaims) {

        Collection<String> authorities = extractAuthorityFromUser(user);
        extraClaims.put(JWT_AUTHORITY, authorities);

        return buildJwtToken(user.getUsername(), extraClaims, expirationInMilliseconds);
    }

    public boolean validate(String token) {

        try {

            Jwts.parser()
                    .verifyWith(getSecretKey()).build()
                    .parseSignedClaims(token);

            return true;

        } catch (ExpiredJwtException e) {

            var message = messageSource.getMessage(JWT_VALIDATION_EXPIRED_MESSAGE,
                    new Object[]{e}, Locale.getDefault());
            log.error(message);
            throw new JwtTokenExpiredException(message);

        } catch (JwtException | IllegalArgumentException e) {

            var message = messageSource.getMessage(JWT_VALIDATION_FAILED_MESSAGE,
                    new Object[]{e}, Locale.getDefault());
            log.error(message);
            throw new JwtException(message);
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {

        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public String extractSubjectFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateRefreshToken(User user) {

        UserDetails userDetails = new AccountDetails(user);
        Map<String, Object> claims = new HashMap<>();

        return buildJwtToken(userDetails.getUsername(), claims, refreshExpirationInMilliseconds);
    }

    private Collection<String> extractAuthorityFromUser(User user) {
        if (Objects.isNull(user.getRoles())) {
            return List.of(defaultRole);
        }

        return user.getRoles().stream().map(GrantedAuthority::getAuthority).toList();
    }

    private Claims extractAllClaims(String token) {

        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @PostConstruct
    private void initSecretKey() {

        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    private SecretKey getSecretKey() {
        return secretKey;
    }

    // todo: close
    public String buildJwtToken(String subject, Map<String, Object> claims, long expiration) {

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return Jwts
                .builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getSecretKey())
                .compact();
    }
}
