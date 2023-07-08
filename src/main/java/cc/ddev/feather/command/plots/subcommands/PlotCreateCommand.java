package cc.ddev.feather.command.plots.subcommands;

import cc.ddev.feather.api.API;
import cc.ddev.feather.api.playerdata.PlayerManager;
import cc.ddev.feather.player.PlayerProfile;
import cc.ddev.feather.player.PlayerWrapper;
import cc.ddev.feather.world.WorldManager;
import cc.ddev.instanceguard.flag.DefaultFlag;
import cc.ddev.instanceguard.flag.DefaultFlagValue;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentBoolean;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

import java.util.UUID;

public class PlotCreateCommand extends Command {

    public PlotCreateCommand() {
        super("create");

        ArgumentString plotNameArgument = ArgumentType.String("plotName");
        ArgumentBoolean topToBottomArgument = ArgumentType.Boolean("topToBottom");

//        setCondition((sender, command) -> sender.hasPermission("feather.plotcreate"));

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Usage: /plot create <name> [<topToBottom>]");
        });

        addSyntax((sender, context) -> {
            Player player = (Player) sender;
            UUID uuid = player.getUuid();
            Point pos1 = PlayerManager.getPlotWandPos1(uuid);
            Point pos2 = PlayerManager.getPlotWandPos2(uuid);
            PlayerProfile playerProfile = PlayerWrapper.getPlayerProfile(player);
            String plotName = context.get(plotNameArgument);

            if (playerProfile == null) return;
            if (pos1 == null || pos2 == null) {
                player.sendMessage("You must select two positions with the plot wand first.");
                return;
            }
            if (player.getInstance() == null) return;

            String instanceName = WorldManager.getInstanceName(player.getInstance());

            API.getInstanceGuard().getRegionManager().createRegion(
                    plotName, instanceName,
                    new Pos(pos1), new Pos(pos2)
            );
            API.getInstanceGuard().getRegionManager().setFlag(plotName, instanceName, DefaultFlag.USE.getName(), DefaultFlagValue.ALLOW.getValue());
            API.getInstanceGuard().getRegionManager().setFlag(plotName, instanceName, DefaultFlag.USE_GROUP.getName(), DefaultFlagValue.MEMBERS.getValue());
            API.getInstanceGuard().getRegionManager().setFlag(plotName,instanceName, DefaultFlag.INTERACT.getName(), DefaultFlagValue.ALLOW.getValue());
            API.getInstanceGuard().getRegionManager().setFlag(plotName,instanceName, DefaultFlag.INTERACT_GROUP.getName(), DefaultFlagValue.MEMBERS.getValue());
            API.getInstanceGuard().getRegionManager().setFlag(plotName,instanceName, DefaultFlag.PVP.getName(), DefaultFlagValue.ALLOW.getValue());
            API.getInstanceGuard().getRegionManager().setFlag(plotName,instanceName, DefaultFlag.CHEST_ACCESS.getName(), DefaultFlagValue.DENY.getValue());
            API.getInstanceGuard().getRegionManager().setFlag(plotName,instanceName, DefaultFlag.CHEST_ACCESS_GROUP.getName(), DefaultFlagValue.NON_MEMBERS.getValue());
            player.sendMessage("You have created a plot named " + plotName + " at " + pos1 + " and " + pos2 + ".");
        }, plotNameArgument, topToBottomArgument.setDefaultValue(true));
    }
}
