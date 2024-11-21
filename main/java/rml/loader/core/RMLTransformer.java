package rml.loader.core;

import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import rml.jrx.announces.EarlyClass;
import rml.jrx.announces.PrivateAPI;
import rml.jrx.announces.RewriteWhenCleanroom;
import rml.jrx.asm.MethodName;
import rml.jrx.utils.ClassHelper;
import rml.jrx.utils.Tasks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.function.ToIntFunction;

/**
 * @Project ResourceModLoader
 * @Author Hileb
 * @Date 2023/12/14 22:59
 **/
@EarlyClass
@PrivateAPI
public class RMLTransformer implements IClassTransformer {
    public static final MethodName m_193061 = MethodName.of()
            .mcp("loadFunctions","()V")
            .srg("func_193061_h","()V")
            .notch("h","()V")
            .build();
    public static final MethodName m_175276 = MethodName.of()
            .mcp("handleComponentClick", "(Lnet/minecraft/util/text/ITextComponent;)Z")
            .srg("func_175276_a", "(Lnet/minecraft/util/text/ITextComponent;)Z")
            .notch("a", "(Lhh;)Z")
            .build();
    public static final MethodName m_175272 = MethodName.of()
            .mcp("handleComponentHover", "(Lnet/minecraft/util/text/ITextComponent;II)V")
            .srg("func_175272_a", "(Lnet/minecraft/util/text/ITextComponent;II)V")
            .notch("a", "(Lhh;II)V")
            .build();
    public static final MethodName m_179140 = MethodName.of()
            .mcp("disableLighting", "()V")
            .srg("func_179140_f", "()V")
            .notch("g", "()V")
            .build();


    public static final HashMap<String, ToIntFunction<ClassNode>> transformers=new HashMap<>();

    //hope not used
    public static final HashSet<GlobalTransformer> globalTransformers = new HashSet<>();

    static {
        //init the bootstrap transformers
        Transformers.initMinecraftTransformers();
        Transformers.initForgeTransformers();
    }

    /**
     * @param name the obfuscated name
     * @param transformedName de-obfuscated name
     * @param basicClass the basic class byte array
     *
     * @return the transformed class byte array
     */
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass!=null && basicClass.length>0){
            try{
                if (!globalTransformers.isEmpty() || transformers.containsKey(transformedName)){
                    ClassReader classReader = new ClassReader(basicClass);
                    ClassNode cn = new ClassNode();
                    classReader.accept(cn, 0);
                    publicClass(cn);
                    int flags = 0;
                    boolean isTarget = false;
                    for(GlobalTransformer transformer:globalTransformers){
                        if (transformer.isTarget(cn)){
                            flags = transformer.apply(cn);
                            isTarget = true;
                        }
                    }
                    if (transformers.containsKey(transformedName)){
                        flags = transformers.get(transformedName).applyAsInt(cn);
                        isTarget = true;
                    }
                    if (!isTarget) return basicClass;
                    else {
                        ClassWriter classWriter = new LaunchClassWriter(classReader, flags);
                        cn.accept(classWriter);
                        return ASMUtil.push(transformedName, classWriter.toByteArray());
                    }
                }else return basicClass;
            }catch (Exception e){
                //crash the game when transform failed.
                throw new RuntimeException(e);
            }
        }
        return basicClass;
    }

    /**
     * @param classNode the class to public
     */
    public static void publicClass(ClassNode classNode){
        classNode.access = toPublic(classNode.access);
        for(MethodNode mn : classNode.methods){
            mn.access = toPublic(mn.access);
        }
        for(FieldNode fn : classNode.fields){
            fn.access = toPublic(fn.access);
        }
    }


    /**
     * @param access the access
     *
     * @return the public access
     */
    private static int toPublic(int access)
    {
        return access & ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED) | Opcodes.ACC_PUBLIC;
    }

    //Fix the crash of calling getCommonSuperClass
    //Usually, the crash from the obfuscated environment
    public static class LaunchClassWriter extends ClassWriter{

        public LaunchClassWriter(ClassReader classReader, int flags) {
            super(classReader, flags);
        }

        @Override
        protected String getCommonSuperClass(String type1, String type2) {
            return getCommonSuperClass0(type1, type2);
        }

        public static String getCommonSuperClass0(final String type1, final String type2) {
            ClassLoader classLoader = LaunchClassLoader.class.getClassLoader();
            Class<?> class1;
            try {
                class1 = Class.forName(type1.replace('/', '.'), false, classLoader);
            } catch (ClassNotFoundException e) {
                try {
                    class1 = Class.forName(type1.replace('/', '.'), false, Launch.classLoader);
                } catch (ClassNotFoundException e1) {
                    throw new TypeNotPresentException(type1, e1);
                }
            }
            Class<?> class2;
            try {
                class2 = Class.forName(type2.replace('/', '.'), false, classLoader);
            } catch (ClassNotFoundException e) {
                try {
                    class2 = Class.forName(type2.replace('/', '.'), false, Launch.classLoader);
                } catch (ClassNotFoundException e2) {
                    throw new TypeNotPresentException(type2, e2);
                }
            }
            if (class1.isAssignableFrom(class2)) {
                return type1;
            }
            if (class2.isAssignableFrom(class1)) {
                return type2;
            }
            if (class1.isInterface() || class2.isInterface()) {
                return "java/lang/Object";
            } else {
                do {
                    class1 = class1.getSuperclass();
                } while (!class1.isAssignableFrom(class2));
                return class1.getName().replace('.', '/');
            }
        }
    }
    
    public static void register(final String canonicalClassName, final ToIntFunction<ClassNode> singleTransformer){
        if (transformers.containsKey(canonicalClassName)){
            final ToIntFunction<ClassNode> orgTran = transformers.get(canonicalClassName);
            transformers.put(canonicalClassName, value -> orgTran.applyAsInt(value) | singleTransformer.applyAsInt(value));
        }else transformers.put(canonicalClassName, singleTransformer);
    }

    /**
     * @Project ResourceModLoader
     * @Author Hileb
     * @Date 2024/3/11 23:00
     **/
    public static class Transformers {
        public static void initForgeTransformers(){
            register("net.minecraftforge.common.crafting.CraftingHelper",
                    (cn)->{
                        Tasks tasks = new Tasks("init", "getItemStack", "getItemStackBasic");

                        for(MethodNode mn:cn.methods){
                            /**
                             *     registerI("forge:ore_dict", (context, json) -> {
                             *             return new OreIngredient(JsonUtils.getString(json, "ore"));
                             *         });
                             *         CraftingHelperInitEvent.post();
                             *     }
                             * **/
                            if ("init".equals(mn.name)){
                                ASMUtil.injectBefore(mn.instructions, ()->{
                                    InsnList hook = new InsnList();
                                    hook.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "rml/loader/api/event/CraftingHelperInitEvent","post","()V",false));
                                    return hook;
                                }, (node) -> node.getOpcode() == Opcodes.RETURN);
                                tasks.complete("init");
                            } else if ("getItemStack".equals(mn.name) || "getItemStackBasic".equals(mn.name)){
                                //mn.
                                // mn.instructions.clear();
                                // mn.visitVarInsn(Opcodes.ALOAD, 0);
                                // mn.visitVarInsn(Opcodes.ALOAD, 1);
                                // mn.visitMethodInsn(Opcodes.INVOKESTATIC, "rml/layer/compat/fml/RMLFMLHooks$LateHooks", "getItemStack", "(Lcom/google/gson/JsonObject;Lnet/minecraftforge/common/crafting/JsonContext;)Lnet/minecraft/item/ItemStack;", false);
                                // mn.visitInsn(Opcodes.ARETURN);
                                tasks.complete(mn.name);
                            }
                        }
                        if (tasks.isCompleted()) return ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
                        else tasks.throwError();
                        return -1;
                    });
            register("net.minecraftforge.fml.common.Loader",
                    (cn)->{
                        Tasks tasks = new Tasks("identifyDuplicates");
                        for(MethodNode mn:cn.methods){
                            /**
                             *  private void identifyDuplicates(List<ModContainer> mods) {
                             *         RMLModDiscover.inject(mods);
                             * **/
                            if ("identifyDuplicates".equals(mn.name)){
                                InsnList injectList = new InsnList();
                                injectList.add(new IntInsnNode(Opcodes.ALOAD,1));
                                injectList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "rml/loader/core/RMLModDiscover","inject","(Ljava/util/List;)V",false));
                                mn.instructions.insert(injectList);
                                tasks.complete("identifyDuplicates");
                            }
                        }
                        if (tasks.isCompleted()) return ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
                        else tasks.throwError();
                        return -1;
                    });
            register("net.minecraftforge.common.config.ConfigManager",
                    (cn)->{
                        Tasks tasks = new Tasks("sync");
                        for(MethodNode mn:cn.methods){
                            // public static sync(Ljava/lang/String;Lnet/minecraftforge/common/config/Config$Type;)V
                            if ( !tasks.isCompleted("sync") && "sync".equals(mn.name) && "(Ljava/lang/String;Lnet/minecraftforge/common/config/Config$Type;)V".equals(mn.desc)){
                                ListIterator<AbstractInsnNode> iterator=mn.instructions.iterator();
                                AbstractInsnNode node;
                                while (iterator.hasNext() && !tasks.isCompleted("sync")){
                                    node=iterator.next();
                                    if (node.getOpcode() == Opcodes.INVOKEINTERFACE && node instanceof MethodInsnNode){
                                        MethodInsnNode methodInsnNode=(MethodInsnNode) node;
                                        if ("java/util/Map".equals(methodInsnNode.owner) && "put".equals(methodInsnNode.name) && "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;".equals(methodInsnNode.desc) && methodInsnNode.itf){
                                            methodInsnNode.setOpcode(Opcodes.INVOKESTATIC);
                                            methodInsnNode.owner= "rml/loader/api/config/ConfigPatcher";
                                            methodInsnNode.name="registerCfg";
                                            methodInsnNode.itf=false;
                                            methodInsnNode.desc="(Ljava/util/Map;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;";
                                            tasks.complete("sync");
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (tasks.isCompleted()) return ClassWriter.COMPUTE_MAXS  | ClassWriter.COMPUTE_FRAMES;
                        else tasks.throwError();
                        return -1;
                    }
            );
            register("net.minecraftforge.client.ForgeHooksClient",
                    (cn)->{
                        Tasks tasks = new Tasks("renderMainMenu");
                        for(MethodNode mn: cn.methods){
                            if ("renderMainMenu".equals(mn.name)){
                                ASMUtil.injectBefore(mn.instructions, ()->{
                                    InsnList hook = new InsnList();
                                    hook.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "rml/loader/deserialize/RMLLoaders$MCMainScreenTextLoader", "processComponent", "(Ljava/lang/String;)Ljava/lang/String;", false));
                                    return hook;
                                }, (node)->node.getOpcode() == Opcodes.ARETURN);
                                tasks.complete("renderMainMenu");
                            }
                        }
                        if (tasks.isCompleted()) return ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
                        else tasks.throwError();
                        return -1;
                    });
            /**
             * Impl of {@link rml.loader.api.event.early.FMLBeforeStageEvent}
             * **/
            register("net.minecraftforge.fml.common.LoadController",
                    (cn)->{
                        Tasks tasks = new Tasks("distributeStateMessage");
                        for(MethodNode mn:cn.methods){
                            if ("distributeStateMessage".equals(mn.name) && "(Lnet/minecraftforge/fml/common/LoaderState;[Ljava/lang/Object;)V".equals(mn.desc)){
                                InsnList hook=new InsnList();
                                hook.add(new VarInsnNode(Opcodes.ALOAD,0));
                                hook.add(new VarInsnNode(Opcodes.ALOAD,1));
                                hook.add(new VarInsnNode(Opcodes.ALOAD,2));
                                hook.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "rml/layer/compat/fml/RMLFMLHooks","beforeFMLBusEventSending","(Lnet/minecraftforge/fml/common/LoadController;Lnet/minecraftforge/fml/common/LoaderState;[Ljava/lang/Object;)V",false));
                                mn.instructions.insert(hook);
                                tasks.complete("distributeStateMessage");
                            }
                        }
                        if (tasks.isCompleted()) return ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
                        else tasks.throwError();
                        return -1;
                    });
            /**
             * Impl of {@link rml.loader.api.event.client.gui.ModMenuInfoEvent}
             * **/
            register("net.minecraftforge.fml.client.GuiModList$Info",
                    (cn)->{
                        Tasks tasks = new Tasks("<init>");
                        for(MethodNode mn:cn.methods){
                            if ("<init>".equals(mn.name)){
                                ListIterator<AbstractInsnNode> iterator = mn.instructions.iterator();
                                AbstractInsnNode node;
                                while (iterator.hasNext()){
                                    node = iterator.next();
                                    if (node instanceof MethodInsnNode){
                                        MethodInsnNode methodInsnNode = (MethodInsnNode) node;
                                        if ("resizeContent".equals(methodInsnNode.name)){
                                            iterator.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                            iterator.add(new VarInsnNode(Opcodes.ALOAD, 1));
                                            iterator.add(new VarInsnNode(Opcodes.ALOAD, 1));
                                            iterator.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraftforge/fml/client/GuiModList", "selectedMod", "Lnet/minecraftforge/fml/common/ModContainer;"));
                                            iterator.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "rml/loader/api/event/client/gui/ModMenuInfoEvent", "post", "(Ljava/util/List;Ljava/lang/Object;Lnet/minecraftforge/fml/client/GuiModList;Lnet/minecraftforge/fml/common/ModContainer;)Ljava/util/List;", false));
                                            tasks.complete("<init>");
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (tasks.isCompleted()) return ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
                        else tasks.throwError();
                        return -1;
                    });
            //public net.minecraftforge.fml.client.GuiModList elements : fields and methods
            register("net.minecraftforge.fml.client.GuiModList", (cn)-> 0);
            //Add Deserializer for all IForgeRegistry
            register("net.minecraftforge.registries.RegistryBuilder", (cn)->{
                Tasks tasks = new Tasks("create");
                for(MethodNode mn : cn.methods){
                    if ("create".equals(mn.name)){
                        ASMUtil.injectBefore(mn.instructions, ()->{
                            InsnList hook = new InsnList();
                            hook.add(new VarInsnNode(Opcodes.ALOAD, 0));
                            hook.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraftforge/registries/RegistryBuilder", "registryName", "Lnet/minecraft/util/ResourceLocation;"));
                            hook.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "rml/loader/deserialize/MCDeserializers", "onNewRegistry", "(Lnet/minecraftforge/registries/IForgeRegistry;Lnet/minecraft/util/ResourceLocation;)Lnet/minecraftforge/registries/IForgeRegistry;", false));
                            return hook;
                        }, (node) -> node.getOpcode() == Opcodes.ARETURN);
                        tasks.complete("create");
                    }
                }
                if (tasks.isCompleted()) return ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
                else tasks.throwError();
                return -1;
            });
        }
        public static void initMinecraftTransformers(){
            register("net.minecraft.advancements.FunctionManager",
                    (cn)->{
                        Tasks tasks = new Tasks("m_193061");
                        for(MethodNode mn:cn.methods){
                            /**
                             FunctionLoadEvent.post(this);
                             }
                             * **/
                            if (m_193061.is(mn)){
                                ASMUtil.injectBefore(mn.instructions, ()->{
                                    InsnList hook=new InsnList();
                                    hook.add(new IntInsnNode(Opcodes.ALOAD,0));
                                    hook.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "rml/loader/api/event/FunctionLoadEvent","post","(Lnet/minecraft/advancements/FunctionManager;)V",false));
                                    return hook;
                                }, (node)->node.getOpcode() == Opcodes.RETURN);
                                tasks.complete("m_193061");
                            }
                        }
                        if (tasks.isCompleted()) return ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
                        else tasks.throwError();
                        return -1;
                    });
            register("net.minecraft.world.storage.loot.LootTableList",
                    (cn)->{
                        Tasks tasks = new Tasks("<clinit>");
                        for(MethodNode mn: cn.methods){
                            /**
                             * static {
                             *         LootTableRegistryEvent.post();
                             *     }
                             * **/
                            if ("<clinit>".equals(mn.name)){
                                ASMUtil.injectBefore(mn.instructions, ()->{
                                    InsnList hook = new InsnList();
                                    hook.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "rml/loader/api/event/LootTableRegistryEvent","post","()V",false));
                                    return hook;
                                }, (node)->node.getOpcode() == Opcodes.RETURN);
                                tasks.complete("<clinit>");
                            }
                        }
                        if (tasks.isCompleted()) return ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
                        else tasks.throwError();
                        return -1;
                    });

            register("net.minecraft.client.gui.GuiMainMenu",
                    (cn)->{
                        Tasks tasks = new Tasks("<init>");
                        for(MethodNode mn:cn.methods){
                            /**
                             *  try {
                             *             List<String> list = MCMainScreenTextLoader.inject(Lists.newArrayList());
                             *             iresource = Minecraft.getMinecraft().getResourceManager().getResource(SPLASH_TEXTS);
                             * **/
                            if ("<init>".equals(mn.name)){
                                ListIterator<AbstractInsnNode> iterator = mn.instructions.iterator();
                                AbstractInsnNode node;
                                while (iterator.hasNext()){
                                    node = iterator.next();
                                    if (node.getOpcode() == Opcodes.INVOKESTATIC){
                                        MethodInsnNode methodInsnNode = (MethodInsnNode) node;
                                        if ("com/google/common/collect/Lists".equals(methodInsnNode.owner) && "newArrayList".equals(methodInsnNode.name) && "()Ljava/util/ArrayList;".equals(methodInsnNode.desc)){
                                            iterator.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "rml/loader/deserialize/RMLLoaders$MCMainScreenTextLoader", "inject", "(Ljava/util/ArrayList;)Ljava/util/ArrayList;", false));
                                            tasks.complete("<init>");
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (tasks.isCompleted()) return ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
                        else tasks.throwError();
                        return -1;
                    });

            register("net.minecraft.client.gui.GuiScreen",
                    (cn)->{
                        Tasks bar = new Tasks("click", "hover");
                        for(MethodNode mn : cn.methods){
                            if (m_175276.is(mn)){
                                Label returnTrueLabel = new Label();
                                LabelNode labelNode = new LabelNode(returnTrueLabel);
                                ASMUtil.injectBefore(mn.instructions,
                                        () -> {
                                            InsnList hook = new InsnList();
                                            hook.add(new VarInsnNode(Opcodes.ALOAD, 1));
                                            hook.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                            hook.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "rml/loader/api/event/client/gui/HandleComponentEvent", "postClick", "(Lnet/minecraft/util/text/ITextComponent;Lnet/minecraft/client/gui/GuiScreen;)Z", false));
                                            hook.add(new JumpInsnNode(Opcodes.IFNE, labelNode));
                                            return hook;
                                        }, (node)->{
                                            if (node.getNext()!=null){
                                                node = node.getNext();
                                                if (node.getOpcode() == Opcodes.LDC){
                                                    if ("Don't know how to handle {}".equals(((LdcInsnNode)node).cst)){
                                                        return true;
                                                    }else return false;
                                                }else return false;
                                            }return false;
                                        });
                                mn.instructions.add(labelNode);
                                mn.instructions.add(new InsnNode(Opcodes.ICONST_M1));
                                mn.instructions.add(new InsnNode(Opcodes.IRETURN));
                                bar.complete("click");
                            }else if (m_175272.is(mn)){
                                ASMUtil.injectBefore(mn.instructions,
                                        () -> {
                                            InsnList hook = new InsnList();
                                            hook.add(new VarInsnNode(Opcodes.ALOAD, 1));
                                            hook.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                            hook.add(new VarInsnNode(Opcodes.ILOAD, 2));
                                            hook.add(new VarInsnNode(Opcodes.ILOAD, 3));
                                            hook.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "rml/loader/api/event/client/gui/HandleComponentEvent", "postHover", "(Lnet/minecraft/util/text/ITextComponent;Lnet/minecraft/client/gui/GuiScreen;II)V", false));
                                            return hook;
                                        }, (node)->{
                                            if (node.getOpcode() != Opcodes.INVOKESTATIC)return false;
                                            else {
                                                MethodInsnNode md = (MethodInsnNode) node;
                                                return m_179140.is(md.name, md.desc) && ("bus".equals(md.owner) || "net/minecraft/client/renderer/GlStateManager".equals(md.owner));
                                            }
                                        });
                                bar.complete("hover");
                            }
                        }
                        if (bar.isCompleted()) return ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
                        else bar.throwError();
                        return -1;
                    });
        }

        public static void initGroovyScriptTransformer(){
            register("com.cleanroommc.groovyscript.sandbox.GroovySandbox",
                    (cn)->{
                        Tasks tasks = new Tasks("load");
                        for(MethodNode mn : cn.methods){
                            if ("load".equals(mn.name) && "(Lgroovy/util/GroovyScriptEngine;Lgroovy/lang/Binding;Ljava/util/Set;Z)V".equals(mn.desc)){
                                InsnList hook = new InsnList();
                                hook.add(new VarInsnNode(Opcodes.ALOAD, 0));
                                hook.add(new VarInsnNode(Opcodes.ALOAD, 1));
                                hook.add(new VarInsnNode(Opcodes.ALOAD, 2));
                                hook.add(new VarInsnNode(Opcodes.ALOAD, 3));
                                hook.add(new VarInsnNode(Opcodes.ILOAD, 4));
                                hook.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "rml/layer/compat/groovyscripts/RMLGroovySandBox", "load", "(Lcom/cleanroommc/groovyscript/sandbox/GroovySandbox;Lgroovy/util/GroovyScriptEngine;Lgroovy/lang/Binding;Ljava/util/Set;Z)V", false));
                                mn.instructions.insert(hook);
                                tasks.complete("load");
                            }
                        }
                        if (tasks.isCompleted()) return ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
                        else tasks.throwError();
                        return -1;
                    });
            //TODO : delete here when Cleanroom/GroovyScript#235 merged
            //proxy the module node, override the groovy script mixin
            register("org.codehaus.groovy.ast.ModuleNode",
                    (cn)->{
                        Tasks tasks = new Tasks("setPackage");
                        MethodNode toAdd = null;
                        for(MethodNode mn : cn.methods){
                            if ("setPackage".equals(mn.name)){
                                mn.name = "setPackage0";
                                MethodVisitor setPackage = toAdd = new MethodNode(mn.access, "setPackage", mn.desc, mn.signature, mn.exceptions.isEmpty() ? null : mn.exceptions.toArray(new String[0]));
                                Label label0 = new Label();
                                Label label1 = new Label();
                                Label label2 = new Label();
                                setPackage.visitTryCatchBlock(label0, label1, label2, "java/lang/Throwable");
                                setPackage.visitLabel(label0);
                                setPackage.visitLineNumber(ClassHelper.getLineNumber(), label0);
                                setPackage.visitVarInsn(Opcodes.ALOAD, 0);
                                setPackage.visitVarInsn(Opcodes.ALOAD, 1);
                                setPackage.visitMethodInsn(Opcodes.INVOKEVIRTUAL, cn.name, mn.name, mn.desc, false);
                                setPackage.visitLabel(label1);
                                setPackage.visitLineNumber(ClassHelper.getLineNumber(), label1);
                                Label label3 = new Label();
                                setPackage.visitJumpInsn(Opcodes.GOTO, label3);
                                setPackage.visitLabel(label2);
                                setPackage.visitLineNumber(ClassHelper.getLineNumber(), label2);
                                setPackage.visitVarInsn(Opcodes.ASTORE, 2);
                                Label label4 = new Label();
                                setPackage.visitLabel(label4);
                                setPackage.visitLineNumber(ClassHelper.getLineNumber(), label4);
                                setPackage.visitVarInsn(Opcodes.ALOAD, 0);
                                setPackage.visitVarInsn(Opcodes.ALOAD, 1);
                                setPackage.visitFieldInsn(Opcodes.PUTFIELD, cn.name, "packageNode", "Lorg/codehaus/groovy/ast/PackageNode;");
                                setPackage.visitLabel(label3);
                                setPackage.visitLineNumber(ClassHelper.getLineNumber(), label3);
                                setPackage.visitInsn(Opcodes.RETURN);

                                tasks.complete("setPackage");
                            }
                        }
                        if (toAdd != null){
                            cn.methods.add(toAdd);
                        }
                        if (tasks.isCompleted()) return ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
                        else tasks.throwError();
                        return -1;
                    });
        }
        public static class Late{
            public static void initModTransformers(Object[] objects){
                ASMDataTable asmDatas = (ASMDataTable)objects[1];
                if (Loader.isModLoaded(CraftTweaker.MODID)){
                    register("crafttweaker.mc1120.CraftTweaker",
                            (cn)->{
                                Tasks tasks = new Tasks("onPreInitialization");
                                for(MethodNode mn:cn.methods){
                                    /**
                                     *  @EventHandler
                                     *     public void onPreInitialization(FMLPreInitializationEvent ev) {
                                     *         CrTZenClassRegisterEvent.post();
                                     *         PROXY.registerEvents();
                                     * **/
                                    if ("onPreInitialization".equals(mn.name)){
                                        InsnList hook = new InsnList();
                                        hook.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "rml/layer/compat/crt/CrTZenClassRegisterEvent", "post", "()V", false));
                                        mn.instructions.insert(hook);
                                        tasks.complete("onPreInitialization");
                                    }
                                }
                                if (tasks.isCompleted()) return ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
                                else tasks.throwError();
                                return -1;
                            });

                    //Find all ITweaker and inject it
                    for (ASMDataTable.ASMData asmData : asmDatas.getAll("crafttweaker/runtime/ITweaker")) {

                        /**
                         *   setScriptProvider(IScriptProvider provider){
                         * +     provider = RMLCrTLoader.inject(provider);
                         *     //...
                         *   }
                         *
                         * **/
                        register(asmData.getClassName().replace('/', '.'),
                                (cn)->{
                                    Tasks tasks = new Tasks("setScriptProvider");
                                    for (MethodNode mn : cn.methods) {
                                        if ("setScriptProvider".equals(mn.name)) {
                                            InsnList list = new InsnList();
                                            list.add(new VarInsnNode(Opcodes.ALOAD, 1));
                                            list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "rml/layer/compat/crt/RMLCrTLoader", "inject", "(Lcrafttweaker/runtime/IScriptProvider;)Lcrafttweaker/runtime/IScriptProvider;", false));
                                            list.add(new VarInsnNode(Opcodes.ASTORE, 1)); // provider
                                            mn.instructions.insert(list);
                                            tasks.complete("setScriptProvider");
                                        }
                                    }
                                    if (tasks.isCompleted()) return ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
                                    else tasks.throwError();
                                    return -1;
                                });
                    }
                }

            }
        }

    }
}
