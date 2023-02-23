package cc.ddev.feather.banking;

import cc.ddev.feather.api.API;
import cc.ddev.feather.api.banking.BankUtils;
import cc.ddev.feather.api.banking.Bankaccount;
import cc.ddev.feather.api.banking.BankingGUI;
import cc.ddev.feather.api.config.BankingConfig;
import cc.ddev.feather.api.config.Messages;
import cc.ddev.feather.api.enums.BankAccountType;
import cc.ddev.feather.api.enums.BankPermission;
import cc.ddev.feather.utils.ChatUtils;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.TransactionOption;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;

public class BankingHandler {
    private static BankingHandler instance;

    public static BankingHandler getInstance() {
        if (instance == null) {
            instance = new BankingHandler();
        }
        return instance;
    }

    public void withdrawMoney(Player player, BankingInventory inventory, ItemStack itemstack, boolean isLeftClick) {
        if (itemstack == null || itemstack.material() == Material.AIR) {
            return;
        }

        if (!itemstack.hasTag(Tag.Double("feathervalue"))) return;
        if (itemstack.getTag(Tag.Double("feathervalue")) != 0.0) {
            double itemValue = itemstack.getTag(Tag.Double("feathervalue")) * itemstack.amount();
            if (inventory.getBankAccountType() == BankAccountType.PERSONAL) {
                if (API.getEconomy().getBalance(player) >= itemValue) {
                    if (!player.getInventory().addItemStack(ItemStack.of(itemstack.material())
                            .withAmount(itemstack.amount())
                            .withLore(ChatUtils.splitStringByNewLineToComponent(Messages.Banking.Money.LORE))
                            .withTag(Tag.Double("feathervalue"), 0.0)
                            .withTag(Tag.Boolean("feather_realmoney"), true))) {
                        player.sendMessage(ChatUtils.translateMiniMessage(Messages.Banking.Money.Withdraw.INVENTORY_FULL));
                        return;
                    }
                    API.getEconomy().removeBalance(player, itemValue);
                    player.sendMessage(ChatUtils.translateMiniMessage(Messages.Banking.Money.Withdraw.MESSAGE_PRIVATE
                            .replace("<amount>", BankUtils.getInstance().format(itemValue))
                            .replace("<account>", player.getUsername()))
                    );
                    if (inventory.getPlayer() != null && inventory.getPlayer().isOnline()) {
                        API.updateScoreboard(inventory.getPlayer());
                    }
                    BankingGUI.getInstance().open(player, inventory.getPlayer(), inventory.getBankAccountType(), inventory.getSize());
                } else {
                    player.sendMessage(ChatUtils.translateMiniMessage(Messages.Banking.Money.INSUFFICIENT_BALANCE));
                }
            } else {
                Bankaccount bankaccount = BankUtils.getInstance().getBankAccount(inventory.getId());
                if (bankaccount.hasPermission(inventory.getPlayer().getUuid(), BankPermission.WITHDRAW)) {
                    String accountName = bankaccount.getName().startsWith("<white>ID: ") ? "" + bankaccount.getId() : ChatUtils.stripMiniMessage(ChatUtils.translateMiniMessage(bankaccount.getName()));
                    player.sendMessage(ChatUtils.translateMiniMessage(Messages.Banking.NoBankingPermission.DEPOSIT.replace("<account>", accountName)));
                    return;
                }
                if (bankaccount.isFrozen()) {
                    player.sendMessage(ChatUtils.translateMiniMessage(Messages.Banking.FROZEN));
                    return;
                }
                if (bankaccount.getBalance() >= itemValue) {
                    bankaccount.setBalance(bankaccount.getBalance() - itemValue);
                    if (!player.getInventory().addItemStack(ItemStack.of(itemstack.material())
                            .withAmount(itemstack.amount())
                            .withLore(ChatUtils.splitStringByNewLineToComponent(Messages.Banking.Money.LORE))
                            .withTag(Tag.Double("feathervalue"), 0.0)
                            .withTag(Tag.Boolean("feather_realmoney"), true))) {
                        player.sendMessage(ChatUtils.translateMiniMessage(Messages.Banking.Money.Withdraw.INVENTORY_FULL));
                        return;
                    }
                    String message = null;
                    if (inventory.getBankAccountType() == BankAccountType.SAVINGS) {
                        message = Messages.Banking.Money.Withdraw.MESSAGE_SAVINGS;
                    } else if (inventory.getBankAccountType() == BankAccountType.GOVERNMENT) {
                        message = Messages.Banking.Money.Withdraw.MESSAGE_GOVERNMENT;
                    } else if (inventory.getBankAccountType() == BankAccountType.BUSINESS) {
                        message = Messages.Banking.Money.Withdraw.MESSAGE_BUSINESS;
                    }
                    if (message == null) return;

                    player.sendMessage(ChatUtils.translateMiniMessage(message.replace("<amount>", BankUtils.getInstance().format(itemValue)).replace("<account>", ChatUtils.stripMiniMessage(ChatUtils.translateMiniMessage(bankaccount.getName())))));
                    BankingGUI.getInstance().open(player, inventory.getPlayer(), inventory.getBankAccountType(), bankaccount.getId());
                } else {
                    player.sendMessage(ChatUtils.translateMiniMessage(Messages.Banking.Money.INSUFFICIENT_BALANCE));
                }
            }
        }
    }

    public void depositMoney(Player player, BankingInventory inventory, ItemStack itemstack, boolean isLeftClick) {
        if (!itemstack.hasTag(Tag.Double("feathervalue"))) return;
        double itemValue = BankingConfig.getMaterialValue(itemstack.material());
        if (itemValue != -1.0) {
            double clickValue = isLeftClick ? 1.0 : itemstack.amount();
            double totalValue = itemValue * clickValue;
            if (inventory.getBankAccountType() == BankAccountType.PERSONAL) {
                if (isFakeMoney(inventory, itemstack)) return;
                if (isLeftClick) {
                    ItemStack stack = itemstack.withAmount(1);
                    player.getInventory().takeItemStack(stack, TransactionOption.ALL);
                } else {
                    player.getInventory().takeItemStack(itemstack, TransactionOption.ALL);
                }
                API.getEconomy().addBalance(inventory.getPlayer(), totalValue);
                player.sendMessage(ChatUtils.translateMiniMessage(Messages.Banking.Money.Deposit.MESSAGE_PRIVATE.replace("<amount>", BankUtils.getInstance().format(totalValue)).replace("<account>", inventory.getPlayer().getUsername())));
                if (inventory.getPlayer() != null && inventory.getPlayer().isOnline()) {
                    API.updateScoreboard(inventory.getPlayer());
                }
                BankingGUI.getInstance().open(player, inventory.getPlayer(), inventory.getBankAccountType(), inventory.getSize());
            } else {
                ItemStack stack;
                Bankaccount bankaccount = BankUtils.getInstance().getBankAccount(inventory.getId());
                if (bankaccount.hasPermission(inventory.getPlayer().getUuid(), BankPermission.DEPOSIT)) {
                    String accountName = bankaccount.getName().startsWith("<white>ID: ") ? "" + bankaccount.getId() : ChatUtils.stripMiniMessage(ChatUtils.translateMiniMessage(bankaccount.getName()));
                    player.sendMessage(ChatUtils.translateMiniMessage(Messages.Banking.NoBankingPermission.DEPOSIT.replace("<account>", accountName)));
                    return;
                }
                if (bankaccount.isFrozen()) {
                    player.sendMessage(ChatUtils.translateMiniMessage(Messages.Banking.FROZEN));
                    return;
                }
                if (isFakeMoney(inventory, itemstack)) return;
                if (isLeftClick) {
                    stack = itemstack.withAmount(1);
                    player.getInventory().takeItemStack(stack, TransactionOption.ALL);
                } else {
                    player.getInventory().takeItemStack(itemstack, TransactionOption.ALL);
                }
                bankaccount.setBalance(bankaccount.getBalance() + totalValue);
                String message = null;
                if (inventory.getBankAccountType() == BankAccountType.SAVINGS) {
                    message = Messages.Banking.Money.Deposit.MESSAGE_SAVINGS;
                } else if (inventory.getBankAccountType() == BankAccountType.GOVERNMENT) {
                    message = Messages.Banking.Money.Deposit.MESSAGE_GOVERNMENT;
                } else if (inventory.getBankAccountType() == BankAccountType.BUSINESS) {
                    message = Messages.Banking.Money.Deposit.MESSAGE_BUSINESS;
                } if (message == null) return;
                player.sendMessage(ChatUtils.translateMiniMessage(message.replace("<amount>", BankUtils.getInstance().format(totalValue)).replace("<account>", ChatUtils.stripMiniMessage(ChatUtils.translateMiniMessage(bankaccount.getName())))));
                BankingGUI.getInstance().open(player, inventory.getPlayer(), inventory.getBankAccountType(), inventory.getId());
            }
        }
    }

    private boolean isFakeMoney(BankingInventory inventory, ItemStack itemstack) {
        if (!(!BankingConfig.ATM.REALMONEY_ENABLED || itemstack.getTag(Tag.Boolean("feather_realmoney")) != null || !itemstack.getLore().isEmpty() && ChatUtils.compareComponent(itemstack.getLore().get(0), Messages.Banking.Money.LORE.split("\n")[0]))) {
            inventory.getPlayer().sendMessage(ChatUtils.translateMiniMessage(Messages.Banking.Money.IS_FAKE_MONEY));
            return true;
        }
        return false;
    }
}