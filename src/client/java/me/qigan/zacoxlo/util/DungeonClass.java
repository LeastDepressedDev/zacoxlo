package me.qigan.zacoxlo.util;

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

    public static final Function<String, Pattern> F_RGX_CLASS_GRAB = (name) -> Pattern.compile("\\[(.)] %s .*".formatted(name), Pattern.CASE_INSENSITIVE);

    // TODO: Check if this working. I wrote it without having an opportunity to test in rt.
    public static DungeonClass capturePlayerClass() {
        if (Sync.inDungeon || Minecraft.getInstance().player == null) return null;
        else {
            List<String> sbord = UnsortedUtils.getScoreboard();
            for (String str : sbord) {
                Matcher matcher = F_RGX_CLASS_GRAB.apply(Minecraft.getInstance().player.nameAndId().name()).matcher(str);
                if (matcher.matches()) {
                    return switch (matcher.group()) {
                        case "M" -> DungeonClass.MAGE;
                        case "T" -> DungeonClass.TANK;
                        case "B" -> DungeonClass.BERSERK;
                        case "A" -> DungeonClass.ARCHER;
                        case "H" -> DungeonClass.HEALER;
                        default -> null;
                    };
                }
            }
            return null;
        }
    }
}
