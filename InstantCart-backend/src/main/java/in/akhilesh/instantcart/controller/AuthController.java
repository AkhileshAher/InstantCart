package in.akhilesh.instantcart.controller;

import in.akhilesh.instantcart.dto.user.*;
import in.akhilesh.instantcart.security.JwtPrincipal;
import in.akhilesh.instantcart.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthServiceResponse> createUser(
            @RequestBody @Valid AuthRequest request) {

        AuthServiceResponse serviceResponse = authService.createUser(request);

        ResponseCookie cookie = ResponseCookie
                .from("access-token", serviceResponse.getAccessToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(serviceResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthServiceResponse> loginUser(
            @RequestBody @Valid LoginRequest request) {

        AuthServiceResponse serviceResponse = authService.login(request);

        ResponseCookie cookie = ResponseCookie
                .from("access-token", serviceResponse.getAccessToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(serviceResponse);
    }

    @PostMapping("/delivery-login")
    public ResponseEntity<AuthServiceResponse> deliveryLogin(
            @RequestBody @Valid LoginRequest request) {

        AuthServiceResponse serviceResponse = authService.login(request);

        ResponseCookie cookie = ResponseCookie
                .from("access-token", serviceResponse.getAccessToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(serviceResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie
                .from("access-token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<RememberResponse> me(
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        JwtPrincipal principal =
                (JwtPrincipal) authentication.getPrincipal();

        RememberResponse response = new RememberResponse(
                principal.getUserId().toHexString(),
                principal.getName(),
                principal.getEmail(),
                principal.getRole()
        );

        return ResponseEntity.ok(response);
    }
}