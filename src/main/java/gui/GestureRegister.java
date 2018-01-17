package gui;

import core.Point;
import crypto.HardwareId;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import json.LoginData;

import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static core.Point.normalize;
import static gui.Main.EVEN_GESTURES_COLOUR;
import static gui.Main.dataPath;

public class GestureRegister implements Initializable, Controller {
    @FXML
    Canvas canvas;
    @FXML
    AnchorPane pane;
    @FXML
    Slider threshold;
    @FXML
    Label thresholdCount;
    @FXML
    Button clear;
    @FXML
    Button next;
    @FXML
    Label info;
    @FXML
    Label messages;
    private boolean capture = false;
    private Stage stage;
    private int range = 5;
    private List<Point> positions = new ArrayList<>();
    LoginData loginData;
    int step = 0;
    private GraphicsContext sb;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loginData = Files.exists(dataPath) ? LoginData.load() : new LoginData();
        } catch (IOException e) {
            System.err.println("Cannot read user data empty data node will be created");
            loginData = new LoginData();
        }
        range = (int) threshold.getValue();
        sb = canvas.getGraphicsContext2D();
        try {
            Bounds bounds = pane.getLayoutBounds();
            new Robot().mouseMove((int) bounds.getMinX(), (int) bounds.getMinY());
        } catch (AWTException e) {
            e.printStackTrace();
        }
        fillCanvas(EVEN_GESTURES_COLOUR);
        next.setOnAction(action -> accept());
        canvas.setOnMouseMoved(event -> {
            if (capture) {
                sb.setFill(Color.BLANCHEDALMOND);
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
        threshold.valueProperty().addListener((observable, oldValue, newValue) -> {
            range = newValue.intValue();
            thresholdCount.setText(newValue.intValue() + "");
            clear(sb, positions);
        });
        clear.setOnAction(action -> {
            messages.setText("Gesture cleared");
            clear(sb, positions);
        });
    }

    private void accept() {
        System.out.println(step);
        if (step == 2) {
            try {
                loginData.setHardwareHash(HardwareId.getId());
            } catch (UnsupportedEncodingException e) {
                System.err.println("Cannot generate hardware id");
                loginData.setHardwareHash("");
            }
            try {
                loginData.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Main.switchScene("Welcome", Main.FXML.WELCOME);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Couldn't complete registration");
            }
        }
    }

    private void clear(GraphicsContext sb, List positions) {
        sb.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        fillCanvas(step < 2 ? Main.EVEN_GESTURES_COLOUR : Main.ODD_GESTURES_COLOUR);
        if (step > 0) {
            if (step == 1) {
                info.setText("Even hours gesture");
            }
            step--;
        }
        positions.clear();
        next.setVisible(false);
    }


    private void nextStep() {
        if (step == 0) {
            loginData.setGestureEven(normalize(positions, range));
            loginData.setRangeEven(range);
            info.setText("Odd hours gesture");
            clear(sb, positions);
            fillCanvas(Main.ODD_GESTURES_COLOUR);
            loginData.setCanvasWidthEven(canvas.getWidth());
            loginData.setCanvasHeightEven(canvas.getHeight());
            step++;
        } else if (step == 1) {
            loginData.setGestureOdd(normalize(positions, range));
            loginData.setRangeOdd(range);
            loginData.setCanvasWidthOdd(canvas.getWidth());
            loginData.setCanvasHeightOdd(canvas.getHeight());
            next.setVisible(true);
            step++;
        }
    }

    @Override
    public void setup(Stage stage) {
        this.stage = stage;
        Scene scene = stage.getScene();
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.CONTROL) {
                capture = true;
            }
            if (event.getCode() == KeyCode.ENTER) {
                accept();
            }
        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.CONTROL) {
                capture = false;
                if (positions.size() > 2) {
                    messages.setText("Gesture saved");
                    nextStep();
                } else {
                    messages.setText("Gesture is to short");
                    clear(sb, positions);
                }
            }
        });
        canvas.setHeight(stage.getHeight());
        canvas.setWidth(stage.getWidth());
        stage.widthProperty().addListener((observable, oldValue, newValue) -> canvas.setWidth(newValue.intValue()));
        stage.heightProperty().addListener((observable, oldValue, newValue) -> canvas.setHeight(newValue.intValue()));
    }

    private void fillCanvas(Color color) {
        Paint p = sb.getFill();
        sb.setFill(color);
        sb.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        sb.setFill(p);
    }
}
