package ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {

    private Long userId;
    private String username;
    private String email;
}
