package cc.ddev.feather.listener.player;

import cc.ddev.feather.api.API;
import cc.ddev.feather.api.config.Messages;
import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.database.models.PlayerModel;
import cc.ddev.feather.listener.handler.Listen;
import cc.ddev.feather.listener.handler.Listener;
import cc.ddev.feather.logger.Log;
import cc.ddev.feather.player.PlayerProfile;
import cc.ddev.feather.player.PlayerWrapper;
import cc.ddev.feather.utils.ChatUtils;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;

public class PlayerDisconnectListener implements Listener {

    // Handle the player disconnect event
    @Listen
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        final Player player = event.getPlayer();

        // Save player model
        PlayerModel playerModel = API.getPlayerManager().getPlayerModel(player);
        playerModel.setLastLocation(player.getPosition().toString());
        playerModel.setUsername(player.getUsername());
        player.setRespawnPoint(player.getPosition());
        StormDatabase.getInstance().saveStormModel(playerModel);

        Log.getLogger().info("Saved player " + player.getUsername() + " with UUID " + player.getUuid());

        if (Messages.Leave.SEND_LEAVE_MESSAGE) {
            ChatUtils.broadcast(ChatUtils.translateMiniMessage(Messages.Leave.LEAVE_MESSAGE.replace("<player>", player.getUsername())));
        }
    }
}
