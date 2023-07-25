package cc.ddev.feather.api.config;

import cc.ddev.feather.configuration.ConfigManager;
import de.leonhard.storage.Toml;
import lombok.Getter;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class BankingConfig {

    @Getter
    private static final Toml bankingConfig = ConfigManager.getInstance().getBankingConfig();

    public static class ATM {
        @Getter
        public static Block BLOCK = Block.fromNamespaceId(getBankingConfig().getString("atm.block"));

        @Getter
        public static Material DEBIT_CARD_ITEM = Material.fromNamespaceId(getBankingConfig().getString("atm.debit_card_item"));

        @Getter
        public static boolean REQUIRE_DEBIT_CARD = getBankingConfig().getBoolean("atm.require_card");

        @Getter
        public static boolean DISABLE_BANKACCOUNTS = getBankingConfig().getBoolean("atm.disable_bankaccounts");

        @Getter
        public static boolean REALMONEY_ENABLED = getBankingConfig().getBoolean("atm.realmoney.enabled");
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

    public static class Items {
        public static class Ghast {
            @Getter
            public static ItemStack ITEM = ItemStack.of(Material.fromNamespaceId(getBankingConfig().getString("items.ghast.item")));

            @Getter
            public static double VALUE = getBankingConfig().getDouble("items.ghast.value");
        }

        public static class Diamond {
            @Getter
            public static ItemStack ITEM = ItemStack.of(Material.fromNamespaceId(getBankingConfig().getString("items.diamond.item")));

            @Getter
            public static double VALUE = getBankingConfig().getDouble("items.diamond.value");
        }

        public static class Redstone {
            @Getter
            public static ItemStack ITEM = ItemStack.of(Material.fromNamespaceId(getBankingConfig().getString("items.redstone.item")));

            @Getter
            public static double VALUE = getBankingConfig().getDouble("items.redstone.value");
        }


        public static class Emerald {
            @Getter
            public static ItemStack ITEM = ItemStack.of(Material.fromNamespaceId(getBankingConfig().getString("items.emerald.item")));

            @Getter
            public static double VALUE = getBankingConfig().getDouble("items.emerald.value");
        }

        public static class Coal {
            @Getter
            public static ItemStack ITEM = ItemStack.of(Material.fromNamespaceId(getBankingConfig().getString("items.coal.item")));

            @Getter
            public static double VALUE = getBankingConfig().getDouble("items.coal.value");
        }

        public static class IronIngot {
            @Getter
            public static ItemStack ITEM = ItemStack.of(Material.fromNamespaceId(getBankingConfig().getString("items.iron_ingot.item")));

            @Getter
            public static double VALUE = getBankingConfig().getDouble("items.iron_ingot.value");
        }

        public static class Quartz {
            @Getter
            public static ItemStack ITEM = ItemStack.of(Material.fromNamespaceId(getBankingConfig().getString("items.quartz.item")));

            @Getter
            public static double VALUE = getBankingConfig().getDouble("items.quartz.value");
        }

        public static class GoldIngot {
            @Getter
            public static ItemStack ITEM = ItemStack.of(Material.fromNamespaceId(getBankingConfig().getString("items.gold_ingot.item")));

            @Getter
            public static double VALUE = getBankingConfig().getDouble("items.gold_ingot.value");
        }

        public static class GoldNugget {
            @Getter
            public static ItemStack ITEM = ItemStack.of(Material.fromNamespaceId(getBankingConfig().getString("items.gold_nugget.item")));

            @Getter
            public static double VALUE = getBankingConfig().getDouble("items.gold_nugget.value");
        }

    }

    @Getter
    public static boolean HAS_DECIMALS = getBankingConfig().getBoolean("money-formatting.has_decimals");

    // Get material value from config
    public static double getMaterialValue(Material material) {
        if (material.equals(Material.GHAST_TEAR)) {
            return Items.Ghast.VALUE;
        } else if (material.equals(Material.DIAMOND)) {
            return Items.Diamond.VALUE;
        } else if (material.equals(Material.REDSTONE)) {
            return Items.Redstone.VALUE;
        } else if (material.equals(Material.EMERALD)) {
            return Items.Emerald.VALUE;
        } else if (material.equals(Material.COAL)) {
            return Items.Coal.VALUE;
        } else if (material.equals(Material.IRON_INGOT)) {
            return Items.IronIngot.VALUE;
        } else if (material.equals(Material.QUARTZ)) {
            return Items.Quartz.VALUE;
        } else if (material.equals(Material.GOLD_INGOT)) {
            return Items.GoldIngot.VALUE;
        } else if (material.equals(Material.GOLD_NUGGET)) {
            return Items.GoldNugget.VALUE;
        }
        return 0;
    }
}
