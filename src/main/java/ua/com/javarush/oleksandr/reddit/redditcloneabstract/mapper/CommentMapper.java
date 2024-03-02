package ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.CommentDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Comment;

import java.util.List;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface CommentMapper {
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "text", target = "text")
    @Mapping(source = "user.userName", target = "userName")
    @Mapping(source = "post.id", target = "postId")
    CommentDTO commentToCommentDTO(Comment comment);

    @InheritInverseConfiguration
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "post", ignore = true)
    Comment commentDTOtoComment(CommentDTO commentDTO);

    List<CommentDTO> commentListToCommentDTOList(List<Comment> comments);
    List<Comment> commentDTOListToCommentList(List<CommentDTO> commentDTOS);
}