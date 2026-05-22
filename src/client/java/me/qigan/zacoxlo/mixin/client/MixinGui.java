package me.qigan.zacoxlo.mixin.client;

import com.google.common.collect.Ordering;
import com.google.gson.JsonObject;
import me.qigan.zacoxlo.Zacoxlo;
import me.qigan.zacoxlo.fr.HudModify;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(Gui.class)
public abstract class MixinGui {
    @Inject(method = "renderEffects", at = @At("HEAD"))
    private void renderEffectsOvr(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
//        if (Zacoxlo.MAIN_CFG.getBoolVal("hud_modify")) {
//            JsonObject cfg = HudModify.rtCfg.get("hud_modify");
//            if (cfg.get("Hide effects").getAsBoolean()) ci.cancel();
//        }
    }

    @Final
    @Shadow
    private static ResourceLocation EFFECT_BACKGROUND_AMBIENT_SPRITE;
    @Final
    @Shadow
    private static ResourceLocation EFFECT_BACKGROUND_SPRITE;
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    public static ResourceLocation getMobEffectSprite(Holder<MobEffect> holder) {
        return null;
    }
}
