package gui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import task4.InputParser;
import task4.StreamFunc;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.function.Function;

// Задание 4: методы на Stream API с вводом входных данных
public class StreamView extends TaskView {

    private int row = 0;

    @Override
    public String getTitle() {
        return "4. Stream API";
    }

    @Override
    public String getDescription() {
        return "Числа разделяются пробелами или запятыми, строки - запятыми. "
                + "Каждый метод выполняется отдельной кнопкой.";
    }

    @Override
    protected Node createInput() {
        GridPane grid = Forms.grid();
        method(grid, "Среднее значение (целые числа):", "7, 3, 9, 3, -2, 10, 7, 4", text -> {
            List<Integer> values = InputParser.parseIntegers(text);
            return String.format(Locale.ROOT, "%.3f", StreamFunc.avg(values));
        });
        method(grid, "Верхний регистр + \"_new_\" (строки):", "java, stream, lambda",
                text -> StreamFunc.upperCaseWithPrefix(InputParser.parseStrings(text)));
        method(grid, "Квадраты уникальных (целые числа):", "7, 3, 9, 3, -2, 10, 7, 4",
                text -> StreamFunc.sqrOfUniq(InputParser.parseIntegers(text)));
        method(grid, "Последний элемент (строки):", "first, second, third",
                text -> StreamFunc.last(InputParser.parseStrings(text)));
        method(grid, "Сумма чётных (массив целых):", "11 -4 8 15 0 6",
                text -> StreamFunc.sumOfEven(InputParser.parseIntArray(text)));
        method(grid, "Map: первый символ -> остаток (строки):", "java, stream, lambda, jvm",
                text -> StreamFunc.toAlphaMap(InputParser.parseStrings(text)));
        return Forms.section("Входные данные методов", grid);
    }

    // Строка формы: поле ввода и кнопка, выполняющая метод и выводящая результат
    private void method(GridPane grid, String label, String example, Function<String, Object> action) {
        TextField field = new TextField(example);
        Button run = new Button("Выполнить");
        Runnable execute = () -> {
            String name = label.substring(0, label.indexOf('(')).trim();
            try {
                println(name + " [" + field.getText() + "] -> " + action.apply(field.getText()));
            } catch (NoSuchElementException | IllegalArgumentException e) {
                println(name + " [" + field.getText() + "] -> исключение: " + e.getMessage());
            }
        };
        run.setOnAction(e -> execute.run());
        field.setOnAction(e -> execute.run());
        Forms.row(grid, row++, label, field, run);
    }
}
