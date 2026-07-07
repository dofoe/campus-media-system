package com.campus.media.controller;

import com.campus.media.dto.request.LoginRequest;
import com.campus.media.dto.response.ApiResponse;
import com.campus.media.dto.response.LoginResponse;
import com.campus.media.entity.User;
import com.campus.media.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ApiResponse.success(response);
    }
    
    @GetMapping("/menus")
    public ApiResponse<Object> getMenus(@AuthenticationPrincipal User user) {
        Object menus = authService.getMenus(user);
        return ApiResponse.success(menus);
    }
}
