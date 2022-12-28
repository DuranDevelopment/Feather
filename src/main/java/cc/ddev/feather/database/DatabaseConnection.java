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
        HOST = Config.Database.HOST;
        PORT = Config.Database.PORT;
        DATABASE = Config.Database.NAME;
        USERNAME = Config.Database.USERNAME;
        PASSWORD = Config.Database.PASSWORD;
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