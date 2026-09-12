package in.akhilesh.instantcart.utils;

import in.akhilesh.instantcart.dto.user.AuthRequest;
import in.akhilesh.instantcart.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt.secret.key}")
    private String jwtSecretkey;

    private final long EXPIRATION_TIME = 1000 * 60 * 60;

    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor(jwtSecretkey.getBytes(StandardCharsets.UTF_8));
    }

    private boolean isTokenExpired(String token) {
        boolean isExpired = extractClaims(token).getExpiration().before(new Date());
        return isExpired;
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId().toString())
                .claim("name", user.getName())
                .claim("role", user.getRole().name())
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis() + EXPIRATION_TIME)
                )
                .signWith(secretKey())
                .compact();
    }

    public String getEmailFromToken(String token) {
        Claims claims = extractClaims(token);
        return claims.getSubject();
    }

    public String getNameFromToken(String token) {
        return extractClaims(token)
                .get("name", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);
            return claims.getExpiration()
                    .after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String getRoleFromToken(String token) {
        return extractClaims(token)
                .get("role", String.class);
    }

    public ObjectId getUserIdFromToken(String token) {
        String userId = extractClaims(token)
                .get("userId", String.class);
        return new ObjectId(userId);
    }

}
