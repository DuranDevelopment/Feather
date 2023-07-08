package cc.ddev.feather.command.plots;

import cc.ddev.feather.command.plots.subcommands.PlotAddMemberCommand;
import cc.ddev.feather.command.plots.subcommands.PlotAddOwnerCommand;
import cc.ddev.feather.command.plots.subcommands.PlotCreateCommand;
import cc.ddev.feather.command.plots.subcommands.PlotSetDescriptionCommand;
import net.minestom.server.command.builder.Command;

public class PlotCommand extends Command {
    public PlotCommand() {
        super("plot");
//        setCondition((sender, command) -> sender.hasPermission("feather.plot"));
        addSubcommand(new PlotCreateCommand());
        addSubcommand(new PlotAddOwnerCommand());
        addSubcommand(new PlotAddMemberCommand());
        addSubcommand(new PlotSetDescriptionCommand());
        setDefaultExecutor((sender, context) -> sender.sendMessage("Usage: /plot <create|addowner|addmember|setdescription>"));
    }
}