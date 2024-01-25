package cc.ddev.feather.listener.player;

import cc.ddev.feather.listener.handler.Listen;
import cc.ddev.feather.listener.handler.Listener;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.network.packet.server.play.OpenSignEditorPacket;

public class PlayerBlockPlaceListener implements Listener {
    @Listen
    public void onPlayerBlockPlace(PlayerBlockPlaceEvent event) {
        if (event.getBlock().name().endsWith("sign")) {
            OpenSignEditorPacket openSignEditorPacket = new OpenSignEditorPacket(event.getBlockPosition(), true);
            event.getPlayer().getPlayerConnection().sendPacket(openSignEditorPacket);
        }
    }
}
