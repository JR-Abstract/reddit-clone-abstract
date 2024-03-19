package ua.com.javarush.oleksandr.reddit.redditcloneabstract.validator;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.UserService;

@Component
public class UserValidator implements Validator {
    private UserService userService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@Valid @NonNull Object target, @NonNull Errors errors) {

        User user = (User) target;

        if (userService.isUsernameTaken(user)) {
            errors.rejectValue(
                    "username",
                    "user.username.exists",
                    new Object[]{user.getUsername()},
                    null);
        }

        if (userService.isEmailTaken(user)) {
            errors.rejectValue(
                    "email",
                    "user.email.exists",
                    new Object[]{user.getEmail()},
                    null);
        }
    }

    @Autowired
    @Lazy
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
