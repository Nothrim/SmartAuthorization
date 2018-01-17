package gui;

import core.GesturePattern;
import core.Point;
import core.TimerFlippedListener;
import core.TimerThread;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import json.LoginData;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GestureLogin implements Initializable, Controller, TimerFlippedListener {
    @FXML
    Canvas canvas;
    @FXML
    AnchorPane pane;
    private LoginData loginData;
    private boolean capture = false;
    int range;
    private TimerThread timer;
    private List<Point> positions = new ArrayList<>();
    private Stage stage;
    GraphicsContext sb;
    List<Point> pattern;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loginData = LoginData.load();
            if (loginData.getCanvasHeightEven() < 100) Main.switchScene("Invalid user data", Main.FXML.REGISTER);
        } catch (IOException e) {
            try {
                Main.switchScene("No login data try registering", Main.FXML.REGISTER);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        sb = canvas.getGraphicsContext2D();
        canvas.setOnMouseMoved(event -> {
            if (capture) {
                sb.setFill(Color.RED);
                sb.fillOval(event.getX(), event.getY(), range, range);
                if (positions.size() > 0) {
                    Point next = new Point((int) event.getX(), (int) event.getY());
                    Point current = positions.get(positions.size() - 1);
                    if (!current.near(next, range)) {
                        positions.add(next);
                    }
                } else {
                    positions.add(new Point((int) event.getX(), (int) event.getY()));
                }
            }
        });
        timer = new TimerThread(1000);
        timer.addListener(this);
        timer.start();
    }


    private void clear(GraphicsContext sb, List positions) {
        if (sb != null) {
            sb.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }
        if (positions != null)
            positions.clear();
    }

    @Override
    public void setup(Stage stage) {
        this.stage = stage;
        Scene scene = stage.getScene();
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.CONTROL) {
                capture = true;
            }
        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.CONTROL) {
                capture = false;
                if (positions.size() > 0) {
                    if (new GesturePattern(pattern, range).fit(Point.normalize(positions, range))) {
                        try {
                            Main.switchScene("Hello", Main.FXML.WELCOME);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Main.switchScene("Login", Main.FXML.LOGIN_GESTURE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });
    }

    @Override
    public void flipped(boolean hourIsEven) {
        clear(sb, positions);
        Paint p;
        if (hourIsEven) {
            if (canvas != null) {
                canvas.setWidth(loginData.getCanvasWidthEven());
                canvas.setHeight(loginData.getCanvasHeightEven());
            }
            p = sb.getFill();
            sb.setFill(Main.EVEN_GESTURES_COLOUR);
            sb.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            sb.setFill(p);
            range = loginData.getRangeEven();
            pattern = loginData.getGestureEven();
        } else {
            if (canvas != null) {
                canvas.setWidth(loginData.getCanvasWidthOdd());
                canvas.setHeight(loginData.getCanvasHeightOdd());
            }
            p = sb.getFill();
            sb.setFill(Main.ODD_GESTURES_COLOUR);
            sb.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            sb.setFill(p);
            range = loginData.getRangeOdd();
            pattern = loginData.getGestureOdd();
        }
    }
}
