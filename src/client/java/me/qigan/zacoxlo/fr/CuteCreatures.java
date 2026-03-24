package me.qigan.zacoxlo.fr;

import com.google.gson.JsonObject;
import me.qigan.zacoxlo.cfg.Module;

public class CuteCreatures extends Module {
    @Override
    public String id() {
        return "mipis";
    }

    @Override
    public String description() {
        return "Scales player models";
    }

    @Override
    public void onRegister() {}

    @Override
    public JsonObject sets() {
        JsonObject object = new JsonObject();
        object.addProperty("Scale X", 0.65f);
        object.addProperty("Scale Y", 0.65f);
        object.addProperty("Scale Z", 0.65f);
        return object;
    }
}
