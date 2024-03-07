package ua.com.javarush.oleksandr.reddit.redditcloneabstract.error;

import lombok.AllArgsConstructor;
import org.springframework.validation.FieldError;

@AllArgsConstructor
public class FieldErrorEntity {
    private String field;
    private String message;

    public FieldErrorEntity(FieldError fieldError) {
        this.field = fieldError.getField();
        this.message = fieldError.getDefaultMessage();
    }
}
