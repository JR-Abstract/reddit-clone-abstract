package ua.com.javarush.oleksandr.reddit.redditcloneabstract.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.error.ErrorEntity;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.error.SimpleErrorEntity;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    @Value("${subreddit.create.exception}")
    private String subredditCreateError;

    private final MessageSource messageSource;

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

    @ExceptionHandler(SubredditCreateException.class)
    public ResponseEntity<Object> handleException(SubredditCreateException e) {

        var errorInfo = new ErrorEntity(
                messageSource.getMessage(subredditCreateError, null, LocaleContextHolder.getLocale()),
                e.getErrors(),
                System.currentTimeMillis()
        );

        log.error(errorInfo.toString());

        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    // todo: check possibility to handle response exception with JwtAuthEntryPoint

    @ExceptionHandler(UserDisabledException.class)
    public ResponseEntity<Object> handleUserDisabledException(UserDisabledException ex) {
        return buildErrorResponseEntity(HttpStatus.FORBIDDEN, ex);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Object> handleInvalidPasswordException(InvalidPasswordException ex) {
        return buildErrorResponseEntity(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return buildErrorResponseEntity(HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        return buildErrorResponseEntity(HttpStatus.NOT_FOUND, ex);
    }

    private ResponseEntity<Object> buildErrorResponseEntity(HttpStatus status, RuntimeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, status);
    }
}