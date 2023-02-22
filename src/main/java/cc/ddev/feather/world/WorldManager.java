package cc.ddev.feather.world;

import cc.ddev.feather.database.StormDatabase;
import cc.ddev.feather.database.models.WorldModel;
import cc.ddev.feather.logger.Log;
import com.craftmend.storm.api.enums.Where;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.AnvilLoader;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;

public class WorldManager {

    private static final String currentDirectory = System.getProperty("user.dir");

    @Getter
    public static final String worldsDirectory = currentDirectory + File.separator + "worlds";

    public static void saveWorld(InstanceContainer instanceContainer) {
        Log.getLogger().info("Saving world \"" + WorldManager.getInstanceName(instanceContainer) + "\" (UUID: " + instanceContainer.getUniqueId() + ")" + "...");
        instanceContainer.saveChunksToStorage();
    }

    public static void saveWorlds() {
        for (@NotNull Instance instance : MinecraftServer.getInstanceManager().getInstances()) {
            Log.getLogger().info("Saving world \"" + WorldManager.getInstanceName(instance) + "\" (UUID: " + instance.getUniqueId() + ")" + "...");
            instance.saveChunksToStorage();
        }
    }

    public static InstanceContainer loadWorld(String loadPath) {
        InstanceContainer instanceContainer = MinecraftServer.getInstanceManager().createInstanceContainer();
        instanceContainer.setChunkLoader(new AnvilLoader(loadPath));
        String worldName = new File(loadPath).getName();
        setInstanceName(instanceContainer, worldName);
        Log.getLogger().info("Loaded world \"" + worldName + "\" (UUID: " + instanceContainer.getUniqueId() + ")" + "!");

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

    public static String getInstanceUniqueId(Instance instance) {
        if (instance.getUniqueId().toString() == null) {
            return null;
        }
        return instance.getUniqueId().toString();
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

    @Nullable
    public static Collection<WorldModel> findWorldModel(String worldName) {
        try {
            Collection<WorldModel> worldModel;
            worldModel = StormDatabase.getInstance().getStorm().buildQuery(WorldModel.class)
                    .where("worldname", Where.EQUAL, worldName)
                    .limit(1)
                    .execute()
                    .join();
            return worldModel;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isMTWorld(String worldName) {
        return !findWorldModel(worldName).isEmpty();
    }

    public static String getLoadingName(String worldName) {
        return findWorldModel(worldName).iterator().next().getLoadingName();
    }

    public static String getColor(String worldName) {
        return findWorldModel(worldName).iterator().next().getColor();
    }
}
