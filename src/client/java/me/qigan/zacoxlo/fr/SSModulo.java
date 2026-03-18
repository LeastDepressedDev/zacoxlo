package me.qigan.zacoxlo.fr;

import me.qigan.zacoxlo.cfg.Module;
import me.qigan.zacoxlo.util.render.Dconsts;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.entity.player.Player;

public class SSModulo extends Module {
    @Override
    public String id() {
        return "reich";
    }

    @Override
    public String description() {
        return "Ss utils";
    }

    @Override
    public void onRegister() {
//        WorldRenderEvents.END_MAIN.register((ctx) -> {
//            Camera cam = Dconsts.getCamera();
//            ctx.matrices().translate(cam.position().multiply(-1f, -1f, -1f));
//            Font.GlyphVisitor df = Font.GlyphVisitor.forMultiBufferSource(ctx.consumers(), ctx.matrices().last().pose(), Font.DisplayMode.NORMAL, 2);
//            Minecraft.getInstance().font.prepareText("dasdasdasd", 0, 0, 0xFFFFFF, true, 5).visit(df);
//        });
    }
}
