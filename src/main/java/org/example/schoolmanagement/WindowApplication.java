package org.example.schoolmanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class WindowApplication extends Application {
    static Stage primaryStage;
    @Setter
    static ApplicationContext applicationContext;

    private static void loadScene(String pathLoginPage, String titlePage, int minWidth, int minHeight) {
        try {
            FXMLLoader loader = new FXMLLoader(WindowApplication.class.getResource(pathLoginPage));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();

            primaryStage.hide();
            primaryStage.setMaximized(true);
            primaryStage.setScene(new Scene(root, minWidth, minHeight));
            primaryStage.setTitle(titlePage);
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loginPage() {
        loadScene("/fxml_code/login.fxml", "School Management", 700, 700);
    }

    public static void adminPage() {
        loadScene("/fxml_code/admin.fxml", "School Management", 700, 700);
    }

    public static void registerPage() {
        loadScene("/fxml_code/register.fxml", "School Management", 700, 700);
    }

    public static void studentPage() {
        loadScene("/fxml_code/student.fxml", "School Management", 700, 700);
    }

    public static void teacherPage() {

    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        loginPage();
    }
}
