package cc.ddev.feather.api;

import cc.ddev.feather.api.economy.EconomyManager;
import cc.ddev.feather.configuration.ConfigManager;
import cc.ddev.feather.api.sidebar.SidebarManager;
import cc.ddev.instanceguard.InstanceGuard;
import lombok.Getter;
import net.minestom.server.entity.Player;

public class API {

    @Getter
    public static ConfigManager configManager = ConfigManager.getInstance();

    @Getter
    public static EconomyManager economy = EconomyManager.getInstance();

    @Getter
    public static InstanceGuard instanceGuard = new InstanceGuard();

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
