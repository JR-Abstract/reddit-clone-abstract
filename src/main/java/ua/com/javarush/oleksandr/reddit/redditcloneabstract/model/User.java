package ua.com.javarush.oleksandr.reddit.redditcloneabstract.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"user\"",
        uniqueConstraints = @UniqueConstraint(name = "uq_user_email", columnNames = "email"))
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "with")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;

    @Email
    @NaturalId
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Username is mandatory")
    @Size(min = 2, max = 30, message = "Username must be between 2 and 30 characters")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @ManyToMany(mappedBy = "users")
    @ToString.Exclude
    @Builder.Default
    @Setter(AccessLevel.PRIVATE)
    private Set<Role> roles = new HashSet<>();

    private boolean enabled;

    @CreationTimestamp
    @Column(name = "created", nullable = false, updatable = false)
    private ZonedDateTime created_at;

    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshToken> refreshTokens;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @ManyToMany(mappedBy = "subscribers", fetch = FetchType.LAZY)
    private Set<Subreddit> subscriptions = new HashSet<>();


    public void addRole(Role role) {
        roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }
}
