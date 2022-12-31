package cc.ddev.feather.database.models;

import cc.ddev.feather.world.WorldManager;
import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.minestom.server.entity.Player;

import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "players")
public class PlayerModel extends StormModel {

    private static @Getter ExecutorService executorService;

    @Column
    private UUID uniqueId;

    @Column(defaultValue = "0")
    private String lastLocation;

    public static String getInstanceName(Player player) {
        if (player == null) {
            return "null";
        }
        return WorldManager.getInstanceUniqueId(player.getInstance());
    }
}
