package me.qigan.zacoxlo.crp;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public record CompactTextComponent(Component component, @Nullable Font font, boolean centered) {
    public static CompactTextComponent quick(String text) {
        return new CompactTextComponent(Component.literal(text), null, false);
    }
}
