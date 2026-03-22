package me.qigan.zacoxlo.fr.macro;

import com.mojang.blaze3d.platform.InputConstants;
import me.qigan.zacoxlo.backbone.ClickSimTick;
import me.qigan.zacoxlo.util.UnsortedUtils;
import net.minecraft.client.KeyMapping;

import java.util.function.Function;

public class MPressTask {
    public static class Bind implements Macro.MacroTask {

        public final String mapping;
        public final int hold;

        public Bind(String mapping, int hold) {
            this.mapping = mapping;
            this.hold = hold;
        }

        @Override
        public Function<Void, Boolean> make() {
            return (v) -> {
                KeyMapping kb = UnsortedUtils.keyVanillaBind(mapping);
                if (kb == null) return false;
                ClickSimTick.updateClick(kb, hold); return true;
            };
        }
    }

    public static class Key implements Macro.MacroTask {

        public final InputConstants.Key key;
        public final int hold;

        public Key(InputConstants.Key key, int hold) {
            this.key = key;
            this.hold = hold;
        }

        @Override
        public Function<Void, Boolean> make() {
            return (v) -> {
                ClickSimTick.updateClick(key, hold); return true;
            };
        }
    }
}
