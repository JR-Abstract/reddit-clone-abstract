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
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.RegisterRequest;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.SingUpException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.LoginException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.AuthService;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.ErrorHandlerService;

import static org.hibernate.query.sqm.tree.SqmNode.log;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final ErrorHandlerService errorHandlerService;
    private final MessageSource messageSource;

    @PostMapping("/signup")
    public String signup(@RequestBody @Valid RegisterRequest registerRequest, BindingResult bindingResult) {

        errorHandlerService.handle(bindingResult, SingUpException::new);

        log.debug(messageSource.getMessage("log.user.create", new Object[]{registerRequest.getUsername()},
                LocaleContextHolder.getLocale()));

        authService.signup(registerRequest);

        log.info(messageSource.getMessage("log.user.created", new Object[]{registerRequest.getUsername()},
                LocaleContextHolder.getLocale()));

        return String.valueOf(new ResponseEntity<>("User Registration Successful", OK));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest, BindingResult bindingResult) {

        errorHandlerService.handle(bindingResult, LoginException::new);
        log.debug(messageSource.getMessage("log.user.login", new Object[]{loginRequest.getUsername()},
                LocaleContextHolder.getLocale()));

        try {
            authService.login(loginRequest);
            log.info(messageSource.getMessage("log.user.logged", new Object[]{loginRequest.getUsername()},
                    LocaleContextHolder.getLocale()));
            return ResponseEntity.ok("Login Successful");
        } catch (IllegalArgumentException e) {
            log.error(messageSource.getMessage("log.user.loginFailed",
                    new Object[]{loginRequest.getUsername(), e.getMessage()},
                    LocaleContextHolder.getLocale()));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login Failed");
        }
    }
}