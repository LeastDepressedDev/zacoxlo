package me.qigan.zacoxlo.mixin.client;

import com.google.gson.JsonObject;
import me.qigan.zacoxlo.Zacoxlo;
import me.qigan.zacoxlo.fr.GuiModify;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class MixinGui {
    @Inject(method = "renderEffects", at = @At("HEAD"), cancellable = true)
    private void renderEffectsOvr(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (Zacoxlo.MAIN_CFG.getBoolVal("gui_modify")) {
            JsonObject cfg = GuiModify.rtCfg.get("gui_modify");
            if (cfg.get("Hide hud effects").getAsBoolean()) ci.cancel();
        }
    }
}
