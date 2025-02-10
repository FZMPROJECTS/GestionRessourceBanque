package com.example.gestionressourcebanque.securite.services;

import com.example.gestionressourcebanque.entities.Role;
import com.example.gestionressourcebanque.entities.Utilisateur;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private AccountService accountService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur user= accountService.loadUserByUsername(username);
        if(user==null) throw new UsernameNotFoundException("User not found");
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().stream().map(Role::getNom).toArray(String[]:: new))
                .build();
    }
}
