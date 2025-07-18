package net.minecraft.client.renderer.debug;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;

public class DebugRendererChunkBorder implements DebugRenderer.IDebugRenderer {
   private final Minecraft minecraft;

   public DebugRendererChunkBorder(Minecraft var1) {
      this.minecraft = ☃;
   }

   @Override
   public void render(float var1, long var2) {
      EntityPlayer ☃ = this.minecraft.player;
      Tessellator ☃x = Tessellator.getInstance();
      BufferBuilder ☃xx = ☃x.getBuffer();
      double ☃xxx = ☃.lastTickPosX + (☃.posX - ☃.lastTickPosX) * ☃;
      double ☃xxxx = ☃.lastTickPosY + (☃.posY - ☃.lastTickPosY) * ☃;
      double ☃xxxxx = ☃.lastTickPosZ + (☃.posZ - ☃.lastTickPosZ) * ☃;
      double ☃xxxxxx = 0.0 - ☃xxxx;
      double ☃xxxxxxx = 256.0 - ☃xxxx;
      GlStateManager.disableTexture2D();
      GlStateManager.disableBlend();
      double ☃xxxxxxxx = (☃.chunkCoordX << 4) - ☃xxx;
      double ☃xxxxxxxxx = (☃.chunkCoordZ << 4) - ☃xxxxx;
      GlStateManager.glLineWidth(1.0F);
      ☃xx.begin(3, DefaultVertexFormats.POSITION_COLOR);

      for (int ☃xxxxxxxxxx = -16; ☃xxxxxxxxxx <= 32; ☃xxxxxxxxxx += 16) {
         for (int ☃xxxxxxxxxxx = -16; ☃xxxxxxxxxxx <= 32; ☃xxxxxxxxxxx += 16) {
            ☃xx.pos(☃xxxxxxxx + ☃xxxxxxxxxx, ☃xxxxxx, ☃xxxxxxxxx + ☃xxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.0F).endVertex();
            ☃xx.pos(☃xxxxxxxx + ☃xxxxxxxxxx, ☃xxxxxx, ☃xxxxxxxxx + ☃xxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
            ☃xx.pos(☃xxxxxxxx + ☃xxxxxxxxxx, ☃xxxxxxx, ☃xxxxxxxxx + ☃xxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.5F).endVertex();
            ☃xx.pos(☃xxxxxxxx + ☃xxxxxxxxxx, ☃xxxxxxx, ☃xxxxxxxxx + ☃xxxxxxxxxxx).color(1.0F, 0.0F, 0.0F, 0.0F).endVertex();
         }
      }

      for (int ☃xxxxxxxxxx = 2; ☃xxxxxxxxxx < 16; ☃xxxxxxxxxx += 2) {
         ☃xx.pos(☃xxxxxxxx + ☃xxxxxxxxxx, ☃xxxxxx, ☃xxxxxxxxx).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx + ☃xxxxxxxxxx, ☃xxxxxx, ☃xxxxxxxxx).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx + ☃xxxxxxxxxx, ☃xxxxxxx, ☃xxxxxxxxx).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx + ☃xxxxxxxxxx, ☃xxxxxxx, ☃xxxxxxxxx).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx + ☃xxxxxxxxxx, ☃xxxxxx, ☃xxxxxxxxx + 16.0).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx + ☃xxxxxxxxxx, ☃xxxxxx, ☃xxxxxxxxx + 16.0).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx + ☃xxxxxxxxxx, ☃xxxxxxx, ☃xxxxxxxxx + 16.0).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx + ☃xxxxxxxxxx, ☃xxxxxxx, ☃xxxxxxxxx + 16.0).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
      }

      for (int ☃xxxxxxxxxx = 2; ☃xxxxxxxxxx < 16; ☃xxxxxxxxxx += 2) {
         ☃xx.pos(☃xxxxxxxx, ☃xxxxxx, ☃xxxxxxxxx + ☃xxxxxxxxxx).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx, ☃xxxxxx, ☃xxxxxxxxx + ☃xxxxxxxxxx).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx, ☃xxxxxxx, ☃xxxxxxxxx + ☃xxxxxxxxxx).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx, ☃xxxxxxx, ☃xxxxxxxxx + ☃xxxxxxxxxx).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx + 16.0, ☃xxxxxx, ☃xxxxxxxxx + ☃xxxxxxxxxx).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx + 16.0, ☃xxxxxx, ☃xxxxxxxxx + ☃xxxxxxxxxx).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx + 16.0, ☃xxxxxxx, ☃xxxxxxxxx + ☃xxxxxxxxxx).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx + 16.0, ☃xxxxxxx, ☃xxxxxxxxx + ☃xxxxxxxxxx).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
      }

      for (int ☃xxxxxxxxxx = 0; ☃xxxxxxxxxx <= 256; ☃xxxxxxxxxx += 2) {
         double ☃xxxxxxxxxxx = ☃xxxxxxxxxx - ☃xxxx;
         ☃xx.pos(☃xxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxx).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxx).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxx + 16.0).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx + 16.0, ☃xxxxxxxxxxx, ☃xxxxxxxxx + 16.0).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx + 16.0, ☃xxxxxxxxxxx, ☃xxxxxxxxx).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxx).color(1.0F, 1.0F, 0.0F, 1.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxx).color(1.0F, 1.0F, 0.0F, 0.0F).endVertex();
      }

      ☃x.draw();
      GlStateManager.glLineWidth(2.0F);
      ☃xx.begin(3, DefaultVertexFormats.POSITION_COLOR);

      for (int ☃xxxxxxxxxx = 0; ☃xxxxxxxxxx <= 16; ☃xxxxxxxxxx += 16) {
         for (int ☃xxxxxxxxxxx = 0; ☃xxxxxxxxxxx <= 16; ☃xxxxxxxxxxx += 16) {
            ☃xx.pos(☃xxxxxxxx + ☃xxxxxxxxxx, ☃xxxxxx, ☃xxxxxxxxx + ☃xxxxxxxxxxx).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
            ☃xx.pos(☃xxxxxxxx + ☃xxxxxxxxxx, ☃xxxxxx, ☃xxxxxxxxx + ☃xxxxxxxxxxx).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
            ☃xx.pos(☃xxxxxxxx + ☃xxxxxxxxxx, ☃xxxxxxx, ☃xxxxxxxxx + ☃xxxxxxxxxxx).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
            ☃xx.pos(☃xxxxxxxx + ☃xxxxxxxxxx, ☃xxxxxxx, ☃xxxxxxxxx + ☃xxxxxxxxxxx).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
         }
      }

      for (int ☃xxxxxxxxxx = 0; ☃xxxxxxxxxx <= 256; ☃xxxxxxxxxx += 16) {
         double ☃xxxxxxxxxxx = ☃xxxxxxxxxx - ☃xxxx;
         ☃xx.pos(☃xxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxx).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxx).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxx + 16.0).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx + 16.0, ☃xxxxxxxxxxx, ☃xxxxxxxxx + 16.0).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx + 16.0, ☃xxxxxxxxxxx, ☃xxxxxxxxx).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxx).color(0.25F, 0.25F, 1.0F, 1.0F).endVertex();
         ☃xx.pos(☃xxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxx).color(0.25F, 0.25F, 1.0F, 0.0F).endVertex();
      }

      ☃x.draw();
      GlStateManager.glLineWidth(1.0F);
      GlStateManager.enableBlend();
      GlStateManager.enableTexture2D();
   }
}
