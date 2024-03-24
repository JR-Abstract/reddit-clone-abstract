package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.UserNotFoundException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.AuthService.AuthenticationResponse;

import java.util.UUID;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class ActivationUserService {

    private final MailService mailService;
    private final UserService userService;
    private final AuthService authService;

    @Value("${application.url}")
    private String url;

    @Transactional
    public void sendActivation(User user) {
        String token = generateToken();
        userService.findUserById(user.getUserId()).setActivationToken(token);

        String email = user.getEmail();
        String confirmLink = createConfirmLink(token);
        String message = "Your confirm link: %s".formatted(confirmLink);

        mailService.sendMessage(email, "Confirm registration on our site", message);
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    private String createConfirmLink(String token) {
        return url + "/activation/confirm?token=" + token;
    }

    public AuthenticationResponse activateUser(String token) {
        User user = userService.findByActivationToken(token);

        if (isNull(user)) throw new UserNotFoundException("USER NOT FOUND");

        user.setEnabled(true);
        user.setActivationToken(null);

        userService.update(user);

        return authService.proceedWithRegistration(user);
    }
}
