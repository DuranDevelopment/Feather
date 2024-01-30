package cc.ddev.feather.command.phone;

import cc.ddev.feather.command.mtworld.subcommands.*;
import net.minestom.server.command.builder.Command;

public class PhoneCommand extends Command {
    public PhoneCommand() {
        super("phone");
        addSubcommand(new MTWorldAddCommand());
        addSubcommand(new MTWorldRemoveCommand());

        setDefaultExecutor((sender, context) -> sender.sendMessage("Usage: /phone <get|setcredit>"));
    }

}
