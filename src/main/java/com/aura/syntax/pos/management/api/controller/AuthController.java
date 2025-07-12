package com.aura.syntax.pos.management.api.controller;

import com.aura.syntax.pos.management.api.dto.AuthRequest;
import com.aura.syntax.pos.management.api.dto.AuthResponse;
import com.aura.syntax.pos.management.api.dto.RefreshTokenRequest;
import com.aura.syntax.pos.management.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173"})
public class AuthController {
    private final AuthenticationService service;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(service.refreshToken(request));
    }
}