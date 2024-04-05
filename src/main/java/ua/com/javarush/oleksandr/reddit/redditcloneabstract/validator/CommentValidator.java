package ua.com.javarush.oleksandr.reddit.redditcloneabstract.validator;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.CommentRequestDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.CommentService;

@Component
public class CommentValidator implements Validator {
    private CommentService commentService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return CommentRequestDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@Valid @NonNull Object target, @NonNull Errors errors) {
        CommentRequestDTO commentDTO = (CommentRequestDTO) target;

        if (commentService.isCommentEmpty(commentDTO)) {
            errors.rejectValue(
                    "text",
                    "comment.text.exists",
                    new Object[]{commentDTO.getText()},
                    null);
        }
    }

    @Autowired
    @Lazy
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }
}
