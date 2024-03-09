package ua.com.javarush.oleksandr.reddit.redditcloneabstract.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.error.ErrorEntity;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.error.SimpleErrorEntity;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.PostCreationException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.PostNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PostCreationException.class)
    public ResponseEntity<?> handleException(PostCreationException exception) {
        ErrorEntity error = new ErrorEntity(exception.getMessage(), exception.getErrors(), System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<?> handleException(PostNotFoundException exception) {
        SimpleErrorEntity error = new SimpleErrorEntity(exception.getMessage(), System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}