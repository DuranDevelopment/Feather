package cc.ddev.feather.command.plots;

import cc.ddev.feather.api.config.Config;
import cc.ddev.feather.utils.ChatUtils;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class PlotwandCommand extends Command {
    public PlotwandCommand() {
        super("plotwand");
//        setCondition((sender, command) -> sender.hasPermission("feather.plotwand"));

        setDefaultExecutor((sender, context) -> {
            Player player = (Player) sender;
            ItemStack plotwand = Config.Plot.PLOTWAND;
            player.getInventory().addItemStack(plotwand);
        });
    }
}
