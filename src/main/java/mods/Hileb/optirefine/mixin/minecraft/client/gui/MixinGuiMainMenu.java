package mods.Hileb.optirefine.mixin.minecraft.client.gui;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.optifine.CustomPanorama;
import net.optifine.CustomPanoramaProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen {

    @ModifyConstant(method = "drawPanorama", constant = @Constant(intValue = 64))
    public int injectDrawPanorama(int value) {
        CustomPanoramaProperties cpp = CustomPanorama.getCustomPanoramaProperties();
        if (cpp != null) {
            return cpp.getBlur1();
        } else return value;
    }

    @ModifyConstant(method = "rotateAndBlurSkybox", constant = @Constant(intValue = 3))
    public int injectRotateAndBlurSkybox(int value){
        CustomPanoramaProperties cpp = CustomPanorama.getCustomPanoramaProperties();
        if (cpp != null) {
            return cpp.getBlur2();
        } else return value;
    }

    @Shadow
    protected abstract void rotateAndBlurSkybox();

    @Shadow
    protected abstract void drawPanorama(int p, int p1, float p2);

    @Shadow public abstract void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_);

    @WrapMethod(method = "renderSkybox")
    public void injectRenderSkybox(int mouseX, int mouseY, float partialTicks, Operation<Void> original){
        CustomPanoramaProperties cpp = CustomPanorama.getCustomPanoramaProperties();
        if (cpp != null) {
            this.mc.getFramebuffer().unbindFramebuffer();
            GlStateManager.viewport(0, 0, 256, 256);
            this.drawPanorama(mouseX, mouseY, partialTicks);
            for (int i = 0; i < cpp.getBlur3(); i++) {
                this.rotateAndBlurSkybox();
                this.rotateAndBlurSkybox();
            }
            this.mc.getFramebuffer().bindFramebuffer(true);
            GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
            float f = 120.0F / (float)(Math.max(this.width, this.height));
            float f1 = (float)this.height * f / 256.0F;
            float f2 = (float)this.width * f / 256.0F;
            int i = this.width;
            int j = this.height;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos(0.0F, j, this.zLevel).tex(0.5F - f1, 0.5F + f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
            bufferbuilder.pos(i, j, this.zLevel).tex(0.5F - f1, 0.5F - f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
            bufferbuilder.pos(i, 0.0F, this.zLevel).tex(0.5F + f1, 0.5F - f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
            bufferbuilder.pos(0.0F, 0.0F, this.zLevel).tex(0.5F + f1, 0.5F + f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
            tessellator.draw();
        } else original.call(mouseX, mouseY, partialTicks);
    }

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;drawGradientRect(IIIIII)V"))
    public void redirectDrawGradientRect1(GuiMainMenu instance, int i1, int i2, int i3, int i4, int i5, int i6){
        CustomPanoramaProperties cpp = CustomPanorama.getCustomPanoramaProperties();
        if (cpp != null) {
            if (i5 == -2130706433) {
                this.drawGradientRect(i1, i2, i3, i4, cpp.getOverlay1Top(), cpp.getOverlay1Bottom());
            } else if (i5 == 0) {
                this.drawGradientRect(i1, i2, i3, i4, cpp.getOverlay2Top(), cpp.getOverlay2Bottom());
            } else this.drawGradientRect(i1, i2, i3, i4, i5, i6);
        } else this.drawGradientRect(i1, i2, i3, i4, i5, i6);
    }




}
