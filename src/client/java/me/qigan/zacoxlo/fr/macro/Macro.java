package me.qigan.zacoxlo.fr.macro;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.InputConstants;
import me.qigan.zacoxlo.backbone.AppliedKey;
import me.qigan.zacoxlo.backbone.SmartFirstRoutines;
import me.qigan.zacoxlo.backbone.SmartTickRoutines;
import me.qigan.zacoxlo.crp.AddressedData;
import me.qigan.zacoxlo.util.UnsortedUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class Macro {

    public static final Map<String, Object> VARS = new HashMap<>();

    public static final Component MACRO_INTERRUPTED_MSG = Component.literal("Interrupted macro>>>");

    public static class MacroSmartTickRoutine extends SmartTickRoutines.Routine {

        public int del = 0;
        public final Iterator<AddressedData<Function<Void, Boolean>, Integer>> iter;

        public MacroSmartTickRoutine(Macro macro) {
            this.iter = macro.build().iterator();
        }

        @Override
        public void run() {
            if (del == 0) {
                if (!iter.hasNext()) {
                    this.unplug();
                    return;
                }
                AddressedData<Function<Void, Boolean>, Integer> current = iter.next();
                if (!current.getNamespace().apply(null)) {
                    Minecraft.getInstance().player.displayClientMessage(MACRO_INTERRUPTED_MSG, false);
                    this.unplug();
                    return;
                }
                this.del = current.getObject();
            } else del--;
        }
    }

    public static class MacroSmartFirstRoutine extends SmartFirstRoutines.Routine {
        public long lastTime = 0;
        public final Iterator<AddressedData<Function<Void, Boolean>, Integer>> iter;
        public int move = 0;

        public MacroSmartFirstRoutine(Macro macro) {
            this.iter = macro.build().iterator();
        }

        @Override
        public void run() {
            if (System.currentTimeMillis() - lastTime > move) {
                if (!iter.hasNext()) {
                    this.unplug();
                    return;
                }
                AddressedData<Function<Void, Boolean>, Integer> current = iter.next();
                if (!current.getNamespace().apply(null)) {
                    Minecraft.getInstance().player.displayClientMessage(MACRO_INTERRUPTED_MSG, false);
                    this.unplug();
                    return;
                }
                this.move = current.getObject();
                this.lastTime = System.currentTimeMillis();
            }
        }
    }

    public static class MacroThreadRoutine extends Thread {

        public MacroThreadRoutine(Macro macro) {
            super(() -> {
                for (AddressedData<Function<Void, Boolean>, Integer> current : macro.build()) {
                    if (!current.getNamespace().apply(null)) {
                        Minecraft.getInstance().player.displayClientMessage(MACRO_INTERRUPTED_MSG, false);
                        break;
                    }
                    try {
                        Thread.sleep(current.getObject());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    public enum DEL_TYPE {
        TICK,
        TIME,
        THREAD
    }

    public interface MacroTask {
        Function<Void, Boolean> make();
    }

    public final AppliedKey kb;
    public final int killzone;
    public final DEL_TYPE delType;
    protected final List<AddressedData<String, Integer>> requires = new ArrayList<>();
    protected final List<AddressedData<MacroTask, Integer>> sequence = new ArrayList<>();

    public Macro(JsonObject obj) throws MacroInitException {
        JsonObject activate = obj.getAsJsonObject("activate");
        // Birth of macro bind
        this.kb = new AppliedKey(
                activate.get("type").getAsString().equalsIgnoreCase("mouse") ? InputConstants.Type.MOUSE : InputConstants.Type.KEYSYM,
                activate.get("key").getAsInt()
        );
        this.killzone = obj.has("killzone") ? obj.get("killzone").getAsInt() : 0;
        switch (obj.get("deltype").getAsString()) {
            case "tick" -> this.delType = DEL_TYPE.TICK;
            case "time" -> this.delType = DEL_TYPE.TIME;
            case "thread" -> this.delType = DEL_TYPE.THREAD;
            default -> throw new MacroInitException("Unknown deltype");
        }

        // Requires
        if (obj.has("require")) {

            try {
                obj.getAsJsonArray("require").forEach(ele -> {
                    JsonObject object = ele.getAsJsonObject();
                    this.requires.add(new AddressedData<>(object.get("sbid").getAsString(),
                            object.has("slot") ? object.get("slot").getAsInt() : null));
                });
            } catch (Exception ex) {
                throw new MacroInitException("Check `require` section");
            }
        }

        if (!obj.has("seq")) throw new MacroInitException("No sequence given");
        AtomicInteger cti = new AtomicInteger();
        obj.getAsJsonArray("seq").forEach(ele -> {
            try {
                JsonObject object = ele.getAsJsonObject();
                switch (object.get("type").getAsString()) {
                    case "log" -> this.beseq(new MLogTask(object.get("msg").getAsString()), object);
                    case "chat" -> this.beseq(new MChatTask(object.get("msg").getAsString()), object);
                    case "switch" -> {
                        Integer intl = object.has("slot") ? object.get("slot").getAsInt() : null;
                        String strid = object.has("sbid") ? object.get("sbid").getAsString() : null;
                        String var = object.has("var") ? object.get("var").getAsString() : null;
                        if (var != null) {
                            this.beseq(new MSwitchTask(null, null).var(var), object);
                        } else {
                            MSwitchTask mSwitchTask = new MSwitchTask(intl, strid);
                            if (object.has("legit")) {
                                mSwitchTask.legit(object.get("legit").getAsBoolean());
                            }
                            this.beseq(mSwitchTask, object);
                        }
                    }
                    case "press" -> {
                        if (object.has("bind")) {
                            this.beseq(new MPressTask.Bind(object.get("bind").getAsString(),
                                    object.has("hold") ? object.get("hold").getAsInt() : 1), object);
                        } else if (object.has("key")) {
                            JsonObject keyJson = object.get("key").getAsJsonObject();
                            InputConstants.Type type = keyJson.get("type").getAsString().equalsIgnoreCase("mouse") ? InputConstants.Type.MOUSE : InputConstants.Type.KEYSYM;
                            this.beseq(new MPressTask.Key(type.getOrCreate(keyJson.get("key").getAsInt()),
                                    object.has("hold") ? object.get("hold").getAsInt() : 1), object);
                        } else {
                            throw new MacroInitException("No activation condition given for `press` event");
                        }
                    }
                    case "var" -> this.beseq(new MVarTask(object.get("name").getAsString(), object.get("cap").getAsString()), object);
                    default -> throw new MacroInitException("Unknown type: %s".formatted(object.get("type").getAsString()));
                }

                cti.getAndIncrement();
            } catch (Exception exception) {
                exception.printStackTrace();
                throw new MacroInitException("Specified exception happened on initialising sequence element: %d".formatted(cti.get()));
            }
        });
    }

    private void beseq(MacroTask task, JsonObject obj) {
        this.sequence.add(new AddressedData<>(task, obj.has("del") ? obj.get("del").getAsInt() : 0));
    }

    public final List<AddressedData<Function<Void, Boolean>, Integer>> build() {
        List<AddressedData<Function<Void, Boolean>, Integer>> addrl = new ArrayList<>();
        for (AddressedData<MacroTask, Integer> dl : this.sequence)
            addrl.add(new AddressedData<>(dl.getNamespace().make(), dl.getObject()));
        return addrl;
    }

    public static void add(JsonObject object) {
        MacroController.macros.add(new Macro(object));
    }

    protected boolean checkRequirements() {
        Player player = Minecraft.getInstance().player;
        Set<String> names = new HashSet<>();
        for (int i = 0; i < 9; i++) {
            String sbid = UnsortedUtils.getSbId(player.getSlot(i).get());
            if (sbid != null) names.add(sbid);
        }
        int inert = 0;
        for (AddressedData<String, Integer> rq : requires) {
            if (rq.getObject() == null) {
                if (names.contains(rq.getNamespace())) inert++;
            } else {
                String sbid = UnsortedUtils.getSbId(player.getSlot(rq.getObject()).get());
                if (sbid != null && sbid.equalsIgnoreCase(rq.getNamespace())) inert++;
            }
        }
        return inert >= requires.size();
    }

    public boolean tryActivate() {
        if (kb.keyPress()) {
            if (this.checkRequirements()) {
                switch (this.delType) {
                    case TICK -> SmartTickRoutines.newRoutine(new MacroSmartTickRoutine(this));
                    case TIME -> SmartFirstRoutines.newRoutine(new MacroSmartFirstRoutine(this));
                    // TODO: Fix thread mode not working
                    case THREAD -> new MacroThreadRoutine(this).start();
                }
                MacroController.update_kz(killzone);
                return true;
            }
        }
        return false;
    }
}