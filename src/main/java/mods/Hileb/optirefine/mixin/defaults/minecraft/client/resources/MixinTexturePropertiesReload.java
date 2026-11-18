package mods.Hileb.optirefine.mixin.defaults.minecraft.client.resources;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mods.Hileb.optirefine.optifine.resources.ModernTextureProperties;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraftforge.client.resource.SelectiveReloadStateHandler;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.optifine.shaders.Shaders;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SimpleReloadableResourceManager.class)
public abstract class MixinTexturePropertiesReload {
    @WrapMethod(method = "notifyReloadListeners")
    private void optiRefine$reloadTextureProperties(Operation<Void> original) {
        var selected = SelectiveReloadStateHandler.INSTANCE.get();
        if (!selected.test(VanillaResourceType.TEXTURES) && !selected.test(VanillaResourceType.MODELS)) {
            original.call();
            return;
        }
        String previousMacros = ModernTextureProperties.blockAtlasMacros();
        ModernTextureProperties.reload((SimpleReloadableResourceManager) (Object) this);
        original.call();
        if (!previousMacros.equals(ModernTextureProperties.blockAtlasMacros())) {
            Shaders.uninit();
        }
    }
}
