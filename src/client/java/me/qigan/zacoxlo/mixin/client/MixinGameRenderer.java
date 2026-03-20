package me.qigan.zacoxlo.mixin.client;

import me.qigan.zacoxlo.util.VectorizationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    @Shadow
    private float fovModifier;

    @Inject(method = "tickFov", at = @At("TAIL"))
    private void tickFov(CallbackInfo ci) {
        VectorizationUtils.actualFov = Minecraft.getInstance().options.fov().get() * fovModifier;
    }
}
