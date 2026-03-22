package me.qigan.zacoxlo.mixin.client;

import me.qigan.zacoxlo.backbone.FirstRoutine;
import me.qigan.zacoxlo.backbone.SmartFirstRoutines;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;handleDelayedCrash()V"), method = "run")
	private void fRunTick(CallbackInfo info) {
        FirstRoutine.procRoutines();
        SmartFirstRoutines.procRoutines();
	}
}