package me.qigan.zacoxlo.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStackRenderState.class)
public class MixinItemStackRenderer {
//    @Inject(method = "submit", at = @At("TAIL"))
//    public void end(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, int j, int k, CallbackInfo ci) {
//        poseStack.popPose();
//    }
//
//    @Inject(method = "submit", at = @At("HEAD"))
//    public void begin(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, int j, int k, CallbackInfo ci) {
//        poseStack.pushPose();
//        poseStack.scale(0.2f, 0.2f, 0.2f);
//    }
}
