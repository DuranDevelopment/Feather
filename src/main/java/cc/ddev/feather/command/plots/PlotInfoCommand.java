package cc.ddev.feather.command.plots;

import cc.ddev.feather.api.API;
import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.database.models.PlayerModel;
import cc.ddev.feather.utils.ChatUtils;
import cc.ddev.instanceguard.InstanceGuard;
import cc.ddev.instanceguard.flag.FlagValue;
import cc.ddev.instanceguard.region.Region;
import com.craftmend.storm.api.enums.Where;
import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class PlotInfoCommand extends Command {

    private final InstanceGuard instanceGuard = API.getInstanceGuard();

    public PlotInfoCommand() {
        super("plotinfo", "pi");

        setDefaultExecutor((sender, context) -> {
            Player player = (Player) sender;

            // TODO: Add to Messages.toml
            if (instanceGuard.getRegionManager().getRegions().isEmpty()) {
                player.sendMessage(ChatUtils.translateMiniMessage("<red>You are currently not on a plot."));
                return;
            }

            for (Region region : instanceGuard.getRegionManager().getRegions()) {
                if (region.containsLocation(new Pos(player.getPosition()))) {
                    player.sendMessage(ChatUtils.translateMiniMessage("<dark_aqua>----------------------------------------"));
                    player.sendMessage(ChatUtils.translateMiniMessage("<dark_aqua>Plot info of plot: <aqua>" + region.getName()));
                    sendMessageWithList(player, "Owners", getNamesFromUUIDs(region.getOwners()));
                    sendMessageWithList(player, "Members", getNamesFromUUIDs(region.getMembers()));
                    sendMessageWithFlag(player, region);
                    player.sendMessage(ChatUtils.translateMiniMessage("<dark_aqua>----------------------------------------"));
                    return;
                }
            }
            player.sendMessage(ChatUtils.translateMiniMessage("<red>You are currently not on a plot."));
        });
    }

    private void sendMessageWithList(Player player, String label, List<String> values) {
        if (values.isEmpty()) {
            player.sendMessage(ChatUtils.translateMiniMessage("<dark_aqua>" + label + ": <aqua>None"));
        } else {
            player.sendMessage(ChatUtils.translateMiniMessage("<dark_aqua>" + label + ": <aqua>" + values.toString().replace("[", "").replace("]", "")));
        }
    }

    private void sendMessageWithFlag(Player player, Region region) {
        FlagValue<?> flagValue = region.getFlagValue("feather-description");
        if (flagValue == null) {
            player.sendMessage(ChatUtils.translateMiniMessage("<dark_aqua>Description: <aqua>None"));
        } else {
            player.sendMessage(ChatUtils.translateMiniMessage("<dark_aqua>Description: <aqua>" + flagValue.getValue()));
        }
    }

    private List<String> getNamesFromUUIDs(Collection<UUID> uuids) {
        List<String> names = new ArrayList<>();
        for (UUID uuid : uuids) {
            try {
                Collection<PlayerModel> playerModels =
                        StormDatabase.getInstance().getStorm().buildQuery(PlayerModel.class)
                                .where("uuid", Where.EQUAL, uuid.toString())
                                .limit(1)
                                .execute()
                                .join();
                for (PlayerModel playerModel : playerModels) {
                    names.add(playerModel.getUsername());
                }
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
        return names;
    }
}
