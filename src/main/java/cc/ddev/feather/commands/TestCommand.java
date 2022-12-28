package cc.ddev.feather.commands;

import net.minestom.server.command.builder.Command;

public class TestCommand extends Command {
    public TestCommand() {
        super("test", "testcommand");
        setDefaultExecutor((sender, context) -> sender.sendMessage("Test command executed!"));
    }
}
