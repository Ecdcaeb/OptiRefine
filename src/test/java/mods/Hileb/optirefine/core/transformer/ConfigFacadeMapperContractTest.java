package mods.Hileb.optirefine.core.transformer;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.ConstantDynamic;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.function.IntSupplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigFacadeMapperContractTest implements Opcodes {
    private static final String FACADE = "mods/Hileb/optirefine/optifine/Config";
    private static final String EXECUTION_HELPER = "mods/Hileb/optirefine/helpers/ConfigFacadeExecutionContract";

    @Test
    void remappedHelperAndLambdaExecuteAgainstGeneratedDefaultConfig() throws Exception {
        byte[] helperBytes = executionHelper();
        byte[] remapped = new OptiRefineConfigTransformer().transform(
                EXECUTION_HELPER, EXECUTION_HELPER.replace('/', '.'), helperBytes);
        ClassNode remappedNode = new ClassNode();
        new ClassReader(remapped).accept(remappedNode, 0);
        ConfigFacadeMapper.verify(remappedNode);

        DefiningClassLoader loader = new DefiningClassLoader();
        loader.define("Config", runtimeConfig());
        Class<?> helperClass = loader.define(EXECUTION_HELPER.replace('/', '.'), remapped);

        assertEquals(41, helperClass.getMethod("direct").invoke(null));
        IntSupplier lambda = (IntSupplier) helperClass.getMethod("lambda").invoke(null);
        assertEquals(41, lambda.getAsInt());
        assertEquals(FACADE, helperClass.getMethod("unrelatedText").invoke(null));
    }

    @Test
    void compileFacadeDefinitionIsReturnedUnchanged() {
        ClassWriter writer = new ClassWriter(0);
        writer.visit(V1_8, ACC_PUBLIC, FACADE, null, "java/lang/Object", null);
        writer.visitEnd();
        byte[] facadeDefinition = writer.toByteArray();

        byte[] transformed = new OptiRefineConfigTransformer().transform(
                FACADE, FACADE.replace('/', '.'), facadeDefinition);
        assertSame(facadeDefinition, transformed);

    }

    @Test
    void unrelatedFacadeTextWithoutTypedReferencesPassesThroughUnparsed() {
        ClassWriter writer = new ClassWriter(0);
        String helperName = "mods/Hileb/optirefine/helpers/TextOnly";
        writer.visit(V1_8, ACC_PUBLIC, helperName, null, "java/lang/Object", null);
        org.objectweb.asm.MethodVisitor method = writer.visitMethod(
                ACC_PUBLIC | ACC_STATIC, "text", "()Ljava/lang/String;", null, null);
        method.visitCode();
        method.visitLdcInsn(FACADE);
        method.visitInsn(ARETURN);
        method.visitMaxs(1, 0);
        method.visitEnd();
        writer.visitEnd();
        byte[] original = writer.toByteArray();

        byte[] transformed = new OptiRefineConfigTransformer().transform(
                helperName, helperName.replace('/', '.'), original);
        assertSame(original, transformed);
    }

    @Test
    void remapsTypedClassMemberAnnotationSignatureAndConstantReferencesInPlace() {
        ClassNode node = new ClassNode();
        node.version = V11;
        node.access = ACC_PUBLIC;
        node.name = "mods/Hileb/optirefine/helpers/TypedReferences";
        node.superName = "java/lang/Object";
        node.signature = "Ljava/lang/Object;L" + FACADE + ";";
        node.visibleAnnotations = new java.util.ArrayList<>();
        node.visibleAnnotations.add(annotation(FACADE));

        FieldNode field = new FieldNode(ACC_PUBLIC, "value", "L" + FACADE + ";", "L" + FACADE + ";", null);
        field.visibleAnnotations = new java.util.ArrayList<>();
        field.visibleAnnotations.add(annotation(FACADE));
        node.fields.add(field);

        MethodNode method = new MethodNode(ACC_PUBLIC | ACC_STATIC, "typed", "(L" + FACADE + ";)L" + FACADE + ";",
                "(L" + FACADE + ";)L" + FACADE + ";", new String[]{FACADE});
        method.instructions.add(new org.objectweb.asm.tree.TypeInsnNode(NEW, FACADE));
        method.instructions.add(new org.objectweb.asm.tree.InsnNode(POP));
        method.instructions.add(new LdcInsnNode(Type.getObjectType(FACADE)));
        method.instructions.add(new org.objectweb.asm.tree.InsnNode(POP));
        method.instructions.add(new LdcInsnNode(new Handle(H_INVOKESTATIC, FACADE, "value", "()I", false)));
        method.instructions.add(new org.objectweb.asm.tree.InsnNode(POP));
        Handle bootstrap = new Handle(H_INVOKESTATIC, "java/lang/invoke/ConstantBootstraps", "nullConstant",
                "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;", false);
        method.instructions.add(new LdcInsnNode(new ConstantDynamic("nested", "Ljava/lang/Object;", bootstrap,
                Type.getObjectType(FACADE))));
        method.instructions.add(new org.objectweb.asm.tree.InsnNode(POP));
        method.instructions.add(new org.objectweb.asm.tree.InsnNode(ACONST_NULL));
        method.instructions.add(new org.objectweb.asm.tree.InsnNode(ARETURN));
        method.maxStack = 1;
        method.maxLocals = 1;
        node.methods.add(method);

        ClassNode identity = node;
        ConfigFacadeMapper.remap(node);
        assertSame(identity, node);
        assertEquals("Ljava/lang/Object;LConfig;", node.signature);
        assertEquals("LConfig;", node.visibleAnnotations.get(0).desc);
        assertEquals("LConfig;", node.fields.get(0).desc);
        assertEquals("(LConfig;)LConfig;", node.methods.get(0).desc);
        ConfigFacadeMapper.verify(node);
    }

    @Test
    void verifierReportsResidualDescriptorAndTypeConstantsWithClassContext() {
        ClassNode descriptorNode = new ClassNode();
        descriptorNode.name = "mods/Hileb/optirefine/helpers/ResidualDescriptor";
        descriptorNode.superName = "java/lang/Object";
        descriptorNode.fields.add(new FieldNode(ACC_PUBLIC, "residual", "L" + FACADE + ";", null, null));
        IllegalStateException descriptorFailure = assertThrows(IllegalStateException.class,
                () -> ConfigFacadeMapper.verify(descriptorNode));
        assertTrue(descriptorFailure.getMessage().contains(descriptorNode.name));
        assertTrue(descriptorFailure.getMessage().contains(FACADE));

        ClassNode typeNode = new ClassNode();
        typeNode.name = "mods/Hileb/optirefine/helpers/ResidualType";
        typeNode.superName = "java/lang/Object";
        MethodNode method = new MethodNode(ACC_PUBLIC | ACC_STATIC, "residual", "()V", null, null);
        method.instructions.add(new LdcInsnNode(Type.getObjectType(FACADE)));
        method.instructions.add(new org.objectweb.asm.tree.InsnNode(POP));
        method.instructions.add(new org.objectweb.asm.tree.InsnNode(RETURN));
        method.maxStack = 1;
        method.maxLocals = 0;
        typeNode.methods.add(method);
        IllegalStateException typeFailure = assertThrows(IllegalStateException.class,
                () -> ConfigFacadeMapper.verify(typeNode));
        assertTrue(typeFailure.getMessage().contains(typeNode.name));
        assertTrue(typeFailure.getMessage().contains(FACADE));
    }

    @Test
    void verifierTraversesMethodBodiesHandlesAndNestedAnnotations() {
        ClassNode bodyNode = classNode("mods/Hileb/optirefine/helpers/ResidualBody");
        MethodNode bodyMethod = new MethodNode(ACC_PUBLIC | ACC_STATIC, "body", "()V", null, null);
        bodyMethod.instructions.add(new org.objectweb.asm.tree.MethodInsnNode(
                INVOKESTATIC, FACADE, "value", "()I", false));
        bodyNode.methods.add(bodyMethod);
        assertResidual(bodyNode);

        ClassNode handleNode = classNode("mods/Hileb/optirefine/helpers/ResidualHandle");
        MethodNode handleMethod = new MethodNode(ACC_PUBLIC | ACC_STATIC, "handle", "()V", null, null);
        handleMethod.instructions.add(new LdcInsnNode(
                new Handle(H_INVOKESTATIC, FACADE, "value", "()I", false)));
        handleNode.methods.add(handleMethod);
        assertResidual(handleNode);

        ClassNode annotationNode = classNode("mods/Hileb/optirefine/helpers/ResidualAnnotation");
        annotationNode.visibleAnnotations = new java.util.ArrayList<>();
        org.objectweb.asm.tree.AnnotationNode outer = new org.objectweb.asm.tree.AnnotationNode("Lexample/Outer;");
        outer.values = new java.util.ArrayList<>();
        outer.values.add("child");
        org.objectweb.asm.tree.AnnotationNode nested = new org.objectweb.asm.tree.AnnotationNode("Lexample/Nested;");
        nested.values = new java.util.ArrayList<>();
        nested.values.add("type");
        nested.values.add(Type.getObjectType(FACADE));
        outer.values.add(nested);
        annotationNode.visibleAnnotations.add(outer);
        assertResidual(annotationNode);
    }

    @Test
    void remapLeavesReferenceFreeClassContentsUntouched() {
        ClassNode node = classNode("mods/Hileb/optirefine/helpers/NoFacadeReference");
        node.fields.add(new FieldNode(ACC_PRIVATE, "value", "Ljava/lang/Object;", null, null));
        node.methods.add(new MethodNode(ACC_PRIVATE, "helper", "()V", null, null));
        java.util.List<FieldNode> fields = node.fields;
        java.util.List<MethodNode> methods = node.methods;

        ConfigFacadeMapper.remap(node);

        assertSame(fields, node.fields);
        assertSame(methods, node.methods);
    }

    private static ClassNode classNode(String name) {
        ClassNode node = new ClassNode();
        node.name = name;
        node.superName = "java/lang/Object";
        return node;
    }

    private static void assertResidual(ClassNode node) {
        IllegalStateException failure = assertThrows(IllegalStateException.class,
                () -> ConfigFacadeMapper.verify(node));
        assertTrue(failure.getMessage().contains(node.name));
        assertTrue(failure.getMessage().contains(FACADE));
    }

    private static org.objectweb.asm.tree.AnnotationNode annotation(String descriptorType) {
        org.objectweb.asm.tree.AnnotationNode annotation = new org.objectweb.asm.tree.AnnotationNode("L" + descriptorType + ";");
        annotation.values = new java.util.ArrayList<>();
        annotation.values.add("value");
        annotation.values.add(Type.getObjectType(descriptorType));
        return annotation;
    }

    private static byte[] executionHelper() {
        ClassWriter writer = new ClassWriter(0);
        writer.visit(V1_8, ACC_PUBLIC | ACC_FINAL, EXECUTION_HELPER, null, "java/lang/Object", null);

        org.objectweb.asm.MethodVisitor init = writer.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        init.visitCode();
        init.visitVarInsn(ALOAD, 0);
        init.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        init.visitInsn(RETURN);
        init.visitMaxs(1, 1);
        init.visitEnd();

        org.objectweb.asm.MethodVisitor direct = writer.visitMethod(ACC_PUBLIC | ACC_STATIC, "direct", "()I", null, null);
        direct.visitCode();
        direct.visitMethodInsn(INVOKESTATIC, FACADE, "value", "()I", false);
        direct.visitInsn(IRETURN);
        direct.visitMaxs(1, 0);
        direct.visitEnd();

        org.objectweb.asm.MethodVisitor lambda = writer.visitMethod(ACC_PUBLIC | ACC_STATIC, "lambda", "()Ljava/util/function/IntSupplier;", null, null);
        lambda.visitCode();
        Handle metafactory = new Handle(H_INVOKESTATIC, "java/lang/invoke/LambdaMetafactory", "metafactory",
                "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;"
                        + "Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;", false);
        lambda.visitInvokeDynamicInsn("getAsInt", "()Ljava/util/function/IntSupplier;", metafactory,
                Type.getMethodType("()I"), new Handle(H_INVOKESTATIC, FACADE, "value", "()I", false),
                Type.getMethodType("()I"));
        lambda.visitInsn(ARETURN);
        lambda.visitMaxs(1, 0);
        lambda.visitEnd();

        org.objectweb.asm.MethodVisitor string = writer.visitMethod(ACC_PUBLIC | ACC_STATIC, "unrelatedText", "()Ljava/lang/String;", null, null);
        string.visitCode();
        string.visitLdcInsn(FACADE);
        string.visitInsn(ARETURN);
        string.visitMaxs(1, 0);
        string.visitEnd();
        writer.visitEnd();
        return writer.toByteArray();
    }

    private static byte[] runtimeConfig() {
        ClassWriter writer = new ClassWriter(0);
        writer.visit(V1_8, ACC_PUBLIC | ACC_FINAL, "Config", null, "java/lang/Object", null);
        org.objectweb.asm.MethodVisitor value = writer.visitMethod(ACC_PUBLIC | ACC_STATIC, "value", "()I", null, null);
        value.visitCode();
        value.visitIntInsn(BIPUSH, 41);
        value.visitInsn(IRETURN);
        value.visitMaxs(1, 0);
        value.visitEnd();
        writer.visitEnd();
        return writer.toByteArray();
    }

    private static final class DefiningClassLoader extends ClassLoader {
        private DefiningClassLoader() {
            super(ConfigFacadeMapperContractTest.class.getClassLoader());
        }

        private Class<?> define(String name, byte[] bytes) {
            return defineClass(name, bytes, 0, bytes.length);
        }
    }

}
