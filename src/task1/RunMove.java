package task1;

public class RunMove implements IMovable {
    @Override
    public String getName() {
        return "Бегом";
    }

    @Override
    public String move() {
        return "бежит";
    }

    @Override
    public double getSpeed() {
        return 12;
    }

    @Override
    public String toString() {
        return getName();
    }
}
