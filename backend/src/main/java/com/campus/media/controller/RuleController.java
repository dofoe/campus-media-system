package com.campus.media.controller;

import com.campus.media.dto.request.RuleRequest;
import com.campus.media.dto.response.ApiResponse;
import com.campus.media.dto.response.PageResponse;
import com.campus.media.entity.AuditRule;
import com.campus.media.entity.User;
import com.campus.media.service.RuleService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/rules")
public class RuleController {
    
    private final RuleService ruleService;
    
    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }
    
    @GetMapping
    public ApiResponse<PageResponse<AuditRule>> getRules(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        PageResponse<AuditRule> response = ruleService.getRules(pageNum, pageSize);
        return ApiResponse.success(response);
    }
    
    @PostMapping
    public ApiResponse<AuditRule> createRule(@Valid @RequestBody RuleRequest request,
                                             @AuthenticationPrincipal User user) {
        AuditRule rule = ruleService.createRule(request, user);
        return ApiResponse.success(rule);
    }
    
    @PutMapping("/{id}")
    public ApiResponse<AuditRule> updateRule(@PathVariable Long id, @Valid @RequestBody RuleRequest request) {
        AuditRule rule = ruleService.updateRule(id, request);
        return ApiResponse.success(rule);
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRule(@PathVariable Long id) {
        ruleService.deleteRule(id);
        return ApiResponse.success();
    }
    
    @PostMapping("/test")
    public ApiResponse<Map<String, Object>> testRule(@RequestBody RuleRequest request) {
        Map<String, Object> response = ruleService.testRule(request);
        return ApiResponse.success(response);
    }
}
