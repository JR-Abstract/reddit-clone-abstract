package ua.com.javarush.oleksandr.reddit.redditcloneabstract.error;

import org.springframework.validation.FieldError;

public record FieldErrorEntity(String field, String message) {

    public FieldErrorEntity(FieldError error) {
        this(error.getField(), error.getDefaultMessage());
    }
}
