package cc.ddev.feather.api.playerdata.objects;

import cc.ddev.feather.database.models.PlayerModel;
import cc.ddev.feather.player.PlayerProfile;
import cc.ddev.feather.player.PlayerWrapper;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

import java.util.UUID;

public class OnlineFeatherPlayer implements FeatherPlayer {
    private UUID uuid;
    private String lastLocation;
    private double balance;

    public OnlineFeatherPlayer(UUID uuid, String lastLocation, double balance) {
        this.uuid = uuid;
        this.lastLocation = lastLocation;
        this.balance = balance;
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

    @Override
    public double getBalance() {
        Player player = MinecraftServer.getConnectionManager().getPlayer(uuid);
        PlayerProfile playerProfile = PlayerWrapper.getPlayerProfile(player);
        PlayerModel playerModel = playerProfile.getPlayerModel();
        return playerModel.getBalance();
    }
}
