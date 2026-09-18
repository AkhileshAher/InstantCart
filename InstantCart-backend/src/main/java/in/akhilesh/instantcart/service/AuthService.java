package in.akhilesh.instantcart.service;

import in.akhilesh.instantcart.dto.TokenRequest;
import in.akhilesh.instantcart.dto.delivery.DeliveryPartnerResponse;
import in.akhilesh.instantcart.dto.user.AuthRequest;
import in.akhilesh.instantcart.dto.user.AuthServiceResponse;
import in.akhilesh.instantcart.dto.user.LoginRequest;
import in.akhilesh.instantcart.dto.user.UserResponse;
import in.akhilesh.instantcart.entity.DeliveryPartner;
import in.akhilesh.instantcart.entity.User;
import in.akhilesh.instantcart.entity.enums.UserRole;
import in.akhilesh.instantcart.repository.UserRepository;
import in.akhilesh.instantcart.security.CustomUserDetails;
import in.akhilesh.instantcart.security.DeliveryPartnerUserDetails;
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

    public AuthServiceResponse createUser(AuthRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User with this Email Already Exist");
        }

        if (request.getRole() != UserRole.CUSTOMER && request.getRole() != UserRole.VENDOR) {
            throw new RuntimeException("Invalid User Role Entered");
        }

        User user = mapRequestToUser(request);
        User savedUser = userRepository.save(user);
        TokenRequest tokenUser = mapUserToTokenRequest(savedUser);

        String accessToken = jwtUtil.generateAccessToken(tokenUser);

        AuthServiceResponse serviceResponse = new AuthServiceResponse();
        serviceResponse.setUser(mapUserToResponse(savedUser));
        serviceResponse.setAccessToken(accessToken);

        return serviceResponse;
    }

    private UserResponse mapUserToResponse(User savedUser) {
        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());
        response.setPhone(savedUser.getPhone());
        response.setAvatar(savedUser.getAvatar());
        response.setRole(savedUser.getRole());
        response.setCreatedAt(savedUser.getCreatedAt());
        response.setUpdatedAt(savedUser.getUpdatedAt());
        return response;
    }


    public AuthServiceResponse login(LoginRequest request) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails userDetails) {

            User user = userDetails.getUser();
            TokenRequest tokenRequest = mapUserToTokenRequest(user);
            String accessToken = jwtUtil.generateAccessToken(tokenRequest);

            AuthServiceResponse serviceResponse = new AuthServiceResponse();
            serviceResponse.setUser(mapUserToResponse(user));
            serviceResponse.setAccessToken(accessToken);

            return serviceResponse;
        }

        if (principal instanceof DeliveryPartnerUserDetails deliveryDetails) {

            DeliveryPartner deliveryPartner =
                    deliveryDetails.getDeliveryPartner();
            TokenRequest tokenRequest = mapPartnerToTokenRequest(deliveryPartner);
            String accessToken = jwtUtil.generateAccessToken(tokenRequest);

            AuthServiceResponse serviceResponse = new AuthServiceResponse();
            serviceResponse.setAccessToken(accessToken);
            serviceResponse.setPartner(mapToPartnerResponse(deliveryPartner));

            return serviceResponse;
        }

        throw new RuntimeException("Unsupported user type");
    }

    private DeliveryPartnerResponse mapToPartnerResponse(DeliveryPartner partner) {
        DeliveryPartnerResponse response = new DeliveryPartnerResponse();
        response.setId(partner.getId().toHexString());
        response.setName(partner.getName());
        response.setEmail(partner.getEmail());
        response.setPhone(partner.getPhone());
        response.setIsActive(partner.getIsActive());
        response.setVehicleType(partner.getVehicleType());
        response.setAvatar(partner.getAvatar());
        response.setCreatedAt(partner.getCreatedAt());
        response.setUpdatedAt(partner.getUpdatedAt());
        return response;
    }

    private TokenRequest mapPartnerToTokenRequest(DeliveryPartner deliveryPartner) {
        TokenRequest token = new TokenRequest();
        token.setId(deliveryPartner.getId());
        token.setName(deliveryPartner.getName());
        token.setEmail(deliveryPartner.getEmail());
        token.setRole(UserRole.DELIVERY);
        return token;
    }

    private User mapRequestToUser(AuthRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        if(request.getRole() != UserRole.CUSTOMER && request.getRole() != UserRole.VENDOR ){
            throw new RuntimeException("Invalid User Role Entered");
        }
        user.setRole(request.getRole());
        user.setPhone(request.getPhone());
        user.setAvatar(request.getAvatar());
        return user;
    }

    private TokenRequest mapUserToTokenRequest(User savedUser) {
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setId(savedUser.getId());
        tokenRequest.setName(savedUser.getName());
        tokenRequest.setEmail(savedUser.getEmail());
        tokenRequest.setRole(savedUser.getRole());
        return tokenRequest;
    }


}
