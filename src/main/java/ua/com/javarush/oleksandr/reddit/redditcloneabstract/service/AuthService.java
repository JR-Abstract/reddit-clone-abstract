package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public void signup(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();

        User user = userMapper.registerRequestToUser(registerRequest);

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);

        mailService.sendMessage(email, "Test", "Test text");
    }

    public User login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        return user;
    }
}
