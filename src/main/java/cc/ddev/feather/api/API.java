package cc.ddev.feather.api;

import cc.ddev.feather.configuration.ConfigManager;
import lombok.Getter;
import net.minestom.server.entity.Player;

public class API {

    @Getter
    public static ConfigManager configManager = ConfigManager.init();

    public static boolean isOp(Player player) {
        return player.getPermissionLevel() == 4;
    }

    public static void setOp(Player player, boolean op) {
        if (op) {
            player.setPermissionLevel(4);
        } else {
            player.setPermissionLevel(0);
        }
    }
}
