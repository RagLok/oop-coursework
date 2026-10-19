package gui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import task3.Dictionary;
import task3.FileReadException;
import task3.InvalidFileFormatException;
import task3.TextFileReader;
import task3.Translator;

import java.io.File;
import java.nio.file.Path;

// Задание 3: переводчик по словарю из файла
public class TranslatorView extends TaskView {

    private static final String DEFAULT_DICTIONARY = "dictionary.txt";

    private final TextField dictionaryPath = new TextField(DEFAULT_DICTIONARY);
    private final Label dictionaryState = new Label("Словарь не загружен");
    private final TextArea input = new TextArea("dog look to the window, dog look forward");
    private Translator translator;

    @Override
    public String getTitle() {
        return "3. Переводчик";
    }

    @Override
    public String getDescription() {
        return "Перевод текста по словарю (строка файла: слово или выражение | перевод). Регистр игнорируется, "
                + "неизвестные слова не переводятся, из нескольких вариантов выбирается самое длинное выражение.";
    }

    @Override
    protected Node createInput() {
        Button chooseDictionary = new Button("Выбрать...");
        chooseDictionary.setOnAction(e -> {
            File file = chooseFile("Файл словаря");
            if (file != null) {
                dictionaryPath.setText(file.getPath());
                loadDictionary();
            }
        });
        Button loadDictionary = new Button("Загрузить");
        loadDictionary.setOnAction(e -> loadDictionary());

        GridPane grid = Forms.grid();
        Forms.row(grid, 0, "Файл словаря:", dictionaryPath, chooseDictionary, loadDictionary);
        grid.add(dictionaryState, 1, 1);

        input.setWrapText(true);
        input.setPrefRowCount(6);
        Button openText = new Button("Открыть файл с текстом...");
        openText.setOnAction(e -> openTextFile());
        Button translate = new Button("Перевести");
        translate.setDefaultButton(true);
        translate.setOnAction(e -> translate());

        VBox text = new VBox(6, input, new HBox(8, openText, translate));

        loadDictionary();
        return new VBox(10,
                Forms.section("Словарь", grid),
                Forms.section("Текст для перевода (можно ввести вручную или загрузить из файла)", text));
    }

    private void loadDictionary() {
        Path path = Path.of(dictionaryPath.getText().trim());
        try {
            Dictionary dictionary = Dictionary.load(path);
            translator = new Translator(dictionary);
            dictionaryState.setText("Загружено записей: " + dictionary.size());
            println("Словарь " + path.toAbsolutePath() + " загружен - записей: " + dictionary.size());
        } catch (FileReadException e) {
            failDictionary("Ошибка чтения словаря: " + e.getMessage());
        } catch (InvalidFileFormatException e) {
            failDictionary("Ошибка формата словаря: " + e.getMessage());
        }
    }

    private void failDictionary(String message) {
        translator = null;
        dictionaryState.setText("Словарь не загружен");
        println(message);
    }

    private void openTextFile() {
        File file = chooseFile("Файл с текстом");
        if (file == null)
            return;
        try {
            input.setText(TextFileReader.read(file.toPath()));
            println("Текст загружен из файла " + file.getPath());
        } catch (FileReadException e) {
            println("Ошибка чтения текста: " + e.getMessage());
        }
    }

    private void translate() {
        if (translator == null) {
            println("Сначала загрузите словарь");
            return;
        }
        println("--- Перевод ---");
        println(translator.translate(input.getText()));
    }

    private File chooseFile(String title) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.setInitialDirectory(new File(".").getAbsoluteFile().getParentFile());
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Текстовые файлы (*.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("Все файлы", "*.*"));
        return chooser.showOpenDialog(input.getScene().getWindow());
    }
}
