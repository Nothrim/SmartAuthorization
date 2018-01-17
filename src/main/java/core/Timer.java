package core;

import java.util.Calendar;

public class Timer {
    public static boolean hourIsEven() {
        return Calendar.getInstance().get(Calendar.MINUTE) % 2 == 0;
    }
}
