package levertion.overviewericons.server;

import com.google.gson.JsonObject;

import net.minecraft.entity.player.EntityPlayer;

public class OverviewerIconsMessages {
    private static JsonObject base_object(EntityPlayer player) {
        JsonObject o = new JsonObject();
        o.addProperty("username", player.getName());
        return o;
    }

    private static JsonObject set_reason(JsonObject o, String status) {
        o.addProperty("reason", status);
        return o;
    }

    private static JsonObject player_object(EntityPlayer player) {
        JsonObject o = base_object(player);
        o.addProperty("x", player.lastTickPosX);
        o.addProperty("y", player.lastTickPosY);
        o.addProperty("z", player.lastTickPosZ);
        return o;
    }

    public static String update_message(EntityPlayer player) {
        return set_reason(player_object(player), "update").toString();
    }

    public static String connect_message(EntityPlayer player) {
        return set_reason(player_object(player), "connect").toString();
    }

    public static String disconnect_message(EntityPlayer player) {
        return set_reason(base_object(player), "disconnect").toString();
    }
}
