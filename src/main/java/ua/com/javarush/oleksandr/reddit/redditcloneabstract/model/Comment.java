package ua.com.javarush.oleksandr.reddit.redditcloneabstract.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "with")
@Entity
@Table(name = "comment",
        uniqueConstraints = @UniqueConstraint(name = "unique_comment_id", columnNames = "id"),
        indexes = {
                @Index(name = "idx_comment_id", columnList = "id", unique = true),
                @Index(name = "idx_comment_user_id", columnList = "user_id"),
                @Index(name = "idx_comment_post_id", columnList = "post_id")

        })
@EqualsAndHashCode(exclude = {"user", "post"})
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "text", columnDefinition = "VARCHAR(255)")
    private String text;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_comment_user"))
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "post_id", nullable = false, foreignKey = @ForeignKey(name = "fk_comment_post"))
    private Post post;
}

