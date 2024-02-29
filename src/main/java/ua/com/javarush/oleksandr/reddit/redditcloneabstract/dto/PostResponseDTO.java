package ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "url")
@Builder(builderMethodName = "with")
public class PostResponseDTO {

    private Long id;

    private String postName;

    private String description;

    private String url;

    private Integer voteCount;

    private String subredditName;

    private String userName;
}
