package cc.ddev.feather.listener.client;

import cc.ddev.feather.Server;
import cc.ddev.feather.api.config.Config;
import cc.ddev.feather.listener.handler.Listen;
import cc.ddev.feather.listener.handler.PacketListener;
import cc.ddev.feather.utils.ChatUtils;
import cc.ddev.feather.world.WorldManager;
import cc.ddev.feather.world.blockhandlers.SignHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.client.play.ClientUpdateSignPacket;
import net.minestom.server.tag.Tag;

public class ClientUpdateSignListener implements PacketListener {
    @Listen
    public static void listener(ClientUpdateSignPacket packet) {
        Block sign = WorldManager.getInstance().getWorld(Config.Spawn.WORLD).getBlock(packet.blockPosition());
        sign = sign.withTag(Tag.String("Text1"), GsonComponentSerializer.gson().serialize(ChatUtils.translateMiniMessage(packet.lines().get(0))));
        sign = sign.withTag(Tag.String("Text2"), GsonComponentSerializer.gson().serialize(ChatUtils.translateMiniMessage(packet.lines().get(1))));
        sign = sign.withTag(Tag.String("Text3"), GsonComponentSerializer.gson().serialize(ChatUtils.translateMiniMessage(packet.lines().get(2))));
        sign = sign.withTag(Tag.String("Text4"), GsonComponentSerializer.gson().serialize(ChatUtils.translateMiniMessage(packet.lines().get(3))));
        sign = sign.withHandler(new SignHandler());
        WorldManager.getInstance().getWorld(Config.Spawn.WORLD).setBlock(packet.blockPosition(), sign);
    }
}
