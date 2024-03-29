package ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "url")
@Builder(builderMethodName = "with")
public class PostRequestDto {

    private Long id;

    @NotNull(message = "Post name cannot be null.")
    @NotBlank(message = "Post name must not be blank.")
    @Length(max = 255, message = "Post name must not exceed 255 characters.")
    private String postName;

    @Length(max = 255, message = "Description must not exceed 255 characters.")
    private String description;

    @URL(message = "Bad URL format")
    private String url;

    @NotNull(message = "Subreddit id must not be blank.")
    private Long subredditId;

    @NotNull(message = "User ID cannot be null.")
    private Long userId;
}
