package ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception;

import org.springframework.security.core.AuthenticationException;

public class RefreshTokenExpiredException extends AuthenticationException {

    public RefreshTokenExpiredException(String message) {
        super(message);
    }
}
