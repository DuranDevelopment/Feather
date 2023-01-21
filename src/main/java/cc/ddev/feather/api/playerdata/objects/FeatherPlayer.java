package cc.ddev.feather.api.playerdata.objects;

import net.minestom.server.coordinate.Pos;

import java.util.UUID;

public interface FeatherPlayer {
    UUID getUUID();
    Pos getLastLocation();
    double getBalance();
}
