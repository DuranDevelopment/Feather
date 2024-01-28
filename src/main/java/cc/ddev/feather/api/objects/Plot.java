package cc.ddev.feather.api.objects;

import cc.ddev.feather.command.plots.subcommands.PlotCalculateCommand;
import cc.ddev.feather.utils.RegionUtils;
import cc.ddev.feather.world.WorldManager;
import cc.ddev.feather.wrapper.Location;
import cc.ddev.instanceguard.region.Region;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Plot {
    private final Region region;
    private final String world;

    public Plot(Region region, String world) {
        this.region = region;
        this.world = world;
    }

    public Region getRegion() {
        return this.region;
    }

    public String getWorld() {
        return this.world;
    }

    public String getDescription(Player player) {
        return RegionUtils.getPlotDescription(player, this.getRegion());
    }

    public CompletableFuture<Double> calculateGroundPrice() {
        return CompletableFuture.supplyAsync(() -> RegionUtils.getInstance().calculatePlotPrice(this.region));
    }

    public CompletableFuture<Map.Entry<Double, Double>> calculateBuildingPrice() {
        Pos pos1 = new Pos(region.getMinLocation().x(), region.getMinLocation().y(), region.getMinLocation().z());
        Pos pos2 = new Pos(region.getMaxLocation().x(), region.getMaxLocation().y(), region.getMaxLocation().z());
        return PlotCalculateCommand.calculatePrices(new Location(pos1, WorldManager.getInstance().getWorld(world)), new Location(pos2, WorldManager.getInstance().getWorld(world)));
    }

    @Deprecated
    public double getCalculatedGroundPrice() {
        return RegionUtils.getInstance().calculatePlotPrice(this.region);
    }

    @Deprecated
    public double getCalculatedBuildingPrice() {
        try {
            return this.calculateBuildingPrice().get().getKey();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -1.0;
        }
    }

    @Deprecated
    public int getBuilderPrice() {
        try {
            return this.calculateBuildingPrice().get().getValue().intValue();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
