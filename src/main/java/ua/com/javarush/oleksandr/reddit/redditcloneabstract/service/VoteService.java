package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.VoteDto;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Vote;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.PostRepository;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.UserRepository;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.VoteRepository;

import java.util.Optional;

import static ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.enums.VoteType.DOWNVOTE;
import static ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.enums.VoteType.UPVOTE;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public void vote(VoteDto voteDto) {

        // todo: get userId from authorized user
        var voteResult = voteRepository.findVoteByPostIdAndUserUserId(voteDto.getPostId(), voteDto.getUserId());

        voteResult.ifPresentOrElse(
                (vote) -> handleExistingVote(vote, voteDto),
                () -> saveNewVote(voteDto, voteDto.getVoteType().equals(UPVOTE) ? 1 : -1));
    }

    private void saveNewVote(VoteDto voteDto, int voteDelta) {

        var vote = Vote.with()
                .voteType(voteDto.getVoteType())
                .user(userRepository.getReferenceById(voteDto.getUserId()))
                .post(postRepository.getReferenceById(voteDto.getPostId()))
                .build();

        voteRepository.save(vote);
        updatePostVotes(vote, voteDelta);
    }

    private void handleExistingVote(Vote vote, VoteDto voteDto) {

        var oldVote = vote.getVoteType();
        var newVote = voteDto.getVoteType();


        if (oldVote.equals(UPVOTE)) {

            final int voteDelta = -1;

            if (newVote.equals(UPVOTE)) {

                updatePostVotes(vote, voteDelta);

            } else saveNewVote(voteDto, voteDelta * 2);

        } else {

            final int voteDelta = 1;

            if (newVote.equals(DOWNVOTE)) {

                updatePostVotes(vote, voteDelta);

            } else saveNewVote(voteDto, voteDelta * 2);
        }

        voteRepository.delete(vote);
    }

    private void updatePostVotes(Vote vote, int voteDelta) {

        var post = vote.getPost();
        int postVotes = post.getVoteCount();
        post.setVoteCount(postVotes + voteDelta);
    }
}