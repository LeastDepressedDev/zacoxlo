package me.qigan.zacoxlo.crp;

import com.mojang.blaze3d.platform.InputConstants;
import me.qigan.zacoxlo.cfg.Module;
import me.qigan.zacoxlo.util.UnsortedUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

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
        KeyMapping mp = new KeyMapping("kb.ss", InputConstants.KEY_R, KeyMapping.Category.MISC);
        ClientTickEvents.END_CLIENT_TICK.register((mc) -> {
            if (!isEnabled()) return;
            if (mp.isDown()) UnsortedUtils.sendQuickLog("ADAD");
        });
    }
}
