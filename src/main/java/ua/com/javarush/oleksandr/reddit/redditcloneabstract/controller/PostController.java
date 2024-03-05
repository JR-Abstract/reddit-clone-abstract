package ua.com.javarush.oleksandr.reddit.redditcloneabstract.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.PostRequestDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.PostResponseDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper.PostMapper;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Post;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.PostService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody @Valid PostRequestDTO postRequestDTO,
                                        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return handleErrorsFromBindingResult(bindingResult);
        }

        var post = postMapper.dtoToPost(postRequestDTO);
        postService.save(post);

        URI location = buildPostLocationUri(post.getId());

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {

        List<PostResponseDTO> postResponseDtos = postService.findAll().stream()
                .map(postMapper::postToDTO)
                .toList();

        return ResponseEntity.ok(postResponseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable Long id) {

        Optional<Post> foundPost = postService.findById(id);

        var foundPostDto = foundPost.map(postMapper::postToDTO)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Post with id = %d not found", id)
                ));

        return ResponseEntity.ok(foundPostDto);
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByUsername(@PathVariable String username) {

        var posts = postService.findPostsByUsername(username).stream()
                .map(postMapper::postToDTO)
                .toList();

        return ResponseEntity.ok(posts);
    }

    private URI buildPostLocationUri(Long postId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(postId)
                .toUri();
    }

    private ResponseEntity<Map<String, Object>> handleErrorsFromBindingResult(BindingResult bindingResult) {

        List<String> errorMessages = bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        Map<String, Object> errors = new HashMap<>();
        errors.put("timestamp", LocalDateTime.now());
        errors.put("status", HttpStatus.BAD_REQUEST.value());
        errors.put("errors", errorMessages);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}