package com.example.gestionressourcebanque;

import com.example.gestionressourcebanque.securite.services.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GestionRessourceBanqueApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionRessourceBanqueApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(AccountService accountService){
        return args -> {


            accountService.createUser("admin", "123", "admin@gmail.com", "123");


            accountService.createRole("ADMIN");


            accountService.addRoleToUser("admin", "ADMIN");





        };
    }

}
