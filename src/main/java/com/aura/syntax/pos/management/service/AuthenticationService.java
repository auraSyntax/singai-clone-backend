package com.aura.syntax.pos.management.service;

import com.aura.syntax.pos.management.api.dto.*;
import com.aura.syntax.pos.management.config.JwtService;
import com.aura.syntax.pos.management.config.RefreshTokenService;
import com.aura.syntax.pos.management.entity.RefreshToken;
import com.aura.syntax.pos.management.entity.User;
import com.aura.syntax.pos.management.exception.ServiceException;
import com.aura.syntax.pos.management.repository.RoleRepository;
import com.aura.syntax.pos.management.repository.UserRepository;
import com.cloudinary.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final RoleRepository roleRepository;

    @Value("${app.jwt.refresh.duration}")
    private String expirationTime;

    @Value("${admin.email}")
    private String adminEmail;

    private final EmailNotificationService emailNotificationService;

    public AuthResponse authenticate(AuthRequest request) {
        try {
            Authentication authentication = authenticateUser(request.getEmail(), request.getPassword());

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new ServiceException("User not found", "Bad request", HttpStatus.BAD_REQUEST));
            if (!request.getRoleId().equals(user.getRoleId())){
                throw new ServiceException("Role and credentials are conflict", "Unauthorized", HttpStatus.UNAUTHORIZED);
            }
            return buildAuthResponse(user, authentication);

        } catch (BadCredentialsException e) {
            throw new ServiceException("Invalid email or password", "Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .orElseThrow(() -> new ServiceException("Refresh token not found", "Unauthorized", HttpStatus.UNAUTHORIZED));

        User user = refreshToken.getUser();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(), null, user.getAuthorities());

        String newAccessToken = jwtService.generateToken(user, authentication);
        return buildAuthResponse(user, newAccessToken);
    }

    private AuthResponse buildAuthResponse(User user, Authentication authentication) {
        String jwtToken = jwtService.generateToken(user, authentication);
        return buildAuthResponse(user, jwtToken);
    }

    private AuthResponse buildAuthResponse(User user, String accessToken) {
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        UserDto userDto = new UserDto();
        userDto.setUserName(user.getFirstName() + " " + user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        userDto.setRoleId(user.getRoleId());
        userDto.setRoleName(roleRepository.getRoleById(user.getRoleId()));

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .userDto(userDto)
                .expirationTime(expirationTime)
                .build();
    }

    private Authentication authenticateUser(String email, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
    }

    public void sendEmail(MessageDto messageDto) {
        try {
            EmailDataDto emailDataDto = new EmailDataDto();
            emailDataDto.setSubject("User Interested");
            emailDataDto.setServiceProvider("singai");
            emailDataDto.setMailTemplateName("user_interest");
            emailDataDto.setRecipients(Collections.singletonList(adminEmail));

            Map<String, Object> data = new HashMap<>();
            data.put("firstName", messageDto.getFirstName());
            data.put("lastName", messageDto.getLastName());
            data.put("contactNumber",messageDto.getContactNumber());
            data.put("email",messageDto.getEmail());
            data.put("subject",messageDto.getSubject());
            data.put("message",messageDto.getMessage());
            emailDataDto.setData(data);

            emailNotificationService.sendEmailWithAttachment(emailDataDto);
        } catch (Exception e) {
            throw new ServiceException("EMAIL_SENDING_FAILED", "Bad request", HttpStatus.BAD_REQUEST);
        }
    }
}