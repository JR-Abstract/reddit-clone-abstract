package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.CommentRequestDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.CommentResponseDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper.CommentMapper;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Comment;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.CommentRepository;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
    private CommentRequestDTO commentRequestDTO;
    private CommentResponseDTO commentResponseDTO;

    @BeforeEach
    void setUp() {
        // Initialize your objects here
        comment = new Comment();
        comment.setId(1L);
        comment.setText("This is a test comment");

        commentRequestDTO = new CommentRequestDTO();
        commentRequestDTO.setText("This is a test comment DTO");

        commentResponseDTO = new CommentResponseDTO();
        commentResponseDTO.setId(1L);
        commentResponseDTO.setText("This is a test comment DTO");

    }

    @Test
    void save() {
        when(commentMapper.map(any(CommentRequestDTO.class))).thenReturn(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        commentService.save(commentRequestDTO);

        verify(commentMapper, times(1)).map(any(CommentRequestDTO.class));
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void getAllCommentsForPost() {
        List<Comment> comments = Collections.singletonList(comment);
        when(commentRepository.findByPostId(anyLong())).thenReturn(comments);
        when(commentMapper.mapList(anyList())).thenReturn(Collections.singletonList(commentResponseDTO));

        List<CommentResponseDTO> fetchedComments = commentService.getAllCommentsForPost(1L);

        verify(commentRepository, times(1)).findByPostId(anyLong());
        verify(commentMapper, times(1)).mapList(anyList());
        assertThat(fetchedComments).hasSize(1);
        assertThat(fetchedComments.get(0)).isEqualTo(commentResponseDTO);
    }

    @Test
    void getAllCommentsForUser() {
        List<Comment> comments = Collections.singletonList(comment);
        when(commentRepository.findByUserUsername(anyString())).thenReturn(comments);
        when(commentMapper.mapList(anyList())).thenReturn(Collections.singletonList(commentResponseDTO));

        List<CommentResponseDTO> fetchedComments = commentService.getAllCommentsForUser("user");

        verify(commentRepository, times(1)).findByUserUsername(anyString());
        verify(commentMapper, times(1)).mapList(anyList());
        assertThat(fetchedComments).hasSize(1);
        assertThat(fetchedComments.get(0)).isEqualTo(commentResponseDTO);
    }
}
