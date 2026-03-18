package me.qigan.zacoxlo.backbone;

import java.util.ArrayList;
import java.util.List;

public class FirstRoutine {
    public static final List<Runnable> ROUTINES = new ArrayList<>();

    public static void addRoutine(Runnable runnable) {
        ROUTINES.add(runnable);
    }

    public static void procRoutines() {
        ROUTINES.forEach(Runnable::run);
    }
}
