package mods.Hileb.optirefine.core.mixin;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MixinPostProcessorTest {
    @Test void lateAddedCallsAreLoweredOnlyForTheScheduledClassIdentity() throws Exception {
        ClassNode target = target();
        MethodNode bridge = new MethodNode(Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC | Opcodes.ACC_NATIVE, "bridge", "(Ljava/lang/Object;)Ljava/lang/String;", null, null);
        AnnotationNode annotation = new AnnotationNode("Lmods/Hileb/optirefine/library/cursedmixinextensions/annotations/AccessibleOperation;");
        annotation.values = new ArrayList<>(List.of("opcode", Opcodes.CHECKCAST, "desc", "java.lang.String"));
        bridge.visibleAnnotations = new ArrayList<>(List.of(annotation)); target.methods.add(bridge);
        MixinPostProcessor processor = new MixinPostProcessor();
        processor.schedule(target);
        MethodNode late = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "lateCall", "(Ljava/lang/Object;)Ljava/lang/String;", null, null);
        late.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        late.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, target.name, "bridge", bridge.desc, false));
        late.instructions.add(new InsnNode(Opcodes.ARETURN)); target.methods.add(late);
        ClassNode sameName = target(); sameName.methods.add(new MethodNode(Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC | Opcodes.ACC_NATIVE, "untouched", "()V", null, null));
        processor.export(null, sameName.name, false, sameName);
        assertEquals("untouched", sameName.methods.getFirst().name);
        processor.export(null, target.name, false, target);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS); target.accept(writer); byte[] bytes = writer.toByteArray();
        Class<?> type = new ClassLoader(getClass().getClassLoader()) {
            Class<?> define() { return defineClass(target.name.replace('/', '.'), bytes, 0, bytes.length); }
        }.define();
        assertEquals("late", type.getMethod("lateCall", Object.class).invoke(null, "late"));
        assertThrows(java.lang.reflect.InvocationTargetException.class, () -> type.getMethod("lateCall", Object.class).invoke(null, 1));
        assertThrows(NoSuchMethodException.class, () -> type.getDeclaredMethod("bridge", Object.class));
        processor.export(null, target.name, false, target);
        assertEquals("again", type.getMethod("lateCall", Object.class).invoke(null, "again"));
    }

    private static ClassNode target() {
        ClassNode node = new ClassNode(); node.version = Opcodes.V17; node.access = Opcodes.ACC_PUBLIC;
        node.name = "contract/LateTarget"; node.superName = "java/lang/Object"; return node;
    }
}
