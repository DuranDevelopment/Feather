package cc.ddev.feather.command.plots.subcommands;

import cc.ddev.feather.api.API;
import cc.ddev.feather.api.playerdata.PlayerManager;
import cc.ddev.feather.logger.Log;
import cc.ddev.feather.player.PlayerProfile;
import cc.ddev.feather.player.PlayerWrapper;
import cc.ddev.feather.utils.ChatUtils;
import cc.ddev.feather.world.WorldManager;
import cc.ddev.instanceguard.flag.DefaultFlag;
import cc.ddev.instanceguard.flag.DefaultFlagValue;
import cc.ddev.instanceguard.region.Region;
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
            Point pos1 = API.getPlayerManager().getPlotWandPos1(uuid);
            Point pos2 = API.getPlayerManager().getPlotWandPos2(uuid);
            PlayerProfile playerProfile = PlayerWrapper.getPlayerProfile(player);
            String plotName = context.get(plotNameArgument);
            boolean topToBottom = context.get(topToBottomArgument);

            if (playerProfile == null) return;
            if (pos1 == null || pos2 == null) {
                player.sendMessage(ChatUtils.translateMiniMessage("<dark_aqua>You must select two positions with the <aqua>plot wand <dark_aqua>first."));
                return;
            }
            for (Region region : API.getInstanceGuard().getRegionManager().getRegions()) {
                if (region.getName().equalsIgnoreCase(plotName)) {
                    player.sendMessage(ChatUtils.translateMiniMessage("<red>A plot with that name already exists."));
                    return;
                }
            }

            if (player.getInstance() == null) return;

            if (!topToBottom) {
                API.getInstanceGuard().getRegionManager().createRegion(
                        plotName, player.getInstance(),
                        new Pos(pos1), new Pos(pos2)
                );
            } else {
                API.getInstanceGuard().getRegionManager().createRegion(
                        plotName, player.getInstance(),
                        new Pos(pos2.withY(player.getInstance().getDimensionType().getMinY())),
                        new Pos(pos1.withY(player.getInstance().getDimensionType().getMaxY()))
                );
            }

            API.getInstanceGuard().getRegionManager().setFlag(plotName, player.getInstance(), DefaultFlag.USE.getName(), DefaultFlagValue.ALLOW.getValue());
            API.getInstanceGuard().getRegionManager().setFlag(plotName, player.getInstance(), DefaultFlag.USE_GROUP.getName(), DefaultFlagValue.MEMBERS.getValue());
            API.getInstanceGuard().getRegionManager().setFlag(plotName, player.getInstance(), DefaultFlag.INTERACT.getName(), DefaultFlagValue.ALLOW.getValue());
            API.getInstanceGuard().getRegionManager().setFlag(plotName, player.getInstance(), DefaultFlag.INTERACT_GROUP.getName(), DefaultFlagValue.MEMBERS.getValue());
            API.getInstanceGuard().getRegionManager().setFlag(plotName, player.getInstance(), DefaultFlag.PVP.getName(), DefaultFlagValue.ALLOW.getValue());
            API.getInstanceGuard().getRegionManager().setFlag(plotName, player.getInstance(), DefaultFlag.CHEST_ACCESS.getName(), DefaultFlagValue.DENY.getValue());
            API.getInstanceGuard().getRegionManager().setFlag(plotName, player.getInstance(), DefaultFlag.CHEST_ACCESS_GROUP.getName(), DefaultFlagValue.NON_MEMBERS.getValue());
            API.getInstanceGuard().getRegionManager().setFlag(plotName, player.getInstance(), DefaultFlag.BUILD.getName(), DefaultFlagValue.ALLOW.getValue());
            API.getInstanceGuard().getRegionManager().setFlag(plotName, player.getInstance(), DefaultFlag.BUILD_GROUP.getName(), DefaultFlagValue.MEMBERS.getValue());

            player.sendMessage(ChatUtils.translateMiniMessage("<dark_aqua>Successfully created plot with the name <aqua>" + plotName + "<dark_aqua>."));
        }, plotNameArgument, topToBottomArgument.setDefaultValue(true));
    }
}
