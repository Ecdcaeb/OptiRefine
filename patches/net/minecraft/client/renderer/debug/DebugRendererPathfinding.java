package net.minecraft.client.renderer.debug;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

public class DebugRendererPathfinding implements DebugRenderer.IDebugRenderer {
   private final Minecraft minecraft;
   private final Map<Integer, Path> pathMap = Maps.newHashMap();
   private final Map<Integer, Float> pathMaxDistance = Maps.newHashMap();
   private final Map<Integer, Long> creationMap = Maps.newHashMap();
   private EntityPlayer player;
   private double xo;
   private double yo;
   private double zo;

   public DebugRendererPathfinding(Minecraft var1) {
      this.minecraft = ☃;
   }

   public void addPath(int var1, Path var2, float var3) {
      this.pathMap.put(☃, ☃);
      this.creationMap.put(☃, System.currentTimeMillis());
      this.pathMaxDistance.put(☃, ☃);
   }

   @Override
   public void render(float var1, long var2) {
      if (!this.pathMap.isEmpty()) {
         long ☃ = System.currentTimeMillis();
         this.player = this.minecraft.player;
         this.xo = this.player.lastTickPosX + (this.player.posX - this.player.lastTickPosX) * ☃;
         this.yo = this.player.lastTickPosY + (this.player.posY - this.player.lastTickPosY) * ☃;
         this.zo = this.player.lastTickPosZ + (this.player.posZ - this.player.lastTickPosZ) * ☃;
         GlStateManager.pushMatrix();
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
         );
         GlStateManager.color(0.0F, 1.0F, 0.0F, 0.75F);
         GlStateManager.disableTexture2D();
         GlStateManager.glLineWidth(6.0F);

         for (Integer ☃x : this.pathMap.keySet()) {
            Path ☃xx = this.pathMap.get(☃x);
            float ☃xxx = this.pathMaxDistance.get(☃x);
            this.renderPathLine(☃, ☃xx);
            PathPoint ☃xxxx = ☃xx.getTarget();
            if (!(this.addDistanceToPlayer(☃xxxx) > 40.0F)) {
               RenderGlobal.renderFilledBox(
                  new AxisAlignedBB(☃xxxx.x + 0.25F, ☃xxxx.y + 0.25F, ☃xxxx.z + 0.25, ☃xxxx.x + 0.75F, ☃xxxx.y + 0.75F, ☃xxxx.z + 0.75F)
                     .offset(-this.xo, -this.yo, -this.zo),
                  0.0F,
                  1.0F,
                  0.0F,
                  0.5F
               );

               for (int ☃xxxxx = 0; ☃xxxxx < ☃xx.getCurrentPathLength(); ☃xxxxx++) {
                  PathPoint ☃xxxxxx = ☃xx.getPathPointFromIndex(☃xxxxx);
                  if (!(this.addDistanceToPlayer(☃xxxxxx) > 40.0F)) {
                     float ☃xxxxxxx = ☃xxxxx == ☃xx.getCurrentPathIndex() ? 1.0F : 0.0F;
                     float ☃xxxxxxxx = ☃xxxxx == ☃xx.getCurrentPathIndex() ? 0.0F : 1.0F;
                     RenderGlobal.renderFilledBox(
                        new AxisAlignedBB(
                              ☃xxxxxx.x + 0.5F - ☃xxx,
                              ☃xxxxxx.y + 0.01F * ☃xxxxx,
                              ☃xxxxxx.z + 0.5F - ☃xxx,
                              ☃xxxxxx.x + 0.5F + ☃xxx,
                              ☃xxxxxx.y + 0.25F + 0.01F * ☃xxxxx,
                              ☃xxxxxx.z + 0.5F + ☃xxx
                           )
                           .offset(-this.xo, -this.yo, -this.zo),
                        ☃xxxxxxx,
                        0.0F,
                        ☃xxxxxxxx,
                        0.5F
                     );
                  }
               }
            }
         }

         for (Integer ☃xx : this.pathMap.keySet()) {
            Path ☃xxx = this.pathMap.get(☃xx);

            for (PathPoint ☃xxxx : ☃xxx.getClosedSet()) {
               if (!(this.addDistanceToPlayer(☃xxxx) > 40.0F)) {
                  DebugRenderer.renderDebugText(String.format("%s", ☃xxxx.nodeType), ☃xxxx.x + 0.5, ☃xxxx.y + 0.75, ☃xxxx.z + 0.5, ☃, -65536);
                  DebugRenderer.renderDebugText(String.format("%.2f", ☃xxxx.costMalus), ☃xxxx.x + 0.5, ☃xxxx.y + 0.25, ☃xxxx.z + 0.5, ☃, -65536);
               }
            }

            for (PathPoint ☃xxxxxx : ☃xxx.getOpenSet()) {
               if (!(this.addDistanceToPlayer(☃xxxxxx) > 40.0F)) {
                  DebugRenderer.renderDebugText(String.format("%s", ☃xxxxxx.nodeType), ☃xxxxxx.x + 0.5, ☃xxxxxx.y + 0.75, ☃xxxxxx.z + 0.5, ☃, -16776961);
                  DebugRenderer.renderDebugText(String.format("%.2f", ☃xxxxxx.costMalus), ☃xxxxxx.x + 0.5, ☃xxxxxx.y + 0.25, ☃xxxxxx.z + 0.5, ☃, -16776961);
               }
            }

            for (int ☃xxxxxxx = 0; ☃xxxxxxx < ☃xxx.getCurrentPathLength(); ☃xxxxxxx++) {
               PathPoint ☃xxxxxxxx = ☃xxx.getPathPointFromIndex(☃xxxxxxx);
               if (!(this.addDistanceToPlayer(☃xxxxxxxx) > 40.0F)) {
                  DebugRenderer.renderDebugText(String.format("%s", ☃xxxxxxxx.nodeType), ☃xxxxxxxx.x + 0.5, ☃xxxxxxxx.y + 0.75, ☃xxxxxxxx.z + 0.5, ☃, -1);
                  DebugRenderer.renderDebugText(String.format("%.2f", ☃xxxxxxxx.costMalus), ☃xxxxxxxx.x + 0.5, ☃xxxxxxxx.y + 0.25, ☃xxxxxxxx.z + 0.5, ☃, -1);
               }
            }
         }

         for (Integer ☃xx : this.creationMap.keySet().toArray(new Integer[0])) {
            if (☃ - this.creationMap.get(☃xx) > 20000L) {
               this.pathMap.remove(☃xx);
               this.creationMap.remove(☃xx);
            }
         }

         GlStateManager.enableTexture2D();
         GlStateManager.disableBlend();
         GlStateManager.popMatrix();
      }
   }

   public void renderPathLine(float var1, Path var2) {
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      ☃x.begin(3, DefaultVertexFormats.POSITION_COLOR);

      for (int ☃xx = 0; ☃xx < ☃.getCurrentPathLength(); ☃xx++) {
         PathPoint ☃xxx = ☃.getPathPointFromIndex(☃xx);
         if (!(this.addDistanceToPlayer(☃xxx) > 40.0F)) {
            float ☃xxxx = (float)☃xx / ☃.getCurrentPathLength() * 0.33F;
            int ☃xxxxx = ☃xx == 0 ? 0 : MathHelper.hsvToRGB(☃xxxx, 0.9F, 0.9F);
            int ☃xxxxxx = ☃xxxxx >> 16 & 0xFF;
            int ☃xxxxxxx = ☃xxxxx >> 8 & 0xFF;
            int ☃xxxxxxxx = ☃xxxxx & 0xFF;
            ☃x.pos(☃xxx.x - this.xo + 0.5, ☃xxx.y - this.yo + 0.5, ☃xxx.z - this.zo + 0.5).color(☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, 255).endVertex();
         }
      }

      ☃.draw();
   }

   private float addDistanceToPlayer(PathPoint var1) {
      return (float)(Math.abs(☃.x - this.player.posX) + Math.abs(☃.y - this.player.posY) + Math.abs(☃.z - this.player.posZ));
   }
}
