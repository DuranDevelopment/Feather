package cc.ddev.feather.listener.player;

import cc.ddev.feather.Server;
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
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.permission.Permission;
import net.minestom.server.timer.TaskSchedule;

public class PlayerLoginListener implements Listener {

    // Handle the player login event
    @Listen
    public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
        final Player player = event.getPlayer();

        // Check if the server is full
        if (MinecraftServer.getConnectionManager().getOnlinePlayers().size() >= Config.Server.MAX_PLAYERS) {
            player.kick("Server is full!");
        }

        // Set the player's instance
        if (!WorldManager.worldsDirectoryIsEmpty()) {
            @NotNull Instance instance = WorldManager.getWorld(Config.Spawn.WORLD);
            if (instance == null) {
                player.setInstance(Server.getInstanceContainer());
                return;
            }
            player.setInstance(instance);
        } else {
            player.setInstance(Server.getInstanceContainer());
        }

        StormDatabase.getInstance().loadPlayerModel(player.getUuid());

        // Delay to ensure the player model is loaded
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            // Load player profile
            PlayerProfile playerProfile = PlayerWrapper.getPlayerProfile(player);
            if (playerProfile == null) {
                player.kick("Failed to load player profile!");
                return;
            }

            // Load player model
            PlayerModel playerModel = playerProfile.getPlayerModel();
            if (playerModel == null) {
                player.kick("Failed to load player model!");
                return;
            }

            // Extract X, Y and Z from the position string
            String[] rawPosition = playerModel.getLastLocation().split(",");
            double x = Double.parseDouble(rawPosition[0].replace("Pos[x=", ""));
            double y = Double.parseDouble(rawPosition[1].replace("y=", ""));
            double z = Double.parseDouble(rawPosition[2].replace("z=", ""));
            float yaw = Float.parseFloat(rawPosition[3].replace("yaw=", ""));
            float pitch = Float.parseFloat(rawPosition[4].replace("pitch=", "").replace("]", ""));
            Pos pos = new Pos(x, y, z, yaw, pitch);

            // Set the spawn position
            player.setRespawnPoint(pos);

            Log.getLogger().info("UUID of player " + player.getUsername() + " is " + player.getUuid());

            // Remove on production
            player.setPermissionLevel(4);
            player.addPermission(new Permission("server.stop"));
        }).delay(TaskSchedule.seconds(5)).schedule();
    }
}
