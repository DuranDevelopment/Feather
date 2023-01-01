package cc.ddev.feather.player;

import cc.ddev.feather.database.models.PlayerModel;
import lombok.Getter;
import net.minestom.server.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerWrapper {
    public static @Getter HashMap<UUID, PlayerModel> playerModels = new HashMap<>();

    public static FeatherPlayer getFeatherPlayer(Player player) {
        if (!playerModels.containsKey(player.getUuid())) {
            return null;
        }

        return new FeatherPlayer(player, playerModels.get(player.getUuid()));
    }
}
