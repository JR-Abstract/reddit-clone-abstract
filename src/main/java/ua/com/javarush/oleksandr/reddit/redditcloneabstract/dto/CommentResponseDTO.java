package ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CommentResponseDTO {

    private Long id;
    private Long postId;
    private LocalDateTime createdDate;
    private String text;
    private String userName;
}
