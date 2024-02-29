package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.LoginRequest;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.RegisterRequest;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper.UserMapper;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void signup(RegisterRequest registerRequest) {
        User user = userMapper.registerRequestToUser(registerRequest);

        userRepository.save(user);
    }

    public User login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!password.equals(user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        return user;
    }
}
