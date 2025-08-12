package mods.Hileb.optirefine.mixin.minecraft.client.renderer.block.model;

import com.google.common.collect.Maps;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomItems;
import net.optifine.util.StrUtils;
import net.optifine.util.TextureUtils;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Objects;

@Mixin(ModelBakery.class)
public abstract class MixinModelBakery {
    @Inject(method = "loadVariantItemModels", at = @At("TAIL"))
    public void $loadVariantItemModels(CallbackInfo ci) {
        CustomItems.update();
        CustomItems.loadModels((ModelBakery)(Object) this);
    }

    @ModifyReturnValue(method = "getModelLocation", at = @At("RETURN"))
    public ResourceLocation modifyMcPatcherLocation(ResourceLocation original, @Local(argsOnly = true) ResourceLocation arg){
        String path = arg.getPath();
        String name = arg.getNamespace();
        if (!path.startsWith("mcpatcher") && !path.startsWith("optifine")) {
            return original;
        } else {
            if (!path.endsWith(".json")) {
                return new ResourceLocation(name, path + ".json");
            }
            return arg;
        }
    }


    private static String fixResourcePath(String path, String basePath) {
        path = TextureUtils.fixResourcePath(path, basePath);
        path = StrUtils.removeSuffix(path, ".json");
        return StrUtils.removeSuffix(path, ".png");
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique @Public
    private static ResourceLocation fixModelLocation(ResourceLocation loc, String basePath) {
        if (loc != null && basePath != null) {
            if (!loc.getNamespace().equals("minecraft")) {
                return loc;
            } else {
                String path = loc.getPath();
                String pathFixed = fixResourcePath(path, basePath);
                if (!Objects.equals(pathFixed, path)) {
                    loc = new ResourceLocation(loc.getNamespace(), pathFixed);
                }

                return loc;
            }
        } else {
            return loc;
        }
    }

    @AccessibleOperation(opcode = Opcodes.PUTFIELD, desc = "net.minecraft.client.renderer.block.model.ModelBlock field_178316_e Lnet.minecraft.util.ResourceLocation;", deobf = true)
    private static native void ModelBlock_parentLocation_set(ModelBlock modelBlock, ResourceLocation resourceLocation);

    @Public @Unique
    private static void fixModelLocations(ModelBlock modelBlock, String basePath) {
        ResourceLocation parentLocFixed = fixModelLocation(modelBlock.getParentLocation(), basePath);
        if (parentLocFixed != modelBlock.getParentLocation()) {
            ModelBlock_parentLocation_set(modelBlock, parentLocFixed);
        }

        if (modelBlock.textures != null) {
            for (Map.Entry<String, String> entry : modelBlock.textures.entrySet()) {
                String path = entry.getValue();
                String pathFixed = fixResourcePath(path, basePath);
                if (!Objects.equals(pathFixed, path)) {
                    entry.setValue(pathFixed);
                }
            }
        }
    }

    @Shadow @Final
    private Map<ResourceLocation, ModelBlock> models;

    @Unique
    public ModelBlock getModelBlock(ResourceLocation resourceLocation) {
        return this.models.get(resourceLocation);
    }

    @WrapOperation(method = "loadModel", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/block/model/ModelBlock;name:Ljava/lang/String;"))
    public void fixLocation(ModelBlock instance, String value, Operation<Void> original, @Local(argsOnly = true) ResourceLocation resourceLocation){
        original.call(instance, value);
        String basePath = TextureUtils.getBasePath(resourceLocation.getPath());
        fixModelLocations(instance, basePath);
    }
}
