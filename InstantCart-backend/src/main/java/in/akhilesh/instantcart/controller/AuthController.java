package in.akhilesh.instantcart.controller;

import in.akhilesh.instantcart.dto.*;
import in.akhilesh.instantcart.entity.User;
import in.akhilesh.instantcart.security.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authenticationService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            @AuthenticationPrincipal User user
    ) {
        UserResponse response = new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {

        authenticationService.register(request);
        return ResponseEntity.ok(
                "Registration successful. OTP sent to your email."
        );
    }

    @PostMapping("/verify-registration")
    public ResponseEntity<String> verifyRegistration(
            @Valid @RequestBody OtpRequest request) {

        LoginResponse result =
                authenticationService.verifyRegistrationOtp(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,
                        createAccessCookie(result.getAccessToken()).toString())
                .header(HttpHeaders.SET_COOKIE,
                        createRefreshCookie(result.getRefreshToken()).toString())
                .body("Email verified successfully. You are now logged in.");
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        LoginResponse result = authenticationService.login(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,
                        createAccessCookie(result.getAccessToken()).toString())
                .header(HttpHeaders.SET_COOKIE,
                        createRefreshCookie(result.getRefreshToken()).toString())
                .body("Login successful");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refresh-token", required = false) String refreshToken) {

        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(401).body("Refresh token missing");
        }

        LoginResponse result = authenticationService.refreshToken(refreshToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,
                        createAccessCookie(result.getAccessToken()).toString())
                .header(HttpHeaders.SET_COOKIE,
                        createRefreshCookie(result.getRefreshToken()).toString())
                .body("Token refreshed successfully");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(name = "refresh-token", required = false) String refreshToken) {
        if (refreshToken != null && !refreshToken.isBlank()) {
            authenticationService.logout(refreshToken);
        }

        ResponseCookie accessCookie = ResponseCookie.from("access-token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(Duration.ZERO)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh-token", "")
                .httpOnly(true)
                .secure(true)
                .path("/api/auth")
                .sameSite("None")
                .maxAge(Duration.ZERO)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body("Logged out successfully");
    }

    private ResponseCookie createAccessCookie(String token) {
        return ResponseCookie.from("access-token", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
                .maxAge(Duration.ofMinutes(15))
                .build();
    }

    private ResponseCookie createRefreshCookie(String token) {
        return ResponseCookie.from("refresh-token", token)
                .httpOnly(true)
                .secure(true)
                .path("/api/auth")
                .sameSite("None")
                .maxAge(Duration.ofDays(7))
                .build();
    }

}
