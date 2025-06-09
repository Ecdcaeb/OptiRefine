package mods.Hileb.optirefine.mixin.minecraft.client.renderer.tileentity;

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
    @Unique
    private static double textRenderDistanceSq = 4096.0;

    @Redirect(method = "render(Lnet/minecraft/tileentity/TileEntitySign;DDDFIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I"))
    public int injectdrawStringColor(FontRenderer instance, String p_78276_1_, int p_78276_2_, int p_78276_3_, int p_78276_4_){
        if (Config.isCustomColors()) {
            return instance.drawString(p_78276_1_, p_78276_2_, p_78276_3_, CustomColors.getSignTextColor(p_78276_4_));
        } else return instance.drawString(p_78276_1_, p_78276_2_, p_78276_3_, p_78276_4_);
    }


    @Definition(id = "p_192841_9_", local = @Local(ordinal = 6))
    @Expression("p_192841_9_ < 0")
    @ModifyExpressionValue(method = "render(Lnet/minecraft/tileentity/TileEntitySign;DDDFIF)V")
    public boolean disableTextRendering(boolean o, @Local(ordinal = 1) TileEntitySign entity){
        return o && isRenderText(entity);
    }

    @Unique
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

    @Unique @Public
    private static void updateTextRenderDistance() {
        Minecraft mc = Config.getMinecraft();
        double fov = Config.limit(mc.gameSettings.fovSetting, 1.0F, 120.0F);
        double textRenderDistance = Math.max(1.5 * mc.displayHeight / fov, 16.0);
        textRenderDistanceSq = textRenderDistance * textRenderDistance;
    }

}
