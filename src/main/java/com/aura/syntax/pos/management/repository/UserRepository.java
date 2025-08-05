package com.aura.syntax.pos.management.repository;

import com.aura.syntax.pos.management.api.dto.UserDto;
import com.aura.syntax.pos.management.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.UserDto(u.id,CONCAT(u.firstName,' ',u.lastName) AS userName,u.email,u.phoneNumber,u.isActive,u.roleId) " +
           "FROM User u " +
           "WHERE :search IS NULL OR CONCAT(u.firstName, ' ', u.lastName) LIKE %:search%")
    Page<UserDto> getAllUsersPagination(Pageable pageable, String search);

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.UserDto(u.id,CONCAT(u.firstName,' ',u.lastName) AS userName) " +
           "FROM User u " +
           "WHERE :search IS NULL OR CONCAT(u.firstName, ' ', u.lastName) LIKE %:search%")
    List<UserDto> getListOfUsers(String search);

    @Query("SELECT CONCAT(u.firstName,' ',u.lastName) FROM User u WHERE u.id = :waiterId")
    String getNameById(Long waiterId);
}
