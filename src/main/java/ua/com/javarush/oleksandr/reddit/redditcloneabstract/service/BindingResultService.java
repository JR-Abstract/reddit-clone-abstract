package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.error.FieldErrorEntity;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.RedditException;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class BindingResultService {

    private final MessageSource messageSource;

    public void handle(Errors errors,
                       Function<List<FieldErrorEntity>, ? extends RedditException> errorHandler) {

        if (errors.hasErrors()) {
            var transformedErrors = transformErrors(errors.getFieldErrors());
            throw errorHandler.apply(transformedErrors);
        }
    }

    private List<FieldErrorEntity> transformErrors(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .map(this::toFieldErrorEntity)
                .toList();
    }

    private FieldErrorEntity toFieldErrorEntity(FieldError error) {
        return new FieldErrorEntity(
                error.getField(),
                messageSource.getMessage(error, LocaleContextHolder.getLocale()));
    }
}
