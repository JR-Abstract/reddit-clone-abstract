package ua.com.javarush.oleksandr.reddit.redditcloneabstract.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.PostRequest;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.PostService;

@Component
@RequiredArgsConstructor
public class PostRequestValidator implements Validator {

    private final PostService postService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return PostRequest.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        PostRequest request = (PostRequest) target;

        String url = request.getUrl();

        if (postService.findByUrl(url).isPresent()) {
            errors.rejectValue("url", "post.url.exist", "Url already exist");
        }
    }
}
