package cc.ddev.feather.banking;

import cc.ddev.feather.api.banking.BankUtils;
import cc.ddev.feather.api.config.Messages;
import cc.ddev.feather.api.enums.BankAccountType;
import cc.ddev.feather.utils.ChatUtils;
import me.hsgamer.hscore.minestom.gui.MinestomGUIDisplay;
import me.hsgamer.hscore.minestom.gui.MinestomGUIHolder;
import me.hsgamer.hscore.minestom.gui.inventory.DelegatingInventory;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;

import java.util.UUID;

public class BankingInventory extends DelegatingInventory {

    private static BankingInventory instance;
    private final BankAccountType type;

    private final int id;

    /**
     * Create a new inventory
     *
     */
    public BankingInventory(double balance, BankAccountType type, int id) {
        super(InventoryType.CHEST_6_ROW
                , ChatUtils.translateMiniMessage(Messages.Banking.Menu.TITLE.replace("<amount>", BankUtils.getInstance().format(balance)))
                , new MinestomGUIDisplay(UUID.randomUUID(), new MinestomGUIHolder()));
        this.id = id;
        this.type = type;
    }

    // Get instance
    public static BankingInventory getInstance(double balance, BankAccountType type, int id) {
        if (instance == null) {
            instance = new BankingInventory(balance, type, id);
        }
        return instance;
    }

    public BankAccountType getBankAccountType() {
        return type;
    }

    public Player getPlayer() {
        return this.getViewers().iterator().next();
    }

    public int getId() {
        return id;
    }
}
