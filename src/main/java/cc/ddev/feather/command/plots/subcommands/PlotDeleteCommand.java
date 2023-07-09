package cc.ddev.feather.command.plots.subcommands;

import cc.ddev.feather.api.API;
import cc.ddev.feather.utils.ChatUtils;
import cc.ddev.instanceguard.InstanceGuard;
import cc.ddev.instanceguard.region.Region;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

public class PlotDeleteCommand extends Command {

    InstanceGuard instanceGuard = API.getInstanceGuard();

    public PlotDeleteCommand() {
        super("delete");

        setDefaultExecutor((sender, context) -> sender.sendMessage("Usage: /plot delete <plot>"));

        ArgumentString plotNameArgument = ArgumentType.String("plotName");

        addSyntax((sender, context) -> {
            Player player = (Player) sender;
            String plotName = context.get(plotNameArgument);

            int plotCount = 0;
            for (Region region : instanceGuard.getRegionManager().getRegions()) {
                if (region.getName().equals(plotName)) {
                    instanceGuard.getRegionManager().removeRegion(region);
                    player.sendMessage(ChatUtils.translateMiniMessage("<dark_aqua>Successfully deleted plot <aqua>" + plotName + "<dark_aqua>."));
                    return;
                }
                plotCount++;
            }

            // The loop has iterated over all the plots without finding a match
            if (plotCount == instanceGuard.getRegionManager().getRegions().size()) {
                player.sendMessage(ChatUtils.translateMiniMessage("<red>Plot <dark_red>" + plotName + " <red>does not exist."));
            }

        }, plotNameArgument);
    }
}
