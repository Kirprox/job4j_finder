package ru.job4j;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Search {
    public static List<Path> search(Path root, String type, String value) {
        Predicate<Path> condition = getCondition(type, value);
        SearchFiles searcher = new SearchFiles(condition);
        try {
            Files.walkFileTree(root, searcher);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searcher.getPaths();
    }

    private static Predicate<Path> getCondition(String type, String value) {
        Predicate<Path> result = null;
        if ("name".equals(type)) {
            result = path -> path.getFileName().toString().equals(value);
        }
        if ("regex".equals(type)) {
            Pattern pattern = Pattern.compile(value);
            result = path -> pattern.matcher(path.getFileName().toString()).matches();
        }
        if ("mask".equals(type)) {
            PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + value);
            result = path -> matcher.matches(path.getFileName());
        }
        return result;
    }
}
