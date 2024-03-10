package ua.com.javarush.oleksandr.reddit.redditcloneabstract.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "\"user\"")
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
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Username is mandatory")
    @Size(min = 2, max = 30, message = "Username must be between 2 and 30 characters")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private boolean enabled;

    @CreationTimestamp
    @Column(name = "created", nullable = false, updatable = false, columnDefinition = "DATETIME(6)")
    private ZonedDateTime created_at;

}
