package me.qigan.zacoxlo.fr.macro;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.platform.InputConstants;
import me.qigan.zacoxlo.backbone.AppliedKey;
import me.qigan.zacoxlo.backbone.FirstRoutine;
import me.qigan.zacoxlo.cfg.Module;
import me.qigan.zacoxlo.util.UnsortedUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Read for keybinding key ids and modes understanding: https://www.glfw.org/docs/3.3/group__keys.html
 */
public class MacroController extends Module {

    public static KeyMapping.Category CATEGORY;
    public static final List<Macro> macros = new ArrayList<>();

    @Override
    public String id() {
        return "macros";
    }

    @Override
    public String description() {
        return "Controller for simple macros.";
    }

    @Override
    public void onRegister() {
        CATEGORY = KeyMapping.Category.register(ResourceLocation.fromNamespaceAndPath("zacoxlo", "mcr"));
//        KeyMapping maper = new KeyMapping("GOOOL", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_I, CATEGORY);

        try {
            reload();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        FirstRoutine.addRoutine(() -> macros.forEach(ele -> {
            if (Minecraft.getInstance().level == null || Minecraft.getInstance().player == null) return;
            ele.tryActivate();
        }));
    }

    public static void reload() throws FileNotFoundException {
        File dir = new File(FabricLoader.getInstance().getConfigDir() + "/zacoxlo/smacro");
        if (!dir.isDirectory()) throw new RuntimeException("I dont even know how you did this dude...");

        macros.forEach(key -> key.kb.unreg());
        macros.clear();

        System.out.println("Reloading macro: "+Arrays.toString(dir.list()));
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            try {
                Macro.add(JsonParser.parseReader(new FileReader(file)).getAsJsonObject());
            } catch (MacroInitException ex) {
                ex.printStackTrace();
                throw new MacroInitException("Exception caught while loading %s".formatted(file.getAbsolutePath()));
            }
        }
    }
}
