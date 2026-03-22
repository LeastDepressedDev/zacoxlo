package me.qigan.zacoxlo.backbone;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SmartFirstRoutines {

    public static abstract class Routine implements Runnable {
        public void unplug() {
            queueRemoval(this);
        }
    }

    public static Set<SmartFirstRoutines.Routine> routines = new HashSet<>();
    private static List<SmartFirstRoutines.Routine> rmque = new ArrayList<>();

    public static void newRoutine(SmartFirstRoutines.Routine routine) {
        routines.add(routine);
    }

    public static void procRoutines() {
        routines.forEach(Runnable::run);
        rmque.forEach(r -> routines.remove(r));
    }
    public static void queueRemoval(SmartFirstRoutines.Routine routine) {
        rmque.add(routine);
    }
}
