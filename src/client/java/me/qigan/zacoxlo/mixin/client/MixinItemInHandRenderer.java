package me.qigan.zacoxlo.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class MixinItemInHandRenderer {

    @Inject(method = "renderArmWithItem", at = @At("HEAD"))
    public void beginAWT(AbstractClientPlayer abstractClientPlayer, float f, float g, InteractionHand interactionHand, float h, ItemStack itemStack, float i, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int j, CallbackInfo ci) {
        poseStack.pushPose();
        // Hand moving and rotating
        //poseStack.translate(0f, 2f, 0f);
    }

    @Inject(method = "renderArmWithItem", at = @At("TAIL"))
    public void endAWT(AbstractClientPlayer abstractClientPlayer, float f, float g, InteractionHand interactionHand, float h, ItemStack itemStack, float i, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int j, CallbackInfo ci) {
        poseStack.popPose();
    }





    @Inject(method = "applyItemArmTransform", at = @At("HEAD"))
    public void aiat(PoseStack poseStack, HumanoidArm humanoidArm, float f, CallbackInfo ci) {
        // Hit and action effect modification
        //poseStack.scale(4f, 4f, 4f);
    }





    @Inject(method = "renderItem", at = @At("HEAD"))
    public void beginRT(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, CallbackInfo ci) {
        poseStack.pushPose();
        // Item size
        //poseStack.scale(0.5f, 0.5f, 0.5f);
    }

    @Inject(method = "renderItem", at = @At("TAIL"))
    public void endRT(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, CallbackInfo ci) {
        poseStack.popPose();
    }






//    @Inject(method = "swingArm", at = @At("HEAD"), cancellable = true)
//    public void swingReplace(float f, float g, PoseStack poseStack, int i, HumanoidArm humanoidArm, CallbackInfo ci) {
//        ci.cancel();
//    }

    /**
     * @author LDD
     * @reason 1.8.9 Comeback is requested
     */
    @Overwrite
    private void swingArm(float f, float g, PoseStack poseStack, int i, HumanoidArm humanoidArm) {
        float h = -0.4F * Mth.sin(Mth.sqrt(f) * (float) Math.PI);
        float j = 0.2F * Mth.sin(Mth.sqrt(f) * (float) (Math.PI * 2));
        float k = -0.2F * Mth.sin(f * (float) Math.PI);
        poseStack.translate(i * h, j, k);
        applyItemArmTransform(poseStack, humanoidArm, g);
        applyItemArmAttackTransform(poseStack, humanoidArm, f);
    }

    @Unique
    private static void applyItemArmTransform(PoseStack poseStack, HumanoidArm humanoidArm, float f) {
        int i = humanoidArm == HumanoidArm.RIGHT ? 1 : -1;
        poseStack.translate(i * 0.56F, -0.52F + f * -0.6F, -0.72F);
    }

    @Unique
    private static void applyItemArmAttackTransform(PoseStack poseStack, HumanoidArm humanoidArm, float f) {
        int i = humanoidArm == HumanoidArm.RIGHT ? 1 : -1;
        float g = Mth.sin(f * f * (float) Math.PI);
        poseStack.mulPose(Axis.YP.rotationDegrees(i * (45.0F + g * -20.0F)));
        float h = Mth.sin(Mth.sqrt(f) * (float) Math.PI);
        poseStack.mulPose(Axis.ZP.rotationDegrees(i * h * -20.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(h * -80.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(i * -45.0F));
    }
}
