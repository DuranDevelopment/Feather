package cc.ddev.feather.command.plots.subcommands;

import cc.ddev.feather.api.API;
import cc.ddev.feather.utils.ChatUtils;
import cc.ddev.instanceguard.InstanceGuard;
import cc.ddev.instanceguard.region.Region;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;

public class PlotAddMemberCommand extends Command {
    private final InstanceGuard instanceGuard = API.getInstanceGuard();

    public PlotAddMemberCommand() {
        super("addmember");

        setDefaultExecutor((sender, context) -> sender.sendMessage("Usage: /plot addmember <player>"));

        ArgumentEntity playerArgument = ArgumentType.Entity("player").onlyPlayers(true).singleEntity(true);
        playerArgument.setCallback((sender, exception) -> sender.sendMessage("Invalid player!"));

        addSyntax((sender, context) -> {
            Player player = (Player) sender;
            EntityFinder finder = context.get("player");
            Player target = finder.findFirstPlayer(player);

            for (Region region : instanceGuard.getRegionManager().getRegions()) {
                if (region.containsLocation(new Pos(player.getPosition()))) {
                    region.addMember(target);
                } else {
                    player.sendMessage(ChatUtils.translateMiniMessage("<red>You are not currently on a plot"));
                }
            }
        }, playerArgument);
    }
}
