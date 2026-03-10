package me.qigan.zacoxlo.util.render;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class Dconsts {

    public static Vector3f getViewVector() {
        return getCamera().getLookVector();
    }

    public static Camera getCamera() {
        return Minecraft.getInstance().gameRenderer.getMainCamera();
    }

    public static Vec3 reverseCam() {
        return getCamera().position().reverse();
    }

    public static Vector3f uniNormal() {
        Vector3f nm = new Vector3f(0f, 1f, 0f)
                .rotateAxis(-Dconsts.getViewVector().y, 0f, 1f, 0f)
                .rotateAxis(Dconsts.getViewVector().x, 1f, 0f, 0f);
        return nm;
    }

//    public static Vector3f getNormalViewTo(Vector3f cord) {
//        double dist = Minecraft.getInstance().player.distanceToSqr(cord.x, cord.y, cord.z);
//        Vector3f nm = Dconsts.getViewVector().mul(-1f).mul((float) Math.sqrt(dist));
//        return nm;
//    }
//
//    public static Vector3f getNormalViewTo(float x, float y, float z) {
//        return getNormalViewTo(new Vector3f(x, y, z));
//    }

    public static MultiBufferSource.BufferSource getVCP()
    {
        return Minecraft.getInstance().renderBuffers().bufferSource();
    }
}
