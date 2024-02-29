package ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.SubredditDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Subreddit;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.PostService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class SubredditMapperTest {

    @MockBean
    private PostService postService;

    @Autowired
    private SubredditMapper subredditMapper;


    @Test
    void subredditToSubredditDto() {
        when(postService.countAllBySubreddit_Id(anyLong())).thenReturn(3);

        Subreddit subreddit = Subreddit.with()
                .id(1L)
                .name("Subreddit name")
                .description("Subreddit description")
                .build();

        SubredditDTO subredditDTO = subredditMapper.subredditToSubredditDto(subreddit);

        assertThat(subredditDTO.getId()).isEqualTo(subreddit.getId());
        assertThat(subredditDTO.getName()).isEqualTo(subreddit.getName());
        assertThat(subredditDTO.getDescription()).isEqualTo(subreddit.getDescription());
        assertThat(subredditDTO.getNumberOfPosts()).isEqualTo(3);
    }

    @Test
    void subredditToSubredditDto_NullId_ZeroPosts() {
        Subreddit subreddit = Subreddit.with()
                .id(null)
                .build();

        SubredditDTO subredditDTO = subredditMapper.subredditToSubredditDto(subreddit);

        assertThat(subredditDTO.getNumberOfPosts()).isEqualTo(0);
    }

    @Test
    void nullSubredditToSubredditDto() {

        SubredditDTO subredditDTO = subredditMapper.subredditToSubredditDto(null);

        assertThat(subredditDTO).isNull();
    }

    @Test
    void subredditDtoToSubreddit() {

        SubredditDTO subredditDTO = SubredditDTO.with()
                .id(1L)
                .name("Subreddit DTO name")
                .description("Subreddit DTO description")
                .build();

        Subreddit subreddit = subredditMapper.subredditDtoToSubreddit(subredditDTO);

        assertThat(subreddit.getId()).isNull();
        assertThat(subreddit.getName()).isEqualTo(subredditDTO.getName());
        assertThat(subreddit.getDescription()).isEqualTo(subredditDTO.getDescription());
        assertThat(subreddit.getUser()).isNull();
        assertThat(subreddit.getCreatedDate()).isNull();
    }

    @Test
    void nullSubredditDtoToSubreddit() {

        Subreddit subreddit = subredditMapper.subredditDtoToSubreddit(null);

        assertThat(subreddit).isNull();
    }
}