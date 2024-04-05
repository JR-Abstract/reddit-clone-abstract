package ua.com.javarush.oleksandr.reddit.redditcloneabstract.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto.VoteDto;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Vote;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.enums.VoteEntityType;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.CommentRepository;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.PostRepository;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.UserRepository;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository.VoteRepository;

import static ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.enums.VoteType.DOWNVOTE;
import static ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.enums.VoteType.UPVOTE;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void vote(VoteDto voteDto) {
        var voteResult = voteDto.getVoteEntityType().equals(VoteEntityType.POST) ?
                voteRepository.findVoteByPostIdAndUserUserId(voteDto.getVoteEntityId(), voteDto.getUserId()) :
                voteRepository.findVoteByCommentIdAndUserUserId(voteDto.getVoteEntityId(), voteDto.getUserId());

        voteResult.ifPresentOrElse(
                (vote) -> handleExistingVote(vote, voteDto),
                () -> saveNewVote(voteDto, voteDto.getVoteType().equals(UPVOTE) ? 1 : -1)
        );
    }

    private void saveNewVote(VoteDto voteDto, int voteDelta) {
        Vote vote = switch (voteDto.getVoteEntityType()) {
            case POST -> Vote.with()
                    .voteType(voteDto.getVoteType())
                    .user(userRepository.getReferenceById(voteDto.getUserId()))
                    .post(postRepository.getReferenceById(voteDto.getVoteEntityId()))
                    .build();
            case COMMENT -> Vote.with()
                    .voteType(voteDto.getVoteType())
                    .user(userRepository.getReferenceById(voteDto.getUserId()))
                    .comment(commentRepository.getReferenceById(voteDto.getVoteEntityId()))
                    .build();
            default -> throw new IllegalArgumentException("Unknown vote entity type: " + voteDto.getVoteEntityType());
        };

        voteRepository.save(vote);
        updateEntityVotes(vote, voteDelta, voteDto.getVoteEntityType());
    }

    private void handleExistingVote(Vote vote, VoteDto voteDto) {
        var oldVote = vote.getVoteType();
        var newVote = voteDto.getVoteType();
        int voteDelta = oldVote.equals(UPVOTE) ? (newVote.equals(UPVOTE) ? -1 : -2) : (newVote.equals(DOWNVOTE) ? 1 : 2);
        updateEntityVotes(vote, voteDelta, voteDto.getVoteEntityType());
        voteRepository.delete(vote);
    }

    private void updateEntityVotes(Vote vote, int voteDelta, VoteEntityType voteEntityType) {
        switch (voteEntityType) {
            case POST -> {
                var post = vote.getPost();
                Integer postVotes = post.getVoteCount();
                int postVotesValue = (postVotes != null) ? postVotes.intValue() : 0;
                post.setVoteCount(postVotesValue + voteDelta);
            }
            case COMMENT -> {
                var comment = vote.getComment();
                Integer commentVotes = comment.getVoteCount();
                int commentVotesValue = (commentVotes != null) ? commentVotes.intValue() : 0;
                comment.setVoteCount(commentVotesValue + voteDelta);
            }
            default -> throw new IllegalArgumentException("Unknown vote entity type: " + voteEntityType);
        }
    }
}
