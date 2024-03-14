package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;

import java.util.UUID;

import static java.util.Objects.isNull;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ActivationUserService {

    @Value("${application.url}")
    private String url;

    private final MailService mailService;
    private final UserService userService;

    public void sendActivation(User user) {
        String token = generateToken();
        user.setActivationToken(token);

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

    public boolean activateUser(String token) {
        User user = userService.findByActivationToken(token);

        if (isNull(user)) return false;

        user.setEnabled(true);
        user.setActivationToken(null);
        userService.update(user);

        return true;
    }
}
