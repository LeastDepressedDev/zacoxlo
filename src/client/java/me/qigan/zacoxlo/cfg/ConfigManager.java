package me.qigan.zacoxlo.cfg;

import me.qigan.zacoxlo.Zacoxlo;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class ConfigManager {
    public final String innerPath;

    public ConfigManager(String path) {
        this.innerPath = FabricLoader.getInstance().getConfigDir() + "/" + path;
    }

    public File[] getConfigFiles() {
        File file = new File(innerPath);
        if (file.exists() && file.isDirectory()) {
            return file.listFiles((dir, name) -> name.endsWith(".cfg"));
        } else return null;
    }

    public void reloadMain() {
        Zacoxlo.MAIN_CFG = new MuConfig();
    }

    public void fromFileToFile(File from, File to) {
        try {
            PrintStream out = new PrintStream(to);
            if (to.canWrite()) {
                String lines = new Scanner(from).useDelimiter("\\Z").next();
                out.println(lines);
            } else {
                to.setWritable(true);
                fromFileToFile(from, to);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFrom(String name) {
        File file = new File(innerPath + "/" + name + ".cfg");
        fromFileToFile(file, new File(FabricLoader.getInstance().getConfigDir() + "/zacoxlo.cfg"));
        reloadMain();
    }

    public void saveTo(String name) {
        File file = new File(innerPath + "/" + name + ".cfg");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        fromFileToFile(Zacoxlo.MAIN_CFG.writer.getFile(), file);
    }
}
