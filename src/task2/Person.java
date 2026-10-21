package task2;

public class Person {
    private final String name;
    private final Point location;

    // приватный конструктор создать можно только через рефлексию
    private Person(String name, Point location) {
        this.name = name;
        this.location = location;
    }

    @Override
    public String toString() {
        return "Person(" + name + ", " + location + ")";
    }
}
