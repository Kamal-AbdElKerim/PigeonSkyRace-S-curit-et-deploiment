package com.example.SpringSecurityDemo.Config;

import com.example.SpringSecurityDemo.Entity.User.Breeder;
import com.example.SpringSecurityDemo.Entity.Role.Role;
import com.example.SpringSecurityDemo.Repository.BreederRepository;
import com.example.SpringSecurityDemo.Repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.UUID;

@Configuration
public class DataLoader {


        @Bean
        public CommandLineRunner loadData(BreederRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
            return args -> {
                // Save roles if they don't already exist
                Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                        .orElseGet(() -> roleRepository.save(new Role(0, "ROLE_ADMIN")));
                Role userRole = roleRepository.findByName("ROLE_USER")
                        .orElseGet(() -> roleRepository.save(new Role(0, "ROLE_USER")));
                Role organizerRole = roleRepository.findByName("ROLE_ORGANIZER")
                        .orElseGet(() -> roleRepository.save(new Role(0, "ROLE_ORGANIZER"))); // ROLE_ORGANIZER

                // Save Admin user
                if (userRepository.findByUsername("admin").isEmpty()) {
                    Breeder adminUser = new Breeder();
                    adminUser.setUsername("admin");
                    adminUser.setNomColombie("Admin");
                    adminUser.setUserID(generateUserID(adminUser));
                    adminUser.setPassword(passwordEncoder.encode("admin123"));

                    adminUser.setRoles(Set.of(adminRole)); // Attach admin role
                    userRepository.save(adminUser);
                }

                // Save Normal User
                if (userRepository.findByUsername("user").isEmpty()) {
                    Breeder normalUser = new Breeder();
                    normalUser.setUsername("user");
                    normalUser.setNomColombie("User");
                    normalUser.setUserID(generateUserID(normalUser));
                    normalUser.setPassword(passwordEncoder.encode("user123"));

                    normalUser.setRoles(Set.of(userRole)); // Attach user role
                    userRepository.save(normalUser);
                }

                // Save Organizer User
                if (userRepository.findByUsername("organizer").isEmpty()) {
                    Breeder organizerUser = new Breeder();
                    organizerUser.setUsername("organizer");
                    organizerUser.setNomColombie("Organizer");
                    organizerUser.setUserID(generateUserID(organizerUser));
                    organizerUser.setPassword(passwordEncoder.encode("organizer123"));

                    organizerUser.setRoles(Set.of(organizerRole)); // Attach organizer role
                    userRepository.save(organizerUser);
                }

                System.out.println("Data Loaded Successfully!");
            };
        }

    private String generateUserID(Breeder breeder) {
        return UUID.randomUUID().toString().substring(0, 10) + breeder.getNomColombie().toLowerCase()  + UUID.randomUUID().toString().substring(0, 10);
    }


}