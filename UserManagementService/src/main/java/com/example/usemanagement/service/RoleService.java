package com.example.usemanagement.service;

import com.example.usemanagement.entity.Role;
import com.example.usemanagement.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    public Role saveUserRole(Role role){
        return roleRepository.save(role);

    }

}
