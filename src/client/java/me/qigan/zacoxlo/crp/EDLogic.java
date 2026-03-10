package me.qigan.zacoxlo.crp;


import me.qigan.zacoxlo.Holder;
import me.qigan.zacoxlo.cfg.Module;

import java.util.HashMap;
import java.util.Map;

public interface EDLogic {

    public static Map<String, Runnable> eLogic = new HashMap<>();
    public static Map<String, Runnable> dLogic = new HashMap<>();

    public static void tryEnableLogic(String id) {
        Module mod = Holder.MAPPER.get(id);
        if (mod instanceof EDLogic) ((EDLogic) mod).onEnable();
        else if (EDLogic.eLogic.containsKey(id)) EDLogic.eLogic.get(id).run();
    }

    public static void tryDisableLogic(String id) {
        Module mod = Holder.MAPPER.get(id);
        if (mod instanceof EDLogic) ((EDLogic) mod).onDisable();
        else if (EDLogic.dLogic.containsKey(id)) EDLogic.dLogic.get(id).run();
    }

    void onEnable();
    void onDisable();
}