package me.qigan.zacoxlo;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import me.qigan.zacoxlo.backbone.ClientTickTimes;
import me.qigan.zacoxlo.cfg.Module;
import me.qigan.zacoxlo.fr.macro.MacroController;
import me.qigan.zacoxlo.gui.MainGui;
import me.qigan.zacoxlo.util.UnsortedUtils;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Objects;

public class Commands {
    public static void registerMain(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext accx) {
        dispatcher.register(ClientCommandManager.literal("zacoxlo")
                .then(ClientCommandManager.argument("args", StringArgumentType.greedyString())
                .executes(
                ctx -> {
                    String[] args = StringArgumentType.getString(ctx, "args").split(" ");

                    if (args.length == 0) {
                        ctx.getSource().sendFeedback(Component.literal("Zacoxlo(%s)\n By LDD".formatted(ZacoxloGm.VERSION)));
                    } else {
                        switch (args[0]) {
                            case "list":
                            {
                                Holder.MRL.forEach((md) -> {
                                    ctx.getSource().sendFeedback( Component.literal("%s |  %s".formatted(md.id(), md.description())).withColor(moduleColor(md)));
                                });
                            }
                            break;
                            case "toggle":
                            {
                                if (args.length > 1) {
                                    Module module = Holder.MAPPER.getOrDefault(args[1], null);
                                    if (module != null) {
                                        Zacoxlo.MAIN_CFG.toggle(args[1]);
                                        ctx.getSource().sendFeedback(
                                                Component.literal("Toggled %s(%s)".formatted(module.fname(), module.id()))
                                                        .withColor(moduleColor(module))
                                        );
                                    } else {
                                        ctx.getSource().sendFeedback(Component.literal("System couldn't find given id..."));
                                    }


                                } else {
                                    ctx.getSource().sendFeedback(Component.literal("Module id not given..."));
                                }
                            }
                            break;
                            case "test":
                            {
//                                ClientTickTimes.schedule(() -> Minecraft.getInstance().setScreenAndShow(new MainGui()), 2);
                            }
                            break;
                            case "sbid": {
                                String sbid = UnsortedUtils.getSbId(Minecraft.getInstance().player.getMainHandItem());
                                if (sbid != null) {
                                    UnsortedUtils.sendQuickLog(sbid);
                                    System.out.println(sbid);
                                }
                            }
                            break;
                            case "macro":
                            {
                                if (args.length > 1) {
                                    if (args[1].equalsIgnoreCase("reload") || args[1].equalsIgnoreCase("rl")) {
                                        try {
                                            MacroController.reload();
                                        } catch (FileNotFoundException e) {
                                            throw new RuntimeException(e);
                                        }
                                        Minecraft.getInstance().player.displayClientMessage(Component.literal("Reloaded macros").withColor(0xFF00AA00), false);
                                    }
                                } else {
                                    File dir = new File(FabricLoader.getInstance().getConfigDir() + "/zacoxlo/smacro");
                                    for (String str : Objects.requireNonNull(dir.list())) {
                                        Minecraft.getInstance().player.displayClientMessage(Component.literal(str).withColor(0xFF00AA00), false);
                                    }
                                }
                            }
                            break;
                        }
                    }

                    return 1;
                })
        ));
    }

    private static int moduleColor(Module module) {
        return module.isEnabled() ? 0x00FF00 : 0xFF2200;
    }
}
