package task3;

import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Dictionary {

    private static final String SEPARATOR = "|";

    private final Map<String, String> entries = new HashMap<>();
    private int maxPhraseLength = 0;

    public static Dictionary load(Path path) throws FileReadException, InvalidFileFormatException {
        if (!Files.exists(path))
        {
            throw new FileReadException("файл словаря не найден: " + path.toAbsolutePath());
        }
        if (Files.isDirectory(path))
        {
            throw new FileReadException("указанный путь является каталогом: " + path.toAbsolutePath());
        }
        if (!Files.isReadable(path))
        {
            throw new FileReadException("нет доступа на чтение файла: " + path.toAbsolutePath());
        }

        List<String> lines;
        try
        {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (NoSuchFileException e)
        {
            throw new FileReadException("файл словаря не найден: " + path.toAbsolutePath(), e);
        } catch (AccessDeniedException e)
        {
            throw new FileReadException("нет доступа на чтение файла: " + path.toAbsolutePath(), e);
        } catch (MalformedInputException e)
        {
            throw new FileReadException("файл словаря должен быть в кодировке UTF-8: " + path.toAbsolutePath(), e);
        } catch (IOException e)
        {
            throw new FileReadException("ошибка ввода-вывода: " + e.getMessage(), e);
        }

        Dictionary dictionary = new Dictionary();
        for (int i = 0; i < lines.size(); i++)
        {
            String line = lines.get(i);
            if (i == 0 && line.startsWith("﻿"))
            {
                line = line.substring(1); // UTF-8 BOM
            }
            if (line.isBlank())
            {
                continue;
            }
            dictionary.parseLine(line, i + 1);
        }
        return dictionary;
    }

    private void parseLine(String line, int lineNumber) throws InvalidFileFormatException {
        int separatorIndex = line.indexOf(SEPARATOR);
        if (separatorIndex < 0 || separatorIndex != line.lastIndexOf(SEPARATOR))
        {
            throw new InvalidFileFormatException(lineNumber, line);
        }

        String source = line.substring(0, separatorIndex).trim();
        String translation = line.substring(separatorIndex + 1).trim();
        if (source.isEmpty() || translation.isEmpty())
        {
            throw new InvalidFileFormatException(lineNumber, line);
        }

        put(source, translation);
    }

    public void put(String source, String translation) {
        String key = normalize(source);
        entries.put(key, translation);
        maxPhraseLength = Math.max(maxPhraseLength, key.split(" ").length);
    }

    //Возвращает перевод выражения или {@code null}, если его нет в словаре
    public String get(String phrase) {
        return entries.get(normalize(phrase));
    }

    // Максимальное количество слов в левой части среди всех записей
    public int getMaxPhraseLength() {
        return maxPhraseLength;
    }

    public int size() {
        return entries.size();
    }

    private static String normalize(String phrase) {
        return phrase.trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }
}
