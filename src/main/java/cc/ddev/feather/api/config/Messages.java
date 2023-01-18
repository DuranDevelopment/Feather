package cc.ddev.feather.api.config;

import lombok.Getter;

public class Messages extends Config {

    @Getter
    public static String NO_PERMISSION = getFeatherConfig().getString("messages.no_permission");

    @Getter
    public static String INVALID_ARGUMENTS = getFeatherConfig().getString("messages.invalid_arguments");

    @Getter
    public static String INVALID_USERNAME = getFeatherConfig().getString("messages.invalid_username");

    @Getter
    public static String NOT_ONLINE = getFeatherConfig().getString("messages.not_online");

    @Getter
    public static String FLY_ON = getFeatherConfig().getString("messages.fly_on");

    @Getter
    public static String FLY_OFF = getFeatherConfig().getString("messages.fly_off");

    @Getter
    public static String JOIN_MESSAGE = getFeatherConfig().getString("messages.join.message");

    @Getter
    public static String TITLE_LINE_1 = getFeatherConfig().getString("messages.title.line1");

    @Getter
    public static String TITLE_LINE_2 = getFeatherConfig().getString("messages.title.line2");
}
