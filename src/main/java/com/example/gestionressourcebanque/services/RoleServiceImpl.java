package com.example.gestionressourcebanque.services;

import com.example.gestionressourcebanque.entities.Role;
import com.example.gestionressourcebanque.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService{
    private RoleRepository RoleRepository ;
    @Override
    public Role saveRole(Role role) {
        return RoleRepository.save(role);
    }

    @Override
    public Role updateRole(Role role) {
        return RoleRepository.save(role);
    }

    @Override
    public void deleteRoleById(long id) {
        RoleRepository.deleteById(id);
    }

    @Override
    public void deleteAllRoles() {
        RoleRepository.deleteAll();
    }

    @Override
    public Role getRoleById(long id) {
        return RoleRepository.findById(id).get();
    }

    @Override
    public List<Role> getAllRoles() {
        return RoleRepository.findAll();
    }


}
