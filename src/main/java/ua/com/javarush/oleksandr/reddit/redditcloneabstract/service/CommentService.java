package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.CommentDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper.CommentMapper;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Comment;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Post;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.CommentRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserService userService;
    private final PostService postService;

    @Transactional
    public void save(CommentDTO commentsDTO) {
        User user = userService.findByUsername(commentsDTO.getUserName());
        Post post = postService.findById(commentsDTO.getPostId());
        Comment comment = commentMapper.commentDTOtoComment(commentsDTO);
        comment.setUser(user);
        comment.setPost(post);
        commentRepository.save(comment);
    }

    public List<CommentDTO> getAllCommentsForPost(Long postId) {
        List<Comment> commentList = commentRepository.findByPostId(postId);
        return commentMapper.commentListToCommentDTOList(commentList);
    }

    public List<CommentDTO> getAllCommentsForUser(String userName) {
        List<Comment> commentList = commentRepository.findByUserUsername(userName);
        return commentMapper.commentListToCommentDTOList(commentList);
    }
}