package me.qigan.zacoxlo.cfg;

import com.google.gson.JsonObject;
import me.qigan.zacoxlo.Zacoxlo;

import java.util.HashMap;
import java.util.Map;


public abstract class Module {

    public static Map<String, JsonObject> rtCfg = new HashMap<>();

    public abstract String id();
    public String fname() {return this.id();}
    public String renderName() {return this.fname();}
    public abstract String description();
    public JsonObject sets() {return null;}
    public final JsonObject cfg() {return rtCfg.get(this.id());}
    public abstract void onRegister();
    public boolean isEnabled() {return Zacoxlo.MAIN_CFG.getBoolVal(this.id());}
    public void onReload() {} // Called after config reloaded
    //public WKeybind moduleBind() {return Index.KEY_MANAGER.get(id());}
}
