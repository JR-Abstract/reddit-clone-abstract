package ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception;

import ua.com.javarush.oleksandr.reddit.redditcloneabstract.error.FieldErrorEntity;

import java.util.List;

public class LoginException extends RedditException {
    public LoginException(List<FieldErrorEntity> errors) {
        super(errors);
    }
}
