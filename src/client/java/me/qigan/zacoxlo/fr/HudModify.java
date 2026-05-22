package me.qigan.zacoxlo.fr;

import com.google.gson.JsonObject;
import me.qigan.zacoxlo.cfg.Module;
import net.minecraft.client.gui.screens.ChatScreen;

public class HudModify extends Module {
    @Override
    public String id() {
        return "hud_modify";
    }

    @Override
    public String description() {
        return "Modifies basic MC hud.";
    }

    @Override
    public JsonObject sets() {
        JsonObject obj = new JsonObject();
        obj.addProperty("Hide effects", true);
        return obj;
    }

    @Override
    public void onRegister() {

    }
}
