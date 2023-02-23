package cc.ddev.feather.command.banking.bankaccount;

import cc.ddev.feather.command.banking.bankaccount.subcommands.AddUserBankAccountSubCommand;
import cc.ddev.feather.command.banking.bankaccount.subcommands.CreateBankAccountSubCommand;
import net.minestom.server.command.builder.Command;

public class BankAccountCommand extends Command {
    public BankAccountCommand() {
        super("bankaccount");
        addSubcommand(new CreateBankAccountSubCommand());
        addSubcommand(new AddUserBankAccountSubCommand());
    }
}
