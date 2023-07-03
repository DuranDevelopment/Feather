package cc.ddev.feather.command.plots.subcommands;

import cc.ddev.feather.Server;
import cc.ddev.feather.api.API;
import cc.ddev.feather.player.PlayerProfile;
import cc.ddev.feather.player.PlayerWrapper;
import cc.ddev.feather.world.WorldManager;
import cc.ddev.instanceguard.flag.DefaultFlag;
import cc.ddev.instanceguard.flag.FlagValue;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentBoolean;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlotCreateCommand extends Command {

    public PlotCreateCommand() {
        super("create");

        ArgumentString plotNameArgument = ArgumentType.String("plotName");
        ArgumentBoolean topToBottomArgument = ArgumentType.Boolean("topToBottom");

//        setCondition((sender, command) -> sender.hasPermission("feather.plotcreate"));

        setDefaultExecutor((sender, context) -> {
            Player player = (Player) sender;
            PlayerProfile playerProfile = PlayerWrapper.getPlayerProfile(player);
            String plotName = context.get(plotNameArgument);

            if (playerProfile == null) return;
            if (playerProfile.getPlotWandPos1() == null || playerProfile.getPlotWandPos2() == null) {
                player.sendMessage("You must select two positions with the plot wand first.");
                return;
            }
            if (player.getInstance() == null) return;
            Map<DefaultFlag, FlagValue> flags = new HashMap<>();
            flags.put(DefaultFlag.USE, FlagValue.ALLOW);
            flags.put(DefaultFlag.USE_GROUP, FlagValue.MEMBERS);
            flags.put(DefaultFlag.INTERACT, FlagValue.ALLOW);
            flags.put(DefaultFlag.INTERACT_GROUP, FlagValue.MEMBERS);
            flags.put(DefaultFlag.PVP, FlagValue.ALLOW);
            flags.put(DefaultFlag.CHEST_ACCESS, FlagValue.DENY);
            flags.put(DefaultFlag.CHEST_ACCESS_GROUP, FlagValue.NON_MEMBERS);
            API.getInstanceGuard().getRegionManager().createRegion(
                    plotName,
                    WorldManager.getInstanceName(player.getInstance()),
                    new Pos(playerProfile.getPlotWandPos1()),
                    new Pos(playerProfile.getPlotWandPos2())
            );
            player.sendMessage("Plot created.");
        });
    }
}
