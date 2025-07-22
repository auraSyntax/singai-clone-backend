package com.aura.syntax.pos.management.api.controller;

import com.aura.syntax.pos.management.api.dto.RoleDto;
import com.aura.syntax.pos.management.service.RoleService;
import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/role")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173"})
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public List<RoleDto> getAllRoles(){
        return roleService.getAllRoles();
    }
}
