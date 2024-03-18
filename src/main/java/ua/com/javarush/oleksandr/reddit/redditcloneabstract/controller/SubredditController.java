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
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.SubscriptionDto;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.SubredditCreateException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper.SubredditMapper;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper.UserMapper;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Subreddit;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.ErrorHandlerService;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.SubredditService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/subreddit")
@RequiredArgsConstructor
@Slf4j
public class SubredditController {

    private final UserMapper userMapper;
    private final SubredditService subredditService;
    private final SubredditMapper subredditMapper;
    private final ErrorHandlerService errorHandlerService;
    private final MessageSource messageSource;

    @PostMapping
    public ResponseEntity<Void> createSubreddit(@RequestBody @Valid SubredditRequestDTO subredditRequestDTO,
                                                BindingResult bindingResult) {

        errorHandlerService.handle(bindingResult, SubredditCreateException::new);

        log.debug(messageSource.getMessage("log.subreddit.create", new Object[]{subredditRequestDTO.getName()},
                LocaleContextHolder.getLocale()));

        subredditService.save(subredditMapper.toEntity(subredditRequestDTO));

        log.info(messageSource.getMessage("log.subreddit.created", new Object[]{subredditRequestDTO.getName()},
                LocaleContextHolder.getLocale()));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<SubredditResponseDTO>> getAllSubreddits() {

        log.debug(messageSource.getMessage("log.subreddit.fetchAll", null, LocaleContextHolder.getLocale()));

        var subredditResponseDtoList = subredditService.findAll()
                .stream()
                .map(subredditMapper::toDto)
                .toList();

        log.info(messageSource.getMessage("log.subreddit.fetchAll.count", new Object[]{subredditResponseDtoList.size()},
                LocaleContextHolder.getLocale()));

        return ResponseEntity.status(HttpStatus.OK).body(subredditResponseDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSubreddit(@PathVariable Long id) {

        log.debug(messageSource.getMessage("log.subreddit.fetch", new Object[]{id}, LocaleContextHolder.getLocale()));

        Optional<Subreddit> subredditResult = subredditService.findById(id);

        if (subredditResult.isEmpty()) {
            log.error(messageSource.getMessage("log.subreddit.notFound", new Object[]{id},
                    LocaleContextHolder.getLocale()));

        } else log.info(messageSource.getMessage("log.subreddit.fetched", new Object[]{id},
                LocaleContextHolder.getLocale()));

        return ResponseEntity.of(subredditResult.map(subredditMapper::toDto));
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestBody SubscriptionDto subscription) {

        Long subredditId = subscription.subredditId();
        Long userId = subscription.userId();

        subredditService.subscribeUser(subredditId, userId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<?> unsubscribe(@RequestBody SubscriptionDto subscription) {

        Long subredditId = subscription.subredditId();
        Long userId = subscription.userId();

        subredditService.unsubscribeUser(subredditId, userId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}/subscribers")
    public ResponseEntity<?> getSubredditSubscribers(@PathVariable Long id) {

        log.debug(messageSource.getMessage("log.subreddit.findAllSubscribersById",
                new Object[]{id},
                LocaleContextHolder.getLocale()));

        var subscribers = subredditService.findAllSubscribersById(id)
                .stream()
                .map(userMapper::map)
                .toList();

        log.info(messageSource.getMessage("log.subreddit.findAllSubscribersById.count",
                new Object[]{id, subscribers.size()},
                LocaleContextHolder.getLocale()));

        return ResponseEntity.status(HttpStatus.OK).body(subscribers);
    }

    @GetMapping("/{id}/subscribers/count")
    public ResponseEntity<?> getCountSubredditSubscribers(@PathVariable Long id) {

        log.debug(messageSource.getMessage("log.subreddit.getCountSubredditSubscribers",
                new Object[]{id},
                LocaleContextHolder.getLocale()));

        var subscribersCount = subredditService.countSubscribersById(id);

        log.info(messageSource.getMessage("log.subreddit.getCountSubredditSubscribers.count",
                new Object[]{id, subscribersCount},
                LocaleContextHolder.getLocale()));

        return ResponseEntity.status(HttpStatus.OK).body(subscribersCount);
    }
}