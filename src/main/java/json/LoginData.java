package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.Point;
import gui.Main;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;

import static gui.Main.dataPath;

;

public class LoginData {
    public static LoginData load() throws IOException {
        return new Gson().fromJson(new String(Files.readAllBytes(Main.dataPath), "UTF-8"), LoginData.class);
    }

    public void save() throws IOException {
        Files.write(dataPath, new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create().toJson(this).getBytes());
    }

    List<Point> gestureOdd;
    List<Point> gestureEven;
    int rangeOdd;
    int rangeEven;
    String hardwareHash;
    String login;
    String password;
    Date lastLoginDate;
    double canvasWidthEven;
    double canvasHeightEven;
    double canvasWidthOdd;
    double canvasHeightOdd;


    public double getCanvasWidthEven() {
        return canvasWidthEven;
    }

    public void setCanvasWidthEven(double canvasWidthEven) {
        this.canvasWidthEven = canvasWidthEven;
    }

    public double getCanvasHeightEven() {
        return canvasHeightEven;
    }

    public void setCanvasHeightEven(double canvasHeightEven) {
        this.canvasHeightEven = canvasHeightEven;
    }

    public double getCanvasWidthOdd() {
        return canvasWidthOdd;
    }

    public void setCanvasWidthOdd(double canvasWidthOdd) {
        this.canvasWidthOdd = canvasWidthOdd;
    }

    public double getCanvasHeightOdd() {
        return canvasHeightOdd;
    }

    public void setCanvasHeightOdd(double canvasHeightOdd) {
        this.canvasHeightOdd = canvasHeightOdd;
    }

    public void setCanvasHeightOdd(int canvasHeightOdd) {
        this.canvasHeightOdd = canvasHeightOdd;
    }

    public LoginData() {
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public List<Point> getGestureOdd() {
        return gestureOdd;
    }

    public void setGestureOdd(List<Point> gestureOdd) {
        this.gestureOdd = gestureOdd;
    }

    public List<Point> getGestureEven() {
        return gestureEven;
    }

    public void setGestureEven(List<Point> gestureEven) {
        this.gestureEven = gestureEven;
    }

    public int getRangeOdd() {
        return rangeOdd;
    }

    public void setRangeOdd(int rangeOdd) {
        this.rangeOdd = rangeOdd;
    }

    public int getRangeEven() {
        return rangeEven;
    }

    public void setRangeEven(int rangeEven) {
        this.rangeEven = rangeEven;
    }

    public String getHardwareHash() {
        return hardwareHash;
    }

    public void setHardwareHash(String hardwareHash) {
        this.hardwareHash = hardwareHash;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
