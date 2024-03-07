package ua.com.javarush.oleksandr.reddit.redditcloneabstract.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.VoteDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.service.VoteService;

@RestController
public class VoteController {
    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/api/v1/votes")
    public ResponseEntity<Void> saveVote(@RequestBody VoteDTO voteDto) {
        voteService.vote(voteDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}