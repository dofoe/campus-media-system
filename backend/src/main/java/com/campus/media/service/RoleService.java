package com.campus.media.service;

import com.campus.media.dto.request.RoleRequest;
import com.campus.media.entity.Role;
import com.campus.media.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {
    
    private final RoleRepository roleRepository;
    
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    
    public List<Role> getRoles() {
        return roleRepository.findByStatusTrue();
    }
    
    @Transactional
    public Role createRole(RoleRequest request) {
        if (roleRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("角色编码已存在");
        }
        
        Role role = new Role();
        role.setName(request.getName());
        role.setCode(request.getCode());
        role.setDescription(request.getDescription());
        role.setDataScope(request.getDataScope());
        role.setStatus(request.getStatus());
        
        return roleRepository.save(role);
    }
    
    @Transactional
    public Role updateRole(Long id, RoleRequest request) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("角色不存在"));
        
        if (!role.getCode().equals(request.getCode()) && roleRepository.existsByCode(request.getCode())) {
            throw new RuntimeException("角色编码已存在");
        }
        
        role.setName(request.getName());
        role.setCode(request.getCode());
        role.setDescription(request.getDescription());
        role.setDataScope(request.getDataScope());
        role.setStatus(request.getStatus());
        
        return roleRepository.save(role);
    }
    
    @Transactional
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("角色不存在");
        }
        roleRepository.deleteById(id);
    }
}
