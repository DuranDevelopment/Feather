package cc.ddev.feather;

import cc.ddev.feather.api.config.Config;
import cc.ddev.feather.commands.TestCommand;
import cc.ddev.feather.configuration.ConfigManager;
import cc.ddev.feather.database.DatabaseConnection;
import cc.ddev.feather.logger.Log;
import de.leonhard.storage.Toml;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        // Initialization
        MinecraftServer minecraftServer = MinecraftServer.init();
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        // Create the instance
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        // Load the configuration file
        ConfigManager configManager = ConfigManager.init();
        Toml featherConfig = configManager.getFeatherConfig();
        // Make the database connection
        DatabaseConnection database = new DatabaseConnection();
        try {
            database.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Log.getLogger().info("Feather successfully connected to database!");
        // Set the ChunkGenerator
        instanceContainer.setGenerator(unit ->
                unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));
        // Add an event callback to specify the spawning instance (and the spawn position)
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            // Set the spawn position
            player.setRespawnPoint(Config.SPAWN);
        });
        // Start the server on port 25565
        minecraftServer.start("0.0.0.0", 25565);
        // Register commands
        MinecraftServer.getCommandManager().register(new TestCommand());

        Log.getLogger().info("Feather has started!");
    }
}