package ua.com.javarush.oleksandr.reddit.redditcloneabstract.error;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ErrorEntity {
    private String message;
    private List<FieldErrorEntity> errors;
    private Long timestamp;
}
