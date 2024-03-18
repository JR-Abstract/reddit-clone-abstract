package ua.com.javarush.oleksandr.reddit.redditcloneabstract.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;

import java.time.ZonedDateTime;
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
    private String email;

    private String username;

    private String password;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "users")
    @Setter(AccessLevel.PRIVATE)
    private Set<Role> roles;

    private boolean enabled;

    @CreationTimestamp
    @Column(name = "created", nullable = false, updatable = false)
    private ZonedDateTime created_at;

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
