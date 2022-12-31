package cc.ddev.feather.listeners.server;

import cc.ddev.feather.api.config.Config;
import cc.ddev.feather.listeners.handler.Listen;
import cc.ddev.feather.listeners.handler.Listener;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.ping.ResponseData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

/*
    Listening to the ServerListPingEvent to set server information
 */
public class ServerListPingListener implements Listener {

    @Listen(event = ServerListPingEvent.class)
    public void onServerListPing(ServerListPingEvent event) {
        int onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers().size();
        ResponseData responseData = event.getResponseData();
        // Set the server's motd
        responseData.setDescription(LegacyComponentSerializer.legacyAmpersand().deserialize(Config.Server.DESCRIPTION));
        // Set online players
        responseData.setOnline(onlinePlayers);
        // Set the server's max player count
        responseData.setMaxPlayer(Config.Server.MAX_PLAYERS);

        // Cache favicon image displayed in server list, needs to be base64 format
        String cachedFavicon;
        try {
            BufferedImage image = ImageIO.read(new File("./server-icon.png")); // Use vanilla file name
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);
            cachedFavicon = Base64.getEncoder().encodeToString(outputStream.toByteArray());
            outputStream.close();
        } catch (IOException e) {
            cachedFavicon = "";
        }

        // Set the server's icon
        if(!cachedFavicon.isEmpty()) {
            responseData.setFavicon("data:image/png;base64,"+ cachedFavicon);
        }
    }
}
