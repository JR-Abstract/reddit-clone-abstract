package ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.PostRequestDto;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.PostResponseDto;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Post;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Subreddit;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.SubredditService;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.UserService;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private SubredditService subredditService;

    @Autowired
    private UserService userService;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subreddit", source = "subredditId", qualifiedByName = "findSubredditById")
    @Mapping(target = "user", source = "userId", qualifiedByName = "findUserById")
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "voteCount", ignore = true)
    public abstract Post dtoToPost(PostRequestDto dto);

    @Mapping(target = "subredditName", expression = "java(post.getSubreddit().getName())")
    @Mapping(target = "userName", expression = "java(post.getUser().getUsername())")
    public abstract PostResponseDto postToDTO(Post post);

    @Named("findSubredditById")
    protected Subreddit findSubredditById(Long id) {
        return subredditService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subreddit not found"));
    }

    @Named("findUserById")
    protected User findUserById(Long id) {
        return userService.findUserById(id);
    }
}
