package cc.ddev.feather.api.config;

import cc.ddev.feather.configuration.ConfigManager;
import cc.ddev.feather.utils.ChatUtils;
import de.leonhard.storage.Toml;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

@Getter
public class Config {

    @Getter
    private static final Toml featherConfig = ConfigManager.getInstance().getFeatherConfig();

    public static class Player {
        public static class Default {
            public static String PREFIX = getFeatherConfig().getString("player.default.prefix");
            public static String PREFIX_COLOR = getFeatherConfig().getString("player.default.prefix_color");
            public static int LEVEL = getFeatherConfig().getInt("player.default.level");
            public static String LEVEL_COLOR = getFeatherConfig().getString("player.default.level_color");
            public static String CITY_COLOR = getFeatherConfig().getString("player.default.city_color");
            public static String CHAT_COLOR = getFeatherConfig().getString("player.default.chat_color");

        }
    }

    public static class Database {
        public static String TYPE = getFeatherConfig().getString("database.type");

        public static String HOST = getFeatherConfig().getString("database.host");

        public static String USERNAME = getFeatherConfig().getString("database.username");

        public static String PASSWORD = getFeatherConfig().getString("database.password");

        public static int PORT = getFeatherConfig().getInt("database.port");

        public static String NAME = getFeatherConfig().getString("database.database");
    }

    public static class Chat {

        public static boolean FORMAT_ENABLED = getFeatherConfig().getBoolean("chat.enabled");

        public static String FORMAT = getFeatherConfig().getString("chat.format");

    }

    public static class Spawn {

        public static Pos COORDS = new Pos(featherConfig.getDouble("spawn.x"), featherConfig.getDouble("spawn.y"), featherConfig.getDouble("spawn.z"));

        public static String WORLD = getFeatherConfig().getString("spawn.world");

        public static boolean SPAWN_ON_JOIN = getFeatherConfig().getBoolean("spawn.spawn_on_join");
    }

    public static class Sidebar {
        public static String TITLE = getFeatherConfig().getString("sidebar.title");

        public static boolean ENABLED = getFeatherConfig().getBoolean("sidebar.enabled");
    }

    public static class Economy {
        public static final double STARTING_BALANCE = getFeatherConfig().getDouble("economy.starting_balance");
    }

    public static class Server {
        public static String SERVER_HOST = getFeatherConfig().getString("server.host");

        public static int SERVER_PORT = getFeatherConfig().getInt("server.port");

        public static int MAX_PLAYERS = getFeatherConfig().getInt("server.max_players");

        public static String DESCRIPTION = getFeatherConfig().getString("server.description");

        public static boolean ONLINE_MODE = getFeatherConfig().getBoolean("server.online_mode");
    }

    public static class Plot {
        public static ItemStack PLOTWAND = ItemStack.builder(Material.STICK)
                .displayName(ChatUtils.translateMiniMessage("<dark_aqua>Plot<aqua>Wand"))
                .lore(ChatUtils.splitStringByNewLineToComponent("<aqua>Right click <dark_aqua>to select the first position\n\n" +
                        "<aqua>Left click <dark_aqua>to select the second position"))
                .build();
    }
}