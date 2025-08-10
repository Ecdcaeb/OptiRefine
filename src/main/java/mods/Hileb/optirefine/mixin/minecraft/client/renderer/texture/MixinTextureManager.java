package mods.Hileb.optirefine.mixin.minecraft.client.renderer.texture;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ProgressManager;
import net.optifine.CustomGuis;
import net.optifine.EmissiveTextures;
import net.optifine.RandomEntities;
import net.optifine.shaders.ShadersTex;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@Mixin(TextureManager.class)
public abstract class MixinTextureManager {
    @Unique
    private ITextureObject boundTexture;
    @Unique
    private ResourceLocation boundTextureLocation;

    @ModifyArgs(method = "bindTexture", at = @At("HEAD"))
    public void beforeBindTexture(Args args){
        ResourceLocation resource = args.get(1);
        if (Config.isRandomEntities()) {
            resource = RandomEntities.getTextureLocation(resource);
        }

        if (Config.isCustomGuis()) {
            resource = CustomGuis.getTextureLocation(resource);
        }
        args.set(1, resource);
    }

    @WrapOperation(method = "bindTexture", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
    public Object processTexture(Map<ResourceLocation, ITextureObject> instance, Object o, Operation<ITextureObject> original){
        ITextureObject iTextureObject = original.call(instance, o);
        if (EmissiveTextures.isActive()) {
            iTextureObject = EmissiveTextures.getEmissiveTexture(iTextureObject, instance);
        }
        return iTextureObject;
    }

    @WrapOperation(method = "bindTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureUtil;bindTexture(I)V"))
    public void modifyBindTexture(int p_94277_0_, Operation<Void> original, @Local(ordinal = 0) ITextureObject iTextureObject, @Local(argsOnly = true) ResourceLocation resource){
        if (Config.isShaders()) {
            ShadersTex.bindTexture(iTextureObject);
        } else {
            original.call(p_94277_0_);
        }

        this.boundTexture = iTextureObject;
        this.boundTextureLocation = resource;
    }

    @ModifyArgs(method = "getDynamicTextureLocation", at = @At("HEAD"))
    public void mojangLogo(Args args){
        if ("logo".equals(args.get(1))) {
            args.set(2, Config.getMojangLogoTexture(args.get(2)));
        }
    }

    @Shadow @Final
    private Map<ResourceLocation, ITextureObject> mapTextureObjects;

    @Unique
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
    public abstract boolean loadTexture(ResourceLocation textureLocation, final ITextureObject textureObj);

    @Unique
    public ITextureObject getBoundTexture() {
        return this.boundTexture;
    }

    @Unique
    public ResourceLocation getBoundTextureLocation() {
        return this.boundTextureLocation;
    }

    /**
     * @author
     * @reason
     */
    @WrapMethod(method = "onResourceManagerReload")
    public void removeOptifineDynamicTexture(IResourceManager resourceManager, Operation<Void> original) {
        Config.dbg("*** Reloading textures ***");
        Config.log("Resource packs: " + Config.getResourcePackNames());

        final ProgressManager.ProgressBar bar = ProgressManager.push("Excluding Optifine Texture Manager", this.mapTextureObjects.size(), true);

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
                    bar.step(location.toString());
                    return false;
                }
        );

        ProgressManager.pop(bar);

        EmissiveTextures.update();

        original.call(resourceManager);
    }
}
