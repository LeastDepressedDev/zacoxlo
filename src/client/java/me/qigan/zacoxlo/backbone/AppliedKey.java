package me.qigan.zacoxlo.backbone;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class AppliedKey {

    public static int global_id = 0;
    public static final Set<AppliedKey> appliedKeys = new TreeSet<>(Comparator.comparing(ele -> ele.gid));
    public static final KeyMapping.Category NULL_CATEGORY = KeyMapping.Category.register(ResourceLocation.fromNamespaceAndPath("zacoxlo", "null"));

    public static void init() {
        appliedKeys.forEach(AppliedKey::unreg);
        FirstRoutine.addRoutine(() -> appliedKeys.forEach(AppliedKey::_update));
    }

    public final int gid;
    public final InputConstants.Type type;
    public final int key;

    private final KeyMapping mapping;

    protected boolean _inpress = false;

    public AppliedKey(InputConstants.Type type, int key, KeyMapping.Category category) {
        this.gid = global_id++;
        this.type = type;
        this.key = key;

        this.mapping = new KeyMapping("nkey:"+gid, this.type, this.key, category == null ? NULL_CATEGORY : category);

        appliedKeys.add(this);
    }

    public AppliedKey(InputConstants.Type type, int key) {
        this(type, key, null);
    }

    public void unreg() {
        appliedKeys.remove(this);
    }

    public boolean consume() {
        return this.mapping.consumeClick();
    }

    public boolean isDown() {
        return this.mapping.isDown();
    }

    // Single key press tracker
    public boolean keyPress() {
        if (this.isDown()) {
            if (this._inpress) {
                return false;
            } else {
                this._inpress = true;
                return true;
            }
        }
        else return false;
    }

    public final void _update() {
        if (!this.mapping.isDown()) this._inpress = false;
    }

    public KeyMapping getRawMapping() {
        return this.mapping;
    }
}
