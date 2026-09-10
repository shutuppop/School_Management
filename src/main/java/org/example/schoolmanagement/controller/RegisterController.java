package org.example.schoolmanagement.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.schoolmanagement.WindowApplication;
import org.example.schoolmanagement.dto.UserCreateDto;
import org.example.schoolmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "registerController")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterController {
    @Autowired
    UserService userService;
    @FXML
    TextField secondNameField;
    @FXML
    TextField firstNameField;
    @FXML
    TextField middleNameField;
    @FXML
    TextField emailField;
    @FXML
    TextField phoneNumberField;
    @FXML
    PasswordField passwordField;
    @FXML
    Label errorLabel;
    @FXML
    Label successLabel;

    private void createStudent(UserCreateDto userDto) {
        userService.createStudent(userDto);
    }

    private UserCreateDto buildeUserCreateDto() {
        UserCreateDto userDto = new UserCreateDto();
        userDto.setSecondName(secondNameField.getText());
        userDto.setFirstname(firstNameField.getText());
        userDto.setMiddleName(middleNameField.getText());
        userDto.setEmail(emailField.getText());
        userDto.setPhoneNumber(phoneNumberField.getText());
        userDto.setPassword(passwordField.getText());
        return userDto;
    }

    @FXML
    public void handleRegister() {
        try {
            createStudent(buildeUserCreateDto());
            successLabel.setVisible(true);
            successLabel.setText("Вы успешно прошли регистрацию!");
        } catch (RuntimeException e) {
            errorLabel.setVisible(true);
            errorLabel.setText("Не удалось регистрацию пользователя.");
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleBackToLogin() {
        WindowApplication.loginPage();
    }
}
