package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.CommentRequestDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.CommentResponseDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper.CommentMapper;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Comment;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Post;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Subreddit;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.CommentRepository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;

    private Subreddit subreddit;
    private User user;
    private Post post;
    private Comment comment;
    private CommentRequestDTO commentRequestDTO;
    private CommentResponseDTO commentResponseDTO;
    private Pageable pageable;

    private List<Comment> commentList;
    private Page<Comment> commentsPage;

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
                .createdDate(localDateTime)
                .text("First comment to the post")
                .voteCount(1)
                .post(post)
                .user(user)
                .build();

        commentRequestDTO = CommentRequestDTO.with()
                .postId(1L)
                .text("This is a test comment")
                .userName("user")
                .build();

        pageable = PageRequest.of(0, 10, Sort.by("voteCount").descending());

        commentList = List.of(comment);

        commentsPage = new PageImpl<>(commentList, pageable, commentList.size());

    }

    @Test
    @DisplayName("Should save comment successfully")
    void shouldSaveCommentSuccessfully() {
        // When
        when(commentMapper.map(commentRequestDTO)).thenReturn(comment);
        commentService.save(commentRequestDTO);

        // Then
        verify(commentRepository).save(comment);
    }

}