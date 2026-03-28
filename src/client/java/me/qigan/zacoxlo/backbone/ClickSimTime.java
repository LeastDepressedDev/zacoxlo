package me.qigan.zacoxlo.backbone;

import com.mojang.blaze3d.platform.InputConstants;
import me.qigan.zacoxlo.crp.AddressedData;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClickSimTime {
    public static Map<KeyMapping, AddressedData<Long, Long>> dataBind = new HashMap<>();
    public static Map<InputConstants.Key, AddressedData<Long, Long>> dataKey = new HashMap<>();

    public static void tick() {
        List<KeyMapping> rml1 = new ArrayList<>();
        for (Map.Entry<KeyMapping, AddressedData<Long, Long>> s : dataBind.entrySet()) {
            if (System.currentTimeMillis() > s.getValue().getNamespace()+s.getValue().getObject()) {
                rml1.add(s.getKey());
                s.getKey().setDown(false);
            }
        }
        rml1.forEach((a) -> dataBind.remove(a));

        List<InputConstants.Key> rml2 = new ArrayList<>();
        for (Map.Entry<InputConstants.Key, AddressedData<Long, Long>> s : dataKey.entrySet()) {
            if (System.currentTimeMillis() > s.getValue().getNamespace()+s.getValue().getObject()) {
                rml2.add(s.getKey());
                KeyMapping.set(s.getKey(), false);
            }
        }
        rml2.forEach((a) -> dataKey.remove(a));
    }

    public static void click(InputConstants.Key key, long hold) {
        KeyMapping.set(key, true);
        KeyMapping.click(key);
        dataKey.put(key, new AddressedData<>(System.currentTimeMillis(), hold));
    }

    public static void updateClick(InputConstants.Key key, long hold) {
        KeyMapping.set(key, true);
        if (!dataKey.containsKey(key)) KeyMapping.click(key);
        dataKey.put(key, new AddressedData<>(System.currentTimeMillis(), hold));
    }

    public static void clickWCheck(InputConstants.Key key, long hold) {
        if (dataKey.containsKey(key)) return;
        click(key, hold);
    }

    public static void click(KeyMapping keybind, long hold) {
        keybind.setDown(true);
        KeyMapping.click(KeyBindingHelper.getBoundKeyOf(keybind));
        dataBind.put(keybind, new AddressedData<>(System.currentTimeMillis(), hold));
    }

    public static void updateClick(KeyMapping keybind, long hold) {
        keybind.setDown(true);
        if (!dataBind.containsKey(keybind)) KeyMapping.click(KeyBindingHelper.getBoundKeyOf(keybind));
        dataBind.put(keybind, new AddressedData<>(System.currentTimeMillis(), hold));
    }

    public static void clickWCheck(KeyMapping keybind, long hold) {
        if (dataBind.containsKey(keybind)) return;
        click(keybind, hold);
    }
}
