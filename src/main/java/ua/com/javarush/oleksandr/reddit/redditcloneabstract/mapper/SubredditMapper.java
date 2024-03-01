package ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.SubredditRequestDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.SubredditResponseDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Subreddit;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.PostService;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.UserService;

@Mapper(componentModel = "spring")
public abstract class SubredditMapper {

    private PostService postService;
    private UserService userService;

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "id", target = "numberOfPosts", qualifiedByName = "getCountSubredditPosts")
    abstract public SubredditResponseDTO subredditToSubredditResponseDto(Subreddit subreddit);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(source = "username", target = "user", qualifiedByName = "getUserByUsername") // TODO: delete this field when was implemented substitution user in subreddit
    abstract public Subreddit subredditRequestDtoToSubreddit(SubredditRequestDTO subredditRequestDTO);

    @Named("getCountSubredditPosts")
    protected Integer getCountSubredditPosts(Long id) {
        return postService.countAllBySubreddit_Id(id);
    }

    @Named("getUserByUsername")
    protected User getUserByUsername(String username) {
        return userService.findUserByUsername(username);
    }

    @Autowired
    protected void setPostService(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }
}
