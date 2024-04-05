package ua.com.javarush.oleksandr.reddit.redditcloneabstract.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.LoginRequest;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.RefreshRequest;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.RegisterRequest;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.LoginException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.SingUpException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.AuthService;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.AuthService.AuthenticationResponse;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.ErrorHandlerService;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;
    private final ErrorHandlerService errorHandlerService;
    private final MessageSource messageSource;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody @Valid RegisterRequest registerRequest, BindingResult bindingResult) {

        errorHandlerService.handle(bindingResult, SingUpException::new);

        log.debug(messageSource.getMessage("log.user.create", new Object[]{registerRequest.getUsername()},
                LocaleContextHolder.getLocale()));

        AuthenticationResponse response = authService.signup(registerRequest);

        log.info(messageSource.getMessage("log.user.created", new Object[]{registerRequest.getUsername()},
                LocaleContextHolder.getLocale()));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest, BindingResult bindingResult) {

        errorHandlerService.handle(bindingResult, LoginException::new);
        log.debug(messageSource.getMessage("log.user.login", new Object[]{loginRequest.getUsername()},
                LocaleContextHolder.getLocale()));

        try {
            AuthenticationResponse response = authService.login(loginRequest);
            log.info(messageSource.getMessage("log.user.logged", new Object[]{loginRequest.getUsername()},
                    LocaleContextHolder.getLocale()));
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error(messageSource.getMessage("log.user.loginFailed",
                    new Object[]{loginRequest.getUsername(), e.getMessage()},
                    LocaleContextHolder.getLocale()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {

        authService.logout();

        return ResponseEntity.ok("logout successful");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshRequest refreshRequest) {

        AuthenticationResponse response = authService.refreshToken(refreshRequest);

        return ResponseEntity.ok(response);
    }
}