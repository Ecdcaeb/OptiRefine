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
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 1

    @Shadow @Final
    // [AUDIT-OK] baseline member mc (private final Minecraft)
    private Minecraft mc;

    @SuppressWarnings("AddedMixinMembersNamePattern")
    // [AUDIT-OK] OF-added static (OF:33)
    private static double itemRenderDistanceSq = 4096.0;

    @ModifyExpressionValue(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
    // [AUDIT-OK] isEmpty() INVOKE unique; player-distance cull hardcodes 4096.0 (= OF:90-92, [AUDIT-FIXED]); isRenderItem uses itemRenderDistanceSq (= OF:144-158) — verified 2026-08-05
    public boolean extraRenderCondition(boolean original, @Local(argsOnly = true) EntityItemFrame itemFrame){
        if (!original) {
            if (!this.isRenderItem(itemFrame)) {
                return true;
            }

            if (!Config.zoomMode) {
                Entity player = this.mc.player;
                // [AUDIT-FIXED] OF hardcodes 4096.0 for the player-distance cull (dynamic value was looser)
                return itemFrame.getDistanceSq(player.posX, player.posY, player.posZ) > 4096.0;
            }
            return false;
        } else return true;
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    // [AUDIT-OK] matches OF isRenderItem:144-158
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
    @Public
    // [AUDIT-OK] OF-added static (OF:160); @Public private static correct (called from OF RenderGlobal:623)
    private static void updateItemRenderDistance() {
        Minecraft mc = Config.getMinecraft();
        double fov = Config.limit(mc.gameSettings.fovSetting, 1.0F, 120.0F);
        double itemRenderDistance = Math.max(6.0 * mc.displayHeight / fov, 16.0);
        itemRenderDistanceSq = itemRenderDistance * itemRenderDistance;
    }
}
