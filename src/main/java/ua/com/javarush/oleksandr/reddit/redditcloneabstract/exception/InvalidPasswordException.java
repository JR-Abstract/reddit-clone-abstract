package ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException(String message) {
        super(message);
    }
}
