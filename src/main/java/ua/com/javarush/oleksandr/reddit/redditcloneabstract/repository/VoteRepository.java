package ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Post;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Vote;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
   Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
   boolean existsByUserUserIdAndPostId(Long userId, Long postId);

}
