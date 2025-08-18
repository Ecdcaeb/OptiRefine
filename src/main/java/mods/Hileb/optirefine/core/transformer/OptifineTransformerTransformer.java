package mods.Hileb.optirefine.core.transformer;

import com.google.common.collect.ImmutableList;
import mods.Hileb.optirefine.core.OptiRefineBlackboard;
import mods.Hileb.optirefine.core.OptiRefineLog;
import mods.Hileb.optirefine.library.foundationx.TransformerHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.*;
import java.net.*;
import java.util.ListIterator;

import static mods.Hileb.optirefine.library.foundationx.ASMHelper.*;


public class OptifineTransformerTransformer implements TransformerHelper.TargetedASMTransformer {

    private static final Logger LOGGER = LogManager.getLogger();

    @SuppressWarnings("unused")
    public static URI url2uri(URL url) throws IOException, URISyntaxException {
        URLConnection connection = url.openConnection();
        if (connection instanceof JarURLConnection jarURLConnection) {
            return jarURLConnection.getJarFileURL().toURI();
        } else  {
            return url.toURI();
        }
    }


    @Override
    public String[] getTargets() {
        return new String[]{
                "optifine.OptiFineClassTransformer"
        };
    }

    @Override
    public int transform(ClassNode classNode) {
        for (var mn : ImmutableList.copyOf(classNode.methods)) {
            if ("<init>".equals(mn.name)) {
                ListIterator<AbstractInsnNode> iterator = mn.instructions.iterator();
                while (iterator.hasNext()) {
                    if (iterator.next() instanceof MethodInsnNode methodInsnNode &&
                            "java/net/URL".equals(methodInsnNode.owner) &&
                            "toURI".equals(methodInsnNode.name) &&
                            "()Ljava/net/URI;".equals(methodInsnNode.desc)) {
                        iterator.set(INVOKESTATIC("mods/Hileb/optirefine/core/transformer/OptifineTransformerTransformer", "url2uri", "(Ljava/net/URL;)Ljava/net/URI;"));
                    }
                }
            } else if ("transform".equals(mn.name)) {
                mn.name = "transform0";
                MethodNode methodVisitor = new MethodNode(Opcodes.ACC_PUBLIC, "transform", mn.desc, null, null);
                Label label0 = new Label();
                methodVisitor.visitLabel(label0);
                methodVisitor.visitVarInsn(ALOAD, 2);
                methodVisitor.visitMethodInsn(INVOKESTATIC, "mods/Hileb/optirefine/core/transformer/OptifineTransformerTransformer", "couldNotTransform", "(Ljava/lang/String;)Z", false);
                Label label1 = new Label();
                methodVisitor.visitJumpInsn(IFEQ, label1);
                Label label2 = new Label();
                methodVisitor.visitLabel(label2);
                methodVisitor.visitVarInsn(ALOAD, 3);
                methodVisitor.visitInsn(ARETURN);
                methodVisitor.visitLabel(label1);
                methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                methodVisitor.visitVarInsn(ALOAD, 0);
                methodVisitor.visitVarInsn(ALOAD, 1);
                methodVisitor.visitVarInsn(ALOAD, 2);
                methodVisitor.visitVarInsn(ALOAD, 3);
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "optifine/OptiFineClassTransformer", "transform0", mn.desc, false);
                methodVisitor.visitInsn(ARETURN);
                Label label3 = new Label();
                methodVisitor.visitLabel(label3);
                methodVisitor.visitLocalVariable("this", "Loptifine/OptiFineClassTransformer;", null, label0, label3, 0);
                methodVisitor.visitLocalVariable("name", "Ljava/lang/String;", null, label0, label3, 1);
                methodVisitor.visitLocalVariable("transformedName", "Ljava/lang/String;", null, label0, label3, 2);
                methodVisitor.visitLocalVariable("bytes", "[B", null, label0, label3, 3);
                methodVisitor.visitMaxs(4, 4);
                LOGGER.info("OptiRefine hijacked the OptifineClassTransformer");
                classNode.methods.add(methodVisitor);
            }
        }
        return ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS;
    }

    public static boolean couldNotTransform(String transformedName) {
        LOGGER.debug("Optifine try class {}", transformedName);
        if (OptiRefineBlackboard.isOverwritePatches(transformedName)) {
            LOGGER.debug("Optifine skipped class {}", transformedName);
            return true;
        }
        else return false;
    }
}
