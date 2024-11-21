package mods.Hileb.optirefine.library.foundationx.asm;

import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class LaunchClassWriter extends ClassWriter {
    public LaunchClassWriter(int flags) {
        super(flags);
    }

    public LaunchClassWriter(ClassReader classReader, int flags) {
        super(classReader, flags);
    }

    @Override
    protected String getCommonSuperClass(String type1, String type2) {
        return getCommonSuperClass0(type1, type2);
    }

    public static String getCommonSuperClass0(final String type1, final String type2) {
        Class<?> class1;
        Class<?> class2;

        ClassLoader classLoader = Launch.appClassLoader;
        try { // Make sure classes in same class loader
            class1 = Class.forName(type1.replace('/', '.'), false, classLoader);
            class2 = Class.forName(type2.replace('/', '.'), false, classLoader);
        } catch (ClassNotFoundException e) {
            try {
                class1 = Class.forName(type1.replace('/', '.'), false, Launch.classLoader);
                class2 = Class.forName(type2.replace('/', '.'), false, Launch.classLoader);
            } catch (ClassNotFoundException e2) {
                throw new TypeNotPresentException(type2, e2);
            }
        }

        if (class1.isAssignableFrom(class2)) {
            return type1;
        }
        if (class2.isAssignableFrom(class1)) {
            return type2;
        }
        if (class1.isInterface() || class2.isInterface()) {
            return "java/lang/Object";
        } else {
            do {
                class1 = class1.getSuperclass();
            } while (!class1.isAssignableFrom(class2));
            return class1.getName().replace('.', '/');
        }
    }
}
