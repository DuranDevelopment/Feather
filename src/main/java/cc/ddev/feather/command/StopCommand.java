package cc.ddev.feather.command;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;

public class StopCommand extends Command {
    public StopCommand() {
        super("stop", "exit", "quit", "end", "shutdown", "halt", "terminate");
        setCondition((sender, command) -> sender.hasPermission("server.stop") || sender instanceof ConsoleSender);
        setDefaultExecutor((sender, context) -> {
            MinecraftServer.stopCleanly();
        });
    }
}
