package cc.ddev.feather.command.plots.subcommands;

import cc.ddev.feather.api.API;
import cc.ddev.feather.utils.ChatUtils;
import cc.ddev.instanceguard.InstanceGuard;
import cc.ddev.instanceguard.region.Region;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;

public class PlotRemoveMemberCommand extends Command {

    private final InstanceGuard instanceGuard = API.getInstanceGuard();

    public PlotRemoveMemberCommand() {
        super("removemember");

        setDefaultExecutor((sender, context) -> sender.sendMessage("Usage: /plot removemember <player>"));

        ArgumentEntity playerArgument = ArgumentType.Entity("player").onlyPlayers(true).singleEntity(true);
        playerArgument.setCallback((sender, exception) -> sender.sendMessage("Invalid player!"));

        addSyntax((sender, context) -> {
            Player player = (Player) sender;
            EntityFinder finder = context.get("player");
            Player target = finder.findFirstPlayer(player);

            if (target == null) return;

            for (Region region : instanceGuard.getRegionManager().getRegions()) {
                if (region.containsLocation(player.getPosition())) {
                    region.removeMember(target);
                    player.sendMessage(ChatUtils.translateMiniMessage("<dark_aqua>You have successfully removed <aqua>" + target.getUsername() + " <dark_aqua>as a member of this plot."));
                } else {
                    player.sendMessage("<red>You are not currently on a plot.");
                }
            }
        }, playerArgument);
    }
}
