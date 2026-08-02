package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.texture;

import com.google.common.collect.ImmutableSet;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
// [AUDIT-ISSUE] unused import ProgressManager left over (OF onResourceManagerReload has no ProgressManager - verified); remove
import net.minecraftforge.fml.common.ProgressManager;
import net.optifine.CustomGuis;
import net.optifine.EmissiveTextures;
import net.optifine.RandomEntities;
import net.optifine.shaders.ShadersTex;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;
@Mixin(TextureManager.class)
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 1
public abstract class MixinTextureManager {
    @Unique
    // [AUDIT-OK] OF-added fields boundTexture/boundTextureLocation (@Unique), not in baseline
    private ITextureObject boundTexture;
    @Unique
    private ResourceLocation boundTextureLocation;

    @WrapMethod(method = "bindTexture")
    // [AUDIT-OK] target bindTexture(ResourceLocation)V matches baseline; RandomEntities/CustomGuis rewrite matches OF bindTexture
    public void beforeBindTexture(ResourceLocation resource, Operation<Void> original){
        if (Config.isRandomEntities()) {
            resource = RandomEntities.getTextureLocation(resource);
        }

        if (Config.isCustomGuis()) {
            resource = CustomGuis.getTextureLocation(resource);
        }
        original.call(resource);
    }

    @WrapOperation(method = "bindTexture", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
    // [AUDIT-OK] target bindTexture; emissive substitution on Map.get matches OF (null-safe like OF)
    public Object processTexture(Map<ResourceLocation, ITextureObject> instance, Object o, Operation<ITextureObject> original){
        ITextureObject iTextureObject = original.call(instance, o);
        if (EmissiveTextures.isActive()) {
            iTextureObject = EmissiveTextures.getEmissiveTexture(iTextureObject, instance);
        }
        return iTextureObject;
    }

    @WrapOperation(method = "bindTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureUtil;bindTexture(I)V"))
    // [AUDIT-OK] target bindTexture; shaders bind + boundTexture/boundTextureLocation tracking matches OF
    public void modifyBindTexture(int p_94277_0_, Operation<Void> original, @Local(ordinal = 0) ITextureObject iTextureObject, @Local(argsOnly = true) ResourceLocation resource){
        if (Config.isShaders()) {
            ShadersTex.bindTexture(iTextureObject);
        } else {
            original.call(p_94277_0_);
        }

        this.boundTexture = iTextureObject;
        this.boundTextureLocation = resource;
    }

    @WrapMethod(method = "getDynamicTextureLocation")
    // [AUDIT-OK] target getDynamicTextureLocation(String,DynamicTexture) matches baseline; logo -> Config.getMojangLogoTexture like OF
    public ResourceLocation mojangLogo(String name, DynamicTexture texture, Operation<ResourceLocation> original){
        if ("logo".equals(name)) {
            texture = Config.getMojangLogoTexture(texture);
        }
        return original.call(name, texture);
    }

    @Shadow @Final
    // [AUDIT-OK] baseline member mapTextureObjects exists in TextureManager
    private Map<ResourceLocation, ITextureObject> mapTextureObjects;

    @Unique
    // [AUDIT-OK] OF-added @Unique method reloadBannerTextures (ImmutableSet copy == OF HashSet copy), not in baseline
    public void reloadBannerTextures() {
        for (Map.Entry<ResourceLocation, ITextureObject> entry : ImmutableSet.copyOf(this.mapTextureObjects.entrySet())) {
            ResourceLocation loc = entry.getKey();
            ITextureObject tex = entry.getValue();
            if (tex instanceof LayeredColorMaskTexture) {
                this.loadTexture(loc, tex);
            }
        }
    }

    @Shadow
    // [AUDIT-OK] baseline member loadTexture exists in TextureManager
    public abstract boolean loadTexture(ResourceLocation textureLocation, final ITextureObject textureObj);

    @Unique
    // [AUDIT-OK] OF-added @Unique method getBoundTexture, not in baseline
    public ITextureObject getBoundTexture() {
        return this.boundTexture;
    }

    @Unique
    // [AUDIT-OK] OF-added @Unique method getBoundTextureLocation, not in baseline
    public ResourceLocation getBoundTextureLocation() {
        return this.boundTextureLocation;
    }

    /**
     * @author
     * @reason
     */
    @WrapMethod(method = "onResourceManagerReload")
    // [AUDIT-OK] target onResourceManagerReload(IResourceManager)V matches baseline; body mirrors OF (no ProgressManager in OF - verified); [AUDIT-NOTE] empty @author/@reason javadoc
    public void removeOptifineDynamicTexture(IResourceManager resourceManager, Operation<Void> original) {
        Config.dbg("*** Reloading textures ***");
        Config.log("Resource packs: " + Config.getResourcePackNames());

        this.mapTextureObjects.entrySet().removeIf(
                (entry) -> {
                    var location = entry.getKey();
                    var tex = entry.getValue();
                    if (StringUtils.startsWithAny(location.getPath(), "mcpatcher/", "optifine/")
                            || EmissiveTextures.isEmissive(location)) {
                        if (tex instanceof AbstractTexture at) {
                            at.deleteGlTexture();
                        }
                        return true;
                    }
                    return false;
                }
        );

        EmissiveTextures.update();

        original.call(resourceManager);
    }
}
