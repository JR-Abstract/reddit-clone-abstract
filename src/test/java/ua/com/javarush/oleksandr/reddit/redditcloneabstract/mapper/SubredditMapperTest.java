package ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.SubredditRequestDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.SubredditResponseDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Subreddit;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.PostService;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class SubredditMapperTest {

    @MockBean
    private PostService postService;

    @MockBean
    private UserService userService;

    @Autowired
    private SubredditMapper subredditMapper;



    @Test
    void subredditToSubredditDto_PostsCountMatching() {
        when(postService.countAllBySubreddit_Id(anyLong())).thenReturn(3);

        Subreddit subreddit = Subreddit.with()
                .id(1L)
                .name("Subreddit name")
                .description("Subreddit description")
                .build();

        SubredditResponseDTO subredditResponseDTO = subredditMapper.toDto(subreddit);

        assertThat(subredditResponseDTO.getId()).isEqualTo(subreddit.getId());
        assertThat(subredditResponseDTO.getName()).isEqualTo(subreddit.getName());
        assertThat(subredditResponseDTO.getDescription()).isEqualTo(subreddit.getDescription());
        assertThat(subredditResponseDTO.getNumberOfPosts()).isEqualTo(3);
    }

    @Test
    void subredditToSubredditDto_NullId_ZeroPosts() {
        Subreddit subreddit = Subreddit.with()
                .id(null)
                .build();

        SubredditResponseDTO subredditResponseDTO = subredditMapper.toDto(subreddit);

        assertThat(subredditResponseDTO.getNumberOfPosts()).isEqualTo(0);
    }

    @Test
    void subredditToSubredditDto_NullSubreddit_NullResponse() {

        SubredditResponseDTO subredditResponseDTO = subredditMapper.toDto(null);

        assertThat(subredditResponseDTO).isNull();
    }

    @Test
    void subredditDtoToSubreddit_Creation() {

        String username = "username";

        SubredditRequestDTO subredditRequestDTO = SubredditRequestDTO.with()
                .name("Subreddit DTO name")
                .description("Subreddit DTO description")
                .username(username)
                .build();

        User user = mock(User.class);
        when(userService.findUserByUsername(username)).thenReturn(user);

        Subreddit subreddit = subredditMapper.toEntity(subredditRequestDTO);

        assertThat(subreddit.getId()).isNull();
        assertThat(subreddit.getName()).isEqualTo(subredditRequestDTO.getName());
        assertThat(subreddit.getDescription()).isEqualTo(subredditRequestDTO.getDescription());
        assertThat(subreddit.getUser()).isEqualTo(user);
        assertThat(subreddit.getCreatedDate()).isNull();
    }

    @Test
    void subredditDtoToSubreddit_NullDto_NullSubreddit() {

        Subreddit subreddit = subredditMapper.toEntity(null);

        assertThat(subreddit).isNull();
    }
}