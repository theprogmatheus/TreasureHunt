package com.github.theprogmatheus.devroom.treasurehunt.database;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class SqlQueryLoader {

    private final String tablePrefix;
    private final Map<String, String> cachedQueries;

    public SqlQueryLoader(String tablePrefix) {
        this.tablePrefix = tablePrefix;
        this.cachedQueries = new HashMap<>();
    }

    public List<String> getQueries(String queryPath) {
        String query = getQuery(queryPath);
        if (query != null)
            return Arrays.stream(query.split(";"))
                    .map(String::trim)
                    .filter(str -> !str.isEmpty())
                    .collect(Collectors.toList());
        return List.of();
    }

    public String getQuery(String queryPath) {
        return this.cachedQueries
                .computeIfAbsent(queryPath, key -> loadQuery(queryPath));
    }

    private String loadQuery(String queryPath) {
        InputStream resource = getClass().getResourceAsStream("/sql/%s.sql".formatted(queryPath));
        if (resource == null)
            throw new IllegalArgumentException("The query '%s' not found.".formatted(queryPath));


        StringBuilder stringBuilder = new StringBuilder();
        try (Scanner scanner = new Scanner(new InputStreamReader(resource, StandardCharsets.UTF_8))) {
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine()).append(System.lineSeparator());
            }
        }

        return stringBuilder.toString()
                .replace("%table_prefix%", this.tablePrefix)
                .trim();
    }


    public String getTablePrefix() {
        return tablePrefix;
    }

    public Map<String, String> getCachedQueries() {
        return cachedQueries;
    }
}
