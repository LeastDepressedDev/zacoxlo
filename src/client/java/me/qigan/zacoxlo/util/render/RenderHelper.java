package me.qigan.zacoxlo.util.render;

import me.qigan.zacoxlo.util.UnsortedUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class RenderHelper {

    public static void autoBox3D(Drawer drawer, Entity entity, int color) {
        Vector3f size = UnsortedUtils.getBoundingFromEntity(entity);
        RenderHelper.drawBox3D(drawer, entity.position().toVector3f().sub(size.x/2, 0, size.z/2), size , color);
    }

    public static void autoBox3D(Drawer drawer, Vector3f pos, Vector3f size, int color) {
        RenderHelper.drawBox3D(drawer, pos.sub(size.x/2, size.y/2, size.z/2), size, color);
    }

    public static void drawBox3D(Drawer drawer, Vector3f cord, Vector3f size, int color) {
        drawer.pos(cord.x, cord.y, cord.z).uniNormalize().color(color);
        drawer.pos(cord.x, cord.y+size.y, cord.z).uniNormalize().color(color);
        drawer.pos(cord.x+size.x, cord.y, cord.z).uniNormalize().color(color);
        drawer.pos(cord.x+size.x, cord.y+size.y, cord.z).uniNormalize().color(color);
        drawer.pos(cord.x, cord.y, cord.z+size.z).uniNormalize().color(color);
        drawer.pos(cord.x, cord.y+size.y, cord.z+size.z).uniNormalize().color(color);
        drawer.pos(cord.x+size.x, cord.y, cord.z+size.z).uniNormalize().color(color);
        drawer.pos(cord.x+size.x, cord.y+size.y, cord.z+size.z).uniNormalize().color(color);

        drawer.pos(cord.x, cord.y, cord.z).uniNormalize().color(color);
        drawer.pos(cord.x, cord.y, cord.z+size.z).uniNormalize().color(color);
        drawer.pos(cord.x+size.x, cord.y, cord.z).uniNormalize().color(color);
        drawer.pos(cord.x+size.x, cord.y, cord.z+size.z).uniNormalize().color(color);
        drawer.pos(cord.x, cord.y+size.y, cord.z).uniNormalize().color(color);
        drawer.pos(cord.x, cord.y+size.y, cord.z+size.z).uniNormalize().color(color);
        drawer.pos(cord.x+size.x, cord.y+size.y, cord.z).uniNormalize().color(color);
        drawer.pos(cord.x+size.x, cord.y+size.y, cord.z+size.z).uniNormalize().color(color);

        drawer.pos(cord.x, cord.y, cord.z).uniNormalize().color(color);
        drawer.pos(cord.x+size.x, cord.y, cord.z).uniNormalize().color(color);
        drawer.pos(cord.x, cord.y+size.y, cord.z).uniNormalize().color(color);
        drawer.pos(cord.x+size.x, cord.y+size.y, cord.z).uniNormalize().color(color);
        drawer.pos(cord.x, cord.y, cord.z+size.z).uniNormalize().color(color);
        drawer.pos(cord.x+size.x, cord.y, cord.z+size.z).uniNormalize().color(color);
        drawer.pos(cord.x, cord.y+size.y, cord.z+size.z).uniNormalize().color(color);
        drawer.pos(cord.x+size.x, cord.y+size.y, cord.z+size.z).uniNormalize().color(color);
    }
}
