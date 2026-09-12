package in.akhilesh.instantcart.security;

import in.akhilesh.instantcart.utils.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Get Authorization header
        String authHeader = request.getHeader("Authorization");

        // 2. No Authorization header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extract JWT
        String token = authHeader.substring(7);

        // 4. Empty token
        if (token.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            // 5. Validate JWT
            if (!jwtUtil.isTokenValid(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 6. Extract information from JWT
            ObjectId userId = jwtUtil.getUserIdFromToken(token);
            String name = jwtUtil.getNameFromToken(token);
            String email = jwtUtil.getEmailFromToken(token);
            String role = jwtUtil.getRoleFromToken(token);

            // 7. Create custom principal
            JwtPrincipal principal = new JwtPrincipal(userId, name, email, role);

            // 8. Create authority
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

            // 9. Create Authentication object
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            List.of(authority)
                    );

            // 10. Store Authentication in SecurityContext
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

        } catch (Exception e) {

            // Invalid/corrupted JWT
            SecurityContextHolder
                    .clearContext();
        }

        // 11. Continue request
        filterChain.doFilter(request, response);
    }
}