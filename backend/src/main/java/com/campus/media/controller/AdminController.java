package com.campus.media.controller;

import com.campus.media.dto.request.*;
import com.campus.media.dto.response.ApiResponse;
import com.campus.media.dto.response.DashboardResponse;
import com.campus.media.dto.response.PageResponse;
import com.campus.media.entity.*;
import com.campus.media.service.*;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    
    private final DashboardService dashboardService;
    private final UserService userService;
    private final RoleService roleService;
    private final CategoryService categoryService;
    private final DepartmentService departmentService;
    private final AuditService auditService;
    private final RuleService ruleService;
    
    public AdminController(DashboardService dashboardService,
                           UserService userService,
                           RoleService roleService,
                           CategoryService categoryService,
                           DepartmentService departmentService,
                           AuditService auditService,
                           RuleService ruleService) {
        this.dashboardService = dashboardService;
        this.userService = userService;
        this.roleService = roleService;
        this.categoryService = categoryService;
        this.departmentService = departmentService;
        this.auditService = auditService;
        this.ruleService = ruleService;
    }
    
    @GetMapping("/dashboard")
    public ApiResponse<DashboardResponse> getDashboard() {
        DashboardResponse response = dashboardService.getDashboard();
        return ApiResponse.success(response);
    }
    
    @GetMapping("/users")
    public ApiResponse<PageResponse<User>> getUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String dept,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        PageResponse<User> response = userService.getUsers(keyword, role, dept, pageNum, pageSize);
        return ApiResponse.success(response);
    }
    
    @PostMapping("/users")
    public ApiResponse<User> createUser(@Valid @RequestBody UserCreateRequest request) {
        User user = userService.createUser(request);
        return ApiResponse.success(user);
    }
    
    @PutMapping("/users/{id}")
    public ApiResponse<User> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        User user = userService.updateUser(id, request);
        return ApiResponse.success(user);
    }
    
    @DeleteMapping("/users/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.success();
    }
    
    @PostMapping("/users/{id}/password")
    public ApiResponse<Void> resetPassword(@PathVariable Long id, @Valid @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(id, request.getNewPassword());
        return ApiResponse.success();
    }
    
    @GetMapping("/roles")
    public ApiResponse<List<Role>> getRoles() {
        List<Role> roles = roleService.getRoles();
        return ApiResponse.success(roles);
    }
    
    @PostMapping("/roles")
    public ApiResponse<Role> createRole(@Valid @RequestBody RoleRequest request) {
        Role role = roleService.createRole(request);
        return ApiResponse.success(role);
    }
    
    @PutMapping("/roles/{id}")
    public ApiResponse<Role> updateRole(@PathVariable Long id, @Valid @RequestBody RoleRequest request) {
        Role role = roleService.updateRole(id, request);
        return ApiResponse.success(role);
    }
    
    @DeleteMapping("/roles/{id}")
    public ApiResponse<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ApiResponse.success();
    }
    
    @GetMapping("/depts")
    public ApiResponse<List<Department>> getDepartments() {
        List<Department> depts = departmentService.getDepartments();
        return ApiResponse.success(depts);
    }
    
    @GetMapping("/categories")
    public ApiResponse<List<Category>> getCategories() {
        List<Category> categories = categoryService.getCategories();
        return ApiResponse.success(categories);
    }
    
    @PostMapping("/categories")
    public ApiResponse<Category> createCategory(@Valid @RequestBody CategoryRequest request) {
        Category category = categoryService.createCategory(request);
        return ApiResponse.success(category);
    }
    
    @PutMapping("/categories/{id}")
    public ApiResponse<Category> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        Category category = categoryService.updateCategory(id, request);
        return ApiResponse.success(category);
    }
    
    @DeleteMapping("/categories/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ApiResponse.success();
    }
    
    @GetMapping("/audit/download")
    public ApiResponse<PageResponse<DownloadLog>> getDownloadLogs(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        PageResponse<DownloadLog> response = auditService.getDownloadLogs(keyword, pageNum, pageSize);
        return ApiResponse.success(response);
    }
    
    @GetMapping("/audit/operation")
    public ApiResponse<PageResponse<OperationLog>> getOperationLogs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String operation,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        PageResponse<OperationLog> response = auditService.getOperationLogs(keyword, operation, pageNum, pageSize);
        return ApiResponse.success(response);
    }
    
    @GetMapping("/rules")
    public ApiResponse<PageResponse<AuditRule>> getRules(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        PageResponse<AuditRule> response = ruleService.getRules(pageNum, pageSize);
        return ApiResponse.success(response);
    }
    
    @PostMapping("/rules")
    public ApiResponse<AuditRule> createRule(@Valid @RequestBody RuleRequest request,
                                             @AuthenticationPrincipal User user) {
        AuditRule rule = ruleService.createRule(request, user);
        return ApiResponse.success(rule);
    }
    
    @PutMapping("/rules/{id}")
    public ApiResponse<AuditRule> updateRule(@PathVariable Long id, @Valid @RequestBody RuleRequest request) {
        AuditRule rule = ruleService.updateRule(id, request);
        return ApiResponse.success(rule);
    }
    
    @DeleteMapping("/rules/{id}")
    public ApiResponse<Void> deleteRule(@PathVariable Long id) {
        ruleService.deleteRule(id);
        return ApiResponse.success();
    }
    
    @PostMapping("/rules/test")
    public ApiResponse<Map<String, Object>> testRule(@RequestBody RuleRequest request) {
        Map<String, Object> response = ruleService.testRule(request);
        return ApiResponse.success(response);
    }
}
