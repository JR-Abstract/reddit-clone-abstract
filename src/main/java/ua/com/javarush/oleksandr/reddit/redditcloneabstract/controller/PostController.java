package ua.com.javarush.oleksandr.reddit.redditcloneabstract.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.PostRequestDto;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.PostResponseDto;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.PostCreationException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper.PostMapper;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Post;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.BindingResultService;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.PostService;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.validator.PostRequestValidator;

import java.net.URI;
import java.util.Collection;
import java.util.List;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final PostRequestValidator postRequestValidator;
    private final BindingResultService bindingResultService;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody @Valid PostRequestDto postRequestDto,
                                        BindingResult bindingResult) {

        postRequestValidator.validate(postRequestDto, bindingResult);
        bindingResultService.handle(bindingResult, PostCreationException::new);

        var post = postMapper.dtoToPost(postRequestDto);
        postService.save(post);

        URI location = buildPostLocationUri(post.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).build();
    }

    @GetMapping
    public ResponseEntity<?> getAllPosts(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                         @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                         @RequestParam(value = "field", required = false) String field,
                                         @RequestParam(value = "top", required = false) boolean top) {
        Collection<Post> posts;

        if (nonNull(pageNumber) && nonNull(pageSize)) {
            if (nonNull(field))
                posts = postService.findAllByPagingAndSort(pageNumber, pageSize, field, top);
            else
                posts = postService.findAllByPaging(pageNumber, pageSize);
        } else
            posts = postService.findAll();

        List<PostResponseDto> postResponseDtoList = posts.stream()
                .map(postMapper::postToDTO)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(postResponseDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {
        Post post = postService.findById(id);

        return ResponseEntity.ok(postMapper.postToDTO(post));
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<?> getPostsByUsername(@PathVariable String username) {

        var posts = postService.findPostsByUsername(username).stream()
                .map(postMapper::postToDTO)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    private URI buildPostLocationUri(Long postId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(postId)
                .toUri();
    }
}