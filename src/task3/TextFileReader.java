package task3;

import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

// Чтение файла с текстом для перевода
public class TextFileReader {

    private TextFileReader() {
    }

    public static String read(Path path) throws FileReadException {
        if (!Files.exists(path))
        {
            throw new FileReadException("файл не найден: " + path.toAbsolutePath());
        }
        if (Files.isDirectory(path))
        {
            throw new FileReadException("указанный путь является каталогом: " + path.toAbsolutePath());
        }
        if (!Files.isReadable(path))
        {
            throw new FileReadException("нет доступа на чтение файла: " + path.toAbsolutePath());
        }

        try
        {
            String text = Files.readString(path, StandardCharsets.UTF_8);
            return text.startsWith("﻿") ? text.substring(1) : text; // UTF-8 BOM
        } catch (NoSuchFileException e)
        {
            throw new FileReadException("файл не найден: " + path.toAbsolutePath(), e);
        } catch (AccessDeniedException e)
        {
            throw new FileReadException("нет доступа на чтение файла: " + path.toAbsolutePath(), e);
        } catch (MalformedInputException e)
        {
            throw new FileReadException("файл должен быть в кодировке UTF-8: " + path.toAbsolutePath(), e);
        } catch (IOException e)
        {
            throw new FileReadException("ошибка ввода-вывода: " + e.getMessage(), e);
        }
    }
}
