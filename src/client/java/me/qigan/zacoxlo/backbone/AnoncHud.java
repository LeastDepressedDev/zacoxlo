package me.qigan.zacoxlo.backbone;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;

public class AnoncHud {

    private static int ticks = 0;
    private static String anonc = "";
    private static int rgba = 0xFFFFFFFF;

    public static Hud.LegacyRoutine render_rt = (ctx, tick) -> {
        if (ticks <= 0) return;
        ctx.pose().pushMatrix();
        ctx.pose().translate(Minecraft.getInstance().getWindow().getGuiScaledWidth()/2f, Minecraft.getInstance().getWindow().getGuiScaledHeight()/3.7f).scale(5f, 4.5f);
        ctx.drawCenteredString(Minecraft.getInstance().font, anonc, 0, 0, rgba);
        ctx.pose().popMatrix();
    };

    public static void anonc(String str, int t, int rgba_color) {
        anonc = str;
        ticks = t;
        rgba = rgba_color;
    }

    public static void tick(Minecraft mc) {
        if (ticks > 0) {
            Minecraft.getInstance().player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 2f, 2f);
            ticks--;
        }
    }
}
