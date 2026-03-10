package me.qigan.zacoxlo.cfg;

import me.qigan.zacoxlo.Zacoxlo;
import me.qigan.zacoxlo.crp.SetsData;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    public abstract String id();
    public String fname() {return this.id();}
    public String renderName() {return this.fname();}
    public abstract String description();
    public List<SetsData<?>> sets() {return new ArrayList<>();}
    public abstract void onRegister();
    public boolean isEnabled() {return Zacoxlo.MAIN_CFG.getBoolVal(this.id());}
    //public WKeybind moduleBind() {return Index.KEY_MANAGER.get(id());}
}
