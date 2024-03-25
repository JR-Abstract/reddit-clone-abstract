package ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostId(Long postId);

    List<Comment> findByUserUsername(String userName);
    Page<Comment> findByPostIdOrderByVoteCountDesc(Long postId, Pageable pageable);

    Page<Comment> findByUserUsernameOrderByVoteCountDesc(String userName, Pageable pageable);
}