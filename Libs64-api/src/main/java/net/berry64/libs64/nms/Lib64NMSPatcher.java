package net.berry64.libs64.nms;

import com.google.common.io.ByteStreams;
import net.berry64.libs64.Core;
import net.berry64.libs64.nms.exceptions.ClassTransformException;
import net.berry64.libs64.nms.internal.VersionTransformer;
import org.bukkit.plugin.Plugin;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Lib64NMSPatcher {

    /**
     * Registers a class with Libs64 for Patching.
     * <p>
     * <strong>MUST BE REGISTERED BEFORE ANY USE OF THE CLASS</strong>
     * </p>
     * it is highly recommended that the registration is done when the plugin is being enabled, after it is defined, you can access it normally. Be advised that this process takes time and will have an impact on the plugin startup time.
     * @param pl The Plugin
     * @param patchClass The name of the class
     * @param versionUsed The version that the class was originally programmed in
     * @return The instantiated class
     */
    public static Class<?> registerPatch(Plugin pl, String patchClass, ServerVersion versionUsed) {
        return null;
    }

    /**
     * Registers a class with Libs64 for Patching<br>the patching is done in a separate thread, so the server can be loaded faster
     * <br>
     * <br>
     * <strong>MUST BE REGISTERED BEFORE ANY USE OF THE CLASS</strong>
     * <br>
     * Note that the patching process takes time, if the class is defined by something else before patching is finished, the patching will fail.
     * this is only safe for classes that you are sure will not be used/called shortly after server starts.
     * <br>
     * <br>
     * it is highly recommended that the registration is done when the plugin is being enabled, after it is defined, you can access it normally
     * @param pl The Plugin
     * @param patchClass The name of the class
     * @param versionUsed The version that the class was originally programmed in
     */
    public static void registerPatchAsync(Plugin pl, String patchClass, ServerVersion versionUsed) {
    }
}
