package cc.ddev.feather.command.essential;

import cc.ddev.feather.api.config.Messages;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class FlyCommand extends Command {

    public FlyCommand() {
        super("fly");

        setDefaultExecutor((sender, context) -> {
            Player player = (Player) sender;
            boolean fly = player.isFlying();
            if (player.hasPermission("feather.fly")) {
                if (fly) {
                    player.setFlying(false);
                    player.sendMessage(Component.text(Messages.FLY_OFF));
                } else {
                    player.setFlying(true);
                    player.sendMessage(Component.text(Messages.FLY_ON));
                }
            } else {
                player.sendMessage(Component.text(Messages.NO_PERMISSION));
            }
        });
    }
}

