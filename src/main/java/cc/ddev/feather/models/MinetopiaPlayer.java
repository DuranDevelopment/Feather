package cc.ddev.feather.models;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class MinetopiaPlayer {
    public String uuid;
    public String username;
    public int level;
    public int exp;
    public int money;

    public MinetopiaPlayer(String uuid, String username, int level, int exp, int money) {
        this.uuid = uuid;
        this.username = username;
        this.level = level;
        this.exp = exp;
        this.money = money;
    }

    //Get UUID of player from username
    public static String getUUID(String name) {
        String uuid;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openStream()));
            uuid = (((JsonObject) JsonParser.parseReader(in)).get("id")).toString().replaceAll("\"", "");
            uuid = uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
            in.close();
        } catch (Exception e) {
            System.out.println("Unable to get UUID of: " + name + "!");
            uuid = "er";
        }
        return uuid;
    }
}
