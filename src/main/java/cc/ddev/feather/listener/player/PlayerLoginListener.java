package cc.ddev.feather.listener.player;

import cc.ddev.feather.Server;
import cc.ddev.feather.api.API;
import cc.ddev.feather.api.config.Config;
import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.database.models.PlayerModel;
import cc.ddev.feather.listener.handler.Listen;
import cc.ddev.feather.listener.handler.Listener;
import cc.ddev.feather.logger.Log;
import cc.ddev.feather.player.PlayerProfile;
import cc.ddev.feather.player.PlayerWrapper;
import cc.ddev.feather.world.WorldManager;
import de.leonhard.storage.shaded.jetbrains.annotations.NotNull;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.permission.Permission;
import net.minestom.server.timer.TaskSchedule;

import java.util.concurrent.CompletableFuture;

public class PlayerLoginListener implements Listener {

    // Handle the player login event
    @Listen
    public void onPlayerLogin(AsyncPlayerConfigurationEvent event) {
        final Player player = event.getPlayer();

        // Check if the server is full
        if (MinecraftServer.getConnectionManager().getOnlinePlayers().size() >= Config.Server.MAX_PLAYERS) {
            player.kick("Server is full!");
        }

        // Set the player's instance
        if (!WorldManager.getInstance().worldsDirectoryIsEmpty()) {
            @NotNull Instance instance = WorldManager.getInstance().getWorld(Config.Spawn.WORLD);
            if (instance == null) {
                event.setSpawningInstance(Server.getInstanceContainer());
                return;
            }
            event.setSpawningInstance(instance);
        } else {
            event.setSpawningInstance(Server.getInstanceContainer());
        }

        PlayerModel playerModel = API.getPlayerManager().getPlayerModel(player);
        if (API.getPlayerManager().getLastLocation(player) == null) {
            player.setRespawnPoint(Config.Spawn.COORDS);
        } else {
            player.setRespawnPoint(API.getPlayerManager().getLastLocation(player));
        }

        // Sets last known username
        playerModel.setUsername(player.getUsername());

        // Save player model
        StormDatabase.getInstance().saveStormModel(playerModel);

        Log.getLogger().info("UUID of player " + player.getUsername() + " is " + player.getUuid());

        // TODO: Remove on production
        player.setPermissionLevel(4);
        player.addPermission(new Permission("server.stop"));
    }
}
