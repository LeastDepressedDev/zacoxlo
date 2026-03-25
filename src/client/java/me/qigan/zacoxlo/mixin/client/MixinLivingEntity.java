package me.qigan.zacoxlo.mixin.client;

import me.qigan.zacoxlo.Zacoxlo;
import me.qigan.zacoxlo.cfg.Module;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
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


    @Shadow
    public InteractionHand swingingArm;

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

    /**
     * @author LDD
     * @reason Very unneeded shit. I could just do getCurrentSwingDuration override but that would interfere the swing animation packet... One worded - meh
     */
    @Overwrite
    public void swing(InteractionHand interactionHand, boolean bl) {
        int swgd = this.getCurrentSwingDuration();
        if (!this.swinging || this.swingTime >= swgd / 2 || this.swingTime < 0) {
            if (!Zacoxlo.MAIN_CFG.getBoolVal("hand_render") || (Zacoxlo.MAIN_CFG.getBoolVal("hand_render") &&
                    !this.swinging || this.swingTime >= Math.round((float) swgd / Module.rtCfg.get("hand_render").get("Swing animation speed").getAsFloat()) / 2 || this.swingTime < 0
            )) {
                this.swingTime = -1;
                this.swinging = true;
                this.swingingArm = interactionHand;
            }
            if (((LivingEntity) (Object) this).level() instanceof ServerLevel) {
                ClientboundAnimatePacket clientboundAnimatePacket = new ClientboundAnimatePacket((LivingEntity) (Object) this, interactionHand == InteractionHand.MAIN_HAND ? 0 : 3);
                ServerChunkCache serverChunkCache = (ServerChunkCache) (((LivingEntity) (Object) this).level()).getChunkSource();
                if (bl) {
                    serverChunkCache.sendToTrackingPlayersAndSelf((LivingEntity) (Object) this, clientboundAnimatePacket);
                } else {
                    serverChunkCache.sendToTrackingPlayers((LivingEntity) (Object) this, clientboundAnimatePacket);
                }
            }
        }
    }
}
