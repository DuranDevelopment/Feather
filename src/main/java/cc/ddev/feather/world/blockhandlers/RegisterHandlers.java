package cc.ddev.feather.world.blockhandlers;

import cc.ddev.feather.logger.Log;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockHandler;

public class RegisterHandlers {
    public static void initHandlers() {
        BlockHandler signHandler = new SignHandler();
        MinecraftServer.getBlockManager().registerHandler("minecraft:sign", () -> signHandler);

        for (Block block : Block.values()) {
            if (block.name().endsWith("sign")) {
                MinecraftServer.getBlockManager().registerHandler(block.name(), () -> signHandler);
            }
        }
        Log.getLogger().info("Registered block handlers");
    }
}
