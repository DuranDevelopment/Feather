package cc.ddev.feather;

import cc.ddev.feather.api.config.Config;
import cc.ddev.feather.commands.TestCommand;
import cc.ddev.feather.commands.essential.GamemodeCommand;
import cc.ddev.feather.commands.essential.OpCommand;
import cc.ddev.feather.configuration.ConfigManager;
import cc.ddev.feather.database.DataManager;
import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.database.models.PlayerModel;
import cc.ddev.feather.logger.Log;
import cc.ddev.feather.placeholders.Placeholders;
import cc.ddev.feather.player.FeatherPlayer;
import cc.ddev.feather.sidebar.SidebarManager;
import cc.ddev.feather.world.WorldManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.ping.ResponseData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class Server {

    private static String cachedFavicon;

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

        // Set serverlist information

        // Cache favicon image displayed in server list, needs to be base64 format
        try {
            BufferedImage image = ImageIO.read(new File("./server-icon.png")); // Use vanilla file name
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            cachedFavicon = Base64.getEncoder().encodeToString(outputStream.toByteArray());
            outputStream.close();
        } catch (IOException e) {
            cachedFavicon = "";
        }

        // Add an event callback to specify the spawning instance (and the spawn position)
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(ServerListPingEvent.class, event -> {
            int onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers().size();
            ResponseData responseData = event.getResponseData();
            // Set the server's motd
            responseData.setDescription(LegacyComponentSerializer.legacyAmpersand().deserialize(Config.Server.DESCRIPTION));
            // Set online players
            responseData.setOnline(onlinePlayers);
            // Set the server's max player count
            responseData.setMaxPlayer(Config.Server.MAX_PLAYERS);
            // Set the server's icon
            if(!cachedFavicon.isEmpty()) {
                responseData.setFavicon("data:image/png;base64,"+cachedFavicon);
            }
        });
        // Handle the login event
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            // Load player model
            StormDatabase.getInstance().loadPlayerModel(player.getUuid());
            // Check if the server is full
            if (MinecraftServer.getConnectionManager().getOnlinePlayers().size() >= Config.Server.MAX_PLAYERS) {
                player.kick("Server is full!");
            }
            // Set the player's instance
            if (!WorldManager.worldsDirectoryIsEmpty()) {
                event.setSpawningInstance(WorldManager.loadWorld(WorldManager.getWorldsDirectory() + File.separator + Config.Spawn.WORLD));
            } else {
                event.setSpawningInstance(instanceContainer);
            }
            // Set the spawn position
            player.setRespawnPoint(Config.Spawn.COORDS);
            Log.getLogger().info("UUID of player " + player.getUsername() + " is " + player.getUuid());
        });
        // Handle the spawn event
        globalEventHandler.addListener(PlayerSpawnEvent.class, event -> {
            final Player player = event.getPlayer();
            player.sendTitlePart(TitlePart.TITLE, Component.text("Welkom in"));
            player.sendTitlePart(TitlePart.SUBTITLE, Component.text(Placeholders.parse(player, "<world>")));
            SidebarManager.buildSidebar(player);
        });
        // Handle the DisconnectEvent
        globalEventHandler.addListener(PlayerDisconnectEvent.class, event -> {
            final Player player = event.getPlayer();
            // Get PlayerModel
            PlayerModel playerModel = StormDatabase.getInstance().loadPlayerModel(player.getUuid()).join();
            // Save player model
            StormDatabase.getInstance().saveStormModel(playerModel);
            Log.getLogger().info("Saved player " + player.getUsername() + " with UUID " + player.getUuid());
        });
        // Start the server from config values
        minecraftServer.start(Config.Server.SERVER_HOST, Config.Server.SERVER_PORT);
        // Register commands
        MinecraftServer.getCommandManager().register(new TestCommand());
        MinecraftServer.getCommandManager().register(new OpCommand());
        MinecraftServer.getCommandManager().register(new GamemodeCommand());

        Log.getLogger().info("Feather has started!");
    }
}