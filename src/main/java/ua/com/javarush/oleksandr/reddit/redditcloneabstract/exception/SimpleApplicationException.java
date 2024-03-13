package ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception;

public class SimpleApplicationException extends RuntimeException {

    public SimpleApplicationException(String message) {
        super(message);
    }
}