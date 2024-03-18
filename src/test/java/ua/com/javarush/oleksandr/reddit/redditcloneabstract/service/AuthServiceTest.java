package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.RegisterRequest;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper.UserMapper;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.UserRepository;

import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    public AuthServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignup_EncodePassword() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("testpassword");

        User user = new User();
        user.setUsername("testuser");

        when(userMapper.map(registerRequest)).thenReturn(user);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        authService.signup(registerRequest);

        verify(passwordEncoder, times(1)).encode("testpassword");
        verify(userRepository, times(1)).save(user);
    }
}
