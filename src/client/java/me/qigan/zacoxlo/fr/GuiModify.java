package me.qigan.zacoxlo.fr;

import com.google.gson.JsonObject;
import me.qigan.zacoxlo.cfg.Module;

public class GuiModify extends Module {
    @Override
    public String id() {
        return "gui_modify";
    }

    @Override
    public String description() {
        return "Modifies basic MC guis.";
    }

    @Override
    public JsonObject sets() {
        JsonObject obj = new JsonObject();
        obj.addProperty("Hide hud effects", true);
        obj.addProperty("Hide inventory effects", true);
        return obj;
    }

    @Override
    public void onRegister() {

    }
}
