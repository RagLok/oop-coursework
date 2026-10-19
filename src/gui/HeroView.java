package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import task1.FlyMove;
import task1.Hero;
import task1.HorseMove;
import task1.IMovable;
import task1.Point;
import task1.RunMove;
import task1.WalkMove;

import java.math.BigDecimal;
import java.util.Locale;

public class HeroView extends TaskView {

    private final ObservableList<Hero> heroes = FXCollections.observableArrayList(
            new Hero("Герой 1"),
            new Hero("Герой 2", new RunMove()));
    private final ObservableList<IMovable> movables = FXCollections.observableArrayList(
            new WalkMove(),
            new RunMove(),
            new HorseMove(),
            new FlyMove());

    private final ComboBox<Hero> heroBox = new ComboBox<>(heroes);
    private final ComboBox<IMovable> moveBox = new ComboBox<>(movables);
    private final TextField newHeroName = new TextField();
    private final Spinner<Double> targetX = coordinateSpinner();
    private final Spinner<Double> targetY = coordinateSpinner();
    private final Label state = new Label();

    @Override
    public String getTitle() {
        return "1. Стратегия перемещения героя";
    }

    @Override
    public String getDescription() {
        return "Герой перемещается между двумя точками выбранным способом. "
                + "Способ перемещения (стратегию) можно менять во время работы программы.";
    }

    @Override
    protected Node createInput() {
        newHeroName.setPromptText("Имя героя");
        Button addHero = new Button("Добавить героя");
        addHero.setOnAction(e -> addHero());

        Button change = new Button("Сменить способ");
        change.setOnAction(e -> changeStrategy());

        Button move = new Button("Переместить");
        move.setDefaultButton(true);
        move.setOnAction(e -> moveHero());
        HBox target = new HBox(6, new Label("X"), targetX, new Label("Y"), targetY);
        target.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        heroBox.setMaxWidth(Double.MAX_VALUE);
        moveBox.setMaxWidth(Double.MAX_VALUE);
        heroBox.getSelectionModel().selectedItemProperty().addListener((obs, old, hero) -> updateState());
        heroBox.getSelectionModel().selectFirst();

        GridPane grid = Forms.grid();
        Forms.row(grid, 0, "Новый герой:", newHeroName, addHero);
        Forms.row(grid, 1, "Герой:", heroBox);
        Forms.row(grid, 2, "Способ перемещения:", moveBox, change);
        Forms.row(grid, 3, "Точка назначения:", target, move);
        grid.add(state, 0, 4, 3, 1);
        return Forms.section("Входные данные", grid);
    }

    private void addHero() {
        String name = newHeroName.getText().trim();
        if (name.isEmpty()) {
            println("Ошибка: введите имя героя");
            return;
        }
        Hero hero = new Hero(name, moveBox.getValue());
        heroes.add(hero);
        heroBox.getSelectionModel().select(hero);
        newHeroName.clear();
        println("Добавлен герой " + name + " (" + hero.getMovable().getName() + ")");
    }

    private void changeStrategy() {
        Hero hero = heroBox.getValue();
        IMovable movable = moveBox.getValue();
        hero.changeMovable(movable);
        println(hero.getName() + ": способ перемещения изменён на \"" + movable.getName() + "\"");
        updateState();
    }

    private void moveHero() {
        Double x = readCoordinate(targetX, "X");
        Double y = readCoordinate(targetY, "Y");
        if (x == null || y == null)
            return;
        Hero hero = heroBox.getValue();
        println(hero.move(new Point(x, y)));
        updateState();
    }

    // Значение из поля спиннера, даже если ввод ещё не подтверждён (null - неверный ввод)
    private Double readCoordinate(Spinner<Double> spinner, String axis) {
        String text = spinner.getEditor().getText();
        if (parseNumber(text) == null) {
            println("Ошибка: координата " + axis + " должна быть числом, введено \"" + text + "\"");
            spinner.getEditor().setText(spinner.getValueFactory().getConverter().toString(spinner.getValue()));
            return null;
        }
        spinner.commitValue();
        return spinner.getValue();
    }

    // Принимаются и точка, и запятая в качестве разделителя
    private static Double parseNumber(String text) {
        try {
            return Double.valueOf(text.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void updateState() {
        Hero hero = heroBox.getValue();
        if (hero == null)
            return;
        state.setText(String.format(Locale.ROOT, "Сейчас: %s в точке %s, способ - %s (%.0f км/ч)",
                hero.getName(), hero.getPosition(), hero.getMovable().getName(), hero.getMovable().getSpeed()));
        // в списке выбирается стратегия того же класса, что у героя
        movables.stream()
                .filter(m -> m.getClass() == hero.getMovable().getClass())
                .findFirst()
                .ifPresent(moveBox::setValue);
    }

    private static Spinner<Double> coordinateSpinner() {
        Spinner<Double> spinner = new Spinner<>(-1000.0, 1000.0, 0.0, 1.0);
        spinner.getValueFactory().setConverter(new StringConverter<>() {
            @Override
            public String toString(Double value) {
                return value == null ? "" : BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
            }

            // при неверном вводе (например, при потере фокуса) остаётся прежнее значение
            @Override
            public Double fromString(String text) {
                Double value = parseNumber(text);
                return value == null ? spinner.getValue() : value;
            }
        });
        spinner.setEditable(true);
        spinner.setPrefWidth(100);
        return spinner;
    }
}
