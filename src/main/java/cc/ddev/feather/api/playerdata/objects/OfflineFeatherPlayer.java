package cc.ddev.feather.api.playerdata.objects;

import cc.ddev.feather.database.models.PlayerModel;
import cc.ddev.feather.logger.Log;
import cc.ddev.feather.player.PlayerProfile;
import cc.ddev.feather.player.PlayerWrapper;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

import java.util.UUID;

public class OfflineFeatherPlayer implements FeatherPlayer {

    private UUID uuid;

    public OfflineFeatherPlayer(UUID uuid) {
        StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        if (traces.length >= 3 && !traces[2].getClassName().startsWith("cc.ddev.feather.api.playerdata")) {
            Log.getLogger().warn(traces[2] + " created an instance of OfflineFeatherPlayer. This is not expected and might lead to issues!");
            this.uuid = uuid;
        }
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    // TODO: Fix this
    @Override
    public Pos getLastLocation() {
        Player player = MinecraftServer.getConnectionManager().getPlayer(uuid);
        PlayerProfile playerProfile = PlayerWrapper.getPlayerProfile(player);
        PlayerModel playerModel = playerProfile.getPlayerModel();
        String[] extractedCoords = playerModel.getLastLocation().split(",");
        extractedCoords[0] = extractedCoords[0].replace("x=", "");
        extractedCoords[1] = extractedCoords[1].replace("y=", "");
        extractedCoords[2] = extractedCoords[2].replace("z=", "");
        double x = Double.parseDouble(extractedCoords[0]);
        double y = Double.parseDouble(extractedCoords[1]);
        double z = Double.parseDouble(extractedCoords[2]);
        return new Pos(x, y, z);
    }
}
