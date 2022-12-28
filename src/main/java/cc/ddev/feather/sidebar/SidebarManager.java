package cc.ddev.feather.sidebar;

import cc.ddev.feather.api.config.Config;
import cc.ddev.feather.placeholders.Placeholders;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;

public class SidebarManager {

    public static void buildSidebar(Player player) {
        Component titleComponent = Component.text(Placeholders.parse(player, Config.SIDEBAR_TITLE.toUpperCase()));
        Sidebar sidebar = new Sidebar(titleComponent);
        Sidebar.ScoreboardLine line = new Sidebar.ScoreboardLine(
                "player_name",
                player.getName(),
                0
        );
        sidebar.createLine(line);
        sidebar.addViewer(player);
    }
}
