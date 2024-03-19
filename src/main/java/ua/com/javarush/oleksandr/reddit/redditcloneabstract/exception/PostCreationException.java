package ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception;

import ua.com.javarush.oleksandr.reddit.redditcloneabstract.error.FieldErrorEntity;

import java.util.List;

public class PostCreationException extends GlobalApplicationException {
    public PostCreationException(List<FieldErrorEntity> errors) {
        super("Post creation error", errors);
    }
}
