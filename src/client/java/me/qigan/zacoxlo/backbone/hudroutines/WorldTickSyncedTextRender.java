package me.qigan.zacoxlo.backbone.hudroutines;

import me.qigan.zacoxlo.backbone.Hud;
import me.qigan.zacoxlo.crp.CompactTextComponent;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

public abstract class WorldTickSyncedTextRender implements Hud.LegacyRoutine {

    public record Element(int x, int y, CompactTextComponent component, int color) { }

    protected final List<Element> texts = new ArrayList<>();
    private List<Element> elements = new ArrayList<>();

    @Override
    public void drawTick(GuiGraphics ctx, DeltaTracker tick) {
        elements.forEach(txt -> {
            if (txt.component.centered()) ctx.drawCenteredString(txt.component.font() == null ? Minecraft.getInstance().font : txt.component.font(),
                    txt.component.component(), txt.x, txt.y, txt.color);
            else ctx.drawString(txt.component.font() == null ? Minecraft.getInstance().font : txt.component.font(),
                    txt.component.component(), txt.x, txt.y, txt.color);
        });
    }

    // This is not a bullshit code. It is a smart decision to fix potential blinks on asynced routines.
    public void update(WorldRenderContext ctx) {
        List<Element> elep = elements;
        this.elements = new ArrayList<>(texts);
        elep.clear(); texts.clear();
    }
}
