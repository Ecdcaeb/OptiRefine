package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity.layers;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LayerArmorBase.class)
public abstract class MixinLayerArmorBase {

    @WrapOperation(method = "renderArmorLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/LayerArmorBase;getArmorResource(Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/inventory/EntityEquipmentSlot;Ljava/lang/String;)Lnet/minecraft/util/ResourceLocation;"))
    public ResourceLocation getResources(LayerArmorBase instance, Entity entity, ItemStack stack, EntityEquipmentSlot slot, String type, Operation<ResourceLocation> original){
        if (!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(stack, slot, type)) {
            return original.call(instance, entity, stack, slot, type);
        }
        return null;
    }

    @WrapWithCondition(method = "renderArmorLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderLivingBase;bindTexture(Lnet/minecraft/util/ResourceLocation;)V"))
    public boolean replace_renderer_bindTexture_this_getArmorResource(RenderLivingBase instance, ResourceLocation location) {
        return location != null;
    }
}
