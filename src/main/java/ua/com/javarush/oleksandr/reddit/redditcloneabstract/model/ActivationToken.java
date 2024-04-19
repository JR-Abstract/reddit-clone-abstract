package ua.com.javarush.oleksandr.reddit.redditcloneabstract.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "activation_token",
        schema = "public",
        indexes = @Index(name = "idx_activation_token_token", columnList = "token", unique = true))
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "with", toBuilder = true)
public class ActivationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token", length = 36, unique = true, updatable = false, nullable = false)
    private String token;

    @Column(name = "expiry_date", nullable = false, updatable = false)
    private LocalDateTime expiryDate;

    @Builder.Default
    @Column(name = "activated", nullable = false)
    private Boolean activated = false;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id",
            nullable = false,
            updatable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_activation_token_user_id", value = ConstraintMode.CONSTRAINT,
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES \"user\"(id) ON DELETE CASCADE"))
    private User user;
}
