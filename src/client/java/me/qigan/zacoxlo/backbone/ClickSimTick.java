package me.qigan.zacoxlo.backbone;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClickSimTick {
    public static Map<KeyMapping, Integer> dataBind = new HashMap<>();
    public static Map<InputConstants.Key, Integer> dataKey = new HashMap<>();

    public static void tick(Minecraft mc) {
        List<KeyMapping> rml1 = new ArrayList<>();
        for (Map.Entry<KeyMapping, Integer> s : dataBind.entrySet()) {
            if (s.getValue() > 0) {
                s.setValue(s.getValue() - 1);
            } else {
                rml1.add(s.getKey());
                s.getKey().setDown(false);
            }
        }
        rml1.forEach((a) -> dataBind.remove(a));

        List<InputConstants.Key> rml2 = new ArrayList<>();
        for (Map.Entry<InputConstants.Key, Integer> s : dataKey.entrySet()) {
            if (s.getValue() > 0) {
                s.setValue(s.getValue() - 1);
            } else {
                rml2.add(s.getKey());
                KeyMapping.set(s.getKey(), false);
            }
        }
        rml2.forEach((a) -> dataKey.remove(a));
    }

    public static void click(InputConstants.Key key, int tick) {
        KeyMapping.set(key, true);
        KeyMapping.click(key);
        dataKey.put(key, tick);
    }

    public static void updateClick(InputConstants.Key key, int tick) {
        KeyMapping.set(key, true);
        if (!dataKey.containsKey(key)) KeyMapping.click(key);
        dataKey.put(key, tick);
    }

    public static void clickWCheck(InputConstants.Key key, int tick) {
        if (dataKey.containsKey(key)) return;
        click(key, tick);
    }
    
    public static void click(KeyMapping keybind, int tick) {
        keybind.setDown(true);
        KeyMapping.click(KeyBindingHelper.getBoundKeyOf(keybind));
        dataBind.put(keybind, tick);
    }

    public static void updateClick(KeyMapping keybind, int tick) {
        keybind.setDown(true);
        if (!dataBind.containsKey(keybind)) KeyMapping.click(KeyBindingHelper.getBoundKeyOf(keybind));
        dataBind.put(keybind, tick);
    }

    public static void clickWCheck(KeyMapping keybind, int tick) {
        if (dataBind.containsKey(keybind)) return;
        click(keybind, tick);
    }
}
