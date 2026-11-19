package in.akhilesh.instantcart.security;

import in.akhilesh.instantcart.dto.LoginRequest;
import in.akhilesh.instantcart.dto.LoginResponse;
import in.akhilesh.instantcart.dto.OtpRequest;
import in.akhilesh.instantcart.dto.RegisterRequest;
import in.akhilesh.instantcart.entity.RefreshToken;
import in.akhilesh.instantcart.entity.User;
import in.akhilesh.instantcart.service.OtpService;
import in.akhilesh.instantcart.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final OtpService otpService;

    public void register(RegisterRequest request) {
        User user = userService.registerUser(request);
        otpService.generateAndSendOtp(user.getEmail());
    }

    public LoginResponse verifyRegistrationOtp(OtpRequest request) {

        otpService.verifyOtp(request.email(), request.otp());
        User user = userService.verifyUserEmail(request.email());
        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user.getUsername());

        return new LoginResponse(
                accessToken,
                refreshToken.getToken()
        );
    }


    public LoginResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = (User) userDetails;

        if (!user.isEmailVerified()) {
            throw new RuntimeException("Please verify your email before logging in");
        }

        String accessToken = jwtService.generateToken(user);

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(userDetails.getUsername());

        return new LoginResponse(accessToken, refreshToken.getToken());
    }

    public LoginResponse refreshToken(String refreshToken) {

        RefreshToken verifiedToken =
                refreshTokenService.verifyRefreshToken(refreshToken);

        User user = verifiedToken.getUser();

        if (!user.isEmailVerified()) {
            throw new RuntimeException("Please verify your email first");
        }

        String newAccessToken = jwtService.generateToken(user);

        RefreshToken newRefreshToken =
                refreshTokenService.rotateRefreshToken(verifiedToken);

        return new LoginResponse(
                newAccessToken,
                newRefreshToken.getToken()
        );
    }

    public void logout(String refreshToken) {
        refreshTokenService.revokeRefreshToken(refreshToken);
    }

}
