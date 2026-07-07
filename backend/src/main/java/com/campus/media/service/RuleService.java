package com.campus.media.service;

import com.campus.media.dto.request.RuleRequest;
import com.campus.media.dto.response.PageResponse;
import com.campus.media.entity.AuditRule;
import com.campus.media.entity.User;
import com.campus.media.repository.AuditRuleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class RuleService {
    
    private final AuditRuleRepository auditRuleRepository;
    
    public RuleService(AuditRuleRepository auditRuleRepository) {
        this.auditRuleRepository = auditRuleRepository;
    }
    
    public PageResponse<AuditRule> getRules(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<AuditRule> page = auditRuleRepository.findAll(pageable);
        return PageResponse.of(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }
    
    @Transactional
    public AuditRule createRule(RuleRequest request, User user) {
        if (auditRuleRepository.existsByName(request.getName())) {
            throw new RuntimeException("规则名称已存在");
        }
        
        AuditRule rule = new AuditRule();
        rule.setName(request.getName());
        rule.setType(request.getType());
        rule.setCondition(request.getCondition());
        rule.setAction(request.getAction());
        rule.setDescription(request.getDescription());
        rule.setStatus(request.getStatus());
        rule.setCreatedBy(user.getId());
        
        return auditRuleRepository.save(rule);
    }
    
    @Transactional
    public AuditRule updateRule(Long id, RuleRequest request) {
        AuditRule rule = auditRuleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("规则不存在"));
        
        if (!rule.getName().equals(request.getName()) && auditRuleRepository.existsByName(request.getName())) {
            throw new RuntimeException("规则名称已存在");
        }
        
        rule.setName(request.getName());
        rule.setType(request.getType());
        rule.setCondition(request.getCondition());
        rule.setAction(request.getAction());
        rule.setDescription(request.getDescription());
        rule.setStatus(request.getStatus());
        
        return auditRuleRepository.save(rule);
    }
    
    @Transactional
    public void deleteRule(Long id) {
        if (!auditRuleRepository.existsById(id)) {
            throw new RuntimeException("规则不存在");
        }
        auditRuleRepository.deleteById(id);
    }
    
    public Map<String, Object> testRule(RuleRequest request) {
        return Map.of(
            "passed", true,
            "matched", new Object[]{}
        );
    }
}
