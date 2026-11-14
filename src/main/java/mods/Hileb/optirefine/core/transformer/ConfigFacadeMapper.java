package mods.Hileb.optirefine.core.transformer;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.ModuleVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.RecordComponentVisitor;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.tree.ClassNode;

/** Remaps typed references to the compile-time Config facade to OptiFine's runtime Config. */
public final class ConfigFacadeMapper {
    private static final String FACADE = "mods/Hileb/optirefine/optifine/Config";
    private static final String RUNTIME = "Config";

    private static final AnnotationVisitor ANNOTATION_SINK = new AnnotationVisitor(Opcodes.ASM9) {
        @Override
        public AnnotationVisitor visitAnnotation(String name, String descriptor) {
            return this;
        }

        @Override
        public AnnotationVisitor visitArray(String name) {
            return this;
        }
    };

    private static final FieldVisitor FIELD_SINK = new FieldVisitor(Opcodes.ASM9) {
        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            return ANNOTATION_SINK;
        }

        @Override
        public AnnotationVisitor visitTypeAnnotation(int typeRef, org.objectweb.asm.TypePath typePath,
                                                     String descriptor, boolean visible) {
            return ANNOTATION_SINK;
        }
    };

    private static final MethodVisitor METHOD_SINK = new MethodVisitor(Opcodes.ASM9) {
        @Override
        public AnnotationVisitor visitAnnotationDefault() {
            return ANNOTATION_SINK;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            return ANNOTATION_SINK;
        }

        @Override
        public AnnotationVisitor visitTypeAnnotation(int typeRef, org.objectweb.asm.TypePath typePath,
                                                     String descriptor, boolean visible) {
            return ANNOTATION_SINK;
        }

        @Override
        public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
            return ANNOTATION_SINK;
        }

        @Override
        public AnnotationVisitor visitInsnAnnotation(int typeRef, org.objectweb.asm.TypePath typePath,
                                                     String descriptor, boolean visible) {
            return ANNOTATION_SINK;
        }

        @Override
        public AnnotationVisitor visitTryCatchAnnotation(int typeRef, org.objectweb.asm.TypePath typePath,
                                                         String descriptor, boolean visible) {
            return ANNOTATION_SINK;
        }

        @Override
        public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, org.objectweb.asm.TypePath typePath,
                                                               org.objectweb.asm.Label[] start,
                                                               org.objectweb.asm.Label[] end, int[] index,
                                                               String descriptor, boolean visible) {
            return ANNOTATION_SINK;
        }
    };

    private static final RecordComponentVisitor RECORD_COMPONENT_SINK = new RecordComponentVisitor(Opcodes.ASM9) {
        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            return ANNOTATION_SINK;
        }

        @Override
        public AnnotationVisitor visitTypeAnnotation(int typeRef, org.objectweb.asm.TypePath typePath,
                                                     String descriptor, boolean visible) {
            return ANNOTATION_SINK;
        }
    };

    private static final ModuleVisitor MODULE_SINK = new ModuleVisitor(Opcodes.ASM9) {
    };

    private ConfigFacadeMapper() {
    }

    public static void remap(ClassNode classNode) {
        if (FACADE.equals(classNode.name) || !hasFacadeReference(classNode)) {
            return;
        }

        ClassNode remapped = new ClassNode();
        classNode.accept(new ClassRemapper(remapped, new Remapper() {
            @Override
            public String map(String internalName) {
                return FACADE.equals(internalName) ? RUNTIME : internalName;
            }
        }));
        copyInto(classNode, remapped);
    }

    public static void verify(ClassNode classNode) {
        if (FACADE.equals(classNode.name)) {
            return;
        }

        try {
            visitReferences(classNode, new Remapper() {
                @Override
                public String map(String internalName) {
                    if (FACADE.equals(internalName)) {
                        throw new ResidualFacadeReference(classNode.name, internalName);
                    }
                    return internalName;
                }
            });
        } catch (ResidualFacadeReference residual) {
            throw new IllegalStateException(residual.getMessage(), residual);
        } catch (RuntimeException malformedType) {
            throw new IllegalStateException("Unable to validate Config facade references in class " + classNode.name,
                    malformedType);
        }
    }

    private static boolean hasFacadeReference(ClassNode classNode) {
        try {
            visitReferences(classNode, new Remapper() {
                @Override
                public String map(String internalName) {
                    if (FACADE.equals(internalName)) {
                        throw FacadeReferenceFound.INSTANCE;
                    }
                    return internalName;
                }
            });
            return false;
        } catch (FacadeReferenceFound found) {
            return true;
        } catch (RuntimeException malformedType) {
            throw new IllegalStateException("Unable to inspect Config facade references in class " + classNode.name,
                    malformedType);
        }
    }

    private static void visitReferences(ClassNode classNode, Remapper remapper) {
        ClassVisitor sink = new ClassVisitor(Opcodes.ASM9) {
            @Override
            public ModuleVisitor visitModule(String name, int access, String version) {
                return MODULE_SINK;
            }

            @Override
            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                return ANNOTATION_SINK;
            }

            @Override
            public AnnotationVisitor visitTypeAnnotation(int typeRef, org.objectweb.asm.TypePath typePath,
                                                         String descriptor, boolean visible) {
                return ANNOTATION_SINK;
            }

            @Override
            public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
                return RECORD_COMPONENT_SINK;
            }

            @Override
            public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                return FIELD_SINK;
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
                                             String[] exceptions) {
                return METHOD_SINK;
            }
        };
        classNode.accept(new ClassRemapper(sink, remapper));
    }

    private static void copyInto(ClassNode target, ClassNode source) {
        target.version = source.version;
        target.access = source.access;
        target.name = source.name;
        target.signature = source.signature;
        target.superName = source.superName;
        target.interfaces = source.interfaces;
        target.sourceFile = source.sourceFile;
        target.sourceDebug = source.sourceDebug;
        target.module = source.module;
        target.outerClass = source.outerClass;
        target.outerMethod = source.outerMethod;
        target.outerMethodDesc = source.outerMethodDesc;
        target.visibleAnnotations = source.visibleAnnotations;
        target.invisibleAnnotations = source.invisibleAnnotations;
        target.visibleTypeAnnotations = source.visibleTypeAnnotations;
        target.invisibleTypeAnnotations = source.invisibleTypeAnnotations;
        target.attrs = source.attrs;
        target.nestHostClass = source.nestHostClass;
        target.nestMembers = source.nestMembers;
        target.permittedSubclasses = source.permittedSubclasses;
        target.innerClasses = source.innerClasses;
        target.recordComponents = source.recordComponents;
        target.fields = source.fields;
        target.methods = source.methods;
    }

    private static final class FacadeReferenceFound extends RuntimeException {
        private static final FacadeReferenceFound INSTANCE = new FacadeReferenceFound();

        private FacadeReferenceFound() {
            super(null, null, false, false);
        }
    }

    private static final class ResidualFacadeReference extends RuntimeException {
        private ResidualFacadeReference(String className, String typeName) {
            super("Residual Config facade type " + typeName + " in class " + className);
        }
    }
}
