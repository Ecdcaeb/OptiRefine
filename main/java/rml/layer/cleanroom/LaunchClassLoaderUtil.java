package rml.layer.cleanroom;

import it.unimi.dsi.fastutil.ints.Int2IntLinkedOpenHashMap;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import rml.jrx.reflection.jvm.FieldAccessor;
import rml.jrx.reflection.jvm.MethodAccessor;
import rml.jrx.reflection.jvm.ReflectionHelper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2024/8/2 18:49
 **/
public class LaunchClassLoaderUtil {
    public static final FieldAccessor<Map<String, Class<?>>, LaunchClassLoader> cachedClasses = ReflectionHelper.getFieldAccessor(LaunchClassLoader.class, "cachedClasses");
    public static final MethodAccessor<String, LaunchClassLoader> transformName = ReflectionHelper.getMethodAccessor(LaunchClassLoader.class, "transformName", "transformName", String.class);
    public static final MethodAccessor<String, LaunchClassLoader> untransformName = ReflectionHelper.getMethodAccessor(LaunchClassLoader.class, "untransformName", "untransformName", String.class);
    public static final MethodAccessor<byte[], LaunchClassLoader> getClassBytes = ReflectionHelper.getMethodAccessor(LaunchClassLoader.class, "getClassBytes", "getClassBytes", String.class);
    public static final MethodAccessor<byte[], LaunchClassLoader> runTransformers = ReflectionHelper.getMethodAccessor(LaunchClassLoader.class, "runTransformers", "runTransformers", String.class, String.class, byte[].class);


    public static byte[] getClassBytes(String name){
        LaunchClassLoader classLoader = Launch.classLoader;
        String transformedName = transformName.invoke(classLoader, name);
        String untransformedName = untransformName.invoke(classLoader, name);
        try {
            return runTransformers.invoke(classLoader, untransformedName, transformedName, classLoader.getClassBytes(untransformedName));
        } catch (Exception e) {
            return null;
        }
    }


    public static class ClassUtil{
        public static String getSuperClass(byte[] clazz){
            Int2IntLinkedOpenHashMap _utfs = new Int2IntLinkedOpenHashMap();
            Int2IntLinkedOpenHashMap _classes=new Int2IntLinkedOpenHashMap();
            int constantsCount=readUnsignedShort(clazz,8);
            int passcount=10;
            for(int i=1;i<constantsCount;i++){
                int size=0;
                switch (clazz[passcount]){
                    case 9:
                    case 10:
                    case 11:
                    case 3:
                    case 4:
                    case 12:
                    case 18:
                        size = 5;
                        break;
                    case 5:
                    case 6:
                        size = 9;
                        break;
                    case 1://UTF8
                        int UTFSize = readUnsignedShort(clazz,passcount + 1);
                        size = 3 + UTFSize;
                        _utfs.put(i, passcount);
                        break;
                    case 15:
                        size = 4;
                        break;
                    case 7://class
                        size = 3;
                        int index = readUnsignedShort(clazz,passcount+1);
                        _classes.put(i, index);
                        break;
                    default:
                        size = 3;
                        break;
                }
                passcount += size;

            }
            passcount += 4;
            passcount = readUnsignedShort(clazz,passcount);
            passcount = _classes.get(passcount);
            passcount = _utfs.get(passcount);
            int UTFSize = readUnsignedShort(clazz,passcount + 1);
            return readUTF8(clazz,passcount+3,UTFSize);
        }
        public static int readUnsignedShort(byte[] b, int index) {
            return ((b[index] & 0xFF) << 8) | (b[index + 1] & 0xFF);
        }
        public static String readUTF8(byte[] b,int index,int length){
            return new String(Arrays.copyOfRange(b, index, index+length));
        }

        public static int getClassModifier(byte[] clazz){
            int constantsCount = readUnsignedShort(clazz,8);
            int passcount = 10;
            for(int i = 1; i < constantsCount; i++){
                int size=0;
                switch (clazz[passcount]){
                    case 9:
                    case 10:
                    case 11:
                    case 3:
                    case 4:
                    case 12:
                    case 18:
                        size = 5;
                        break;
                    case 5:
                    case 6:
                        size = 9;
                        break;
                    case 1:
                        int UTFSize=readUnsignedShort(clazz,passcount + 1);
                        size = 3 + UTFSize;
                        break;
                    case 15:
                        size = 4;
                        break;
                    default:
                        size = 3;
                        break;
                }
                passcount += size;
            }
            return readUnsignedShort(clazz, passcount);
        }
    }


}
