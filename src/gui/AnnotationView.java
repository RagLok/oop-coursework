package gui;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import task2.AnnotatedClass;
import task2.ArgumentFactory;
import task2.MethodInvoker;
import task2.Repeat;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class AnnotationView extends TaskView {

    // Типы, значения которых пользователь задаёт вручную, и способ их разбора
    private final Map<Class<?>, Function<String, Object>> parsers = new LinkedHashMap<>();
    private final Map<Class<?>, TextField> fields = new LinkedHashMap<>();
    private final ArgumentFactory factory = new ArgumentFactory();

    public AnnotationView() {
        parsers.put(int.class, s -> Integer.parseInt(s.trim()));
        parsers.put(long.class, s -> Long.parseLong(s.trim()));
        parsers.put(double.class, s -> Double.parseDouble(s.trim()));
        parsers.put(boolean.class, AnnotationView::parseBoolean);
        parsers.put(char.class, AnnotationView::parseChar);
        parsers.put(String.class, s -> s);
    }

    @Override
    public String getTitle() {
        return "2. Аннотации и рефлексия";
    }

    @Override
    public String getDescription() {
        return "Вызываются все protected и private методы, отмеченные @Repeat(n), ровно n раз. "
                + "Аргументы подбираются по типам параметров, массивы заполняются значениями типа элементов.";
    }

    @Override
    protected Node createInput() {
        GridPane grid = Forms.grid();
        int row = 0;
        for (Class<?> type : parsers.keySet())
        {
            TextField field = new TextField(String.valueOf(factory.getValue(type)));
            field.setPrefColumnCount(10);
            fields.put(type, field);
            Forms.row(grid, row++, type.getSimpleName() + ":", field);
        }

        Button run = new Button("Вызвать аннотированные методы");
        run.setDefaultButton(true);
        run.setOnAction(e -> invoke());
        grid.add(run, 0, row, 2, 1);

        TableView<Method> table = createMethodsTable();
        HBox.setHgrow(table, Priority.ALWAYS);

        HBox box = new HBox(12,
                Forms.section("Значения аргументов по типам", grid),
                Forms.section("Методы класса AnnotatedClass", table));

        HBox.setHgrow(box.getChildren().get(1), Priority.ALWAYS);

        return box;
    }

    private TableView<Method> createMethodsTable() {
        TableView<Method> table = new TableView<>();

        table.getColumns().add(column("Метод", 230, MethodInvoker::signature));

        table.getColumns().add(column("Модификатор", 100, m -> Modifier.toString(m.getModifiers())));

        table.getColumns().add(column("@Repeat", 70, m -> {
            Repeat repeat = m.getAnnotation(Repeat.class);
            return repeat == null ? "-" : String.valueOf(repeat.value());
        }));

        table.getColumns().add(column("Вызывается", 90, m -> MethodInvoker.isTarget(m) ? "да" : "нет"));
        table.getItems().setAll(MethodInvoker.sortedMethods(AnnotatedClass.class));
        table.setPrefHeight(250);
        return table;
    }

    private static TableColumn<Method, String> column(String title, double width, Function<Method, String> value)
    {
        TableColumn<Method, String> column = new TableColumn<>(title);

        column.setPrefWidth(width);
        column.setSortable(false);
        column.setCellValueFactory(cell -> new ReadOnlyStringWrapper(value.apply(cell.getValue())));

        return column;
    }

    private void invoke() {
        for (Map.Entry<Class<?>, TextField> entry : fields.entrySet())
        {
            Class<?> type = entry.getKey();
            String text = entry.getValue().getText();

            try
            {
                factory.setValue(type, parsers.get(type).apply(text));
            } catch (IllegalArgumentException e)
            {
                println("Ошибка: неверное значение для типа " + type.getSimpleName() + ": \"" + text + "\"");
                return;
            }
        }

        println("=== Вызов аннотированных методов ===");

        AnnotatedClass target = new AnnotatedClass(this::println);

        new MethodInvoker(factory, this::println).invokeAnnotated(target);
    }

    private static Object parseBoolean(String s) {
        return switch (s.trim().toLowerCase()) {
            case "true", "да", "1" -> true;
            case "false", "нет", "0" -> false;
            default -> throw new IllegalArgumentException(s);
        };
    }

    private static Object parseChar(String s) {
        if (s.length() != 1)
            throw new IllegalArgumentException(s);
        return s.charAt(0);
    }
}
