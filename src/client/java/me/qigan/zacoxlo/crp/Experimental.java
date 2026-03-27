package me.qigan.zacoxlo.crp;

import com.mojang.blaze3d.platform.InputConstants;
import me.qigan.zacoxlo.cfg.Module;
import me.qigan.zacoxlo.util.UnsortedUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

public class Experimental extends Module {
    @Override
    public String id() {
        return "exptl";
    }

    @Override
    public String description() {
        return "Experimental function alpha";
    }

    @Override
    public void onRegister() {
        ClientTickEvents.END_CLIENT_TICK.register((mc) -> {
            if (!isEnabled() || Minecraft.getInstance().player == null) return;
            UnsortedUtils.sendQuickLog(Mth.wrapDegrees(mc.player.getYRot())+" "+mc.player.getYRot());
        });
    }
}
