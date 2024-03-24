package ua.com.javarush.oleksandr.reddit.redditcloneabstract.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.LoginRequest;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.RefreshRequest;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.RegisterRequest;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.AuthService;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.AuthService.AuthenticationResponse;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody RegisterRequest registerRequest) {

        AuthenticationResponse response = authService.signup(registerRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {

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