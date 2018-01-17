package gui;

import api.LoginControlerImpl;
import api.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main extends Application {
    public static final Color ODD_GESTURES_COLOUR = Color.CORNFLOWERBLUE;
    public static final Color EVEN_GESTURES_COLOUR = Color.CORAL;
    public static final FXMLLoader loader = new FXMLLoader();
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 720;
    private static Main instance;
    private Stage primaryStage;
    public static final Path dataPath = Paths.get("data.json");

    public static class FXML {
        public static final String REGISTER_GESTURE = "gesture_register.fxml";
        public static final String WELCOME = "main_screen.fxml";
        public static final String MAIN = "sample.fxml";
        public static final String REGISTER = "register.fxml";
        public static final String LOGIN_GESTURE = "gesture_login.fxml";
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LoginController login = new LoginControlerImpl();
        instance = this;
        this.primaryStage = primaryStage;
        try {
            if (login.checkHardwareId()) {
                switchScene("Log in using gestures", FXML.LOGIN_GESTURE);
            } else {
                switchScene("Are you new user?", FXML.MAIN);
            }
        } catch (Exception e) {
            System.err.println("Cannot load login data");
            switchScene("Are you new user?", FXML.MAIN);
        }


    }
    public static void switchScene(String title, String fxml) throws IOException {
        loader.setLocation(instance.getClass().getClassLoader().getResource(fxml));
        instance.primaryStage.setTitle(title);
        loader.setController(null);
        loader.setRoot(null);
        instance.primaryStage.setScene(new Scene(loader.load(), WIDTH, HEIGHT));
        instance.primaryStage.getScene().getStylesheets().add("flatterfx.css");
        instance.primaryStage.show();
        ((Controller) loader.getController()).setup(instance.primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
