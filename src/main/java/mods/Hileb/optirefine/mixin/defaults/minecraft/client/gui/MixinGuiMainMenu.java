package mods.Hileb.optirefine.mixin.defaults.minecraft.client.gui;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomPanorama;
import net.optifine.CustomPanoramaProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen {
// [AUDIT] 2026-08-03 - see AGENT.md; issues: 0


// [AUDIT-OK] target drawPanorama(IIF)V matches baseline; single int-64 constant (loop bound)
    @ModifyConstant(method = "drawPanorama", constant = @Constant(intValue = 64))
    public int optiRefine$getPanoramaBlurSamples(int defaultSamples) {
        CustomPanoramaProperties properties = CustomPanorama.getCustomPanoramaProperties();
        if (properties != null) {
            return properties.getBlur1();
        } else return defaultSamples;
    }

// [AUDIT-OK] baseline static final member TITLE_PANORAMA_PATHS declared in GuiMainMenu (deobf:66)
    @Shadow
    @Final
    private static ResourceLocation[] TITLE_PANORAMA_PATHS;

// [AUDIT-OK] OF drawPanorama: var14 = var8.getPanoramaLocations() replaces TITLE_PANORAMA_PATHS (single GETSTATIC read, deobf:385)
    @Redirect(method = "drawPanorama", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/GuiMainMenu;TITLE_PANORAMA_PATHS:[Lnet/minecraft/util/ResourceLocation;"))
    public ResourceLocation[] optiRefine$getPanoramaTextures() {
        CustomPanoramaProperties properties = CustomPanorama.getCustomPanoramaProperties();
        if (properties != null) {
            return properties.getPanoramaLocations();
        } else {
            return TITLE_PANORAMA_PATHS;
        }
    }

// [AUDIT-OK] target rotateAndBlurSkybox()V matches baseline
// [AUDIT-NOTE] two int-3 constants (int i=3; and j<3;) both replaced with blur2; i unused so result == OF blur2 loop count
    @ModifyConstant(method = "rotateAndBlurSkybox", constant = @Constant(intValue = 3))
    public int optiRefine$getSkyboxBlurPasses(int defaultPasses){
        CustomPanoramaProperties properties = CustomPanorama.getCustomPanoramaProperties();
        if (properties != null) {
            return properties.getBlur2();
        } else return defaultPasses;
    }

// [AUDIT-OK] baseline member rotateAndBlurSkybox exists in target class
    @Shadow
    protected abstract void rotateAndBlurSkybox();

// [AUDIT-OK] baseline member drawPanorama exists in target class
    @Shadow
    protected abstract void drawPanorama(int mouseX, int mouseY, float partialTicks);

// [AUDIT-OK] baseline member drawScreen exists in target class
    @Shadow public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);

// [AUDIT-FIXED] target renderSkybox(IIF)V matches baseline; handler params match; replicates OF renderSkybox (total passes = 1 + 2*blur3)
    @WrapMethod(method = "renderSkybox")
    public void optiRefine$renderCustomPanoramaSkybox(int mouseX, int mouseY, float partialTicks, Operation<Void> original){
        CustomPanoramaProperties properties = CustomPanorama.getCustomPanoramaProperties();
        if (properties != null) {
            this.mc.getFramebuffer().unbindFramebuffer();
            GlStateManager.viewport(0, 0, 256, 256);
            this.drawPanorama(mouseX, mouseY, partialTicks);
            this.rotateAndBlurSkybox();
            for (int i = 0; i < properties.getBlur3(); i++) {
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

// [AUDIT-OK] target drawScreen matches baseline; two drawGradientRect invokes present; handler distinguishes by start color
    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;drawGradientRect(IIIIII)V"))
    public void optiRefine$drawCustomPanoramaOverlay(GuiMainMenu screen, int left, int top, int right, int bottom, int topColor, int bottomColor){
        CustomPanoramaProperties properties = CustomPanorama.getCustomPanoramaProperties();
        if (properties != null) {
            if (topColor == -2130706433) {
                this.drawGradientRect(left, top, right, bottom, properties.getOverlay1Top(), properties.getOverlay1Bottom());
            } else if (topColor == 0) {
                this.drawGradientRect(left, top, right, bottom, properties.getOverlay2Top(), properties.getOverlay2Bottom());
            } else this.drawGradientRect(left, top, right, bottom, topColor, bottomColor);
        } else this.drawGradientRect(left, top, right, bottom, topColor, bottomColor);
    }

}
