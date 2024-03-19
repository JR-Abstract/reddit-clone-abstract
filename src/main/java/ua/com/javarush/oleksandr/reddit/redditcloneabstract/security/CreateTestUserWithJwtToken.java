package ua.com.javarush.oleksandr.reddit.redditcloneabstract.security;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

@Component
@AllArgsConstructor
public class CreateTestUserWithJwtToken {

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder encoder;
    private final DataSource dataSource;


    @PostConstruct
    public void createTestJwtToken() throws SQLException {
        String email = "test@gmail.com";
        List<String> authorities = List.of("ROLE_ADMIN", "ROLE_USER");
        HashMap<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("authorities", authorities);
        String encodedToken = jwtTokenProvider.buildJwtToken(email, extraClaims, 84_600_000L * 1_000_000);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement findUserStatement = connection.prepareStatement("SELECT * FROM \"user\" WHERE email = ?")) {

            findUserStatement.setString(1, email);
            ResultSet rs = findUserStatement.executeQuery();

            if (!rs.next()) {
                PreparedStatement insertUserStatement = connection.prepareStatement("INSERT INTO \"user\"(email, username, password, enabled, created) VALUES (?, ?, ?, ?, ?)");
                insertUserStatement.setString(1, email);
                insertUserStatement.setString(2, "test");
                insertUserStatement.setString(3, encoder.encode("test"));
                insertUserStatement.setBoolean(4, true);
                insertUserStatement.setTimestamp(5, Timestamp.from(Instant.now()));
                insertUserStatement.executeUpdate();
            }
        }

        System.out.println("Test user ->\n username: test, password: test, email: test@gmail.com, with ROLE_USER");
        System.out.println("Test JWT token: \n" + encodedToken);
        System.out.println();
    }
}
