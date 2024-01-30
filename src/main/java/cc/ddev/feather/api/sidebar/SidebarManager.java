package cc.ddev.feather.api.sidebar;

import cc.ddev.feather.api.config.Config;
import cc.ddev.feather.database.models.PlayerModel;
import cc.ddev.feather.placeholders.Placeholders;
import cc.ddev.feather.player.PlayerProfile;
import cc.ddev.feather.player.PlayerWrapper;
import cc.ddev.feather.world.WorldManager;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class SidebarManager {
    public static HashMap<Player, Sidebar> sidebarMap = new HashMap<>();

    public static void buildSidebar(Player player) {
        Component titleComponent = Component.text(Placeholders.parse(player, Config.Sidebar.TITLE).toUpperCase());
        Sidebar sidebar = new Sidebar(titleComponent);

        PlayerProfile playerProfile = PlayerWrapper.getPlayerProfile(player);
        if (playerProfile == null) return;
        PlayerModel playerModel = playerProfile.getPlayerModel();
        Double balance = playerModel.getBalance();

        Component balanceComponent = balance == 0 ? Component.text("0") : Component.text(balance);
        Sidebar.ScoreboardLine line = new Sidebar.ScoreboardLine(
                "player_money",
                Component.text("Balance: ").append(balanceComponent),
                0
        );
        sidebar.createLine(line);
        sidebar.addViewer(player);
        sidebarMap.put(player, sidebar);
    }

    public static void removeSidebar(Player player) {
        Sidebar sidebar = sidebarMap.get(player);
        sidebar.removeViewer(player);
        sidebarMap.remove(player);
    }

    public static void refreshSidebar(Player player) {
        if (player.getInstance() == null) return;
        if (WorldManager.getInstance().isMTWorld(WorldManager.getInstance().getInstanceName(player.getInstance()))) {
            SidebarManager.buildSidebar(player);
        }
    }

    public static void refreshSidebar() {
        for (@NotNull Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            if (player.getInstance() == null) return;
            if (SidebarManager.getSidebarEnabled().containsKey(player)) {
                SidebarManager.removeSidebar(player);
                if (WorldManager.getInstance().isMTWorld(WorldManager.getInstance().getInstanceName(player.getInstance()))) {
                    SidebarManager.buildSidebar(player);
                }
            }
        }
    }

    public static HashMap<Player, Sidebar> getSidebarEnabled() {
        return sidebarMap;
    }
}
