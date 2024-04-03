package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.CommentDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper.CommentMapper;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Comment;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.CommentRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;

    private Comment comment;
    private CommentDTO commentDTO;

    @BeforeEach
    void setUp() {
        // Initialize your objects here
        comment = new Comment();
        comment.setId(1L);
        comment.setText("This is a test comment");

        commentDTO = new CommentDTO();
        commentDTO.setId(1L);
        commentDTO.setText("This is a test comment DTO");
    }

    @Test
    void save() {
        when(commentMapper.commentDTOtoComment(any(CommentDTO.class))).thenReturn(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        commentService.save(commentDTO);

        verify(commentMapper, times(1)).commentDTOtoComment(any(CommentDTO.class));
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void getAllCommentsForPost() {
        // Arrange
        Comment comment = new Comment();
        CommentDTO commentDTO = new CommentDTO();

        List<Comment> comments = Collections.singletonList(comment);
        Page<Comment> commentsPage = new PageImpl<>(comments);
        when(commentRepository.findByPostIdOrderByVoteCountDesc(anyLong(), any())).thenReturn(commentsPage);
        when(commentMapper.commentToCommentDTO(comment)).thenReturn(commentDTO);

        Page<CommentDTO> fetchedComments = commentService.getAllCommentsForPost(1L, Pageable.ofSize(1));

        verify(commentRepository, times(1)).findByPostIdOrderByVoteCountDesc(anyLong(), any());
        verify(commentMapper, times(1)).commentToCommentDTO(comment);
        assertThat(fetchedComments).hasSize(1);
        assertThat(fetchedComments.getContent().get(0)).isEqualTo(commentDTO);
    }
    @Test
    void getAllCommentsForUser() {
        List<Comment> comments = Collections.singletonList(comment);
        Page<Comment> commentsPage = new PageImpl<>(comments);
        when(commentRepository.findByUserUsernameOrderByVoteCountDesc(anyString(), any())).thenReturn(commentsPage);
        when(commentMapper.commentToCommentDTO(any(Comment.class))).thenReturn(commentDTO);

        Page<CommentDTO> fetchedComments = commentService.getAllCommentsForUser("user", Pageable.unpaged());

        verify(commentRepository, times(1)).findByUserUsernameOrderByVoteCountDesc(anyString(), any());
        verify(commentMapper, times(1)).commentToCommentDTO(any(Comment.class));
        assertThat(fetchedComments).hasSize(1);
        assertThat(fetchedComments.getContent().get(0)).isEqualTo(commentDTO);
    }

}