package ua.com.javarush.oleksandr.reddit.redditcloneabstract.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ConstraintMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

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
    @Column(name = "id", updatable = false, columnDefinition = "BIGINT")
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(name = "description", columnDefinition = "VARCHAR(255)")
    private String description;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false, columnDefinition = "DATETIME(6)")
    private ZonedDateTime createdDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id",
            updatable = false,
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_user_id",
                    value = ConstraintMode.CONSTRAINT,
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE"),
            columnDefinition = "BIGINT")
    private User user;
}