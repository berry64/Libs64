/*============================
 = Copyright (c) 2019. berry64
 = All Rights Reserved
 ===========================*/

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
    private static Map<Plugin, List<String>> patchMap = new ConcurrentHashMap<>();
    private static Method defineClass = null;
    static {
        try {
            defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

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
    public static Class<?> registerPatch(Plugin pl, String patchClass, ServerVersion versionUsed){
        List<String> classList = patchMap.computeIfAbsent(pl, k -> new ArrayList<>());
        if(classList.contains(patchClass))
            Core.warn("Attempted to re-register Lib64NMSPatch class: "+patchClass);
        else
            classList.add(patchClass);

        return registerPatch_internal(pl, patchClass, versionUsed);
    }
    private static Class<?> registerPatch_internal(Plugin pl, String classname, ServerVersion version){
        try {
            return transform_internal(pl.getClass().getClassLoader(), classname, version);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new ClassTransformException("Unable to read class file for "+classname);
        }
    }
    private static byte[] getClassBytes_internal(ClassLoader ld, String clz){
        String classpath = clz.replace(".", File.separator)+".class";
        System.out.println("Reading "+classpath);
        InputStream is = ld.getResourceAsStream(classpath);
        try {
            return ByteStreams.toByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ClassTransformException("Cannot read class: "+clz);
        }
    }
    private static Class<?> transform_internal(ClassLoader loader, String patchClass, ServerVersion curVer) throws FileNotFoundException {
        ClassReader reader = new ClassReader(getClassBytes_internal(loader, patchClass));
        ClassWriter cw = new ClassWriter(reader, 0);
        //TraceClassVisitor processed = new TraceClassVisitor(cw, new PrintWriter("/Users/berry64/Desktop/processed.txt"));
        VersionTransformer vt = new VersionTransformer(cw, curVer.getVersionString(), Lib64NMS.getCurrentServerVersion().getVersionString());
        //TraceClassVisitor original = new TraceClassVisitor(vt, new PrintWriter("/Users/berry64/Desktop/original.txt"));
        reader.accept(vt, 0);

        return defineClass(loader, patchClass, cw.toByteArray());
    }

    private final static class RegisterThread extends Thread{
        Plugin pl;
        String patchClass;
        ServerVersion versionUsed;
        public RegisterThread(Plugin pl, String patchClass, ServerVersion versionUsed){
            super();
            this.pl = pl;
            this.patchClass = patchClass;
            this.versionUsed = versionUsed;
        }
        @Override
        public void run() {
            List<String> classList = patchMap.computeIfAbsent(pl, k -> new ArrayList<>());
            if(classList.contains(patchClass))
                Core.warn("Attempted to re-register Lib64NMSPatch class: "+patchClass);
            else
                classList.add(patchClass);

            registerPatch_internal(pl, patchClass, versionUsed);
        }
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
    public static void registerPatchAsync(Plugin pl, String patchClass, ServerVersion versionUsed){
        new RegisterThread(pl, patchClass, versionUsed).start();
    }

    private static byte[] getClassBytes(Class<?> clz){
        String classpath = clz.getName().replace(".", File.separator)+".class";
        InputStream is = clz.getClassLoader().getResourceAsStream(classpath);
        try {
            return ByteStreams.toByteArray(is);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ClassTransformException("Cannot read class"+clz.getName());
        }
    }

    private static Class<?> transform(ClassLoader loader, Class<?> patchClass, ServerVersion curVer){
        ClassReader reader = new ClassReader(getClassBytes(patchClass));
        ClassWriter cw = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES);
        VersionTransformer vt = new VersionTransformer(cw, curVer.getVersionString(), Lib64NMS.getCurrentServerVersion().getVersionString());
        reader.accept(vt, 0);

        return defineClass(loader, patchClass.getName(), cw.toByteArray());
    }

    private static Class<?> defineClass(ClassLoader loader, String name, byte[] bytes){
        System.out.println("Defining Class "+name);
        try {
            boolean flag = false;
            if(!defineClass.isAccessible()){
                flag = true;
                defineClass.setAccessible(true);
            }
            Class<?> val = (Class<?>) defineClass.invoke(loader, name, bytes, 0, bytes.length);
            if(flag)
                defineClass.setAccessible(false);
            return val;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new ClassTransformException("Unable to define transformed class as "+name + " maybe the class was used before it is registered with Libs64?");
        }
    }
}