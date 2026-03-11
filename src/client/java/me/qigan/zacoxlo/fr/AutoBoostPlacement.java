package me.qigan.zacoxlo.fr;

import me.qigan.zacoxlo.Zacoxlo;
import me.qigan.zacoxlo.backbone.ClickSimTick;
import me.qigan.zacoxlo.cfg.Module;
import me.qigan.zacoxlo.util.Sync;
import me.qigan.zacoxlo.util.UnsortedUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AutoBoostPlacement extends Module {

    private static int del = 0;
    public static Set<Item> PLACE_ITEMS = new HashSet<>(Arrays.asList(
            Items.SOUL_SAND, Items.CHEST, Items.ENDER_CHEST
    ));

    @Override
    public String id() {
        return "lav_boost";
    }

    @Override
    public String description() {
        return "Automatic placmenent of soul sand if selected";
    }

    @Override
    public void onRegister() {
        WorldRenderEvents.END_MAIN.register((ctx) -> {
            if (!isEnabled() || Minecraft.getInstance().level == null ||
                    Minecraft.getInstance().player == null || !Sync.inDungeon) return;
            if (del != 0) return;

            BlockHitResult bhit = UnsortedUtils.traceBlock();
            if (bhit == null) return;
            Vec3 pos = new Vec3(
                    Minecraft.getInstance().player.position().x,
                    Minecraft.getInstance().player.position().y+Minecraft.getInstance().player.getEyeHeight(),
                    Minecraft.getInstance().player.position().z
            );
            BlockPos bp = bhit.getBlockPos().offset(0, 1, 0);
            BlockState state = Minecraft.getInstance().level.getBlockState(bp);
            if (pos.distanceToSqr(bhit.getLocation()) > 4.2*4.2 || state.getBlock() != Blocks.LAVA ||
                   !PLACE_ITEMS.contains(Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND).getItem())) return;

            ClickSimTick.click(Minecraft.getInstance().options.keyUse, 2);
            del = 7;
        });

        ClientTickEvents.END_CLIENT_TICK.register((minecraft -> {
            if (!isEnabled() || Minecraft.getInstance().level == null ||
                    Minecraft.getInstance().player == null || !Sync.inDungeon) return;
            if (del > 0) del--;
        }));
    }
}
