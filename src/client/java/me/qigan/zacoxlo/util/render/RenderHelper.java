package me.qigan.zacoxlo.util.render;

import me.qigan.zacoxlo.util.UnsortedUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class RenderHelper {

    public static void autoBox3D(Drawer drawer, Entity entity, int color) {
        Vector3f size = UnsortedUtils.getBoundingFromEntity(entity);
        RenderHelper.drawBox3D(drawer, entity.position().toVector3f().sub(size.x/2, 0, size.z/2), size , color, null);
    }

    public static void autoBox3D(Drawer drawer, Vector3f pos, Vector3f size, int color) {
        RenderHelper.drawBox3D(drawer, pos.sub(size.x/2, size.y/2, size.z/2), size, color, null);
    }
    
    public static void autoBox3D(Drawer drawer, BlockPos pos, int color, Vector3f normal) {
        RenderHelper.drawBox3D(drawer, new Vector3f(pos.getX(), pos.getY(), pos.getZ()), new Vector3f(1f, 1f, 1f), color, normal);
    }

    public static void drawBox3D(Drawer drawer, Vector3f cord, Vector3f size, int color, @Nullable Vector3f normal) {
        if (normal == null) normal = Dconsts.uniNormal();
        drawer.pos(cord.x, cord.y, cord.z).normalize(normal).color(color);
        drawer.pos(cord.x, cord.y+size.y, cord.z).normalize(normal).color(color);
        drawer.pos(cord.x+size.x, cord.y, cord.z).normalize(normal).color(color);
        drawer.pos(cord.x+size.x, cord.y+size.y, cord.z).normalize(normal).color(color);
        drawer.pos(cord.x, cord.y, cord.z+size.z).normalize(normal).color(color);
        drawer.pos(cord.x, cord.y+size.y, cord.z+size.z).normalize(normal).color(color);
        drawer.pos(cord.x+size.x, cord.y, cord.z+size.z).normalize(normal).color(color);
        drawer.pos(cord.x+size.x, cord.y+size.y, cord.z+size.z).normalize(normal).color(color);

        drawer.pos(cord.x, cord.y, cord.z).normalize(normal).color(color);
        drawer.pos(cord.x, cord.y, cord.z+size.z).normalize(normal).color(color);
        drawer.pos(cord.x+size.x, cord.y, cord.z).normalize(normal).color(color);
        drawer.pos(cord.x+size.x, cord.y, cord.z+size.z).normalize(normal).color(color);
        drawer.pos(cord.x, cord.y+size.y, cord.z).normalize(normal).color(color);
        drawer.pos(cord.x, cord.y+size.y, cord.z+size.z).normalize(normal).color(color);
        drawer.pos(cord.x+size.x, cord.y+size.y, cord.z).normalize(normal).color(color);
        drawer.pos(cord.x+size.x, cord.y+size.y, cord.z+size.z).normalize(normal).color(color);

        drawer.pos(cord.x, cord.y, cord.z).normalize(normal).color(color);
        drawer.pos(cord.x+size.x, cord.y, cord.z).normalize(normal).color(color);
        drawer.pos(cord.x, cord.y+size.y, cord.z).normalize(normal).color(color);
        drawer.pos(cord.x+size.x, cord.y+size.y, cord.z).normalize(normal).color(color);
        drawer.pos(cord.x, cord.y, cord.z+size.z).normalize(normal).color(color);
        drawer.pos(cord.x+size.x, cord.y, cord.z+size.z).normalize(normal).color(color);
        drawer.pos(cord.x, cord.y+size.y, cord.z+size.z).normalize(normal).color(color);
        drawer.pos(cord.x+size.x, cord.y+size.y, cord.z+size.z).normalize(normal).color(color);
    }
}
