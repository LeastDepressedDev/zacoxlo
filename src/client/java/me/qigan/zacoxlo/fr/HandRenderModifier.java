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
        object.addProperty("Disable down swing animation", false);

        JsonObject itemScales = new JsonObject();
        itemScales.addProperty("Scale X", 1f);
        itemScales.addProperty("Scale Y", 1f);
        itemScales.addProperty("Scale Z", 1f);
        object.add("Item scale", itemScales);

        JsonObject itemPos = new JsonObject();
        itemPos.addProperty("Translate X", 0f);
        itemPos.addProperty("Translate Y", 0f);
        itemPos.addProperty("Translate Z", 0f);
        object.add("Item translate", itemPos);

//        JsonObject animSupress = new JsonObject();
//        animSupress.addProperty("Multiply X", 1f);
//        animSupress.addProperty("Multiply Y", 1f);
//        animSupress.addProperty("Multiply Z", 1f);
//        object.add("Animation supress", animSupress);

        return object;
    }
}
