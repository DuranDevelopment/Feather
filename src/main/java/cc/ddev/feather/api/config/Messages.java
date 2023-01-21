package cc.ddev.feather.api.config;

import cc.ddev.feather.configuration.ConfigManager;
import de.leonhard.storage.Toml;
import lombok.Getter;

public class Messages {

    @Getter
    private static final Toml messagesConfig = ConfigManager.init().getMessagesConfig();

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
    public static String TITLE_LINE_1 = getMessagesConfig().getString("title.line1");

    @Getter
    public static String TITLE_LINE_2 = getMessagesConfig().getString("title.line2");

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
    }
}
