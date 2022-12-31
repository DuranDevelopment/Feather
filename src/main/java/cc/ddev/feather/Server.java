package cc.ddev.feather;

import cc.ddev.feather.api.config.Config;
import cc.ddev.feather.commands.TestCommand;
import cc.ddev.feather.commands.essential.GamemodeCommand;
import cc.ddev.feather.commands.essential.OpCommand;
import cc.ddev.feather.configuration.ConfigManager;
import cc.ddev.feather.database.DataManager;
import cc.ddev.feather.listeners.player.PlayerDisconnectListener;
import cc.ddev.feather.listeners.player.PlayerLoginListener;
import cc.ddev.feather.listeners.player.PlayerSpawnListener;
import cc.ddev.feather.listeners.server.ServerListPingListener;
import cc.ddev.feather.logger.Log;
import cc.ddev.feather.player.FeatherPlayer;
import cc.ddev.feather.world.WorldManager;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;

public class Server {

    @Getter
    private static InstanceContainer instanceContainer;

    public static void main(String[] args) {
        // Initialization
        MinecraftServer minecraftServer = MinecraftServer.init();
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        // Create the instance
        instanceContainer = instanceManager.createInstanceContainer();
        // Load the configuration file
        ConfigManager configManager = ConfigManager.init();
        configManager.createConfigDirectory();
        // Make the database connection
        new DataManager().initialize();
        // Create the worlds directory
        WorldManager.createWorldsDirectory();
        // Check if worlds are present
        if (WorldManager.worldsDirectoryIsEmpty()) {
            Log.getLogger().error("No worlds found! Please create a world in the worlds directory!");
            Log.getLogger().error("Resorting to default world...");
            // Set the ChunkGenerator
            instanceContainer.setGenerator(unit ->
                unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));
        }
        // Set the UUID provider
        MinecraftServer.getConnectionManager().setUuidProvider((playerConnection, username) -> {
            // This method will be called at players connection to set their UUID
            return FeatherPlayer.getUniqueId(username); /* Set here your custom UUID registration system */
        });

        // Register server listeners
        new ServerListPingListener().register();
        // Register player listeners
        new PlayerLoginListener().register();
        new PlayerSpawnListener().register();
        new PlayerDisconnectListener().register();
        // Start the server from config values
        minecraftServer.start(Config.Server.SERVER_HOST, Config.Server.SERVER_PORT);
        // Register commands
        MinecraftServer.getCommandManager().register(new TestCommand());
        MinecraftServer.getCommandManager().register(new OpCommand());
        MinecraftServer.getCommandManager().register(new GamemodeCommand());

        Log.getLogger().info("Feather has started!");
    }
}