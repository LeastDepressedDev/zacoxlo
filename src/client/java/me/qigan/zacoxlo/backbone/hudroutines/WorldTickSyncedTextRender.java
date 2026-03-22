package me.qigan.zacoxlo.backbone.hudroutines;

import me.qigan.zacoxlo.backbone.Hud;
import me.qigan.zacoxlo.crp.CompactTextComponent;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public abstract class WorldTickSyncedTextRender implements Hud.LegacyRoutine {

    public record Element(int x, int y, CompactTextComponent component, int color, Vector2f subscale) { }

    protected final List<Element> texts = new ArrayList<>();
    private List<Element> elements = new ArrayList<>();

    @Override
    public void drawTick(GuiGraphics ctx, DeltaTracker tick) {
        elements.forEach(txt -> {
            ctx.pose().pushMatrix();
            ctx.pose().translate(txt.x, txt.y);
            ctx.pose().scale(txt.component.scale());
            ctx.pose().scale(txt.subscale);
            if (txt.component.centered()) ctx.drawCenteredString(txt.component.font() == null ? Minecraft.getInstance().font : txt.component.font(),
                    txt.component.component(), 0, 0, txt.color);
            else ctx.drawString(txt.component.font() == null ? Minecraft.getInstance().font : txt.component.font(),
                    txt.component.component(), 0, 0, txt.color);

            ctx.pose().popMatrix();
        });
    }

    // This is not a bullshit code. It is a smart decision to fix potential blinks on asynced routines.
    public void update(WorldRenderContext ctx) {
        List<Element> elep = elements;
        this.elements = new ArrayList<>(texts);
        elep.clear(); texts.clear();
    }
}
