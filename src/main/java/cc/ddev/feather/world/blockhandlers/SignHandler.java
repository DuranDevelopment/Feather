package cc.ddev.feather.world.blockhandlers;

import cc.ddev.feather.logger.Log;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.network.packet.server.play.OpenSignEditorPacket;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

public class SignHandler implements BlockHandler {

    @Override
    public @NotNull NamespaceID getNamespaceId() {
        return NamespaceID.from("minecraft", "sign");
    }

    @Override
    public void onPlace(@NotNull BlockHandler.Placement placement) {
        if (!(placement instanceof PlayerPlacement playerPlacement)) return;
        OpenSignEditorPacket openSignEditorPacket = new OpenSignEditorPacket(placement.getBlockPosition(), true);
        playerPlacement.getPlayer().getPlayerConnection().sendPacket(openSignEditorPacket);
    }

    public @NotNull Collection<Tag<?>> getBlockEntityTags() {
        return Arrays.asList(
                Tag.Byte("GlowingText"),
                Tag.String("Color"),
                Tag.String("Text1"),
                Tag.String("Text2"),
                Tag.String("Text3"),
                Tag.String("Text4"));
    }
}