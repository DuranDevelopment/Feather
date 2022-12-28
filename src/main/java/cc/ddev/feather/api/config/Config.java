package cc.ddev.feather.api.config;

import cc.ddev.feather.configuration.ConfigManager;
import de.leonhard.storage.Toml;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;

public class Config {

    @Getter
    private static final ConfigManager configManager = ConfigManager.init();

    @Getter
    public static final Toml featherConfig = configManager.getFeatherConfig();

    @Getter
    @Setter
    public static Pos SPAWN = new Pos(featherConfig.getDouble("spawn.x"), featherConfig.getDouble("spawn.y"), featherConfig.getDouble("spawn.z"));

    @Getter
    public static String DATABASE_TYPE = getFeatherConfig().getString("database.type");

    @Getter
    public static String DATABASE_HOST = getFeatherConfig().getString("database.host");

    @Getter
    public static String DATABASE_USERNAME = getFeatherConfig().getString("database.username");

    @Getter
    public static String DATABASE_PASSWORD = getFeatherConfig().getString("database.password");

    @Getter
    public static int DATABASE_PORT = getFeatherConfig().getInt("database.port");

    @Getter
    public static String DATABASE_NAME = getFeatherConfig().getString("database.name");

    @Getter
    public static String SERVER_HOST = getFeatherConfig().getString("server.host");

    @Getter
    public static int SERVER_PORT = getFeatherConfig().getInt("server.port");
}
