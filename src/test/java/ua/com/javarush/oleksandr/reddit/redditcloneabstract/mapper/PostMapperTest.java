package ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.PostRequest;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.PostResponse;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Post;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Subreddit;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.SubredditService;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.UserService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest
class PostMapperTest {

    @MockBean
    private SubredditService subredditService;

    @MockBean
    private UserService userService;

    @Autowired
    private PostMapper postMapper;

    private Subreddit newSubreddit;

    private User newUser;

    @BeforeEach
    void setUp() {

        newSubreddit = Subreddit.with()
                .id(7L)
                .name("Test subreddit")
                .build();

        newUser = User.with()
                .userId(5L)
                .username("TestUser")
                .build();
    }

    @Test
    void shouldMapDtoToPostCorrectly() {
        when(subredditService.findById(7L)).thenReturn(Optional.of(newSubreddit));
        when(userService.findUserById(5L)).thenReturn(newUser);

        var postDto = PostRequest.with()
                .id(1L)
                .postName("TestPost")
                .description("Post description")
                .url("https://localhost:8080/1")
                .subredditId(7L)
                .userId(5L)
                .build();

        var post = postMapper.dtoToPost(postDto);

        assertThat(post)
                .extracting(
                        Post::getId,
                        Post::getPostName,
                        Post::getDescription,
                        Post::getUrl)
                .containsExactly(
                        null,
                        postDto.getPostName(),
                        postDto.getDescription(),
                        postDto.getUrl()
                );
        assertThat(post.getSubreddit())
                .extracting(
                        Subreddit::getId,
                        Subreddit::getName)
                .containsExactly(
                        newSubreddit.getId(),
                        newSubreddit.getName()
                );
    }

    @Test
    void shouldMapPostToDtoSuccessfully() {

        var post = Post.with()
                .id(1L)
                .postName("TestPost")
                .description("Post description")
                .url("https://localhost:8080/1")
                .subreddit(newSubreddit)
                .user(newUser)
                .build();

        var postDto = postMapper.postToDTO(post);

        assertThat(postDto)
                .extracting(
                        PostResponse::getId,
                        PostResponse::getPostName,
                        PostResponse::getDescription,
                        PostResponse::getUrl,
                        PostResponse::getUserName,
                        PostResponse::getSubredditName)
                .containsExactly(
                        post.getId(),
                        post.getPostName(),
                        post.getDescription(),
                        post.getUrl(),
                        post.getUser().getUsername(),
                        post.getSubreddit().getName()
                );
    }

    @Test
    void whenDtoToPostSubredditNotFoundShouldThrowsException() {
        Long nonExistentSubredditId = 999L;
        when(subredditService.findById(nonExistentSubredditId)).thenReturn(Optional.empty());

        PostRequest postDto = new PostRequest();
        postDto.setSubredditId(nonExistentSubredditId);

        assertThatThrownBy(() -> postMapper.dtoToPost(postDto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Subreddit not found");
    }
}