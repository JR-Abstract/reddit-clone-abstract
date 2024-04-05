package ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
