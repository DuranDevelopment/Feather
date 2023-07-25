package cc.ddev.feather.api.config;

import cc.ddev.feather.configuration.ConfigManager;
import cc.ddev.feather.utils.ChatUtils;
import de.leonhard.storage.Toml;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class Config {

    @Getter
    private static final Toml featherConfig = ConfigManager.getInstance().getFeatherConfig();

    public static class Database {
        @Getter
        public static String TYPE = getFeatherConfig().getString("database.type");

        @Getter
        public static String HOST = getFeatherConfig().getString("database.host");

        @Getter
        public static String USERNAME = getFeatherConfig().getString("database.username");

        @Getter
        public static String PASSWORD = getFeatherConfig().getString("database.password");

        @Getter
        public static int PORT = getFeatherConfig().getInt("database.port");

        @Getter
        public static String NAME = getFeatherConfig().getString("database.database");
    }

    public static class Spawn {
        @Getter
        @Setter
        public static Pos COORDS = new Pos(featherConfig.getDouble("spawn.x"), featherConfig.getDouble("spawn.y"), featherConfig.getDouble("spawn.z"));

        @Getter
        @Setter
        public static String WORLD = getFeatherConfig().getString("spawn.world");

        @Getter
        public static boolean SPAWN_ON_JOIN = getFeatherConfig().getBoolean("spawn.spawn_on_join");

        @Getter
        public static boolean SEND_JOIN_MESSAGE = getFeatherConfig().getBoolean("spawn.send_join_message");
    }

    public static class Sidebar {
        @Getter
        public static String TITLE = getFeatherConfig().getString("sidebar.title");

        @Getter
        public static boolean ENABLED = getFeatherConfig().getBoolean("sidebar.enabled");
    }

    public static class Economy {
        @Getter
        public static final double STARTING_BALANCE = getFeatherConfig().getDouble("economy.starting_balance");
    }

    public static class Server {
        @Getter
        public static String SERVER_HOST = getFeatherConfig().getString("server.host");

        @Getter
        public static int SERVER_PORT = getFeatherConfig().getInt("server.port");

        @Getter
        public static int MAX_PLAYERS = getFeatherConfig().getInt("server.max_players");

        @Getter
        public static String DESCRIPTION = getFeatherConfig().getString("server.description");
    }

    public static class Plot {
        @Getter
        public static ItemStack PLOTWAND = ItemStack.builder(Material.STICK)
                .displayName(ChatUtils.translateMiniMessage("<dark_aqua>Plot<aqua>Wand"))
                .lore(ChatUtils.splitStringByNewLineToComponent("<aqua>Right click <dark_aqua>to select the first position\n\n" +
                        "<aqua>Left click <dark_aqua>to select the second position"))
                .build();
    }
}