package ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "with")
public class SubredditDTO {

    private Long id;

    @NotEmpty(message = "Name should not be empty")
    private String name;

    private String description;

    @Builder.Default
    private Integer numberOfPosts = 0;
}