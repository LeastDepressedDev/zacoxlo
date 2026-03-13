package me.qigan.zacoxlo.fr;

import me.qigan.zacoxlo.Zacoxlo;
import me.qigan.zacoxlo.cfg.Module;
import me.qigan.zacoxlo.crp.AddressedData;
import me.qigan.zacoxlo.crp.SetsData;
import me.qigan.zacoxlo.crp.ValType;
import me.qigan.zacoxlo.util.render.Drawer;
import me.qigan.zacoxlo.util.render.RSect;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Trail extends Module {

    private static List<AddressedData<Vector3f, Color>> pos_l = new ArrayList<>();
    private static float c_angle = 0;

    @Override
    public String id() {
        return "dtrail";
    }

    @Override
    public String description() {
        return "Idk why did i add this...";
    }

    @Override
    public void onRegister() {
        ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register((mc, level) -> {
            pos_l.clear();
        });
        ClientTickEvents.END_CLIENT_TICK.register((mc) -> {
            if (!isEnabled() || mc.player == null) return;
            Vector3f pos = mc.player.position().toVector3f();
            Color color = Color.getHSBColor(c_angle, 1f, 1f);
            pos_l.add(new AddressedData<>(pos, color));
            if (pos_l.size() > Zacoxlo.MAIN_CFG.getIntVal("dtrail_ticks")) pos_l.removeFirst();

            c_angle+=(float) Zacoxlo.MAIN_CFG.getDoubleVal("dtrail_shift");
        });
        WorldRenderEvents.END_MAIN.register((ctx) -> {
            if (!isEnabled() || pos_l.isEmpty() || Minecraft.getInstance().level == null) return;
            Drawer drawer = new Drawer(RSect.rtypesf.DEF_LINE).withContext(ctx).begin();
            drawer.globalize().line(3f).capture();

            AtomicReference<AddressedData<Vector3f, Color>> pr = new AtomicReference<>(pos_l.getFirst());
            pos_l.forEach((inpr) -> {
                drawer.pos(pr.get().getNamespace()).uniNormalize().color(pr.get().getObject().getRGB());
                drawer.pos(inpr.getNamespace()).uniNormalize().color(inpr.getObject().getRGB());

                pr.set(inpr);
            });

            drawer.end();
        });
    }

    @Override
    public List<SetsData<?>> sets() {
        return Arrays.asList(
                new SetsData<>("dtrail_ticks", "Ticks count", ValType.NUMBER, "200"),
                new SetsData<>("dtrail_shift", "Color shift angle", ValType.DOUBLE_NUMBER, "0.05")
        );
    }
}
