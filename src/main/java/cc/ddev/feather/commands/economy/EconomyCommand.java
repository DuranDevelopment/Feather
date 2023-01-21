package cc.ddev.feather.commands.economy;

import cc.ddev.feather.commands.economy.subcommands.EconomySetCommand;
import net.minestom.server.command.builder.Command;

public class EconomyCommand extends Command {
    public EconomyCommand() {
        super("economy");
        addSubcommand(new EconomySetCommand());

        setDefaultExecutor((sender, context) -> sender.sendMessage("Usage: /eco <set|add> <player> <amount>"));
    }
}
