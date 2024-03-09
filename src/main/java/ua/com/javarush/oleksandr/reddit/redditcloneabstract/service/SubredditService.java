package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Subreddit;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.SubredditRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final MessageSource messageSource;

    public List<Subreddit> findAll() {

        log.debug(messageSource.getMessage("log.subreddit.service.findAll.start", null, LocaleContextHolder.getLocale()));

        List<Subreddit> subreddits = subredditRepository.findAll();

        log.debug(messageSource.getMessage("log.subreddit.service.findAll.success", new Object[]{subreddits.size()}, LocaleContextHolder.getLocale()));

        return subreddits;
    }

    public Optional<Subreddit> findById(@NotNull Long id) {

        log.debug(messageSource.getMessage("log.subreddit.service.findById.start", new Object[]{id}, LocaleContextHolder.getLocale()));

        Optional<Subreddit> subredditResult = subredditRepository.findById(id);

        if (subredditResult.isPresent()) {
            log.debug(messageSource.getMessage("log.subreddit.service.findById.success", new Object[]{id}, LocaleContextHolder.getLocale()));
        } else {
            log.debug(messageSource.getMessage("log.subreddit.service.findById.failure", new Object[]{id}, LocaleContextHolder.getLocale()));
        }

        return subredditResult;
    }

    @Transactional
    public void save(Subreddit subreddit) {

        try {
            subredditRepository.save(subreddit);

            String successLog = messageSource.getMessage("log.subreddit.service.created",
                    new Object[]{subreddit.getName()}, LocaleContextHolder.getLocale());
            log.debug(successLog);

        } catch (Exception e) {
            String errorLog = messageSource.getMessage("log.subreddit.service.creationError",
                    new Object[]{subreddit.getName(), e.getMessage()}, LocaleContextHolder.getLocale());
            log.error(errorLog);

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    messageSource.getMessage("subreddit.creation.failure", null, LocaleContextHolder.getLocale()), e);
        }
    }
}