package me.qigan.zacoxlo;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import me.qigan.zacoxlo.cfg.Module;
import me.qigan.zacoxlo.util.Sync;
import me.qigan.zacoxlo.util.UnsortedUtils;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.network.chat.Component;

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
//                                UnsortedUtils.sendQuickLog(String.join("\n", UnsortedUtils.getScoreboard()));
                                UnsortedUtils.sendQuickLog(""+Sync.inDungeon);
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
