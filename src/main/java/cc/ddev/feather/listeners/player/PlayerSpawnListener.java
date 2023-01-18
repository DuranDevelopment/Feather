package cc.ddev.feather.listeners.player;

import cc.ddev.feather.api.config.Messages;
import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.database.models.PlayerModel;
import cc.ddev.feather.listeners.handler.Listen;
import cc.ddev.feather.listeners.handler.Listener;
import cc.ddev.feather.logger.Log;
import cc.ddev.feather.placeholders.Placeholders;
import cc.ddev.feather.player.PlayerProfile;
import cc.ddev.feather.player.PlayerWrapper;
import cc.ddev.feather.sidebar.SidebarManager;
import cc.ddev.feather.utils.ChatUtils;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerSpawnEvent;

public class PlayerSpawnListener implements Listener {

    // Handle the player spawn event
    @Listen(event = PlayerSpawnEvent.class)
    public void onPlayerSpawn(PlayerSpawnEvent event) {
        final Player player = event.getPlayer();

        PlayerProfile playerProfile = PlayerWrapper.getPlayerProfile(player);
        PlayerModel playerModel = playerProfile.getPlayerModel();
        playerModel.setLastLocation(player.getPosition().toString());
        StormDatabase.getInstance().saveStormModel(playerModel);

        if (playerModel.getIsOperator()) {
            player.setPermissionLevel(4);
        }

        Log.getLogger().info(player.getPosition().toString());
        player.sendTitlePart(TitlePart.TITLE, ChatUtils.translateMiniMessage(Placeholders.parse(player, Messages.TITLE_LINE_1)));
        player.sendTitlePart(TitlePart.SUBTITLE, ChatUtils.translateMiniMessage(Placeholders.parse(player, Messages.TITLE_LINE_2)));
        SidebarManager.buildSidebar(player);
    }
}
