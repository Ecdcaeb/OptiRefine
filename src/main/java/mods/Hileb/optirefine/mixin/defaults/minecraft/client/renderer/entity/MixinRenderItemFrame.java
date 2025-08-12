package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItemFrame;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.optifine.shaders.Shaders;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderItemFrame.class)
public abstract class MixinRenderItemFrame {

    @Shadow @Final
    private Minecraft mc;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private static double itemRenderDistanceSq = 4096.0;

    @ModifyExpressionValue(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
    public boolean extraRenderCondition(boolean original, @Local(argsOnly = true) EntityItemFrame itemFrame){
        if (!original) {
            if (!this.isRenderItem(itemFrame)) {
                return true;
            }

            if (!Config.zoomMode) {
                Entity player = this.mc.player;
                return itemFrame.getDistanceSq(player.posX, player.posY, player.posZ) > itemRenderDistanceSq;
            }
            return false;
        } else return true;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private boolean isRenderItem(EntityItemFrame itemFrame) {
        if (Shaders.isShadowPass) {
            return false;
        } else {
            if (!Config.zoomMode) {
                Entity viewEntity = this.mc.getRenderViewEntity();
                double distSq = itemFrame.getDistanceSq(viewEntity.posX, viewEntity.posY, viewEntity.posZ);
                return !(distSq > itemRenderDistanceSq);
            }

            return true;
        }
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique @Public
    private static void updateItemRenderDistance() {
        Minecraft mc = Config.getMinecraft();
        double fov = Config.limit(mc.gameSettings.fovSetting, 1.0F, 120.0F);
        double itemRenderDistance = Math.max(6.0 * mc.displayHeight / fov, 16.0);
        itemRenderDistanceSq = itemRenderDistance * itemRenderDistance;
    }
}
