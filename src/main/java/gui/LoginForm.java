package gui;

import api.LoginControlerImpl;
import api.LoginController;
import crypto.HardwareId;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import json.LoginData;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginForm implements Initializable, Controller {
    @FXML
    Button registerButton;
    @FXML
    Button loginButton;
    @FXML
    TextField login;
    @FXML
    PasswordField password;
    LoginController loginController = new LoginControlerImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registerButton.setOnAction(action -> {
            try {
                Main.switchScene("Register", Main.FXML.REGISTER);
            } catch (IOException e) {
                System.err.println("Cannot go to registration right now");
            }
        });
        loginButton.setOnAction(action -> login());
    }

    private void login() {
        try {
            login.setStyle("");
            password.setStyle("");
            if (loginController.checkCredentials(login.getText(), password.getText())) {
                LoginData d = LoginData.load();
                d.setHardwareHash(HardwareId.getId());
                d.save();
                Main.switchScene("Welcome after normal login", Main.FXML.WELCOME);
            } else {
                login.setStyle("-fx-background-color: red;");
                password.setStyle("-fx-background-color: red;");
            }
        } catch (IOException e) {
            System.err.println("Cannot access login data");
        }
    }

    @Override
    public void setup(Stage stage) {
        stage.getScene().setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER:
                    login();
                    break;
            }
        });
    }
}
