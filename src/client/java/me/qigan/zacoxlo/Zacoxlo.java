package me.qigan.zacoxlo;

import me.qigan.zacoxlo.backbone.ClickSimTick;
import me.qigan.zacoxlo.backbone.ClientTickTimes;
import me.qigan.zacoxlo.cfg.ConfigManager;
import me.qigan.zacoxlo.cfg.MuConfig;
import me.qigan.zacoxlo.util.Sync;
import me.qigan.zacoxlo.util.render.RSect;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class Zacoxlo implements ClientModInitializer {

    public static MuConfig MAIN_CFG;
    public static ConfigManager CFG_MANAGER;

	@Override
	public void onInitializeClient() {
        File file = new File(FabricLoader.getInstance().getConfigDir() + "/zacoxlo/configs");
        if (!file.exists()) file.mkdirs();
        RSect.register();

        Holder.init();

        Zacoxlo.MAIN_CFG = new MuConfig();
        Zacoxlo.CFG_MANAGER = new ConfigManager("zacoxlo/configs");

        ClientTickEvents.END_CLIENT_TICK.register(ClickSimTick::tick);
        ClientTickEvents.END_CLIENT_TICK.register(Sync::clientTick);
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickTimes::clientStaticTick);

        ClientCommandRegistrationCallback.EVENT.register((
                (commandDispatcher,
                 commandBuildContext) -> {
                    Commands.registerMain(commandDispatcher, commandBuildContext);
                    return;
                }));

        WorldRenderEvents.END_MAIN.register((ctx) -> {RSect.endBatches();});
	}
}