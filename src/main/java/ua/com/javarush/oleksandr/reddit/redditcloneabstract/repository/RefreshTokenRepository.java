package ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    void deleteAllByUserEmail(String email);

    @Query("select rt from RefreshToken rt join fetch rt.user where rt.value = :token")
    Optional<RefreshToken> findByValue(String token);
}
