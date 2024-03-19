package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.ActivationToken;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.ActivationTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.apache.commons.lang3.BooleanUtils.isFalse;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ActivationTokenService {

    private final ActivationTokenRepository activationTokenRepository;
    private final MailService mailService;
    @Value("${application.url}")
    private String url;

    public void sendActivation(User user) {
        String token = generateToken();
        ActivationToken activationToken = ActivationToken.with()
                .activated(false)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .token(token)
                .user(user)
                .build();

        activationTokenRepository.save(activationToken);

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

    private boolean isActivated(ActivationToken token) {
        return isFalse(token.getActivated());
    }

    private boolean isExpired(ActivationToken token) {
        return token.getExpiryDate().isBefore(LocalDateTime.now());
    }


    @Transactional
    public boolean activateUser(String token) {
        Optional<ActivationToken> optionalActivationToken = activationTokenRepository.findByToken(token);

        if (optionalActivationToken.isEmpty()) return false;

        ActivationToken activationToken = optionalActivationToken.get();
        User user = activationToken.getUser();

        if (!isActivated(activationToken)) return false;
        if (isExpired(activationToken)) {
            sendActivation(user);
            return false;
        }

        user.setEnabled(true);
        activationToken.setActivated(true);

        activationTokenRepository.save(activationToken);

        return true;
    }
}
