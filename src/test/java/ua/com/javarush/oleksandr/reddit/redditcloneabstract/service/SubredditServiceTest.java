package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.SubredditNotFoundException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Subreddit;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.SubredditRepository;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.validator.SubredditValidator;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubredditServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private SubredditRepository subredditRepository;

    @Mock
    private SubredditValidator subredditValidator;

    @Mock
    private ErrorHandlerService errorHandlerService;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private SubredditService subredditService;

    private User user;
    private Subreddit subreddit;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        subreddit = new Subreddit();
        subreddit.setSubscribers(new HashSet<>());
        user.setSubscriptions(new HashSet<>());
        subreddit.getSubscribers().add(user);
        user.getSubscriptions().add(subreddit);
    }

    @Test
    void findAll_whenCalled_invokesRepositoryAndReturnsList() {
        Subreddit subreddit1 = new Subreddit();
        Subreddit subreddit2 = new Subreddit();
        List<Subreddit> expectedSubreddits = List.of(subreddit1, subreddit2);

        when(subredditRepository.findAll()).thenReturn(expectedSubreddits);

        List<Subreddit> actualSubreddits = subredditService.findAll();

        verify(subredditRepository, times(1)).findAll();
        assertEquals(expectedSubreddits, actualSubreddits);
    }

    @Test
    void findById_whenSubredditFound_returnsSubreddit() {
        Long subredditId = 1L;
        Subreddit expectedSubreddit = new Subreddit();
        expectedSubreddit.setId(subredditId);

        when(subredditRepository.findById(subredditId)).thenReturn(Optional.of(expectedSubreddit));

        Optional<Subreddit> actualSubreddit = subredditService.findById(subredditId);

        verify(subredditRepository, times(1)).findById(subredditId);
        assertTrue(actualSubreddit.isPresent());
        assertEquals(expectedSubreddit, actualSubreddit.get());
    }

    @Test
    void findById_whenSubredditNotFound_returnsEmpty() {
        Long subredditId = 1L;

        when(subredditRepository.findById(subredditId)).thenReturn(Optional.empty());

        Optional<Subreddit> actualSubreddit = subredditService.findById(subredditId);

        verify(subredditRepository, times(1)).findById(subredditId);
        assertFalse(actualSubreddit.isPresent());
    }

    @Test
    void subscribeUser_whenSubredditExists_subscribesSuccessfully() {
        when(userService.findUserById(anyLong())).thenReturn(user);
        when(subredditRepository.findByIdWithSubscribers(anyLong())).thenReturn(Optional.of(subreddit));

        subredditService.subscribeUser(1L, 1L);

        verify(userService, times(1)).findUserById(1L);
        verify(subredditRepository, times(1)).findByIdWithSubscribers(1L);
        assertTrue(subreddit.getSubscribers().contains(user));
        assertTrue(user.getSubscriptions().contains(subreddit));
    }

    @Test
    void subscribeUser_whenSubredditDoesNotExist_throwsException() {
        when(userService.findUserById(anyLong())).thenReturn(user);
        when(subredditRepository.findByIdWithSubscribers(anyLong())).thenReturn(Optional.empty());

        assertThrows(SubredditNotFoundException.class, () -> subredditService.subscribeUser(1L, 1L));

        verify(userService, times(1)).findUserById(1L);
        verify(subredditRepository, times(1)).findByIdWithSubscribers(1L);
    }

    @Test
    void unsubscribeUser_whenSubredditExists_unsubscribesSuccessfully() {
        when(userService.findUserById(anyLong())).thenReturn(user);
        when(subredditRepository.findByIdWithSubscribers(anyLong())).thenReturn(Optional.of(subreddit));

        subredditService.unsubscribeUser(1L, 1L);

        verify(userService, times(1)).findUserById(1L);
        verify(subredditRepository, times(1)).findByIdWithSubscribers(1L);
        assertFalse(subreddit.getSubscribers().contains(user));
        assertFalse(user.getSubscriptions().contains(subreddit));
    }

    @Test
    void unsubscribeUser_whenSubredditDoesNotExist_throwsException() {
        when(userService.findUserById(anyLong())).thenReturn(user);
        when(subredditRepository.findByIdWithSubscribers(anyLong())).thenReturn(Optional.empty());

        assertThrows(SubredditNotFoundException.class, () -> subredditService.unsubscribeUser(1L, 1L));

        verify(userService, times(1)).findUserById(1L);
        verify(subredditRepository, times(1)).findByIdWithSubscribers(1L);
    }

    @Test
    void countSubscribersById_whenCalled_returnsCorrectCount() {
        Long subredditId = 1L;
        Long expectedCount = 42L;

        when(subredditRepository.countSubscribersById(subredditId)).thenReturn(expectedCount);

        Long actualCount = subredditService.countSubscribersById(subredditId);

        verify(subredditRepository, times(1)).countSubscribersById(subredditId);
        assertEquals(expectedCount, actualCount);
    }

    @Test
    void findAllSubscribersById_whenCalled_returnsSubscribersList() {
        Long subredditId = 1L;
        User user1 = new User();
        User user2 = new User();
        Collection<User> expectedSubscribers = List.of(user1, user2);

        when(subredditRepository.findAllSubscribersById(subredditId)).thenReturn(expectedSubscribers);

        Collection<User> actualSubscribers = subredditService.findAllSubscribersById(subredditId);

        verify(subredditRepository, times(1)).findAllSubscribersById(subredditId);
        assertEquals(expectedSubscribers, actualSubscribers);
    }

    @Test
    void save_whenSubredditIsValid_savesSubreddit() {
        Subreddit subreddit = new Subreddit();
        subreddit.setName("Test Subreddit");

        doNothing().when(subredditValidator).validate(any(Subreddit.class), any(Errors.class));

        subredditService.save(subreddit);

        verify(subredditRepository, times(1)).save(subreddit);
    }

    @Test
    void testIsSubredditNameTakenByUser() {
        Subreddit subreddit = new Subreddit();
        subreddit.setName("testSubreddit");
        User user = new User();
        subreddit.setUser(user);

        when(subredditRepository.existsSubredditByNameAndUser(subreddit.getName(), subreddit.getUser())).thenReturn(true);

        boolean isTaken = subredditService.isSubredditNameTakenByUser(subreddit);

        assertTrue(isTaken);
        verify(subredditRepository).existsSubredditByNameAndUser(subreddit.getName(), subreddit.getUser());
    }
}
