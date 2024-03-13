package ua.com.javarush.oleksandr.reddit.redditcloneabstract.error;

public record SimpleErrorEntity(
        String message,
        Long timestamp
) {
}