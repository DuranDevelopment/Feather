package cc.ddev.feather.world;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;

public class SavePlayerTask {
    public static void registerTask() {
        for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {

        }
    }

    public static void registerShutdownHook() {

    }

    public static void savePlayer() {

    }
}
