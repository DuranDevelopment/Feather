package cc.ddev.feather.database;

import cc.ddev.feather.logger.Log;
import net.minestom.server.MinecraftServer;

import java.sql.SQLException;

public class DataManager {
    public void initialize() {
        StormDatabase stormHikari = new StormDatabase();

        try {
            stormHikari.init();
        } catch (SQLException exception) {
            exception.printStackTrace();
            Log.getLogger().error("Failed to connect to the database.");
            MinecraftServer.stopCleanly();
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }
}
