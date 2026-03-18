package me.qigan.zacoxlo.fr;

import me.qigan.zacoxlo.Zacoxlo;
import me.qigan.zacoxlo.backbone.ClickSimTick;
import me.qigan.zacoxlo.backbone.FirstRoutine;
import me.qigan.zacoxlo.cfg.Module;
import me.qigan.zacoxlo.crp.SetsData;
import me.qigan.zacoxlo.crp.ValType;
import me.qigan.zacoxlo.util.Sync;
import me.qigan.zacoxlo.util.UnsortedUtils;
import me.qigan.zacoxlo.util.render.Drawer;
import me.qigan.zacoxlo.util.render.RSect;
import me.qigan.zacoxlo.util.render.RenderHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.WorldLoader;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AutoLevers extends Module {

    public static final List<BlockPos> CONSTS = Arrays.asList(
            new BlockPos(94, 124, 113), new BlockPos(106, 124, 113),
            new BlockPos(23, 132, 138), new BlockPos(27, 124, 127),
            new BlockPos(2, 122, 55), new BlockPos(14, 122, 55),
            new BlockPos(84, 121, 34), new BlockPos(86, 128, 46),

            new BlockPos(62, 133, 142), new BlockPos(58, 136, 142),
            new BlockPos(62, 136, 142), new BlockPos(60, 134, 142),
            new BlockPos(58, 133, 142), new BlockPos(60, 135, 142)
    );


    public static Set<BlockPos> tracks = new HashSet<>(CONSTS);
    private static int del = 0;

    @Override
    public String id() {
        return "f7levers";
    }

    @Override
    public String description() {
        return "Automatically clicks levers on hovering";
    }

    @Override
    public void onRegister() {
        ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register(((minecraft, clientLevel) -> {
            tracks = new HashSet<>(CONSTS);
        }));

        UseBlockCallback.EVENT.register((player, level, interactionHand, blockHitResult) ->
                {
                    if (!isEnabled() || Minecraft.getInstance().level == null ||
                            Minecraft.getInstance().player == null || !Sync.inDungeon) return InteractionResult.PASS;
                    if (
                            !isEnabled() ||
                            !level.isClientSide() ||
                            interactionHand != InteractionHand.MAIN_HAND)

                        return InteractionResult.PASS;

                    BlockPos pos = blockHitResult.getBlockPos();
                    tracks.remove(new BlockPos(pos.getX(), pos.getY(), pos.getZ()));

                    return InteractionResult.PASS;
                }
        );

        ClientTickEvents.END_CLIENT_TICK.register((minecraft -> {
            if (!isEnabled() || Minecraft.getInstance().level == null || Minecraft.getInstance().player == null
                    || !Sync.inDungeon) return;
//            UnsortedUtils.sendQuickLog(Integer.toString(del) + ":" + Minecraft.getInstance().hitResult.distanceTo(Minecraft.getInstance().player));
            if (del > 0) del--;
        }));

        FirstRoutine.addRoutine(() -> {
            if (!isEnabled() || Minecraft.getInstance().level == null ||
                    Minecraft.getInstance().player == null || !Sync.inDungeon) return;
            if (del != 0) return;

            BlockPos bp = UnsortedUtils.traceRawBlockPos();
            Vec3 pos = new Vec3(
                    Minecraft.getInstance().player.position().x,
                    Minecraft.getInstance().player.position().y+Minecraft.getInstance().player.getEyeHeight(),
                    Minecraft.getInstance().player.position().z
            );
            if (bp == null || pos.distanceToSqr(Minecraft.getInstance().hitResult.getLocation()) >
                    Math.pow(Zacoxlo.MAIN_CFG.getDoubleVal("f7levers_tdist"), 2)) return;
            if (tracks.contains(bp) && !Minecraft.getInstance().player.isCrouching()) {

                ClickSimTick.click(Minecraft.getInstance().options.keyUse, Zacoxlo.MAIN_CFG.getIntVal("f7levers_hold"));

                del = Zacoxlo.MAIN_CFG.getIntVal("f7levers_t");
            }
        });

        WorldRenderEvents.END_MAIN.register((ctx) -> {
            if (!isEnabled() || Minecraft.getInstance().level == null || !Sync.inDungeon) return;
            Drawer drawer = new Drawer(RSect.rtypesf.DEF_LINE).withContext(ctx).begin();
            drawer.globalize().capture();
            CONSTS.forEach((bp -> {
                RenderHelper.autoBox3D(drawer, bp, tracks.contains(bp) ? 0xFF2200 : 0x00FF00, null);
            }));
        });
    }

    @Override
    public List<SetsData<?>> sets() {
        return Arrays.asList(
                new SetsData<>("f7levers_t", "Tick delay", ValType.NUMBER, "5"),
                new SetsData<>("f7levers_hold", "Hold ticks", ValType.NUMBER, "3"),
                new SetsData<>("f7levers_tdist", "Active distance", ValType.DOUBLE_NUMBER, "4.2")
        );
    }
}
