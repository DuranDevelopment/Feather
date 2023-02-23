package cc.ddev.feather.command;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.CommandExecutor;
import org.jetbrains.annotations.NotNull;

public final class StopCommand extends Command implements CommandExecutor {
    public StopCommand() {
        super("stop", "exit", "quit", "end", "shutdown", "halt", "terminate");
        setCondition((sender, command) -> sender.hasPermission("server.stop") || sender instanceof ConsoleSender);
        setDefaultExecutor(this);
    }

    @Override
    public void apply(
            final @NotNull CommandSender sender,
            final @NotNull CommandContext context
    ) {
        MinecraftServer.stopCleanly();
    }
}
