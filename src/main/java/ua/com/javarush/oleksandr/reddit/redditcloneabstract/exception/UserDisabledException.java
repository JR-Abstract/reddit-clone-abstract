package ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception;


public class UserDisabledException extends RuntimeException {

    public UserDisabledException(String msg) {
        super(msg);
    }
}
