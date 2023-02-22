package cc.ddev.feather.api;

import cc.ddev.feather.api.economy.Economy;
import cc.ddev.feather.configuration.ConfigManager;
import cc.ddev.feather.sidebar.SidebarManager;
import lombok.Getter;
import net.minestom.server.entity.Player;

public class API {

    @Getter
    public static ConfigManager configManager = ConfigManager.init();

    @Getter
    public static Economy economy = Economy.getInstance();

    public static boolean hasScoreboard(Player player) {
        return SidebarManager.getSidebarEnabled().containsKey(player);
    }

    public static void toggleScoreboard(Player player) {
        if (API.hasScoreboard(player)) {
            SidebarManager.removeSidebar(player);
        } else {
            SidebarManager.buildSidebar(player);
        }
    }

    public static void updateScoreboard(Player player) {
        if (API.hasScoreboard(player)) {
            SidebarManager.removeSidebar(player);
            SidebarManager.buildSidebar(player);
        }
    }
}
