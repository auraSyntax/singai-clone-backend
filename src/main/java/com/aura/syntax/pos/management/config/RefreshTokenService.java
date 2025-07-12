package com.aura.syntax.pos.management.config;

import com.aura.syntax.pos.management.entity.RefreshToken;
import com.aura.syntax.pos.management.exception.ServiceException;
import com.aura.syntax.pos.management.repository.RefreshTokenRepository;
import com.aura.syntax.pos.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${app.jwt.refresh.duration}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findById(userId).get())
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new ServiceException("Refresh token was expired. Please make a new signin request","Un authorized", HttpStatus.UNAUTHORIZED);
        }
        return token;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
}
