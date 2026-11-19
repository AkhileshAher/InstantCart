package in.akhilesh.instantcart.security;

import in.akhilesh.instantcart.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.expiration:900000}")
    private Long jwtExpiration;

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        return generateToken(new HashMap(), user);
    }

    private String generateToken(Map<String, Object> extraClaims, User user) {
        Map<String, Object> claims = new HashMap(extraClaims);
        claims.put("userId", user.getId().toString());
        claims.put("email", user.getEmail());
        claims.put("avatar", user.getAvatar());

        return Jwts
                .builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey())
                .compact();
    }

    // Extract User Id
    public Long extractUserId(String jwtToken) {
        String userIdStr = extractClaim(jwtToken, claims -> claims.get("userId", String.class));
        return userIdStr != null ? Long.parseLong(userIdStr) : null;
    }

    // Extract user email
    public String extractUserEmail(String jwtToken) {
        String userEmail = extractClaim(jwtToken, claims -> claims.get("email",String.class));
        return userEmail != null ? userEmail : null;
    }

    // Extract username
    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    private <T> T extractClaim(String jwtToken, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }




    public boolean isTokenValid(String jwtToken, User user) {
        final Long userIdFromToken = extractUserId(jwtToken);
        final Long userIdDetails = user.getId();
        return (
                userIdFromToken != null &&
                userIdFromToken.equals(userIdDetails) &&
                !isTokenExpired(jwtToken)
        );
    }

    private boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

}