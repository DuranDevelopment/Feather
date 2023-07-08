package cc.ddev.feather.listener.handler;

import cc.ddev.feather.logger.Log;
import net.minestom.server.MinecraftServer;
import net.minestom.server.network.packet.client.ClientPacket;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface PacketListener {
    default void register() {
        var packetListenerManager = MinecraftServer.getPacketListenerManager();
        var methods = this.getClass().getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Listen.class)) {
                var paramTypes = method.getParameterTypes();
                // Check if the method has a single parameter of type Packet or a subclass of Packet
                if(paramTypes.length == 1 && ClientPacket.class.isAssignableFrom(paramTypes[0])) {
                    // Get the class of the packet from the parameter type
                    Class<? extends ClientPacket> packetToStickTo = paramTypes[0].asSubclass(ClientPacket.class);
                    // Register the packet listener
                    packetListenerManager.setListener(packetToStickTo, (packet, player) -> {
                        try {
                            method.invoke(this, packet);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    if (Log.getLogger().isDebugEnabled()) Log.getLogger().debug("Registered listener for packet " + packetToStickTo.getSimpleName());
                } else {
                    throw new IllegalArgumentException("Method annotated with @Listen must have a single parameter of type ClientPacket or a subclass of ClientPacket.");
                }
            }
        }
    }
}
