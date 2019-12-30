/*============================
 = Copyright (c) 2019. berry64
 = All Rights Reserved
 ===========================*/

package net.berry64.libs64;

import net.berry64.libs64.nms.Lib64NMS;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.IOException;

public class Core extends JavaPlugin implements Listener{
    private static Core instance = null;

    public static Core getInstance() {
        return instance;
    }
    public static void warn(String msg){
        if(instance != null)
            instance.getLogger().warning(msg);
        else
            System.err.println("[Libs64] "+msg);
    }
    public static void info(String msg){
        if(instance != null)
            instance.getLogger().info(msg);
        else
            System.out.println("[Libs64] "+msg);
    }

    @Override
    public void onEnable() {
        instance = this;
        Lib64NMS.getCurrentServerVersion();
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
