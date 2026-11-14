package mods.Hileb.optirefine.library.cursedmixinextensions;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InnerClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CursedMixinExtensionsTest {
    private static final String ANNOTATIONS = "Lmods/Hileb/optirefine/library/cursedmixinextensions/annotations/";

    private static ClassNode target(String name) {
        ClassNode node = new ClassNode();
        node.version = Opcodes.V17;
        node.access = Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER;
        node.name = "audit/" + name;
        node.superName = "java/lang/Object";
        return node;
    }

    private static MethodNode method(ClassNode owner, int access, String name, String descriptor) {
        MethodNode method = new MethodNode(access, name, descriptor, null, null);
        owner.methods.add(method);
        return method;
    }

    private static AnnotationNode annotation(String name, Object... values) {
        AnnotationNode result = new AnnotationNode(ANNOTATIONS + name + ";");
        result.values = new ArrayList<>(Arrays.asList(values));
        return result;
    }

    private static void constructor(ClassNode node) {
        MethodNode init = method(node, Opcodes.ACC_PUBLIC, "<init>", "()V");
        init.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        init.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false));
        init.instructions.add(new InsnNode(Opcodes.RETURN));
    }

    private static Class<?> process(ClassNode node) {
        CursedMixinExtensions.postApply(node);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        node.accept(writer);
        byte[] bytes = writer.toByteArray();
        return new ClassLoader(CursedMixinExtensionsTest.class.getClassLoader()) {
            Class<?> load() { return defineClass(node.name.replace('/', '.'), bytes, 0, bytes.length); }
        }.load();
    }

    @Test
    void fieldAliasesRemainExecutable() throws Exception {
        ClassNode node = target("FieldAliases");
        node.fields.add(new FieldNode(Opcodes.ACC_PRIVATE, "actual", "I", null, null));
        FieldNode placeholder = new FieldNode(Opcodes.ACC_PUBLIC, "placeholder", "I", null, null);
        placeholder.visibleAnnotations = new ArrayList<>(List.of(annotation("AccessTransformer", "name", "actual", "access", Opcodes.ACC_PUBLIC)));
        node.fields.add(placeholder);
        constructor(node);
        MethodNode init = node.methods.getFirst();
        InsnList assignment = new InsnList();
        assignment.add(new VarInsnNode(Opcodes.ALOAD, 0));
        assignment.add(new IntInsnNode(Opcodes.BIPUSH, 37));
        assignment.add(new FieldInsnNode(Opcodes.PUTFIELD, node.name, "placeholder", "I"));
        init.instructions.insertBefore(init.instructions.getLast(), assignment);
        MethodNode get = method(node, Opcodes.ACC_PUBLIC, "read", "()I");
        get.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        get.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, node.name, "placeholder", "I"));
        get.instructions.add(new InsnNode(Opcodes.IRETURN));

        Class<?> type = process(node);
        Object value = type.getConstructor().newInstance();
        assertEquals(37, type.getMethod("read").invoke(value));
        assertEquals(37, type.getField("actual").getInt(value));
        assertThrows(NoSuchFieldException.class, () -> type.getDeclaredField("placeholder"));
    }

    @Test
    void signatureChangesRemainLinkable() throws Exception {
        ClassNode node = target("Signature");
        MethodNode value = method(node, Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "value", "()Ljava/lang/Object;");
        value.visibleAnnotations = new ArrayList<>(List.of(annotation("SignatureFix", "value", "()Ljava.lang.String;")));
        value.instructions.add(new LdcInsnNode("linked"));
        value.instructions.add(new InsnNode(Opcodes.ARETURN));
        MethodNode caller = method(node, Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "call", "()Ljava/lang/String;");
        caller.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, node.name, "value", "()Ljava/lang/Object;", false));
        caller.instructions.add(new InsnNode(Opcodes.ARETURN));

        Class<?> type = process(node);
        assertEquals(String.class, type.getMethod("value").getReturnType());
        assertEquals("linked", type.getMethod("call").invoke(null));
    }

    @Test
    void constructorHelpersRemainCallable() throws Exception {
        ClassNode node = target("Constructor");
        MethodNode shadow = method(node, Opcodes.ACC_PRIVATE | Opcodes.ACC_NATIVE, "superHelper", "()V");
        shadow.visibleAnnotations = new ArrayList<>(List.of(annotation("ShadowSuperConstructor")));
        MethodNode helper = method(node, Opcodes.ACC_PUBLIC, "constructHelper", "()V");
        helper.visibleAnnotations = new ArrayList<>(List.of(annotation("NewConstructor")));
        helper.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        helper.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, node.name, "superHelper", "()V", false));
        helper.instructions.add(new InsnNode(Opcodes.RETURN));
        MethodNode factory = method(node, Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "create", "()L" + node.name + ";");
        factory.instructions.add(new TypeInsnNode(Opcodes.NEW, node.name));
        factory.instructions.add(new InsnNode(Opcodes.DUP));
        factory.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, node.name, "constructHelper", "()V", false));
        factory.instructions.add(new InsnNode(Opcodes.ARETURN));

        Class<?> type = process(node);
        assertEquals(type, type.getMethod("create").invoke(null).getClass());
        assertEquals(type, type.getConstructor().newInstance().getClass());
    }

    @Test
    void staticInnerClassFlagsRemainLegal() throws Exception {
        ClassNode node = target("Outer$Inner");
        node.innerClasses.add(new InnerClassNode(node.name, "audit/Outer", "Inner", Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC));
        constructor(node);
        MethodNode transform = method(node, Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC | Opcodes.ACC_NATIVE, "classAccess", "()V");
        transform.visibleAnnotations = new ArrayList<>(List.of(annotation("AccessTransformer", "name", "<class>", "access", Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC)));

        Class<?> type = process(node);
        type.getConstructor().newInstance();
        assertTrue(Modifier.isPublic(type.getModifiers()));
        assertFalse((node.access & Opcodes.ACC_STATIC) != 0);
        assertTrue((node.innerClasses.getFirst().access & Opcodes.ACC_STATIC) != 0);
    }

    @Test
    void chainedAliasesAndDescriptorOverloadsRemainExecutable() throws Exception {
        ClassNode node = target("ChainedAliases");
        node.fields.add(new FieldNode(Opcodes.ACC_PRIVATE, "actual", "I", null, null));
        node.fields.add(new FieldNode(Opcodes.ACC_PRIVATE, "actual", "J", null, null));
        FieldNode outerInt = new FieldNode(Opcodes.ACC_PUBLIC, "outerInt", "I", null, null);
        outerInt.visibleAnnotations = new ArrayList<>(List.of(annotation("AccessTransformer", "name", "middleInt", "access", Opcodes.ACC_PUBLIC)));
        node.fields.add(outerInt);
        FieldNode middleInt = new FieldNode(Opcodes.ACC_PUBLIC, "middleInt", "I", null, null);
        middleInt.visibleAnnotations = new ArrayList<>(List.of(annotation("AccessTransformer", "name", "actual", "access", Opcodes.ACC_PUBLIC)));
        node.fields.add(middleInt);
        FieldNode longAlias = new FieldNode(Opcodes.ACC_PUBLIC, "longAlias", "J", null, null);
        longAlias.visibleAnnotations = new ArrayList<>(List.of(annotation("AccessTransformer", "name", "actual", "access", Opcodes.ACC_PUBLIC)));
        node.fields.add(longAlias);
        constructor(node);
        InsnList assignments = new InsnList();
        assignments.add(new VarInsnNode(Opcodes.ALOAD, 0));
        assignments.add(new IntInsnNode(Opcodes.BIPUSH, 23));
        assignments.add(new FieldInsnNode(Opcodes.PUTFIELD, node.name, "outerInt", "I"));
        assignments.add(new VarInsnNode(Opcodes.ALOAD, 0));
        assignments.add(new LdcInsnNode(0x102030405L));
        assignments.add(new FieldInsnNode(Opcodes.PUTFIELD, node.name, "longAlias", "J"));
        node.methods.getFirst().instructions.insertBefore(node.methods.getFirst().instructions.getLast(), assignments);
        MethodNode readInt = method(node, Opcodes.ACC_PUBLIC, "readInt", "()I");
        readInt.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        readInt.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, node.name, "outerInt", "I"));
        readInt.instructions.add(new InsnNode(Opcodes.IRETURN));
        MethodNode readLong = method(node, Opcodes.ACC_PUBLIC, "readLong", "()J");
        readLong.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        readLong.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, node.name, "longAlias", "J"));
        readLong.instructions.add(new InsnNode(Opcodes.LRETURN));

        Class<?> type = process(node);
        Object value = type.getConstructor().newInstance();
        assertEquals(23, type.getMethod("readInt").invoke(value));
        assertEquals(0x102030405L, type.getMethod("readLong").invoke(value));
        assertThrows(NoSuchFieldException.class, () -> type.getDeclaredField("outerInt"));
        assertThrows(NoSuchFieldException.class, () -> type.getDeclaredField("middleInt"));
        assertThrows(NoSuchFieldException.class, () -> type.getDeclaredField("longAlias"));
    }

    @Test
    void accessibleFieldOperationsRespectMcpAndSrgNamespaceDetection() throws Exception {
        assertAccessibleFieldOperation("McpOperation", "ordinaryMarker", "value");
        assertAccessibleFieldOperation("SrgOperation", "field_777_a", "field_123_a");
    }

    private static void assertAccessibleFieldOperation(String name, String namespaceMarker, String targetField) throws Exception {
        ClassNode node = target(name);
        node.fields.add(new FieldNode(Opcodes.ACC_PRIVATE, namespaceMarker, "I", null, null));
        node.fields.add(new FieldNode(Opcodes.ACC_PRIVATE, targetField, "I", null, null));
        FieldNode fieldAlias = new FieldNode(Opcodes.ACC_PRIVATE, "fieldAlias", "I", null, null);
        fieldAlias.visibleAnnotations = new ArrayList<>(List.of(annotation("AccessTransformer",
                "name", targetField, "access", Opcodes.ACC_PUBLIC, "deobf", true)));
        node.fields.add(fieldAlias);
        constructor(node);
        MethodNode operation = method(node, Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC | Opcodes.ACC_NATIVE,
                "readTarget", "(L" + node.name + ";)I");
        operation.visibleAnnotations = new ArrayList<>(List.of(annotation("AccessibleOperation",
                "opcode", Opcodes.GETFIELD, "desc", targetField + " I", "deobf", true)));
        MethodNode reader = method(node, Opcodes.ACC_PUBLIC, "read", "()I");
        reader.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        reader.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, node.name, "readTarget", "(L" + node.name + ";)I", false));
        reader.instructions.add(new InsnNode(Opcodes.IRETURN));
        MethodNode init = node.methods.getFirst();
        InsnList assignment = new InsnList();
        assignment.add(new VarInsnNode(Opcodes.ALOAD, 0));
        assignment.add(new IntInsnNode(Opcodes.BIPUSH, 41));
        assignment.add(new FieldInsnNode(Opcodes.PUTFIELD, node.name, targetField, "I"));
        init.instructions.insertBefore(init.instructions.getLast(), assignment);

        Class<?> type = process(node);
        assertEquals(41, type.getMethod("read").invoke(type.getConstructor().newInstance()));
        assertEquals(41, type.getField(targetField).getInt(type.getConstructor().newInstance()));
    }
    @Test
    void unannotatedClassRemainsExecutable() throws Exception {
        ClassNode node = target("NoMarkers");
        constructor(node);
        MethodNode answer = method(node, Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "answer", "()I");
        answer.instructions.add(new IntInsnNode(Opcodes.BIPUSH, 42));
        answer.instructions.add(new InsnNode(Opcodes.IRETURN));

        Class<?> type = process(node);
        assertEquals(42, type.getMethod("answer").invoke(null));
        type.getConstructor().newInstance();
    }
}
