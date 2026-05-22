package me.qigan.zacoxlo.fr;

import me.qigan.zacoxlo.cfg.Module;
import me.qigan.zacoxlo.crp.AddressedData;
import me.qigan.zacoxlo.fr.macro.MPressTask;
import me.qigan.zacoxlo.fr.macro.MSwitchTask;
import me.qigan.zacoxlo.fr.macro.Macro;
import me.qigan.zacoxlo.util.UnsortedUtils;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DaggerSwap extends Module {

    public enum STATE {
        SPIRIT,
        CRYSTAL,
        ASHEN,
        AURIC
    }

    public static long atl = 0;

    @SuppressWarnings("null")
    private static STATE match() {
        double dist = 12;
        STATE state = null;
        Minecraft mc = Minecraft.getInstance();
        for (Entity ent : mc.level.getEntitiesOfClass(ArmorStand.class, UnsortedUtils.getRadiusAABB(5))) {
            String lnM = UnsortedUtils.decolorize(ent.getName().getString());
            double d = mc.player.distanceTo(ent);
            if (lnM.contains("ASHEN") && d < dist) state = STATE.ASHEN;
            else if (lnM.contains("SPIRIT") && d < dist) state = STATE.SPIRIT;
            else if (lnM.contains("AURIC") && d < dist) state = STATE.AURIC;
            else if (lnM.contains("CRYSTAL") && d < dist) state = STATE.CRYSTAL;
        }
        return state;
    }


    private static AddressedData<Integer, Item>[] getSlotsAndStates() {
        AddressedData<Integer, Item>[] data = new AddressedData[]{null, null};
        for (int i = 0; i < 9; i++) {
            ItemStack stack = Minecraft.getInstance().player.getSlot(i).get();
            if (stack.isEmpty()) continue;
            if (stack.getDisplayName().getString().contains("Dagger")) {
                if (stack.getItem() == Items.IRON_SWORD || stack.getItem() == Items.DIAMOND_SWORD) data[0] = new AddressedData<>(i, stack.getItem());
                else if (stack.getItem() == Items.STONE_SWORD || stack.getItem() == Items.GOLDEN_SWORD) data[1] = new AddressedData<>(i, stack.getItem());
            }
        }
        return data;
    }


    @Override
    public String id() {
        return "fuck_dagger_swap";
    }

    @Override
    public String description() {
        return "Automatically swaps daggers on hit";
    }

    @Override
    public void onRegister() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!isEnabled() || System.currentTimeMillis() < atl) return InteractionResult.PASS;
            Minecraft mc = Minecraft.getInstance();
            if (player == mc.player) {
                AddressedData<Integer, Item>[] dats = getSlotsAndStates();
                STATE state = match();
                if (dats[0] == null || dats[1] == null || state == null) return InteractionResult.PASS;
                Macro macro = new Macro(null, 0, Macro.DEL_TYPE.TIME);
                switch (state) {
                    case ASHEN:
                        macro.pushSeq(new MSwitchTask(dats[1].getNamespace(), null), 30);
                        if (dats[1].getObject() == Items.GOLDEN_SWORD) macro.pushSeq(new MPressTask.Bind(mc.options.keyUse.getName(), 1), 0);
                        break;
                    case AURIC:
                        macro.pushSeq(new MSwitchTask(dats[1].getNamespace(), null), 30);
                        if (dats[1].getObject() == Items.STONE_SWORD) macro.pushSeq(new MPressTask.Bind(mc.options.keyUse.getName(), 1), 0);
                        break;
                    case SPIRIT:
                        macro.pushSeq(new MSwitchTask(dats[0].getNamespace(), null), 30);
                        if (dats[0].getObject() == Items.DIAMOND_SWORD) macro.pushSeq(new MPressTask.Bind(mc.options.keyUse.getName(), 1), 0);
                        break;
                    case CRYSTAL:
                        macro.pushSeq(new MSwitchTask(dats[0].getNamespace(), null), 30);
                        if (dats[0].getObject() == Items.IRON_SWORD) macro.pushSeq(new MPressTask.Bind(mc.options.keyUse.getName(), 1), 0);
                        break;
                }
                macro.forceActivate();
                atl = System.currentTimeMillis()+500;
            }

            return InteractionResult.PASS;
        });
    }
}
