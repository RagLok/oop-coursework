package task4;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// Разбор входных данных для методов StreamFunc из строки, введённой пользователем
public class InputParser {

    // Элементы разделяются запятыми и/или пробелами
    private static final Pattern NUMBER_SEPARATOR = Pattern.compile("[,;\\s]+");
    private static final Pattern STRING_SEPARATOR = Pattern.compile(",");

    private InputParser() {
    }

    public static List<Integer> parseIntegers(String text) {
        return Arrays.stream(NUMBER_SEPARATOR.split(text.trim()))
                .filter(s -> !s.isEmpty())
                .map(InputParser::parseInt)
                .collect(Collectors.toList());
    }

    public static int[] parseIntArray(String text) {
        return parseIntegers(text).stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }

    // Строки разделяются запятыми, пробелы по краям обрезаются. Пустая строка - пустой список
    public static List<String> parseStrings(String text) {
        if (text.isBlank())
            return List.of();
        return Arrays.stream(STRING_SEPARATOR.split(text, -1))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    private static Integer parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("\"" + s + "\" не является целым числом");
        }
    }
}
