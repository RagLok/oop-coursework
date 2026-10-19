package task1;

import java.util.Locale;

public class Hero {
    private final String name;
    private IMovable moveStrategy;
    private Point position;

    public Hero(String name)
    {
        this(name, new WalkMove());
    }

    public Hero(String name, IMovable moveStrategy)
    {
        this(name, moveStrategy, new Point(0, 0));
    }

    public Hero(String name, IMovable moveStrategy, Point position)
    {
        this.name = name;
        this.moveStrategy = moveStrategy;
        this.position = position;
    }

    public void changeMovable(IMovable moveStrategy)
    {
        this.moveStrategy = moveStrategy;
    }

    public IMovable getMovable() {
        return moveStrategy;
    }

    // Перемещение героя из текущей точки в точку to выбранным способом
    public String move(Point to) {
        Point from = position;
        double distance = from.distanceTo(to);
        double hours = distance / moveStrategy.getSpeed();
        position = to;
        return String.format(Locale.ROOT, "%s %s из %s в %s: расстояние %.2f км, время %.2f ч",
                name, moveStrategy.move(), from, to, distance, hours);
    }

    public Point getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
