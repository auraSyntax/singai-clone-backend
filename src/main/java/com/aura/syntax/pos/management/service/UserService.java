package com.aura.syntax.pos.management.service;

import com.aura.syntax.pos.management.api.dto.EmailDataDto;
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
import com.cloudinary.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final ApplicationConfig applicationConfig;

    private final RoleRepository roleRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${admin.email}")
    private String adminEmail;

    private final EmailNotificationService emailNotificationService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
        User savedUser = userRepository.save(user);
        sendConfirmationEmail(savedUser,userDto);
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

    private void sendConfirmationEmail(User user,UserDto userDto) {
        try {
            EmailDataDto emailDataDto = new EmailDataDto();
            emailDataDto.setSubject("User Registration Confirmation - Singai Restaurant");
            emailDataDto.setServiceProvider("singai");
            emailDataDto.setMailTemplateName("user_confirmation");
            emailDataDto.setRecipients(Collections.singletonList(user.getEmail()));

            if (StringUtils.isNotBlank(adminEmail)) {
                emailDataDto.setCcList(Collections.singletonList(adminEmail));
                emailDataDto.setBccList(Collections.singletonList(adminEmail));
            } else {
                log.warn("Admin email is not configured, CC/BCC will not be sent");
            }

            Map<String, Object> data = new HashMap<>();
            data.put("userName", user.getFirstName());
            data.put("email", user.getEmail());
            data.put("password",userDto.getPassword());
            emailDataDto.setData(data);

            emailNotificationService.sendEmailWithAttachment(emailDataDto);
        } catch (Exception e) {
            log.error("Failed to send confirmation email for booking: {}", user.getId(), e);
            throw new ServiceException("EMAIL_SENDING_FAILED", "Bad request", HttpStatus.BAD_REQUEST);
        }
    }

}
