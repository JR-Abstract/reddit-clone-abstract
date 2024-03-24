package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.LoginRequest;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.RefreshRequest;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.RegisterRequest;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.*;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper.UserMapper;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.RefreshToken;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Role;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.RoleRepository;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.UserRepository;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.security.JwtBlacklist;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.security.JwtTokenProvider;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final RefreshTokenService refreshTokenService;

    private final JwtBlacklist blacklist;

    private final MessageSource messageSource;

    private static final String AUTH_USER_IS_DISABLED = "authentication.user.is.not.enabled";
    private static final String AUTH_USER_ALREADY_EXISTS = "authentication.user.already.exists";
    private static final String AUTH_USER_NOT_FOUND = "authentication.user.not.found";
    private static final String AUTH_INVALID_PASSWORD = "authentication.invalid.password";
    private static final String AUTH_ROLE_BY_DEFAULT_NOT_FOUND = "authentication.role.by.default.not.found";
    private static final String AUTH_REFRESH_TOKEN_NOT_FOUND = "authentication.refresh.token.is.not.founded";
    private static final String AUTH_REFRESH_TOKEN_IS_EXPIRED = "authentication.refresh.token.is.expired";
    private static final String AUTH_TOKENS_NOT_MATCHED = "authentication.tokens.not.matched";
    private static final String AUTH_JWT_TOKEN_IS_IN_BLACKLIST = "jwt.validation.token.is.invalid";

    @Value("${user.role.by.default}")
    private String defaultRole;

    @Transactional
    public AuthenticationResponse signup(RegisterRequest registerRequest) {

        User user = userMapper.registerRequestToUser(registerRequest);

        registerUser(user);

        return generateTokensAndAuthenticationResponse(user);
    }

    @Transactional
    public AuthenticationResponse login(LoginRequest loginRequest) {

        User user = validateUserCredentials(loginRequest.getUsername(), loginRequest.getPassword());

        // todo: implement maintaining max 3 (device) refresh tokens per user

        return generateTokensAndAuthenticationResponse(user);
    }

    @Transactional
    public void logout() {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var jwtToken = (String) authentication.getCredentials();
        blacklist.invalidateToken(jwtToken);
        refreshTokenService.removeAll(authentication);
    }

    @Transactional
    public AuthenticationResponse refreshToken(RefreshRequest request) {

        String jwtToken = request.getJwtToken();
        String requestRefreshToken = request.getRefreshToken();

        var foundRefreshToken = refreshTokenService.findRefreshToken(requestRefreshToken);

        RefreshToken refreshToken = foundRefreshToken
                .orElseThrow(() -> refreshTokenNotFoundException(requestRefreshToken));

        checkTokenMatch(jwtToken, refreshToken);
        checkRefreshTokenExpiration(refreshToken);

        refreshTokenService.remove(refreshToken);

        return generateTokensAndAuthenticationResponse(refreshToken.getUser());
    }

    private RuntimeException refreshTokenNotFoundException(String refreshToken) {

        String message = messageSource.getMessage(AUTH_REFRESH_TOKEN_NOT_FOUND,
                new Object[]{refreshToken}, Locale.getDefault());

        log.error(message);
        return new BadCredentialsException(message);
    }

    private void checkTokenMatch(String jwtToken, RefreshToken refreshToken) {

        String jwtSubject = jwtTokenProvider.extractSubjectFromToken(jwtToken);


        if (blacklist.isTokenInvalid(jwtToken)) {
            refreshTokenService.remove(refreshToken);
            log.error(AUTH_JWT_TOKEN_IS_IN_BLACKLIST);
            throw new BadCredentialsException(AUTH_JWT_TOKEN_IS_IN_BLACKLIST);
        }

        if (!Objects.equals(jwtSubject, refreshToken.getUser().getEmail())) {
            refreshTokenService.remove(refreshToken);
            log.error(AUTH_TOKENS_NOT_MATCHED);
            throw new TokenPrincipalMismatchException(AUTH_TOKENS_NOT_MATCHED);
        }
    }

    private void checkRefreshTokenExpiration(RefreshToken refreshToken) {

        if (refreshToken.getExpirationAt().before(new Date())) {
            String message = messageSource.getMessage(AUTH_REFRESH_TOKEN_IS_EXPIRED,
                    new Object[]{refreshToken}, Locale.getDefault());
            log.error(message);
            throw new RefreshTokenExpiredException(message);
        }
    }

    private void registerUser(User user) {

        ensureUserDoesNotExist(user);

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        var role = getRole(defaultRole);
        role.addUser(user);

        userRepository.save(user);
    }

    private void ensureUserDoesNotExist(User user) {

        if (userExistsCheck(user.getUsername())) {

            String message = messageSource.getMessage(AUTH_USER_ALREADY_EXISTS,
                    new Object[]{user.getUsername(), user.getEmail()}, Locale.getDefault());

            log.error(message);

            throw new UserDisabledException(message);
        }
    }

    private boolean userExistsCheck(String username) {

        return userRepository.findByUsername(username).isPresent();
    }

    private User validateUserCredentials(String username, String password) {

        User user = getUser(username);

        String storedPassword = user.getPassword();
        userPasswordCheck(password, storedPassword);

        userEnabledCheck(user);

        return user;
    }

    private User getUser(String username) {

        return userRepository.findByUsername(username)
                .orElseThrow(() -> {

                    String message = messageSource.getMessage(AUTH_USER_NOT_FOUND,
                            new Object[]{username}, Locale.getDefault());

                    log.error(message);

                    return new UserNotFoundException(message);
                });
    }

    private void userPasswordCheck(String inputPassword, String userPassword) {

        if (!passwordEncoder.matches(inputPassword, userPassword)) {

            String message = messageSource.getMessage(AUTH_INVALID_PASSWORD,
                    new Object[]{inputPassword}, Locale.getDefault());

            log.error(message);

            throw new InvalidPasswordException(message);
        }
    }

    private void userEnabledCheck(User user) {

        if (!user.isEnabled()) {

            String message = messageSource.getMessage(AUTH_USER_IS_DISABLED,
                    new Object[]{user.getEmail()}, Locale.getDefault());

            log.error(message);

            throw new UserDisabledException(message);
        }
    }

    private AuthenticationResponse generateTokensAndAuthenticationResponse(User user) {

        var jwtToken = jwtTokenProvider.generateToken(user);
        var jwtRefreshToken = jwtTokenProvider.generateRefreshToken(user);

        saveRefreshTokenEntity(jwtRefreshToken, user);

        return AuthenticationResponse.with()
                .accessToken(jwtToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }

    private void saveRefreshTokenEntity(String jwtRefreshToken, User user) {

        var refreshTokenEntity = RefreshToken.with()
                .value(jwtRefreshToken)
                .expirationAt(Timestamp.from(jwtTokenProvider.extractClaim(jwtRefreshToken,
                        Claims::getExpiration).toInstant()))
                .issuedAt(Timestamp.from(jwtTokenProvider.extractClaim(jwtRefreshToken,
                        Claims::getIssuedAt).toInstant()))
                .user(user)
                .build();

        refreshTokenService.save(refreshTokenEntity);
    }

    private Role getRole(String roleName) {

        Optional<Role> roleResult = roleRepository.getReferenceByName(roleName);
        return roleResult.orElseThrow(() -> {

            String message = messageSource.getMessage(AUTH_ROLE_BY_DEFAULT_NOT_FOUND,
                    new Object[]{roleName}, Locale.getDefault());

            log.error(message);

            return new RoleByDefaultNotFoundException(message);
        });
    }

    @Builder(builderMethodName = "with")
    public record AuthenticationResponse(

            @JsonProperty("access_token")
            String accessToken,

            @JsonProperty("refresh_token")
            String refreshToken
    ) {
    }
}
