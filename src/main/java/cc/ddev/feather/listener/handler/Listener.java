package cc.ddev.feather.listener.handler;

import cc.ddev.feather.logger.Log;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface Listener {
    default void register() {
        var eventHandler = MinecraftServer.getGlobalEventHandler();
        var methods = this.getClass().getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Listen.class)) {
                var paramTypes = method.getParameterTypes();
                // Check if the method has a single parameter of type Event or a subclass of Event
                if(paramTypes.length == 1 && Event.class.isAssignableFrom(paramTypes[0])) {
                    // Get the class of the event from the parameter type
                    Class<?> eventToStickTo = paramTypes[0];
                    // Check if the event is a subclass of Event
                    if(Event.class.isAssignableFrom(eventToStickTo)) {
                        // Register the event

                        eventHandler.addListener(eventToStickTo.asSubclass(Event.class), event -> {
                            try {
                                method.invoke(this, event);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        Log.getLogger().debug("Registered listener for event " + eventToStickTo.getSimpleName());
                    } else {
                        throw new IllegalArgumentException("Method annotated with @Listen must have a single parameter of type Event or a subclass of Event.");
                    }
                } else {
                    throw new IllegalArgumentException("Method annotated with @Listen must have a single parameter of type Event or a subclass of Event.");
                }
            }
        }
    }
}