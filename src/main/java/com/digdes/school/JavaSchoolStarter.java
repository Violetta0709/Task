package com.digdes.school;


import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;
        import java.util.stream.Collectors;

public class JavaSchoolStarter {

    private final List<Map<String, Object>> data;

    public JavaSchoolStarter() {
        data = new ArrayList<>();
    }

    public List<Map<String, Object>> executeCommand(String command) {
        String[] commandParts = command.split(" ");
        String operation = commandParts[0];

        switch (operation) {
            case "insert":
                return insert(commandParts);
            case "update":
                return update(commandParts);
            case "delete":
                return delete(commandParts);
            case "find":
                return find(commandParts);
            default:
                throw new IllegalArgumentException("Invalid command");
        }
    }

    private List<Map<String, Object>> insert(String[] commandParts) {
        Map<String, Object> row = new HashMap<>();
        for (int i = 1; i < commandParts.length; i += 2) {
            row.put(commandParts[i], parseValue(commandParts[i + 1]));
        }
        data.add(row);
        return List.of(row);
    }

    private List<Map<String, Object>> update(String[] commandParts) {
        String columnName = commandParts[1];
        Object value = parseValue(commandParts[2]);
        String whereColumnName = commandParts[4];
        Object whereValue = parseValue(commandParts[5]);

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> row : data) {
            if (row.get(whereColumnName).equals(whereValue)) {
                row.put(columnName, value);
                result.add(row);
            }
        }
        return result;
    }

    private List<Map<String, Object>> delete(String[] commandParts) {
        String whereColumnName = commandParts[2];
        Object whereValue = parseValue(commandParts[3]);

        List<Map<String, Object>> result = new ArrayList<>();
        data.removeIf(row -> {
            if (row.get(whereColumnName).equals(whereValue)) {
                result.add(row);
                return true;
            }
            return false;
        });
        return result;
    }

    private List<Map<String, Object>> find(String[] commandParts) {
        String whereColumnName = commandParts[1];
        Object whereValue = parseValue(commandParts[2]);

        return data.stream()
                .filter(row -> row.get(whereColumnName).equals(whereValue))
                .collect(Collectors.toList());
    }

    private Object parseValue(String value) {
        if (value.equals("true") || value.equals("false")) {
            return Boolean.parseBoolean(value);
        } else if (value.contains(".")) {
            return Double.parseDouble(value);
        } else {
            return Long.parseLong(value);
        }
    }
}
