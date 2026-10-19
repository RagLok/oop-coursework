package app;

import gui.AnnotationView;
import gui.HeroView;
import gui.StreamView;
import gui.TaskView;
import gui.TranslatorView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class MainApp extends Application {

    private final Label title = new Label();
    private final Label description = new Label();
    private final BorderPane content = new BorderPane();

    @Override
    public void start(Stage stage) {
        ListView<TaskView> tasks = new ListView<>();

        tasks.getItems()
                .addAll(
                        new HeroView(),
                        new AnnotationView(),
                        new TranslatorView(),
                        new StreamView());

        tasks.setPrefWidth(250);

        tasks.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, old, task) -> show(task));

        title.getStyleClass().add("task-title");

        description.setWrapText(true);

        VBox header = new VBox(4, title, description);

        header.setPadding(new Insets(0, 0, 10, 0));

        content.setTop(header);
        content.setPadding(new Insets(12));

        BorderPane root = new BorderPane();
        root.setLeft(tasks);
        root.setCenter(content);

        Scene scene = new Scene(root, 1150, 720);
        scene
                .getStylesheets()
                .add(Objects.requireNonNull(
                        getClass().getResource("/style.css")).toExternalForm()
                );

        stage.setTitle("Курсовая работа ООП - задания 1-4");

        stage.setScene(scene);
        stage.show();

        tasks.getSelectionModel().selectFirst();
    }

    private void show(TaskView task) {
        title.setText(task.getTitle());
        description.setText(task.getDescription());
        content.setCenter(task.getView());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
