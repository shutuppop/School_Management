package org.example.schoolmanagement.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.schoolmanagement.WindowApplication;
import org.example.schoolmanagement.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component(value = "loginController")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginController {
    @Autowired
    AuthService authService;
    @FXML
    TextField emailField;
    @FXML
    PasswordField passwordField;
    @FXML
    Label errorLabel;
    final Logger LOGGER_LOGGING_SYSTEM = Logger.getLogger(this.getClass().getName());
    @FXML
    public void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        if (email.isBlank() || password.isBlank()) {
            errorLabel.setVisible(true);
            errorLabel.setText("Заполните пропуски.");
        }
        try {
            authService.setupSecurityContextHolder(email, password);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            assert authentication != null;
            boolean isAdmin = authentication.getAuthorities()
                    .stream()
                    .anyMatch(auth -> Objects.equals(auth.getAuthority(), "ROLE_ADMIN"));

            boolean isTeacher = authentication.getAuthorities()
                    .stream()
                    .anyMatch(auth -> Objects.equals(auth.getAuthority(), "ROLE_TEACHER"));

            boolean isStudent = authentication.getAuthorities()
                    .stream()
                    .anyMatch(auth -> Objects.equals(auth.getAuthority(), "ROLE_STUDENT"));

            if (isAdmin) {
                LOGGER_LOGGING_SYSTEM.log(Level.INFO, "This user is Admin");
                WindowApplication.adminPage();
            }
            if (isTeacher) {
                LOGGER_LOGGING_SYSTEM.log(Level.INFO, "This user is Teacher");
                WindowApplication.teacherPage();
            }
            if (isStudent) {
                LOGGER_LOGGING_SYSTEM.log(Level.INFO, "This user is Student");
                WindowApplication.studentPage();
            }

        } catch (UsernameNotFoundException e) {
            errorLabel.setVisible(true);
            errorLabel.setText("Неверный логин или пароль.");
        }
    }

    @FXML
    public void handleRegister() {
        WindowApplication.registerPage();
    }
}
