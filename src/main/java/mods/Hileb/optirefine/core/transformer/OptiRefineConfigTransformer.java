package mods.Hileb.optirefine.core.transformer;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

public final class OptiRefineConfigTransformer implements IClassTransformer {
    private static final byte[] FACADE_NAME = "mods/Hileb/optirefine/optifine/Config".getBytes(java.nio.charset.StandardCharsets.UTF_8);
    private static final String OPTIREFINE_PREFIX = "mods.Hileb.optirefine.";
    private static final String FACADE_CLASS = "mods.Hileb.optirefine.optifine.Config";

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null || transformedName == null
                || !transformedName.startsWith(OPTIREFINE_PREFIX)
                || FACADE_CLASS.equals(transformedName)
                || !containsFacadeReference(basicClass)) {
            return basicClass;
        }

        ClassNode classNode = new ClassNode();
        new ClassReader(basicClass).accept(classNode, 0);
        ConfigFacadeMapper.remap(classNode);
        ConfigFacadeMapper.verify(classNode);
        ClassWriter writer = new ClassWriter(0);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    private static boolean containsFacadeReference(byte[] bytes) {
        if (containsTypeDescriptor(bytes)) {
            return true;
        }

        int facadeUtf8Index = findFacadeUtf8Index(bytes);
        return facadeUtf8Index >= 0 && hasClassConstantForIndex(bytes, facadeUtf8Index);
    }

    private static boolean containsTypeDescriptor(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] != 'L' || i + FACADE_NAME.length + 1 >= bytes.length) {
                continue;
            }
            boolean matches = true;
            for (int j = 0; j < FACADE_NAME.length; j++) {
                if (bytes[i + j + 1] != FACADE_NAME[j]) {
                    matches = false;
                    break;
                }
            }
            if (matches) {
                int suffix = i + FACADE_NAME.length + 1;
                if (bytes[suffix] == ';' || bytes[suffix] == '<') {
                    return true;
                }
            }
        }
        return false;
    }

    private static int findFacadeUtf8Index(byte[] bytes) {
        if (bytes.length < 10 || readInt(bytes, 0) != 0xCAFEBABE) {
            return -1;
        }
        int constantPoolCount = readUnsignedShort(bytes, 8);
        int offset = 10;
        for (int index = 1; index < constantPoolCount; index++) {
            if (offset >= bytes.length) {
                return -1;
            }
            int tag = bytes[offset++] & 0xff;
            switch (tag) {
                case 1 -> {
                    if (offset + 2 > bytes.length) return -1;
                    int length = readUnsignedShort(bytes, offset);
                    offset += 2;
                    if (offset + length > bytes.length) return -1;
                    if (length == FACADE_NAME.length && matches(bytes, offset, FACADE_NAME)
                            && hasClassConstantForIndex(bytes, index)) {
                        return index;
                    }
                    offset += length;
                }
                case 3, 4 -> offset += 4;
                case 5, 6 -> {
                    offset += 8;
                    index++;
                }
                case 7, 8, 16, 19, 20 -> offset += 2;
                case 9, 10, 11, 12, 17, 18 -> offset += 4;
                case 15 -> offset += 3;
                default -> { return -1; }
            }
            if (offset > bytes.length) {
                return -1;
            }
        }
        return -1;
    }

    private static boolean hasClassConstantForIndex(byte[] bytes, int targetIndex) {
        int constantPoolCount = readUnsignedShort(bytes, 8);
        int offset = 10;
        for (int index = 1; index < constantPoolCount; index++) {
            int tag = bytes[offset++] & 0xff;
            switch (tag) {
                case 1 -> offset += 2 + readUnsignedShort(bytes, offset);
                case 3, 4 -> offset += 4;
                case 5, 6 -> {
                    offset += 8;
                    index++;
                }
                case 7 -> {
                    if (readUnsignedShort(bytes, offset) == targetIndex) return true;
                    offset += 2;
                }
                case 8, 16, 19, 20 -> offset += 2;
                case 9, 10, 11, 12, 17, 18 -> offset += 4;
                case 15 -> offset += 3;
                default -> { return false; }
            }
        }
        return false;
    }

    private static boolean matches(byte[] bytes, int offset, byte[] value) {
        for (int i = 0; i < value.length; i++) {
            if (bytes[offset + i] != value[i]) return false;
        }
        return true;
    }

    private static int readUnsignedShort(byte[] bytes, int offset) {
        return (bytes[offset] & 0xff) << 8 | bytes[offset + 1] & 0xff;
    }

    private static int readInt(byte[] bytes, int offset) {
        return (bytes[offset] & 0xff) << 24 | (bytes[offset + 1] & 0xff) << 16
                | (bytes[offset + 2] & 0xff) << 8 | bytes[offset + 3] & 0xff;
    }
}
