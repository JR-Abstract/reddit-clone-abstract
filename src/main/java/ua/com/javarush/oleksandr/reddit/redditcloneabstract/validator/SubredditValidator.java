package ua.com.javarush.oleksandr.reddit.redditcloneabstract.validator;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Subreddit;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.SubredditService;

@Component
public class SubredditValidator implements Validator {

    private SubredditService subredditService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return Subreddit.class.isAssignableFrom(clazz);
    }


    @Override
    public void validate(@Valid @NonNull Object target, @NonNull Errors errors) {

        Subreddit subreddit = (Subreddit) target;

        if (subredditService.isSubredditNameTakenByUser(subreddit)) {
            errors.rejectValue(
                    "name",
                    "subreddit.name.exists",
                    new Object[]{subreddit.getName(), subreddit.getUser().getUsername()},
                    null);
        }
    }

    @Autowired
    @Lazy
    public void setSubredditService(SubredditService subredditService) {
        this.subredditService = subredditService;
    }
}
