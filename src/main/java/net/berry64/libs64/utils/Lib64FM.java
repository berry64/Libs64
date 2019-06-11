package net.berry64.libs64.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.regex.Pattern;

public class Lib64FM {
    private static Pattern filenameregex = Pattern.compile("\\w+\\.\\w+");




    private Plugin pl;

    public Lib64FM(Plugin plugin){
        pl = plugin;
    }

    public void loadConfig(){
        File f = new File(pl.getDataFolder(), "config.yml");
        if(!f.exists())
            pl.saveDefaultConfig();
        pl.reloadConfig();
    }

    public YamlConfiguration loadFile(String filename){
        return loadFile(pl.getDataFolder(), filename);
    }

    public YamlConfiguration loadFile(File directory, String filename){
        if(!filenameregex.matcher(filename).matches())
            filename += ".yml";

        File f = new File(directory, filename);
        if(!f.exists()) {
            if(!saveResource(filename, f)){
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return (f.exists())? YamlConfiguration.loadConfiguration(f):new YamlConfiguration();
    }

    public boolean createFile(File target){
        if(target.isDirectory()){
            return target.mkdirs();
        } else {
            if(target.exists())
                return false;
            try {
                target.createNewFile();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public boolean saveResource(String name, File output){
        InputStream in;
        if((in = pl.getResource(name)) != null){
            output.getParentFile().mkdirs();
            try {
                OutputStream out = new FileOutputStream(output);
                byte[] buf = new byte[1024];
                int len;
                while((len= in.read(buf)) >0){
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean saveFile(File file, FileConfiguration yml){
        try {
            yml.save(file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
