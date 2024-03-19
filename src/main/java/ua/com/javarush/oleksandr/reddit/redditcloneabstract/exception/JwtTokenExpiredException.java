package ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception;


import org.springframework.security.core.AuthenticationException;

public class JwtTokenExpiredException extends AuthenticationException {

    public JwtTokenExpiredException(String message) {
        super(message);
    }
}