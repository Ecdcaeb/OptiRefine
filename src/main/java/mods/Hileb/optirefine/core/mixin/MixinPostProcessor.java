package mods.Hileb.optirefine.core.mixin;

import mods.Hileb.optirefine.core.transformer.ConfigFacadeMapper;
import mods.Hileb.optirefine.library.cursedmixinextensions.CursedMixinExtensions;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/** Processes the completed target, including calls inserted by late MixinExtras injectors. */
public final class MixinPostProcessor implements IExtension {
    private static final MixinPostProcessor INSTANCE = new MixinPostProcessor();
    private static boolean registered;
    private final Set<ClassNode> pending = Collections.synchronizedSet(Collections.newSetFromMap(new IdentityHashMap<>()));

    public static synchronized void register() {
        if (registered) return;
        MixinEnvironment environment = MixinEnvironment.getCurrentEnvironment();
        Extensions extensions = (Extensions) ((IMixinTransformer) environment.getActiveTransformer()).getExtensions();
        extensions.add(INSTANCE);
        extensions.select(environment);
        registered = true;
    }

    public static void enqueue(ClassNode target) { INSTANCE.schedule(target); }

    void schedule(ClassNode target) { pending.add(target); }

    @Override public boolean checkActive(MixinEnvironment environment) { return true; }
    @Override public void preApply(ITargetClassContext context) {}
    @Override public void postApply(ITargetClassContext context) {}

    @Override public void export(MixinEnvironment environment, String name, boolean force, ClassNode target) {
        if (!pending.remove(target)) return;
        try {
            CursedMixinExtensions.postApply(target);
        } catch (RuntimeException | LinkageError exception) {
            throw failure(target, "extension lowering", exception);
        }
        try {
            ConfigFacadeMapper.remap(target);
            ConfigFacadeMapper.verify(target);
        } catch (RuntimeException | LinkageError exception) {
            throw failure(target, "Config facade mapping", exception);
        }
        CursedMixinExtensions.dumpFinalClass(target);
    }

    private static IllegalStateException failure(ClassNode target, String phase, Throwable cause) {
        return new IllegalStateException("OptiRefine postprocessing failed in " + phase + " for " + target.name + ": " + cause.getMessage(), cause);
    }
}
