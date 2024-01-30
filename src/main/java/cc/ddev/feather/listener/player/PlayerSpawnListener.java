package cc.ddev.feather.listener.player;

import cc.ddev.feather.api.API;
import cc.ddev.feather.api.config.Config;
import cc.ddev.feather.api.config.Messages;
import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.database.models.PlayerModel;
import cc.ddev.feather.listener.handler.Listen;
import cc.ddev.feather.listener.handler.Listener;
import cc.ddev.feather.logger.Log;
import cc.ddev.feather.placeholders.Placeholders;
import cc.ddev.feather.player.PlayerProfile;
import cc.ddev.feather.player.PlayerWrapper;
import cc.ddev.feather.api.sidebar.SidebarManager;
import cc.ddev.feather.utils.ChatUtils;
import cc.ddev.feather.world.WorldManager;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerSpawnEvent;

public class PlayerSpawnListener implements Listener {

    // Handle the player spawn event
    @Listen
    public void onPlayerSpawn(PlayerSpawnEvent event) {
        final Player player = event.getPlayer();

        if (player.getInstance() == null) {
            player.kick("Failed to load player instance!");
            return;
        }

        // Check if player is in an MTWorld
        if (WorldManager.getInstance().isMTWorld(WorldManager.getInstance().getInstanceName(player.getInstance()))) {

            PlayerModel playerModel = API.getPlayerManager().getPlayerModel(player);

            // Update username if it has changed
            playerModel.setUsername(player.getUsername());
            StormDatabase.getInstance().saveStormModel(playerModel);

//            // Set the spawn position
//            if (playerModel.getLastLocation() != null) {
//                // Extract X, Y and Z from the position string
//                String[] rawPosition = playerModel.getLastLocation().split(",");
//                double x = Double.parseDouble(rawPosition[0].replace("Pos[x=", ""));
//                double y = Double.parseDouble(rawPosition[1].replace("y=", ""));
//                double z = Double.parseDouble(rawPosition[2].replace("z=", ""));
//                float yaw = Float.parseFloat(rawPosition[3].replace("yaw=", ""));
//                float pitch = Float.parseFloat(rawPosition[4].replace("pitch=", "").replace("]", ""));
//                Pos pos = new Pos(x, y, z, yaw, pitch);
//
//                player.setRespawnPoint(pos);
//                player.teleport(pos);
//            } else {
//                player.setRespawnPoint(Config.Spawn.COORDS);
//                player.teleport(Config.Spawn.COORDS);
//            }


            //player.teleport(new Pos(7.5, 300, -3.5));

            if (playerModel.getIsOperator()) {
                player.setPermissionLevel(4);
            }

            // Check if join messages are enabled
            if (Messages.Join.SEND_JOIN_MESSAGE) {
            ChatUtils.broadcast(ChatUtils.translateMiniMessage(Messages.Join.JOIN_MESSAGE.replace("<player>", player.getUsername())));
            }

            // Build sidebar
            SidebarManager.buildSidebar(player);
            player.sendTitlePart(TitlePart.TITLE, ChatUtils.translateMiniMessage(Placeholders.parse(player, Messages.JOIN_TITLE)));
            player.sendTitlePart(TitlePart.SUBTITLE, ChatUtils.translateMiniMessage(Placeholders.parse(player, Messages.JOIN_SUBTITLE)));
        }
    }
}
