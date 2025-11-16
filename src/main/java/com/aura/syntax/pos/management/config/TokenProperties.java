package com.aura.syntax.pos.management.config;

import com.aura.syntax.pos.management.exception.ServiceException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class TokenProperties {

    private final HttpServletRequest request;
    private final JwtService jwtService;

    @Before("@annotation(scope)")
    public void checkScope(Scope scope) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ServiceException("NO_TOKEN_PROVIDED", "UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        Claims claims;
        try {
            claims = jwtService.extractAllClaims(token);
        } catch (Exception e) {
            throw new ServiceException("INVALID_TOKEN", "UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }

        // Extract user_id and role_id from JWT
        Object roleIdObj = claims.get("role_id");
        Object userIdObj = claims.get("user_id");

        if (roleIdObj == null || userIdObj == null) {
            throw new ServiceException("TOKEN_MISSING_REQUIRED_CLAIMS", "UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }

        String userRole = roleIdObj.toString();
        String userId = userIdObj.toString();

        List<String> allowedRoles = Arrays.asList(scope.value());

        log.info("Allowed Roles = {}", allowedRoles);
        log.info("User Role = {}", userRole);
        log.info("User ID = {}", userId); // <----- YOU CAN GET USER ID HERE

        if (!allowedRoles.contains(userRole)) {
            throw new ServiceException("YOUR_ROLE_IS_NOT_AUTHORIZED", "UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }
    }

    public JwtTokenDto getTokenPropertiesFromToken() {
        String token = request.getHeader("Authorization").substring(7);
        Claims claims = jwtService.extractAllClaims(token);

        return JwtTokenDto.builder()
                .userId(String.valueOf(claims.get("user_id")))
                .email(String.valueOf(claims.get("email")))
                .roleId(Long.valueOf(claims.get("role_id").toString()))
                .build();
    }
}
