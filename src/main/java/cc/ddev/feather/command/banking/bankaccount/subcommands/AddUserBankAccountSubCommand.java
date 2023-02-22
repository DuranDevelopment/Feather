//package cc.ddev.feather.command.banking.bankaccount.subcommands;
//
//import cc.ddev.feather.api.banking.BankUtils;
//import cc.ddev.feather.api.enums.BankPermission;
//import net.minestom.server.command.builder.Command;
//import net.minestom.server.command.builder.arguments.ArgumentType;
//import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
//import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
//import net.minestom.server.entity.Player;
//import net.minestom.server.utils.entity.EntityFinder;
//
//public class AddUserBankAccountSubCommand extends Command {
//    public AddUserBankAccountSubCommand() {
//        super("adduser");
//
//        ArgumentEntity playerArgument = ArgumentType.Entity("player").onlyPlayers(true);
//        ArgumentInteger idArgument = ArgumentType.Integer("id");
//        addSyntax((sender, context) -> {
//            Player player = (Player) sender;
//            Player target = context.get(playerArgument).findFirstPlayer(player);
//            int id = context.get(idArgument);
//
//            if (BankUtils.getInstance().getBankAccount(id) == null) {
//                sender.sendMessage(Messages.BANKACCOUNTCMD_ID_NOTEXIST.sendMessage(sender));
//                return true;
//            }
//            Player a21 = Bukkit.getOfflinePlayer((String)args[2]);
//            if (a21 == null || !a21.hasPlayedBefore() && !a21.isOnline()) {
//                sender.sendMessage(Messages.NO_PLAYER.sendMessage(sender));
//                return true;
//            }
//            BankPermission a22 = BankPermission.ADMIN;
//            if (args.length == 4) {
//                try {
//                    a22 = BankPermission.getPermission(args[3]);
//                } catch (IllegalArgumentException a23) {
//                    boolean a24 = ConfigManager.getConfig().getConfigString(ConfigValues.DO_NOT_CHANGE_LANGUAGE).equals("NL");
//                    sender.sendMessage(Messages.BANKACCOUNTCMD_INVALID_BANKPERMISSION.sendMessage(sender).replace("<Permissions>", String.join((CharSequence)", ", Arrays.stream(BankPermission.values()).map(a24 ? BankPermission::getDutch : BankPermission::getEnglish).collect(Collectors.toList()))));
//                }
//            }
//            BankAccountAddUserEvent a25 = new BankAccountAddUserEvent(sender, a21, a22, id);
//            Bukkit.getPluginManager().callEvent((Event)a25);
//            if (a25.isCancelled()) {
//                return true;
//            }
//            if (!BankUtils.getInstance().getBankAccount(id).getAuthorisedUsers().contains(a21.getUniqueId())) {
//                String a26;
//                Bankaccount a27 = BankUtils.getInstance().getBankAccount(id);
//                a27.addUser(a21.getUniqueId(), a22);
//                boolean a28 = ConfigManager.getConfig().getConfigString(ConfigValues.DO_NOT_CHANGE_LANGUAGE).equals("NL");
//                String string = a26 = a28 ? a22.getDutch() : a22.getEnglish();
//                if (a22 == BankPermission.ADMIN) {
//                    a26 = a28 ? "opnemen en storten" : "deposit and withdraw";
//                }
//                sender.sendMessage(Messages.BANKACCOUNTCMD_ADDUSER_SUCCESS.sendMessage(sender).replace("<player>", a21.getName()).replace("<permission>", a26));
//            } else {
//                sender.sendMessage(Messages.BANKACCOUNTCMD_ADDUSER_ALREADY_ADDED.sendMessage(sender).replace("<player>", a21.getName()));
//            }
//        }
//    }
//}
