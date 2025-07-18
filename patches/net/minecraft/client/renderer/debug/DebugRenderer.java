package net.minecraft.client.renderer.debug;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;

public class DebugRenderer {
   public final DebugRenderer.IDebugRenderer pathfinding;
   public final DebugRenderer.IDebugRenderer water;
   public final DebugRenderer.IDebugRenderer chunkBorder;
   public final DebugRenderer.IDebugRenderer heightMap;
   public final DebugRenderer.IDebugRenderer collisionBox;
   public final DebugRenderer.IDebugRenderer neighborsUpdate;
   public final DebugRenderer.IDebugRenderer solidFace;
   private boolean chunkBorderEnabled;
   private boolean pathfindingEnabled;
   private boolean waterEnabled;
   private boolean heightMapEnabled;
   private boolean collisionBoxEnabled;
   private boolean neighborsUpdateEnabled;
   private boolean solidFaceEnabled;

   public DebugRenderer(Minecraft var1) {
      this.pathfinding = new DebugRendererPathfinding(☃);
      this.water = new DebugRendererWater(☃);
      this.chunkBorder = new DebugRendererChunkBorder(☃);
      this.heightMap = new DebugRendererHeightMap(☃);
      this.collisionBox = new DebugRendererCollisionBox(☃);
      this.neighborsUpdate = new DebugRendererNeighborsUpdate(☃);
      this.solidFace = new DebugRendererSolidFace(☃);
   }

   public boolean shouldRender() {
      return this.chunkBorderEnabled
         || this.pathfindingEnabled
         || this.waterEnabled
         || this.heightMapEnabled
         || this.collisionBoxEnabled
         || this.neighborsUpdateEnabled
         || this.solidFaceEnabled;
   }

   public boolean toggleChunkBorders() {
      this.chunkBorderEnabled = !this.chunkBorderEnabled;
      return this.chunkBorderEnabled;
   }

   public void renderDebug(float var1, long var2) {
      if (this.pathfindingEnabled) {
         this.pathfinding.render(☃, ☃);
      }

      if (this.chunkBorderEnabled && !Minecraft.getMinecraft().isReducedDebug()) {
         this.chunkBorder.render(☃, ☃);
      }

      if (this.waterEnabled) {
         this.water.render(☃, ☃);
      }

      if (this.heightMapEnabled) {
         this.heightMap.render(☃, ☃);
      }

      if (this.collisionBoxEnabled) {
         this.collisionBox.render(☃, ☃);
      }

      if (this.neighborsUpdateEnabled) {
         this.neighborsUpdate.render(☃, ☃);
      }

      if (this.solidFaceEnabled) {
         this.solidFace.render(☃, ☃);
      }
   }

   public static void renderDebugText(String var0, int var1, int var2, int var3, float var4, int var5) {
      renderDebugText(☃, ☃ + 0.5, ☃ + 0.5, ☃ + 0.5, ☃, ☃);
   }

   public static void renderDebugText(String var0, double var1, double var3, double var5, float var7, int var8) {
      Minecraft ☃ = Minecraft.getMinecraft();
      if (☃.player != null && ☃.getRenderManager() != null && ☃.getRenderManager().options != null) {
         FontRenderer ☃x = ☃.fontRenderer;
         EntityPlayer ☃xx = ☃.player;
         double ☃xxx = ☃xx.lastTickPosX + (☃xx.posX - ☃xx.lastTickPosX) * ☃;
         double ☃xxxx = ☃xx.lastTickPosY + (☃xx.posY - ☃xx.lastTickPosY) * ☃;
         double ☃xxxxx = ☃xx.lastTickPosZ + (☃xx.posZ - ☃xx.lastTickPosZ) * ☃;
         GlStateManager.pushMatrix();
         GlStateManager.translate((float)(☃ - ☃xxx), (float)(☃ - ☃xxxx) + 0.07F, (float)(☃ - ☃xxxxx));
         GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
         GlStateManager.scale(0.02F, -0.02F, 0.02F);
         RenderManager ☃xxxxxx = ☃.getRenderManager();
         GlStateManager.rotate(-☃xxxxxx.playerViewY, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate((☃xxxxxx.options.thirdPersonView == 2 ? 1 : -1) * ☃xxxxxx.playerViewX, 1.0F, 0.0F, 0.0F);
         GlStateManager.disableLighting();
         GlStateManager.enableTexture2D();
         GlStateManager.enableDepth();
         GlStateManager.depthMask(true);
         GlStateManager.scale(-1.0F, 1.0F, 1.0F);
         ☃x.drawString(☃, -☃x.getStringWidth(☃) / 2, 0, ☃);
         GlStateManager.enableLighting();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.popMatrix();
      }
   }

   public interface IDebugRenderer {
      void render(float var1, long var2);
   }
}
