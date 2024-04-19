package ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
