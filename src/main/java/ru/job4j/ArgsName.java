package ru.job4j;

import java.util.HashMap;
import java.util.Map;

public class ArgsName {

    private final Map<String, String> values = new HashMap<>();

    public String get(String key) {
        if (!values.containsKey(key)) {
            throw new IllegalArgumentException(String.format("This key: '%s' is missing", key));
        }
        return values.get(key);
    }

    private void parse(String[] args) {
        String key, value;

        for (var str : args) {
            String[] arguments = validateAndParseArguments(str);
            key = arguments[0].substring(1);
            value = arguments[1];
            values.put(key, value);
        }
    }

    private static String[] validateAndParseArguments(String str) {
        String[] arguments = str.split("=", 2);
        String textErr = "Error: This argument '";
        if (arguments.length < 2) {
            textErr = String.format(
                    "%s%s' does not contain an equal sign", textErr, arguments[0]);
            throw new IllegalArgumentException(String.format(textErr));
        }
        if (arguments[0].length() <= 1) {
            textErr = String.format(
                    "%s%s=%s' does not contain a key", textErr, arguments[0], arguments[1]);
            throw new IllegalArgumentException(String.format(textErr));
        }
        if (!arguments[0].startsWith("-")) {
            textErr = String.format(
                    "%s%s=%s' does not start with a '-' character", textErr, arguments[0], arguments[1]);
            throw new IllegalArgumentException(String.format(textErr));
        }
        if (arguments[1].isEmpty()) {
            textErr = String.format(
                    "%s%s=%s' does not contain a value", textErr, arguments[0], arguments[1]);
            throw new IllegalArgumentException(String.format(textErr));
        }
        return arguments;
    }

    public static ArgsName of(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Arguments not passed to program");
        }
        ArgsName names = new ArgsName();
        names.parse(args);
        return names;
    }
}