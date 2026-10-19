package task1;

public class WalkMove implements IMovable {
    @Override
    public String getName() {
        return "Пешком";
    }

    @Override
    public String move() {
        return "идёт пешком";
    }

    @Override
    public double getSpeed() {
        return 5;
    }

    @Override
    public String toString() {
        return getName();
    }
}
