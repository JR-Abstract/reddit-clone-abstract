package ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.CommentDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.PostNotFoundException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Comment;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Post;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.PostService;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.UserService;

import java.util.List;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public abstract class CommentMapper {
    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Mapping(source = "user.username", target = "userName")
    @Mapping(source = "post.id", target = "postId")
    public abstract  CommentDTO commentToCommentDTO(Comment comment);

    @InheritInverseConfiguration
    @Mapping(target = "user", source = "userName", qualifiedByName = "getUserByUsername")
    @Mapping(target = "post", source = "postId", qualifiedByName = "getPostById")
    public abstract Comment commentDTOtoComment(CommentDTO commentDTO);

    public abstract  List<CommentDTO> commentListToCommentDTOList(List<Comment> comments);
    public abstract  List<Comment> commentDTOListToCommentList(List<CommentDTO> commentDTOS);

    @Named(value = "getPostById")
    protected Post getPostById(Long id) {
        return postService.findById(id).orElseThrow(PostNotFoundException::new);
    }

    @Named(value = "getUserByUsername")
    protected User getUserByUsername(String username) {
        return userService.findUserByUsername(username);
    }
}