package cc.ddev.feather.listeners.player;

import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.database.models.PlayerModel;
import cc.ddev.feather.listeners.handler.Listen;
import cc.ddev.feather.listeners.handler.Listener;
import cc.ddev.feather.logger.Log;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;

public class PlayerDisconnectListener implements Listener {

    // Handle the player disconnect event
    @Listen(event = PlayerDisconnectEvent.class)
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        final Player player = event.getPlayer();
        // Get PlayerModel
        PlayerModel playerModel = StormDatabase.getInstance().loadPlayerModel(player.getUuid()).join();
        // Save player model
        StormDatabase.getInstance().saveStormModel(playerModel);
        Log.getLogger().info("Saved player " + player.getUsername() + " with UUID " + player.getUuid());
    }
}
