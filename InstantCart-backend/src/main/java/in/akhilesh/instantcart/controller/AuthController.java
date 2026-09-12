package in.akhilesh.instantcart.controller;

import in.akhilesh.instantcart.dto.user.AuthRequest;
import in.akhilesh.instantcart.dto.user.AuthResponse;
import in.akhilesh.instantcart.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> createUser(
            @RequestBody AuthRequest request) {

        AuthResponse response = authService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(
            @RequestBody AuthRequest request) {

        String response = authService.login(request);

        return ResponseEntity.ok(response);
    }
}