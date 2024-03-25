package ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.PostRequestDto;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.PostResponseDto;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Post;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Subreddit;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.SubredditService;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostMapperTest {

    @Mock
    private SubredditService subredditService;

    @Mock
    private UserService userService;

    @InjectMocks
    private final PostMapper postMapper = Mappers.getMapper(PostMapper.class);

    private Subreddit newSubreddit;

    private User newUser;

    private PostRequestDto postDto;

    @BeforeEach
    void setUp() {

        newSubreddit = Subreddit.with().id(7L).name("Test subreddit").build();
        newUser = User.with().userId(5L).username("TestUser").build();
        postDto = PostRequestDto.with()
                .id(1L)
                .postName("TestPost")
                .description("Post description")
                .url("https://localhost:8080/1")
                .subredditId(7L)
                .userId(5L)
                .build();
    }

    @Test
    void shouldMapDtoToPostCorrectly() {

        when(subredditService.findById(7L)).thenReturn(Optional.of(newSubreddit));
        when(userService.findUserById(5L)).thenReturn(newUser);

        var post = postMapper.dtoToPost(postDto);

        assertPostRequestDtoMatchesPost(postDto, post);
    }

    @Test
    void shouldMapPostToPostResponseDtoSuccessfully() {

        var post = createPost();

        var postDto = postMapper.postToDTO(post);

        assertPostMatchesPostResponseDto(postDto, post);
    }

    @Test
    void whenDtoToPostSubredditNotFoundShouldThrowsException() {

        Long nonExistSubredditId = 999L;
        when(subredditService.findById(nonExistSubredditId)).thenReturn(Optional.empty());

        postDto.setSubredditId(nonExistSubredditId);

        assertThatThrownBy(() -> postMapper.dtoToPost(postDto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Subreddit not found");
    }

    private Post createPost() {
        return Post.with()
                .id(1L)
                .postName("TestPost")
                .description("Post description")
                .url("https://localhost:8080/1")
                .subreddit(newSubreddit)
                .user(newUser)
                .build();
    }

    private void assertPostMatchesPostResponseDto(PostResponseDto postDto, Post post) {

        assertThat(postDto)
                .extracting(
                        PostResponseDto::getPostName,
                        PostResponseDto::getDescription,
                        PostResponseDto::getUrl,
                        PostResponseDto::getUserName,
                        PostResponseDto::getSubredditName)
                .containsExactly(
                        post.getPostName(),
                        post.getDescription(),
                        post.getUrl(),
                        post.getUser().getUsername(),
                        post.getSubreddit().getName());
    }


    private void assertPostRequestDtoMatchesPost(PostRequestDto postDto, Post post) {

        assertThat(postDto)
                .extracting(
                        PostRequestDto::getPostName,
                        PostRequestDto::getDescription,
                        PostRequestDto::getUrl,
                        PostRequestDto::getUserId,
                        PostRequestDto::getSubredditId)
                .containsExactly(
                        post.getPostName(),
                        post.getDescription(),
                        post.getUrl(),
                        post.getUser().getUserId(),
                        post.getSubreddit().getId());
    }

}
