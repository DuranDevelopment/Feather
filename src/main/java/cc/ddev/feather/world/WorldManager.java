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


    private static WorldManager worldManager;
    public static WorldManager getInstance() {
        if (worldManager == null) {
            worldManager = new WorldManager();
        }
        return worldManager;
    }


    private final String currentDirectory = System.getProperty("user.dir");

    @Getter
    public final String worldsDirectory = currentDirectory + File.separator + "worlds";

    public void saveWorld(InstanceContainer instanceContainer) {
        Log.getLogger().info("Saving world \"" + getInstanceName(instanceContainer) + "\" (UUID: " + instanceContainer.getUniqueId() + ")" + "...");
        instanceContainer.saveChunksToStorage();
    }

    public void saveWorlds() {
        for (@NotNull Instance instance : MinecraftServer.getInstanceManager().getInstances()) {
            Log.getLogger().info("Saving world \"" + getInstanceName(instance) + "\" (UUID: " + instance.getUniqueId() + ")" + "...");
            instance.saveChunksToStorage();
        }
    }

    public InstanceContainer loadWorld(String loadPath) {
        InstanceContainer instanceContainer = MinecraftServer.getInstanceManager().createInstanceContainer();
        instanceContainer.setChunkLoader(new AnvilLoader(loadPath));
        String worldName = new File(loadPath).getName();
        setInstanceName(instanceContainer, worldName);
        Log.getLogger().info("Loaded world \"" + worldName + "\" (UUID: " + instanceContainer.getUniqueId() + ")" + "!");

        return instanceContainer;
    }

    public InstanceContainer getWorld(String worldName) {
        for (Instance instance : MinecraftServer.getInstanceManager().getInstances()) {
            if (instance.getTag(Tag.String("name")).equalsIgnoreCase(worldName)) {
                return (InstanceContainer) instance;
            }
        }
        return null;
    }

    public void createWorldsDirectory() {
        File f = new File(worldsDirectory);
        if (!f.exists()) {
            Log.getLogger().info("Creating worlds directory...");
            f.mkdir();
        } else {
            Log.getLogger().info("Worlds directory already exists!");
        }
    }

    public void setInstanceName(Instance instance, String name) {
        instance.setTag(Tag.String("name"), name);
    }

    public String getInstanceName(Instance instance) {
        String name = instance.getTag(Tag.String("name"));
        if (name == null) {
            name = instance.getUniqueId().toString();
        }
        return name;
    }

    public String getInstanceUniqueId(Instance instance) {
        if (instance.getUniqueId().toString() == null) {
            return null;
        }
        return instance.getUniqueId().toString();
    }

    public boolean worldExists(String worldName) {
        File f = new File(worldsDirectory + File.separator + worldName);
        return f.exists();
    }

    public boolean worldsDirectoryIsEmpty() {
        File f = new File(worldsDirectory);
        String[] fileList = f.list();
        if (fileList == null) {
            return true;  // Directory is empty or doesn't exist
        }
        return fileList.length == 0;
    }


    @Nullable
    public Collection<WorldModel> findWorldModel(String worldName) {
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

    public boolean isMTWorld(String worldName) {
        Collection<WorldModel> worldModel = findWorldModel(worldName);
        return worldModel != null && !worldModel.isEmpty();
    }

    public String getLoadingName(String worldName) {
        Collection<WorldModel> worldModels = findWorldModel(worldName);

        if (worldModels != null && !worldModels.isEmpty()) {
            return worldModels.iterator().next().getLoadingName();
        }
        return null;
    }

    public String getColor(String worldName) {
        Collection<WorldModel> worldModels = findWorldModel(worldName);

        if (worldModels != null && !worldModels.isEmpty()) {
            return worldModels.iterator().next().getColor();
        }
        return null;
    }
}
