package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.VoteDto;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Post;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Vote;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.enums.VoteEntityType;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.enums.VoteType;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.PostRepository;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.UserRepository;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.VoteRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.enums.VoteType.DOWNVOTE;
import static ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.enums.VoteType.UPVOTE;

public class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private VoteService voteService;

    private AutoCloseable closeable;
    private Post testPost;

    @BeforeEach
    void setUp() {

        closeable = MockitoAnnotations.openMocks(this);

        var testUser = new User();
        testPost = Post.with().voteCount(0).build();

        when(userRepository.getReferenceById(anyLong())).thenReturn(testUser);
        when(postRepository.getReferenceById(anyLong())).thenReturn(testPost);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void shouldSaveNewUpvoteWhenNoExistingVote() {

        var voteDto = createVoteDto(UPVOTE);
        when(voteRepository.findVoteByPostIdAndUserUserId(anyLong(), anyLong())).thenReturn(Optional.empty());

        voteService.vote(voteDto);

        verify(voteRepository, times(1)).save(any(Vote.class));
        verify(postRepository, times(1)).getReferenceById(anyLong());

        int expectedVoteCount = 1;
        assertEquals(expectedVoteCount, testPost.getVoteCount());
    }

    @Test
    void shouldToggleVoteWhenExistingVoteIsDifferent() {

        var existingVote = new Vote();
        existingVote.setVoteType(UPVOTE);
        var voteDto = createVoteDto(DOWNVOTE);
        when(voteRepository.findVoteByPostIdAndUserUserId(anyLong(), anyLong()))
                .thenReturn(Optional.of(existingVote));

        voteService.vote(voteDto);
        verify(voteRepository, times(1)).delete(any(Vote.class));
        verify(voteRepository, times(1)).save(any(Vote.class));

        int expectedVoteCount = -2;
        assertEquals(expectedVoteCount, testPost.getVoteCount());
    }

    @Test
    void shouldRemoveVoteWhenExistingAndNewVoteAreTheSame() {

        var existingVote = new Vote();
        existingVote.setVoteType(UPVOTE);
        existingVote.setPost(testPost);

        var voteDto = createVoteDto(UPVOTE);

        when(voteRepository.findVoteByPostIdAndUserUserId(anyLong(), anyLong())).thenReturn(Optional.of(existingVote));

        voteService.vote(voteDto);

        verify(voteRepository, times(1)).delete(existingVote);
        verify(voteRepository, never()).save(any(Vote.class));

        int expectedVoteCount = -1;
        assertEquals(expectedVoteCount, testPost.getVoteCount());
    }

    @Test
    void shouldCorrectlyHandleDownvoteWhenNoExistingVote() {

        var voteDto = createVoteDto(DOWNVOTE);
        when(voteRepository.findVoteByPostIdAndUserUserId(anyLong(), anyLong())).thenReturn(Optional.empty());

        voteService.vote(voteDto);

        verify(voteRepository, times(1)).save(any(Vote.class));

        int expectedVoteCount = -1;
        assertEquals(expectedVoteCount, testPost.getVoteCount());
    }

    @Test
    void shouldThrowExceptionWhenPostDoesNotExist() {
        var voteDto = createVoteDto(UPVOTE);
        when(postRepository.getReferenceById(999L)).thenThrow(new EntityNotFoundException("Post not found"));
        assertThrows(EntityNotFoundException.class, () -> voteService.vote(voteDto));
    }

    private VoteDto createVoteDto(VoteType voteType) {
        var voteDto = new VoteDto();
        voteDto.setUserId(1L);
        voteDto.setVoteType(voteType);
        voteDto.setVoteEntityId(999L);
        voteDto.setVoteEntityType(VoteEntityType.POST);
        return voteDto;
    }

}
