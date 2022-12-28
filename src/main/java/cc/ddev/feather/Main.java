package cc.ddev.feather;

import cc.ddev.feather.api.config.Config;
import cc.ddev.feather.commands.TestCommand;
import cc.ddev.feather.configuration.ConfigManager;
import cc.ddev.feather.database.DatabaseConnection;
import cc.ddev.feather.logger.Log;
import cc.ddev.feather.models.MinetopiaPlayer;
import cc.ddev.feather.sidebar.SidebarManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;

import java.sql.SQLException;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        // Initialization
        MinecraftServer minecraftServer = MinecraftServer.init();
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        // Create the instance
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        // Load the configuration file
        ConfigManager configManager = ConfigManager.init();
        configManager.createConfigDirectory();
        // Make the database connection
        DatabaseConnection database = new DatabaseConnection();
        try {
            database.connect();
            Log.getLogger().info("Feather successfully connected to database!");
        } catch (SQLException e) {
            Log.getLogger().error("Feather couldn't connect to database:");
            e.printStackTrace();
        }
        // Set the ChunkGenerator
        instanceContainer.setGenerator(unit ->
                unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));
        // Set the UUID provider
        MinecraftServer.getConnectionManager().setUuidProvider((playerConnection, username) -> {
            // This method will be called at players connection to set their UUID
            return UUID.fromString(MinetopiaPlayer.getUUID(username)); /* Set here your custom UUID registration system */
        });

        // Add an event callback to specify the spawning instance (and the spawn position)
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            // Set the spawn position
            player.setRespawnPoint(Config.SPAWN);
            Log.getLogger().info("UUID of player " + player.getUsername() + " is " + player.getUuid());
        });
        globalEventHandler.addListener(PlayerSpawnEvent.class, event -> {
            final Player player = event.getPlayer();
            SidebarManager.buildSidebar(player);
        });
        // Start the server on port 25565
        minecraftServer.start(Config.SERVER_HOST, Config.SERVER_PORT);
        // Register commands
        MinecraftServer.getCommandManager().register(new TestCommand());

        Log.getLogger().info("Feather has started!");
    }
}