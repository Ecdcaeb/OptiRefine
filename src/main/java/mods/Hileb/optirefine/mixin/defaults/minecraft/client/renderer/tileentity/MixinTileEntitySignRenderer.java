package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.tileentity;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.Public;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntitySign;
import net.optifine.CustomColors;
import net.optifine.shaders.Shaders;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
@Mixin(TileEntitySignRenderer.class)
public abstract class MixinTileEntitySignRenderer {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0
    @SuppressWarnings("AddedMixinMembersNamePattern")
    // [AUDIT-OK] OF-added static (OF:21)
    private static double textRenderDistanceSq = 4096.0;

    @Redirect(method = "render(Lnet/minecraft/tileentity/TileEntitySign;DDDFIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I"))
    // [AUDIT-OK] both drawString INVOKEs redirected; getSignTextColor matches OF:78
    public int optiRefine$drawCustomSignTextColor(FontRenderer fontRenderer, String text, int x, int y, int color){
        if (Config.isCustomColors()) {
            return fontRenderer.drawString(text, x, y, CustomColors.getSignTextColor(color));
        } else return fontRenderer.drawString(text, x, y, color);
    }


    @Definition(id = "destroyStage", local = @Local(type = int.class, argsOnly = true))
    @Expression("destroyStage < 0")
    @ModifyExpressionValue(method = "render(Lnet/minecraft/tileentity/TileEntitySign;DDDFIF)V", at = @At("MIXINEXTRAS:EXPRESSION"))
    // destroyStage is the only int argument; the tile entity is captured from arguments too.
    public boolean optiRefine$cullDistantSignText(boolean original, @Local(argsOnly = true) TileEntitySign sign){
        return original && isRenderText(sign);
    }

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    // [AUDIT-OK] matches OF isRenderText:108-122
    private static boolean isRenderText(TileEntitySign tileEntity) {
        if (Shaders.isShadowPass) {
            return false;
        } else {
            if (!Config.zoomMode && tileEntity.lineBeingEdited < 0) {
                Entity viewEntity = Config.getMinecraft().getRenderViewEntity();
                return !(viewEntity != null && tileEntity.getDistanceSq(viewEntity.posX, viewEntity.posY, viewEntity.posZ) > textRenderDistanceSq);
            }
            return true;
        }
    }

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Public
    // [AUDIT-OK] OF-added static (OF:124); @Public private static correct (called from OF RenderGlobal:763)
    private static void updateTextRenderDistance() {
        Minecraft mc = Config.getMinecraft();
        double fov = Config.limit(mc.gameSettings.fovSetting, 1.0F, 120.0F);
        double textRenderDistance = Math.max(1.5 * mc.displayHeight / fov, 16.0);
        textRenderDistanceSq = textRenderDistance * textRenderDistance;
    }

}
