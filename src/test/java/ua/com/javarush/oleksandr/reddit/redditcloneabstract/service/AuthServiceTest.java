package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.LoginRequest;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.RegisterRequest;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper.UserMapper;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ActivationTokenService activationTokenService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private static final String TEST_USERNAME = "testuser";

    private static final String TEST_PASSWORD = "testpassword";

    private static final String WRONG_USERNAME = "wronguser";

    private static final String WRONG_PASSWORD = "wrongpassword";

    public AuthServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignup_EncodePassword() {

        User user = createUserWithEncodedPassword();
        authService.signup(createRegisterRequest());

        verify(passwordEncoder, times(1)).encode(TEST_PASSWORD);
        verify(userRepository, times(1)).save(user);
        verify(activationTokenService, times(1)).sendActivation(user);
        assertEquals(TEST_PASSWORD + "_encoded", user.getPassword());
    }

    @Test
    public void testLogin_Success() {

        User user = createUserWithEncodedPassword();
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(TEST_PASSWORD, user.getPassword())).thenReturn(true);

        User result = authService.login(createLoginRequest(TEST_USERNAME, TEST_PASSWORD));

        verify(userRepository, times(1)).findByUsername(TEST_USERNAME);
        verify(passwordEncoder, times(1)).matches(TEST_PASSWORD, user.getPassword());
        assertEquals(result.getUsername(), user.getUsername());
    }

    @Test
    public void testLogin_FailPassword() {
        User user = createUserWithEncodedPassword();
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(WRONG_PASSWORD, user.getPassword())).thenReturn(false);

        try {
            authService.login(createLoginRequest(TEST_USERNAME, WRONG_PASSWORD));
            fail("No exception thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid password", e.getMessage());
        }

        verify(userRepository, times(1)).findByUsername(TEST_USERNAME);
        verify(passwordEncoder, times(1)).matches(WRONG_PASSWORD, user.getPassword());
    }

    @Test
    public void testLogin_FailUsername() {
        when(userRepository.findByUsername(WRONG_USERNAME)).thenReturn(Optional.empty());

        try {
            authService.login(createLoginRequest(WRONG_USERNAME, TEST_PASSWORD));
            fail("No exception thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("User not found", e.getMessage());
        }

        verify(userRepository, times(1)).findByUsername(WRONG_USERNAME);
    }

    private RegisterRequest createRegisterRequest() {

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(AuthServiceTest.TEST_USERNAME);
        registerRequest.setPassword(AuthServiceTest.TEST_PASSWORD);
        return registerRequest;
    }

    private LoginRequest createLoginRequest(String username, String password) {

        var loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);
        return loginRequest;
    }


    private User createUserWithEncodedPassword() {
        User user = new User();
        user.setUsername(AuthServiceTest.TEST_USERNAME);
        user.setPassword(AuthServiceTest.TEST_PASSWORD);
        when(userMapper.registerRequestToUser(Mockito.any(RegisterRequest.class))).thenReturn(user);
        when(passwordEncoder.encode(Mockito.anyString())).thenAnswer(i -> i.getArguments()[0] + "_encoded");
        return user;
    }
}
