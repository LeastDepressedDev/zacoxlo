package me.qigan.zacoxlo.fr.macro;

import me.qigan.zacoxlo.util.UnsortedUtils;

import java.util.function.Function;

public class MLogTask implements Macro.MacroTask {

    public final String message;

    public MLogTask(String message) {
        this.message = message;
    }

    @Override
    public Function<Void, Boolean> make() {
        return (v) -> {
            try {
                UnsortedUtils.sendQuickLog(this.message);
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        };
    }
}
