package ua.com.javarush.oleksandr.reddit.redditcloneabstract.model;

import jakarta.persistence.*;
import lombok.*;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.enums.VoteType;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "with")
@Entity
@Table(name = "vote",
        indexes = {
                @Index(name = "idx_vote_user_id", columnList = "user_id"),
                @Index(name = "idx_vote_post_id", columnList = "post_id"),
                @Index(name = "idx_vote_comment_id", columnList = "comment_id")
        })
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long voteId;

    @Enumerated(EnumType.ORDINAL)
    private VoteType voteType;

    @ToString.Exclude
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_vote_user"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "fk_vote_post"))
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id", foreignKey = @ForeignKey(name = "fk_vote_comment"))
    private Comment comment;
}
