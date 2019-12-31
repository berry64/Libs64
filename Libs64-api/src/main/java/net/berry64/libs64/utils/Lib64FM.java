package net.berry64.libs64.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import java.io.*;
import java.util.regex.Pattern;

public class Lib64FM {

    public Lib64FM(Plugin plugin) {
    }

    public void loadConfig() {
    }

    public YamlConfiguration loadFile(String filename) {
        return null;
    }

    public YamlConfiguration loadFile(File directory, String filename) {
        return null;
    }

    public boolean createFile(File target) {
        return true;
    }

    public boolean saveResource(String name, File output) {
        return true;
    }

    public boolean createDirectory(File directory) {
        return true;
    }

    public void deleteDirectory(File directory) {
    }

    public boolean saveFile(File file, FileConfiguration yml) {
        return true;
    }
}
