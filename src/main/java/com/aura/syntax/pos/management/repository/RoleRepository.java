package com.aura.syntax.pos.management.repository;

import com.aura.syntax.pos.management.api.dto.StockDto;
import com.aura.syntax.pos.management.entity.Role;
import com.aura.syntax.pos.management.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Long> {
    @Query("SELECT r.roleName " +
           "FROM Role r " +
           "WHERE r.id = :id")
    String getRoleById(Long id);

}
