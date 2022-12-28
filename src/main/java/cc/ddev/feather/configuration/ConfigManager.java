package cc.ddev.feather.configuration;

import cc.ddev.feather.logger.Log;
import de.leonhard.storage.Toml;
import lombok.Getter;

import java.io.File;

public class ConfigManager {

    public static ConfigManager init() {
        return new ConfigManager();
    }

    String currentDirectory = System.getProperty("user.dir");
    String configDirectory = currentDirectory + File.separator + "configuration";

    @Getter
    private final Toml featherConfig = new Toml("feather", configDirectory);

    public void createConfigDirectory() {
        Log.getLogger().info(featherConfig.getFilePath());
        Log.getLogger().info("Current directory: " + currentDirectory);
        Log.getLogger().info("Config directory: " + configDirectory);
        File f = new File(configDirectory);
        if (!f.exists()) {
            Log.getLogger().info("Creating configuration directory...");
            f.mkdir();
        } else {
            Log.getLogger().info("Configuration directory already exists!");
        }
    }
}
