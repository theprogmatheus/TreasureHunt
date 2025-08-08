package com.github.theprogmatheus.devroom.treasurehunt.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseManager {

    private final String hostname;
    private final String database;
    private final String username;
    private final String password;
    private final String tablePrefix;
    private final SqlQueryLoader sqlQueryLoader;

    private HikariDataSource dataSource;

    public DatabaseManager(String hostname, String database, String username, String password, String tablePrefix) {
        this.hostname = hostname;
        this.database = database;
        this.username = username;
        this.password = password;
        this.sqlQueryLoader = new SqlQueryLoader(this.tablePrefix = tablePrefix);
    }


    public void init() {
        try {
            this.dataSource = new HikariDataSource(buildHikariConfig());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void terminate() {
        try {
            this.dataSource.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    private HikariConfig buildHikariConfig() {
        HikariConfig config = new HikariConfig();

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(3);
        config.setDriverClassName(getDriverClassName());
        config.setJdbcUrl("jdbc:mysql://%s/%s".formatted(this.hostname, this.database));
        config.setUsername(this.username);
        config.setPassword(this.password);

        return config;
    }

    private String getDriverClassName() {
        try {
            Class<?> driverClass = Class.forName("com.mysql.cj.jdbc.Driver");
            return driverClass.getName();
        } catch (Exception ignored) {
            return "com.mysql.jdbc.Driver";
        }
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }

    public String getHostname() {
        return hostname;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public SqlQueryLoader getSqlQueryLoader() {
        return sqlQueryLoader;
    }
}
