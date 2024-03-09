package ua.com.javarush.oleksandr.reddit.redditcloneabstract.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.SubredditRequestDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.SubredditResponseDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.SubredditCreateException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper.SubredditMapper;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Subreddit;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.BindingResultService;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.SubredditService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/subreddit")
@RequiredArgsConstructor
@Slf4j
public class SubredditController {

    private final SubredditService subredditService;
    private final SubredditMapper subredditMapper;
    private final BindingResultService bindingResultService;
    private final MessageSource messageSource;

    @PostMapping
    public ResponseEntity<Void> createSubreddit(@RequestBody @Valid SubredditRequestDTO subredditRequestDTO,
                                                BindingResult bindingResult) {

        bindingResultService.handle(bindingResult, SubredditCreateException::new);

        log.debug(messageSource.getMessage("log.subreddit.create", new Object[]{subredditRequestDTO.getName()}, LocaleContextHolder.getLocale()));

        subredditService.save(subredditMapper.toEntity(subredditRequestDTO));

        log.info(messageSource.getMessage("log.subreddit.created", new Object[]{subredditRequestDTO.getName()}, LocaleContextHolder.getLocale()));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<SubredditResponseDTO>> getAllSubreddits() {

        log.debug(messageSource.getMessage("log.subreddit.fetchAll", null, LocaleContextHolder.getLocale()));

        var subredditResponseDtoList = subredditService.findAll()
                .stream()
                .map(subredditMapper::toDto)
                .toList();

        log.info(messageSource.getMessage("log.subreddit.fetchAll.count", new Object[]{subredditResponseDtoList.size()}, LocaleContextHolder.getLocale()));

        return ResponseEntity.status(HttpStatus.OK).body(subredditResponseDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSubreddit(@PathVariable Long id) {

        log.debug(messageSource.getMessage("log.subreddit.fetch", new Object[]{id}, LocaleContextHolder.getLocale()));

        Optional<Subreddit> subredditResult = subredditService.findById(id);

        if (subredditResult.isEmpty()) {
            log.error(messageSource.getMessage("log.subreddit.notFound", new Object[]{id}, LocaleContextHolder.getLocale()));

        } else log.info(messageSource.getMessage("log.subreddit.fetched", new Object[]{id}, LocaleContextHolder.getLocale()));

        return ResponseEntity.of(subredditResult.map(subredditMapper::toDto));
    }
}