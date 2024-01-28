package cc.ddev.feather.api.config;

import cc.ddev.feather.configuration.ConfigManager;
import de.leonhard.storage.Toml;
import lombok.Getter;

@Getter
public class Messages {

    @Getter
    private static final Toml messagesConfig = ConfigManager.getInstance().getMessagesConfig();

    
    public static String LANGUAGE = getMessagesConfig().getString("language");

    
    public static String NO_PERMISSION = getMessagesConfig().getString("no_permissions");

    
    public static String INVALID_ARGUMENTS = getMessagesConfig().getString("invalid_arguments");

    
    public static String INVALID_USERNAME = getMessagesConfig().getString("invalid_username");

    
    public static String NOT_ONLINE = getMessagesConfig().getString("not_online");

    
    public static String FLY_ON = getMessagesConfig().getString("fly_on");

    
    public static String FLY_OFF = getMessagesConfig().getString("fly_off");

    
    public static String JOIN_TITLE = getMessagesConfig().getString("join_title.title");

    
    public static String JOIN_SUBTITLE = getMessagesConfig().getString("join_title.subtitle");

    public static class Join {
        
        public static String JOIN_MESSAGE = getMessagesConfig().getString("join.message");

        
        public static boolean SEND_JOIN_MESSAGE = getMessagesConfig().getBoolean("join.enabled");
    }

    public static class Leave {
        
        public static String LEAVE_MESSAGE = getMessagesConfig().getString("leave.message");

        
        public static boolean SEND_LEAVE_MESSAGE = getMessagesConfig().getBoolean("leave.enabled");
    }

    public static class MTWorldCMD {
        
        public static String ADD_SUCCES = getMessagesConfig().getString("mtworldcmd.add.success");
    }

    public static class Banking {
        public static class Selector {
            
            public static String TITLE = getMessagesConfig().getString("banking.selector.title");

            
            public static String ACCOUNT_TITLE = getMessagesConfig().getString("banking.selector.account.title");

            
            public static String ACCOUNT_LORE = getMessagesConfig().getString("banking.selector.account.lore");

            
            public static String GOVERNMENT = getMessagesConfig().getString("banking.selector.accounttype.government");

            
            public static String BUSINESS = getMessagesConfig().getString("banking.selector.accounttype.business");

            
            public static String PRIVATE = getMessagesConfig().getString("banking.selector.accounttype.private");

            
            public static String SAVINGS = getMessagesConfig().getString("banking.selector.accounttype.savings");
        }

        public static class Menu {
            
            public static String TITLE = getMessagesConfig().getString("banking.menu.title");

            
            public static String ITEM_LORE = getMessagesConfig().getString("banking.menu.item_lore");

        }

        public static class Money {
            
            public static String LORE = getMessagesConfig().getString("banking.money.lore");

            
            public static String INSUFFICIENT_BALANCE = getMessagesConfig().getString("banking.money.insufficient_balance");

            
            public static String IS_FAKE_MONEY = getMessagesConfig().getString("banking.money.is_fake_money");

            public static class Withdraw {
                
                public static String INVENTORY_FULL = getMessagesConfig().getString("banking.money.withdraw.inventory_full");

                
                public static String MESSAGE_PRIVATE = getMessagesConfig().getString("banking.money.withdraw.private");

                
                public static String MESSAGE_BUSINESS = getMessagesConfig().getString("banking.money.withdraw.business");

                
                public static String MESSAGE_SAVINGS = getMessagesConfig().getString("banking.money.withdraw.savings");

                
                public static String MESSAGE_GOVERNMENT = getMessagesConfig().getString("banking.money.withdraw.government");
            }

            public static class Deposit {
                
                public static String MESSAGE_PRIVATE = getMessagesConfig().getString("banking.money.deposit.private");

                
                public static String MESSAGE_BUSINESS = getMessagesConfig().getString("banking.money.deposit.business");

                
                public static String MESSAGE_SAVINGS = getMessagesConfig().getString("banking.money.deposit.savings");

                
                public static String MESSAGE_GOVERNMENT = getMessagesConfig().getString("banking.money.deposit.government");
            }
        }

        public static class NoBankingPermission {
            
            public static String DEPOSIT = getMessagesConfig().getString("banking.nobankingpermission.deposit");
        }

        
        public static String FROZEN = getMessagesConfig().getString("banking.frozen");

        
        public static String NO_SAVINGS_ACCOUNT = getMessagesConfig().getString("banking.selector.accounttype.no_savings_accounts");

        
        public static String NO_BUSINESS_ACCOUNT = getMessagesConfig().getString("banking.selector.accounttype.no_business_accounts");
    }

    public static class BankaccountCMD {
        
        public static String ID_NOTEXIST = getMessagesConfig().getString("bankaccountcmd.id_does_not_exist");

        
        public static String INVALID_BANKPERMISSION = getMessagesConfig().getString("bankaccountcmd.invalid_bankpermission");

        public static class AddUser {
            
            public static String SUCCESS = getMessagesConfig().getString("bankaccountcmd.adduser.success");

            
            public static String ALREADY_ADDED = getMessagesConfig().getString("bankaccountcmd.adduser.already_added");
        }
    }

    public static class ATM {
        
        public static String DEBIT_CARD_REQUIRED = getMessagesConfig().getString("atm.require_card");
    }

    public static class Plotwand {
        
        public static String TITLE = getMessagesConfig().getString("plotwand.item.title");

        
        public static String LORE = getMessagesConfig().getString("plotwand.item.lore");
    }
}
