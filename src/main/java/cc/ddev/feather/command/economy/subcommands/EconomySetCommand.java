package cc.ddev.feather.command.economy.subcommands;

import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.database.models.PlayerModel;
import cc.ddev.feather.player.PlayerProfile;
import cc.ddev.feather.player.PlayerWrapper;
import cc.ddev.feather.api.sidebar.SidebarManager;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.arguments.number.ArgumentDouble;
import net.minestom.server.entity.Player;

public class EconomySetCommand extends Command {
    public EconomySetCommand() {
        super("set");

        ArgumentDouble amountArgument = ArgumentType.Double("amount");
        ArgumentEntity playerArgument = ArgumentType.Entity("player").onlyPlayers(true);

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Usage: /eco set <player> <amount>");
        });

        addSyntax((sender, context) -> {
            Player player = (Player) sender;
            Player target = context.get(playerArgument).findFirstPlayer(player);

            if (target == null) {
                sender.sendMessage(Component.text("<red>Player not found."));
                return;
            }

            PlayerProfile playerProfile = PlayerWrapper.getPlayerProfile(target);
            PlayerModel playerModel = playerProfile.getPlayerModel();
            playerModel.setBalance(context.get("amount"));
            StormDatabase.getInstance().saveStormModel(playerModel);
            SidebarManager.refreshSidebar(player);
            sender.sendMessage(Component.text("Set ").append(playerProfile.getUsername()).append(Component.text("'s balance to ").append(Component.text(playerModel.getBalance()))));
        }, playerArgument, amountArgument);
    }
}
