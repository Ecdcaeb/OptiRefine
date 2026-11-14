package mods.Hileb.optirefine.core.mixin;

import net.minecraftforge.fml.common.Loader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public final class ModMixinPlugin implements IMixinConfigPlugin {
    private static final String PREFIX = "mods.Hileb.optirefine.mixin.mods.";

    @Override public void onLoad(String mixinPackage) { MixinPostProcessor.register(); }
    @Override public String getRefMapperConfig() { return ""; }
    @Override public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String mod = requiredMod(mixinClassName);
        return mod != null && Loader.isModLoaded(mod);
    }

    static String requiredMod(String mixinClassName) {
        if (!mixinClassName.startsWith(PREFIX)) return null;
        int end = mixinClassName.indexOf('.', PREFIX.length());
        String mod = end < 0 ? mixinClassName.substring(PREFIX.length()) : mixinClassName.substring(PREFIX.length(), end);
        return mod.isEmpty() ? null : mod;
    }

    @Override public void acceptTargets(Set<String> ownTargets, Set<String> otherTargets) {}
    @Override public List<String> getMixins() { return List.of(); }
    @Override public void preApply(String targetClassName, ClassNode target, String mixinClassName, IMixinInfo info) {}
    @Override public void postApply(String targetClassName, ClassNode target, String mixinClassName, IMixinInfo info) { MixinPostProcessor.enqueue(target); }
}
