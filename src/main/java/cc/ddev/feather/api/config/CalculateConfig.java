package cc.ddev.feather.api.config;

import cc.ddev.feather.configuration.ConfigManager;
import de.leonhard.storage.Toml;
import lombok.Getter;

@Getter
public class CalculateConfig {

    private static CalculateConfig instance;
    public static CalculateConfig getInstance() {
        if (instance == null) {
            instance = new CalculateConfig();
        }
        return instance;
    }

    @Getter
    private static final Toml calculateConfig = ConfigManager.getInstance().getCalculateConfig();

    public static double BUILDERS_COST_PER_BLOCK = getCalculateConfig().getDouble("BuildersCostsPerBlock");

    public double getBlockCost(String materialName) {
        return getCalculateConfig().getDouble(materialName);
    }
}
