package me.qigan.zacoxlo.util;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.*;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UnsortedUtils {
    public static AABB getRadiusAABB(double r) {
        Vec3 vec3 = Minecraft.getInstance().player.position();
        return new AABB(vec3.x-r, vec3.y-r, vec3.z-r, vec3.x+r, vec3.y+r, vec3.z+r);
    }

    public static Vector3f getBoundingFromEntity(Entity entity) {
        return new Vector3f((float) entity.getBoundingBox().getXsize(), (float) entity.getBoundingBox().getYsize(), (float) entity.getBoundingBox().getZsize());
    }

    public static List<ArmorStand> getArmorStands(Entity entity) {
        return getArmorStands(entity.level(), entity.getBoundingBox());
    }

    public static List<ArmorStand> getArmorStands(Level world, AABB box) {
        return world.getEntitiesOfClass(ArmorStand.class, box.inflate(0, 2, 0), EntitySelector.ENTITY_NOT_BEING_RIDDEN);
    }

    public static void sendQuickLog(String log) {
        Minecraft.getInstance().player.displayClientMessage(Component.literal(log), false);
    }

    public static BlockHitResult traceBlock() {
        if (Minecraft.getInstance().hitResult == null || Minecraft.getInstance().hitResult.getType() != HitResult.Type.BLOCK) return null;
        return (BlockHitResult) Minecraft.getInstance().hitResult;
    }

    public static BlockPos traceRawBlockPos() {
        BlockHitResult bph = traceBlock();
        if (bph == null) return null;
        return new BlockPos(bph.getBlockPos());
    }

    public static List<String> getScoreboardNames() {
        List<String> lines = new ArrayList<>();
        if (Minecraft.getInstance().level == null) return lines;
        Scoreboard scoreboard = Minecraft.getInstance().level.getScoreboard();
        scoreboard.getObjectives().forEach((objective -> {
            lines.add(objective.getDisplayName().getString());
        }));

        return lines;
    }

    public static List<String> getScoreboard() {
        List<String> lines = new ArrayList<>();
        if (Minecraft.getInstance().level == null) return lines;
        Scoreboard scoreboard = Minecraft.getInstance().level.getScoreboard();
        Objective objective = scoreboard.getDisplayObjective(DisplaySlot.BY_ID.apply(1));
        scoreboard.getTrackedPlayers().forEach((scoreHolder -> {
            if (scoreboard.listPlayerScores(scoreHolder).containsKey(objective)) {
                PlayerTeam team = scoreboard.getPlayersTeam(scoreHolder.getScoreboardName());

                if (team != null) {
                    Component textLine = Component.empty().append(team.getPlayerPrefix().copy()).append(team.getPlayerSuffix().copy());
                    String strLine = team.getPlayerPrefix().getString() + team.getPlayerSuffix().getString();

                    lines.add(strLine);
                }
            }
        }));

        return lines;
    }

    public static InputConstants.Key getKeyByInt(InputConstants.Type type, int key) {
        return type.getOrCreate(key);
    }
}
