/*============================
 = Copyright (c) 2019. berry64
 = All Rights Reserved
 ===========================*/

package net.berry64.libs64.nms;

import net.berry64.libs64.Core;

public class ServerVersion {
    private int primaryVersion = -1, secondaryVersion = -1, releaseVersion = -1;
    private String versionString = null;

    public int getPrimaryVersion() {
        return primaryVersion;
    }

    public int getMinecraftVersion() {
        return secondaryVersion;
    }

    public int getReleaseVersion() {
        return releaseVersion;
    }

    public String getVersionString() {
        return versionString;
    }

    public static ServerVersion fromPackageString(String packageName) {
        return new ServerVersion(packageName);
    }

    public ServerVersion(String packageName) {
        versionString = packageName;
        try {
            primaryVersion = Integer.parseInt((versionString.split("_")[0]).replaceAll("[^0-9]", ""));
            secondaryVersion = Integer.parseInt((versionString.split("_")[1]).replaceAll("[^0-9]", ""));
            releaseVersion = Integer.parseInt((versionString.split("_")[2]).replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e){
            Core.warn("Unknown/Invalid ServerVersion packageName "+packageName+" version will be invalid");
        }
    }

    public ServerVersion(int primaryVersion, int minecraftVersion, int releaseVersion) {
        this.primaryVersion = primaryVersion;
        this.secondaryVersion = minecraftVersion;
        this.releaseVersion = releaseVersion;

        versionString = String.format(format, primaryVersion, minecraftVersion, releaseVersion);
    }
    public ServerVersion(int mainVersion, int secondaryVersion){
        this(1, mainVersion, secondaryVersion);
    }

    private static final String format = "v%d_%d_R%d";
}
