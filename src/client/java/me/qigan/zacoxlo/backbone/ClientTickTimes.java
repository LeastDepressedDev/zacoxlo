package me.qigan.zacoxlo.backbone;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientTickTimes {

    private static final Map<Runnable, Integer> M = new HashMap<>();

    public static void clientStaticTick(Minecraft mc) {
        List<Runnable> rml = new ArrayList<>();
        for (Map.Entry<Runnable, Integer> pr : M.entrySet()) {
            if (pr.getValue() > 0) {
                pr.setValue(pr.getValue()-1);
            } else {
                pr.getKey().run();
                rml.add(pr.getKey());
            }
        }
        rml.forEach(M::remove);
    }

    public static void schedule(Runnable runnable, int ticks) {
        M.put(runnable, ticks);
    }
}
