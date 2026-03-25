package me.qigan.zacoxlo.mixin.client;

import me.qigan.zacoxlo.Zacoxlo;
import me.qigan.zacoxlo.cfg.Module;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    /**
     * @author LDD
     * @reason Animation speed adjuster
     */
    @Inject(method = "getCurrentSwingDuration", at = @At("HEAD"), cancellable = true)
    protected void swing(CallbackInfoReturnable<Integer> cir) {
        LivingEntity living = (LivingEntity) ((Object) this);
        int duration = MobEffectUtil.hasDigSpeed(living) ?
                6 - (1 + MobEffectUtil.getDigSpeedAmplification(living)) :
                living.hasEffect(MobEffects.MINING_FATIGUE) ? 6 + (1 + living.getEffect(MobEffects.MINING_FATIGUE).getAmplifier()) * 2 : 6;
        if (Zacoxlo.MAIN_CFG.getBoolVal("hand_render"))
            duration = Math.round((float) duration / Module.rtCfg.get("hand_render").get("Swing animation speed").getAsFloat());
        cir.setReturnValue(Math.max(duration, 1));
        cir.cancel();
    }
}
