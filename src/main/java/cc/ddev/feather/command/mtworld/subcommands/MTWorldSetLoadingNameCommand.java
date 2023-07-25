package cc.ddev.feather.command.mtworld.subcommands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;

public class MTWorldSetLoadingNameCommand extends Command {
    public MTWorldSetLoadingNameCommand() {
        super("setloadingname");

        ArgumentString worldArgument = ArgumentType.String("world");

        setDefaultExecutor((sender, context) -> sender.sendMessage("Usage: /mtworld setloadingname <world> <loadingname>"));
    }
}
