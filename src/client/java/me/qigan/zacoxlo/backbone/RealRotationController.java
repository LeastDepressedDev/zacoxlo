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

        public float a;
        public float dm;
        public float rough;

        public float dead_zone = 0f;
        public float minima = 0f;

        /**
         *
         * @param ac Acceleration constant
         * @param dc Resistance of surface (0-1)
         * @param dm Distance scale float value
         */
        public Target(float ac, float dc, float dm) {
            this.a = ac;
            this.rough = Math.min(Math.max(1f-dc, 0f), 1f);
            this.dm = dm;
        }

        public Target dead(float f1) {
            this.dead_zone = f1;
            return this;
        }

        public Target minima(float f1) {
            this.minima = f1;
            return this;
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
    public static Target target = null;

    public static void init() {
        FirstRoutine.addRoutine(() -> {
            if (System.currentTimeMillis() < lt+TDEL) return;
            if (Minecraft.getInstance().player == null) return;
            if (tickleft > 0 && target != null) {
                Player player = Minecraft.getInstance().player;
                vel.mul(target.rough);
                Float f = target.yaw == null ? null : Mth.wrapDegrees(target.yaw)-Mth.wrapDegrees(player.getYRot());
                float fs = f == null ? 1 : Math.signum(f);
                Vector2f deltas = new Vector2f(
                        target.yaw == null ? 0 : Math.abs(f) > 180 ? fs*(360-Math.abs(f)) : f,
                        target.pitch == null ? 0 : target.pitch-player.getXRot()
                );
                float r = deltas.length();
                // Acceleration vector
                Vector2f a_vec = new Vector2f(deltas.x, deltas.y).div(r);
                Vector2f minima = new Vector2f(a_vec).mul(target.minima);
                a_vec.mul(target.a*(float) Math.pow(r, 2*target.dm));
                vel.add(a_vec);

                if (vel.length() < r-target.dead_zone) {
                    addRotation(minima.length() > vel.length() ? minima : vel);
                }
            }
            tickleft--;
        });
    }

    public static void setTDEL(long tdel) {TDEL = tdel;}
    private static Vector2f currentRotationPosition() {
        Camera camera = Dconsts.getCamera();
        return new Vector2f(Mth.wrapDegrees(camera.getYRot()), camera.getXRot());
    }

    public static void rotate(Target tgt, long ticks) {
        target = tgt;
        tickleft = ticks;
    }

    public static void addRotation(Vector2f vec) {
        Minecraft.getInstance().player.setYRot(Minecraft.getInstance().player.getYRot()+vec.x);
        Minecraft.getInstance().player.setXRot(Minecraft.getInstance().player.getXRot()+vec.y);
    }
}
