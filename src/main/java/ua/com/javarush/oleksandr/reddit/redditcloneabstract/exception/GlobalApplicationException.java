package ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception;

import lombok.Getter;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.error.FieldErrorEntity;

import java.util.List;

@Getter
public class GlobalApplicationException extends RuntimeException {

    private final List<FieldErrorEntity> errors;

    public GlobalApplicationException(String message, List<FieldErrorEntity> errors) {
        super(message);
        this.errors = errors;
    }
}