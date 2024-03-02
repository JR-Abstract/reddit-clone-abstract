package ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.PostRequestDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.PostResponseDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Post;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Subreddit;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.SubredditService;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.UserService;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    private SubredditService subredditService;
    private UserService userService;

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "postName", target = "postName")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "url", target = "url")
    @Mapping(source = "subredditId", target = "subreddit", qualifiedByName = "findSubredditById")
    @Mapping(source = "userId", target = "user", qualifiedByName = "findUserById")
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "voteCount", ignore = true)
    abstract public Post dtoToPost(PostRequestDTO dto);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "postName", target = "postName")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "url", target = "url")
    @Mapping(source = "voteCount", target = "voteCount")
    @Mapping(target = "subredditName", expression = "java(post.getSubreddit().getName())")
    @Mapping(target = "userName", expression = "java(post.getUser().getUsername())")
    abstract public PostResponseDTO postToDTO(Post post);

    @Named("findSubredditById")
    protected Subreddit findSubredditById(Long id) {
        return subredditService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subreddit not found"));
    }

    @Named("findUserById")
    protected User findUserById(Long id) {
        return userService.findUserById(id);
    }

    @Autowired
    protected void setPostService(SubredditService subredditService, UserService userService) {
        this.subredditService = subredditService;
        this.userService = userService;
    }
}
