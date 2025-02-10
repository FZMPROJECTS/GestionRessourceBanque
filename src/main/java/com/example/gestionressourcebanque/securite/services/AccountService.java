package com.example.gestionressourcebanque.securite.services;

import com.example.gestionressourcebanque.entities.Role;
import com.example.gestionressourcebanque.entities.Utilisateur;

public interface AccountService {
    Utilisateur createUser(String username, String password, String email, String confirmPassword);
    Role createRole(String newRole);
    void addRoleToUser(String usename, String newRole);
    void removeRoleFromUser(String User, String newRole);
    Utilisateur loadUserByUsername (String username);
}
