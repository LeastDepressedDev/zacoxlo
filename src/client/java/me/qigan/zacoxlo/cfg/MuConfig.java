package me.qigan.zacoxlo.cfg;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.qigan.zacoxlo.Holder;
import me.qigan.zacoxlo.crp.*;
import me.qigan.zacoxlo.util.UnsortedUtils;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MuConfig {
    private Map<String, String> sets = new HashMap<>();
    public String MAIN_PTH;

    public final AddressedWriter writer;

    public MuConfig() {
        this.writer = new AddressedWriter(FabricLoader.getInstance().getConfigDir() + "/zacoxlo.cfg");
        this.MAIN_PTH = FabricLoader.getInstance().getConfigDir()  + "/zacoxlo";
        reload();
    }

    public void reload() {
        sets.clear();
        Module.rtCfg.clear();
        for (Module mdl: Holder.MRL) {
            if (!writer.contains(mdl.id()) /*|| Debug.DISABLE_STATE.contains(mdl.id())*/) {
                EnabledByDefault enb = mdl.getClass().getAnnotation(EnabledByDefault.class);
                this.writer.set(mdl.id(), enb == null ? "false" : "true");
            }
            AutoDisable annot = mdl.getClass().getAnnotation(AutoDisable.class);
            if (annot != null) this.writer.set(mdl.id(), "false");

            File file = new File("%s/%s.json".formatted(this.MAIN_PTH, mdl.id()));
            JsonObject para = mdl.sets();
            if (para != null) {
                JsonObject mdlObj;
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                        FileWriter fileWriter = new FileWriter(file);
                        fileWriter.write(para.toString());
                        mdlObj = para;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        JsonObject object = JsonParser.parseReader(new FileReader(file)).getAsJsonObject();
                        UnsortedUtils.syncWithPrototype(para, object);
                        FileWriter fileWriter = new FileWriter(file);
                        fileWriter.write(para.toString());
                        mdlObj = object;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                Module.rtCfg.put(mdl.id(), mdlObj);
            }
        }

        for(AddressedData<String, String> w: writer.get()) {
            sets.put(w.getNamespace(), w.getObject());
        }
    }

	/*public List<AddressedData<String, Boolean>> getAll() {
		List<AddressedData<String, Boolean>> result = new ArrayList<AddressedData<String, Boolean>>();
		for(Entry<String, Boolean> val: sets.entrySet()) {
			result.add(new AddressedData<String, Boolean>(val.getKey(), val.getValue()));
		}
		return result;
	}*/

    public Map<String, String> getAll() {
        return sets;
    }

    public void set(String namespace, String value) {
        this.sets.put(namespace, value);
        this.writer.set(namespace, value);
    }

    public String getStrVal(String namespace) {
        return sets.get(namespace);
    }

    public int getIntVal(String namespace) {
        return Integer.parseInt(getStrVal(namespace));
    }

    public double getDoubleVal(String namespace) {
        return Double.parseDouble(getStrVal(namespace));
    }

    public boolean has(String namespace) {
        return sets.containsKey(namespace);
    }

    public boolean getBoolVal(String namespace) {
        return sets.get(namespace).equalsIgnoreCase("true");
    }

    public void toggle(String namespace) {
        if (sets.get(namespace).equalsIgnoreCase("true")) {
            this.set(namespace, "false");
            EDLogic.tryDisableLogic(namespace);
        } else {
            this.set(namespace, "true");
            EDLogic.tryEnableLogic(namespace);
        }
    }
}