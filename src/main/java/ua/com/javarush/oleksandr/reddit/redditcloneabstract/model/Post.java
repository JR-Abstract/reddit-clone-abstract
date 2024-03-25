package ua.com.javarush.oleksandr.reddit.redditcloneabstract.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "url")
@Builder(builderMethodName = "with")
@ToString
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uq_post_url", columnNames = "url"),
        indexes = {
                @Index(name = "idx_post_postName", columnList = "post_name"),
                @Index(name = "idx_post_subreddit_id", columnList = "subreddit_id"),
                @Index(name = "idx_post_user_id", columnList = "user_id")
        }
)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Post name cannot be null.")
    @NotBlank(message = "Post name must not be blank.")
    @Size(max = 255)
    @Column(name = "post_name", nullable = false)
    private String postName;

    private String description;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @URL
    @NaturalId
    @Column(nullable = false)
    private String url;

    @Column(name = "vote_count")
    private Integer voteCount;

    @ToString.Exclude
    @ManyToOne(optional = false)
    @JoinColumn(name = "subreddit_id", nullable = false, foreignKey = @ForeignKey(name = "fk_post_subreddit"))
    private Subreddit subreddit;

    @ToString.Exclude
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_post_user"))
    private User user;
}
