package ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception;

import ua.com.javarush.oleksandr.reddit.redditcloneabstract.error.FieldErrorEntity;

import java.util.List;

public class SingUpException extends RedditException{
    public SingUpException(List<FieldErrorEntity> errors) {
        super(errors);
    }
}
