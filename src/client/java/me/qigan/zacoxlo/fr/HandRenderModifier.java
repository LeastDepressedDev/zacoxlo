package me.qigan.zacoxlo.fr;

import com.google.gson.JsonObject;
import me.qigan.zacoxlo.cfg.Module;

public class HandRenderModifier extends Module {
    @Override
    public String id() {
        return "hand_render";
    }

    @Override
    public String description() {
        return "Modifies, how your hand looks. I tried to write it the most flexible i could. Scrapping mc code is, uhm, insane shit";
    }

    @Override
    public void onRegister() {}

    @Override
    public JsonObject sets() {
        JsonObject object = new JsonObject();
        // TODO: Write cfg for this
        return object;
    }
}
