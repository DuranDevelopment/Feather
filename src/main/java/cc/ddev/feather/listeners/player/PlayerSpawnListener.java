package cc.ddev.feather.listeners.player;

import cc.ddev.feather.listeners.handler.Listen;
import cc.ddev.feather.listeners.handler.Listener;
import cc.ddev.feather.placeholders.Placeholders;
import cc.ddev.feather.sidebar.SidebarManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerSpawnEvent;

public class PlayerSpawnListener implements Listener {

    // Handle the player spawn event
    @Listen(event = PlayerSpawnEvent.class)
    public void onPlayerSpawn(PlayerSpawnEvent event) {
        final Player player = event.getPlayer();
        player.sendTitlePart(TitlePart.TITLE, Component.text("Welkom in"));
        player.sendTitlePart(TitlePart.SUBTITLE, Component.text(Placeholders.parse(player, "<world>")));
        SidebarManager.buildSidebar(player);
    }
}
