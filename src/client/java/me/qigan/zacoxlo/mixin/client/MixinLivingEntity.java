package me.qigan.zacoxlo.mixin.client;

import me.qigan.zacoxlo.Zacoxlo;
import me.qigan.zacoxlo.cfg.Module;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    @Shadow
    public boolean swinging;

    @Shadow
    public int swingTime;

    @Shadow
    public float attackAnim;

    @Shadow
    public abstract boolean hasEffect(Holder<MobEffect> holder);

    @Shadow
    public abstract MobEffectInstance getEffect(Holder<MobEffect> holder);
    

    @Inject(method = "updateSwingTime", at = @At("HEAD"), cancellable = true)
    protected void updateSwingTime(CallbackInfo ci) {
        int i = this.getCurrentSwingDuration();
        if (Zacoxlo.MAIN_CFG.getBoolVal("hand_render"))
            i = Math.round((float) i / Module.rtCfg.get("hand_render").get("Swing animation speed").getAsFloat());
        if (this.swinging) {
            this.swingTime++;
            if (this.swingTime >= i) {
                this.swingTime = 0;
                this.swinging = false;
            }
        } else {
            this.swingTime = 0;
        }

        this.attackAnim = (float)this.swingTime / i;
        ci.cancel();
    }

    @Unique
    private int getCurrentSwingDuration() {
        if (MobEffectUtil.hasDigSpeed((LivingEntity) (Object) this)) {
            return 6 - (1 + MobEffectUtil.getDigSpeedAmplification((LivingEntity) (Object) this));
        } else {
            return this.hasEffect(MobEffects.MINING_FATIGUE) ? 6 + (1 + this.getEffect(MobEffects.MINING_FATIGUE).getAmplifier()) * 2 : 6;
        }
    }
}
