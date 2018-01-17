package gui;

import com.google.common.hash.Hashing;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import json.LoginData;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class Register implements Controller, Initializable {
    @FXML
    TextField login;
    @FXML
    PasswordField password;
    @FXML
    PasswordField repeatPassword;
    @FXML
    Button next;

    @Override
    public void setup(Stage stage) {
        stage.getScene().setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ENTER:
                    register();
                    break;
            }
        });
    }

    public boolean validateFields() {
        boolean correct = true;
        if (login.getText().isEmpty()) {
            correct = false;
            login.setStyle("-fx-background-color: red;");
        } else {
            login.setStyle("");
        }
        if (password.getText().isEmpty() || !password.getText().equals(repeatPassword.getText())) {
            correct = false;
            password.setStyle("-fx-background-color: red;");
            repeatPassword.setStyle("-fx-background-color: red;");
        } else {
            password.setStyle("");
            repeatPassword.setStyle("");
        }
        return correct;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        next.setOnAction(action -> register());
    }

    private void register() {
        if (validateFields()) {
            try {
                LoginData d = new LoginData();
                d.setLogin(login.getText());
                d.setPassword(Hashing.sha256().hashString(password.getCharacters(), StandardCharsets.UTF_8).toString());
                d.save();
                Main.switchScene("Set up gesture login", Main.FXML.REGISTER_GESTURE);
            } catch (IOException e) {
                System.err.println("Cannot switch screen right now");
            }
        }
    }
}
