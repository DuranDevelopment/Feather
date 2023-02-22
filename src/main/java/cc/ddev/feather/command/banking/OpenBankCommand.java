package cc.ddev.feather.command.banking;

import cc.ddev.feather.api.banking.BankUtils;
import cc.ddev.feather.api.banking.SelectorGUI;
import cc.ddev.feather.api.enums.ATMOpenType;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;

public class OpenBankCommand extends Command {
    public OpenBankCommand() {
        super("openbank");

        ArgumentEntity playerArgument = ArgumentType.Entity("player").onlyPlayers(true);

        addSyntax((sender, context) -> {
            Player player = (Player) sender;
            EntityFinder finder = context.get(playerArgument);
            Player target = finder.findFirstPlayer(sender);

            if (target == null) {
                sender.sendMessage("Player not found");
                return;
            }

            SelectorGUI.openSelectionMenu(player, target, ATMOpenType.OPENBANK_CMD);
            sender.sendMessage("Opening bank for " + target.getUsername());
            try {
                BankUtils.getInstance().pullCache();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, playerArgument);

        // If no player is specified, open the bank for the sender
        addSyntax((sender, context) -> {
            Player player = (Player) sender;
            SelectorGUI.openSelectionMenu(player, player, ATMOpenType.OPENBANK_CMD);
            sender.sendMessage("Opening bank for " + player.getUsername());
            try {
                BankUtils.getInstance().pullCache();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
