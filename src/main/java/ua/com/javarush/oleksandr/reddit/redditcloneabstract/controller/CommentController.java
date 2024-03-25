package ua.com.javarush.oleksandr.reddit.redditcloneabstract.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Page<CommentDTO>> getAllCommentsForPost(@PathVariable Long postId, Pageable pageable) {
        Page<CommentDTO> commentsPage = commentService.getAllCommentsForPost(postId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(commentsPage);
    }

    @GetMapping("/by-user/{userName}")
    public ResponseEntity<Page<CommentDTO>> getAllCommentsForUser(@PathVariable String userName, Pageable pageable) {
        Page<CommentDTO> commentsPage = commentService.getAllCommentsForUser(userName, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(commentsPage);
    }

}