package ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "with")
public class CommentDto {

    private Long id;

    @NotNull(message = "Post ID cannot be null.")
    private Long postId;

    private LocalDateTime createdDate;

    @Length(max = 255, message = "Description must not exceed 255 characters.")
    private String text;

    @NotNull(message = "User name cannot be null.")
    @NotBlank(message = "User name must not be blank.")
    private String userName;
}