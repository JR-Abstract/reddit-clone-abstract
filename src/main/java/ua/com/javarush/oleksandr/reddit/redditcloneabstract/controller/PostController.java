package ua.com.javarush.oleksandr.reddit.redditcloneabstract.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.PostRequestDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.PostResponseDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Post;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Subreddit;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.PostService;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.SubredditService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final SubredditService subredditService;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody @Valid PostRequestDTO postRequestDTO,
                                        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return handleErrorsFromBindingResult(bindingResult);
        }

        var post = mapPostRequestDtoToPost(postRequestDTO);
        postService.save(post);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<PostResponseDTO> getAllPosts() {

        return postService.findAll().stream()
                .map(this::mapPostToPostResponseDto)
                .toList();
    }

    @GetMapping("/{id}")
    public PostResponseDTO getPost(@PathVariable Long id) {
        return mapPostToPostResponseDto(postService.findById(id));
    }

    @GetMapping("/by-user/{username}")
    public List<PostResponseDTO> getPostsByUsername(@PathVariable String username) {

        return postService.findPostsByUsername(username).stream()
                .map(this::mapPostToPostResponseDto)
                .toList();
    }

    private static ResponseEntity<Map<String, Object>> handleErrorsFromBindingResult(BindingResult bindingResult) {

        List<String> errorMessages = bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        Map<String, Object> errors = new HashMap<>();
        errors.put("timestamp", LocalDateTime.now());
        errors.put("status", HttpStatus.BAD_REQUEST.value());
        errors.put("errors", errorMessages);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    private Post mapPostRequestDtoToPost(PostRequestDTO postRequestDTO) {

        User currentUser = User.with()
                .userId(postRequestDTO.getUserId())
                .build();

        Subreddit subreddit = subredditService.findById(postRequestDTO.getSubredditId()).orElse(null);

        return Post.with()
                .id(postRequestDTO.getId())
                .user(currentUser)
                .subreddit(subreddit)
                .postName(postRequestDTO.getPostName())
                .url(postRequestDTO.getUrl())
                .description(postRequestDTO.getDescription())
                .build();
    }

    private PostResponseDTO mapPostToPostResponseDto(Post post) {

        return PostResponseDTO.with()
                .id(post.getId())
                .postName(post.getPostName())
                .description(post.getDescription())
                .url(post.getUrl())
                .voteCount(post.getVoteCount())
                .subredditName(post.getSubreddit().getName())
                .userName(post.getUser().getUsername())
                .build();
    }
}