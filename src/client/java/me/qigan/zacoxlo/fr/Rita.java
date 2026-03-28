package me.qigan.zacoxlo.fr;

import me.qigan.zacoxlo.backbone.AnoncHud;
import me.qigan.zacoxlo.backbone.ChatUpdateEvent;
import me.qigan.zacoxlo.cfg.Module;
import me.qigan.zacoxlo.util.UnsortedUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.AABB;
import org.joml.Vector2f;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rita extends Module {

    //62 125 36
    //64 130 34
    public static final AABB I4_BOX = new AABB(62, 125, 36, 64, 130, 34);
    public static Function<String, Pattern> F_RGX_DEVICE = (name) -> Pattern.compile(".*%s completed a device!.*".formatted(name));
    public static Pattern RGX_TERMINAL_7O7 = Pattern.compile(".* (completed|activated) .* \\(([0-9])\\/([0-9])\\).*");

    @Override
    public String id() {
        return "rita";
    }

    @Override
    public String description() {
        return "Spams you sound and visuals on certain events. Mig-29 gaming.";
    }

    @Override
    public void onRegister() {
        ClientTickEvents.END_CLIENT_TICK.register((mc) -> {
            if (!isEnabled()) return;
            if (Minecraft.getInstance().player == null) return;
            if (I4_BOX.contains(Minecraft.getInstance().player.position())) {
                Minecraft.getInstance().level.getEntitiesOfClass(ArmorStand.class, I4_BOX).forEach(ent -> {
                    if (ent.getName().getString().contains("Active")) {
                        AnoncHud.anonc("I4 DONE", 5, 0xFF00DD00);
                    }
                });
            }
        });

        ChatUpdateEvent.register((component) -> {
            if (!isEnabled()) return null;
            if (Minecraft.getInstance().player == null) return null;
            String stripMsg = UnsortedUtils.decolorize(component.getString());
            if (I4_BOX.contains(Minecraft.getInstance().player.position())) {
                if (F_RGX_DEVICE.apply(Minecraft.getInstance().player.nameAndId().name()).matcher(stripMsg).matches()) {
                    AnoncHud.anonc("I4 DONE", 40, 0xFF00DD00);
                }
            }

            Matcher term_matcher = RGX_TERMINAL_7O7.matcher(stripMsg);
            if (term_matcher.matches() && term_matcher.group(2).equalsIgnoreCase(term_matcher.group(3))) {
                AnoncHud.anonc("LEAP LEAP", 40, 0xFFFF90AA, new Vector2f(0, -40),
                        SoundEvents.EXPERIENCE_ORB_PICKUP, new Vector2f(2f, 1f));
            }
            return null;
        });
    }
}
