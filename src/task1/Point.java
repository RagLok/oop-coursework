package task1;

import java.util.Locale;

public record Point(double x, double y) {

    public double distanceTo(Point other) {
        return Math.hypot(other.x - x, other.y - y);
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(%.1f; %.1f)", x, y);
    }
}
