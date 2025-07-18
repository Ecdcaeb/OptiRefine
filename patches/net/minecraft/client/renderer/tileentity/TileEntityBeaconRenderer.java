package net.minecraft.client.renderer.tileentity;

import java.util.List;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class TileEntityBeaconRenderer extends TileEntitySpecialRenderer<TileEntityBeacon> {
   public static final ResourceLocation TEXTURE_BEACON_BEAM = new ResourceLocation("textures/entity/beacon_beam.png");

   public void render(TileEntityBeacon var1, double var2, double var4, double var6, float var8, int var9, float var10) {
      this.renderBeacon(☃, ☃, ☃, ☃, ☃.shouldBeamRender(), ☃.getBeamSegments(), ☃.getWorld().getTotalWorldTime());
   }

   public void renderBeacon(double var1, double var3, double var5, double var7, double var9, List<TileEntityBeacon.BeamSegment> var11, double var12) {
      GlStateManager.alphaFunc(516, 0.1F);
      this.bindTexture(TEXTURE_BEACON_BEAM);
      if (☃ > 0.0) {
         GlStateManager.disableFog();
         int ☃ = 0;

         for (int ☃x = 0; ☃x < ☃.size(); ☃x++) {
            TileEntityBeacon.BeamSegment ☃xx = ☃.get(☃x);
            renderBeamSegment(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃xx.getHeight(), ☃xx.getColors());
            ☃ += ☃xx.getHeight();
         }

         GlStateManager.enableFog();
      }
   }

   public static void renderBeamSegment(double var0, double var2, double var4, double var6, double var8, double var10, int var12, int var13, float[] var14) {
      renderBeamSegment(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, 0.2, 0.25);
   }

   public static void renderBeamSegment(
      double var0, double var2, double var4, double var6, double var8, double var10, int var12, int var13, float[] var14, double var15, double var17
   ) {
      int ☃ = ☃ + ☃;
      GlStateManager.glTexParameteri(3553, 10242, 10497);
      GlStateManager.glTexParameteri(3553, 10243, 10497);
      GlStateManager.disableLighting();
      GlStateManager.disableCull();
      GlStateManager.disableBlend();
      GlStateManager.depthMask(true);
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      Tessellator ☃x = Tessellator.getInstance();
      BufferBuilder ☃xx = ☃x.getBuffer();
      double ☃xxx = ☃ + ☃;
      double ☃xxxx = ☃ < 0 ? ☃xxx : -☃xxx;
      double ☃xxxxx = MathHelper.frac(☃xxxx * 0.2 - MathHelper.floor(☃xxxx * 0.1));
      float ☃xxxxxx = ☃[0];
      float ☃xxxxxxx = ☃[1];
      float ☃xxxxxxxx = ☃[2];
      double ☃xxxxxxxxx = ☃xxx * 0.025 * -1.5;
      double ☃xxxxxxxxxx = 0.5 + Math.cos(☃xxxxxxxxx + (Math.PI * 3.0 / 4.0)) * ☃;
      double ☃xxxxxxxxxxx = 0.5 + Math.sin(☃xxxxxxxxx + (Math.PI * 3.0 / 4.0)) * ☃;
      double ☃xxxxxxxxxxxx = 0.5 + Math.cos(☃xxxxxxxxx + (Math.PI / 4)) * ☃;
      double ☃xxxxxxxxxxxxx = 0.5 + Math.sin(☃xxxxxxxxx + (Math.PI / 4)) * ☃;
      double ☃xxxxxxxxxxxxxx = 0.5 + Math.cos(☃xxxxxxxxx + (Math.PI * 5.0 / 4.0)) * ☃;
      double ☃xxxxxxxxxxxxxxx = 0.5 + Math.sin(☃xxxxxxxxx + (Math.PI * 5.0 / 4.0)) * ☃;
      double ☃xxxxxxxxxxxxxxxx = 0.5 + Math.cos(☃xxxxxxxxx + (Math.PI * 7.0 / 4.0)) * ☃;
      double ☃xxxxxxxxxxxxxxxxx = 0.5 + Math.sin(☃xxxxxxxxx + (Math.PI * 7.0 / 4.0)) * ☃;
      double ☃xxxxxxxxxxxxxxxxxx = 0.0;
      double ☃xxxxxxxxxxxxxxxxxxx = 1.0;
      double ☃xxxxxxxxxxxxxxxxxxxx = -1.0 + ☃xxxxx;
      double ☃xxxxxxxxxxxxxxxxxxxxx = ☃ * ☃ * (0.5 / ☃) + ☃xxxxxxxxxxxxxxxxxxxx;
      ☃xx.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
      ☃xx.pos(☃ + ☃xxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxx).tex(1.0, ☃xxxxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 1.0F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxx).tex(1.0, ☃xxxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 1.0F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxxxx).tex(0.0, ☃xxxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 1.0F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxxxx).tex(0.0, ☃xxxxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 1.0F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxxxxxxxx).tex(1.0, ☃xxxxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 1.0F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxxxxxxxx).tex(1.0, ☃xxxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 1.0F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxxxxxx).tex(0.0, ☃xxxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 1.0F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxxxxxx).tex(0.0, ☃xxxxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 1.0F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxxxx).tex(1.0, ☃xxxxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 1.0F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxxxx).tex(1.0, ☃xxxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 1.0F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxxxxxxxx).tex(0.0, ☃xxxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 1.0F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxxxxxxxx).tex(0.0, ☃xxxxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 1.0F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxxxxxx).tex(1.0, ☃xxxxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 1.0F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxxxxxx).tex(1.0, ☃xxxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 1.0F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxx).tex(0.0, ☃xxxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 1.0F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxx).tex(0.0, ☃xxxxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 1.0F).endVertex();
      ☃x.draw();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      GlStateManager.depthMask(false);
      ☃xxxxxxxxx = 0.5 - ☃;
      ☃xxxxxxxxxx = 0.5 - ☃;
      ☃xxxxxxxxxxx = 0.5 + ☃;
      ☃xxxxxxxxxxxx = 0.5 - ☃;
      ☃xxxxxxxxxxxxx = 0.5 - ☃;
      ☃xxxxxxxxxxxxxx = 0.5 + ☃;
      ☃xxxxxxxxxxxxxxx = 0.5 + ☃;
      ☃xxxxxxxxxxxxxxxx = 0.5 + ☃;
      ☃xxxxxxxxxxxxxxxxx = 0.0;
      ☃xxxxxxxxxxxxxxxxxx = 1.0;
      ☃xxxxxxxxxxxxxxxxxxx = -1.0 + ☃xxxxx;
      ☃xxxxxxxxxxxxxxxxxxxx = ☃ * ☃ + ☃xxxxxxxxxxxxxxxxxxx;
      ☃xx.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
      ☃xx.pos(☃ + ☃xxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxx).tex(1.0, ☃xxxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 0.125F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxx).tex(1.0, ☃xxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 0.125F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxxx).tex(0.0, ☃xxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 0.125F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxxx).tex(0.0, ☃xxxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 0.125F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxxxxxxx).tex(1.0, ☃xxxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 0.125F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxxxxxxx).tex(1.0, ☃xxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 0.125F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxxxxx).tex(0.0, ☃xxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 0.125F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxxxxx).tex(0.0, ☃xxxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 0.125F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxxx).tex(1.0, ☃xxxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 0.125F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxxx).tex(1.0, ☃xxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 0.125F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxxxxxxx).tex(0.0, ☃xxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 0.125F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxxxxxxx).tex(0.0, ☃xxxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 0.125F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxxxxx).tex(1.0, ☃xxxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 0.125F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxxxxxx).tex(1.0, ☃xxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 0.125F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxx).tex(0.0, ☃xxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 0.125F).endVertex();
      ☃xx.pos(☃ + ☃xxxxxxxxx, ☃ + ☃, ☃ + ☃xxxxxxxxxx).tex(0.0, ☃xxxxxxxxxxxxxxxxxxxx).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 0.125F).endVertex();
      ☃x.draw();
      GlStateManager.enableLighting();
      GlStateManager.enableTexture2D();
      GlStateManager.depthMask(true);
   }

   public boolean isGlobalRenderer(TileEntityBeacon var1) {
      return true;
   }
}
