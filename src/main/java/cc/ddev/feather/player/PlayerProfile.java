package cc.ddev.feather.player;

import cc.ddev.feather.database.models.PlayerModel;
import cc.ddev.feather.logger.Log;
import cc.ddev.feather.world.WorldManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

@Getter
public class PlayerProfile {

    private final Player player;
    private final PlayerModel playerModel;
    private final UUID uniqueId;
    private final Component username;

    public PlayerProfile(@NotNull Player player, @NotNull PlayerModel playerModel) {
        this.player = player;
        this.uniqueId = player.getUuid();
        this.username = player.getName();
        this.playerModel = playerModel;
    }

    public static String getInstanceName(Player player) {
        if (player == null || player.getInstance() == null) {
            return null;
        }
        return WorldManager.getInstanceName(player.getInstance());
    }

    public static String getInstanceLoadingName(Player player) {
        return WorldManager.getLoadingName(getInstanceName(player));
    }

    public static String getInstanceColor(Player player) {
        return WorldManager.getColor(getInstanceName(player));
    }
}
