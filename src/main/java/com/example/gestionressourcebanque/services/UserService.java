package com.example.gestionressourcebanque.services;

import com.example.gestionressourcebanque.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UserService {
    User saveUser(User user);
    List<User> getAllUser();
    User getUserById(Long id);
    User updateUser(User user);
    void deleteUserById(Long id);
}
