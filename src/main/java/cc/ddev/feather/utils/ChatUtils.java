package cc.ddev.feather.utils;

import cc.ddev.feather.api.config.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatUtils {
    public static Component translateMiniMessage(String string) {
        return MiniMessage.miniMessage().deserialize(string).decoration(TextDecoration.ITALIC, false);
    }

    public static Component translateMiniMessage(String string, boolean italic) {
        return MiniMessage.miniMessage().deserialize(string).decoration(TextDecoration.ITALIC, italic);
    }

    public static String stripMiniMessage(Component component) {
        return MiniMessage.miniMessage().serialize(component);
    }

    public static List<String> splitStringByNewline(String input) {
        return Arrays.asList(input.split("\n"));
    }

    public static List<Component> splitStringByNewLineToComponent(String input) {
        String[] list = input.split("\n");
        List<Component> componentList = new ArrayList<>(List.of());
        for (String s : list) {
            componentList.add(translateMiniMessage(s));
        }
        return componentList;
    }

    public static boolean compareComponent(Component component, String compare) {
        // Clear all styles from the component
        Component clearedTitle = component.style(Style.style());
        // Clear all styles from the compare component
        Component clearedCompareComponent = ChatUtils.translateMiniMessage(compare).style(Style.style());
        return clearedTitle.equals(clearedCompareComponent);
    }

    public static void broadcast(Component component) {
        MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(player -> player.sendMessage(component));
    }
}
