package ua.com.javarush.oleksandr.reddit.redditcloneabstract.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.ActivationUserService;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.AuthService.AuthenticationResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activation")
public class ActivationController {

    private final ActivationUserService activationUserService;
    @Value("${application.url}")
    private String url;

    @GetMapping("/confirm")
    public AuthenticationResponse activateUser(@RequestParam("token") String token) {
        return activationUserService.activateUser(token);
    }

    // TODO: Needs to be deleted when receiving a request to the front part is implemented
    @GetMapping("/result")
    public ResponseEntity<?> activationResult(@RequestParam("success") Boolean success) {

        String message;
        HttpStatus status;

        if (success) {
            message = "Activation successful";
            status = HttpStatus.OK;
        } else {
            message = "Activation failed";
            status = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity.status(status).body(message);
    }
}
