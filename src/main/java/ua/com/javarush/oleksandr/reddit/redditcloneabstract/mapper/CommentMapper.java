package ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.CommentRequestDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.CommentResponseDTO;
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
    public abstract CommentResponseDTO map(Comment comment);

    @InheritInverseConfiguration
    @Mapping(target = "user", source = "userName", qualifiedByName = "getUserByUsername")
    @Mapping(target = "post", source = "postId", qualifiedByName = "getPostById")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    public abstract Comment map(CommentRequestDTO commentRequestDTO);

    public abstract  List<CommentResponseDTO> mapList(List<Comment> comments);
    public abstract  List<Comment> mapListInverse(List<CommentRequestDTO> commentRequestDTOS);

    @Named(value = "getPostById")
    protected Post getPostById(Long id) {
        return postService.findById(id);
    }

    @Named(value = "getUserByUsername")
    protected User getUserByUsername(String username) {
        return userService.findUserByUsername(username);
    }
}