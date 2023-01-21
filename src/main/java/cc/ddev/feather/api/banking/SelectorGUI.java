package cc.ddev.feather.api.banking;

import cc.ddev.feather.api.config.BankingConfig;
import cc.ddev.feather.api.config.Messages;
import cc.ddev.feather.api.enums.ATMOpenType;
import cc.ddev.feather.api.enums.BankAccountType;
import cc.ddev.feather.api.events.player.ATMOpenEvent;
import cc.ddev.feather.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;

import java.util.List;

public class SelectorGUI {
    public static void openSelectionMenu(Player player, Player target, ATMOpenType reason) {
        if (BankingConfig.ATM.DISABLE_BANKACCOUNTS) {
            SelectorGUI.openAccountSelector(player, target, BankAccountType.PERSONAL);
            return;
        }
        ATMOpenEvent event = new ATMOpenEvent(player, target, reason);
        MinecraftServer.getGlobalEventHandler().call(event);
        if (event.isCancelled()) {
            return;
        }
        Inventory inv = new Inventory(InventoryType.CHEST_3_ROW, ChatUtils.translateMiniMessage(Messages.Banking.Selector.TITLE));
        ItemStack savingsAccountBlock = BankingConfig.Blocks.SAVINGS_ACCOUNT.withDisplayName(ChatUtils.translateMiniMessage(Messages.Banking.Selector.SAVINGS));
        ItemStack privateAccountBlock = BankingConfig.Blocks.PRIVATE_ACCOUNT.withDisplayName(ChatUtils.translateMiniMessage(Messages.Banking.Selector.PRIVATE));
        ItemStack businessAccountBlock = BankingConfig.Blocks.BUSINESS_ACCOUNT.withDisplayName(ChatUtils.translateMiniMessage(Messages.Banking.Selector.BUSINESS));
        if (BankUtils.getInstance().getAccounts(target.getUuid(), BankAccountType.GOVERNMENT).size() > 0) {
            inv.setItemStack(10, BankingConfig.Blocks.GOVERNMENT_ACCOUNT.withDisplayName(ChatUtils.translateMiniMessage(Messages.Banking.Selector.GOVERNMENT)));
            inv.setItemStack(12, savingsAccountBlock);
            inv.setItemStack(14, privateAccountBlock);
            inv.setItemStack(16, businessAccountBlock);
        } else {
            inv.setItemStack(11, savingsAccountBlock);
            inv.setItemStack(13, privateAccountBlock);
            inv.setItemStack(15, businessAccountBlock);
        }
        player.openInventory(inv);
    }

    public static void openAccountSelector(Player player, Player target, BankAccountType type) {
        Inventory inv = new Inventory(InventoryType.CHEST_2_ROW, Messages.Banking.Selector.ACCOUNT_TITLE);
        if (type == BankAccountType.PERSONAL) {
            inv.setItemStack(0, BankingConfig.Blocks.PRIVATE_ACCOUNT.withDisplayName(target.getName()).withLore(List.of(ChatUtils.translateMiniMessage(Messages.Banking.Selector.ACCOUNT_LORE.replace("<ID>", "2491")))));
        } else {
            ItemStack mat = null;
            if (type == BankAccountType.SAVINGS) {
                mat = BankingConfig.Blocks.SAVINGS_ACCOUNT;
            } else if (type == BankAccountType.BUSINESS) {
                mat = BankingConfig.Blocks.BUSINESS_ACCOUNT;
            } else if (type == BankAccountType.GOVERNMENT) {
                mat = BankingConfig.Blocks.GOVERNMENT_ACCOUNT;
            }
            for (Bankaccount acc : BankUtils.getInstance().getAccounts(target.getUuid(), type)) {
                ItemStack accItem = mat.withDisplayName(Component.text(acc.getName())).withLore(List.of(ChatUtils.translateMiniMessage(Messages.Banking.Selector.ACCOUNT_LORE.replace("<ID>", "" + acc.getId()))));
                inv.addItemStack(accItem.withTag(Tag.Integer("accountId"), acc.getId()));
            }
        }
        player.openInventory(inv);
    }
}