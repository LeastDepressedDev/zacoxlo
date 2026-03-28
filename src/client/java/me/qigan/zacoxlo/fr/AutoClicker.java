package me.qigan.zacoxlo.fr;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.InputConstants;
import me.qigan.zacoxlo.backbone.ClickSimTime;
import me.qigan.zacoxlo.backbone.FirstRoutine;
import me.qigan.zacoxlo.cfg.Module;
import me.qigan.zacoxlo.util.UnsortedUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * {
 *   "items": [
 *     {
 *       "sbid": "TERMINATOR",
 *       "lcm": {
 *         "hold": 50,
 *         "del": 200
 *       },
 *       "rcm": {
 *         "hold": 50,
 *         "del": {
 *           "min": 200,
 *           "max": 500
 *         }
 *       }
 *     }
 *   ]
 * }
 */
public class AutoClicker extends Module {

    public static class ACInfo {
        public record Click(long del_min, long del_max, long hold_min, long hold_max) {};

        protected final Random random;
        private final Click lcm;
        private long at_lcm = 0;
        private final Click rcm;
        private long at_rcm = 0;

        public ACInfo(JsonObject obj) {
            Click lcm = null; Click rcm = null;
            if (obj.has("lcm")) {
                JsonObject lo = obj.getAsJsonObject("lcm");
                JsonElement del = lo.get("del");
                JsonElement hold = lo.get("hold");
                long dm, dx, hm, hx;
                if (del.isJsonObject()) {
                    JsonObject del_obj = del.getAsJsonObject();
                    dm = del_obj.get("min").getAsLong();
                    dx = del_obj.get("max").getAsLong();
                } else {
                    dm = del.getAsLong();
                    dx = del.getAsLong();
                }

                if (hold.isJsonObject()) {
                    JsonObject hold_obj = hold.getAsJsonObject();
                    hm = hold_obj.get("min").getAsLong();
                    hx = hold_obj.get("max").getAsLong();
                } else {
                    hm = hold.getAsLong();
                    hx = hold.getAsLong();
                }

                lcm = new Click(dm, dx, hm, hx);
            }
            if (obj.has("rcm")) {
                JsonObject ro = obj.getAsJsonObject("rcm");
                JsonElement del = ro.get("del");
                JsonElement hold = ro.get("hold");
                long dm, dx, hm, hx;
                if (del.isJsonObject()) {
                    JsonObject del_obj = del.getAsJsonObject();
                    dm = del_obj.get("min").getAsLong();
                    dx = del_obj.get("max").getAsLong();
                } else {
                    dm = del.getAsLong();
                    dx = del.getAsLong();
                }

                if (hold.isJsonObject()) {
                    JsonObject hold_obj = hold.getAsJsonObject();
                    hm = hold_obj.get("min").getAsLong();
                    hx = hold_obj.get("max").getAsLong();
                } else {
                    hm = hold.getAsLong();
                    hx = hold.getAsLong();
                }

                rcm = new Click(dm, dx, hm, hx);
            }
            this.lcm = lcm;
            this.rcm = rcm;
            this.random = new Random();
        }

        public ACInfo(Click lcm, Click rcm) {
            this.lcm = lcm;
            this.rcm = rcm;
            this.random = new Random();
        }

        public final void tryAct(Minecraft mc, long handle) {
            InputConstants.Key lcmKey = KeyBindingHelper.getBoundKeyOf(mc.options.keyAttack);
            InputConstants.Key rcmKey = KeyBindingHelper.getBoundKeyOf(mc.options.keyUse);
            int lcmV = lcmKey.getValue(); InputConstants.Type lcmT = lcmKey.getType();
            int rcmV = rcmKey.getValue(); InputConstants.Type rcmT = rcmKey.getType();
            if (lcm != null && System.currentTimeMillis() > at_lcm &&
                    (lcmT == InputConstants.Type.KEYSYM ? GLFW.glfwGetKey(handle, lcmV) : GLFW.glfwGetMouseButton(handle, lcmV)) == 1
            ) {
                long hold = this.random.nextLong(lcm.hold_min, lcm.hold_max+1), del = this.random.nextLong(lcm.del_min, lcm.del_max);
                mc.options.keyAttack.setDown(false);
                ClickSimTime.click(mc.options.keyAttack, hold);
                at_lcm = System.currentTimeMillis()+del;
            }
            if (rcm != null && System.currentTimeMillis() > at_rcm &&
                    (rcmT == InputConstants.Type.KEYSYM ? GLFW.glfwGetKey(handle, rcmV) : GLFW.glfwGetMouseButton(handle, rcmV)) == 1
            ) {
                long hold = this.random.nextLong(rcm.hold_min, rcm.hold_max+1), del = this.random.nextLong(rcm.del_min, rcm.del_max);
                mc.options.keyUse.setDown(false);
                ClickSimTime.click(mc.options.keyUse, hold);
                at_rcm = System.currentTimeMillis()+del;
            }
        }
    }

    @Override
    public String id() {
        return "acl";
    }

    @Override
    public String description() {
        return "Autoclicker for certain things";
    }

    public static Map<String, ACInfo> AC_CLICKS = new HashMap<>();

    @Override
    public void onRegister() {
        FirstRoutine.addRoutine(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (!isEnabled() || mc.player == null) return;
            ItemStack stack = mc.player.getMainHandItem();
            if (stack.isEmpty()) return;
            String sbid = UnsortedUtils.getSbId(stack);
            if (sbid == null) return;
            ACInfo inf = AC_CLICKS.getOrDefault(sbid, null);
            if (inf != null) inf.tryAct(mc, mc.getWindow().handle());
        });
    }

    @Override
    public JsonObject sets() {
        JsonObject holder = new JsonObject();
        JsonArray items = new JsonArray();
        holder.add("items", items);
        return holder;
    }

    @Override
    public void onReload() {
        this.cfg().get("items").getAsJsonArray().forEach((e) -> {
            JsonObject obj = e.getAsJsonObject();
            AC_CLICKS.put(obj.get("sbid").getAsString(), new ACInfo(obj));
        });
    }
}
