package cc.ddev.feather.api;

import cc.ddev.feather.api.config.Config;
import cc.ddev.feather.configuration.ConfigManager;
import lombok.Getter;

public class API {

    @Getter
    public static ConfigManager configManager = Config.getConfigManager();


}
