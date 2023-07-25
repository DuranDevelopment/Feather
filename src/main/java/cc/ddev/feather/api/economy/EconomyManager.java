package cc.ddev.feather.api.economy;

import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.database.models.PlayerModel;
import cc.ddev.feather.player.PlayerProfile;
import cc.ddev.feather.player.PlayerWrapper;
import net.minestom.server.entity.Player;

public class EconomyManager {

    private static EconomyManager instance;

    public static EconomyManager getInstance() {
        if (instance == null) {
            instance = new EconomyManager();
        }
        return instance;
    }

    public void removeBalance(Player player, double amount) {
        PlayerProfile playerProfile = PlayerWrapper.getPlayerProfile(player);
        if (playerProfile == null) throw new NullPointerException("PlayerProfile is null");
        PlayerModel playerModel = playerProfile.getPlayerModel();

        playerModel.setBalance(playerModel.getBalance() - amount);
        StormDatabase.getInstance().saveStormModel(playerModel);
    }

    public void addBalance(Player player, double amount) {
        PlayerProfile playerProfile = PlayerWrapper.getPlayerProfile(player);
        if (playerProfile == null) throw new NullPointerException("PlayerProfile is null");
        PlayerModel playerModel = playerProfile.getPlayerModel();

        playerModel.setBalance(playerModel.getBalance() + amount);
        StormDatabase.getInstance().saveStormModel(playerModel);
    }

    public void setBalance(Player player, double amount) {
        PlayerProfile playerProfile = PlayerWrapper.getPlayerProfile(player);
        if (playerProfile == null) throw new NullPointerException("PlayerProfile is null");
        PlayerModel playerModel = playerProfile.getPlayerModel();

        playerModel.setBalance(amount);
        StormDatabase.getInstance().saveStormModel(playerModel);
    }

    public double getBalance(Player player) {
        PlayerProfile playerProfile = PlayerWrapper.getPlayerProfile(player);
        if (playerProfile == null) throw new NullPointerException("PlayerProfile is null");
        PlayerModel playerModel = playerProfile.getPlayerModel();

        return playerModel.getBalance();
    }
}
