package ua.com.javarush.oleksandr.reddit.redditcloneabstract.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.CommentRequestDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.CommentResponseDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentRequestDTO commentRequestDTO) {
        commentService.save(commentRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<Page<CommentResponseDTO>> getAllCommentsForPost(@PathVariable Long postId, Pageable pageable) {
        Page<CommentResponseDTO> commentsPage = commentService.getAllCommentsForPost(postId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(commentsPage);
    }

    @GetMapping("/by-user/{userName}")
    public ResponseEntity<Page<CommentResponseDTO>> getAllCommentsForUser(@PathVariable String userName, Pageable pageable) {
        Page<CommentResponseDTO> commentsPage = commentService.getAllCommentsForUser(userName, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(commentsPage);
    }

}