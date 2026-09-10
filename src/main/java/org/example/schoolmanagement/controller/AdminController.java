package org.example.schoolmanagement.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.schoolmanagement.WindowApplication;
import org.example.schoolmanagement.dto.*;
import org.example.schoolmanagement.service.ClassService;
import org.example.schoolmanagement.service.SubjectService;
import org.example.schoolmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component(value = "adminController")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    ClassService classService;
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
    TableView<UserFindDto> teachersTable;
    @FXML
    TableColumn<UserFindDto, String> secondNameColumn;
    @FXML
    TableColumn<UserFindDto, String> firstNameColumn;
    @FXML
    TableColumn<UserFindDto, String> middleNameColumn;
    @FXML
    TableColumn<UserFindDto, String> emailColumn;
    @FXML
    TableColumn<UserFindDto, String> phoneNumberColumn;
    @FXML
    HBox toastNotification;
    @FXML
    Text toastIcon;
    @FXML
    Label toastMessage;
    @FXML
    TextField subjectNameField;
    @FXML
    ComboBox<UserFindDto> teacherComboBox;
    @FXML
    TableView<SubjectFindDto> subjectsTable;
    @FXML
    TableColumn<SubjectFindDto, String> subjectNameColumn;
    @FXML
    TableColumn<SubjectFindDto, List<UserFindDto>> teacherNameColumn;
    @FXML
    TextField classLevelField;
    @FXML
    TextField classNameField;
    @FXML
    ComboBox<UserFindDto> classTeacherComboBox;
    @FXML
    TableView<UserFindDto> selectedTeachersTable;
    @FXML
    TableColumn<UserFindDto, String> selectedTeacherNameColumn;
    @FXML
    TableColumn<UserFindDto, String> selectedTeacherEmailColumn;
    @FXML
    ComboBox<UserFindDto> classStudentComboBox;
    @FXML
    TableView<UserFindDto> selectedStudentsTable;
    @FXML
    TableColumn<UserFindDto, String> selectedStudentNameColumn;
    @FXML
    TableColumn<UserFindDto, String> selectedStudentEmailColumn;
    @FXML
    TableView<ClassFindDto> classesTable;
    @FXML
    TableColumn<ClassFindDto, String> classNumberColumn;
    @FXML
    TableColumn<ClassFindDto, String> classNameColumn;
    @FXML
    TableView<ClassFindDto> studentsInClassTable;
    @FXML
    TableColumn<ClassFindDto, String> studentSecondNameColumn;
    @FXML
    TableColumn<ClassFindDto, String> studentFirstNameColumn;
    @FXML
    TableColumn<ClassFindDto, String> studentMiddleNameColumn;
    @FXML
    TableColumn<ClassFindDto, String> studentEmailColumn;
    @FXML
    Label studentsCountLabel;
    @FXML
    TableView<ClassFindDto> teachersInClassTable;
    @FXML
    TableColumn<ClassFindDto, String> teacherSecondNameColumn;
    @FXML
    TableColumn<ClassFindDto, String> teacherFirstNameColumn;
    @FXML
    TableColumn<ClassFindDto, String> teacherMiddleNameColumn;
    @FXML
    TableColumn<ClassFindDto, String> teacherEmailColumn;
    @FXML
    Label teachersCountLabel;

    Timeline toastTimeline;

    List<UserFindDto> listSelectedTeachers = new ArrayList<>();

    List<UserFindDto> listSelectedStudents = new ArrayList<>();

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> Objects.equals(auth.getAuthority(), "ROLE_ADMIN"));
    }

    private void hideToast() {
        if (toastNotification == null) return;

        Timeline fadeOut = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(toastNotification.opacityProperty(), 1)),
                new KeyFrame(Duration.millis(300), new KeyValue(toastNotification.opacityProperty(), 0))
        );
        fadeOut.setOnFinished(e -> {
            toastNotification.setVisible(false);
            toastNotification.setManaged(false);
        });
        fadeOut.play();
    }

    private void showToast(String icon, String msg, String color) {
        toastIcon.setText(icon);
        toastMessage.setText(msg);
        toastNotification.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 8;");
        toastNotification.setVisible(true);
        toastNotification.setManaged(true);

        toastNotification.setOpacity(0);
        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(toastNotification.opacityProperty(), 0)),
                new KeyFrame(Duration.millis(300), new KeyValue(toastNotification.opacityProperty(), 1))
        );
        fadeIn.play();

        if (toastTimeline != null) {
            toastTimeline.stop();
        }
        toastTimeline = new Timeline(
                new KeyFrame(Duration.seconds(3), event -> hideToast())
        );
        toastTimeline.play();
    }

    private void showSuccessToast(String msg) {
        showToast("✅", msg, "#2ecc71");
    }

    private void createTeacher(UserCreateDto userDto) {
        if (isAdmin()) {
            userService.createTeacher(userDto);
            showSuccessToast("Учитель " +
                    userDto.getSecondName() +
                    userDto.getFirstname() +
                    userDto.getMiddleName() +
                    " успешно создан!"
            );
            return;
        }
        throw new RuntimeException("Недостаточно прав для выполнения данной операции.");
    }

    private UserCreateDto buildeUserCreateDto() {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setSecondName(secondNameField.getText());
        userCreateDto.setFirstname(firstNameField.getText());
        userCreateDto.setMiddleName(middleNameField.getText());
        userCreateDto.setEmail(emailField.getText());
        userCreateDto.setPhoneNumber(phoneNumberField.getText());
        userCreateDto.setPassword(passwordField.getText());
        return userCreateDto;
    }

    private List<UserFindDto> getAllTeacher() {
        if (isAdmin()) {
            return userService.getAlTeacher();
        }
        return null;
    }

    private List<UserFindDto> getAllStudents() {
        if (isAdmin()) {
            return userService.getAllStudents();
        }
        return null;
    }

    private void setupColumnTeacherTable() {
        secondNameColumn.setCellValueFactory(cellData -> {
            UserFindDto userFindDto = cellData.getValue();
            String secondName = userFindDto.getSecondName();
            return new SimpleObjectProperty<>(secondName);
        });

        firstNameColumn.setCellValueFactory(cellData -> {
            UserFindDto userFindDto = cellData.getValue();
            String firstName = userFindDto.getFirstName();
            return new SimpleObjectProperty<>(firstName);
        });

        middleNameColumn.setCellValueFactory(cellData -> {
            UserFindDto userFindDto = cellData.getValue();
            String middleName = userFindDto.getMiddleName();
            return new SimpleObjectProperty<>(middleName);
        });

        emailColumn.setCellValueFactory(cellData -> {
            UserFindDto userFindDto = cellData.getValue();
            String email = userFindDto.getEmail();
            return new SimpleObjectProperty<>(email);
        });

        phoneNumberColumn.setCellValueFactory(cellData -> {
            UserFindDto userFindDto = cellData.getValue();
            String phoneNumber = userFindDto.getPhoneNumber();
            return new SimpleObjectProperty<>(phoneNumber);
        });
    }

    private void setupColumnSubjects() {
        subjectNameColumn.setCellValueFactory(cellData -> {
            SubjectFindDto subjectDto = cellData.getValue();
            String nameSubject = subjectDto.getNameSubject();
            return new SimpleObjectProperty<>(nameSubject);
        });

        teacherNameColumn.setCellValueFactory(cellData -> {
            SubjectFindDto subjectDto = cellData.getValue();
            List<UserFindDto> userDto = subjectDto.getTeachers();
            return new SimpleObjectProperty<>(userDto);
        });
    }

    private List<UserFindDto> getAllStudent() {
        return userService.getAllStudents();
    }

    @FXML
    public void initialize() {
        setupColumnTeacherTable();
        List<UserFindDto> teacherList = getAllTeacher();
        List<UserFindDto> studentList = getAllStudent();
        ObservableList<UserFindDto> observableListFindTeachers = FXCollections.observableArrayList(teacherList);
        teachersTable.setItems(observableListFindTeachers);

        teacherList.forEach(element -> teacherComboBox.getItems().add(element));
        setupColumnSubjects();
        List<SubjectFindDto> subjectsList = subjectService.getAllSubject();
        ObservableList<SubjectFindDto> observableList = FXCollections.observableArrayList(subjectsList);
        subjectsTable.setItems(observableList);

        teacherList.forEach(element -> classTeacherComboBox.getItems().add(element));
        studentList.forEach(element -> classStudentComboBox.getItems().add(element));

        classNumberColumn.setCellValueFactory(cellData -> {
            ClassFindDto value = cellData.getValue();
            Integer lvlClass = value.getLvlClass();
            return new SimpleObjectProperty<>(String.valueOf(lvlClass));
        });

        classNameColumn.setCellValueFactory(cellData -> {
            ClassFindDto classFindDto = cellData.getValue();
            String nameClass = classFindDto.getNameClass();
            return new SimpleObjectProperty<>(nameClass);
        });

        List<ClassFindDto> allClass = getAllClass();
        ObservableList<ClassFindDto> observableClassList = FXCollections.observableArrayList(allClass);
        classesTable.setItems(observableClassList);
    }

    @FXML
    public void handleCreateTeacher() {
        createTeacher(buildeUserCreateDto());
    }

    @FXML
    public void handleRefreshTable() {
        setupColumnTeacherTable();
        List<UserFindDto> teacherList = getAllTeacher();
        ObservableList<UserFindDto> observableListFindTeachers = FXCollections.observableArrayList(teacherList);
        teachersTable.setItems(observableListFindTeachers);
    }

    @FXML
    public void handleClearFields() {
        firstNameField.clear();
        secondNameField.clear();
        middleNameField.clear();
        emailField.clear();
        phoneNumberField.clear();
        passwordField.clear();
    }

    private void createSubject(SubjectCreateDto subjectDto) {
        subjectService.createSubject(subjectDto);
    }

    @FXML
    public void handleCreateSubject() {
        SubjectCreateDto subjectCreateDto = new SubjectCreateDto();
        subjectCreateDto.setNameSubject(subjectNameField.getText());
        subjectCreateDto.setEmailTeacher(teacherComboBox.getValue().getEmail());
        createSubject(subjectCreateDto);
    }

    @FXML
    public void handleClearSubjectFields() {
        subjectNameField.clear();
    }

    @FXML
    public void handleRefreshSubjects() {
        setupColumnSubjects();
        List<SubjectFindDto> subjects = subjectService.getAllSubject();
        ObservableList<SubjectFindDto> observableList = FXCollections.observableArrayList(subjects);
        subjectsTable.setItems(observableList);
    }

    @FXML
    public void handleClearClassFields() {
        classNameField.clear();
        classLevelField.clear();
    }

    @FXML
    public void addStudentToClass() {
        UserFindDto selectStudent = classStudentComboBox.getValue();
        if (listSelectedStudents.contains(selectStudent)) {
            return;
        }
        listSelectedStudents.add(selectStudent);
        selectedStudentNameColumn.setCellValueFactory(cellData -> {
            UserFindDto student = classStudentComboBox.getValue();
            String username = student.getSecondName() + " " + student.getFirstName();
            return new SimpleObjectProperty<>(username);
        });
        selectedStudentEmailColumn.setCellValueFactory(cellData -> {
            UserFindDto student = classStudentComboBox.getValue();
            String emailStudent = student.getEmail();
            return new SimpleObjectProperty<>(emailStudent);
        });
        ObservableList<UserFindDto> observableStudentList = FXCollections.observableArrayList(this.listSelectedStudents);
        selectedStudentsTable.setItems(observableStudentList);
    }

    @FXML
    public void addTeacherToClass() {
        UserFindDto selectTeacher = classTeacherComboBox.getValue();
        if (listSelectedTeachers.contains(selectTeacher)) {
            return;
        }
        this.listSelectedTeachers.add(selectTeacher);
        selectedTeacherNameColumn.setCellValueFactory(cellData -> {
            UserFindDto teacher = cellData.getValue();
            String username = teacher.getSecondName() + " " + teacher.getFirstName();
            return new SimpleObjectProperty<>(username);
        });
        selectedTeacherEmailColumn.setCellValueFactory(cellData -> {
            UserFindDto teacher = cellData.getValue();
            String emailTeacher = teacher.getEmail();
            return new SimpleObjectProperty<>(emailTeacher);
        });
        ObservableList<UserFindDto> observableTeacherList = FXCollections.observableArrayList(this.listSelectedTeachers);
        selectedTeachersTable.setItems(observableTeacherList);
    }

    private List<String> getEmailUsers(List<UserFindDto> users) {
        return users
                .stream()
                .map(UserFindDto::getEmail)
                .toList();
    }

    private List<ClassFindDto> getAllClass() {
        return classService.getAllClass();
    }

    @FXML
    public void handleCreateClass() {
        if (!listSelectedStudents.isEmpty()
                && !listSelectedTeachers.isEmpty()
                && !classLevelField.getText().isEmpty()
                && !classNameField.getText().isEmpty()) {
            ClassCreateDto createDto = new ClassCreateDto();
            createDto.setNameClass(classNameField.getText());
            createDto.setLvlClass(Integer.parseInt(classLevelField.getText()));
            List<String> emailStudents = this.getEmailUsers(listSelectedStudents);
            List<String> emailTeachers = this.getEmailUsers(listSelectedTeachers);
            createDto.setEmailStudentList(emailStudents);
            createDto.setEmailTeacherList(emailTeachers);
            classService.createClass(createDto);
        } else {
            throw new RuntimeException("Класс не удалось создать.");
        }
    }

    @FXML
    public void handleRefreshClasses() {
        List<ClassFindDto> allClasses = getAllClass();
        classNumberColumn.setCellValueFactory(cellData -> {
            ClassFindDto classFindDto = cellData.getValue();
            Integer lvlClass = classFindDto.getLvlClass();
            return new SimpleObjectProperty<>(String.valueOf(lvlClass));
        });

        ObservableList<ClassFindDto> observableList = FXCollections.observableArrayList(allClasses);
        classesTable.setItems(observableList);
    }

    @FXML
    public void handleLogout() {
        SecurityContextHolder.clearContext();
        WindowApplication.loginPage();
    }
}
