package ua.com.javarush.oleksandr.reddit.redditcloneabstract.error;

import java.util.List;

public record ErrorEntity(
        String message,
        List<FieldErrorEntity> errors,
        long timestamp
) {
}
