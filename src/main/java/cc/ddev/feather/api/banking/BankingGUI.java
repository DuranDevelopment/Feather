package cc.ddev.feather.api.banking;

import cc.ddev.feather.api.API;
import cc.ddev.feather.api.config.Messages;
import cc.ddev.feather.api.enums.BankAccountType;
import cc.ddev.feather.banking.BankingInventory;
import cc.ddev.feather.configuration.ConfigManager;
import cc.ddev.feather.utils.ChatUtils;
import de.leonhard.storage.shaded.jetbrains.annotations.NotNull;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;

public class BankingGUI {
    private static BankingGUI instance;

    public static BankingGUI getInstance() {
        if (instance == null) {
            instance = new BankingGUI();
        }
        return instance;
    }

    public void open(@NotNull Player player, @NotNull Player target, BankAccountType type, int id) {
        double balance = type == BankAccountType.PERSONAL ? API.getEconomy().getBalance(target) : BankUtils.getInstance().getBankAccount(id).getBalance();
        Inventory inv = new BankingInventory(balance, type, id);
        ItemStack ghast = this.createItem("ghast");
        ItemStack diamond = this.createItem("diamond");
        ItemStack redstone = this.createItem("redstone");
        ItemStack emerald = this.createItem("emerald");
        ItemStack coal = this.createItem("coal");
        ItemStack iron = this.createItem("iron");
        ItemStack quartz = this.createItem("quartz");
        ItemStack goldingot = this.createItem("gold_ingot");
        ItemStack goldnugget = this.createItem("gold_nugget");
        if (balance >= 2304.0 * this.getValue("ghast")) {
            balance = 2304.0 * this.getValue("ghast");
        }
        while (balance >= this.getValue("ghast") * 64.0 && ghast.material() != Material.STICK && ghast.material() != Material.BARRIER) {
            ItemStack gStack = ItemStack.of(ghast.material()).withLore(ghast.getLore()).withDisplayName(ghast.getDisplayName());
            inv.addItemStack(gStack.withAmount(64));
            balance -= this.getValue("ghast") * 64.0;
        }
        while (balance >= this.getValue("ghast") && ghast.material() != Material.STICK && ghast.material() != Material.BARRIER) {
            balance -= this.getValue("ghast");
            inv.addItemStack(ghast);
        }
        while (balance >= this.getValue("diamond") && diamond.material() != Material.STICK && diamond.material() != Material.BARRIER) {
            balance -= this.getValue("diamond");
            inv.addItemStack(diamond);
        }
        while (balance >= this.getValue("redstone") && emerald.material() != Material.STICK && emerald.material() != Material.BARRIER) {
            balance -= this.getValue("redstone");
            inv.addItemStack(redstone);
        }
        while (balance >= this.getValue("emerald") && emerald.material() != Material.STICK && emerald.material() != Material.BARRIER) {
            balance -= this.getValue("emerald");
            inv.addItemStack(emerald);
        }
        while (balance >= this.getValue("coal") && coal.material() != Material.STICK && coal.material() != Material.BARRIER) {
            balance -= this.getValue("coal");
            inv.addItemStack(coal);
        }
        while (balance >= this.getValue("iron") && iron.material() != Material.STICK && iron.material() != Material.BARRIER) {
            balance -= this.getValue("iron");
            inv.addItemStack(iron);
        }
        while (balance >= this.getValue("quartz") && quartz.material() != Material.STICK && quartz.material() != Material.BARRIER) {
            balance -= this.getValue("quartz");
            inv.addItemStack(quartz);
        }
        while (balance >= this.getValue("gold_ingot") && goldingot.material() != Material.STICK && goldingot.material() != Material.BARRIER) {
            balance -= this.getValue("gold_ingot");
            inv.addItemStack(goldingot);
        }
        while (balance >= this.getValue("gold_nugget") && goldnugget.material() != Material.STICK && goldnugget.material() != Material.BARRIER) {
            balance -= this.getValue("gold_nugget");
            inv.addItemStack(goldnugget);
        }
        ItemStack glass = ItemStack.of(Material.PURPLE_STAINED_GLASS_PANE);
        inv.setItemStack(36, glass);
        inv.setItemStack(37, glass);
        inv.setItemStack(38, glass);
        inv.setItemStack(39, glass);
        inv.setItemStack(40, glass);
        inv.setItemStack(41, glass);
        inv.setItemStack(42, glass);
        inv.setItemStack(43, glass);
        inv.setItemStack(44, glass);
        inv.setItemStack(53, ghast);
        inv.setItemStack(52, diamond);
        inv.setItemStack(51, redstone);
        inv.setItemStack(50, emerald);
        inv.setItemStack(49, coal);
        inv.setItemStack(48, iron);
        inv.setItemStack(47, quartz);
        inv.setItemStack(46, goldingot);
        inv.setItemStack(45, goldnugget);
        player.openInventory(inv);
    }

    public ItemStack createItem(String path) {

        Material material = Material.fromNamespaceId(ConfigManager.getInstance().getBankingConfig().getString("items."+path+".item"));

        if (material == null) {
            return ItemStack.of(Material.BARRIER)
                    .withDisplayName(ChatUtils.translateMiniMessage("<red>Error"))
                    .withLore(ChatUtils.splitStringByNewLineToComponent(
                            "<red>Please contact an administrator to fix this error.\n" +
                                    "<dark_red>Error: Material is null\n" +
                                    "<dark_red>Path: " + path + "\n"
                    ));
        }

        if (material == Material.STICK) {
            return ItemStack.of(Material.STICK);
        }

        double value = this.getValue(path);
        ItemStack item = ItemStack.of(material).withLore(
                ChatUtils.splitStringByNewLineToComponent(
                    Messages.Banking.Menu.ITEM_LORE
                        .replace("<amount>", "" + value)
                        .replace("%newline%", "\n")
                )
        );
        return item.withTag(Tag.Double("feathervalue"), value);
    }

    public double getValue(String path) {
        return ConfigManager.getInstance().getBankingConfig().getDouble("items."+path+".value");
    }
}