package cc.ddev.feather.listener.player;

import cc.ddev.feather.api.phone.PhoneManager;
import cc.ddev.feather.listener.handler.Listen;
import cc.ddev.feather.listener.handler.Listener;
import net.minestom.server.event.player.PlayerUseItemEvent;

public class PlayerInteractListener implements Listener {

    @Listen
    public void onPlayerInteract(PlayerUseItemEvent event) {
        PhoneManager.getInstance().createNewPhone(1, event.getPlayer().getUuid());
        PhoneManager.getInstance().getPhone(1);

    }
}
