package me.qigan.zacoxlo.util;

import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sync {
    public static boolean inDungeon = false;

    public static void clientTick(Minecraft mc) {
        List<String> sbord = UnsortedUtils.getScoreboard();
        boolean flagDungeon0 = false, flagDungeon1 = false, flagDungeon2 = false;
        for (String str : sbord) {
            if (str.contains("The Catacombs")) flagDungeon0 = true;
            if (str.contains("Time Elapsed:")) flagDungeon1 = true;
            if (str.contains("Cleared:")) flagDungeon2 = true;
        }
        inDungeon = flagDungeon0 || (flagDungeon1 && flagDungeon2);
    }
}
