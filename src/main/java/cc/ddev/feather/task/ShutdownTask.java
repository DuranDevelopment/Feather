package cc.ddev.feather.task;

import cc.ddev.feather.logger.Log;
import cc.ddev.feather.world.WorldManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.SchedulerManager;

public class ShutdownTask {
    public static void registerTask() {
        SchedulerManager schedulerManager = MinecraftServer.getSchedulerManager();
        schedulerManager.buildShutdownTask(() -> {
            Log.getLogger().info("Saving worlds...");
            WorldManager.saveWorlds();
        });
    }

    public static void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Log.getLogger().info("Saving worlds...");
            WorldManager.saveWorlds();
        }));
    }
}
