package me.qigan.zacoxlo.fr.macro;

import me.qigan.zacoxlo.util.UnsortedUtils;

public class MLogTask implements Macro.MacroTask{

    public final String message;

    public MLogTask(String message) {
        this.message = message;
    }

    @Override
    public Runnable make() {
        return () -> UnsortedUtils.sendQuickLog(this.message);
    }
}
