package ua.com.javarush.oleksandr.reddit.redditcloneabstract.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.SubredditResponseDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.mapper.SubredditMapper;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Subreddit;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final SubredditMapper subredditMapper;

    @Transactional
    @GetMapping("/subscriptions")
    public ResponseEntity<?> getSubscriptions() {
        List<Subreddit> subscriptions = userService.getSubscriptionsCurrentUser();
        List<SubredditResponseDTO> subscriptionsDtos = subredditMapper.toDtoList(subscriptions);
        return ResponseEntity.status(HttpStatus.OK).body(subscriptionsDtos);
    }

}