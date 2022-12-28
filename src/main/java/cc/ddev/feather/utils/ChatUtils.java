package cc.ddev.feather.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ChatUtils {
    public static Component translateMiniMessage(String string) {
        return MiniMessage.miniMessage().deserialize(string);
    }
}
