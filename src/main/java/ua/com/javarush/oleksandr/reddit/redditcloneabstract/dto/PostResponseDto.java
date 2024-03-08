package ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "url")
@Builder(builderMethodName = "with")
public class PostResponseDto {

    private Long id;

    private String postName;

    private String description;

    private String url;

    private Integer voteCount;

    private String subredditName;

    private String userName;
}
