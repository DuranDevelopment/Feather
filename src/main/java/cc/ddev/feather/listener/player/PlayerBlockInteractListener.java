package cc.ddev.feather.listener.player;

import cc.ddev.feather.api.banking.SelectorGUI;
import cc.ddev.feather.api.config.BankingConfig;
import cc.ddev.feather.api.config.Messages;
import cc.ddev.feather.api.enums.ATMOpenType;
import cc.ddev.feather.listener.handler.Listen;
import cc.ddev.feather.listener.handler.Listener;
import cc.ddev.feather.utils.ChatUtils;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;

public class PlayerBlockInteractListener implements Listener {
    @Listen
    public void onPlayerBlockInteract(PlayerBlockInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getBlock() == Block.RED_SANDSTONE_STAIRS) {
            if (BankingConfig.ATM.REQUIRE_DEBIT_CARD) {
                if (!player.getItemInMainHand().equals(ItemStack.of(BankingConfig.ATM.DEBIT_CARD_ITEM))) {
                    event.setCancelled(true);
                    player.sendMessage(ChatUtils.translateMiniMessage(Messages.ATM.DEBIT_CARD_REQUIRED));
                    return;
                }
            }
            SelectorGUI.openSelectionMenu(player, player, ATMOpenType.CLICK_ATM);
        }
    }
}
