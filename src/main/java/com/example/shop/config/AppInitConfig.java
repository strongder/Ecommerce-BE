package com.example.shop.config;

import com.example.shop.model.Category;
import com.example.shop.model.Role;
import com.example.shop.model.User;
import com.example.shop.repository.CategoryRepository;
import com.example.shop.repository.RoleRepository;
import com.example.shop.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AppInitConfig {


    PasswordEncoder passwordEncoder;
    CategoryRepository categoryRepository;

    @NonFinal
    private String ADMIN_USER_NAME = "admin";

    @NonFinal
    private String ADMIN_USER_PASS = "admin";


    @Bean
    public ApplicationRunner applicationRunner (UserRepository userRepository, RoleRepository roleRepository)
    {
        return args -> {
            if(userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {
                Role adminRole = roleRepository.save(
                        Role.builder()
                                .name("ADMIN")
                                .build());

                roleRepository.save(Role.builder()
                        .name("USER").build());

                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);

                User admin = userRepository.save(
                        User.builder()
                                .email(ADMIN_USER_NAME)
                                .username("admin")
                                .password(passwordEncoder.encode(ADMIN_USER_PASS))
                              //  .avatar("https://th.bing.com/th/id/OIP.-HoDebcd1MseGnmiTJALDAHaEo?rs=1&pid=ImgDetMain")
                                .roles(roles)
                                .build()
                );
            }
            if(categoryRepository.findById(1L).isEmpty())
            categoryRepository.save(
                    Category.builder()
                            .name("Áo")
                            .build()
            );

        };
    }
}
