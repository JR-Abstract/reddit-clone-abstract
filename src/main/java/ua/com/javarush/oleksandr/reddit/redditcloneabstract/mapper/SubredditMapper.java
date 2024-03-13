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

    @Mapping(target = "numberOfPosts", source = "id", qualifiedByName = "countAllBySubreddit_Id")
    public abstract SubredditResponseDTO subredditToSubredditResponseDto(Subreddit subreddit);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "user", source = "userId", qualifiedByName = "findUserById")
    public abstract Subreddit subredditRequestDtoToSubreddit(SubredditRequestDTO subredditRequestDTO);

    @Named(value = "countAllBySubreddit_Id")
    protected Integer countAllBySubreddit_Id(Long id) {
        return postService.countAllBySubreddit_Id(id);
    }

    @Named(value = "findUserById")
    protected User findUserById(Long id) {
        return userService.findUserById(id);
    }
}



