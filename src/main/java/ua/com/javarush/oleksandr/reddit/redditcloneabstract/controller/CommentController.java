package ua.com.javarush.oleksandr.reddit.redditcloneabstract.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.CommentDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.CommentCreationException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.BindingResultService;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.CommentService;

import java.util.List;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final BindingResultService bindingResultService;
    private final MessageSource messageSource;

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody @Valid CommentDTO commentDTO, BindingResult bindingResult) {
        bindingResultService.handle(bindingResult, CommentCreationException::new);

        log.debug(messageSource.getMessage("log.comment.create", new Object[]{commentDTO.getUserName()},
                LocaleContextHolder.getLocale()));

        commentService.save(commentDTO);

        log.info(messageSource.getMessage("log.comment.created", new Object[]{commentDTO.getUserName()},
                LocaleContextHolder.getLocale()));

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