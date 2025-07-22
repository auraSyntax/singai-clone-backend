package com.aura.syntax.pos.management.repository;

import com.aura.syntax.pos.management.api.dto.RoleDto;
import com.aura.syntax.pos.management.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Long> {
    @Query("SELECT r.roleName " +
           "FROM Role r " +
           "WHERE r.id = :id")
    String getRoleById(Long id);

    @Query("SELECT NEW com.aura.syntax.pos.management.api.dto.RoleDto(r.id,r.roleName) " +
           "FROM Role r " +
           "WHERE r.isActive = true")
    List<RoleDto> getAllRoles();

}
