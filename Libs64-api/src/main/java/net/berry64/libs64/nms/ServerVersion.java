package net.berry64.libs64.nms;

import net.berry64.libs64.Core;

public class ServerVersion {

    public int getPrimaryVersion() {
        return 0;
    }

    public int getMinecraftVersion() {
        return 0;
    }

    public int getReleaseVersion() {
        return 0;
    }

    public String getVersionString() {
        return null;
    }

    public static ServerVersion fromPackageString(String packageName) {
        return null;
    }

    public ServerVersion(String packageName) {
    }

    public ServerVersion(int primaryVersion, int minecraftVersion, int releaseVersion) {
    }

    public ServerVersion(int mainVersion, int secondaryVersion) {
    }
}
