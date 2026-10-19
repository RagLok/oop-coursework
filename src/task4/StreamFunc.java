package task4;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamFunc {

    private static final String PREFIX = "_new_";

    private StreamFunc()
    {
    }

    public static double avg(List<Integer> values)
    {
        return values.stream()
                .collect(Collectors.averagingInt(Integer::intValue));
    }

    public static List<String> upperCaseWithPrefix(List<String> lines)
    {
        return lines.stream()
                .map(String::toUpperCase)
                .map(PREFIX::concat)
                .collect(Collectors.toList());
    }

    // Квадраты считаются в long, чтобы не было переполнения int
    public static List<Long> sqrOfUniq(List<Integer> values)
    {
        Map<Integer, Long> frequency = values.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return values.stream()
                .filter(v -> frequency.get(v) == 1L)
                .map(v -> (long) v * v)
                .collect(Collectors.toList());
    }

    public static <E> E last(Collection<E> items)
    {
        return items.stream()
                .skip(Math.max(0, items.size() - 1))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Коллекция пуста - последнего элемента нет"));
    }

    // Сумма считается в long, чтобы не было переполнения int
    public static long sumOfEven(int[] values)
    {
        return IntStream.of(values)
                .filter(v -> (v & 1) == 0)
                .asLongStream()
                .sum();
    }

    public static Map<Character, String> toAlphaMap(List<String> lines)
    {
        return lines.stream()
                .filter(l -> !l.isEmpty())
                .collect(Collectors.toMap(
                        l -> l.charAt(0),
                        l -> l.substring(1),
                        (prev, l) -> l,
                        TreeMap::new));
    }
}
