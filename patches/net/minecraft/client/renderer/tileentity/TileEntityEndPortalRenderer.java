package net.minecraft.client.renderer.tileentity;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TileEntityEndPortalRenderer extends TileEntitySpecialRenderer<TileEntityEndPortal> {
   private static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation("textures/environment/end_sky.png");
   private static final ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");
   private static final Random RANDOM = new Random(31100L);
   private static final FloatBuffer MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
   private static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer(16);
   private final FloatBuffer buffer = GLAllocation.createDirectFloatBuffer(16);

   public void render(TileEntityEndPortal var1, double var2, double var4, double var6, float var8, int var9, float var10) {
      GlStateManager.disableLighting();
      RANDOM.setSeed(31100L);
      GlStateManager.getFloat(2982, MODELVIEW);
      GlStateManager.getFloat(2983, PROJECTION);
      double ☃ = ☃ * ☃ + ☃ * ☃ + ☃ * ☃;
      int ☃x = this.getPasses(☃);
      float ☃xx = this.getOffset();
      boolean ☃xxx = false;

      for (int ☃xxxx = 0; ☃xxxx < ☃x; ☃xxxx++) {
         GlStateManager.pushMatrix();
         float ☃xxxxx = 2.0F / (18 - ☃xxxx);
         if (☃xxxx == 0) {
            this.bindTexture(END_SKY_TEXTURE);
            ☃xxxxx = 0.15F;
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
         }

         if (☃xxxx >= 1) {
            this.bindTexture(END_PORTAL_TEXTURE);
            ☃xxx = true;
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
         }

         if (☃xxxx == 1) {
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
         }

         GlStateManager.texGen(GlStateManager.TexGen.S, 9216);
         GlStateManager.texGen(GlStateManager.TexGen.T, 9216);
         GlStateManager.texGen(GlStateManager.TexGen.R, 9216);
         GlStateManager.texGen(GlStateManager.TexGen.S, 9474, this.getBuffer(1.0F, 0.0F, 0.0F, 0.0F));
         GlStateManager.texGen(GlStateManager.TexGen.T, 9474, this.getBuffer(0.0F, 1.0F, 0.0F, 0.0F));
         GlStateManager.texGen(GlStateManager.TexGen.R, 9474, this.getBuffer(0.0F, 0.0F, 1.0F, 0.0F));
         GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
         GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
         GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);
         GlStateManager.popMatrix();
         GlStateManager.matrixMode(5890);
         GlStateManager.pushMatrix();
         GlStateManager.loadIdentity();
         GlStateManager.translate(0.5F, 0.5F, 0.0F);
         GlStateManager.scale(0.5F, 0.5F, 1.0F);
         float ☃xxxxxx = ☃xxxx + 1;
         GlStateManager.translate(17.0F / ☃xxxxxx, (2.0F + ☃xxxxxx / 1.5F) * ((float)Minecraft.getSystemTime() % 800000.0F / 800000.0F), 0.0F);
         GlStateManager.rotate((☃xxxxxx * ☃xxxxxx * 4321.0F + ☃xxxxxx * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);
         GlStateManager.scale(4.5F - ☃xxxxxx / 4.0F, 4.5F - ☃xxxxxx / 4.0F, 1.0F);
         GlStateManager.multMatrix(PROJECTION);
         GlStateManager.multMatrix(MODELVIEW);
         Tessellator ☃xxxxxxx = Tessellator.getInstance();
         BufferBuilder ☃xxxxxxxx = ☃xxxxxxx.getBuffer();
         ☃xxxxxxxx.begin(7, DefaultVertexFormats.POSITION_COLOR);
         float ☃xxxxxxxxx = (RANDOM.nextFloat() * 0.5F + 0.1F) * ☃xxxxx;
         float ☃xxxxxxxxxx = (RANDOM.nextFloat() * 0.5F + 0.4F) * ☃xxxxx;
         float ☃xxxxxxxxxxx = (RANDOM.nextFloat() * 0.5F + 0.5F) * ☃xxxxx;
         if (☃.shouldRenderFace(EnumFacing.SOUTH)) {
            ☃xxxxxxxx.pos(☃, ☃, ☃ + 1.0).color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 1.0F).endVertex();
            ☃xxxxxxxx.pos(☃ + 1.0, ☃, ☃ + 1.0).color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 1.0F).endVertex();
            ☃xxxxxxxx.pos(☃ + 1.0, ☃ + 1.0, ☃ + 1.0).color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 1.0F).endVertex();
            ☃xxxxxxxx.pos(☃, ☃ + 1.0, ☃ + 1.0).color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 1.0F).endVertex();
         }

         if (☃.shouldRenderFace(EnumFacing.NORTH)) {
            ☃xxxxxxxx.pos(☃, ☃ + 1.0, ☃).color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 1.0F).endVertex();
            ☃xxxxxxxx.pos(☃ + 1.0, ☃ + 1.0, ☃).color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 1.0F).endVertex();
            ☃xxxxxxxx.pos(☃ + 1.0, ☃, ☃).color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 1.0F).endVertex();
            ☃xxxxxxxx.pos(☃, ☃, ☃).color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 1.0F).endVertex();
         }

         if (☃.shouldRenderFace(EnumFacing.EAST)) {
            ☃xxxxxxxx.pos(☃ + 1.0, ☃ + 1.0, ☃).color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 1.0F).endVertex();
            ☃xxxxxxxx.pos(☃ + 1.0, ☃ + 1.0, ☃ + 1.0).color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 1.0F).endVertex();
            ☃xxxxxxxx.pos(☃ + 1.0, ☃, ☃ + 1.0).color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 1.0F).endVertex();
            ☃xxxxxxxx.pos(☃ + 1.0, ☃, ☃).color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 1.0F).endVertex();
         }

         if (☃.shouldRenderFace(EnumFacing.WEST)) {
            ☃xxxxxxxx.pos(☃, ☃, ☃).color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 1.0F).endVertex();
            ☃xxxxxxxx.pos(☃, ☃, ☃ + 1.0).color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 1.0F).endVertex();
            ☃xxxxxxxx.pos(☃, ☃ + 1.0, ☃ + 1.0).color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 1.0F).endVertex();
            ☃xxxxxxxx.pos(☃, ☃ + 1.0, ☃).color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 1.0F).endVertex();
         }

         if (☃.shouldRenderFace(EnumFacing.DOWN)) {
            ☃xxxxxxxx.pos(☃, ☃, ☃).color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 1.0F).endVertex();
            ☃xxxxxxxx.pos(☃ + 1.0, ☃, ☃).color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 1.0F).endVertex();
            ☃xxxxxxxx.pos(☃ + 1.0, ☃, ☃ + 1.0).color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 1.0F).endVertex();
            ☃xxxxxxxx.pos(☃, ☃, ☃ + 1.0).color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 1.0F).endVertex();
         }

         if (☃.shouldRenderFace(EnumFacing.UP)) {
            ☃xxxxxxxx.pos(☃, ☃ + ☃xx, ☃ + 1.0).color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 1.0F).endVertex();
            ☃xxxxxxxx.pos(☃ + 1.0, ☃ + ☃xx, ☃ + 1.0).color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 1.0F).endVertex();
            ☃xxxxxxxx.pos(☃ + 1.0, ☃ + ☃xx, ☃).color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 1.0F).endVertex();
            ☃xxxxxxxx.pos(☃, ☃ + ☃xx, ☃).color(☃xxxxxxxxx, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, 1.0F).endVertex();
         }

         ☃xxxxxxx.draw();
         GlStateManager.popMatrix();
         GlStateManager.matrixMode(5888);
         this.bindTexture(END_SKY_TEXTURE);
      }

      GlStateManager.disableBlend();
      GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
      GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
      GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
      GlStateManager.enableLighting();
      if (☃xxx) {
         Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
      }
   }

   protected int getPasses(double var1) {
      int ☃;
      if (☃ > 36864.0) {
         ☃ = 1;
      } else if (☃ > 25600.0) {
         ☃ = 3;
      } else if (☃ > 16384.0) {
         ☃ = 5;
      } else if (☃ > 9216.0) {
         ☃ = 7;
      } else if (☃ > 4096.0) {
         ☃ = 9;
      } else if (☃ > 1024.0) {
         ☃ = 11;
      } else if (☃ > 576.0) {
         ☃ = 13;
      } else if (☃ > 256.0) {
         ☃ = 14;
      } else {
         ☃ = 15;
      }

      return ☃;
   }

   protected float getOffset() {
      return 0.75F;
   }

   private FloatBuffer getBuffer(float var1, float var2, float var3, float var4) {
      ((Buffer)this.buffer).clear();
      this.buffer.put(☃).put(☃).put(☃).put(☃);
      ((Buffer)this.buffer).flip();
      return this.buffer;
   }
}
