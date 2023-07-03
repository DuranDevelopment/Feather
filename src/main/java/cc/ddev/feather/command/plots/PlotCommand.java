package cc.ddev.feather.command.plots;

import cc.ddev.feather.command.plots.subcommands.PlotCreateCommand;
import net.minestom.server.command.builder.Command;

public class PlotCommand extends Command {
    public PlotCommand() {
        super("plot");
//        setCondition((sender, command) -> sender.hasPermission("feather.plot"));
        addSubcommand(new PlotCreateCommand());
        setDefaultExecutor((sender, context) -> sender.sendMessage("Usage: /plot <create>"));
    }
}