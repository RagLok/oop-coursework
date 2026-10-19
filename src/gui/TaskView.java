package gui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

// Базовый класс экрана задания: заголовок для списка, описание и область вывода
public abstract class TaskView {

    // Область вывода защищена от редактирования
    protected final TextArea output = new TextArea();
    private Node view;

    protected TaskView() {
        output.setEditable(false);
        output.setWrapText(true);
        output.getStyleClass().add("output");
    }

    public abstract String getTitle();

    public abstract String getDescription();

    // Панель ввода входных данных задания
    protected abstract Node createInput();

    // Экран создаётся один раз, при первом выборе задания
    public Node getView() {
        if (view == null) {
            BorderPane pane = new BorderPane();
            pane.setTop(createInput());
            pane.setCenter(createOutput());
            BorderPane.setMargin(pane.getCenter(), new Insets(10, 0, 0, 0));
            view = pane;
        }
        return view;
    }

    protected void println(String line) {
        output.appendText(line + System.lineSeparator());
    }

    private Node createOutput() {
        Label title = new Label("Вывод");
        title.getStyleClass().add("section-title");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button clear = new Button("Очистить");
        clear.setOnAction(e -> output.clear());
        HBox header = new HBox(8, title, spacer, clear);

        VBox box = new VBox(6, header, output);
        VBox.setVgrow(output, Priority.ALWAYS);
        return box;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
