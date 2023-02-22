package cc.ddev.feather.command.mtworld;

import cc.ddev.feather.command.mtworld.subcommands.*;
import net.minestom.server.command.builder.Command;

public class MTWorldCommand extends Command {
    public MTWorldCommand() {
        super("mtworld");
        addSubcommand(new MTWorldAddCommand());
        addSubcommand(new MTWorldRemoveCommand());
        addSubcommand(new MTWorldSetColorCommand());
        addSubcommand(new MTWorldSetLoadingNameCommand());
        addSubcommand(new MTWorldSetTemperatureCommand());
        addSubcommand(new MTWorldSetTitleCommand());

        setDefaultExecutor((sender, context) -> sender.sendMessage("Usage: /mtworld <add|remove|setcolor|setloadingname|settemperature|settitle>"));
    }
}
