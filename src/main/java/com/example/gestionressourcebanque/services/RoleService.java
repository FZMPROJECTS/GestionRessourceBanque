package com.example.gestionressourcebanque.services;

import com.example.gestionressourcebanque.entities.Role;
import com.example.gestionressourcebanque.entities.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {
    Role saveRole (Role role);
    Role updateRole(Role role);
    void deleteRoleById(long id);
    void deleteAllRoles();
    Role getRoleById(long id);
    List<Role> getAllRoles();
}
