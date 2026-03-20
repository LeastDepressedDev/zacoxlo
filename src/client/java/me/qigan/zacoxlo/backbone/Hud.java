package me.qigan.zacoxlo.backbone;

import me.qigan.zacoxlo.crp.AddressedData;
import me.qigan.zacoxlo.util.UnsortedUtils;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

public class Hud {

    public interface LegacyRoutine {void drawTick(GuiGraphics ctx, DeltaTracker tick);}

    public static final PriorityQueue<AddressedData<Integer, LegacyRoutine>> LEGACY_ROUTINES
            = new PriorityQueue<>(Comparator.comparing(k -> -k.getNamespace()));

    @SuppressWarnings("deprecation")
    public static void init() {
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            for (AddressedData<Integer, LegacyRoutine> legacyRoutine : LEGACY_ROUTINES) {
                legacyRoutine.getObject().drawTick(drawContext, tickCounter);
            }
        });
    }

    public static void newLegacyRoutine(LegacyRoutine routine) {
        LEGACY_ROUTINES.add(new AddressedData<>(0, routine));
    }

    public static void newLegacyRoutine(int prio, LegacyRoutine routine) {
        LEGACY_ROUTINES.add(new AddressedData<>(prio, routine));
    }
}
