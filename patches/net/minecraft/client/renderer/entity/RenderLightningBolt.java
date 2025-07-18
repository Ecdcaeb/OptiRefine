package net.minecraft.client.renderer.entity;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.ResourceLocation;

public class RenderLightningBolt extends Render<EntityLightningBolt> {
   public RenderLightningBolt(RenderManager var1) {
      super(☃);
   }

   public void doRender(EntityLightningBolt var1, double var2, double var4, double var6, float var8, float var9) {
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      GlStateManager.disableTexture2D();
      GlStateManager.disableLighting();
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
      double[] ☃xx = new double[8];
      double[] ☃xxx = new double[8];
      double ☃xxxx = 0.0;
      double ☃xxxxx = 0.0;
      Random ☃xxxxxx = new Random(☃.boltVertex);

      for (int ☃xxxxxxx = 7; ☃xxxxxxx >= 0; ☃xxxxxxx--) {
         ☃xx[☃xxxxxxx] = ☃xxxx;
         ☃xxx[☃xxxxxxx] = ☃xxxxx;
         ☃xxxx += ☃xxxxxx.nextInt(11) - 5;
         ☃xxxxx += ☃xxxxxx.nextInt(11) - 5;
      }

      for (int ☃xxxxxxx = 0; ☃xxxxxxx < 4; ☃xxxxxxx++) {
         Random ☃xxxxxxxx = new Random(☃.boltVertex);

         for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < 3; ☃xxxxxxxxx++) {
            int ☃xxxxxxxxxx = 7;
            int ☃xxxxxxxxxxx = 0;
            if (☃xxxxxxxxx > 0) {
               ☃xxxxxxxxxx = 7 - ☃xxxxxxxxx;
            }

            if (☃xxxxxxxxx > 0) {
               ☃xxxxxxxxxxx = ☃xxxxxxxxxx - 2;
            }

            double ☃xxxxxxxxxxxx = ☃xx[☃xxxxxxxxxx] - ☃xxxx;
            double ☃xxxxxxxxxxxxx = ☃xxx[☃xxxxxxxxxx] - ☃xxxxx;

            for (int ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxx; ☃xxxxxxxxxxxxxx >= ☃xxxxxxxxxxx; ☃xxxxxxxxxxxxxx--) {
               double ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxxx;
               double ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxx;
               if (☃xxxxxxxxx == 0) {
                  ☃xxxxxxxxxxxx += ☃xxxxxxxx.nextInt(11) - 5;
                  ☃xxxxxxxxxxxxx += ☃xxxxxxxx.nextInt(11) - 5;
               } else {
                  ☃xxxxxxxxxxxx += ☃xxxxxxxx.nextInt(31) - 15;
                  ☃xxxxxxxxxxxxx += ☃xxxxxxxx.nextInt(31) - 15;
               }

               ☃x.begin(5, DefaultVertexFormats.POSITION_COLOR);
               float ☃xxxxxxxxxxxxxxxxx = 0.5F;
               float ☃xxxxxxxxxxxxxxxxxx = 0.45F;
               float ☃xxxxxxxxxxxxxxxxxxx = 0.45F;
               float ☃xxxxxxxxxxxxxxxxxxxx = 0.5F;
               double ☃xxxxxxxxxxxxxxxxxxxxx = 0.1 + ☃xxxxxxx * 0.2;
               if (☃xxxxxxxxx == 0) {
                  ☃xxxxxxxxxxxxxxxxxxxxx *= ☃xxxxxxxxxxxxxx * 0.1 + 1.0;
               }

               double ☃xxxxxxxxxxxxxxxxxxxxxx = 0.1 + ☃xxxxxxx * 0.2;
               if (☃xxxxxxxxx == 0) {
                  ☃xxxxxxxxxxxxxxxxxxxxxx *= (☃xxxxxxxxxxxxxx - 1) * 0.1 + 1.0;
               }

               for (int ☃xxxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxxx < 5; ☃xxxxxxxxxxxxxxxxxxxxxxx++) {
                  double ☃xxxxxxxxxxxxxxxxxxxxxxxx = ☃ + 0.5 - ☃xxxxxxxxxxxxxxxxxxxxx;
                  double ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃ + 0.5 - ☃xxxxxxxxxxxxxxxxxxxxx;
                  if (☃xxxxxxxxxxxxxxxxxxxxxxx == 1 || ☃xxxxxxxxxxxxxxxxxxxxxxx == 2) {
                     ☃xxxxxxxxxxxxxxxxxxxxxxxx += ☃xxxxxxxxxxxxxxxxxxxxx * 2.0;
                  }

                  if (☃xxxxxxxxxxxxxxxxxxxxxxx == 2 || ☃xxxxxxxxxxxxxxxxxxxxxxx == 3) {
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxx += ☃xxxxxxxxxxxxxxxxxxxxx * 2.0;
                  }

                  double ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃ + 0.5 - ☃xxxxxxxxxxxxxxxxxxxxxx;
                  double ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃ + 0.5 - ☃xxxxxxxxxxxxxxxxxxxxxx;
                  if (☃xxxxxxxxxxxxxxxxxxxxxxx == 1 || ☃xxxxxxxxxxxxxxxxxxxxxxx == 2) {
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxxx += ☃xxxxxxxxxxxxxxxxxxxxxx * 2.0;
                  }

                  if (☃xxxxxxxxxxxxxxxxxxxxxxx == 2 || ☃xxxxxxxxxxxxxxxxxxxxxxx == 3) {
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx += ☃xxxxxxxxxxxxxxxxxxxxxx * 2.0;
                  }

                  ☃x.pos(☃xxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxx, ☃ + ☃xxxxxxxxxxxxxx * 16, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxx)
                     .color(0.45F, 0.45F, 0.5F, 0.3F)
                     .endVertex();
                  ☃x.pos(☃xxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxx, ☃ + (☃xxxxxxxxxxxxxx + 1) * 16, ☃xxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxx)
                     .color(0.45F, 0.45F, 0.5F, 0.3F)
                     .endVertex();
               }

               ☃.draw();
            }
         }
      }

      GlStateManager.disableBlend();
      GlStateManager.enableLighting();
      GlStateManager.enableTexture2D();
   }

   @Nullable
   protected ResourceLocation getEntityTexture(EntityLightningBolt var1) {
      return null;
   }
}
