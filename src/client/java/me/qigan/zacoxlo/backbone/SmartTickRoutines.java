package me.qigan.zacoxlo.backbone;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SmartTickRoutines {

    public static abstract class Routine implements Runnable {
        public void unplug() {
            queueRemoval(this);
        }
    }

    public static Set<Routine> routines = new HashSet<>();
    private static List<Routine> rmque = new ArrayList<>();

    public static void newRoutine(Routine routine) {
        routines.add(routine);
    }

    public static void tick(Minecraft mc) {
        routines.forEach(Runnable::run);
        rmque.forEach(r -> routines.remove(r));
    }
    public static void queueRemoval(Routine routine) {
        rmque.add(routine);
    }
}
