package ua.com.javarush.oleksandr.reddit.redditcloneabstract.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subreddit",
        uniqueConstraints = @UniqueConstraint(name = "unique_subreddit_id", columnNames = "id"),
        indexes = {
                @Index(name = "idx_subreddit_id", columnList = "id", unique = true),
                @Index(name = "idx_subreddit_name", columnList = "name"),
                @Index(name = "idx_subreddit_user_id", columnList = "user_id")
        })
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "with")
public class Subreddit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(name = "description", columnDefinition = "VARCHAR(255)")
    private String description;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private ZonedDateTime createdDate;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id",
            updatable = false,
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_user_id",
                    value = ConstraintMode.CONSTRAINT,
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES \"user\"(id) ON DELETE CASCADE"))
    private User user;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "subreddit_subscribers",
            schema = "public",
            indexes = {
                    @Index(name = "idx_subreddit_subscribers_user_id", columnList = "user_id"),
                    @Index(name = "idx_subreddit_subscribers_subreddit_id", columnList = "subreddit_id")
            },
            joinColumns = @JoinColumn(name = "subreddit_id",
                    referencedColumnName = "id",
                    nullable = false),
            inverseJoinColumns = @JoinColumn(name = "user_id",
                    referencedColumnName = "id",
                    nullable = false),
            inverseForeignKey = @ForeignKey(name = "fk_subreddit_subscribers_user_id",
                    value = ConstraintMode.CONSTRAINT,
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES \"user\"(id) ON DELETE CASCADE"),
            foreignKey = @ForeignKey(name = "fk_subreddit_subscribers_subreddit_id",
                    value = ConstraintMode.CONSTRAINT,
                    foreignKeyDefinition = "FOREIGN KEY (subreddit_id) REFERENCES subreddit(id) ON DELETE CASCADE"))
    private Set<User> subscribers = new HashSet<>();
}