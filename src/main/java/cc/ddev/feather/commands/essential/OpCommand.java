package cc.ddev.feather.commands.essential;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;

import java.util.List;

public class OpCommand extends Command {

    public OpCommand() {

        super("op");

        ArgumentEntity player = ArgumentType.Entity("targets").onlyPlayers(true);

        //Upon invalid usage, print the correct usage of the command to the sender
        setDefaultExecutor((sender, context) -> {
            String commandName = context.getCommandName();

            sender.sendMessage(Component.text("Usage: /" + commandName + " <target>", NamedTextColor.RED));
        });

        //Command Syntax for /op <target>
        addSyntax((sender, context) -> {
            //Check permission for players only
            //This allows the console to use this syntax too
            if (sender instanceof Player p && p.getPermissionLevel() < 2) {
                sender.sendMessage(Component.text("You don't have permission to use this command.", NamedTextColor.RED));
                return;
            }

            EntityFinder finder = context.get(player);

            //Set the gamemode for the targets
            executeOthers(sender, finder.find(sender));
        }, player);
    }

    /**
     * Sets the gamemode for the specified entities, and
     * notifies them (and the sender) in the chat.
     */
    private void executeOthers(CommandSender sender, List<Entity> entities) {
        if (entities.size() == 0) {
            //If there are no players that could be modified, display an error message
            if (sender instanceof Player)
                sender.sendMessage(Component.translatable("argument.entity.notfound.player", NamedTextColor.RED));
            else sender.sendMessage(Component.text("No player was found", NamedTextColor.RED));
        } else for (Entity entity : entities) {
            if (entity instanceof Player p) {
                if (p == sender) {
                    //If the player is the same as the sender, call
                    //executeSelf to display one message instead of two
                    executeSelf((Player) sender);
                } else {
                    p.setPermissionLevel(4);

                    Component playerName = p.getDisplayName() == null ? p.getName() : p.getDisplayName();

                    //Send a message to the changed player and the sender
                    sender.sendMessage(Component.text("You are now op!", NamedTextColor.GREEN));
                    sender.sendMessage(Component.text("You succesfully opped " + playerName + "!", NamedTextColor.GREEN));
                }
            }
        }
    }

    /**
     * Sets the gamemode for the executing Player, and
     * notifies them in the chat.
     */
    private void executeSelf(Player sender) {
        sender.setPermissionLevel(4);

        //Send the translated message to the player.
        sender.sendMessage(Component.text("You are now op!", NamedTextColor.GREEN));
    }
}