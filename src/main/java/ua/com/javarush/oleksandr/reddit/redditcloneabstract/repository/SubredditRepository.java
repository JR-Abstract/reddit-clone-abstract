package ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Subreddit;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.User;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface SubredditRepository extends JpaRepository<Subreddit, Long> {
    boolean existsSubredditByNameAndUser(String name, User user);

    @Query("SELECT DISTINCT s FROM Subreddit s LEFT JOIN FETCH s.subscribers WHERE s.id = :id")
    Optional<Subreddit> findByIdWithSubscribers(Long id);

    @Query("SELECT DISTINCT s.subscribers FROM Subreddit s WHERE s.id = :id")
    Collection<User> findAllSubscribersById(Long id);

    @Query("SELECT COUNT(DISTINCT u) FROM Subreddit s JOIN s.subscribers u WHERE s.id = :id")
    long countSubscribersById(Long id);
}