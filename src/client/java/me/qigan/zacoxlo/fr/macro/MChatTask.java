package me.qigan.zacoxlo.fr.macro;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

public class MChatTask implements Macro.MacroTask{

    public final String message;

    public MChatTask(String message) {
        this.message = message;
    }

    @Override
    public Function<Void, Boolean> make() {
        return (v) -> {
            if (Minecraft.getInstance().player == null) return false;
            try {
                String msg = normalize(message);
                if (msg.startsWith("/")) {
                    Minecraft.getInstance().player.connection.sendCommand(msg.substring(1));
                } else {
                    Minecraft.getInstance().player.connection.sendChat(msg);
                }
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        };
    }

    public static String normalize(String string) {
        return StringUtil.trimChatMessage(StringUtils.normalizeSpace(string.trim()));
    }
}
