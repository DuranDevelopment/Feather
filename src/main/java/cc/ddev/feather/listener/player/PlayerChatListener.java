package cc.ddev.feather.listener.player;

import cc.ddev.feather.api.API;
import cc.ddev.feather.api.config.Config;
import cc.ddev.feather.database.models.PlayerModel;
import cc.ddev.feather.listener.handler.Listen;
import cc.ddev.feather.listener.handler.Listener;
import cc.ddev.feather.player.PlayerProfile;
import cc.ddev.feather.player.PlayerWrapper;
import cc.ddev.feather.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerChatEvent;

public class PlayerChatListener implements Listener {

    @Listen
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();

        PlayerModel playerModel = API.getPlayerManager().getPlayerModel(player);
        if (playerModel == null) return;

        Component chatFormatComponent = ChatUtils.translateMiniMessage(
                Config.Chat.FORMAT
                        .replace("<level>", playerModel.getLevel().toString())
                        .replace("<levelcolor>", playerModel.getLevelcolor())
                        .replace("<prefix>", playerModel.getPrefix())
                        .replace("<chatcolor>", playerModel.getLevelcolor())
                        .replace("<namecolor>", playerModel.getLevelcolor())
                        .replace("<prefixcolor>", playerModel.getLevelcolor())
                        .replace("<player>", player.getUsername())
                        .replace("<message>", event.getMessage())

        );

        if (Config.Chat.FORMAT_ENABLED) {
            event.setChatFormat(chatEvent -> chatFormatComponent);
        }
    }
}
