package me.qigan.zacoxlo.webi;

import me.friwi.jcefmaven.CefAppBuilder;
import me.friwi.jcefmaven.CefInitializationException;
import me.friwi.jcefmaven.MavenCefAppHandlerAdapter;
import me.friwi.jcefmaven.UnsupportedPlatformException;
import me.friwi.jcefmaven.impl.progress.ConsoleProgressHandler;
import net.fabricmc.loader.api.FabricLoader;
import org.cef.CefApp;

import java.io.File;
import java.io.IOException;

public class Webi {

    public static CefApp MAIN_APP;

    public static void init() throws UnsupportedPlatformException, CefInitializationException, IOException, InterruptedException {
        //Create a new CefAppBuilder instance
        CefAppBuilder builder = new CefAppBuilder();

        //Configure the builder instance
        builder.setInstallDir(new File(FabricLoader.getInstance().getConfigDir() + "/zacoxlo/webi"));
        builder.setProgressHandler(new WebIProgressionHandler());
        builder.addJcefArgs("--disable-gpu");
        builder.getCefSettings().windowless_rendering_enabled = true; //Default - select OSR mode

        //Set an app handler. Do not use CefApp.addAppHandler(...), it will break your code on MacOSX!
        builder.setAppHandler(new MavenCefAppHandlerAdapter(){});

        //Build a CefApp instance using the configuration above
        MAIN_APP = builder.build();
    }
}
