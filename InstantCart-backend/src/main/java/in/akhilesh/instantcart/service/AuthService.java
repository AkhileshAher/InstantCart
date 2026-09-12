package in.akhilesh.instantcart.service;

import in.akhilesh.instantcart.dto.user.AuthRequest;
import in.akhilesh.instantcart.dto.user.AuthResponse;
import in.akhilesh.instantcart.entity.User;
import in.akhilesh.instantcart.entity.enums.UserRole;
import in.akhilesh.instantcart.repository.UserRepository;
import in.akhilesh.instantcart.security.CustomUserDetails;
import in.akhilesh.instantcart.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public AuthResponse createUser(AuthRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User with this Email Already Exist");
        }
        User user = mapRequestToUser(request);
        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateAccessToken(savedUser);
        return mapUserToResponse(savedUser,token);
    }



    public String login(AuthRequest request) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        // We need the actual User to generate the JWT
        User user = userRepository.findByEmail(
                userDetails.getUsername()
        ).orElseThrow();

        return jwtUtil.generateAccessToken(user);
    }

    private User mapRequestToUser(AuthRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.USER);
        user.setPhone(request.getPhone());
        user.setAvatar(request.getAvatar());
        return user;
    }

    private AuthResponse mapUserToResponse(User savedUser,String token) {
        AuthResponse response = new AuthResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                token
        );
        return response;
    }


}
