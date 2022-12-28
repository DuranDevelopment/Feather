package cc.ddev.feather.database;

import cc.ddev.feather.api.config.Config;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.SQLException;

public class DatabaseConnection {
    private final String HOST;
    private final int PORT;
    private final String DATABASE;
    private final String USERNAME;
    private final String PASSWORD;

    private HikariDataSource hikari;

    public DatabaseConnection() {
        HOST = Config.DATABASE_HOST;
        PORT = Config.DATABASE_PORT;
        DATABASE = Config.DATABASE_NAME;
        USERNAME = Config.DATABASE_USERNAME;
        PASSWORD = Config.DATABASE_PASSWORD;
    }

    public void connect() throws SQLException {
        hikari = new HikariDataSource();
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", HOST);
        hikari.addDataSourceProperty("port", PORT);
        hikari.addDataSourceProperty("databaseName", DATABASE);
        hikari.addDataSourceProperty("user", USERNAME);
        hikari.addDataSourceProperty("password", PASSWORD);
    }

    public boolean isConnected() { return hikari != null; }

    public HikariDataSource getHikari() { return hikari; }

    public void disconnect() {
        if (!isConnected()) {
            hikari.close();
        }
    }
}