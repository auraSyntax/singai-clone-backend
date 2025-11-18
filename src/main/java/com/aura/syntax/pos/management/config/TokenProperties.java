package com.aura.syntax.pos.management.config;

import com.aura.syntax.pos.management.exception.ServiceException;
import io.jsonwebtoken.Claims;
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

    private final JwtService jwtService;

    @Before("@annotation(scope)")
    public void checkScope(Scope scope) {
        // This method only works for HTTP requests
        String authHeader = RequestContextHolderUtil.getAuthorizationHeader();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ServiceException("NO_TOKEN_PROVIDED", "UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }

        JwtTokenDto dto = getTokenFromHeader(authHeader);

        List<String> allowedRoles = Arrays.asList(scope.value());
        log.info("Allowed Roles = {}", allowedRoles);
        log.info("User Role = {}", dto.getRoleId());
        log.info("User ID = {}", dto.getUserId());

        if (!allowedRoles.contains(String.valueOf(dto.getRoleId()))) {
            throw new ServiceException("YOUR_ROLE_IS_NOT_AUTHORIZED", "UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        }
    }

    public JwtTokenDto getTokenFromHeader(String header) {
        if (header.startsWith("Bearer ")) {
            header = header.substring(7);
        }
        return getTokenFromRawToken(header);
    }

    public JwtTokenDto getTokenFromRawToken(String token) {
        Claims claims = jwtService.extractAllClaims(token);

        return JwtTokenDto.builder()
                .userId(String.valueOf(claims.get("user_id")))
                .email(String.valueOf(claims.get("email")))
                .roleId(Long.valueOf(claims.get("role_id").toString()))
                .build();
    }
}
