package cc.ddev.feather.player;

import cc.ddev.feather.database.models.PlayerModel;
import cc.ddev.feather.logger.Log;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

public class FeatherPlayer {

    private final @Getter Player player;
    private final @Getter
    PlayerModel playerModel;
    private final @Getter UUID uniqueId;
    private final @Getter Component username;

    public FeatherPlayer(@NotNull Player player, @NotNull PlayerModel playerModel) {
        this.player = player;
        this.uniqueId = getUniqueId(player.getName().toString());
        this.username = player.getName();
        this.playerModel = playerModel;
    }

    //Get UUID of player from username
    public static UUID getUniqueId(String name) {
        String uniqueId;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openStream()));
            uniqueId = (((JsonObject) JsonParser.parseReader(in)).get("id")).toString().replaceAll("\"", "");
            uniqueId = uniqueId.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
            in.close();
        } catch (Exception e) {
            Log.getLogger().error("Unable to get UUID of: " + name + "!");
            uniqueId = "er";
        }
        return UUID.fromString(uniqueId);
    }
}
