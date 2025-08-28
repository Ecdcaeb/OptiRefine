package mods.Hileb.optirefine.library.cursedmixinextensions;

import mods.Hileb.optirefine.core.OptiRefineLog;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.*;
import mods.Hileb.optirefine.library.cursedmixinextensions.util.CallTransformTask;
import mods.Hileb.optirefine.library.foundationx.asm.NonLoadingClassWriter;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.TraceClassVisitor;
import org.spongepowered.asm.util.Annotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
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

            AnnotationNode signFix = Annotations.getVisible(method, SignatureFix.class);
            if (signFix != null) {
                method.signature = ((String)Annotations.getValue(signFix)).replace('.', '/');
            }

            if (Annotations.getVisible(method, ShadowSuperConstructor.class) != null) {
                LOGGER.debug("ShadowSuperConstructor Instance, {}", method.name);
                methodToRemove.add(method);
                tasks.add((instructions, call) -> {
                    if (call.name.equals(method.name) && call.desc.equals(method.desc)) {
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
                    if (call.name.equals(method.name) && call.desc.equals(method.desc)) {
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
                    if (call.name.equals(method.name) && call.desc.equals(method.desc)) {
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
                    if (call.name.equals(method.name) && call.desc.equals(method.desc)) {
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
                    if (call.name.equals(method.name) && call.desc.equals(method.desc)) {
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
                    targetClass.access = access;
                } else {
                    String desc = method.desc;
                    if (access == -1) access = method.access;
                    if (name == null) name = method.name;

                    if (deobf) {
                        name = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(targetClass.name, name, desc);
                        desc = FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc);
                    }

                    for (MethodNode mn : targetClass.methods) {
                        if (mn.name.equals(name) && mn.desc.equals(desc)) {
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
                        if (deobf) {
                            owner = FMLDeobfuscatingRemapper.INSTANCE.map(str[0]);
                            _opt_desc = FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(str[1]);
                        } else {
                            owner = str[0];
                            _opt_desc = str[1];
                        }
                    } else if (str.length == 1) {
                        if (deobf) {
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

                } else if (opcodes == Opcodes.INSTANCEOF) {
                    final String owner;
                    if (deobf) {
                        owner = FMLDeobfuscatingRemapper.INSTANCE.map(desc);
                    } else owner = desc;
                    LOGGER.debug("AccessibleOperation {} : INSTANCEOF : {}", targetClass.name, owner);
                    tasks.add((instructions, call) -> {
                        if (call.owner.equals(targetClass.name) && call.name.equals(method.name) && call.desc.equals(method.desc)) {
                            instructions.insert(call, new TypeInsnNode(Opcodes.INSTANCEOF, owner.replace('.', '/')));
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
                            if (deobf) {
                                owner = FMLDeobfuscatingRemapper.INSTANCE.map(targetClass.name);
                                name = FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(targetClass.name, str[0], str[1]);
                                _opt_desc = FMLDeobfuscatingRemapper.INSTANCE.mapDesc(str[1]);
                            } else {
                                owner = targetClass.name;
                                name = str[0];
                                _opt_desc = str[1];
                            }
                        } else if (str.length == 3) {
                            if (deobf) {
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
                            if (deobf) {
                                owner = FMLDeobfuscatingRemapper.INSTANCE.map(targetClass.name);
                                name = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(targetClass.name, str[0], str[1]);
                                _opt_desc = FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(str[1]);
                            } else {
                                owner = targetClass.name;
                                name = str[0];
                                _opt_desc = str[1];
                            }
                        } else if (str.length == 3) {
                            if (deobf) {
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
                                instructions.insert(call, new FieldInsnNode(opc, owner, name, _opt_desc));
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

                if(deobf) {
                    name = FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(targetClass.name, name, desc);
                    desc = FMLDeobfuscatingRemapper.INSTANCE.mapDesc(desc);
                }

                for (FieldNode mn : targetClass.fields) {
                    if(mn.name.equals(name) && mn.desc.equals(desc)) {
                        mn.access = access;
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
                        printStream.printf("NEW CLASS %s %s", name);
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

    private static void transformCalls(ClassNode classNode, CallTransformTask... tasks) {
        for (CallTransformTask task : tasks) {
            for (MethodNode method : classNode.methods) {
                for (AbstractInsnNode insn : method.instructions) {
                    if (insn instanceof MethodInsnNode call) {
                        task.transform(method.instructions, call);
                    }
                }
            }
        }
    }

    private static final boolean DUMP = Boolean.parseBoolean(System.getProperty("foundation.dump", "false"));

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
