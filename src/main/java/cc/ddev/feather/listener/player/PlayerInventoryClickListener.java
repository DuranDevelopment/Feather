package cc.ddev.feather.listener.player;

import cc.ddev.feather.api.banking.BankUtils;
import cc.ddev.feather.api.banking.Bankaccount;
import cc.ddev.feather.api.banking.BankingGUI;
import cc.ddev.feather.api.banking.SelectorGUI;
import cc.ddev.feather.api.config.BankingConfig;
import cc.ddev.feather.api.config.Messages;
import cc.ddev.feather.api.enums.BankAccountType;
import cc.ddev.feather.banking.BankingHandler;
import cc.ddev.feather.banking.BankingInventory;
import cc.ddev.feather.listener.handler.Listen;
import cc.ddev.feather.listener.handler.Listener;
import cc.ddev.feather.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.tag.Tag;

public class PlayerInventoryClickListener implements Listener {

    @Listen
    public void onPlayerPreClickInventory(InventoryPreClickEvent event) {
        Player player = event.getPlayer();
        Component clickedInventoryTitle;

        if (event.getInventory() == null) {
            clickedInventoryTitle = Component.empty();
        } else {
            clickedInventoryTitle = event.getInventory().getTitle();
        }

        if (ChatUtils.compareComponent(clickedInventoryTitle, Messages.Banking.Selector.TITLE)) {
            if (event.getClickedItem().material() == BankingConfig.Blocks.GOVERNMENT_ACCOUNT.material()) {
                event.setCancelled(true);
                SelectorGUI.openAccountSelector(player, player, BankAccountType.GOVERNMENT);
            }
            if (event.getClickedItem().material() == BankingConfig.Blocks.SAVINGS_ACCOUNT.material()) {
                event.setCancelled(true);
                if (BankUtils.getInstance().getAccounts(player.getUuid(), BankAccountType.SAVINGS).size() == 0) {
                    player.sendMessage(ChatUtils.translateMiniMessage(Messages.Banking.NO_SAVINGS_ACCOUNT));
                    return;
                }
                SelectorGUI.openAccountSelector(player, player, BankAccountType.SAVINGS);
            }
            if (event.getClickedItem().material() == BankingConfig.Blocks.PRIVATE_ACCOUNT.material()) {
                event.setCancelled(true);
                SelectorGUI.openAccountSelector(player, player, BankAccountType.PERSONAL);
            }
            if (event.getClickedItem().material() == BankingConfig.Blocks.BUSINESS_ACCOUNT.material()) {
                event.setCancelled(true);
                if (BankUtils.getInstance().getAccounts(player.getUuid(), BankAccountType.BUSINESS).size() == 0) {
                    player.sendMessage(ChatUtils.translateMiniMessage(Messages.Banking.NO_BUSINESS_ACCOUNT));
                    return;
                }
                SelectorGUI.openAccountSelector(player, player, BankAccountType.BUSINESS);
            }
            if (event.getSlot() < event.getInventory().getSize()) {
                if (event.getClickedItem().material() == BankingConfig.Blocks.PRIVATE_ACCOUNT.material()) {
                    BankingGUI.getInstance().open(player, player, BankAccountType.PERSONAL, -1);
                } else if (event.getClickedItem().hasTag(Tag.Integer("accountId"))) {
                    int accountId = event.getClickedItem().getTag(Tag.Integer("accountId"));
                    Bankaccount bankaccount = BankUtils.getInstance().getBankAccount(accountId);
                    BankingGUI.getInstance().open(player, player, bankaccount.getType(), accountId);
                }
                event.setCancelled(true);
            }
        }

        if (ChatUtils.compareComponent(clickedInventoryTitle, Messages.Banking.Selector.ACCOUNT_TITLE)) {
            if (event.getSlot() < event.getInventory().getSize()) {
                if (event.getClickedItem().material() == BankingConfig.Blocks.BUSINESS_ACCOUNT.material()) {
                    BankingGUI.getInstance().open(player, player, BankAccountType.BUSINESS, event.getClickedItem().getTag(Tag.Integer("accountId")));
                }
                if (event.getClickedItem().material() == BankingConfig.Blocks.GOVERNMENT_ACCOUNT.material()) {
                    BankingGUI.getInstance().open(player, player, BankAccountType.GOVERNMENT, event.getClickedItem().getTag(Tag.Integer("accountId")));
                }
                if (event.getClickedItem().material() == BankingConfig.Blocks.SAVINGS_ACCOUNT.material()) {
                    BankingGUI.getInstance().open(player, player, BankAccountType.SAVINGS, event.getClickedItem().getTag(Tag.Integer("accountId")));
                }
                event.setCancelled(true);
            }
        }

        if (event.getInventory() instanceof BankingInventory bankingInventory) {
            event.setCancelled(true);
            BankingHandler.getInstance().withdrawMoney(player, bankingInventory, event.getClickedItem(), event.getClickType() == ClickType.LEFT_CLICK);
        }
        if (event.getInventory() == null && player.getOpenInventory() instanceof BankingInventory bankingInventory) {
            event.setCancelled(true);
            BankingHandler.getInstance().depositMoney(player, bankingInventory, event.getClickedItem(), event.getClickType() == ClickType.LEFT_CLICK);
        }
    }
}
