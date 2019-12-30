/*============================
 = Copyright (c) 2019. berry64
 = All Rights Reserved
 ===========================*/

package net.berry64.libs64.nms;

import org.bukkit.Bukkit;

public class Lib64NMS {
    private static ServerVersion currentServer = null;
    public static ServerVersion getCurrentServerVersion(){
        //org.bukkit.craftbukkit.v1_14_R1.CraftServer
        if(currentServer == null)
            currentServer = new ServerVersion(Bukkit.getServer().getClass().getName().split("\\.")[3]);
        return currentServer;
    }
}
