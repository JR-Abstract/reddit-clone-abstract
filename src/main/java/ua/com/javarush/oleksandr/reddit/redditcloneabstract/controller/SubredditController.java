package ua.com.javarush.oleksandr.reddit.redditcloneabstract.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.SubredditRequestDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.SubredditResponseDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper.SubredditMapper;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Subreddit;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.SubredditService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/subreddit")
@RequiredArgsConstructor
public class SubredditController {

    private final SubredditService subredditService;
    private final SubredditMapper subredditMapper;

    @PostMapping
    public ResponseEntity<Void> createSubreddit(@RequestBody @Valid SubredditRequestDTO subredditRequestDTO,
                                                               BindingResult bindingResult) {

        // TODO: implement subredditValidator.validate(subredditDTO);
        if (bindingResult.hasErrors()) {
            // TODO: implement throw exception
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid subreddit data");
        }

        Subreddit subreddit = subredditMapper.subredditRequestDtoToSubreddit(subredditRequestDTO);

        subredditService.save(subreddit);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<SubredditResponseDTO>> getAllSubreddits() {

        List<SubredditResponseDTO> subredditResponseDTOS = subredditService.findAll()
                .stream()
                .map(subredditMapper::subredditToSubredditResponseDto)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(subredditResponseDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditResponseDTO> getSubreddit(@PathVariable Long id) {

        Optional<Subreddit> subredditOptional = subredditService.findById(id);

        if (subredditOptional.isEmpty()) {
            // TODO: implement throw exception
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subreddit not found");
        }

        SubredditResponseDTO subredditResponseDTO = subredditMapper.subredditToSubredditResponseDto(subredditOptional.get());

        return ResponseEntity.status(HttpStatus.OK).body(subredditResponseDTO);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(ResponseStatusException exception) {
        // TODO: implement handling
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
    }
}