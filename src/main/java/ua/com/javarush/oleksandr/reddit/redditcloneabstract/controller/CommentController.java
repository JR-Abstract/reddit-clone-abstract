package ua.com.javarush.oleksandr.reddit.redditcloneabstract.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.CommentDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentDTO commentDTO) {
        commentService.save(commentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsForPost(@PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentsForPost(postId));
    }

    @GetMapping("/by-user/{userName}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsForUser(@PathVariable String userName) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentsForUser(userName));
    }
}