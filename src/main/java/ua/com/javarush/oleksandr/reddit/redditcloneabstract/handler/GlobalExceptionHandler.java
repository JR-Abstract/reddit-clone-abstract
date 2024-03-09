package ua.com.javarush.oleksandr.reddit.redditcloneabstract.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.error.ErrorEntity;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.SubredditCreateException;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(SubredditCreateException.class)
    public ResponseEntity<Object> handleException(SubredditCreateException e) {

        var errorInfo = new ErrorEntity(
                messageSource.getMessage("subreddit.create.exception", null, LocaleContextHolder.getLocale()),
                e.getErrors(),
                System.currentTimeMillis()
        );

        log.error("log.subreddit.create.exception" + errorInfo);

        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }
}
