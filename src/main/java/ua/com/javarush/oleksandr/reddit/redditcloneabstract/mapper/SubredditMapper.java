package ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.SubredditDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Subreddit;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.PostService;

@Mapper(componentModel = "spring")
public abstract class SubredditMapper {

    private PostService postService;

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "id", target = "numberOfPosts", qualifiedByName = "getCountSubredditPosts")
    abstract public SubredditDTO subredditToSubredditDto(Subreddit subreddit);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "user", ignore = true)
    abstract public Subreddit subredditDtoToSubreddit(SubredditDTO subredditDTO);

    @Named("getCountSubredditPosts")
    protected Integer getCountSubredditPosts(Long id) {
        return postService.countAllBySubreddit_Id(id);
    }

    @Autowired
    protected void setPostService(PostService postService) {
        this.postService = postService;
    }
}
