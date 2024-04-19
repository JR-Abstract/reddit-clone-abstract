package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Post;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Subreddit;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.PostRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    private Subreddit subreddit;
    private User user;
    private Post post;
    private List<Post> posts;

    @BeforeEach
    void setUp() {
        LocalDateTime localDateTime = LocalDateTime.of(2024, 3, 7, 15, 30);
        ZoneId zoneId = ZoneId.of("Europe/London");
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zoneId);

        user = User.with()
                .userId(1L)
                .username("user")
                .email("test@mail.com")
                .password("password")
                .enabled(true)
                .created_at(zonedDateTime)
                .build();

        subreddit = Subreddit.with()
                .id(1L)
                .name("Test Subreddit")
                .description("Test Description")
                .user(user)
                .createdDate(zonedDateTime)
                .build();

        post = Post.with()
                .id(1L)
                .postName("Test Post")
                .description("Test Description")
                .url("http://test.com")
                .createdDate(localDateTime)
                .user(user)
                .subreddit(subreddit)
                .voteCount(0)
                .build();

        posts = new ArrayList<>() {{
            add(post);
            add(post.toBuilder()
                    .id(2L)
                    .voteCount(10)
                    .build());
        }};
    }

    @Test
    @DisplayName("Save post successfully")
    void save() {
        postService.save(post);
        verify(postRepository).save(post);
    }

    @Test
    @DisplayName("Find post by ID successfully")
    void findById_Success() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Post foundPost = postService.findById(1L);
        assertEquals(post, foundPost);
    }

    @Test
    @DisplayName("Fail to find post by non-existing ID")
    void findById_NotFound() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> postService.findById(1L));
    }

    @Test
    @DisplayName("Retrieve all posts successfully")
    void findAll() {
        when(postRepository.findAll()).thenReturn(Collections.singletonList(post));
        Collection<Post> posts = postService.findAll();
        assertFalse(posts.isEmpty());
        assertEquals(1, posts.size());
        assertTrue(posts.contains(post));
    }

    @Test
    @DisplayName("Find all posts by paging successfully")
    void findAllByPaging() {
        Integer pageNumber = 0;
        Integer pageSize = 2;

        Page<Post> page = new PageImpl<>(posts);
        when(postRepository.findAll(any(Pageable.class))).thenReturn(page);
        Collection<Post> findPosts = postService.findAllByPaging(pageNumber, pageSize);

        assertFalse(findPosts.isEmpty());
        assertEquals(pageSize, findPosts.size());
        assertTrue(findPosts.containsAll(posts));
    }

    @Test
    @DisplayName("Find all posts by paging and sorting successfully")
    void findAllByPagingAndSorting() {
        Integer pageNumber = 0;
        Integer pageSize = 2;
        String field = "voteCount";
        Boolean top = false;

        Page<Post> page = new PageImpl<>(posts);
        when(postRepository.findAll(any(Pageable.class))).thenReturn(page);
        Collection<Post> findPosts = postService.findAllByPagingAndSort(pageNumber, pageSize, field, top);

        assertFalse(findPosts.isEmpty());
        assertEquals(pageSize, findPosts.size());
        assertTrue(findPosts.containsAll(posts));
        assertTrue(() -> posts.get(0).getVoteCount() <= posts.get(1).getVoteCount());
    }

    @Test
    @DisplayName("Find posts by username successfully")
    void findPostsByUsername() {
        when(postRepository.findByUser_Username("user")).thenReturn(Collections.singletonList(post));
        Collection<Post> posts = postService.findPostsByUsername("user");
        assertFalse(posts.isEmpty());
        assertEquals(1, posts.size());
        assertEquals("Test Post", posts.iterator().next().getPostName());
    }

    @Test
    @DisplayName("Count posts in subreddit successfully")
    void countAllBySubreddit_Id() {
        when(postRepository.countAllBySubreddit_Id(1L)).thenReturn(10);
        Integer count = postService.countAllBySubreddit_Id(1L);
        assertNotNull(count);
        assertEquals(10, count);
    }
}
