package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.RefreshToken;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.RefreshTokenRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repository;


    public void save(RefreshToken token) {
        repository.save(token);
    }

    public void removeAll(Authentication authentication) {

        String userEmail = (String) authentication.getPrincipal();
        repository.deleteAllByUserEmail(userEmail);
    }

    public Optional<RefreshToken> findRefreshToken(String token) {

        return repository.findByValue(token);
    }

    public void remove(RefreshToken refreshToken) {
        repository.delete(refreshToken);
    }
}
