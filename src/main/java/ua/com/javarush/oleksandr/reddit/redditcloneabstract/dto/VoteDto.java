package ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.enums.VoteEntityType;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.model.enums.VoteType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "with")
public class VoteDto {
    private VoteType voteType;
    private Long userId;
    private Long voteEntityId;
    private VoteEntityType voteEntityType;
}
