package me.qigan.zacoxlo.backbone;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.joml.Vector2f;

public class AnoncHud {

    private static int ticks = 0;
    private static String anonc = "";
    private static int rgba = 0xFFFFFFFF;
    private static Vector2f oft;
    private static Vector2f specs;

    private static Vector2f scale = new Vector2f(5f, 4.5f);
    private static SoundEvent s_e = SoundEvents.EXPERIENCE_ORB_PICKUP;

    public static Hud.LegacyRoutine render_rt = (ctx, tick) -> {
        if (ticks <= 0) return;
        ctx.pose().pushMatrix();
        ctx.pose().translate(Minecraft.getInstance().getWindow().getGuiScaledWidth()/2f+oft.x,
                Minecraft.getInstance().getWindow().getGuiScaledHeight()/2f-Minecraft.getInstance().font.lineHeight/2f*scale.y+oft.y).scale(scale);
        ctx.drawCenteredString(Minecraft.getInstance().font, anonc, 0, 0, rgba);
        ctx.pose().popMatrix();
    };

    public static void anonc(String str, int t, int rgba_color, Vector2f offset, SoundEvent soundEvent, Vector2f specs_vec) {
        anonc = str;
        ticks = t;
        rgba = rgba_color;
        oft = offset;
        s_e = soundEvent;
        specs= specs_vec;
    }

    public static void anonc(String str, int t, int rgba_color) {
        anonc(str, t, rgba_color, new Vector2f(0,0), SoundEvents.EXPERIENCE_ORB_PICKUP, new Vector2f(2f, 2f));
    }

    public static void tick(Minecraft mc) {
        if (ticks > 0) {
            Minecraft.getInstance().player.playSound(s_e, specs.x, specs.y);
            ticks--;
        }
    }
}
