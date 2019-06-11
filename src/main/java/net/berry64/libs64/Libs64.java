package net.berry64.libs64;

import net.berry64.libs64.utils.Lib64FM;
import org.bukkit.plugin.Plugin;

public class Libs64 {
    private Plugin pl;

    public Libs64(Plugin plugin){
        pl = plugin;
    }

    public Lib64FM getFileManager(){
        return new Lib64FM(pl);
    }
}
