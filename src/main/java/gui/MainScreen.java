package gui;

import api.LoginControlerImpl;
import api.LoginController;
import com.sun.glass.ui.Application;
import core.TimerFlippedListener;
import core.TimerThread;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import json.LoginData;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

import static gui.Main.switchScene;

public class MainScreen implements Initializable, Controller, TimerFlippedListener {
    @FXML
    Label welcomeString;
    @FXML
    Label evenOdd;
    @FXML
    Button logout;
    @FXML
    Button register;
    @FXML
    Button close;

    @Override
    public void setup(Stage stage) {

    }
    LoginController login = new LoginControlerImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            LoginData d = LoginData.load();
//            welcomeString.setText("Welcome " + d.getLogin() + "Last logged in:" + d.getLastLoginDate() != null ? d.getLastLoginDate().toString() : "not available" + " hardware id:" + d.getHardwareHash());
        } catch (Exception e) {
            e.printStackTrace();
        }
        TimerThread t = new TimerThread(1);
        t.addListener(this);
        t.start();
        logout.setOnAction(action -> {
            t.terminate();
            try {
                if (login.checkHardwareId()) {
                    switchScene("Log in using gestures", Main.FXML.LOGIN_GESTURE);
                } else {
                    switchScene("Are you new user?", Main.FXML.MAIN);
                }
            } catch (IOException e) {
                System.err.println("Cannot find log in panel");
                ;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        });

        register.setOnAction(action -> {
            t.terminate();
            try {
                switchScene("Register screen", Main.FXML.REGISTER);
            } catch (IOException e) {
                System.err.println("Cannot find register panel");
                ;
            }
        });
        close.setOnAction(action -> {
            System.exit(1);
        });

    }

    @Override
    public void flipped(boolean hourIsEven) {
//        Application.invokeLater(() -> evenOdd.setText(hourIsEven ? "Time is even right now" : "Time is odd right now"));
    }
}
