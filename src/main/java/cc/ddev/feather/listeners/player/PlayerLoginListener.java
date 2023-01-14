package cc.ddev.feather.listeners.player;

import cc.ddev.feather.Server;
import cc.ddev.feather.api.config.Config;
import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.listeners.handler.Listen;
import cc.ddev.feather.listeners.handler.Listener;
import cc.ddev.feather.logger.Log;
import cc.ddev.feather.world.WorldManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

    // Handle the player login event
    @Listen(event = PlayerLoginEvent.class)
    public void onPlayerLogin(PlayerLoginEvent event) {
        final Player player = event.getPlayer();

        // Check if the server is full
        if (MinecraftServer.getConnectionManager().getOnlinePlayers().size() >= Config.Server.MAX_PLAYERS) {
            player.kick("Server is full!");
        }

        // Load player model
        StormDatabase.getInstance().loadPlayerModel(player.getUuid());

        // Set the player's instance
        if (!WorldManager.worldsDirectoryIsEmpty()) {
            event.setSpawningInstance(WorldManager.getWorld(Config.Spawn.WORLD));
        } else {
            event.setSpawningInstance(Server.getInstanceContainer());
        }

        // Set the spawn position
        player.setRespawnPoint(Config.Spawn.COORDS);

        Log.getLogger().info("UUID of player " + player.getUsername() + " is " + player.getUuid());
    }
}
