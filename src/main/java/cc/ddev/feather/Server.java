package cc.ddev.feather;

import cc.ddev.feather.api.API;
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
import cc.ddev.feather.command.plots.PlotCommand;
import cc.ddev.feather.command.plots.PlotInfoCommand;
import cc.ddev.feather.command.plots.PlotwandCommand;
import cc.ddev.feather.command.plots.subcommands.PlotCalculateCommand;
import cc.ddev.feather.configuration.ConfigManager;
import cc.ddev.feather.database.DataManager;
import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.listener.client.ClientUpdateSignListener;
import cc.ddev.feather.listener.player.*;
import cc.ddev.feather.listener.server.ServerListPingListener;
import cc.ddev.feather.logger.Log;
import cc.ddev.feather.task.SaveWorldTask;
import cc.ddev.feather.task.ShutdownTask;
import cc.ddev.feather.task.SidebarRefreshTask;
import cc.ddev.feather.world.WorldManager;
import cc.ddev.feather.world.blockhandlers.RegisterHandlers;
import cc.ddev.instanceguard.flag.FlagValue;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import net.minestom.vanilla.VanillaReimplementation;
import net.minestom.vanilla.system.RayFastManager;

import java.io.File;
import java.util.UUID;

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
        ConfigManager configManager = ConfigManager.getInstance();
        configManager.createConfigDirectory();

        // Make the database connection
        new DataManager().initialize();

        // Register block handlers
        RegisterHandlers.initHandlers();

        // Vanilla reimplementation
        VanillaReimplementation vri = VanillaReimplementation.hook(MinecraftServer.process());

        // Set up raycasting lib
        RayFastManager.init();

        // Pull database cache
        BankUtils.getInstance().pullCache();

        // Create the worlds directory
        WorldManager.getInstance().createWorldsDirectory();

        // Check if worlds are present
        if (WorldManager.getInstance().worldsDirectoryIsEmpty()) {
            Log.getLogger().error("No worlds found! Please create a world in the worlds directory!");
            Log.getLogger().error("Resorting to default world...");
            // Create the instance
            instanceContainer = instanceManager.createInstanceContainer();
            // Set the ChunkGenerator
            instanceContainer.setGenerator(unit ->
                    unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));
        } else {
            // Load the world
            instanceContainer = WorldManager.loadWorld(WorldManager.getInstance().getWorldsDirectory() + File.separator + Config.Spawn.WORLD);
        }

        // Sets online-mode
        if (Config.Server.ONLINE_MODE) {
            MojangAuth.init();
            Log.getLogger().info("Using Mojang authentication system...");
        } else {
            // Set the UUID provider
            MinecraftServer.getConnectionManager().setUuidProvider((playerConnection, username) -> {
                // TODO: Make a custom non-Mojang UUID registration system
                // This method will be called at players connection to set their UUID
                return UUID.randomUUID(); /* Set here your custom UUID registration system */
            });
        }

        // Register server listeners
        new ServerListPingListener().register();
        // Register player listeners
        new PlayerChatListener().register();
        new PlayerLoginListener().register();
        new PlayerSpawnListener().register();
        new PlayerDisconnectListener().register();
        new PlayerInventoryClickListener().register();
        new ClientUpdateSignListener().register();
        new PlayerBlockInteractListener().register();
        new PlayerBlockPlaceListener().register();
        new PlayerBlockBreakListener().register();
        new PlayerItemDropListener().register();
        new PlayerItemPickupListener().register();
        new PlayerInteractListener().register();

        // Register commands
        MinecraftServer.getCommandManager().register(new TestCommand());
        MinecraftServer.getCommandManager().register(new OpCommand());
        MinecraftServer.getCommandManager().register(new GamemodeCommand());
        MinecraftServer.getCommandManager().register(new MTWorldCommand());
        MinecraftServer.getCommandManager().register(new EconomyCommand());
        MinecraftServer.getCommandManager().register(new OpenBankCommand());
        MinecraftServer.getCommandManager().register(new BankAccountCommand());
        MinecraftServer.getCommandManager().register(new StopCommand());
        MinecraftServer.getCommandManager().register(new PlotwandCommand());
        MinecraftServer.getCommandManager().register(new PlotCommand());
        MinecraftServer.getCommandManager().register(new PlotInfoCommand());

        // Start the server from config values
        minecraftServer.start(Config.Server.SERVER_HOST, Config.Server.SERVER_PORT);

        for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            API.getPlayerManager().getPlayerModel(player);
        }

        // Hook InstanceGuard API to global event handler
        API.getInstanceGuard().enable(MinecraftServer.getGlobalEventHandler());
        // Register custom InstanceGuard flag
        API.getInstanceGuard().getFlagManager().registerCustomFlag("feather-description", new FlagValue<>(""));
        API.getInstanceGuard().getFlagManager().registerCustomFlag("feather-plotlevels", new FlagValue<>(""));

        SidebarRefreshTask.registerTask();
        SaveWorldTask.registerTask();
        ShutdownTask.registerTask();
    }
}