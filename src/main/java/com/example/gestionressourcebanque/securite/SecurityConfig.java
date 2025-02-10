package com.example.gestionressourcebanque.securite;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){  //algorithme de cryptage
        return new BCryptPasswordEncoder();
    }




    //pour que la methode soit connu dans la classe
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                //.formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())  // Désactive CSRF pour simplifier
                .authorizeHttpRequests(authCustomizer -> authCustomizer

                        // Autoriser l'accès public à l'API du chatbot
                        .requestMatchers("/api/chatbot/**", "/login","/webjars/**", "/css/**", "/js/**", "/images/**").permitAll()
                        // Restriction pour le rôle ADMIN
                        .requestMatchers(
                                "/", "/updateMateriel", "/EditMateriel", "/deleteMateriel", "/saveMateriel", "/CreateMateriel",
                                "/updateDemandeStatut", "/CreateEntite", "/saveEntite", "/deleteEntite", "/EditEntite",
                                "/updateEntite", "/updateEntiteStatut", "/uploadFile", "/CreateMouvement", "/saveMouvement",
                                "/MouvementsList", "/CreateRole", "/saveRole", "RolesList", "EditRole", "updateRole",
                                "deleteRole", "/CreateUtilisateur", "/saveUtilisateur"
                        ).hasAnyRole("ADMIN", "ADMINISTRATEUR")

                        // Restriction stricte pour le rôle USER uniquement
                        .requestMatchers("/notifications", "/markAsRead","/DemanderMateriel").hasAnyRole("UTILISATEUR", "USER")

                        // Ressources accessibles à la fois par USER et ADMIN
                        .requestMatchers(
                                "/chatbot","/api/chatbot", "/api/chatbot/message",
                                "/MaterielsList", "/MaterielDetails", "/DemandesList", "/EntitesList",
                                "/searchMaterielByType", "/EditUtilisateur", "/updateUtilisateur", "/UtilisateurDetails", "UtilisateursList"
                        ).hasAnyRole("UTILISATEUR", "USER", "ADMIN", "ADMINISTRATEUR")

                        // Toute autre requête doit être authentifiée
                        .anyRequest().authenticated()
                )
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/login")
                                .defaultSuccessUrl("/home", true)  // Redirige vers /home après une connexion réussie
                                .permitAll()

                )
                .exceptionHandling(e -> e.accessDeniedPage("/accessDenied"))
                .build();
    }



    //@Bean  //n'ai plus executable apres commenté annotation bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){  //permet de créer des utilisateur avec leurs roles
        return new InMemoryUserDetailsManager(
                User.withUsername("admin").password(bCryptPasswordEncoder().encode("123")).roles("ADMIN").build(),
                User.withUsername("utilisateur").password(bCryptPasswordEncoder().encode("123")).roles("USER").build()

        );
    }


}
