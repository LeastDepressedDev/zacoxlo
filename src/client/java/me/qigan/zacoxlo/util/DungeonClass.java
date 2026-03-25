package me.qigan.zacoxlo.util;

import me.qigan.zacoxlo.crp.AddressedData;
import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum DungeonClass {
    TANK,
    HEALER,
    MAGE,
    BERSERK,
    ARCHER

    ;

    public static final Function<String, Pattern> F_RGX_CLASS_GRAB = (name) -> Pattern.compile("\\[[0-9]+] %s \\((.+) ([I, V, L, X]+)\\)".formatted(name), Pattern.CASE_INSENSITIVE);

    // Class, lvl
    public static AddressedData<DungeonClass, Integer> capturePlayerClass(String name) {
        if (!Sync.inDungeon || Minecraft.getInstance().player == null) return null;
        else {
            List<String> sbord = UnsortedUtils.getTab();
            for (String str : sbord) {
                Matcher matcher = F_RGX_CLASS_GRAB.apply(name).matcher(str);
                if (matcher.matches()) {
                    DungeonClass cls = switch (matcher.group(1)) {
                        case "Mage" -> DungeonClass.MAGE;
                        case "Tank" -> DungeonClass.TANK;
                        case "Berserk" -> DungeonClass.BERSERK;
                        case "Archer" -> DungeonClass.ARCHER;
                        case "Healer" -> DungeonClass.HEALER;
                        default -> null;
                    };
                    return cls == null ? null : new AddressedData<>(cls, UnsortedUtils.romanToInt(matcher.group(2)));
                }
            }
            return null;
        }
    }

    public static AddressedData<DungeonClass, Integer> capturePlayerClass() {
        if (Minecraft.getInstance().player == null) return null;
        return capturePlayerClass(Minecraft.getInstance().player.nameAndId().name());
    }
}
