package core;

import java.util.ArrayList;
import java.util.List;

public class Point {
    private final int x;
    private final int y;

    @Override
    public String toString() {
        return x + ":" + y;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean near(Point that, int range) {
        return Math.sqrt((that.x - this.x) * (that.x - this.x) + (that.y - this.y) * (that.y - this.y)) < range;
    }

    public static List<Point> normalize(List<Point> p, int range) {
        List<Point> out = new ArrayList<>();
        if (p.size() > 0) {
            Point prev = p.get(0);
            final int shiftX = prev.x;
            final int shiftY = prev.y;
            out.add(new Point(p.get(0).x - shiftX, p.get(0).y - shiftY));
            for (int i = 0; i < p.size(); i++) {
                for (int j = i; j < p.size(); j++) {
                    if (!p.get(i).near(p.get(j), range)) {
                        out.add(new Point(p.get(j).x - shiftX, p.get(j).y - shiftY));
                        i = j;
                        break;
                    }
                }
            }
        }
        return out;
    }
}
