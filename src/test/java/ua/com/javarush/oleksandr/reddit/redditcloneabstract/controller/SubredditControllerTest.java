package ua.com.javarush.oleksandr.reddit.redditcloneabstract.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.SubredditRequestDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.SubredditResponseDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.SubscriptionDto;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.UserResponseDto;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper.SubredditMapper;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper.UserMapper;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Subreddit;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.ErrorHandlerService;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.SubredditService;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubredditControllerTest {

    @Mock
    private SubredditService subredditService;

    @Mock
    private ErrorHandlerService errorHandlerService;

    @Mock
    private SubredditMapper subredditMapper = Mappers.getMapper(SubredditMapper.class);

    @Mock
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private SubredditController subredditController;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createSubreddit_whenCalled_verifiesMethods() {
        SubredditRequestDTO subredditRequestDTO = new SubredditRequestDTO();
        subredditRequestDTO.setName("Test Subreddit");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("Some message");

        subredditController.createSubreddit(subredditRequestDTO, bindingResult);

        verify(errorHandlerService, times(1)).handle(eq(bindingResult), any());
        verify(subredditService, times(1)).save(any());
        verify(messageSource, atLeastOnce()).getMessage(anyString(), any(), any(Locale.class));
    }

    @Test
    void getAllSubreddits_whenCalled_verifiesResponse() {
        Subreddit subreddit = new Subreddit();
        List<Subreddit> subredditList = List.of(subreddit);

        SubredditResponseDTO subredditResponseDTO = new SubredditResponseDTO();
        List<SubredditResponseDTO> subredditResponseDTOList = List.of(subredditResponseDTO);

        when(subredditService.findAll()).thenReturn(subredditList);
        when(subredditMapper.toDto(any(Subreddit.class))).thenReturn(subredditResponseDTO);

        ResponseEntity<List<SubredditResponseDTO>> response = subredditController.getAllSubreddits();

        verify(subredditService, times(1)).findAll();
        verify(subredditMapper, times(1)).toDto(any(Subreddit.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(subredditResponseDTOList, response.getBody());
    }

    @Test
    void getSubreddit_whenSubredditFound_returnsSubreddit() {
        Long subredditId = 1L;
        Subreddit subreddit = new Subreddit();
        SubredditResponseDTO subredditResponseDTO = new SubredditResponseDTO();

        when(subredditService.findById(subredditId)).thenReturn(Optional.of(subreddit));
        when(subredditMapper.toDto(subreddit)).thenReturn(subredditResponseDTO);

        ResponseEntity<?> response = subredditController.getSubreddit(subredditId);

        verify(subredditService, times(1)).findById(subredditId);
        verify(subredditMapper, times(1)).toDto(subreddit);

        assertInstanceOf(SubredditResponseDTO.class, response.getBody());
        assertEquals(subredditResponseDTO, response.getBody());
    }

    @Test
    void getSubreddit_whenSubredditNotFound_returnsNotFound() {
        Long subredditId = 1L;

        when(subredditService.findById(subredditId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = subredditController.getSubreddit(subredditId);

        verify(subredditService, times(1)).findById(subredditId);

        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    void subscribe_whenCalled_invokesServiceAndReturnsOk() {
        Long subredditId = 1L;
        Long userId = 2L;
        SubscriptionDto subscriptionDto = new SubscriptionDto(subredditId, userId);

        ResponseEntity<?> response = subredditController.subscribe(subscriptionDto);

        verify(subredditService, times(1)).subscribeUser(subredditId, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void unsubscribe_whenCalled_invokesServiceAndReturnsOk() {
        Long subredditId = 1L;
        Long userId = 2L;
        SubscriptionDto subscriptionDto = new SubscriptionDto(subredditId, userId);

        ResponseEntity<?> response = subredditController.unsubscribe(subscriptionDto);

        verify(subredditService, times(1)).unsubscribeUser(subredditId, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getSubredditSubscribers_whenCalled_returnsSubscribersList() {
        Long subredditId = 1L;
        User user = new User();
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUsername("TestUser");
        userResponseDto.setEmail("test@example.com");
        List<User> userList = List.of(user);
        List<UserResponseDto> userResponseDtoList = List.of(userResponseDto);

        when(subredditService.findAllSubscribersById(subredditId)).thenReturn(userList);
        when(userMapper.map(any(User.class))).thenReturn(userResponseDto);

        ResponseEntity<?> response = subredditController.getSubredditSubscribers(subredditId);

        verify(subredditService, times(1)).findAllSubscribersById(subredditId);
        verify(userMapper, times(userList.size())).map(any(User.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponseDtoList, response.getBody());
    }

    @Test
    void getCountSubredditSubscribers_whenCalled_returnsSubscribersCount() {
        Long subredditId = 1L;
        Long expectedSubscribersCount = 42L;

        when(subredditService.countSubscribersById(subredditId)).thenReturn(expectedSubscribersCount);

        ResponseEntity<?> response = subredditController.getCountSubredditSubscribers(subredditId);

        verify(subredditService, times(1)).countSubscribersById(subredditId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedSubscribersCount, response.getBody());
    }
}
