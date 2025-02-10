package com.example.gestionressourcebanque.securite.services;

import com.example.gestionressourcebanque.entities.Role;
import com.example.gestionressourcebanque.entities.Utilisateur;
import com.example.gestionressourcebanque.repositories.RoleRepository;
import com.example.gestionressourcebanque.repositories.UtilisateurRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Transactional //permet insertion de role
public class AccountServiceImpl implements AccountService{
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UtilisateurRepository utilisateurRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Utilisateur createUser(String username, String password, String email, String confirmPassword) {
        /*

        User user1= new User();
        user1.setUserId(UUID.randomUUID().toString());
        user1.setUsername(username);
        user1.setPassword(bCryptPasswordEncoder.encode(password));
        user1.setEmail(email);
        userRepository.save(user1);


         */




        Utilisateur user1= utilisateurRepository.findByUsername(username);
        if(user1!=null) throw new RuntimeException("User already exists");
        if(!password.equals(confirmPassword)) throw new RuntimeException("Password do not match");
        user1= Utilisateur.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(password))
                .email(email)
                .build();
        return utilisateurRepository.save(user1);

    }

    @Override
    public Role createRole(String newRole) {
        Role role= roleRepository.findByNom(newRole);
        if(role!= null) throw new RuntimeException("Role already exists");
        role= Role
                .builder()
                .nom(newRole)
                .build();
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String usename, String newRole) {

        Utilisateur user= utilisateurRepository.findByUsername(usename);
        Role role= roleRepository.findByNom(newRole);
        user.getRoles().add(role);

    }

    @Override
    public void removeRoleFromUser(String username, String newRole) {
        Utilisateur user= utilisateurRepository.findByUsername(username);
        Role role= roleRepository.findByNom(newRole);
        user.getRoles().remove(role);

    }

    @Override
    public Utilisateur loadUserByUsername(String username) {
        return utilisateurRepository.findByUsername(username);
    }
}
