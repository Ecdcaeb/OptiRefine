package mods.Hileb.optirefine.library.cursedmixinextensions;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.*;
import mods.Hileb.optirefine.library.cursedmixinextensions.util.CallTransformTask;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.util.Annotations;

import java.util.*;

/**
 * @author LlamaLad7
 * @author Cat Core
 * @author Hileb
 */
@SuppressWarnings("all")
public class CursedMixinExtensions {
    public static void postApply(ClassNode targetClass) {
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
            ListIterator<Object> iterator = implementsItf.values.listIterator();
            while (iterator.hasNext()) {
                Object current = iterator.next();
                if ("value".equals(current)) {
                    for (Type type : (List<Type>) iterator.next()) {
                        if (!targetClass.interfaces.contains(type.getClassName())) {
                            targetClass.interfaces.add(type.getClassName());
                        }
                    }
                } else if ("itfs".equals(current)) {
                    for (String type : (List<String>) iterator.next()) {
                        type = type.replace('.', '/');
                        if (!targetClass.interfaces.contains(type)) {
                            targetClass.interfaces.add(type);
                        }
                    }
                }
            }
        }

        List<String> ctrToReplace = new ArrayList<>();

        for (ListIterator<MethodNode> it = targetClass.methods.listIterator(); it.hasNext(); ) {
            MethodNode method = it.next();
            boolean remove = false;

            if (Annotations.getVisible(method, ShadowSuperConstructor.class) != null) {
                remove = true;
                tasks.add((instructions, call) -> {
                    if (call.name.equals(method.name) && call.desc.equals(method.desc)) {
                        call.setOpcode(Opcodes.INVOKESPECIAL);
                        call.name = "<init>";
                        call.owner = targetClass.superName;
                    }
                });
            }

            if (Annotations.getVisible(method, ShadowConstructor.class) != null) {
                remove = true;
                tasks.add((instructions, call) -> {
                    if (call.name.equals(method.name) && call.desc.equals(method.desc)) {
                        call.setOpcode(Opcodes.INVOKESPECIAL);
                        call.name = "<init>";
                    }
                });
            }

            AnnotationNode superMethod = Annotations.getVisible(method, ShadowSuper.class);

            if (superMethod != null) {
                remove = true;
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
                method.access |= Opcodes.ACC_PUBLIC;
                method.access &= ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED);
            }

            if (Annotations.getVisible(method, NewConstructor.class) != null) {
                tasks.add((instructions, call) -> {
                    if (call.name.equals(method.name) && call.desc.equals(method.desc)) {
                        call.setOpcode(Opcodes.INVOKESPECIAL);
                        call.name = "<init>";
                    }
                });
                method.name = "<init>";
            }

            if (Annotations.getVisible(method, ReplaceConstructor.class) != null) {
                ctrToReplace.add(method.desc);
                tasks.add((instructions, call) -> {
                    if (call.name.equals(method.name) && call.desc.equals(method.desc)) {
                        call.setOpcode(Opcodes.INVOKESPECIAL);
                        call.name = "<init>";
                    }
                });
                method.name = "<init>";
            }

            if (Annotations.getVisible(method, AccessTransformer.class) != null) {
                remove = true;
                int access = -1;
                String name = null;
                boolean deobf = false;
                ListIterator<Object> iterator = Annotations.getVisible(method, AccessibleOperation.class).values.listIterator();
                while (iterator.hasNext()) {
                    Object current = iterator.next();
                    if ("access".equals(current)) {
                        access = (Integer) iterator.next();
                    } else if ("name".equals(current)) {
                        name = (String) iterator.next();
                    } else if ("deobf".equals(current)) {
                        deobf = (Boolean) iterator.next();
                    }
                }

                String desc = method.desc;
                if (access == -1) access = method.access;
                if (name == null) name = method.name;

                if(deobf) {
                    name = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(targetClass.name, name, desc);
                    desc = FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc);
                }

                for (MethodNode mn : targetClass.methods) {
                    if(mn.name.equals(name) && mn.desc.equals(desc)) {
                        mn.access = access;
                    }
                }

            }

            if (Annotations.getVisible(method, AccessibleOperation.class) != null) {
                remove = true;
                int opcodes = 0;
                String desc = null;
                boolean itf = false;
                boolean deobf = false;
                ListIterator<Object> iterator = Annotations.getVisible(method, AccessibleOperation.class).values.listIterator();
                while (iterator.hasNext()) {
                    Object current = iterator.next();
                    if ("opcode".equals(current)) {
                        opcodes = (Integer) iterator.next();
                    } else if ("desc".equals(current)){
                        desc = ((String) iterator.next()).replace('.', '/');
                    } else if ("itf".equals(current)) {
                        itf = (Boolean) iterator.next();
                    } else if ("deobf".equals(current)) {
                        deobf = (Boolean) iterator.next();
                    }
                }
                if (opcodes == Opcodes.NOP) {
                    tasks.add((instructions, call) -> {
                        if (call.owner.equals(targetClass.name) && call.name.equals(method.name) && call.desc.equals(method.desc)) {
                            instructions.insert(call, new InsnNode(Opcodes.NOP));
                            instructions.remove(call);
                        }
                    });
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

                    tasks.add((instructions, call) -> {
                        if (call.owner.equals(targetClass.name) && call.name.equals(method.name) && call.desc.equals(method.desc)) {
                            instructions.insertBefore(call, new TypeInsnNode(Opcodes.NEW, owner));
                            instructions.insertBefore(call, new InsnNode(Opcodes.DUP));
                            call.owner = owner;
                            call.name = "<init>";
                            call.desc = _opt_desc;
                            call.itf = false;
                            call.setOpcode(Opcodes.INVOKESPECIAL);
                        }
                    });

                } else if (opcodes == Opcodes.INSTANCEOF) {
                    final String owner;
                    if (deobf) {
                        owner = FMLDeobfuscatingRemapper.INSTANCE.map(desc);
                    } else owner = desc;
                    tasks.add((instructions, call) -> {
                        if (call.owner.equals(targetClass.name) && call.name.equals(method.name) && call.desc.equals(method.desc)) {
                            instructions.insert(call, new TypeInsnNode(Opcodes.INSTANCEOF, owner));
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
                    if (opcodes >= Opcodes.GETSTATIC && opcodes <= Opcodes.PUTFIELD) {
                        tasks.add((instructions, call) -> {
                            if (call.owner.equals(targetClass.name) && call.name.equals(method.name) && call.desc.equals(method.desc)) {
                                instructions.insert(call, new FieldInsnNode(opc, owner, name, _opt_desc));
                                instructions.remove(call);
                            }
                        });
                    } else if (opcodes >= Opcodes.INVOKEVIRTUAL && opcodes <= Opcodes.INVOKEDYNAMIC) {
                        tasks.add((instructions, call) -> {
                            if (call.owner.equals(targetClass.name) && call.name.equals(method.name) && call.desc.equals(method.desc)) {
                                call.owner = owner;
                                call.name = name;
                                call.desc = _opt_desc;
                                call.itf = itf_;
                                call.setOpcode(opc);
                            }
                        });
                    }
                }
            }

            if (remove) {
                it.remove();
            }
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
            if (Annotations.getVisible(field, AccessTransformer.class) != null) {
                fieldNodeIterator.remove();
                int access = -1;
                String name = null;
                boolean deobf = false;
                ListIterator<Object> iterator = Annotations.getVisible(field, AccessibleOperation.class).values.listIterator();
                while (iterator.hasNext()) {
                    Object current = iterator.next();
                    if ("access".equals(current)) {
                        access = (Integer) iterator.next();
                    } else if ("name".equals(current)) {
                        name = (String) iterator.next();
                    } else if ("deobf".equals(current)) {
                        deobf = (Boolean) iterator.next();
                    }
                }

                String desc = field.desc;
                if (access == -1) access = field.access;
                if (name == null) name = field.name;

                if(deobf) {
                    name = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(targetClass.name, name, desc);
                    desc = FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc);
                }

                for (FieldNode mn : targetClass.fields) {
                    if(mn.name.equals(name) && mn.desc.equals(desc)) {
                        mn.access = access;
                    }
                }

            }
        }

        transformCalls(targetClass, tasks.toArray(CallTransformTask[]::new));
    }
    private static void transformCalls(ClassNode classNode, CallTransformTask... tasks) {
        for (MethodNode method : classNode.methods) {
            for (AbstractInsnNode insn : method.instructions) {
                if (insn instanceof MethodInsnNode call) {
                    for (CallTransformTask task : tasks) {
                        task.transform(method.instructions, call);
                    }
                }
            }
        }
    }
}
