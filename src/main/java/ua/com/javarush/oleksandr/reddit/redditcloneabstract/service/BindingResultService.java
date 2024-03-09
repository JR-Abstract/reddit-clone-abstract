package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.error.FieldErrorEntity;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.RedditException;

import java.util.List;
import java.util.function.Function;

@Service
public class BindingResultService {

    public void handle(BindingResult bindingResult, Function<List<FieldErrorEntity>, ? extends RedditException> errorHandler) {

        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getFieldErrors().stream()
                    .map(FieldErrorEntity::new)
                    .toList();

            throw errorHandler.apply(errors);
        }
    }
}
