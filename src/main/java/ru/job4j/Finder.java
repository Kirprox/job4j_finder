package ru.job4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Finder {
    public static void main(String[] args) {
        ArgsName argsName = ArgsName.of(args);
        Path source = new File(argsName.get("d")).toPath();
        File output = new File(argsName.get("o"));
        String value = argsName.get("n");
        String typeOfFind = argsName.get("t");
        validateInput(source, value, typeOfFind);
        List<Path> results = Search.search(source, typeOfFind, value);
        System.out.println(results);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))) {
            for (Path path : results) {
                writer.write(path.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void validateInput(Path input, String find,
                                      String typeOfFind) {
        List<String> types = List.of("mask", "name", "regex");
        if (!types.contains(typeOfFind)) {
            throw new IllegalArgumentException("illegal type of find");
        }
        if (!input.toFile().exists()) {
            throw new IllegalArgumentException("illegal input parameter!");
        }
        if (find == null || find.isEmpty()) {
            throw new IllegalArgumentException("illegal name argument!");
        }
        if (typeOfFind.equals("mask") && !find.contains("*") || !find.contains("?")) {
            System.out.println("Warning! Mask does not contain * or ?");
        }
        if (typeOfFind.equals("regex")) {
            try {
                Pattern.compile(find);
            } catch (PatternSyntaxException e) {
                throw new IllegalArgumentException("illegal regex expression!");
            }
        }
    }
}
