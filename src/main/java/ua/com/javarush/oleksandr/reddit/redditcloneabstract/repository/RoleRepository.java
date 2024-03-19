package ua.com.javarush.oleksandr.reddit.redditcloneabstract.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> getReferenceByName(@NotNull String name);
}
