package cc.ddev.feather.api.config;

import cc.ddev.feather.configuration.ConfigManager;
import de.leonhard.storage.Toml;
import lombok.Getter;

public class Messages {

    @Getter
    private static final Toml messagesConfig = ConfigManager.init().getMessagesConfig();

    @Getter
    public static String LANGUAGE = getMessagesConfig().getString("language");

    @Getter
    public static String NO_PERMISSION = getMessagesConfig().getString("no_permissions");

    @Getter
    public static String INVALID_ARGUMENTS = getMessagesConfig().getString("invalid_arguments");

    @Getter
    public static String INVALID_USERNAME = getMessagesConfig().getString("invalid_username");

    @Getter
    public static String NOT_ONLINE = getMessagesConfig().getString("not_online");

    @Getter
    public static String FLY_ON = getMessagesConfig().getString("fly_on");

    @Getter
    public static String FLY_OFF = getMessagesConfig().getString("fly_off");

    @Getter
    public static String JOIN_MESSAGE = getMessagesConfig().getString("join.message");

    @Getter
    public static String JOIN_TITLE = getMessagesConfig().getString("join_title.title");

    @Getter
    public static String JOIN_SUBTITLE = getMessagesConfig().getString("join_title.subtitle");

    public static class MTWorldCMD {
        @Getter
        public static String ADD_SUCCES = getMessagesConfig().getString("mtworldcmd.add.success");
    }

    public static class Banking {
        public static class Selector {
            @Getter
            public static String TITLE = getMessagesConfig().getString("banking.selector.title");

            @Getter
            public static String ACCOUNT_TITLE = getMessagesConfig().getString("banking.selector.account.title");

            @Getter
            public static String ACCOUNT_LORE = getMessagesConfig().getString("banking.selector.account.lore");

            @Getter
            public static String GOVERNMENT = getMessagesConfig().getString("banking.selector.accounttype.government");

            @Getter
            public static String BUSINESS = getMessagesConfig().getString("banking.selector.accounttype.business");

            @Getter
            public static String PRIVATE = getMessagesConfig().getString("banking.selector.accounttype.private");

            @Getter
            public static String SAVINGS = getMessagesConfig().getString("banking.selector.accounttype.savings");
        }

        public static class Menu {
            @Getter
            public static String TITLE = getMessagesConfig().getString("banking.menu.title");

            @Getter
            public static String ITEM_LORE = getMessagesConfig().getString("banking.menu.item_lore");

        }

        public static class Money {
            @Getter
            public static String LORE = getMessagesConfig().getString("banking.money.lore");

            @Getter
            public static String INSUFFICIENT_BALANCE = getMessagesConfig().getString("banking.money.insufficient_balance");

            @Getter
            public static String IS_FAKE_MONEY = getMessagesConfig().getString("banking.money.is_fake_money");

            public static class Withdraw {
                @Getter
                public static String INVENTORY_FULL = getMessagesConfig().getString("banking.money.withdraw.inventory_full");

                @Getter
                public static String MESSAGE_PRIVATE = getMessagesConfig().getString("banking.money.withdraw.private");

                @Getter
                public static String MESSAGE_BUSINESS = getMessagesConfig().getString("banking.money.withdraw.business");

                @Getter
                public static String MESSAGE_SAVINGS = getMessagesConfig().getString("banking.money.withdraw.savings");

                @Getter
                public static String MESSAGE_GOVERNMENT = getMessagesConfig().getString("banking.money.withdraw.government");
            }

            public static class Deposit {
                @Getter
                public static String MESSAGE_PRIVATE = getMessagesConfig().getString("banking.money.deposit.private");

                @Getter
                public static String MESSAGE_BUSINESS = getMessagesConfig().getString("banking.money.deposit.business");

                @Getter
                public static String MESSAGE_SAVINGS = getMessagesConfig().getString("banking.money.deposit.savings");

                @Getter
                public static String MESSAGE_GOVERNMENT = getMessagesConfig().getString("banking.money.deposit.government");
            }
        }

        public static class NoBankingPermission {
            @Getter
            public static String DEPOSIT = getMessagesConfig().getString("banking.nobankingpermission.deposit");
        }

        @Getter
        public static String FROZEN = getMessagesConfig().getString("banking.frozen");

        @Getter
        public static String NO_SAVINGS_ACCOUNT = getMessagesConfig().getString("banking.selector.accounttype.no_savings_accounts");

        @Getter
        public static String NO_BUSINESS_ACCOUNT = getMessagesConfig().getString("banking.selector.accounttype.no_business_accounts");
    }

    public static class BankaccountCMD {
        @Getter
        public static String ID_NOTEXIST = getMessagesConfig().getString("bankaccountcmd.id_does_not_exist");

        @Getter
        public static String INVALID_BANKPERMISSION = getMessagesConfig().getString("bankaccountcmd.invalid_bankpermission");

        public static class AddUser {
            @Getter
            public static String SUCCESS = getMessagesConfig().getString("bankaccountcmd.adduser.success");

            @Getter
            public static String ALREADY_ADDED = getMessagesConfig().getString("bankaccountcmd.adduser.already_added");
        }
    }
}
