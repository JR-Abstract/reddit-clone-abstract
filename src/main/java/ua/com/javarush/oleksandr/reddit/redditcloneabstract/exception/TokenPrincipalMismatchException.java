package ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception;


import org.springframework.security.core.AuthenticationException;

public class TokenPrincipalMismatchException extends AuthenticationException {

    public TokenPrincipalMismatchException(String message) {
        super(message);
    }
}