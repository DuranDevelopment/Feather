package cc.ddev.feather.command.economy;

import cc.ddev.feather.command.economy.subcommands.EconomySetCommand;
import net.minestom.server.command.builder.Command;

public class EconomyCommand extends Command {
    public EconomyCommand() {
        super("economy", "eco");
        addSubcommand(new EconomySetCommand());

        setDefaultExecutor((sender, context) -> sender.sendMessage("Usage: /eco <set|add> <player> <amount>"));
    }
}
