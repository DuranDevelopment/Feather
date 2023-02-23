package cc.ddev.feather.listener.player;

import cc.ddev.feather.api.banking.BankingGUI;
import cc.ddev.feather.api.config.Messages;
import cc.ddev.feather.api.enums.BankAccountType;
import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.database.models.PlayerModel;
import cc.ddev.feather.listener.handler.Listen;
import cc.ddev.feather.listener.handler.Listener;
import cc.ddev.feather.placeholders.Placeholders;
import cc.ddev.feather.player.PlayerProfile;
import cc.ddev.feather.player.PlayerWrapper;
import cc.ddev.feather.sidebar.SidebarManager;
import cc.ddev.feather.utils.ChatUtils;
import cc.ddev.feather.world.WorldManager;
import net.kyori.adventure.title.TitlePart;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.network.packet.client.play.ClientUpdateSignPacket;
import net.minestom.server.network.packet.server.play.OpenSignEditorPacket;
import net.minestom.server.timer.TaskSchedule;

public class PlayerSpawnListener implements Listener {

    // Handle the player spawn event
    @Listen
    public void onPlayerSpawn(PlayerSpawnEvent event) {
        final Player player = event.getPlayer();

        // Delay the task to prevent the instance from being null
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            // Check if player is in an MTWorld
            if (WorldManager.isMTWorld(WorldManager.getInstanceName(player.getInstance()))) {
                PlayerProfile playerProfile = PlayerWrapper.getPlayerProfile(player);
                PlayerModel playerModel = playerProfile.getPlayerModel();

                // Extract X, Y and Z from the position string
                String[] position = playerModel.getLastLocation().split(",");
                double x = Double.parseDouble(position[0].replace("Pos[x=", ""));
                double y = Double.parseDouble(position[1].replace("y=", ""));
                double z = Double.parseDouble(position[2].replace("z=", ""));
                float yaw = Float.parseFloat(position[3].replace("yaw=", ""));
                float pitch = Float.parseFloat(position[4].replace("pitch=", "").replace("]", ""));

                player.teleport(new Pos(x, y, z, yaw, pitch));
                StormDatabase.getInstance().saveStormModel(playerModel);

                if (playerModel.getIsOperator()) {
                    player.setPermissionLevel(4);
                }

                // Build sidebar
                SidebarManager.buildSidebar(player);
                player.sendTitlePart(TitlePart.TITLE, ChatUtils.translateMiniMessage(Placeholders.parse(player, Messages.JOIN_TITLE)));
                player.sendTitlePart(TitlePart.SUBTITLE, ChatUtils.translateMiniMessage(Placeholders.parse(player, Messages.JOIN_SUBTITLE)));
            }
        }).delay(TaskSchedule.seconds(1)).schedule();
    }
}
