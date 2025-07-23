package com.aura.syntax.pos.management.service;

import com.aura.syntax.pos.management.api.dto.PaginatedResponseDto;
import com.aura.syntax.pos.management.api.dto.ResponseDto;
import com.aura.syntax.pos.management.api.dto.UserDto;
import com.aura.syntax.pos.management.config.ApplicationConfig;
import com.aura.syntax.pos.management.config.SecurityConfig;
import com.aura.syntax.pos.management.entity.User;
import com.aura.syntax.pos.management.exception.ServiceException;
import com.aura.syntax.pos.management.repository.RefreshTokenRepository;
import com.aura.syntax.pos.management.repository.RoleRepository;
import com.aura.syntax.pos.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final ApplicationConfig applicationConfig;

    private final RoleRepository roleRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    public ResponseDto saveUser(UserDto userDto) {
        User user = User.builder()
                .id(userDto.getId())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .isActive(Boolean.TRUE)
                .createdAt(LocalDateTime.now())
                .roleId(userDto.getRoleId())
                .phoneNumber(userDto.getPhoneNumber())
                .password(applicationConfig.passwordEncoder().encode(userDto.getPassword()))
                .build();
        userRepository.save(user);
        return new ResponseDto("User saved successfully");
    }

    public ResponseDto updateUser(UserDto userDto) {
        User existingUser = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new ServiceException("User not found", "Bad request", HttpStatus.BAD_REQUEST));

        existingUser.setId(userDto.getId());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setUpdatedAt(LocalDateTime.now());
        existingUser.setRoleId(userDto.getRoleId());
        existingUser.setPhoneNumber(userDto.getPhoneNumber());
        existingUser.setPassword(applicationConfig.passwordEncoder().encode(userDto.getPassword()));
        userRepository.save(existingUser);
        return new ResponseDto("User updated successfully");
    }

    public UserDto findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ServiceException("User not found", "Bad request", HttpStatus.BAD_REQUEST));
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .roleId(user.getRoleId())
                .phoneNumber(user.getPhoneNumber())
                .roleName(roleRepository.getRoleById(user.getRoleId()))
                .build();
    }

    public PaginatedResponseDto<UserDto> getAllUsersPagination(Integer page,Integer size,String search){
        Pageable pageable = PageRequest.of(page - 1,size);
        Page<UserDto> userDtos = userRepository.getAllUsersPagination(pageable,search);
        PaginatedResponseDto<UserDto> userDtoPaginatedResponseDto = new PaginatedResponseDto<>();
        List<UserDto> userDtoList = userDtos.getContent();
        userDtoList.stream()
                        .forEach(userDto -> {
                            userDto.setRoleName(roleRepository.getRoleById(userDto.getRoleId()));
                        });
        userDtoPaginatedResponseDto.setData(userDtoList);
        userDtoPaginatedResponseDto.setCurrentPage(page);
        userDtoPaginatedResponseDto.setTotalPages(userDtos.getTotalPages());
        userDtoPaginatedResponseDto.setTotalItems(userDtos.getTotalElements());

        return userDtoPaginatedResponseDto;
    }

    public List<UserDto> getListOfUsers(String search){
        return userRepository.getListOfUsers(search);
    }

    public ResponseDto updateStatus(Long id, Boolean status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ServiceException("User not found", "Bad request", HttpStatus.BAD_REQUEST));

        user.setIsActive(status);
        userRepository.save(user);
        return new ResponseDto("User status updated successfully");

    }

    public ResponseDto deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ServiceException("User not found", "Bad request", HttpStatus.BAD_REQUEST));
        userRepository.deleteById(id);
        return new ResponseDto("User deleted successfully");
    }

}
