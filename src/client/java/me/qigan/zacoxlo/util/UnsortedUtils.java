package me.qigan.zacoxlo.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

public class UnsortedUtils {
    public static AABB getRadiusAABB(double r) {
        Vec3 vec3 = Minecraft.getInstance().player.position();
        return new AABB(vec3.x-r, vec3.y-r, vec3.z-r, vec3.x+r, vec3.y+r, vec3.z+r);
    }

    public static Vector3f getBoundingFromEntity(Entity entity) {
        return new Vector3f((float) entity.getBoundingBox().getXsize(), (float) entity.getBoundingBox().getYsize(), (float) entity.getBoundingBox().getZsize());
    }

    public static List<ArmorStand> getArmorStands(Entity entity) {
        return getArmorStands(entity.level(), entity.getBoundingBox());
    }

    public static List<ArmorStand> getArmorStands(Level world, AABB box) {
        return world.getEntitiesOfClass(ArmorStand.class, box.inflate(0, 2, 0), EntitySelector.ENTITY_NOT_BEING_RIDDEN);
    }
}
