package ua.com.javarush.oleksandr.reddit.redditcloneabstract.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.CommentRequestDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.CommentResponseDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.CommentCreationException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.BindingResultService;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.CommentService;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final BindingResultService bindingResultService;
    private final MessageSource messageSource;

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody @Valid CommentRequestDTO commentRequestDTO,
                                           BindingResult bindingResult) {
        bindingResultService.handle(bindingResult, CommentCreationException::new);

        log.debug(messageSource.getMessage("log.comment.create", new Object[]{commentRequestDTO.getUserName()},
                LocaleContextHolder.getLocale()));

        commentService.save(commentRequestDTO);

        log.info(messageSource.getMessage("log.comment.created", new Object[]{commentRequestDTO.getUserName()},
                LocaleContextHolder.getLocale()));

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