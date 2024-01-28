package cc.ddev.feather.command.plots;

import cc.ddev.feather.command.plots.subcommands.*;
import net.minestom.server.command.builder.Command;

public class PlotCommand extends Command {
    public PlotCommand() {
        super("plot");
//        setCondition((sender, command) -> sender.hasPermission("feather.plot"));
        addSubcommand(new PlotCreateCommand());
        addSubcommand(new PlotDeleteCommand());
        addSubcommand(new PlotAddOwnerCommand());
        addSubcommand(new PlotRemoveOwnerCommand());
        addSubcommand(new PlotAddMemberCommand());
        addSubcommand(new PlotRemoveMemberCommand());
        addSubcommand(new PlotSetDescriptionCommand());
        addSubcommand(new PlotCalculateCommand());

        setDefaultExecutor((sender, context) -> sender.sendMessage("Usage: /plot <create|addowner|addmember|setdescription>"));
    }
}