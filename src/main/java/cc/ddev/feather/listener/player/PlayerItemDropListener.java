package cc.ddev.feather.listener.player;

import cc.ddev.feather.listener.handler.Listen;
import cc.ddev.feather.listener.handler.Listener;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.utils.time.TimeUnit;

public class PlayerItemDropListener implements Listener {

    @Listen
    public void onPlayerItemDrop(ItemDropEvent event) {
        Player player = event.getPlayer();
        ItemStack droppedItem = event.getItemStack();

        ItemEntity itemEntity = new ItemEntity(droppedItem);
        itemEntity.setPickupDelay(500, TimeUnit.MILLISECOND);
        itemEntity.setInstance(player.getInstance());
        itemEntity.teleport(player.getPosition().add(0, 1.5f, 0));

        Vec velocity = player.getPosition().direction().mul(6);
        itemEntity.setVelocity(velocity);
    }
}
