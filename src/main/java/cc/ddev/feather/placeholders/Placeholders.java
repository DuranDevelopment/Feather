package cc.ddev.feather.placeholders;

import cc.ddev.feather.player.PlayerProfile;
import net.minestom.server.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Placeholders {

    private static final Pattern placeholderPattern = Pattern.compile("<\\w+>", Pattern.CASE_INSENSITIVE);

    private static final Map<String /*placeholder key*/, Function<Player, String> /*function to parse said key*/> placeholders = new HashMap<>();

    static {
        addPlaceholder("player", Player::getUsername);
        addPlaceholder("world", PlayerProfile::getInstanceName);
    }

    /**
     * Parse a placeholdered string
     * @param p the player to be parsed
     * @param str the string to be parsed
     * @return the parsed and placeholdered string
     */
    public static String parse(Player p, String str){

        AtomicReference<String> text = new AtomicReference<>(str);

        Matcher m = placeholderPattern.matcher(text.get());
        m.results().forEach(matchResult -> {
            String s = matchResult.group();
            String noTags = s.substring(1, s.length() - 1);
            Function<Player, String> f = placeholders.get(noTags);
            if (f != null) {
                text.set(text.get().replace(s, f.apply(p)));
            }
        });

        return text.get();
    }

    public static void addPlaceholder(String key, Function<Player, String> func){
        placeholders.put(key, func);
    }
}
