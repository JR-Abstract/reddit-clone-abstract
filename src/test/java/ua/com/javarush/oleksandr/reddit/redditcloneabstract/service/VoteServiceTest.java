package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.VoteDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Post;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Vote;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.enums.VoteType;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.PostRepository;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.UserRepository;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.VoteRepository;

import static org.mockito.Mockito.*;

public class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private VoteService voteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testVote() {
        // Arrange
        Long userId = 1L;
        Long postId = 2L;
        VoteType voteType = VoteType.UPVOTE;

        VoteDTO voteDto = new VoteDTO();
        voteDto.setUserId(userId);
        voteDto.setPostId(postId);
        voteDto.setVoteType(voteType);

        User user = new User();
        user.setUserId(userId);

        Post post = new Post();
        post.setId(postId);

        when(userRepository.getById(userId)).thenReturn(user);
        when(postRepository.getById(postId)).thenReturn(post);
        when(voteRepository.existsByUserUserIdAndPostId(userId, postId)).thenReturn(false);

        voteService.vote(voteDto);

        verify(voteRepository, times(1)).save(any(Vote.class));
    }
}
