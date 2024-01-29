package cc.ddev.feather.api.playerdata;

import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.database.models.PlayerModel;
import cc.ddev.feather.logger.Log;
import cc.ddev.feather.player.PlayerProfile;
import cc.ddev.feather.player.PlayerWrapper;
import cc.ddev.feather.world.WorldManager;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerManager {

    private static PlayerManager instance;

    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }
        return instance;
    }

    public Map<UUID, Point> plotWandPos1Map = new HashMap<>();
    public Map<UUID, Point> plotWandPos2Map = new HashMap<>();

    public void setPlotWandPos1(UUID uuid, Point point) {
        plotWandPos1Map.remove(uuid);
        plotWandPos1Map.put(uuid, point);
    }

    public void setPlotWandPos2(UUID uuid, Point point) {
        plotWandPos2Map.remove(uuid);
        plotWandPos2Map.put(uuid, point);
    }

    public Point getPlotWandPos1(UUID uuid) {
        return plotWandPos1Map.get(uuid);
    }

    public Point getPlotWandPos2(UUID uuid) {
        return plotWandPos2Map.get(uuid);
    }

    public PlayerProfile getPlayerProfile(Player player) {
        PlayerProfile playerProfile = PlayerWrapper.getPlayerProfile(player);
        if (playerProfile == null) {
            player.kick("Failed to load player profile!");
            return null;
        }
        return playerProfile;
    }

    public PlayerModel getPlayerModel(Player player) {
        CompletableFuture<PlayerModel> future = StormDatabase.getInstance().loadPlayerModel(player.getUuid());
        PlayerModel playerModel;

        try {
            // Prevent not loading the player model in time
            future.get();

            // Load player profile
            PlayerProfile playerProfile = getPlayerProfile(player);

            // Load player model
            playerModel = playerProfile.getPlayerModel();
            if (playerModel == null) {
                player.kick("Failed to load player model!");
                return null;
            }
        } catch (Exception e) {
            Log.getLogger().info("Failed to load player profile and model for " + player.getUsername() + "!");
            e.printStackTrace();
            return null;
        }
        return playerModel;
    }

    public Pos getLastLocation(Player player) {
        if (player == null) {
            return null;
        }
        PlayerModel playerModel = getPlayerModel(player);
        if (playerModel == null) {
            return null;
        }
        // Set the spawn position
        if (playerModel.getLastLocation() != null) {
            // Extract X, Y and Z from the position string
            String[] rawPosition = playerModel.getLastLocation().split(",");
            double x = Double.parseDouble(rawPosition[0].replace("Pos[x=", ""));
            double y = Double.parseDouble(rawPosition[1].replace("y=", ""));
            double z = Double.parseDouble(rawPosition[2].replace("z=", ""));
            float yaw = Float.parseFloat(rawPosition[3].replace("yaw=", ""));
            float pitch = Float.parseFloat(rawPosition[4].replace("pitch=", "").replace("]", ""));

            return new Pos(x, y, z, yaw, pitch);
        }
        return null;
    }

    public static String getInstanceName(Player player) {
        if (player == null || player.getInstance() == null) {
            return null;
        }
        return WorldManager.getInstance().getInstanceName(player.getInstance());
    }

    public static String getInstanceLoadingName(Player player) {
        return WorldManager.getInstance().getLoadingName(getInstanceName(player));
    }

    public static String getInstanceColor(Player player) {
        return WorldManager.getInstance().getColor(getInstanceName(player));
    }
}
