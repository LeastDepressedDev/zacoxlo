package me.qigan.zacoxlo;

import me.qigan.zacoxlo.cfg.Module;
import me.qigan.zacoxlo.crp.Experimental;
import me.qigan.zacoxlo.fr.*;
import me.qigan.zacoxlo.fr.macro.MacroController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Holder {
    public static Map<String, Module> MAPPER = new HashMap<>();
    public static List<Module> MRL = new ArrayList<>();

    public static void init() {
        register(new StarredMobs());
        register(new AutoLevers());
        register(new AutoBoostPlacement());
        register(new Trail());
        register(new SSModulo());
        register(new MacroController());

        register(new Experimental());
    }

    public static void register(Module module) {
        MAPPER.put(module.id(), module);
        MRL.add(module);
        module.onRegister();
    }
}
