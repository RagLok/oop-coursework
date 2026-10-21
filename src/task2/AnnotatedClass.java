package task2;

import java.util.Arrays;
import java.util.function.Consumer;

public class AnnotatedClass {

    // Куда методы пишут свой вывод (в GUI - текстовая область)
    private final Consumer<String> out;

    public AnnotatedClass(Consumer<String> out) {
        this.out = out;
    }

    public void publicHello(String name) {
        out.accept("  publicHello: Привет, " + name);
    }

    @Repeat(2)
    public void publicSay(String text) {
        out.accept("  publicSay: " + text);
    }

    public void publicSayGoodbye() {
        out.accept("  publicSayGoodbye: Пока!");
    }


    @Repeat(3)
    protected double protectedMultiply(int a, double b) {
        out.accept("  protectedMultiply: " + a + " * " + b + " = " + (a * b));
        return a * b;
    }

    @Repeat(2)
    protected void protectedGreet(String text, int count, boolean loud) {
        String msg = text.repeat(Math.max(0, count));
        out.accept("  protectedGreet: " + (loud ? msg.toUpperCase() : msg));
    }

    @Repeat(2)
    protected Point protectedMove(Point point, int dx) {
        Point moved = point.move(dx, dx);
        out.accept("  protectedMove: " + point + " -> " + moved);
        return moved;
    }

    protected void protectedNotAnnotated(long x) {
        out.accept("  protectedNotAnnotated: " + x);
    }


    @Repeat(2)
    private String privateSecret(String code) {
        out.accept("  privateSecret: код = " + code);
        return "secret-" + code;
    }

    @Repeat(1)
    private void privateManyParams(char c, String[] texts, int[] numbers) {
        out.accept("  privateManyParams: c=" + c
                + ", texts=" + Arrays.toString(texts)
                + ", numbers=" + Arrays.toString(numbers));
    }

    @Repeat(2)
    private void privateIntroduce(Person person, Color color, Point[] points) {
        out.accept("  privateIntroduce: " + person + ", цвет=" + color
                + ", points=" + Arrays.toString(points));
    }

    private void privateNotAnnotated(int x) {
        out.accept("  privateNotAnnotated: " + x);
    }
}
