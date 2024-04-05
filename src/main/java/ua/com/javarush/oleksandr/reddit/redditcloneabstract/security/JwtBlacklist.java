package ua.com.javarush.oleksandr.reddit.redditcloneabstract.security;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class JwtBlacklist {

    private final ConcurrentMap<String, Long> blocklist = new ConcurrentHashMap<>();

    public void invalidateToken(String jti) {
        blocklist.put(jti, 0L);
    }

    public boolean isTokenInvalid(String jti) {
        return blocklist.containsKey(jti);
    }
}
