package cc.ddev.feather.command.banking.bankaccount.subcommands;

import cc.ddev.feather.api.banking.BankUtils;
import cc.ddev.feather.api.banking.Bankaccount;
import cc.ddev.feather.api.config.Messages;
import cc.ddev.feather.api.enums.BankPermission;
import cc.ddev.feather.api.events.bank.BankAccountAddUserEvent;
import cc.ddev.feather.logger.Log;
import cc.ddev.feather.utils.ChatUtils;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;

import java.util.Arrays;
import java.util.stream.Collectors;

public class AddUserBankAccountSubCommand extends Command {
    public AddUserBankAccountSubCommand() {
        super("adduser");

        ArgumentEntity playerArgument = ArgumentType.Entity("player").onlyPlayers(true);
        ArgumentInteger idArgument = ArgumentType.Integer("id");
        ArgumentString permissionArgument = ArgumentType.String("permission");
        boolean isEnglish = Messages.LANGUAGE.equalsIgnoreCase("en");

        addSyntax((sender, context) -> {
            Player player = (Player) sender;
            Player target = context.get(playerArgument).findFirstPlayer(player);
            int id = context.get(idArgument);

            if (BankUtils.getInstance().getBankAccount(id) == null) {
                sender.sendMessage(ChatUtils.translateMiniMessage(Messages.BankaccountCMD.ID_NOTEXIST));
                return;
            }
            String permission = context.get(permissionArgument);

            BankPermission bankPermission;
            try {
                bankPermission = BankPermission.getPermission(permission);
            } catch (IllegalArgumentException ex) {
                sender.sendMessage(ChatUtils.translateMiniMessage(Messages.BankaccountCMD.INVALID_BANKPERMISSION.replace("<permissions>", Arrays.stream(BankPermission.values()).map(isEnglish ? BankPermission::getEnglish : BankPermission::getDutch).collect(Collectors.joining(", ")))));
                return;
            }

            if (target == null || !target.isOnline()) {
                sender.sendMessage(ChatUtils.translateMiniMessage(Messages.NOT_ONLINE));
                return;
            }
            BankAccountAddUserEvent addUserEvent = new BankAccountAddUserEvent(sender, target, bankPermission, id);
            MinecraftServer.getGlobalEventHandler().call(addUserEvent);
            if (addUserEvent.isCancelled()) {
                return;
            }
            if (!BankUtils.getInstance().getBankAccount(id).getAuthorisedUsers().contains(target.getUuid())) {
                Bankaccount bankaccount = BankUtils.getInstance().getBankAccount(id);
                bankaccount.addUser(target.getUuid(), bankPermission);
                String message = isEnglish ? bankPermission.getEnglish() : bankPermission.getDutch();
                if (bankPermission == BankPermission.ADMIN) {
                    message = isEnglish ? "deposit and withdraw" : "opnemen en storten";
                }
                sender.sendMessage(ChatUtils.translateMiniMessage(Messages.BankaccountCMD.AddUser.SUCCESS.replace("<player>", target.getUsername()).replace("<permission>", message)));
            } else {
                sender.sendMessage(ChatUtils.translateMiniMessage(Messages.BankaccountCMD.AddUser.ALREADY_ADDED.replace("<player>", target.getUsername())));
            }
        }, playerArgument, idArgument, permissionArgument.setDefaultValue("both"));
    }
}
