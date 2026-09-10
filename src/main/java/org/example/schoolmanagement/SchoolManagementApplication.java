package org.example.schoolmanagement;

import javafx.application.Application;
import org.example.schoolmanagement.entity.Role;
import org.example.schoolmanagement.entity.User;
import org.example.schoolmanagement.repository.RoleRepository;
import org.example.schoolmanagement.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SchoolManagementApplication {
    public static void main(String[] args) {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        var context = SpringApplication.run(SchoolManagementApplication.class, args);
        WindowApplication.setApplicationContext(context);
        Application.launch(WindowApplication.class, args);

        UserRepository userRepository = context.getBean(UserRepository.class);
        RoleRepository roleRepository = context.getBean(RoleRepository.class);
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);

//        createAdmin(userRepository, roleRepository, passwordEncoder);
    }

    private static void createAdmin(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setFirstname("Сабирьянов");
        user.setSecondName("Амир");
        user.setMiddleName("Аликович");
        user.setEmail("amirsabir@yandex.ru");
        user.setPhoneNumber("89122045991");
        user.setPassword(passwordEncoder.encode("passwordAdmin"));

        Role role = roleRepository.findRoleByNameRole("ADMIN").orElse(null);

        user.getRoles().add(role);
        role.getUsers().add(user);

        userRepository.save(user);
        roleRepository.save(role);
    }

}
