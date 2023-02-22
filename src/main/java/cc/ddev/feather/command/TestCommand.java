package cc.ddev.feather.command;

import cc.ddev.feather.api.config.Messages;
import net.minestom.server.command.builder.Command;

public class TestCommand extends Command {
    public TestCommand() {
        super("test", "testcommand");
        setDefaultExecutor((sender, context) -> sender.sendMessage(Messages.NO_PERMISSION));
    }
}
