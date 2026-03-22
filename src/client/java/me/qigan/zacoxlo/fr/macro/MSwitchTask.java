package me.qigan.zacoxlo.fr.macro;

import me.qigan.zacoxlo.backbone.ClickSimTick;
import me.qigan.zacoxlo.util.UnsortedUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public class MSwitchTask implements Macro.MacroTask {

    public final Integer slot;
    public final String sbid;
    protected boolean legit = false;
    protected String var = null;

    public MSwitchTask(Integer slot, String sbid) {
        this.slot = slot;
        this.sbid = sbid;
    }

    public MSwitchTask(Integer slot) {
        this(slot, null);
    }

    public MSwitchTask(String sbid) {
        this(null, sbid);
    }

    public MSwitchTask legit(boolean leg) {
        this.legit = leg;
        return this;
    }

    public MSwitchTask var(String var) {
        this.var = var;
        return this;
    }

    @Override
    public Function<Void, Boolean> make() {
        if (sbid != null && slot != null) {
            return (v) -> {
                Player player = Minecraft.getInstance().player;
                if (player == null) return false;
                ItemStack stack = player.getSlot(slot).get();
                String id = UnsortedUtils.getSbId(stack);
                if (id == null || !id.equalsIgnoreCase(sbid)) return false;
                switchToSlot(slot, legit);
                return true;
            };
        } else if (sbid == null && slot != null) {
            return (v) -> {
                Player player = Minecraft.getInstance().player;
                if (player == null) return false;
                switchToSlot(slot, legit);
                return true;
            };
        } else if (sbid != null && slot == null) {
            return (v) -> {
                Player player = Minecraft.getInstance().player;
                if (player == null) return false;
                for (int i = 0; i < 9; i++) {
                    ItemStack stack = player.getSlot(i).get();
                    String id = UnsortedUtils.getSbId(stack);
                    if (id != null && id.equalsIgnoreCase(sbid)) {
                        switchToSlot(i, legit);
                        return true;
                    }
                }
                return false;
            };
        } else {
            if (var != null) {
                return (v) -> {
                    switchToSlot((Integer) Macro.VARS.get(var), legit);
                    return true;
                };
            } else return (v) -> {
                UnsortedUtils.sendQuickLog("IDK how you created empty switch macro element without specifications man");
                return false;
            };
        }
    }

    protected void switchToSlot(int slot, boolean legit) {
        if (Minecraft.getInstance().player == null) return;
        if (legit) Minecraft.getInstance().player.getInventory().setSelectedSlot(slot);
        else {
            KeyMapping keyMapping = Minecraft.getInstance().options.keyHotbarSlots[slot];
            ClickSimTick.click(keyMapping, 1);
        };
    }
}
