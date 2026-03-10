package me.qigan.zacoxlo.fr;

import me.qigan.zacoxlo.cfg.Module;
import me.qigan.zacoxlo.util.UnsortedUtils;
import me.qigan.zacoxlo.util.render.Drawer;
import me.qigan.zacoxlo.util.render.RSect;
import me.qigan.zacoxlo.util.render.RenderHelper;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector3f;

import java.util.List;

public class StarredMobs extends Module {

    private String starredRegex = "^.*✯ .*\\d{1,3}(?:,\\d{3})*(?:\\.\\d+)?(?:[kM])?❤$";

    @Override
    public String id() {
        return "smobs";
    }

    @Override
    public String description() {
        return "Finally starred mobs";
    }

    @Override
    public void onRegister() {
        WorldRenderEvents.END_MAIN.register((ctx) -> {
            if (!isEnabled()) return;
            if (Minecraft.getInstance().level == null) return;
            Drawer drawer = new Drawer(RSect.rtypesf.ESP_LINE).withContext(ctx).begin();
            drawer.globalize().line(3f).capture();

//            Minecraft.getInstance().level.entitiesForRendering().forEach((ent) -> {
//                if (!ent.isAlive() || ent.distanceTo(Minecraft.getInstance().player) > 50) return;
//                if (isStarred(ent)) {
//                    RenderHelper.autoBox3D(drawer, ent, 0xFF00FFFF);
//                }
//            });

            Minecraft.getInstance().level.getEntitiesOfClass(ArmorStand.class, UnsortedUtils.getRadiusAABB(50)).forEach((ent) -> {
                if (ent.getName().getString().contains("✯") && ent.getName().getString().contains("❤")) {
                    RenderHelper.autoBox3D(drawer, ent.position().toVector3f().add(0, -1.1f, 0),
                            new Vector3f(0.7f, 2f, 0.7f), 0xFF00FFFF);
                }
            });
            Minecraft.getInstance().level.getEntitiesOfClass(Player.class, UnsortedUtils.getRadiusAABB(50)).forEach((ent) -> {
                if (ent.getName().getString().contains("Shadow Assassin"))
                    RenderHelper.autoBox3D(drawer, ent, 0xFFFFFF00);
            });

            drawer.end();
        });
    }


    public static boolean isStarred(Entity entity) {
        List<ArmorStand> armorStands = UnsortedUtils.getArmorStands(entity);
        return !armorStands.isEmpty() && armorStands.getFirst().getName().getString().contains("✯");
    }

}
