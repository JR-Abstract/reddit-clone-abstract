package ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception;

import lombok.Getter;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.error.FieldErrorEntity;

import java.util.List;

@Getter
public class RedditException extends RuntimeException {

    private final List<FieldErrorEntity> errors;

    public RedditException(List<FieldErrorEntity> errors) {
        this.errors = errors;
    }
}
