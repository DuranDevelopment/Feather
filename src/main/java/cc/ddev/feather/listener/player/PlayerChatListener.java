package cc.ddev.feather.listener.player;

import cc.ddev.feather.api.config.Config;
import cc.ddev.feather.listener.handler.Listen;
import cc.ddev.feather.listener.handler.Listener;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.player.PlayerChatEvent;

public class PlayerChatListener implements Listener {

    @Listen
    public void onPlayerChat(PlayerChatEvent event) {
        Component chatFormatComponent = Component.text(
                Config.Chat.FORMAT
                        .replace("<levelcolor>", "Config.Chat.LEVEL_COLOR")
                        .replace("<player>", event.getPlayer().getUsername())
                        .replace("<message>", event.getMessage())

        );

        if (Config.Chat.FORMAT_ENABLED) {
            event.setChatFormat(chatEvent -> chatFormatComponent);
        }
    }
}
