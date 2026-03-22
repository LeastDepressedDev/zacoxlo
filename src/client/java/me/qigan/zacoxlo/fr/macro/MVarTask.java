package me.qigan.zacoxlo.fr.macro;

import net.minecraft.client.Minecraft;

import java.util.function.Function;

public class MVarTask implements Macro.MacroTask {

    public enum VarType {
        SLOT
    }

    public final String name;
    public final VarType type;

    public MVarTask(String name, String type) {
        this.name = name;
        switch (type) {
            case "slot" -> this.type = VarType.SLOT;
            default -> throw new MacroInitException("Unknown var operation type");
        }
    }

    @Override
    public Function<Void, Boolean> make() {
        return switch (type) {
            case SLOT -> (v) -> {
                if (Minecraft.getInstance().player == null) return false;
                Macro.VARS.put(name, Minecraft.getInstance().player.getInventory().getSelectedSlot());
                return true;
            };
            default -> null;
        };
    }
}
