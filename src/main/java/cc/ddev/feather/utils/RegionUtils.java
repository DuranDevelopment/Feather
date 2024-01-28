package cc.ddev.feather.utils;

import cc.ddev.feather.api.API;
import cc.ddev.feather.api.config.Config;
import cc.ddev.feather.api.objects.Plot;
import cc.ddev.feather.world.WorldManager;
import cc.ddev.instanceguard.flag.FlagValue;
import cc.ddev.instanceguard.region.Region;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;
import java.util.List;

public class RegionUtils {

    private static RegionUtils regionUtils;
    public static RegionUtils getInstance() {
        if (regionUtils == null) {
            regionUtils = new RegionUtils();
        }
        return regionUtils;
    }

    public double calculatePlotPrice(Region region) {
        int length = (int)Math.abs(region.getMinLocation().x() - region.getMaxLocation().x());
        int width = (int)Math.abs(region.getMinLocation().z() - region.getMaxLocation().z());
        String formula = Config.Plot.CALCULATE_PRICEFORMULA.replace("<length>", "l").replace("<width>", "w");
        Expression expression = new ExpressionBuilder(formula).variables("l", "w").build().setVariable("l", length).setVariable("w", width);
        return expression.evaluate();
    }

    public static void setPlotLevels(Region region, int levels) {
        region.setFlag("feather-plotlevels", new FlagValue<>(levels));
    }

    public static String getPlotDescription(Player player, Region region) {
        FlagValue<?> flagValue = region.getFlagValue("feather-description");
        String description = (String) flagValue.getValue();
        if (description.isEmpty()) {
            return "None";
        }
        return description;
    }

    public List<Plot> getUnclaimedPlots() {
        ArrayList<Plot> unclaimedPlots = new ArrayList<Plot>();
        for (Instance instance : MinecraftServer.getInstanceManager().getInstances()) {
            if (!WorldManager.getInstance().isMTWorld(WorldManager.getInstance().getInstanceName(instance))) continue;
            try {
                for (Region region : API.getInstanceGuard().getRegionManager().getRegions()) {
                    if (!region.getOwners().isEmpty() || !region.getMembers().isEmpty()) continue;
                    unclaimedPlots.add(new Plot(region, WorldManager.getInstance().getInstanceName(instance)));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return unclaimedPlots;
    }
}
