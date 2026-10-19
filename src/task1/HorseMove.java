package task1;

public class HorseMove implements IMovable {
    @Override
    public String getName() {
        return "На лошади";
    }

    @Override
    public String move() {
        return "скачет на лошади";
    }

    @Override
    public double getSpeed() {
        return 30;
    }

    @Override
    public String toString() {
        return getName();
    }
}
