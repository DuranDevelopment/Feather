package cc.ddev.feather.api.playerdata;

import net.minestom.server.coordinate.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    public static Map<UUID, Point> plotWandPos1Map = new HashMap<>();
    public static Map<UUID, Point> plotWandPos2Map = new HashMap<>();

    public static void setPlotWandPos1(UUID uuid, Point point) {
        plotWandPos1Map.remove(uuid);
        plotWandPos1Map.put(uuid, point);
    }

    public static void setPlotWandPos2(UUID uuid, Point point) {
        plotWandPos2Map.remove(uuid);
        plotWandPos2Map.put(uuid, point);
    }

    public static Point getPlotWandPos1(UUID uuid) {
        return plotWandPos1Map.get(uuid);
    }

    public static Point getPlotWandPos2(UUID uuid) {
        return plotWandPos2Map.get(uuid);
    }
}
