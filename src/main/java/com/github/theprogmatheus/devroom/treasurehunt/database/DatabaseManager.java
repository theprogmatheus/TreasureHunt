package com.github.theprogmatheus.devroom.treasurehunt.database;

import com.github.theprogmatheus.devroom.treasurehunt.TreasureHunt;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseManager {

    private final boolean debug;
    private final String hostname;
    private final String database;
    private final String username;
    private final String password;
    private final String tablePrefix;
    private final SqlQueryLoader sqlQueryLoader;

    private HikariDataSource dataSource;

    public DatabaseManager(boolean debug, String hostname, String database, String username, String password, String tablePrefix) {
        this.debug = debug;
        this.hostname = hostname;
        this.database = database;
        this.username = username;
        this.password = password;
        this.sqlQueryLoader = new SqlQueryLoader(this.tablePrefix = tablePrefix);
    }


    public boolean init() {
        try {
            this.dataSource = new HikariDataSource(buildHikariConfig());
            return true;
        } catch (Exception exception) {
            TreasureHunt.getInstance().getLogger().warning("Unable to start a database connection, check your MySQL settings in the \"config.yml\" file.");
            if (debug)
                exception.printStackTrace();
            return false;
        }
    }

    public void terminate() {
        try {
            this.dataSource.close();
        } catch (Exception exception) {
            if (debug)
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
