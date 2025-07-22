package com.aura.syntax.pos.management.service;

import com.aura.syntax.pos.management.api.dto.RoleDto;
import com.aura.syntax.pos.management.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<RoleDto> getAllRoles(){
        return roleRepository.getAllRoles();
    }
}
