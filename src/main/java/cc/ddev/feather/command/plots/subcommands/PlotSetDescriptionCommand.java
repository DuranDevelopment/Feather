package cc.ddev.feather.command.plots.subcommands;

import cc.ddev.feather.api.API;
import cc.ddev.feather.utils.ChatUtils;
import cc.ddev.instanceguard.flag.FlagValue;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

public class PlotSetDescriptionCommand extends Command {
    public PlotSetDescriptionCommand() {
        super("setdescription");

        setDefaultExecutor((sender, context) -> sender.sendMessage("Usage: /plot setdescription <description>"));

        ArgumentString descriptionArgument = ArgumentType.String("description");
        descriptionArgument.setCallback((sender, exception) -> sender.sendMessage("Invalid description!"));

        addSyntax((sender, context) -> {
            Player player = (Player) sender;
            if (API.getInstanceGuard().getRegionManager().getRegion(player.getPosition()) == null) {
                player.sendMessage(ChatUtils.translateMiniMessage("<red>You are currently not on a plot."));
                return;
            }
            String description = context.get("description");
            API.getInstanceGuard().getRegionManager().getRegion(player.getPosition()).setFlag("feather-description", new FlagValue<>(description));
            player.sendMessage(ChatUtils.translateMiniMessage("<dark_aqua>Set the description of the plot to <aqua>" + context.get("description") + "<dark_aqua>."));
        }, ArgumentType.String("description"));
    }
}
