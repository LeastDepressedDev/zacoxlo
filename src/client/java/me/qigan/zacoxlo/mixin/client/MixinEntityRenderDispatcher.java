package me.qigan.zacoxlo.mixin.client;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import me.qigan.zacoxlo.Zacoxlo;
import me.qigan.zacoxlo.cfg.Module;
import me.qigan.zacoxlo.fr.CuteCreatures;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Mixin(EntityRenderDispatcher.class)
public class MixinEntityRenderDispatcher {

    @Unique
    private static Set<EntityType> ALLOWED_TYPES = new HashSet<>(Arrays.asList(
            EntityType.PLAYER
    ));

    @Inject(method = "submit", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;submit(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V"))
    public <S extends EntityRenderState> void mk(S entityRenderState, CameraRenderState cameraRenderState, double d, double e, double f, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CallbackInfo ci) {
        if (Zacoxlo.MAIN_CFG.getBoolVal("mipis")) {
            if (ALLOWED_TYPES.contains(entityRenderState.entityType)) {
                JsonObject cfg = Module.rtCfg.get("mipis");
                poseStack.scale(cfg.get("Scale X").getAsFloat(), cfg.get("Scale X").getAsFloat(), cfg.get("Scale X").getAsFloat());
            }
        }
    }
}
