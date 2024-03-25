package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.CommentDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper.CommentMapper;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Comment;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.CommentRepository;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public void save(CommentDTO commentsDTO) {
        Comment comment = commentMapper.commentDTOtoComment(commentsDTO);
        commentRepository.save(comment);
    }

    public Page<CommentDTO> getAllCommentsForPost(Long postId, Pageable pageable) {
        Page<Comment> commentsPage = commentRepository.findByPostIdOrderByVoteCountDesc(postId, pageable);
        return commentsPage.map(commentMapper::commentToCommentDTO);
    }

    public Page<CommentDTO> getAllCommentsForUser(String userName, Pageable pageable) {
        Page<Comment> commentsPage = commentRepository.findByUserUsernameOrderByVoteCountDesc(userName, pageable);
        return commentsPage.map(commentMapper::commentToCommentDTO);
    }
}
