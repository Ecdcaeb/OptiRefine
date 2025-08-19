package mods.Hileb.optirefine.mixin.defaults.minecraft.client.renderer.entity;

import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.entity.RenderXPOrb;
import net.minecraft.entity.item.EntityXPOrb;
import net.optifine.CustomColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderXPOrb.class)
public abstract class MixinRenderXPOrb {
    //float u = ((float)entity.xpColor + partialTicks) / 2.0F;
    @Expression("@(? / 2.0)")
    @ModifyExpressionValue(method = "doRender(Lnet/minecraft/entity/item/EntityXPOrb;DDDFF)V", at = @At("MIXINEXTRAS:EXPRESSION"))
    public float customXPColor(float original, @Share(namespace = "optirefine", value = "color")LocalFloatRef color){
        if (Config.isCustomColors()) {
            color.set(CustomColors.getXpOrbTimer(original));
            return color.get();
        } else {
            color.set(original);
            return original;
        }
    }

    @Inject(method = "doRender(Lnet/minecraft/entity/item/EntityXPOrb;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BufferBuilder;begin(ILnet/minecraft/client/renderer/vertex/VertexFormat;)V"))
    public void hookBeforeDraw(EntityXPOrb entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci,
                               @Share(namespace = "optirefine", value = "r") LocalIntRef r,
                               @Share(namespace = "optirefine", value = "g") LocalIntRef g,
                               @Share(namespace = "optirefine", value = "b") LocalIntRef b,
                               @Share(namespace = "optirefine", value = "color")LocalFloatRef color
                               ){
        if (Config.isCustomColors()) {
            int col = CustomColors.getXpOrbColor(color.get());
            if (col >= 0) {
                r.set(col >> 16 & 0xFF);
                g.set(col >> 8 & 0xFF);
                //noinspection PointlessBitwiseExpression
                b.set(col >> 0 & 0xFF);
            }
        } else {
            r.set(-1);
            g.set(-1);
            b.set(-1);
        }
    }

    @Redirect(method = "doRender(Lnet/minecraft/entity/item/EntityXPOrb;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BufferBuilder;color(IIII)Lnet/minecraft/client/renderer/BufferBuilder;"))
    public BufferBuilder setColor(BufferBuilder instance, int red, int green, int blue, int alpha,
                                  @Share(namespace = "optirefine", value = "r") LocalIntRef r,
                                  @Share(namespace = "optirefine", value = "g") LocalIntRef g,
                                  @Share(namespace = "optirefine", value = "b") LocalIntRef b
    ){
        return instance.color(r.get() < 0 ? red : r.get(), g.get() < 0 ? green : g.get(), b.get() < 0 ? blue : b.get(), alpha);
    }


}
/*
@@ -6,12 +6,13 @@
 import net.minecraft.client.renderer.RenderHelper;
 import net.minecraft.client.renderer.Tessellator;
 import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
 import net.minecraft.entity.item.EntityXPOrb;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.util.math.MathHelper;
+import net.optifine.CustomColors;

 public class RenderXPOrb extends Render<EntityXPOrb> {
    private static final ResourceLocation EXPERIENCE_ORB_TEXTURES = new ResourceLocation("textures/entity/experience_orb.png");

    public RenderXPOrb(RenderManager var1) {
       super(var1);
@@ -35,30 +36,46 @@
          float var17 = 0.25F;
          int var18 = var1.getBrightnessForRender();
          int var19 = var18 % 65536;
          int var20 = var18 / 65536;
          OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var19, var20);
          GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
-         float var26 = 255.0F;
-         float var27 = (var1.xpColor + var9) / 2.0F;
-         var20 = (int)((MathHelper.sin(var27 + 0.0F) + 1.0F) * 0.5F * 255.0F);
-         short var21 = 255;
-         int var22 = (int)((MathHelper.sin(var27 + (float) (Math.PI * 4.0 / 3.0)) + 1.0F) * 0.1F * 255.0F);
+         float var21 = 255.0F;
+         float var22 = (var1.xpColor + var9) / 2.0F;
+         if (Config.isCustomColors()) {
+            var22 = CustomColors.getXpOrbTimer(var22);
+         }
+
+         var20 = (int)((MathHelper.sin(var22 + 0.0F) + 1.0F) * 0.5F * 255.0F);
+         short var23 = 255;
+         int var24 = (int)((MathHelper.sin(var22 + (float) (Math.PI * 4.0 / 3.0)) + 1.0F) * 0.1F * 255.0F);
          GlStateManager.translate(0.0F, 0.1F, 0.0F);
          GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
          GlStateManager.rotate((this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
-         float var23 = 0.3F;
+         float var25 = 0.3F;
          GlStateManager.scale(0.3F, 0.3F, 0.3F);
-         Tessellator var24 = Tessellator.getInstance();
-         BufferBuilder var25 = var24.getBuffer();
-         var25.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
-         var25.pos(-0.5, -0.25, 0.0).tex(var11, var14).color(var20, 255, var22, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
-         var25.pos(0.5, -0.25, 0.0).tex(var12, var14).color(var20, 255, var22, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
-         var25.pos(0.5, 0.75, 0.0).tex(var12, var13).color(var20, 255, var22, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
-         var25.pos(-0.5, 0.75, 0.0).tex(var11, var13).color(var20, 255, var22, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
-         var24.draw();
+         Tessellator var26 = Tessellator.getInstance();
+         BufferBuilder var27 = var26.getBuffer();
+         var27.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
+         int var28 = var20;
+         int var29 = 255;
+         int var30 = var24;
+         if (Config.isCustomColors()) {
+            int var31 = CustomColors.getXpOrbColor(var22);
+            if (var31 >= 0) {
+               var28 = var31 >> 16 & 0xFF;
+               var29 = var31 >> 8 & 0xFF;
+               var30 = var31 >> 0 & 0xFF;
+            }
+         }
+
+         var27.pos(-0.5, -0.25, 0.0).tex(var11, var14).color(var28, var29, var30, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
+         var27.pos(0.5, -0.25, 0.0).tex(var12, var14).color(var28, var29, var30, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
+         var27.pos(0.5, 0.75, 0.0).tex(var12, var13).color(var28, var29, var30, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
+         var27.pos(-0.5, 0.75, 0.0).tex(var11, var13).color(var28, var29, var30, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
+         var26.draw();
          GlStateManager.disableBlend();
          GlStateManager.disableRescaleNormal();
          GlStateManager.popMatrix();
          super.doRender(var1, var2, var4, var6, var8, var9);
       }
    }
 */
