package ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.CommentRequestDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.CommentResponseDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Comment;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Post;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Subreddit;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.PostService;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.UserService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

    @Mock
    private UserService userService;

    @Mock
    private PostService postService;

    @InjectMocks
    private CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    private User user;
    private Subreddit subreddit;
    private Post post;
    private Comment comment;

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

        comment = Comment.with()
                .id(1L)
                .text("Test Comment")
                .createdDate(localDateTime)
                .user(user)
                .post(post)
                .build();
    }

    @Test
    void commentToCommentDTO() {
        CommentResponseDTO commentRequestDTO = commentMapper.map(comment);

        assertThat(commentRequestDTO).isNotNull();
        assertThat(commentRequestDTO.getUserName()).isEqualTo(comment.getUser().getUsername());
        assertThat(commentRequestDTO.getPostId()).isEqualTo(comment.getPost().getId());
    }

    @Test
    void commentDTOToComment() {
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO();
        commentRequestDTO.setText(comment.getText());
        commentRequestDTO.setUserName(user.getUsername());
        commentRequestDTO.setPostId(post.getId());

        when(userService.findUserByUsername(commentRequestDTO.getUserName())).thenReturn(user);
        when(postService.findById(commentRequestDTO.getPostId())).thenReturn(post);

        Comment mappedComment = commentMapper.map(commentRequestDTO);

        assertThat(mappedComment).isNotNull();
        assertThat(mappedComment.getUser()).isEqualTo(user);
        assertThat(mappedComment.getPost()).isEqualTo(post);
        assertThat(mappedComment.getText()).isEqualTo(commentRequestDTO.getText());
    }
}
