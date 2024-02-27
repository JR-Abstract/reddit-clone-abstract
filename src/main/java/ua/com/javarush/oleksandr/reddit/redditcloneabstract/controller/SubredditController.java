package ua.com.javarush.oleksandr.reddit.redditcloneabstract.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.SubredditDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Subreddit;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.SubredditService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/subreddit")
public class SubredditController {

    private final SubredditService subredditService;

    @Autowired
    public SubredditController(SubredditService subredditService) {
        this.subredditService = subredditService;
    }

    @PostMapping
    public ResponseEntity<SubredditDTO> createSubreddit(@RequestBody @Valid SubredditDTO subredditDTO,
                                                        BindingResult bindingResult) {

        // TODO: implement subredditValidator.validate(subredditDTO);
        if (bindingResult.hasErrors()) {
            // TODO: implement throw exception
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid subreddit data");
        }

        // TODO: implement mapper
        Subreddit subreddit = Subreddit.with()
                .name(subredditDTO.getName())
                .description(subredditDTO.getDescription())
                .build();

        subredditService.save(subreddit);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<SubredditDTO>> getAllSubreddits() {
        // TODO: implement mapper
        List<Subreddit> subreddits = subredditService.findAll();

        List<SubredditDTO> subredditDTOList = subreddits
                .stream()
                .map(subreddit -> SubredditDTO.with()
                        .name(subreddit.getName())
                        .description(subreddit.getDescription())
                        .id(subreddit.getId())
                        .build())
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(subredditDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDTO> getSubreddit(@PathVariable Long id) {
        // TODO: implement mapper
        Optional<Subreddit> subredditOptional = subredditService.findById(id);
        if (subredditOptional.isEmpty()) {
            // TODO: implement throw exception
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subreddit not found");
        }

        Subreddit subreddit = subredditOptional.get();
        SubredditDTO subredditDTO = SubredditDTO.with()
                .name(subreddit.getName())
                .description(subreddit.getDescription())
                .id(subreddit.getId())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(subredditDTO);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(ResponseStatusException exception) {
        // TODO: implement handling
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
    }
}