package in.akhilesh.instantcart.service;

import in.akhilesh.instantcart.dto.RegisterRequest;
import in.akhilesh.instantcart.entity.User;
import in.akhilesh.instantcart.entity.UserRole;
import in.akhilesh.instantcart.exception.DuplicateEmailException;
import in.akhilesh.instantcart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(RegisterRequest request) {
        if(userRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException("Email Already Exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(UserRole.CUSTOMER);
        user.setPhone(request.phone());
        user.setEmailVerified(false);
        return userRepository.save(user);
    }

    public User verifyUserEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmailVerified(true);

        return userRepository.save(user);
    }

}
