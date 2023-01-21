package cc.ddev.feather.commands.economy.subcommands;

import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.database.models.PlayerModel;
import cc.ddev.feather.player.PlayerProfile;
import cc.ddev.feather.player.PlayerWrapper;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentDouble;
import net.minestom.server.entity.Player;

public class EconomySetCommand extends Command {
    public EconomySetCommand() {
        super("set");

        ArgumentDouble amountArgument = ArgumentType.Double("amount");

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Usage: /eco set <player> <amount>");
        });

        addSyntax((sender, context) -> {
            PlayerProfile playerProfile = PlayerWrapper.getPlayerProfile((Player) sender);
            PlayerModel playerModel = playerProfile.getPlayerModel();
            playerModel.setBalance(context.get("amount"));
            StormDatabase.getInstance().saveStormModel(playerModel);
            sender.sendMessage(Component.text("Set ").append(playerProfile.getUsername()).append(Component.text("'s balance to ").append(Component.text(playerModel.getBalance()))));
        }, amountArgument);
    }
}
