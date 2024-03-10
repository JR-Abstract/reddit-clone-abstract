package ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception;

import ua.com.javarush.oleksandr.reddit.redditcloneabstract.error.FieldErrorEntity;

import java.util.List;

public class SubredditCreateException extends RedditException {

    public SubredditCreateException(List<FieldErrorEntity> errors) {
        super(errors);
    }
}
