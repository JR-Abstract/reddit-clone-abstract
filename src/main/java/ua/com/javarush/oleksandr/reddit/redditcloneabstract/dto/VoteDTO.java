package ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.enums.VoteType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "with")
public class VoteDTO {
    private Long id;
    private VoteType voteType;
    private Long userId;
    private Long postId;
}
