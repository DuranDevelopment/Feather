package cc.ddev.feather.world;

import cc.ddev.feather.logger.Log;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;

public class SaveWorldTask {

    // Save the world every 5 minutes
    public static void registerTask() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();
        scheduler.submitTask(() -> {
            Log.getLogger().info("Saving worlds...");
            saveWorlds();
            return TaskSchedule.minutes(5);
        });
    }

    public static void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Log.getLogger().info("Saving worlds...");
            saveWorlds();
        }));
    }

    private static void saveWorlds() {
        for (@NotNull Instance instance : MinecraftServer.getInstanceManager().getInstances()) {
            Log.getLogger().info("Saving world \"" + WorldManager.getInstanceName(instance) + "\" (UUID: " + instance.getUniqueId() + ")" + "...");
            instance.saveChunksToStorage();
        }
    }
}
