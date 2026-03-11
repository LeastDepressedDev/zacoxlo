package me.qigan.zacoxlo.backbone;

import me.qigan.zacoxlo.crp.AddressedData;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClickSimTick {
    public static Map<KeyMapping, Integer> data = new HashMap<>();

    public static void tick(Minecraft mc) {
        List<KeyMapping> rml = new ArrayList<>();
        for (Map.Entry<KeyMapping, Integer> s : data.entrySet()) {
            if (s.getValue() > 0) {
                s.setValue(s.getValue() - 1);
            } else {
                rml.add(s.getKey());
                s.getKey().setDown(false);
            }
        }
        rml.forEach((a) -> data.remove(a));
    }

    public static void click(KeyMapping key, int tick) {
        key.setDown(true);
        //KeyBinding.onTick(code);
        data.put(key, tick);
    }

    public static void updatableClick(KeyMapping key, int tick) {
        key.setDown(true);
        //if (!data.containsKey(code)) KeyBinding.onTick(code);
        data.put(key, tick);
    }

    public static void clickWCheck(KeyMapping key, int tick) {
        if (data.containsKey(key)) return;
        click(key, tick);
    }
}
