package me.qigan.zacoxlo.crp;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

public record CompactTextComponent(Component component, @Nullable Font font, boolean centered, Vector2f scale) {

    public static final Vector2f DEF_SCALE_VEC = new Vector2f(1f,1f);

    public static CompactTextComponent quick(String text) {
        return new CompactTextComponent(Component.literal(text), null, false, DEF_SCALE_VEC);
    }
}
