package me.qigan.zacoxlo.backbone.hudroutines;

import me.qigan.zacoxlo.crp.CompactTextComponent;

import me.qigan.zacoxlo.util.UnsortedUtils;
import me.qigan.zacoxlo.util.VectorizationUtils;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;

public abstract class InworldTextRenderer extends WorldTickSyncedTextRender {
    public final void drawTextComponent(CompactTextComponent component, Vec3 pos, int color) {
        Vector2f nPos = VectorizationUtils.vec3ddto2df(pos);
        if (nPos != null) texts.add(new Element((int) nPos.x, (int) nPos.y, component, color));
    }
}
