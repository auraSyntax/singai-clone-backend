package com.aura.syntax.pos.management.repository;

import com.aura.syntax.pos.management.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByUser_Id(Long userId);

    @Query("SELECT r.id " +
           "FROM RefreshToken r " +
           "WHERE r.userId = :id")
    List<String> getAllRefreshTokenByUserId(Long id);
}
