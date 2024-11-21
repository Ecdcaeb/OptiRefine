package rml.loader.core;

import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import rml.jrx.announces.EarlyClass;
import rml.jrx.announces.PrivateAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2023/12/14 23:01
 **/
@EarlyClass
@PrivateAPI
@SuppressWarnings("all")
public class ASMUtil {
    public static File gameDir;
    public static boolean saveTransformedClass = false;
    public static final Method m_defineClass;

    static {
        try {
            m_defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
            m_defineClass.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException("Unable to launch RML ASMUtil", e);
        }
    }

    public static byte[] push(String rawName,byte[] clazz){
        if (saveTransformedClass){
            final File outRoot = new File(gameDir,"clazzs/");
            final File outFile = new File(outRoot, rawName.replace('.', File.separatorChar) + ".class");
            final File outDir = outFile.getParentFile();

            if (!outRoot.exists()) {
                outRoot.mkdirs();
            }
            if (!outDir.exists()) {
                outDir.mkdirs();
            }

            if (outFile.exists()) {
                outFile.delete();
            }

            try {
                final OutputStream output = new FileOutputStream(outFile);
                output.write(clazz);
                output.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return clazz;
    }
    public static void injectBefore(InsnList method, Supplier<InsnList> hook, Predicate<AbstractInsnNode> predicate){
        LinkedList<AbstractInsnNode> nodes = new LinkedList<>();
        ListIterator<AbstractInsnNode> iterator=method.iterator();
        AbstractInsnNode node;
        while (iterator.hasNext()){
            node = iterator.next();
            if (predicate.test(node)){
                nodes.add(node);
            }
        }
        for(AbstractInsnNode node1 : nodes){
            method.insertBefore(node1, hook.get());
        }
    }
    public static Class<?> defineClass(String name, byte[] clazz) throws InvocationTargetException, IllegalAccessException {
        return (Class<?>)m_defineClass.invoke(Launch.classLoader, name, clazz, 0, clazz.length);
    }
}
