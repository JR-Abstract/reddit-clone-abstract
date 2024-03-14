package ua.com.javarush.oleksandr.reddit.redditcloneabstract.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.ActivationUserService;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activation")
public class ActivationController {

    @Value("${application.url}")
    private String url;

    private final ActivationUserService activationUserService;

    @GetMapping("/confirm")
    public ResponseEntity<?> activateUser(@RequestParam("token") String token) {

        boolean isActivated = activationUserService.activateUser(token);
        String redirectUrl = url + "/activation/result?success=" + isActivated;

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));

        return ResponseEntity.status(HttpStatus.SEE_OTHER).headers(headers).build();
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
