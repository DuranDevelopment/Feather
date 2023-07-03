package cc.ddev.feather.listener.player;

import cc.ddev.feather.api.config.Config;
import cc.ddev.feather.listener.handler.Listen;
import cc.ddev.feather.listener.handler.Listener;
import cc.ddev.feather.logger.Log;
import cc.ddev.feather.player.PlayerProfile;
import cc.ddev.feather.player.PlayerWrapper;
import cc.ddev.feather.utils.ChatUtils;
import net.kyori.adventure.text.format.Style;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerBlockBreakEvent;

public class PlayerBlockBreakListener implements Listener {
    @Listen
    public void onPlayerBlockBreak(PlayerBlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInMainHand().material() == Config.Plot.PLOTWAND.material()
        && player.getItemInMainHand().getDisplayName() == Config.Plot.PLOTWAND.getDisplayName()) {
            Log.getLogger().info("Player " + player.getUsername() + " broke a block with the plot wand in hand.");
            PlayerProfile playerProfile = PlayerWrapper.getPlayerProfile(player);
            if (playerProfile == null) return;

            playerProfile.setPlotWandPos1(event.getBlockPosition());
            player.sendMessage(ChatUtils.translateMiniMessage("<green>Position 1 set to <dark_green>"
                    + playerProfile.getPlotWandPos1().blockX()
                    + ", " + playerProfile.getPlotWandPos1().blockY()
                    + ", " + playerProfile.getPlotWandPos1().blockZ()));
            event.setCancelled(true);
        }
    }
}