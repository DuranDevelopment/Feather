package cc.ddev.feather.command.mtworld.subcommands;

import cc.ddev.feather.api.sidebar.SidebarManager;
import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.database.models.BankAccountUserModel;
import cc.ddev.feather.database.models.PlayerModel;
import cc.ddev.feather.database.models.WorldModel;
import cc.ddev.feather.utils.ChatUtils;
import com.craftmend.storm.api.enums.Where;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class MTWorldRemoveCommand extends Command {
    public MTWorldRemoveCommand() {
        super("remove");

        ArgumentString worldArgument = ArgumentType.String("world");

        setDefaultExecutor((sender, context) -> sender.sendMessage("Usage: /mtworld remove <world>"));

        addSyntax((sender, context) -> {
            Player player = (Player) sender;
            String world = context.get(worldArgument);
            try {
                Collection<WorldModel> worldModels =
                        StormDatabase.getInstance().getStorm().buildQuery(WorldModel.class)
                                .execute()
                                .join();
                int count = 0;
                for (WorldModel worldModel : worldModels) {
                    if (worldModel.getWorldName().equals(world)) {
                        // TODO: Add to Messages.toml
                        player.sendMessage(ChatUtils.translateMiniMessage("<dark_aqua>World <aqua>" + world + " <dark_aqua>removed."));
                        StormDatabase.getInstance().getStorm().delete(worldModel);
                        SidebarManager.refreshSidebar();
                        return;
                    }
                    count++;
                }
                if (count == worldModels.size()) {
                    // TODO: Add to Messages.toml
                    player.sendMessage(ChatUtils.translateMiniMessage("<dark_red>World <red>" + world + " <dark_red>not found."));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, worldArgument);
    }
}
