package cc.ddev.feather;

import cc.ddev.feather.api.banking.BankUtils;
import cc.ddev.feather.api.config.Config;
import cc.ddev.feather.command.StopCommand;
import cc.ddev.feather.command.TestCommand;
import cc.ddev.feather.command.banking.OpenBankCommand;
import cc.ddev.feather.command.banking.bankaccount.BankAccountCommand;
import cc.ddev.feather.command.economy.EconomyCommand;
import cc.ddev.feather.command.essential.GamemodeCommand;
import cc.ddev.feather.command.essential.OpCommand;
import cc.ddev.feather.command.mtworld.MTWorldCommand;
import cc.ddev.feather.configuration.ConfigManager;
import cc.ddev.feather.database.DataManager;
import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.listener.client.ClientUpdateSignListener;
import cc.ddev.feather.listener.player.*;
import cc.ddev.feather.listener.server.ServerListPingListener;
import cc.ddev.feather.logger.Log;
import cc.ddev.feather.player.PlayerProfile;
import cc.ddev.feather.task.SaveWorldTask;
import cc.ddev.feather.task.ShutdownTask;
import cc.ddev.feather.world.WorldManager;
import cc.ddev.feather.world.blockhandlers.RegisterHandlers;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.client.play.ClientUpdateSignPacket;

import java.io.File;

public class Server {

    @Getter
    private static InstanceContainer instanceContainer;

    public static void main(String[] args) {
        // Initialization
        MinecraftServer minecraftServer = MinecraftServer.init();
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        // Set brand name
        MinecraftServer.setBrandName("Feather");
        // Load the configuration file
        ConfigManager configManager = ConfigManager.init();
        configManager.createConfigDirectory();
        // Make the database connection
        new DataManager().initialize();
        // Register block handlers
        RegisterHandlers.initHandlers();
        // Pull database cache
        BankUtils.getInstance().pullCache();
        // Create the worlds directory
        WorldManager.createWorldsDirectory();
        // Check if worlds are present
        if (WorldManager.worldsDirectoryIsEmpty()) {
            Log.getLogger().error("No worlds found! Please create a world in the worlds directory!");
            Log.getLogger().error("Resorting to default world...");
            // Create the instance
            instanceContainer = instanceManager.createInstanceContainer();
            // Set the ChunkGenerator
            instanceContainer.setGenerator(unit ->
                unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));
        }
        // Set the UUID provider
        MinecraftServer.getConnectionManager().setUuidProvider((playerConnection, username) -> {
            // This method will be called at players connection to set their UUID
            return PlayerProfile.getMojangUniqueId(username); /* Set here your custom UUID registration system */
        });

        // Register server listeners
        new ServerListPingListener().register();
        // Register player listeners
        new PlayerLoginListener().register();
        new PlayerSpawnListener().register();
        new PlayerDisconnectListener().register();
        new PlayerClickInventoryListener().register();
        new ClientUpdateSignListener().register();
        new PlayerBlockInteractListener().register();
        new PlayerBlockPlaceListener().register();
        // Start the server from config values
        minecraftServer.start(Config.Server.SERVER_HOST, Config.Server.SERVER_PORT);
        // Register commands
        MinecraftServer.getCommandManager().register(new TestCommand());
        MinecraftServer.getCommandManager().register(new OpCommand());
        MinecraftServer.getCommandManager().register(new GamemodeCommand());
        MinecraftServer.getCommandManager().register(new MTWorldCommand());
        MinecraftServer.getCommandManager().register(new EconomyCommand());
        MinecraftServer.getCommandManager().register(new OpenBankCommand());
        MinecraftServer.getCommandManager().register(new BankAccountCommand());
        MinecraftServer.getCommandManager().register(new StopCommand());
        for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            StormDatabase.getInstance().loadPlayerModel(player.getUuid());
        }

        WorldManager.loadWorld(WorldManager.getWorldsDirectory() + File.separator + Config.Spawn.WORLD);
        SaveWorldTask.registerTask();
        ShutdownTask.registerTask();
        ShutdownTask.registerShutdownHook();
    }
}