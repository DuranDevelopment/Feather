package cc.ddev.feather.listener.player;

import cc.ddev.feather.api.API;
import cc.ddev.feather.api.config.Config;
import cc.ddev.feather.api.playerdata.PlayerManager;
import cc.ddev.feather.listener.handler.Listen;
import cc.ddev.feather.listener.handler.Listener;
import cc.ddev.feather.logger.Log;
import cc.ddev.feather.player.PlayerProfile;
import cc.ddev.feather.player.PlayerWrapper;
import cc.ddev.feather.utils.ChatUtils;
import net.kyori.adventure.text.format.Style;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerBlockBreakEvent;

import java.util.UUID;

public class PlayerBlockBreakListener implements Listener {
    @Listen
    public void onPlayerBlockBreak(PlayerBlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUuid();
        if (player.getItemInMainHand().material() == Config.Plot.PLOTWAND.material()
        && player.getItemInMainHand().getDisplayName() == Config.Plot.PLOTWAND.getDisplayName()) {
            API.getPlayerManager().setPlotWandPos1(player.getUuid(), event.getBlockPosition());
            player.sendMessage(ChatUtils.translateMiniMessage("<green>Position 1 set to <dark_green>"
                    + API.getPlayerManager().getPlotWandPos1(uuid).blockX()
                    + ", " + API.getPlayerManager().getPlotWandPos1(uuid).blockY()
                    + ", " + API.getPlayerManager().getPlotWandPos1(uuid).blockZ()));
            event.setCancelled(true);
        }
    }
}