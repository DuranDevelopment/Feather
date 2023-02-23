package cc.ddev.feather.api.events.bank;

import cc.ddev.feather.api.banking.BankUtils;
import cc.ddev.feather.api.enums.BankAccountType;
import cc.ddev.feather.api.enums.BankPermission;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;

public class BankAccountAddUserEvent implements Event {
    private final CommandSender player;
    private boolean cancelled = false;
    private final int bankId;
    private final Player addedPlayer;
    private final BankAccountType accountType;
    private final BankPermission permission;

    public BankAccountAddUserEvent(CommandSender player, Player addedPlayer, BankPermission permission, int bankId) {
        this.player = player;
        this.bankId = bankId;
        this.accountType = BankUtils.getInstance().getBankAccount(bankId).getType();
        this.permission = permission;
        this.addedPlayer = addedPlayer;
    }

    public CommandSender getPlayer() {
        return this.player;
    }

    public Player getAddedPlayer() {
        return this.addedPlayer;
    }

    public BankPermission getPermission() {
        return this.permission;
    }

    public int getAccountId() {
        return this.bankId;
    }

    public BankAccountType getBankAccountType() {
        return this.accountType;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}