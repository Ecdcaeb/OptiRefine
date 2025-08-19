package mods.Hileb.optirefine.mixin.defaults.minecraft.client.gui;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import net.minecraft.client.gui.GuiSlot;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiSlot.class)
public abstract class MixinGuiSlot{
    @SuppressWarnings("unused")
    @Shadow
    public int width;
    @SuppressWarnings("unused")
    @Shadow
    public int height;
    @Shadow
    public int top;
    @Shadow
    public int bottom;
    @SuppressWarnings("unused")
    @Shadow
    public int right;
    @SuppressWarnings("unused")
    @Shadow
    public int left;
    @Shadow
    @Final
    public int slotHeight;

    @Shadow
    protected abstract void drawSlot(int i1, int i2, int i3, int i4, int i5, int i6, float v);

    
    @Redirect(method = "drawSelectionBox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiSlot;drawSlot(IIIIIIF)V"))
    public void injectDrawSelectionBox(GuiSlot instance, int i1, int i2, int i3, int i4, int i5, int i6, float v){
        if (!_is_GuiResourcePackList() || i3 >= this.top - this.slotHeight && i3 <= this.bottom) {
            this.drawSlot(i1, i2, i3, i4, i5, i6, v);
        }
    }

    @Unique
    @AccessibleOperation(opcode = Opcodes.INSTANCEOF, desc = "net.minecraft.client.gui.GuiResourcePackList")
    public boolean _is_GuiResourcePackList(){
        throw new AbstractMethodError();
    }
}

/*
+++ net/minecraft/client/gui/GuiSlot.java	Tue Aug 19 14:59:58 2025
@@ -149,28 +149,20 @@
          int var5 = var4 + 6;
          this.bindAmountScrolled();
          GlStateManager.disableLighting();
          GlStateManager.disableFog();
          Tessellator var6 = Tessellator.getInstance();
          BufferBuilder var7 = var6.getBuffer();
-         this.mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
-         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
-         float var8 = 32.0F;
-         var7.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
-         var7.pos(this.left, this.bottom, 0.0).tex(this.left / 32.0F, (this.bottom + (int)this.amountScrolled) / 32.0F).color(32, 32, 32, 255).endVertex();
-         var7.pos(this.right, this.bottom, 0.0).tex(this.right / 32.0F, (this.bottom + (int)this.amountScrolled) / 32.0F).color(32, 32, 32, 255).endVertex();
-         var7.pos(this.right, this.top, 0.0).tex(this.right / 32.0F, (this.top + (int)this.amountScrolled) / 32.0F).color(32, 32, 32, 255).endVertex();
-         var7.pos(this.left, this.top, 0.0).tex(this.left / 32.0F, (this.top + (int)this.amountScrolled) / 32.0F).color(32, 32, 32, 255).endVertex();
-         var6.draw();
-         int var9 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
-         int var10 = this.top + 4 - (int)this.amountScrolled;
+         this.drawContainerBackground(var6);
+         int var8 = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
+         int var9 = this.top + 4 - (int)this.amountScrolled;
          if (this.hasListHeader) {
-            this.drawListHeader(var9, var10, var6);
+            this.drawListHeader(var8, var9, var6);
          }

-         this.drawSelectionBox(var9, var10, var1, var2, var3);
+         this.drawSelectionBox(var8, var9, var1, var2, var3);
          GlStateManager.disableDepth();
          this.overlayBackground(0, this.top, 255, 255);
          this.overlayBackground(this.bottom, this.height, 255, 255);
          GlStateManager.enableBlend();
          GlStateManager.tryBlendFuncSeparate(
             GlStateManager.SourceFactor.SRC_ALPHA,
@@ -178,51 +170,51 @@
             GlStateManager.SourceFactor.ZERO,
             GlStateManager.DestFactor.ONE
          );
          GlStateManager.disableAlpha();
          GlStateManager.shadeModel(7425);
          GlStateManager.disableTexture2D();
-         byte var11 = 4;
+         byte var10 = 4;
          var7.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
          var7.pos(this.left, this.top + 4, 0.0).tex(0.0, 1.0).color(0, 0, 0, 0).endVertex();
          var7.pos(this.right, this.top + 4, 0.0).tex(1.0, 1.0).color(0, 0, 0, 0).endVertex();
          var7.pos(this.right, this.top, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
          var7.pos(this.left, this.top, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
          var6.draw();
          var7.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
          var7.pos(this.left, this.bottom, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
          var7.pos(this.right, this.bottom, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
          var7.pos(this.right, this.bottom - 4, 0.0).tex(1.0, 0.0).color(0, 0, 0, 0).endVertex();
          var7.pos(this.left, this.bottom - 4, 0.0).tex(0.0, 0.0).color(0, 0, 0, 0).endVertex();
          var6.draw();
-         int var12 = this.getMaxScroll();
-         if (var12 > 0) {
-            int var13 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
-            var13 = MathHelper.clamp(var13, 32, this.bottom - this.top - 8);
-            int var14 = (int)this.amountScrolled * (this.bottom - this.top - var13) / var12 + this.top;
-            if (var14 < this.top) {
-               var14 = this.top;
+         int var11 = this.getMaxScroll();
+         if (var11 > 0) {
+            int var12 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
+            var12 = MathHelper.clamp(var12, 32, this.bottom - this.top - 8);
+            int var13 = (int)this.amountScrolled * (this.bottom - this.top - var12) / var11 + this.top;
+            if (var13 < this.top) {
+               var13 = this.top;
             }

             var7.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
             var7.pos(var4, this.bottom, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
             var7.pos(var5, this.bottom, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
             var7.pos(var5, this.top, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
             var7.pos(var4, this.top, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
             var6.draw();
             var7.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
-            var7.pos(var4, var14 + var13, 0.0).tex(0.0, 1.0).color(128, 128, 128, 255).endVertex();
-            var7.pos(var5, var14 + var13, 0.0).tex(1.0, 1.0).color(128, 128, 128, 255).endVertex();
-            var7.pos(var5, var14, 0.0).tex(1.0, 0.0).color(128, 128, 128, 255).endVertex();
-            var7.pos(var4, var14, 0.0).tex(0.0, 0.0).color(128, 128, 128, 255).endVertex();
+            var7.pos(var4, var13 + var12, 0.0).tex(0.0, 1.0).color(128, 128, 128, 255).endVertex();
+            var7.pos(var5, var13 + var12, 0.0).tex(1.0, 1.0).color(128, 128, 128, 255).endVertex();
+            var7.pos(var5, var13, 0.0).tex(1.0, 0.0).color(128, 128, 128, 255).endVertex();
+            var7.pos(var4, var13, 0.0).tex(0.0, 0.0).color(128, 128, 128, 255).endVertex();
             var6.draw();
             var7.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
-            var7.pos(var4, var14 + var13 - 1, 0.0).tex(0.0, 1.0).color(192, 192, 192, 255).endVertex();
-            var7.pos(var5 - 1, var14 + var13 - 1, 0.0).tex(1.0, 1.0).color(192, 192, 192, 255).endVertex();
-            var7.pos(var5 - 1, var14, 0.0).tex(1.0, 0.0).color(192, 192, 192, 255).endVertex();
-            var7.pos(var4, var14, 0.0).tex(0.0, 0.0).color(192, 192, 192, 255).endVertex();
+            var7.pos(var4, var13 + var12 - 1, 0.0).tex(0.0, 1.0).color(192, 192, 192, 255).endVertex();
+            var7.pos(var5 - 1, var13 + var12 - 1, 0.0).tex(1.0, 1.0).color(192, 192, 192, 255).endVertex();
+            var7.pos(var5 - 1, var13, 0.0).tex(1.0, 0.0).color(192, 192, 192, 255).endVertex();
+            var7.pos(var4, var13, 0.0).tex(0.0, 0.0).color(192, 192, 192, 255).endVertex();
             var6.draw();
          }

          this.renderDecorations(var1, var2);
          GlStateManager.enableTexture2D();
          GlStateManager.shadeModel(7424);
@@ -346,13 +338,15 @@
             var8.pos(var13 - 1, var10 - 1, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
             var8.pos(var12 + 1, var10 - 1, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
             var7.draw();
             GlStateManager.enableTexture2D();
          }

-         this.drawSlot(var9, var1, var10, var11, var3, var4, var5);
+         if (!(this instanceof GuiResourcePackList) || var10 >= this.top - this.slotHeight && var10 <= this.bottom) {
+            this.drawSlot(var9, var1, var10, var11, var3, var4, var5);
+         }
       }
    }

    protected int getScrollBarX() {
       return this.width / 2 + 124;
    }
@@ -375,8 +369,21 @@
       this.left = var1;
       this.right = var1 + this.width;
    }

    public int getSlotHeight() {
       return this.slotHeight;
+   }
+
+   protected void drawContainerBackground(Tessellator var1) {
+      BufferBuilder var2 = var1.getBuffer();
+      this.mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
+      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
+      float var3 = 32.0F;
+      var2.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
+      var2.pos(this.left, this.bottom, 0.0).tex(this.left / 32.0F, (this.bottom + (int)this.amountScrolled) / 32.0F).color(32, 32, 32, 255).endVertex();
+      var2.pos(this.right, this.bottom, 0.0).tex(this.right / 32.0F, (this.bottom + (int)this.amountScrolled) / 32.0F).color(32, 32, 32, 255).endVertex();
+      var2.pos(this.right, this.top, 0.0).tex(this.right / 32.0F, (this.top + (int)this.amountScrolled) / 32.0F).color(32, 32, 32, 255).endVertex();
+      var2.pos(this.left, this.top, 0.0).tex(this.left / 32.0F, (this.top + (int)this.amountScrolled) / 32.0F).color(32, 32, 32, 255).endVertex();
+      var1.draw();
    }
 }
 */
