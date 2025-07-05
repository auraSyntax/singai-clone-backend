package com.aura.syntax.pos.management.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/menu-management")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000"})

public class MenuManagementController {
}
