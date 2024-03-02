package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.VoteDTO;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Vote;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.PostRepository;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.UserRepository;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.VoteRepository;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public void vote(VoteDTO voteDto) {
        boolean voteExists = voteRepository.existsByUserUserIdAndPostId(voteDto.getUserId(), voteDto.getPostId());
        if (voteExists) {
            return;
        }
        Vote vote = new Vote();
        vote.setVoteType(voteDto.getVoteType());
        vote.setUser(userRepository.getById(voteDto.getUserId()));
        vote.setPost(postRepository.getById(voteDto.getPostId()));
        voteRepository.save(vote);

    }
}