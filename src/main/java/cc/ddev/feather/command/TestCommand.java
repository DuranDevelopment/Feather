package cc.ddev.feather.command;

import cc.ddev.feather.api.config.Messages;
import cc.ddev.feather.world.WorldManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class TestCommand extends Command {
    public TestCommand() {
        super("test", "testcommand");
        setDefaultExecutor((sender, context) -> {
            Player player = (Player) sender;
            WorldManager.saveWorlds();
        });
    }
}
