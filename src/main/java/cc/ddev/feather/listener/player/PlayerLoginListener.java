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
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.timer.TaskSchedule;

public class PlayerLoginListener implements Listener {

    // Handle the player login event
    @Listen
    public void onPlayerLogin(PlayerLoginEvent event) {
        final Player player = event.getPlayer();

        // Check if the server is full
        if (MinecraftServer.getConnectionManager().getOnlinePlayers().size() >= Config.Server.MAX_PLAYERS) {
            player.kick("Server is full!");
        }

        // Set the player's instance
        if (!WorldManager.worldsDirectoryIsEmpty()) {
            @NotNull Instance instance = WorldManager.getWorld(Config.Spawn.WORLD);
            if (instance == null) {
                event.setSpawningInstance(Server.getInstanceContainer());
                return;
            }
            event.setSpawningInstance(instance);
        } else {
            event.setSpawningInstance(Server.getInstanceContainer());
        }

        // Load player model
        StormDatabase.getInstance().loadPlayerModel(player.getUuid());

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            PlayerProfile playerProfile = PlayerWrapper.getPlayerProfile(player);
            PlayerModel playerModel = playerProfile.getPlayerModel();
            if (playerModel == null) {
                player.kick("Failed to load player model!");
                return;
            }
            // Extract X, Y and Z from the position string
            String[] position = playerModel.getLastLocation().split(",");
            double x = Double.parseDouble(position[0].replace("Pos[x=", ""));
            double y = Double.parseDouble(position[1].replace("y=", ""));
            double z = Double.parseDouble(position[2].replace("z=", ""));
            float yaw = Float.parseFloat(position[3].replace("yaw=", ""));
            float pitch = Float.parseFloat(position[4].replace("pitch=", "").replace("]", ""));

            // Set the spawn position
            player.setRespawnPoint(new Pos(x, y, z, yaw, pitch));
        }).delay(TaskSchedule.seconds(5)).schedule();

        Log.getLogger().info("UUID of player " + player.getUsername() + " is " + player.getUuid());
    }
}
