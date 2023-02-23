package cc.ddev.feather.listener.player;

import cc.ddev.feather.listener.handler.Listen;
import cc.ddev.feather.listener.handler.Listener;
import cc.ddev.feather.world.blockhandlers.SignHandler;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.block.BlockHandler;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTException;
import org.jglrxavpok.hephaistos.parser.SNBTParser;

import java.io.StringReader;

public class PlayerBlockPlaceListener implements Listener {
    @Listen
    public void onPlayerPlaceBlock(PlayerBlockPlaceEvent event) {
        if (event.getBlock().name().endsWith("sign")) {
            BlockHandler signHandler = new SignHandler();
            try {
                NBT nbt = new SNBTParser(new StringReader("{\"GlowingText\":0B,\"Color\":\"black\",\"Text1\":\"{\\\"text\\\":\\\"\\\"}\"," +
                        "\"Text2\":\"{\\\"text\\\":\\\"\\\"}\",\"Text3\":\"{\\\"text\\\":\\\"\\\"}\",\"Text4\":\"{\\\"text\\\":\\\"\\\"}\"}")).parse();
                event.setBlock(event.getBlock().withHandler(signHandler).withNbt((NBTCompound) nbt));
            } catch (NBTException ex) {
                ex.printStackTrace();
            }
        }
    }
}
