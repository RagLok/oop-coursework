package task3;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translator {

    private static final Pattern WORD = Pattern.compile("[\\p{L}\\p{N}]+(?:['-][\\p{L}\\p{N}]+)*");

    private final Dictionary dictionary;

    public Translator(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public String translate(String text)
    {
        List<Token> words = new ArrayList<>();
        Matcher matcher = WORD.matcher(text);
        while (matcher.find())
        {
            words.add(new Token(matcher.start(), matcher.end()));
        }

        StringBuilder result = new StringBuilder();
        int position = 0;
        int i = 0;
        while (i < words.size())
        {
            Token first = words.get(i);
            result.append(text, position, first.start());

            int matchedCount = 0;
            String translation = null;

            // Перебор от самого длинного выражения к самому короткому
            int maxCount = Math.min(dictionary.getMaxPhraseLength(), words.size() - i);

            for (int count = maxCount; count >= 1; count--)
            {
                if (!onlyWhitespaceBetween(text, words, i, count))
                {
                    continue;
                }

                String candidate = text.substring(first.start(), words.get(i + count - 1).end());
                translation = dictionary.get(candidate);

                if (translation != null)
                {
                    matchedCount = count;
                    break;
                }
            }

            if (translation != null)
            {
                result.append(translation);
                position = words.get(i + matchedCount - 1).end();
                i += matchedCount;
            } else
            {
                result.append(text, first.start(), first.end());
                position = first.end();
                i++;
            }
        }

        result.append(text.substring(position));
        return result.toString();
    }

    // Выражение не должно пересекать знаки препинания между словами допускаются только пробелы
    private static boolean onlyWhitespaceBetween(String text, List<Token> words, int from, int count)
    {
        for (int k = from; k < from + count - 1; k++)
        {
            if (!text.substring(words.get(k).end(), words.get(k + 1).start()).isBlank())
            {
                return false;
            }
        }
        return true;
    }

    private record Token(int start, int end)
    {
    }
}
