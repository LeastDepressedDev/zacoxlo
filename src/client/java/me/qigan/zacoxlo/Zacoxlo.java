package me.qigan.zacoxlo;

import me.qigan.zacoxlo.cfg.ConfigManager;
import me.qigan.zacoxlo.cfg.MuConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class Zacoxlo implements ClientModInitializer {

    public static MuConfig MAIN_CFG;
    public static ConfigManager CFG_MANAGER;

	@Override
	public void onInitializeClient() {
        File file = new File(FabricLoader.getInstance().getConfigDir() + "/abse/configs");
        if (!file.exists()) file.mkdirs();

        Holder.init();

        Zacoxlo.MAIN_CFG = new MuConfig();
        Zacoxlo.CFG_MANAGER = new ConfigManager("abse/configs");
	}
}