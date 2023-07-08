package cc.ddev.feather.command.plots;

import cc.ddev.feather.api.API;
import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.database.models.PlayerModel;
import cc.ddev.feather.logger.Log;
import cc.ddev.feather.player.PlayerProfile;
import cc.ddev.feather.utils.ChatUtils;
import cc.ddev.instanceguard.InstanceGuard;
import cc.ddev.instanceguard.region.Region;
import com.craftmend.storm.api.enums.Where;
import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

import java.util.*;

public class PlotInfoCommand extends Command {

    private final InstanceGuard instanceGuard = API.getInstanceGuard();

    public PlotInfoCommand() {
        super("plotinfo", "pi");

        setDefaultExecutor((sender, context) -> {
            Player player = (Player) sender;
            if (instanceGuard.getRegionManager().getRegions().size() == 0) {
                player.sendMessage(ChatUtils.translateMiniMessage("<red>You are not in a plot!"));
                return;
            }

            for (Region region : instanceGuard.getRegionManager().getRegions()) {
                if (region.containsLocation(new Pos(player.getPosition()))) {
                    player.sendMessage(ChatUtils.translateMiniMessage("<dark_aqua>----------------------------------------"));
                    player.sendMessage(ChatUtils.translateMiniMessage("<dark_aqua>Plot info of plot: <aqua>" + region.getName()));
                    if (region.getOwners().size() == 0) {
                        player.sendMessage(ChatUtils.translateMiniMessage("<dark_aqua>Owners:"));
                    } else {
                        for (UUID uuid : region.getOwners()) {
                            List<String> ownerNames = new ArrayList<>();
                            try {
                                Collection<PlayerModel> playerModels =
                                        StormDatabase.getInstance().getStorm().buildQuery(PlayerModel.class)
                                                .where("uuid", Where.EQUAL, uuid.toString())
                                                .limit(1)
                                                .execute()
                                                .join();
                                for (PlayerModel playerModel : playerModels) {
                                    ownerNames.add(playerModel.getUsername());
                                }
                            } catch (Exception exception) {
                                throw new RuntimeException(exception);
                            }
                            player.sendMessage(ChatUtils.translateMiniMessage("<dark_aqua>Owners: <aqua>" + ownerNames));
                        }
                    }

                    if (region.getMembers().size() == 0) {
                        player.sendMessage(ChatUtils.translateMiniMessage("<dark_aqua>Members:"));
                    } else {
                        for (UUID uuid : region.getMembers()) {
                            List<String> memberNames = new ArrayList<>();
                            try {
                                Collection<PlayerModel> playerModels =
                                        StormDatabase.getInstance().getStorm().buildQuery(PlayerModel.class)
                                                .where("uuid", Where.EQUAL, uuid.toString())
                                                .limit(1)
                                                .execute()
                                                .join();
                                for (PlayerModel playerModel : playerModels) {
                                    memberNames.add(playerModel.getUsername());
                                }
                            } catch (Exception exception) {
                                throw new RuntimeException(exception);
                            }
                            player.sendMessage(ChatUtils.translateMiniMessage("<dark_aqua>Members: <aqua>" + memberNames));
                        }
                    }
                    if (region.getFlagValue("feather-description") == null) {
                        player.sendMessage(ChatUtils.translateMiniMessage("<dark_aqua>Description: <aqua>None"));
                    } else {
                        player.sendMessage(ChatUtils.translateMiniMessage("<dark_aqua>Description: <aqua>" + region.getFlagValue("feather-description").getValue()));
                    }
                    player.sendMessage(ChatUtils.translateMiniMessage("<dark_aqua>----------------------------------------"));

                } else {
                    player.sendMessage(ChatUtils.translateMiniMessage("<red>You are not in a plot!"));
                }
            }
        });
    }
}
