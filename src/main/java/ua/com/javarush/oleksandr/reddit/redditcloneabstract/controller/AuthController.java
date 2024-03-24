package ua.com.javarush.oleksandr.reddit.redditcloneabstract.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.LoginRequest;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.RefreshRequest;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.RegisterRequest;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.LoginException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.SingUpException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper.UserMapper;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.ActivationUserService;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.AuthService;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.AuthService.AuthenticationResponse;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.ErrorHandlerService;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UserMapper mapper;

    private final ActivationUserService activationUserService;

    private final ErrorHandlerService errorHandlerService;

    private final MessageSource messageSource;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid RegisterRequest registerRequest,
                                    BindingResult bindingResult) {

        errorHandlerService.handle(bindingResult, SingUpException::new);

        log.debug(messageSource.getMessage("log.user.create", new Object[]{registerRequest.getUsername()},
                LocaleContextHolder.getLocale()));

        User user = mapper.registerRequestToUser(registerRequest);

        authService.signup(user);
        activationUserService.sendActivation(user);

        log.info(messageSource.getMessage("log.user.created", new Object[]{registerRequest.getUsername()},
                LocaleContextHolder.getLocale()));

        return ResponseEntity.ok("check your email and confirm it");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest,
                                   BindingResult bindingResult) {

        errorHandlerService.handle(bindingResult, LoginException::new);
        log.debug(messageSource.getMessage("log.user.login", new Object[]{loginRequest.getUsername()},
                LocaleContextHolder.getLocale()));

        AuthenticationResponse response = authService.login(loginRequest);

        return ResponseEntity.ok(response);
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