package cc.ddev.feather.api.playerdata;

import cc.ddev.feather.api.playerdata.objects.FeatherPlayer;
import cc.ddev.feather.api.playerdata.objects.OfflineFeatherPlayer;
import cc.ddev.feather.api.playerdata.objects.OnlineFeatherPlayer;
import cc.ddev.feather.database.models.PlayerModel;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {
    private static final ConcurrentHashMap<UUID, OnlineFeatherPlayer> featherplayers = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<UUID, OfflineFeatherPlayer> cachedofflineplayers = new ConcurrentHashMap<>();
    private static PlayerManager instance;
    public static PlayerManager getInstance() {
        if (instance == null)
            instance = new PlayerManager();
        return instance;
    }
    public Collection<OnlineFeatherPlayer> getOnlinePlayers() {
        return featherplayers.values();
    }

    public static FeatherPlayer getPlayer(UUID uuid) {
        if (featherplayers.containsKey(uuid))
            return getOnlinePlayer(uuid);
        return getOfflinePlayer(uuid);
    }

    public static OnlineFeatherPlayer getOnlinePlayer(UUID uuid) {
        return featherplayers.get(uuid);
    }

    public static OfflineFeatherPlayer getOfflinePlayer(UUID uuid) {
        if (cachedofflineplayers.contains(uuid))
            return cachedofflineplayers.get(uuid);
        OfflineFeatherPlayer target = new OfflineFeatherPlayer(uuid);
        cachedofflineplayers.remove(uuid);
        cachedofflineplayers.put(uuid, target);
        return target;
    }

    private void changeUUID(UUID newUuid, PlayerModel playerModel) {
        playerModel.setUniqueId(newUuid);
    }
}
