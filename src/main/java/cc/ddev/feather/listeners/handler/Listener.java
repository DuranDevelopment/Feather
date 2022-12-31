package cc.ddev.feather.listeners.handler;

import net.minestom.server.MinecraftServer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface Listener {
    default void register() {
        var eventHandler = MinecraftServer.getGlobalEventHandler();
        var methods = this.getClass().getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Listen.class)) {
                    var annotation = method.getAnnotation(Listen.class);
                    var eventToStickTo = annotation.event();

                    eventHandler.addListener(eventToStickTo, event -> {
                        try {
                            method.invoke(this, event);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
        }
    }
}
