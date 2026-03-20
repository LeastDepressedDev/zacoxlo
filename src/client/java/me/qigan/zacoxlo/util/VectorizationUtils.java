package me.qigan.zacoxlo.util;

import me.qigan.zacoxlo.util.render.Dconsts;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class VectorizationUtils {

    public static float actualFov = 0.0f;

    public static @Nullable Vector2f vec3ddto2df(Vec3 pos) {
        Camera camera = Dconsts.getCamera();
        Minecraft mc = Minecraft.getInstance();

        Vec3 camPos = camera.position();
        double relX = pos.x - camPos.x;
        double relY = pos.y - camPos.y;
        double relZ = pos.z - camPos.z;

        float screenWidth = (float) mc.getWindow().getGuiScaledWidth();
        float screenHeight = (float) mc.getWindow().getGuiScaledHeight();

        Vector3f rotated = new Vector3f(-(float)relX, (float)relY, (float)relZ);
        rotated.rotateY((float) -Math.toRadians(camera.getYRot()));
        rotated.rotateX((float) -Math.toRadians(camera.getXRot()));

        float fovFactor = (float) (screenHeight / (2 * Math.tan(Math.toRadians(actualFov) / 2.0)));

        // Inner not in screen check
        return rotated.z <= 0 ? null : new Vector2f(
                (screenWidth / 2.0f + rotated.x * fovFactor / rotated.z),
                (screenHeight / 2.0f - rotated.y * fovFactor / rotated.z)
        );
    }
}
