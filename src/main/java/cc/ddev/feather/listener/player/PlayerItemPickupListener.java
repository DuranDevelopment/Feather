package cc.ddev.feather.listener.player;

import cc.ddev.feather.listener.handler.Listen;
import cc.ddev.feather.listener.handler.Listener;
import net.minestom.server.entity.Player;
import net.minestom.server.event.item.PickupItemEvent;

public class PlayerItemPickupListener implements Listener {
    @Listen
    public void onPlayerPickupItem(PickupItemEvent event) {
        Player player = (Player) event.getEntity();
        boolean couldAdd = player.getInventory().addItemStack(event.getItemStack());
        event.setCancelled(!couldAdd); // Cancel event if player does not have enough inventory space
    }
}
