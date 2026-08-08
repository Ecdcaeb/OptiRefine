package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity.layers;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomItems;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
@Mixin(LayerElytra.class)
public abstract class MixinLayerElytra {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
    @Shadow @Final
    // [AUDIT-OK] baseline static final member TEXTURE_ELYTRA declared in LayerElytra (deobf:19)
    private static ResourceLocation TEXTURE_ELYTRA;


    @Redirect(method = "doRenderLayer", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/layers/LayerElytra;TEXTURE_ELYTRA:Lnet/minecraft/util/ResourceLocation;"))
    // [AUDIT-OK] both TEXTURE_ELYTRA field reads redirected; getCustomElytraTexture fallback matches OF:37-47
    public ResourceLocation customTexture(@Local(argsOnly = true) EntityLivingBase entityLivingBaseIn){
        if (Config.isCustomItems()) {
            ItemStack itemStack = entityLivingBaseIn.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            return CustomItems.getCustomElytraTexture(itemStack, TEXTURE_ELYTRA);
        } else return TEXTURE_ELYTRA;
    }

    @SuppressWarnings({"unused", "MissingUnique"})
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.entity.AbstractClientPlayer hasElytraCape ()Z")
    // [AUDIT-FIXED] OF:34 cape-fallback branch requires hasElytraCape(); the member exists at runtime only
    // (MixinAbstractClientPlayer @Public) so it is not on the compile classpath -> own bridge, declared here
    private static native boolean _acc_AbstractClientPlayer_hasElytraCape_(AbstractClientPlayer player);

    @ModifyExpressionValue(method = "doRenderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;hasPlayerInfo()Z"))
    // [AUDIT-FIXED] OF:34 `hasElytraCape() && hasPlayerInfo() && getLocationCape() != null && isWearing(CAPE)`;
    // runtime branch lacks hasElytraCape() so the elytra texture falls back to the cape even for non-cape skins
    private boolean optiRefine$hasElytraCapeGate(boolean original, @Local AbstractClientPlayer abstractclientplayer) {
        return original && _acc_AbstractClientPlayer_hasElytraCape_(abstractclientplayer);
    }
}
