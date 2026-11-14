package mods.Hileb.optirefine.core.mixin;

import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public final class DefaultMixinPlugin implements IMixinConfigPlugin {
    private final MixinModulePlan modules = MixinModulePlan.loadDefault();

    @Override public void onLoad(String mixinPackage) {
        modules.validateSide(FMLLaunchHandler.side().isClient());
        MixinPostProcessor.register();
    }
    @Override public String getRefMapperConfig() { return null; }
    @Override public boolean shouldApplyMixin(String targetClassName, String mixinClassName) { return true; }
    @Override public void acceptTargets(Set<String> ownTargets, Set<String> otherTargets) {}
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String targetClassName, ClassNode target, String mixinClassName, IMixinInfo info) {}
    @Override public void postApply(String targetClassName, ClassNode target, String mixinClassName, IMixinInfo info) { MixinPostProcessor.enqueue(target); }
}
