package cc.ddev.feather.api.economy;

import cc.ddev.feather.database.models.PlayerModel;
import cc.ddev.feather.player.PlayerProfile;
import cc.ddev.feather.player.PlayerWrapper;
import net.minestom.server.entity.Player;

public class Economy {

    private static Economy instance;

    public static Economy getInstance() {
        if (instance == null) {
            instance = new Economy();
        }
        return instance;
    }

    public void removeBalance(Player player, double amount) {
        PlayerProfile playerProfile = PlayerWrapper.getPlayerProfile(player);
        if (playerProfile == null) throw new NullPointerException("PlayerProfile is null");
        PlayerModel playerModel = playerProfile.getPlayerModel();

        playerModel.setBalance(playerModel.getBalance() - amount);
    }

    public void addBalance(Player player, double amount) {
        PlayerProfile playerProfile = PlayerWrapper.getPlayerProfile(player);
        if (playerProfile == null) throw new NullPointerException("PlayerProfile is null");
        PlayerModel playerModel = playerProfile.getPlayerModel();

        playerModel.setBalance(playerModel.getBalance() + amount);
    }

    public void setBalance(Player player, double amount) {
        PlayerProfile playerProfile = PlayerWrapper.getPlayerProfile(player);
        if (playerProfile == null) throw new NullPointerException("PlayerProfile is null");
        PlayerModel playerModel = playerProfile.getPlayerModel();

        playerModel.setBalance(amount);
    }

    public double getBalance(Player player) {
        PlayerProfile playerProfile = PlayerWrapper.getPlayerProfile(player);
        if (playerProfile == null) throw new NullPointerException("PlayerProfile is null");
        PlayerModel playerModel = playerProfile.getPlayerModel();

        return playerModel.getBalance();
    }
}
