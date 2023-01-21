package cc.ddev.feather.sidebar;

import cc.ddev.feather.world.WorldManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;

public class SidebarRefreshTask {

    // Refresh the sidebar every 5 seconds
    public static void registerTask() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();
        scheduler.submitTask(() -> {
            refreshSidebar();
            return TaskSchedule.seconds(5);
        });
    }

    private static void refreshSidebar() {
        for (@NotNull Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            if (WorldManager.isMTWorld(WorldManager.getInstanceName(player.getInstance()))) {
                SidebarManager.buildSidebar(player);
            }
        }
    }
}
