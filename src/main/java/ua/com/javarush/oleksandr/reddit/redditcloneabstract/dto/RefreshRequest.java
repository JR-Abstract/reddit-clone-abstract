package ua.com.javarush.oleksandr.reddit.redditcloneabstract.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshRequest {
    private String jwtToken;
    private String refreshToken;
}