package mods.Hileb.optirefine.mixin.minecraft.client.renderer.entity.layers;

import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LayerElytra.class)
public class MixinLayerElytra {
    @Shadow @Final
    private static ResourceLocation TEXTURE_ELYTRA;


    @Redirect(method = "doRenderLayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/layers/LayerElytra;TEXTURE_ELYTRA:Lnet/minecraft/util/ResourceLocation;"))
    public ResourceLocation customTexture(@Local(argsOnly = true) EntityLivingBase entityLivingBaseIn){
        if (Config.isCustomItems()) {
            ItemStack itemStack = entityLivingBaseIn.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            return CustomItems.getCustomElytraTexture(itemStack, TEXTURE_ELYTRA);
        } else return TEXTURE_ELYTRA;
    }
}
