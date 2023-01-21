package cc.ddev.feather.api.config;

import cc.ddev.feather.configuration.ConfigManager;
import de.leonhard.storage.Toml;
import lombok.Getter;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class BankingConfig {

    @Getter
    private static final Toml bankingConfig = ConfigManager.init().getBankingConfig();

    public static class ATM {
        @Getter
        public static boolean DISABLE_BANKACCOUNTS = getBankingConfig().getBoolean("atm.disable_bankaccounts");
    }

    public static class Blocks {
        @Getter
        public static ItemStack SAVINGS_ACCOUNT = ItemStack.of(Material.fromNamespaceId(getBankingConfig().getString("blocks.savings")));

        @Getter
        public static ItemStack PRIVATE_ACCOUNT = ItemStack.of(Material.fromNamespaceId(getBankingConfig().getString("blocks.private")));

        @Getter
        public static ItemStack BUSINESS_ACCOUNT = ItemStack.of(Material.fromNamespaceId(getBankingConfig().getString("blocks.business")));

        @Getter
        public static ItemStack GOVERNMENT_ACCOUNT = ItemStack.of(Material.fromNamespaceId(getBankingConfig().getString("blocks.government")));
    }

    @Getter
    public static boolean HAS_DECIMALS = getBankingConfig().getBoolean("money-formatting.has_decimals");
}
