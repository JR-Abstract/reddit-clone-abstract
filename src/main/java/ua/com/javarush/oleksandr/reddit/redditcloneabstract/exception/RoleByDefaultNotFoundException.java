package ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception;

public class RoleByDefaultNotFoundException extends RuntimeException {

    public RoleByDefaultNotFoundException(String message) {
        super(message);
    }
}
