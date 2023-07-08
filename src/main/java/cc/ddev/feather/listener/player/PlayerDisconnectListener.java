package cc.ddev.feather.listener.player;

import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.database.models.PlayerModel;
import cc.ddev.feather.listener.handler.Listen;
import cc.ddev.feather.listener.handler.Listener;
import cc.ddev.feather.logger.Log;
import cc.ddev.feather.player.PlayerProfile;
import cc.ddev.feather.player.PlayerWrapper;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;

public class PlayerDisconnectListener implements Listener {

    // Handle the player disconnect event
    @Listen
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        final Player player = event.getPlayer();
        PlayerProfile playerProfile = PlayerWrapper.getPlayerProfile(player);
        if (playerProfile == null) {
            Log.getLogger().warn("Player " + player.getUsername() + " with UUID " + player.getUuid() + " has no player profile!");
            return;
        }
        // Save player model
        PlayerModel playerModel = playerProfile.getPlayerModel();
        playerModel.setLastLocation(player.getPosition().toString());
        playerModel.setUsername(player.getUsername());
        player.setRespawnPoint(player.getPosition());
        StormDatabase.getInstance().saveStormModel(playerModel);

        Log.getLogger().info("Saved player " + player.getUsername() + " with UUID " + player.getUuid());
    }
}
