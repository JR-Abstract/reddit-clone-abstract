package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.LoginRequest;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.InvalidPasswordException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.RoleByDefaultNotFoundException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.UserDisabledException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.UserNotFoundException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.RefreshToken;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Role;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.RefreshTokenRepository;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.RoleRepository;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.UserRepository;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.security.JwtTokenProvider;

import java.sql.Timestamp;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final RefreshTokenRepository refreshTokenRepository;

    private final MessageSource messageSource;

    private static final String AUTH_USER_IS_DISABLED = "authentication.user.is.not.enabled";
    private static final String AUTH_USER_ALREADY_EXISTS = "authentication.user.already.exists";
    private static final String AUTH_USER_NOT_FOUND = "authentication.user.not.found";
    private static final String AUTH_INVALID_PASSWORD = "authentication.invalid.password";
    private static final String AUTH_ROLE_BY_DEFAULT_NOT_FOUND = "authentication.role.by.default.not.found";

    @Value("${user.role.by.default}")
    private String defaultRole;

    @Transactional
    public void signup(User user) {
        register(user);
    }

    @Transactional
    public AuthenticationResponse login(LoginRequest loginRequest) {

        User user = validateUserCredentials(loginRequest.getUsername(), loginRequest.getPassword());

        // todo: implement maintaining max 3 (device) refresh tokens per user

        return generateTokensAndAuthenticationResponse(user);
    }

    @Transactional
    public void register(User user) {

        ensureUserDoesNotExist(user);

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        var role = getRole(defaultRole);
        role.addUser(user);

        userRepository.save(user);
    }

    public AuthenticationResponse proceedWithRegistration(User user) {
        return generateTokensAndAuthenticationResponse(user);
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
                .expirationAt(Timestamp.from(jwtTokenProvider.extractClaim(jwtRefreshToken, Claims::getExpiration).toInstant()))
                .issuedAt(Timestamp.from(jwtTokenProvider.extractClaim(jwtRefreshToken, Claims::getIssuedAt).toInstant()))
                .user(user)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);
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
