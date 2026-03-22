package me.qigan.zacoxlo.fr;

import me.qigan.zacoxlo.backbone.Hud;
import me.qigan.zacoxlo.backbone.hudroutines.InworldTextRenderer;
import me.qigan.zacoxlo.cfg.Module;
import me.qigan.zacoxlo.crp.CompactTextComponent;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;

public class SSModulo extends Module {

    InworldTextRenderer renderer = new InworldTextRenderer() {
        @Override
        public void update(WorldRenderContext ctx) {
            drawTextComponent(new CompactTextComponent(Component.literal("Nigga"), null, true,
                            new Vector2f(1f, 1f)),
                    new Vec3(0,0,0), 0xFFFFFFFF);
            super.update(ctx);
        }
    };

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
//        WorldRenderEvents.END_MAIN.register(renderer::update);
//        Hud.newLegacyRoutine(renderer);
    }
}
