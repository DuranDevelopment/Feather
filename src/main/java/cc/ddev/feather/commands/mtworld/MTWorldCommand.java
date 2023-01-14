package cc.ddev.feather.commands.mtworld;

import cc.ddev.feather.commands.mtworld.subcommands.*;
import net.minestom.server.command.builder.Command;

public class MTWorldCommand extends Command {
    public MTWorldCommand() {
        super("mtworld");
        addSubcommand(new MTWorldAdd());
        addSubcommand(new MTWorldRemove());
        addSubcommand(new MTWorldSetColor());
        addSubcommand(new MTWorldSetLoadingName());
        addSubcommand(new MTWorldSetTemperature());
        addSubcommand(new MTWorldSetTitle());

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Usage: /mtworld <add|remove|setcolor|setloadingname|settemperature|settitle>");
        });
    }
}
