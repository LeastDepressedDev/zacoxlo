package me.qigan.zacoxlo.pre;

import me.friwi.jcefmaven.CefInitializationException;
import me.friwi.jcefmaven.UnsupportedPlatformException;
import me.qigan.zacoxlo.webi.Webi;

import java.io.IOException;

public class PreLoad {
    public static PreProgressionWindow prog_hndl;

    public static void load() {
        System.setProperty("java.awt.headless", "false");
        prog_hndl = new PreProgressionWindow();
        prog_hndl.setVisible(true);

        try {
            Webi.init();
        } catch (UnsupportedPlatformException e) {
            throw new RuntimeException(e);
        } catch (CefInitializationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        long sys_del = System.currentTimeMillis();
        while (System.currentTimeMillis()<=sys_del+2500) {}
        prog_hndl.setVisible(false);
    }
}
