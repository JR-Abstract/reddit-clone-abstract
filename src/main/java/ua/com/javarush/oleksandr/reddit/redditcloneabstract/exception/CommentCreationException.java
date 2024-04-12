package ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception;

import ua.com.javarush.oleksandr.reddit.redditcloneabstract.error.FieldErrorEntity;

import java.util.List;

public class CommentCreationException extends GlobalApplicationException{
    public CommentCreationException(List<FieldErrorEntity> errors) {
        super("Comment creation error", errors);
    }
}
