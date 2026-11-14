package mods.Hileb.optirefine.library.cursedmixinextensions;

import mods.Hileb.optirefine.core.OptiRefineLog;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.*;
import mods.Hileb.optirefine.library.cursedmixinextensions.util.CallTransformTask;
import mods.Hileb.optirefine.library.foundationx.asm.NonLoadingClassWriter;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.TraceClassVisitor;
import org.spongepowered.asm.util.Annotations;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Predicate;

/**
 * @author LlamaLad7
 * @author Cat Core
 * @author Hileb
 */

@SuppressWarnings("unused")
public class CursedMixinExtensions {
    
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static void postApply(ClassNode targetClass) {
        LOGGER.debug("OptiRefine for !!! {}", targetClass.name);

        LinkedList<CallTransformTask> tasks = new LinkedList<>();
        boolean namespaceKnown = false;
        boolean srgRuntime = false;

        AnnotationNode changeSuperClass = Annotations.getVisible(targetClass, ChangeSuperClass.class);
        if (changeSuperClass != null) {
            String oldOwner = targetClass.superName;
            targetClass.superName = Annotations.<Type>getValue(changeSuperClass).getInternalName();
            tasks.add((instructions, call) -> {
                if (call.getOpcode() == Opcodes.INVOKESPECIAL && call.owner.equals(oldOwner)) {
                    call.owner = targetClass.superName;
                }
            });
        }

        AnnotationNode implementsItf = Annotations.getVisible(targetClass, Implements.class);
        if (implementsItf != null) {
            List<Type> targetToAdd = Annotations.getValue(implementsItf, "value");
            if (targetToAdd != null) {
                for (Type type : targetToAdd) {
                    if (!targetClass.interfaces.contains(type.getInternalName())) {
                        targetClass.interfaces.add(type.getInternalName());
                    }
                }
            }

            List<String> targetToAddStr = Annotations.getValue(implementsItf, "itfs");
            if (targetToAddStr != null) {
                for (String type : targetToAddStr) {
                    type = type.replace('.', '/');
                    if (!targetClass.interfaces.contains(type)) {
                        targetClass.interfaces.add(type);
                    }
                }
            }

            List<Type> targetToRemove = Annotations.getValue(implementsItf, "removes");
            if (targetToRemove != null) {
                for (Type type : targetToRemove) {
                    targetClass.interfaces.remove(type.getInternalName());
                }
            }

            List<String> targetToRemoveStr = Annotations.getValue(implementsItf, "removed");
            if (targetToRemoveStr != null) {
                for (String type : targetToRemoveStr) {
                    targetClass.interfaces.remove(type.replace('.', '/'));
                }
            }


        }

        List<String> ctrToReplace = new ArrayList<>();
        LinkedHashSet<MethodNode> methodToRemove = new LinkedHashSet<>();

        for (MethodNode method : targetClass.methods) {
            boolean remove = false;
            final String originalName = method.name;
            final String originalDesc = method.desc;

            AnnotationNode signFix = Annotations.getVisible(method, SignatureFix.class);
            if (signFix != null) {
                String descriptor = ((String) Annotations.getValue(signFix)).replace('.', '/');
                Type returnType = Type.getReturnType(descriptor);
                if (!Arrays.equals(Type.getArgumentTypes(originalDesc), Type.getArgumentTypes(descriptor))) {
                    throw new IllegalArgumentException("SignatureFix cannot change method arguments: " + targetClass.name + "." + originalName);
                }
                if (!Type.getReturnType(originalDesc).equals(returnType)) {
                    if (returnType.getSort() != Type.OBJECT && returnType.getSort() != Type.ARRAY) {
                        throw new IllegalArgumentException("SignatureFix requires a reference return type: " + targetClass.name + "." + originalName);
                    }
                    for (AbstractInsnNode insn : method.instructions.toArray()) {
                        if (insn.getOpcode() == Opcodes.ARETURN) {
                            method.instructions.insertBefore(insn, new TypeInsnNode(Opcodes.CHECKCAST, returnType.getInternalName()));
                        }
                    }
                }
                method.desc = descriptor;
                method.signature = null;
                tasks.add((instructions, call) -> {
                    if (call.owner.equals(targetClass.name) && call.name.equals(originalName) && call.desc.equals(originalDesc)) {
                        call.desc = descriptor;
                    }
                });
            }

            if (Annotations.getVisible(method, ShadowSuperConstructor.class) != null) {
                LOGGER.debug("ShadowSuperConstructor Instance, {}", method.name);
                methodToRemove.add(method);
                tasks.add((instructions, call) -> {
                    if (call.owner.equals(targetClass.name) && call.name.equals(originalName) && call.desc.equals(originalDesc)) {
                        call.setOpcode(Opcodes.INVOKESPECIAL);
                        call.name = "<init>";
                        call.owner = targetClass.superName;
                    }
                });
            }

            if (Annotations.getVisible(method, ShadowConstructor.class) != null) {
                LOGGER.debug("ShadowConstructor Instance, {}", method.name);
                methodToRemove.add(method);
                tasks.add((instructions, call) -> {
                    if (call.owner.equals(targetClass.name) && call.name.equals(originalName) && call.desc.equals(originalDesc)) {
                        call.setOpcode(Opcodes.INVOKESPECIAL);
                        call.name = "<init>";
                    }
                });
            }

            AnnotationNode superMethod = Annotations.getVisible(method, ShadowSuper.class);
            if (superMethod != null) {
                LOGGER.debug("ShadowSuper Instance, {}", method.name);
                methodToRemove.add(method);
                String targetName = Annotations.getValue(superMethod);

                tasks.add((instructions, call) -> {
                    if (call.owner.equals(targetClass.name) && call.name.equals(originalName) && call.desc.equals(originalDesc)) {
                        call.setOpcode(Opcodes.INVOKESPECIAL);
                        call.name = targetName;
                        call.owner = targetClass.superName;
                    }
                });

            }

            if (Annotations.getVisible(method, Public.class) != null) {
                LOGGER.debug("Public Instance, {}", method.name);
                method.access |= Opcodes.ACC_PUBLIC;
                method.access &= ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED);
            }

            if (Annotations.getVisible(method, NewConstructor.class) != null) {
                LOGGER.debug("NewConstructor Instance, {}", method.name);
                tasks.add((instructions, call) -> {
                    if (call.owner.equals(targetClass.name) && call.name.equals(originalName) && call.desc.equals(originalDesc)) {
                        call.setOpcode(Opcodes.INVOKESPECIAL);
                        call.name = "<init>";
                    }
                });
                method.name = "<init>";
            }

            if (Annotations.getVisible(method, ReplaceConstructor.class) != null) {
                LOGGER.debug("ReplaceConstructor Instance, {}", method.name);
                ctrToReplace.add(method.desc);
                tasks.add((instructions, call) -> {
                    if (call.owner.equals(targetClass.name) && call.name.equals(originalName) && call.desc.equals(originalDesc)) {
                        call.setOpcode(Opcodes.INVOKESPECIAL);
                        call.name = "<init>";
                    }
                });
                method.name = "<init>";
            }

            AnnotationNode accessTransform = Annotations.getVisible(method, AccessTransformer.class);
            if (accessTransform != null) {
                LOGGER.debug("Accessible Transformer Instance, {}", method.name);
                methodToRemove.add(method);
                int access = Annotations.getValue(accessTransform, "access", -1);
                String name = Annotations.getValue(accessTransform, "name");
                if(name != null) name = name.replace('.', '/');
                boolean deobf = Annotations.getValue(accessTransform, "deobf", Boolean.FALSE);

                if ("<class>".equals(name)) {
                    if (access >= 0) {
                        targetClass.access = (access & ~(Opcodes.ACC_STATIC | Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED))
                                | (targetClass.access & Opcodes.ACC_SUPER);
                        for (InnerClassNode innerClass : targetClass.innerClasses) {
                            if (innerClass.name.equals(targetClass.name)) innerClass.access = access;
                        }
                    }
                } else {
                    String desc = method.desc;
                    if (access == -1) access = method.access;
                    if (name == null) name = method.name;

                    String rawName = name;
                    String rawDesc = desc;
                    if (deobf) {
                        name = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(targetClass.name, name, desc);
                        desc = FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc);
                    }

                    for (MethodNode mn : targetClass.methods) {
                        if (mn.name.equals(name) && mn.desc.equals(desc)) {
                            mn.access = access;
                        }
                        // deobf=true: also match the raw (SRG) name for SRG runtimes
                        if (deobf && mn.name.equals(rawName) && mn.desc.equals(rawDesc)) {
                            mn.access = access;
                        }
                    }
                }

            }

            AnnotationNode accessibleOperation = Annotations.getVisible(method, AccessibleOperation.class);
            if (accessibleOperation != null) {
                LOGGER.debug("Accessible Operation Instance, {}", method.name);
                boolean isBuildIn = Annotations.getVisible(method, AccessibleOperation.BuildIn.class) != null;
                methodToRemove.add(method);
                int opcodes = Annotations.getValue(accessibleOperation, "opcode", Opcodes.NOP);
                String desc = Annotations.getValue(accessibleOperation, "desc");
                if(desc != null) desc = desc.replace('.','/');
                boolean itf = Annotations.getValue(accessibleOperation, "itf", Boolean.FALSE);
                boolean deobf = Annotations.getValue(accessibleOperation, "deobf", Boolean.FALSE);
                if (deobf && !namespaceKnown) {
                    srgRuntime = isSrgRuntime(targetClass);
                    namespaceKnown = true;
                }
                final boolean useDeobf = deobf && !srgRuntime;

                if (isBuildIn) {
                    method.access = method.access & ~(Opcodes.ACC_NATIVE | Opcodes.ACC_ABSTRACT);
                }
                if (opcodes == Opcodes.NOP) {
                    LOGGER.debug("AccessibleOperation {} : NOP", targetClass.name);
                    if (isBuildIn) {
                        String rt = Type.getMethodType(desc).getReturnType().toString();
                        if (rt.startsWith("L")) {
                            method.visitInsn(Opcodes.ARETURN);
                        } else if ("J".equals(rt)) {
                            method.visitInsn(Opcodes.LRETURN);
                        } else if ("D".equals(rt)) {
                            method.visitInsn(Opcodes.DRETURN);
                        } else if ("F".equals(rt)) {
                            method.visitInsn(Opcodes.FRETURN);
                        } else if ("V".equals(rt)) {
                            method.visitInsn(Opcodes.RETURN);
                        } else method.visitInsn(Opcodes.IRETURN);
                    } else {
                        tasks.add((instructions, call) -> {
                            if (call.owner.equals(targetClass.name) && call.name.equals(method.name) && call.desc.equals(method.desc)) {
                                instructions.remove(call);
                            }
                        });
                    }
                } else if (opcodes == Opcodes.NEW) {
                    String[] str = desc.split(" ");
                    final String owner;
                    final String name = "<init>";
                    final String _opt_desc;
                    if (str.length == 2) {
                        if (useDeobf) {
                            owner = FMLDeobfuscatingRemapper.INSTANCE.map(str[0]);
                            _opt_desc = FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(str[1]);
                        } else {
                            owner = str[0];
                            _opt_desc = str[1];
                        }
                    } else if (str.length == 1) {
                        if (useDeobf) {
                            owner = targetClass.name;
                            _opt_desc = FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(str[0]);
                        } else {
                            owner = targetClass.name;
                            _opt_desc = str[0];
                        }
                    } else {
                        owner = null;
                        _opt_desc = null;
                    }

                    LOGGER.debug("AccessibleOperation {} : NEW : {}, {}, {}", targetClass.name, owner, "<init>", _opt_desc);
                    tasks.add((instructions, call) -> {
                        if (call.owner.equals(targetClass.name) && call.name.equals(method.name) && call.desc.equals(method.desc)) {
                            AbstractInsnNode construction = findPreviousNode(call, (n)-> n instanceof MethodInsnNode methodInsnNode &&
                                    "mods/Hileb/optirefine/library/cursedmixinextensions/annotations/AccessibleOperation$Construction".equals(methodInsnNode.owner) &&
                                    "construction".equals(methodInsnNode.name));
                            InsnList insnNodes = new InsnList();
                            insnNodes.add(new TypeInsnNode(Opcodes.NEW, owner.replace('.', '/')));
                            insnNodes.add(new InsnNode(Opcodes.DUP));
                            instructions.insertBefore(construction, insnNodes);
                            instructions.remove(construction);
                            call.owner = owner.replace('.', '/');
                            call.name = "<init>";
                            call.desc = _opt_desc.replace('.', '/');
                            call.itf = false;
                            call.setOpcode(Opcodes.INVOKESPECIAL);
                        }
                    });

                } else if (opcodes == Opcodes.INSTANCEOF || opcodes == Opcodes.CHECKCAST) {
                    final String owner;
                    if (useDeobf) {
                        owner = FMLDeobfuscatingRemapper.INSTANCE.map(desc);
                    } else owner = desc;
                    LOGGER.debug("AccessibleOperation {} : INSTANCEOF : {}", targetClass.name, owner);
                    tasks.add((instructions, call) -> {
                        if (call.owner.equals(targetClass.name) && call.name.equals(method.name) && call.desc.equals(method.desc)) {
                            instructions.insert(call, new TypeInsnNode(opcodes, owner.replace('.', '/')));
                            instructions.remove(call);
                        }
                    });
                } else {
                    String[] str = desc.split(" ");
                    final int opc = opcodes;
                    final String owner;
                    final String name;
                    final String _opt_desc;
                    final boolean itf_ = itf;

                    if (opcodes >= Opcodes.GETSTATIC && opcodes <= Opcodes.PUTFIELD) {
                        if (str.length == 2) {
                            if (useDeobf) {
                                owner = FMLDeobfuscatingRemapper.INSTANCE.map(targetClass.name);
                                name = FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(targetClass.name, str[0], str[1]);
                                _opt_desc = FMLDeobfuscatingRemapper.INSTANCE.mapDesc(str[1]);
                            } else {
                                owner = targetClass.name;
                                name = str[0];
                                _opt_desc = str[1];
                            }
                        } else if (str.length == 3) {
                            if (useDeobf) {
                                owner = FMLDeobfuscatingRemapper.INSTANCE.map(str[0]);
                                name = FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(str[0], str[1], str[2]);
                                _opt_desc = FMLDeobfuscatingRemapper.INSTANCE.mapDesc(str[2]);
                            } else {
                                owner = str[0];
                                name = str[1];
                                _opt_desc = str[2];
                            }
                        } else {
                            owner = null;
                            name = null;
                            _opt_desc = null;
                        }
                    } else if (opcodes >= Opcodes.INVOKEVIRTUAL && opcodes <= Opcodes.INVOKEDYNAMIC) {
                        if (str.length == 2) {
                            if (useDeobf) {
                                owner = FMLDeobfuscatingRemapper.INSTANCE.map(targetClass.name);
                                name = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(targetClass.name, str[0], str[1]);
                                _opt_desc = FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(str[1]);
                            } else {
                                owner = targetClass.name;
                                name = str[0];
                                _opt_desc = str[1];
                            }
                        } else if (str.length == 3) {
                            if (useDeobf) {
                                owner = FMLDeobfuscatingRemapper.INSTANCE.map(str[0]);
                                name = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(str[0], str[1], str[2]);
                                _opt_desc = FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(str[2]);
                            } else {
                                owner = str[0];
                                name = str[1];
                                _opt_desc = str[2];
                            }
                        } else {
                            owner = null;
                            name = null;
                            _opt_desc = null;
                        }
                    } else {
                        owner = null;
                        name = null;
                        _opt_desc = null;
                    }

                    LOGGER.debug("AccessibleOperation {} : FIELD : {}, {}, {}", targetClass.name, owner, name, _opt_desc);
                    if (opcodes >= Opcodes.GETSTATIC && opcodes <= Opcodes.PUTFIELD) {
                        tasks.add((instructions, call) -> {
                            if (call.owner.equals(targetClass.name) && call.name.equals(method.name) && call.desc.equals(method.desc)) {
                                instructions.insert(call, new FieldInsnNode(opc, owner.replace('.', '/'), name.replace('.', '/'), _opt_desc.replace('.', '/')));
                                instructions.remove(call);
                            }
                        });
                    } else if (opcodes >= Opcodes.INVOKEVIRTUAL && opcodes <= Opcodes.INVOKEDYNAMIC) {
                        LOGGER.debug("AccessibleOperation {} : METHOD : {}, {}, {}", targetClass.name, owner, name, _opt_desc);
                        tasks.add((instructions, call) -> {
                            if (call.owner.equals(targetClass.name) && call.name.equals(method.name) && call.desc.equals(method.desc)) {
                                call.owner = owner.replace('.', '/');
                                call.name = name.replace('.', '/');
                                call.desc = _opt_desc.replace('.', '/');
                                call.itf = itf_;
                                call.setOpcode(opc);
                            }
                        });
                    }
                }
            }
        }

        for(MethodNode methodNode : methodToRemove) {
            LOGGER.debug("REMOVE METHOD FOR {} {}", targetClass.name, methodNode.name);
            targetClass.methods.remove(methodNode);
        }

        if (!ctrToReplace.isEmpty()) {
            for (ListIterator<MethodNode> it = targetClass.methods.listIterator(); it.hasNext(); ) {
                MethodNode method = it.next();

                if (Annotations.getVisible(method, ReplaceConstructor.class) == null) {
                    if (Objects.equals(method.name, "<init>") && ctrToReplace.contains(method.desc)) {
                        it.remove();
                    }
                }
            }
        }

        record FieldReferenceReplacement(String sourceName, String sourceDesc, String targetName, String targetDesc) {}
        List<FieldReferenceReplacement> fieldReferenceReplacements = null;
        Iterator<FieldNode> fieldNodeIterator = targetClass.fields.iterator();
        while (fieldNodeIterator.hasNext()){
            FieldNode field = fieldNodeIterator.next();

            if (Annotations.getVisible(field, Public.class) != null) {
                field.access |= Opcodes.ACC_PUBLIC;
                field.access &= ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED);
            }
            AnnotationNode accesstrasnformer = Annotations.getVisible(field, AccessTransformer.class);
            if (accesstrasnformer != null) {
                fieldNodeIterator.remove();
                int access = Annotations.getValue(accesstrasnformer, "access", -1);
                String name = Annotations.getValue(accesstrasnformer, "name");
                boolean deobf = Annotations.getValue(accesstrasnformer, "deobf", Boolean.FALSE);

                String desc = field.desc;
                if (access == -1) access = field.access;
                if (name == null) name = field.name;

                String rawName = name;
                String rawDesc = desc;
                if(deobf) {
                    name = FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(targetClass.name, name, desc);
                    desc = FMLDeobfuscatingRemapper.INSTANCE.mapDesc(desc);
                }

                for (FieldNode mn : targetClass.fields) {
                    if ((mn.name.equals(name) && mn.desc.equals(desc))
                            || (deobf && mn.name.equals(rawName) && mn.desc.equals(rawDesc))) {
                        mn.access = access;
                        if (!field.name.equals(mn.name) || !field.desc.equals(mn.desc)) {
                            if (fieldReferenceReplacements == null) {
                                fieldReferenceReplacements = new ArrayList<>();
                            }
                            fieldReferenceReplacements.add(new FieldReferenceReplacement(
                                    field.name, field.desc, mn.name, mn.desc));
                        }
                    }
                }
            }
        }

        if (fieldReferenceReplacements != null) {
            for (MethodNode method : targetClass.methods) {
                for (AbstractInsnNode instruction : method.instructions) {
                    if (instruction instanceof FieldInsnNode reference
                            && reference.owner.equals(targetClass.name)) {
                        for (FieldReferenceReplacement replacement : fieldReferenceReplacements) {
                            if (reference.name.equals(replacement.sourceName())
                                    && reference.desc.equals(replacement.sourceDesc())) {
                                reference.name = replacement.targetName();
                                reference.desc = replacement.targetDesc();
                            }
                        }
                    }
                }
            }
        }

        transformCalls(targetClass, tasks.toArray(CallTransformTask[]::new));

        if (DUMP) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(byteArrayOutputStream);
            try{
                ClassWriter classWriter = new NonLoadingClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

                targetClass.accept(new TraceClassVisitor(new ClassVisitor(Opcodes.ASM9, classWriter) {
                    @Override
                    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                        printStream.printf("NEW CLASS %s %s", name, superName);
                        super.visit(version, access, name, signature, superName, interfaces);
                    }

                    @Override
                    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                        printStream.printf("NEW METHOD %s %s", name, descriptor);
                        try {
                            return super.visitMethod(access, name, descriptor, signature, exceptions);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace(printStream);
                            return new MethodVisitor(Opcodes.ASM9, null) {};
                        }
                    }
                }, new ASMifier(), new PrintWriter(printStream)));
                classWriter.toByteArray();
            }catch (Throwable t) {
                t.printStackTrace(printStream);
                System.err.print(byteArrayOutputStream.toByteArray());
            }
        }
    }

    /**
     * Writes the fully post-processed class bytes to &lt;Launch.minecraftHome&gt;/.klass/&lt;class&gt;.class
     * when foundation.dump is enabled, after all postprocessors have completed.
     */
    public static void dumpFinalClass(ClassNode classNode) {
        if (!DUMP) {
            return;
        }
        try {
            File home = Launch.minecraftHome;
            if (home == null) {
                return;
            }
            File out = new File(new File(home, ".klass"), classNode.name + ".class");
            out.getParentFile().mkdirs();
            ClassWriter writer = new ClassWriter(0);
            classNode.accept(writer);
            Files.write(out.toPath(), writer.toByteArray());
        } catch (Throwable t) {
            LOGGER.warn("OptiRefine: failed to dump post-apply class {}", classNode.name, t);
        }
    }

    private static void transformCalls(ClassNode classNode, CallTransformTask... tasks) {
        for (MethodNode method : classNode.methods) {
            List<MethodInsnNode> calls = new ArrayList<>();
            for (AbstractInsnNode insn = method.instructions.getFirst(); insn != null; insn = insn.getNext()) {
                if (insn instanceof MethodInsnNode mi) calls.add(mi);
            }
            for (CallTransformTask task : tasks) {
                for (MethodInsnNode call : calls) {
                    if (call.getPrevious() != null || call.getNext() != null || method.instructions.getFirst() == call) {
                        task.transform(method.instructions, call);
                    }
                }
            }
        }
    }

    private static final boolean DUMP = Boolean.parseBoolean(System.getProperty("foundation.dump", "false"));


    /**
     * Detects whether the runtime uses SRG member names (production/cleanroom SRG runtime)
     * or MCP member names (devrun / MCP runtime).
     */
    private static boolean isSrgRuntime(ClassNode targetClass) {
        for (FieldNode field : targetClass.fields) {
            if (isSrgName(field.name, "field_")) {
                return true;
            }
        }
        for (MethodNode method : targetClass.methods) {
            if (isSrgName(method.name, "func_")) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSrgName(String name, String prefix) {
        if (!name.startsWith(prefix)) return false;
        int separator = name.indexOf('_', prefix.length());
        if (separator <= prefix.length() || separator == name.length() - 1) return false;
        for (int i = prefix.length(); i < separator; i++) {
            char c = name.charAt(i);
            if (c < '0' || c > '9') return false;
        }
        for (int i = separator + 1; i < name.length(); i++) {
            char c = name.charAt(i);
            if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z')) return false;
        }
        return true;
    }
    public static AbstractInsnNode findPreviousNode(AbstractInsnNode start,
                                                    Predicate<AbstractInsnNode> checkNode) {
        final int MAX_SEARCH = 10000;
        int counter = 0;

        AbstractInsnNode current = start;
        while (current != null && counter++ < MAX_SEARCH) {
            if (checkNode.test(current)) {
                return current;
            }

            current = current.getPrevious();
        }

        if (counter >= MAX_SEARCH) {
            throw new IllegalStateException("Forward search exceeded safety limit. Possible circular reference?");
        }

        return null;
    }
}
