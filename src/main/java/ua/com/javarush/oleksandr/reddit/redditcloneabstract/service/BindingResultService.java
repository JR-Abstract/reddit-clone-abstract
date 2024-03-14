package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.error.FieldErrorEntity;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class BindingResultService {

    public void handle(BindingResult bindingResult, Function<List<FieldErrorEntity>, RuntimeException> exception) {

        if (bindingResult.hasErrors()) {
            List<FieldErrorEntity> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldErrorEntity::new)
                    .toList();

            throw exception.apply(errors);
        }
    }
}
