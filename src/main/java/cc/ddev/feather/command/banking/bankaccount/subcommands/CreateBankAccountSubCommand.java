package cc.ddev.feather.command.banking.bankaccount.subcommands;

import cc.ddev.feather.api.banking.BankUtils;
import cc.ddev.feather.api.enums.BankAccountType;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;

public class CreateBankAccountSubCommand extends Command {
    public CreateBankAccountSubCommand() {
        super("create", "maak");

        ArgumentString typeArgument = ArgumentType.String("type");

        addSyntax((sender, context) -> {
            String type = context.get(typeArgument);


            if (type.equalsIgnoreCase("business")) {
                BankUtils.getInstance().create(BankAccountType.BUSINESS);
                sender.sendMessage("Creating bank account of type " + type);
            } else if (type.equalsIgnoreCase("savings")) {
                BankUtils.getInstance().create(BankAccountType.SAVINGS);
                sender.sendMessage("Creating bank account of type " + type);
            } else if (type.equalsIgnoreCase("government")) {
                BankUtils.getInstance().create(BankAccountType.GOVERNMENT);
                sender.sendMessage("Creating bank account of type " + type);
            } else {
                sender.sendMessage("Invalid type");
            }
            sender.sendMessage("Creating bank account of type " + type);
        }, typeArgument);
    }
}
