package me.qigan.zacoxlo.backbone.hudroutines;

import me.qigan.zacoxlo.crp.CompactTextComponent;

import me.qigan.zacoxlo.util.VectorizationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class InworldTextRenderer extends WorldTickSyncedTextRender {
    public final void drawTextComponent(CompactTextComponent component, Vec3 pos, int color, Vector2f subscale) {
        Vector2f nPos = VectorizationUtils.vec3ddto2df(pos);
        if (nPos != null) texts.add(new Element((int) nPos.x, (int) nPos.y, component, color, subscale));
    }

    public final void drawTextComponent(CompactTextComponent component, Vec3 pos, int color) {
        this.drawTextComponent(component, pos, color, calculateDistancedScale(pos));
    }

    // Super robusty text size processing TODO: Adjust static multipliers to match the perfection
    private static Vector2f calculateDistancedScale(Vec3 pos) {
        Vector3f vec3f = Minecraft.getInstance().player.position().toVector3f().add(0, Minecraft.getInstance().player.getEyeHeight(), 0);
        return new Vector2f(1f, 1f).div(-0.3666f+(float) Math.pow(vec3f.distance(pos.toVector3f()), 0.3));
    }
}
