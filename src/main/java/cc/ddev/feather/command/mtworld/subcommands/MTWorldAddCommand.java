package cc.ddev.feather.command.mtworld.subcommands;

import cc.ddev.feather.api.config.Messages;
import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.database.models.WorldModel;
import cc.ddev.feather.api.sidebar.SidebarManager;
import cc.ddev.feather.utils.ChatUtils;
import cc.ddev.feather.world.WorldManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

public class MTWorldAddCommand extends Command {
    public MTWorldAddCommand() {
        super("add");

        ArgumentString worldArgument = ArgumentType.String("world");

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Usage: /mtworld add <world>");
        });

        //Command Syntax for /mtworld add <world>
        addSyntax((sender, context) -> {
            Instance instance = WorldManager.getWorld(context.get(worldArgument));

            //Check permission for players only
            //This allows the console to use this syntax too
            if (sender instanceof Player player && player.getPermissionLevel() < 2 || sender.hasPermission("feather.mtworld.add")) {
                sender.sendMessage(Component.text("You don't have permission to use this command.", NamedTextColor.RED));
                return;
            }

            if (sender instanceof Player) {
                if (instance == null) {
                    sender.sendMessage(ChatUtils.translateMiniMessage("<red>This world doesn't exist!"));
                    return;
                }
                WorldModel worldModel = new WorldModel();
                worldModel.setColor("<gray>");
                worldModel.setTitle(WorldManager.getInstanceName(instance));
                worldModel.setLoadingName(WorldManager.getInstanceName(instance));
                worldModel.setTemperature(21.64);
                worldModel.setWorldName(context.get(worldArgument));
                StormDatabase.getInstance().saveStormModel(worldModel);
                sender.sendMessage(ChatUtils.translateMiniMessage(Messages.MTWorldCMD.ADD_SUCCES));
                SidebarManager.buildSidebar((Player) sender);
            }
        }, worldArgument);
    }
}
