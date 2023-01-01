package cc.ddev.feather.api.playerdata;

import cc.ddev.feather.api.playerdata.objects.OfflineFeatherPlayer;
import cc.ddev.feather.api.playerdata.objects.OnlineFeatherPlayer;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {
    private final ConcurrentHashMap<UUID, OnlineFeatherPlayer> featherplayers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, OfflineFeatherPlayer> cachedofflineplayers = new ConcurrentHashMap<>();
    private static PlayerManager instance;
    public static PlayerManager getInstance() {
        if (instance == null)
            instance = new PlayerManager();
        return instance;
    }


}
