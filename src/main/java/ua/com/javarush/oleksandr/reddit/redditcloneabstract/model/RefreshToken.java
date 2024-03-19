package ua.com.javarush.oleksandr.reddit.redditcloneabstract.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Entity
@Table(
        name = "refresh_token",
        indexes = @Index(name = "refresh_token_expiration_at_idx", columnList = "expiration_at"))
@Builder(builderMethodName = "with")
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String value;

    @NotNull
    @Column(name = "issued_at", nullable = false)
    private Timestamp issuedAt;

    @NotNull
    @Column(name = "expiration_at", nullable = false)
    private Timestamp expirationAt;

    @NotNull
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "refresh_token_user_fk"), nullable = false)
    private User user;

}