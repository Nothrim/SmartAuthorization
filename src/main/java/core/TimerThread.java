package core;

import java.util.ArrayList;
import java.util.List;

public class TimerThread extends Thread {
    private final int sleepInMilis;
    private boolean run = true;
    private boolean isEven;
    private final List<TimerFlippedListener> listeners = new ArrayList<>();

    public TimerThread(int sleepInMilis) {
        this.sleepInMilis = sleepInMilis;
    }

    public void addListener(TimerFlippedListener listener) {
        listeners.add(listener);
    }

    public void terminate() {
        run = false;
    }

    @Override
    public void run() {
        super.run();
        listeners.forEach(e->e.flipped(Timer.hourIsEven()));
        while (run) {
            if (isEven != Timer.hourIsEven()) {
                isEven = Timer.hourIsEven();
                listeners.forEach(e -> e.flipped(isEven));
            }
            try {
                Thread.sleep(sleepInMilis);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
