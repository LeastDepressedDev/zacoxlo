package me.qigan.zacoxlo.backbone;

import me.qigan.zacoxlo.util.render.Dconsts;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector2f;

// TODO: Unfinished SNIPPET, back when have working brain to understand a single shiiii here
public class RealRotationController {

    public static class Target {
        public Float yaw;
        public Float pitch;

        public float accelerator;
        public float decelerator;

        public Target(float ac, float dc) {
            this.accelerator = ac;
            this.decelerator = dc;
        }

        public Target point(Vector2f vec2f) {
            //noinspection SuspiciousNameCombination
            return this.point(vec2f.x, vec2f.y);
        }

        public Target point(float y, float p) {
            this.yaw = Mth.wrapDegrees(y);
            this.pitch = Mth.wrapDegrees(p);
            return this;
        }
    }

    protected static Vector2f vel = new Vector2f(0, 0);

    public static long tickleft = 0;

    private static long TDEL = 50;
    private static long lt = 0;

    public static void init() {
        FirstRoutine.addRoutine(() -> {
            if (System.currentTimeMillis() < lt+TDEL) return;
            if (Minecraft.getInstance().player == null) return;
            if (tickleft > 0) {

            }
            tickleft--;
        });
    }

    public static void setTDEL(long tdel) {TDEL = tdel;}
    private static Vector2f currentRotationPosition() {
        Camera camera = Dconsts.getCamera();
        return new Vector2f(Mth.wrapDegrees(camera.getYRot()), camera.getXRot());
    }
}
