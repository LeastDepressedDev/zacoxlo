package me.qigan.zacoxlo.util.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.RenderType;
import org.joml.Vector3f;

public class Drawer {

    private final RenderType rtl;
    public PoseStack stack = null;
    public VertexConsumer buf;
    private PoseStack.Pose lpose;

    public Drawer(RenderType renderType) {
        this.rtl = renderType;
    }

    public Drawer withContext(WorldRenderContext context) {
        this.stack = context.matrices();
        this.lpose = stack.last();
        return this;
    }

    public Drawer capture() {
        this.lpose = this.stack.last();
        return this;
    }


    public VertexConsumer vertex(float x, float y, float z) {
        return this.buf.addVertex(this.lpose, x, y, z);
    }

    public VertexConsumer vertex(Vector3f vec) {
        return this.vertex(vec.x, vec.y, vec.z);
    }



    public Drawer pos(float x, float y, float z) {
        this.buf.addVertex(this.lpose, x, y, z);
        return this;
    }

    public Drawer pos(Vector3f vec) {
        this.vertex(vec.x, vec.y, vec.z);
        return this;
    }

    public Drawer normalize(float x, float y, float z) {
        this.buf.setNormal(x, y, z);
        return this;
    }

    public Drawer normalize(Vector3f vec) {
        return this.normalize(vec.x, vec.y, vec.z);
    }

    public Drawer uniNormalize() {
        Vector3f nm = Dconsts.uniNormal();
        return this.normalize(nm);
    }

    public Drawer color(float r, float g, float b, float a) {
        this.buf.setColor(r, g, b, a);
        return this;
    }

    public Drawer color(int rgba) {
        this.buf.setColor(rgba);
        return this;
    }




    public final Drawer globalize() {
        Camera cam = Dconsts.getCamera();
        this.stack.translate(cam.position().multiply(-1f, -1f, -1f));
        return this;
    }






    public Drawer incompose() {
        this.stack.pushPose();
        return this;
    }

    public Drawer outcompose() {
        this.stack.popPose();
        return this;
    }

    public Drawer begin() {
        if (this.stack == null) throw new DrawerBeginException("PoseStack is not initialised. Probably no context given.");
        this.buf = Dconsts.getVCP().getBuffer(rtl);
        this.stack.pushPose();
        return this;
    }

    public void end() {
        this.stack.popPose();
    }

    public RenderType getType() {
        return rtl;
    }
}
