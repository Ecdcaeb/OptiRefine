package net.minecraft.client.renderer.debug;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class DebugRendererCollisionBox implements DebugRenderer.IDebugRenderer {
   private final Minecraft minecraft;
   private EntityPlayer player;
   private double renderPosX;
   private double renderPosY;
   private double renderPosZ;

   public DebugRendererCollisionBox(Minecraft var1) {
      this.minecraft = ☃;
   }

   @Override
   public void render(float var1, long var2) {
      this.player = this.minecraft.player;
      this.renderPosX = this.player.lastTickPosX + (this.player.posX - this.player.lastTickPosX) * ☃;
      this.renderPosY = this.player.lastTickPosY + (this.player.posY - this.player.lastTickPosY) * ☃;
      this.renderPosZ = this.player.lastTickPosZ + (this.player.posZ - this.player.lastTickPosZ) * ☃;
      World ☃ = this.minecraft.player.world;
      List<AxisAlignedBB> ☃x = ☃.getCollisionBoxes(this.player, this.player.getEntityBoundingBox().grow(6.0));
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      GlStateManager.glLineWidth(2.0F);
      GlStateManager.disableTexture2D();
      GlStateManager.depthMask(false);

      for (AxisAlignedBB ☃xx : ☃x) {
         RenderGlobal.drawSelectionBoundingBox(☃xx.grow(0.002).offset(-this.renderPosX, -this.renderPosY, -this.renderPosZ), 1.0F, 1.0F, 1.0F, 1.0F);
      }

      GlStateManager.depthMask(true);
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }
}
