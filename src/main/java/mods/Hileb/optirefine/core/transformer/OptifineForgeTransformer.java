package mods.Hileb.optirefine.core.transformer;

import mods.Hileb.optirefine.library.foundationx.TransformerHelper;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** Direct Forge links for the reflection sites in the 1.12.2 HD U G5 jar. */
public final class OptifineForgeTransformer implements TransformerHelper.TargetedASMTransformer, Opcodes {
    private static final String REFLECTOR = "net/optifine/reflect/Reflector";
    private static final String FORGE = "net/minecraftforge/";
    private static final String RESULT = FORGE + "fml/common/eventhandler/Event$Result";
    private static final String MODEL_LOADER = FORGE + "client/model/ModelLoader";
    private static final String VANILLA_LOADER = MODEL_LOADER + "$VanillaLoader";
    private static final String LIGHT_UTIL = FORGE + "client/model/pipeline/LightUtil";

    // The vanilla section and BetterFoliageClient are deliberately outside this cutover.
    public static final int FORGE_METADATA_FIELDS = 249;
    private static final int REMOVED_FIELDS = FORGE_METADATA_FIELDS + 1; // logForge

    private static final String[] TARGETS = {
            "net.optifine.ConnectedTextures",
            "net.optifine.CustomColors$5",
            "net.optifine.CustomItemProperties",
            "net.optifine.config.ConnectedParser",
            "net.optifine.override.ChunkCacheOF",
            "net.optifine.shaders.BlockAliases",
            "net.optifine.shaders.ItemAliases",
            "net.optifine.shaders.EntityAliases",
            "net.optifine.shaders.Shaders",
            "net.optifine.shaders.ShadersRender",
            "net.optifine.reflect.ReflectorForge",
            "net.optifine.reflect.ReflectorForge$1",
            "net.optifine.reflect.Reflector"
    };

    // Counts include existence probes, field operations and calls, per unmodified G5 method.
    private static final Map<String, Map<String, Integer>> SITES = Map.ofEntries(
            Map.entry("net/optifine/ConnectedTextures", Map.of("getNeighbourIcon", 2)),
            Map.entry("net/optifine/CustomColors$5", Map.of("getColor", 2)),
            Map.entry("net/optifine/CustomItemProperties", Map.of("loadItemModel", 6, "getModelLocation", 1)),
            Map.entry("net/optifine/override/ChunkCacheOF", Map.of("isSideSolid", 1)),
            Map.entry("net/optifine/shaders/BlockAliases", Map.of("update", 1)),
            Map.entry("net/optifine/shaders/ItemAliases", Map.of("update", 1)),
            Map.entry("net/optifine/shaders/EntityAliases", Map.of("update", 1)),
            Map.entry("net/optifine/shaders/Shaders", Map.of("loadShaderPack", 3)),
            Map.entry("net/optifine/shaders/ShadersRender", Map.of("renderShadowMap", 5)),
            Map.entry("net/optifine/reflect/ReflectorForge$1", Map.of("call", 1))
    );

    private record DirectCall(int opcode, String owner, String name, String descriptor) {
        MethodInsnNode instruction() {
            return new MethodInsnNode(opcode, owner, name, descriptor, opcode == INVOKEINTERFACE);
        }
    }

    private static final Map<String, DirectCall> CALLS = Map.ofEntries(
            Map.entry("ForgeBlock_getExtendedState", new DirectCall(INVOKEVIRTUAL,
                    "net/minecraft/block/Block", "getExtendedState",
                    "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;")),
            Map.entry("ForgeBiome_getWaterColorMultiplier", new DirectCall(INVOKEVIRTUAL,
                    "net/minecraft/world/biome/Biome", "getWaterColorMultiplier", "()I")),
            Map.entry("ForgeChunkCache_isSideSolid", new DirectCall(INVOKEVIRTUAL,
                    "net/minecraft/world/ChunkCache", "isSideSolid",
                    "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Z)Z")),
            Map.entry("ModelLoader_VanillaLoader_loadModel", new DirectCall(INVOKEVIRTUAL,
                    VANILLA_LOADER, "loadModel",
                    "(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraftforge/client/model/IModel;")),
            Map.entry("IModel_getTextures", new DirectCall(INVOKEINTERFACE,
                    FORGE + "client/model/IModel", "getTextures", "()Ljava/util/Collection;")),
            Map.entry("ForgeHooksClient_setRenderPass", new DirectCall(INVOKESTATIC,
                    FORGE + "client/ForgeHooksClient", "setRenderPass", "(I)V")),
            Map.entry("IForgeRegistryEntry_Impl_getRegistryName", new DirectCall(INVOKEVIRTUAL,
                    FORGE + "registries/IForgeRegistryEntry$Impl", "getRegistryName",
                    "()Lnet/minecraft/util/ResourceLocation;"))
    );

    @Override
    public String[] getTargets() {
        return TARGETS.clone();
    }

    @Override
    public int transform(ClassNode node) {
        switch (node.name) {
            case REFLECTOR -> removeForgeMetadata(node);
            case "net/optifine/reflect/ReflectorForge" -> replaceForgeInitializer(node);
            case "net/optifine/config/ConnectedParser" -> replaceEntityLookups(node);
            default -> convertSites(node);
        }
        // Every edit preserves the original control-flow edges and stack-map types. New
        // method bodies supply their own frames. COMPUTE_FRAMES would load game classes
        // through ClassWriter.getCommonSuperClass during coremod bootstrap.
        return ClassWriter.COMPUTE_MAXS;
    }

    private static void convertSites(ClassNode node) {
        Map<String, Integer> expected = SITES.get(node.name);
        require(expected != null, node, null, "unexpected transformer target");
        Set<String> converted = new HashSet<>();
        for (MethodNode method : node.methods) {
            int count = 0;
            for (AbstractInsnNode instruction : method.instructions.toArray()) {
                if (!(instruction instanceof FieldInsnNode field) || !REFLECTOR.equals(field.owner)) {
                    continue;
                }
                require(field.getOpcode() == GETSTATIC && expected.containsKey(method.name),
                        node, method, "unexpected Reflector field " + field.name);
                convertSite(node, method, field);
                count++;
            }
            if (count != 0) {
                require(count == expected.get(method.name) && converted.add(method.name),
                        node, method, "unexpected Forge conversion count " + count);
            }
        }
        require(converted.equals(expected.keySet()), node, null, "missing G5 Forge caller methods");
        // These targets have no vanilla or optional-mod Reflector calls in G5.
        for (MethodNode method : node.methods) {
            for (AbstractInsnNode instruction : method.instructions) {
                require(!(instruction instanceof FieldInsnNode field && REFLECTOR.equals(field.owner))
                                && !(instruction instanceof MethodInsnNode call && REFLECTOR.equals(call.owner)),
                        node, method, "unconverted Reflector instruction");
            }
        }
    }

    private static void convertSite(ClassNode node, MethodNode method, FieldInsnNode field) {
        AbstractInsnNode next = next(field);
        if (next instanceof MethodInsnNode probe && "exists".equals(probe.name)) {
            require(probe.getOpcode() == INVOKEVIRTUAL
                            && field.desc.equals("L" + probe.owner + ";")
                            && "()Z".equals(probe.desc) && isGuaranteedProbe(field.name),
                    node, method, "unexpected Forge existence probe " + field.name);
            method.instructions.set(field, new InsnNode(ICONST_1));
            method.instructions.remove(probe);
            return;
        }
        DirectCall direct = CALLS.get(field.name);
        if (direct != null) {
            unpackCall(node, method, field, direct);
            return;
        }
        switch (field.name) {
            case "ModelLoader_VanillaLoader_INSTANCE" -> readStaticField(node, method, field,
                    VANILLA_LOADER, "INSTANCE", "L" + VANILLA_LOADER + ";");
            case "ModelLoaderRegistry_textures" -> readStaticField(node, method, field,
                    FORGE + "client/model/ModelLoaderRegistry", "textures", "Ljava/util/Set;");
            case "ModelLoader_stateModels" -> {
                requireMethod(node, method, next, INVOKESTATIC, REFLECTOR, "getFieldValue",
                        "(Ljava/lang/Object;Lnet/optifine/reflect/ReflectorField;)Ljava/lang/Object;");
                method.instructions.insertBefore(field, new TypeInsnNode(CHECKCAST, MODEL_LOADER));
                method.instructions.set(field, new FieldInsnNode(GETFIELD, MODEL_LOADER, "stateModels", "Ljava/util/Map;"));
                method.instructions.remove(next);
            }
            case "LightUtil_itemConsumer", "LightUtil_tessellator" -> {
                require(next != null && next.getOpcode() == ACONST_NULL, node, method,
                        "expected null LightUtil cache reset");
                AbstractInsnNode setter = next(next);
                requireMethod(node, method, setter, INVOKEVIRTUAL,
                        "net/optifine/reflect/ReflectorField", "setValue", "(Ljava/lang/Object;)V");
                String name = field.name.substring("LightUtil_".length());
                String descriptor = "itemConsumer".equals(name)
                        ? "L" + LIGHT_UTIL + "$ItemConsumer;"
                        : "Lnet/minecraftforge/client/model/pipeline/IVertexConsumer;";
                method.instructions.remove(field);
                method.instructions.set(setter, new FieldInsnNode(PUTSTATIC, LIGHT_UTIL, name, descriptor));
            }
            default -> throw unexpected(node, method, "unknown Forge metadata " + field.name);
        }
    }

    private static boolean isGuaranteedProbe(String name) {
        return switch (name) {
            case "ForgeBlock_getExtendedState", "ForgeBiome_getWaterColorMultiplier", "ModelLoader",
                    "Loader_getActiveModList", "LightUtil", "ForgeHooksClient_setRenderPass" -> true;
            default -> false;
        };
    }

    private static void readStaticField(ClassNode node, MethodNode method, FieldInsnNode metadata,
                                        String owner, String name, String descriptor) {
        AbstractInsnNode getter = next(metadata);
        requireMethod(node, method, getter, INVOKEVIRTUAL,
                "net/optifine/reflect/ReflectorField", "getValue", "()Ljava/lang/Object;");
        method.instructions.set(metadata, new FieldInsnNode(GETSTATIC, owner, name, descriptor));
        method.instructions.remove(getter);
    }

    /** G5 emits only local loads or integer constants at these seven Forge method targets. */
    private static void unpackCall(ClassNode node, MethodNode method, FieldInsnNode metadata, DirectCall direct) {
        require("Lnet/optifine/reflect/ReflectorMethod;".equals(metadata.desc),
                node, method, "unexpected method metadata type");
        Type[] arguments = Type.getArgumentTypes(direct.descriptor);
        AbstractInsnNode length = next(metadata);
        require(integer(length) == arguments.length, node, method, "unexpected Forge argument count");
        AbstractInsnNode array = next(length);
        require(array instanceof TypeInsnNode type && array.getOpcode() == ANEWARRAY
                        && "java/lang/Object".equals(type.desc),
                node, method, "expected G5 Object[] construction");
        AbstractInsnNode cursor = next(array);
        for (int i = 0; i < arguments.length; i++) {
            AbstractInsnNode duplicate = cursor;
            require(duplicate != null && duplicate.getOpcode() == DUP,
                    node, method, "expected argument array duplication");
            AbstractInsnNode index = next(duplicate);
            require(integer(index) == i, node, method, "unexpected argument index");
            AbstractInsnNode value = next(index);
            Type argument = arguments[i];
            AbstractInsnNode store;
            if (argument.getSort() == Type.OBJECT) {
                require(value instanceof VarInsnNode && value.getOpcode() == ALOAD,
                        node, method, "expected reference argument local");
                store = next(value);
            } else {
                require((value instanceof VarInsnNode && value.getOpcode() == ILOAD)
                                || (value != null && integer(value) != Integer.MIN_VALUE),
                        node, method, "expected primitive argument local or constant");
                AbstractInsnNode boxing = next(value);
                String wrapper = argument.getSort() == Type.BOOLEAN ? "java/lang/Boolean" : "java/lang/Integer";
                require(argument.getSort() == Type.BOOLEAN || argument.getSort() == Type.INT,
                        node, method, "unexpected G5 primitive argument type");
                requireMethod(node, method, boxing, INVOKESTATIC, wrapper, "valueOf",
                        "(" + argument.getDescriptor() + ")L" + wrapper + ";");
                store = next(boxing);
                method.instructions.remove(boxing);
            }
            require(store != null && store.getOpcode() == AASTORE,
                    node, method, "expected argument array store");
            cursor = next(store);
            method.instructions.remove(duplicate);
            method.instructions.remove(index);
            method.instructions.remove(store);
        }
        boolean instance = direct.opcode != INVOKESTATIC;
        String reflectorDescriptor = (instance ? "(Ljava/lang/Object;" : "(")
                + "Lnet/optifine/reflect/ReflectorMethod;[Ljava/lang/Object;)";
        Type result = Type.getReturnType(direct.descriptor);
        String reflectorName;
        String reflectorResult;
        if (result.getSort() == Type.INT) {
            reflectorName = "callInt";
            reflectorResult = "I";
        } else if (result.getSort() == Type.BOOLEAN) {
            reflectorName = "callBoolean";
            reflectorResult = "Z";
        } else if (result.getSort() == Type.VOID && cursor instanceof MethodInsnNode call
                && "callVoid".equals(call.name)) {
            reflectorName = "callVoid";
            reflectorResult = "V";
        } else {
            reflectorName = "call";
            reflectorResult = "Ljava/lang/Object;";
        }
        requireMethod(node, method, cursor, INVOKESTATIC, REFLECTOR, reflectorName,
                reflectorDescriptor + reflectorResult);
        if (result.getSort() == Type.VOID && "call".equals(reflectorName)) {
            // Method.invoke returns null for void; G5 immediately discards it. A typed
            // void invocation pushes nothing, so retaining that POP would fail verification.
            AbstractInsnNode discard = next(cursor);
            require(discard != null && discard.getOpcode() == POP,
                    node, method, "expected discarded reflective void return");
            method.instructions.remove(discard);
        }
        if (instance && ("ModelLoader_VanillaLoader_loadModel".equals(metadata.name)
                || "IModel_getTextures".equals(metadata.name))) {
            // Only G5's loader and model locals have Object stack-map types.
            method.instructions.insertBefore(metadata, new TypeInsnNode(CHECKCAST, direct.owner));
        }
        method.instructions.remove(metadata);
        method.instructions.remove(length);
        method.instructions.remove(array);
        method.instructions.set(cursor, direct.instruction());
    }

    private static void replaceEntityLookups(ClassNode node) {
        replaceEntityLookup(node, "getEntityClass", "ForgeEntityList_getClass", "getClass",
                "(Lnet/minecraft/util/ResourceLocation;)Ljava/lang/Class;", ARETURN);
        replaceEntityLookup(node, "getEntityTypeId", "ForgeEntityList_getID", "getID",
                "(Ljava/lang/Class;)I", IRETURN);
    }

    private static void replaceEntityLookup(ClassNode node, String name, String metadata,
                                            String forgeName, String descriptor, int returnOpcode) {
        MethodNode method = method(node, name, descriptor);
        int references = 0;
        for (AbstractInsnNode instruction : method.instructions) {
            if (instruction instanceof FieldInsnNode field && REFLECTOR.equals(field.owner)) {
                require(metadata.equals(field.name), node, method, "unexpected entity lookup metadata");
                references++;
            }
        }
        require(references == 2 && (method.access & ACC_STATIC) == 0,
                node, method, "unexpected entity lookup shape");
        clearBody(method);
        method.instructions.add(new VarInsnNode(ALOAD, 1));
        method.instructions.add(new MethodInsnNode(INVOKESTATIC, "net/minecraft/entity/EntityList",
                forgeName, descriptor, false));
        method.instructions.add(new InsnNode(returnOpcode));
        method.maxLocals = 2;
        method.maxStack = 1;
    }

    private static void replaceForgeInitializer(ClassNode node) {
        MethodNode initializer = method(node, "<clinit>", "()V");
        String[] names = {"ALLOW", "DENY", "DEFAULT"};
        AbstractInsnNode cursor = nextReal(initializer.instructions.getFirst());
        for (String name : names) {
            require(cursor instanceof FieldInsnNode field && field.getOpcode() == GETSTATIC
                            && REFLECTOR.equals(field.owner) && ("Event_Result_" + name).equals(field.name)
                            && "Lnet/optifine/reflect/ReflectorField;".equals(field.desc),
                    node, initializer, "unexpected Event.Result initializer");
            cursor = next(cursor);
            requireMethod(node, initializer, cursor, INVOKESTATIC, REFLECTOR, "getFieldValue",
                    "(Lnet/optifine/reflect/ReflectorField;)Ljava/lang/Object;");
            cursor = next(cursor);
            require(cursor instanceof FieldInsnNode field && field.getOpcode() == PUTSTATIC
                            && node.name.equals(field.owner) && ("EVENT_RESULT_" + name).equals(field.name)
                            && "Ljava/lang/Object;".equals(field.desc),
                    node, initializer, "unexpected Event.Result destination");
            cursor = next(cursor);
        }
        require(cursor != null && cursor.getOpcode() == RETURN && next(cursor) == null,
                node, initializer, "unexpected ReflectorForge initializer tail");
        clearBody(initializer);
        for (String name : names) {
            initializer.instructions.add(new FieldInsnNode(GETSTATIC, RESULT, name, "L" + RESULT + ";"));
            initializer.instructions.add(new FieldInsnNode(PUTSTATIC, node.name,
                    "EVENT_RESULT_" + name, "Ljava/lang/Object;"));
        }
        initializer.instructions.add(new InsnNode(RETURN));
        initializer.maxLocals = 0;
        initializer.maxStack = 1;
    }

    private static void removeForgeMetadata(ClassNode node) {
        Set<String> removed = new HashSet<>();
        boolean inForge = false;
        boolean foundVanilla = false;
        for (FieldNode field : node.fields) {
            if ("logForge".equals(field.name)) {
                require(!inForge, node, null, "duplicate Forge metadata boundary");
                inForge = true;
            }
            if ("logVanilla".equals(field.name)) {
                require(inForge, node, null, "missing Forge metadata boundary");
                foundVanilla = true;
                break;
            }
            if (inForge && !"BetterFoliageClient".equals(field.name)) {
                require((field.access & ACC_STATIC) != 0
                                && ("logForge".equals(field.name) || field.desc.startsWith("Lnet/optifine/reflect/Reflector")),
                        node, null, "unexpected Forge metadata declaration " + field.name);
                removed.add(field.name);
            }
        }
        require(foundVanilla && removed.size() == REMOVED_FIELDS
                        && removed.contains("WorldEvent_Load_Constructor"),
                node, null, "expected 249 G5 Forge metadata fields and logForge");
        MethodNode initializer = method(node, "<clinit>", "()V");
        require(initializer.tryCatchBlocks.isEmpty(), node, initializer, "unexpected initializer handlers");
        Set<String> initializers = new HashSet<>();
        AbstractInsnNode start = initializer.instructions.getFirst();
        for (AbstractInsnNode instruction : initializer.instructions.toArray()) {
            require(!(instruction instanceof JumpInsnNode || instruction instanceof TableSwitchInsnNode
                            || instruction instanceof LookupSwitchInsnNode || instruction instanceof FrameNode),
                    node, initializer, "unexpected G5 initializer control flow");
            if (instruction instanceof FieldInsnNode field && field.getOpcode() == PUTSTATIC
                    && node.name.equals(field.owner)) {
                AbstractInsnNode after = instruction.getNext();
                if (removed.contains(field.name)) {
                    require(initializers.add(field.name), node, initializer, "duplicate Forge initializer");
                    for (AbstractInsnNode part = start; part != after;) {
                        AbstractInsnNode following = part.getNext();
                        // Keep debug labels and line numbers; no control-flow labels are removed.
                        if (part.getOpcode() >= 0) {
                            initializer.instructions.remove(part);
                        }
                        part = following;
                    }
                }
                start = after;
            }
        }
        require(initializers.equals(removed), node, initializer, "missing Forge metadata initializers");
        node.fields.removeIf(field -> removed.contains(field.name));
        deferRetainedClassResolution(node, initializer);
        int removedHelpers = 0;
        for (MethodNode method : node.methods.toArray(MethodNode[]::new)) {
            if ("postForgeBusEvent".equals(method.name)) {
                if ("(Lnet/optifine/reflect/ReflectorConstructor;[Ljava/lang/Object;)Z".equals(method.desc)) {
                    node.methods.remove(method);
                    removedHelpers++;
                } else if ("(Ljava/lang/Object;)Z".equals(method.desc)) {
                    replacePostForgeBusEvent(method);
                    removedHelpers++;
                } else {
                    throw unexpected(node, method, "unexpected Forge bus helper overload");
                }
            }
        }
        require(removedHelpers == 2, node, null, "missing G5 Forge bus helpers");
        for (MethodNode method : node.methods) {
            for (AbstractInsnNode instruction : method.instructions) {
                require(!(instruction instanceof FieldInsnNode field && REFLECTOR.equals(field.owner)
                                && removed.contains(field.name)),
                        node, method, "remaining removed Forge metadata reference");
            }
        }
    }

    private static void deferRetainedClassResolution(ClassNode node, MethodNode initializer) {
        String reflectorClass = "net/optifine/reflect/ReflectorClass";
        int converted = 0;
        for (AbstractInsnNode instruction : initializer.instructions) {
            if (!(instruction instanceof MethodInsnNode constructor)
                    || !reflectorClass.equals(constructor.owner) || !"<init>".equals(constructor.name)) {
                continue;
            }
            require(constructor.getOpcode() == INVOKESPECIAL, node, initializer,
                    "unexpected retained ReflectorClass invocation");
            if ("(Ljava/lang/String;)V".equals(constructor.desc)) {
                continue;
            }
            require("(Ljava/lang/Class;)V".equals(constructor.desc), node, initializer,
                    "unexpected retained ReflectorClass constructor");
            AbstractInsnNode literal = previous(constructor);
            AbstractInsnNode duplicate = previous(literal);
            AbstractInsnNode allocation = previous(duplicate);
            AbstractInsnNode destination = next(constructor);
            require(literal instanceof LdcInsnNode constant && constant.cst instanceof Type type
                            && type.getSort() == Type.OBJECT
                            && duplicate != null && duplicate.getOpcode() == DUP
                            && allocation instanceof TypeInsnNode created && created.getOpcode() == NEW
                            && reflectorClass.equals(created.desc)
                            && destination instanceof FieldInsnNode field && field.getOpcode() == PUTSTATIC
                            && node.name.equals(field.owner) && ("L" + reflectorClass + ";").equals(field.desc),
                    node, initializer, "expected G5 object class literal ReflectorClass initializer");
            LdcInsnNode constant = (LdcInsnNode) literal;
            // A string constant avoids resolving the game class at the LDC itself.
            // ReflectorClass(String) registers deferred resolution with ReflectorResolver.
            constant.cst = ((Type) constant.cst).getClassName();
            constructor.desc = "(Ljava/lang/String;)V";
            converted++;
        }
        require(converted == 55, node, initializer,
                "expected 55 retained G5 class literal ReflectorClass initializers");
    }

    private static AbstractInsnNode previous(AbstractInsnNode instruction) {
        AbstractInsnNode previous = instruction == null ? null : instruction.getPrevious();
        while (previous != null && previous.getOpcode() < 0) {
            previous = previous.getPrevious();
        }
        return previous;
    }

    private static void replacePostForgeBusEvent(MethodNode method) {
        clearBody(method);
        LabelNode nonNull = new LabelNode();
        method.instructions.add(new VarInsnNode(ALOAD, 0));
        method.instructions.add(new JumpInsnNode(IFNONNULL, nonNull));
        method.instructions.add(new InsnNode(ICONST_0));
        method.instructions.add(new InsnNode(IRETURN));
        method.instructions.add(nonNull);
        method.instructions.add(new FrameNode(F_SAME, 0, null, 0, null));
        method.instructions.add(new FieldInsnNode(GETSTATIC, FORGE + "common/MinecraftForge", "EVENT_BUS",
                "Lnet/minecraftforge/fml/common/eventhandler/EventBus;"));
        method.instructions.add(new VarInsnNode(ALOAD, 0));
        method.instructions.add(new TypeInsnNode(CHECKCAST, FORGE + "fml/common/eventhandler/Event"));
        method.instructions.add(new MethodInsnNode(INVOKEVIRTUAL, FORGE + "fml/common/eventhandler/EventBus", "post",
                "(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", false));
        method.instructions.add(new InsnNode(IRETURN));
        method.maxLocals = 1;
        method.maxStack = 2;
    }

    private static void clearBody(MethodNode method) {
        method.instructions.clear();
        method.tryCatchBlocks.clear();
        if (method.localVariables != null) {
            method.localVariables.clear();
        }
        if (method.visibleLocalVariableAnnotations != null) {
            method.visibleLocalVariableAnnotations.clear();
        }
        if (method.invisibleLocalVariableAnnotations != null) {
            method.invisibleLocalVariableAnnotations.clear();
        }
    }

    private static MethodNode method(ClassNode node, String name, String descriptor) {
        for (MethodNode method : node.methods) {
            if (name.equals(method.name) && descriptor.equals(method.desc)) {
                return method;
            }
        }
        throw unexpected(node, null, "missing method " + name + descriptor);
    }

    private static void requireMethod(ClassNode node, MethodNode method, AbstractInsnNode instruction,
                                       int opcode, String owner, String name, String descriptor) {
        require(instruction instanceof MethodInsnNode call && opcode == call.getOpcode()
                        && owner.equals(call.owner) && name.equals(call.name) && descriptor.equals(call.desc),
                node, method, "expected " + owner + "." + name + descriptor);
    }

    private static AbstractInsnNode next(AbstractInsnNode instruction) {
        return instruction == null ? null : nextReal(instruction.getNext());
    }

    private static AbstractInsnNode nextReal(AbstractInsnNode instruction) {
        while (instruction != null && instruction.getOpcode() < 0) {
            instruction = instruction.getNext();
        }
        return instruction;
    }

    private static int integer(AbstractInsnNode instruction) {
        if (instruction == null) {
            return Integer.MIN_VALUE;
        }
        int opcode = instruction.getOpcode();
        if (opcode >= ICONST_M1 && opcode <= ICONST_5) {
            return opcode - ICONST_0;
        }
        if (instruction instanceof IntInsnNode integer && (opcode == BIPUSH || opcode == SIPUSH)) {
            return integer.operand;
        }
        if (instruction instanceof LdcInsnNode constant && constant.cst instanceof Integer integer) {
            return integer;
        }
        return Integer.MIN_VALUE;
    }

    private static void require(boolean condition, ClassNode node, MethodNode method, String message) {
        if (!condition) {
            throw unexpected(node, method, message);
        }
    }

    private static IllegalStateException unexpected(ClassNode node, MethodNode method, String message) {
        return new IllegalStateException("Unsupported OptiFine G5 Forge bytecode in " + node.name
                + (method == null ? "" : "." + method.name + method.desc) + ": " + message);
    }
}
