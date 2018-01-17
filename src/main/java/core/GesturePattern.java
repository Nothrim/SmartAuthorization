package core;

import java.util.LinkedList;
import java.util.List;

public class GesturePattern extends LinkedList<Point> {
    private final int range;

    public GesturePattern(List<Point> in, int range) {
        this.range = range;
        this.addAll(in);
    }

    public boolean fit(List<Point> that) {
        if (this.size() != that.size()) return false;
        int index = -1;
        for (Point point : this) {
            index++;
            if (!searchForward(that, point, index, 3, range)) {
                return false;
            }
        }
        return true;
    }

    private boolean searchForward(List<Point> listToSearch, Point pointToFind, int index, int limit, int range) {
        if (listToSearch.size() - 1 < index || limit <= 0) return false;
        if (!listToSearch.get(index).near(pointToFind, range))
            searchForward(listToSearch, pointToFind, index + 1, limit - 1, range);
        return listToSearch.get(index).near(pointToFind, range);
    }
}
