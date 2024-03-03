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

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Mapping(target = "numberOfPosts", source = "id", qualifiedByName = "getCountSubredditPosts")
    public abstract SubredditResponseDTO subredditToSubredditResponseDto(Subreddit subreddit);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "user", source = "username", qualifiedByName = "getUserByUsername")
    public abstract Subreddit subredditRequestDtoToSubreddit(SubredditRequestDTO subredditRequestDTO);

    @Named(value = "getCountSubredditPosts")
    protected Integer getCountSubredditPosts(Long id) {
        return postService.countAllBySubreddit_Id(id);
    }

    @Named(value = "getUserByUsername")
    protected User getUserByUsername(String username) {
        return userService.findUserByUsername(username);
    }
}



