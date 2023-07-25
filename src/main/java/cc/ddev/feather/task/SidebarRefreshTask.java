package cc.ddev.feather.task;

import cc.ddev.feather.api.sidebar.SidebarManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;

public class SidebarRefreshTask {

    // Refresh the sidebar every 5 seconds
    public static void registerTask() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();
        scheduler.submitTask(() -> {
            SidebarManager.refreshSidebar();
            return TaskSchedule.seconds(5);
        });
    }
}
