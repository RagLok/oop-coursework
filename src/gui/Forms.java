package gui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

final class Forms {

    private Forms() {
    }

    static GridPane grid() {
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        ColumnConstraints label = new ColumnConstraints();
        ColumnConstraints field = new ColumnConstraints();
        field.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(label, field);
        return grid;
    }

    static void row(GridPane grid, int row, String label, Node field, Node... buttons) {
        grid.add(new Label(label), 0, row);
        grid.add(field, 1, row);
        for (int i = 0; i < buttons.length; i++)
            grid.add(buttons[i], 2 + i, row);
    }

    static TitledPane section(String title, Node content) {
        TitledPane pane = new TitledPane(title, content);
        pane.setCollapsible(false);
        return pane;
    }
}
