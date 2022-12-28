package cc.ddev.feather.configuration;

import de.leonhard.storage.Toml;
import lombok.Getter;

public class ConfigManager {

    public static ConfigManager init() {
        return new ConfigManager();
    }

    @Getter
    private final Toml featherConfig = new Toml("feather", "configuration");

}
