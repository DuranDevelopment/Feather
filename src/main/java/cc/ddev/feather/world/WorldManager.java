package cc.ddev.feather.world;

import cc.ddev.feather.logger.Log;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.tag.Tag;

import java.io.File;

public class WorldManager {

    private static final String currentDirectory = System.getProperty("user.dir");

    @Getter
    public static final String worldsDirectory = currentDirectory + File.separator + "worlds";

    public static void saveWorld(String savePath, InstanceContainer instanceContainer) {
        instanceContainer.saveChunksToStorage();
    }

    public static InstanceContainer loadWorld(String loadPath) {
        InstanceContainer instanceContainer = MinecraftServer.getInstanceManager().createInstanceContainer();
        instanceContainer.setChunkLoader(new AnvilLoader(loadPath));
        String worldName = new File(loadPath).getName();
        setInstanceName(instanceContainer, worldName);
        Log.getLogger().info("Loaded world!");

        return instanceContainer;
    }

    public static InstanceContainer getWorld(String worldName) {
        for (Instance instance : MinecraftServer.getInstanceManager().getInstances()) {
            if (instance.getTag(Tag.String("name")).equalsIgnoreCase(worldName)) {
                return (InstanceContainer) instance;
            }
        }

        return null;
    }

    public static void createWorldsDirectory() {
        File f = new File(worldsDirectory);
        if (!f.exists()) {
            Log.getLogger().info("Creating worlds directory...");
            f.mkdir();
        } else {
            Log.getLogger().info("Worlds directory already exists!");
        }
    }

    public static void setInstanceName(Instance instance, String name) {
        instance.setTag(Tag.String("name"), name);
    }

    public static String getInstanceName(Instance instance) {
        String name = instance.getTag(Tag.String("name"));
        if (name == null) {
            name = instance.getUniqueId().toString();
        }
        return name;
    }

    public static boolean worldExists(String worldName) {
        File f = new File(worldsDirectory + File.separator + worldName);
        return f.exists();
    }

    public static boolean worldsDirectoryIsEmpty() {
        File f = new File(worldsDirectory);
        if (f.list() == null) {
            return true;
        }
        return f.list().length == 0;
    }
}
