package task1;

public class FlyMove implements IMovable {
    @Override
    public String getName() {
        return "По воздуху";
    }

    @Override
    public String move() {
        return "летит";
    }

    @Override
    public double getSpeed() {
        return 60;
    }

    @Override
    public String toString() {
        return getName();
    }
}
