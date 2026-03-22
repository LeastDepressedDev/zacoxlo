package me.qigan.zacoxlo.fr.macro;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.InputConstants;
import me.qigan.zacoxlo.backbone.AppliedKey;
import me.qigan.zacoxlo.backbone.SmartTickRoutines;
import me.qigan.zacoxlo.crp.AddressedData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Macro {

    public static class MacroSmartRoutine extends SmartTickRoutines.Routine {

        public int del = 0;
        public final Iterator<AddressedData<Runnable, Integer>> iter;

        public MacroSmartRoutine(Macro macro) {
            this.iter = macro.build().iterator();
        }

        @Override
        public void run() {
            if (del == 0) {
                if (!iter.hasNext()) {
                    this.unplug();
                    return;
                }
                AddressedData<Runnable, Integer> current = iter.next();
                current.getNamespace().run();
                this.del = current.getObject();
            } else del--;
        }
    }

    public enum DEL_TYPE {
        TICK,
        TIME,
        THREAD
    }

    public interface MacroTask {
        Runnable make();
    }

    public final AppliedKey kb;
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
                    case "log" -> this.beseq(new MLogTask(object.get("msg").toString()), object);
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

    public final List<AddressedData<Runnable, Integer>> build() {
        List<AddressedData<Runnable, Integer>> addrl = new ArrayList<>();
        for (AddressedData<MacroTask, Integer> dl : this.sequence)
            addrl.add(new AddressedData<>(dl.getNamespace().make(), dl.getObject()));
        return addrl;
    }

    public static void add(JsonObject object) {
        MacroController.macros.add(new Macro(object));
    }

    protected boolean checkRequirements() {
        // TODO: Implement proper requirement check
        return true;
    }

    public void tryActivate() {
        if (kb.keyPress()) {
            if (this.checkRequirements()) {
                switch (this.delType) {
                    case TICK -> SmartTickRoutines.newRoutine(new MacroSmartRoutine(this));
                }
            }
        }
    }
}