package me.qigan.zacoxlo.fr;

import me.qigan.zacoxlo.backbone.AnoncHud;
import me.qigan.zacoxlo.cfg.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.AABB;

public class CompleteNotifier extends Module {

    //62 125 36
    //64 130 34
    public static final AABB I4_BOX = new AABB(62, 125, 36, 64, 130, 34);

    @Override
    public String id() {
        return "c_anonc";
    }

    @Override
    public String description() {
        return "Spams you that you have done your shit";
    }

    @Override
    public void onRegister() {
        ClientTickEvents.END_CLIENT_TICK.register((mc) -> {
            if (!isEnabled()) return;
            if (Minecraft.getInstance().player == null) return;
            if (I4_BOX.contains(Minecraft.getInstance().player.position())) {
                Minecraft.getInstance().level.getEntitiesOfClass(ArmorStand.class, I4_BOX).forEach(ent -> {
                    if (ent.getName().getString().contains("Active")) {
                        AnoncHud.anonc("I4 DONE", 5, 0xFF00DD00);
                    }
                });
            }
        });
    }
}
