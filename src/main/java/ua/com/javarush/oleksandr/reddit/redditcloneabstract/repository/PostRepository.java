package ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Post;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Collection<Post> findByUser_Username(String username);

    Integer countAllBySubreddit_Id(Long id);

    Optional<Post> findFirstByUrl(String url);
}
