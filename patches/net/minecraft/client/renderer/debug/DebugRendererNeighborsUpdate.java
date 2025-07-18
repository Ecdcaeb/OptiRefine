package net.minecraft.client.renderer.debug;

import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DebugRendererNeighborsUpdate implements DebugRenderer.IDebugRenderer {
   private final Minecraft minecraft;
   private final Map<Long, Map<BlockPos, Integer>> lastUpdate = Maps.newTreeMap(Ordering.natural().reverse());

   DebugRendererNeighborsUpdate(Minecraft var1) {
      this.minecraft = ☃;
   }

   public void addUpdate(long var1, BlockPos var3) {
      Map<BlockPos, Integer> ☃ = this.lastUpdate.get(☃);
      if (☃ == null) {
         ☃ = Maps.newHashMap();
         this.lastUpdate.put(☃, ☃);
      }

      Integer ☃x = ☃.get(☃);
      if (☃x == null) {
         ☃x = 0;
      }

      ☃.put(☃, ☃x + 1);
   }

   @Override
   public void render(float var1, long var2) {
      long ☃ = this.minecraft.world.getTotalWorldTime();
      EntityPlayer ☃x = this.minecraft.player;
      double ☃xx = ☃x.lastTickPosX + (☃x.posX - ☃x.lastTickPosX) * ☃;
      double ☃xxx = ☃x.lastTickPosY + (☃x.posY - ☃x.lastTickPosY) * ☃;
      double ☃xxxx = ☃x.lastTickPosZ + (☃x.posZ - ☃x.lastTickPosZ) * ☃;
      World ☃xxxxx = this.minecraft.player.world;
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      GlStateManager.glLineWidth(2.0F);
      GlStateManager.disableTexture2D();
      GlStateManager.depthMask(false);
      int ☃xxxxxx = 200;
      double ☃xxxxxxx = 0.0025;
      Set<BlockPos> ☃xxxxxxxx = Sets.newHashSet();
      Map<BlockPos, Integer> ☃xxxxxxxxx = Maps.newHashMap();
      Iterator<Entry<Long, Map<BlockPos, Integer>>> ☃xxxxxxxxxx = this.lastUpdate.entrySet().iterator();

      while (☃xxxxxxxxxx.hasNext()) {
         Entry<Long, Map<BlockPos, Integer>> ☃xxxxxxxxxxx = ☃xxxxxxxxxx.next();
         Long ☃xxxxxxxxxxxx = ☃xxxxxxxxxxx.getKey();
         Map<BlockPos, Integer> ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxx.getValue();
         long ☃xxxxxxxxxxxxxx = ☃ - ☃xxxxxxxxxxxx;
         if (☃xxxxxxxxxxxxxx > 200L) {
            ☃xxxxxxxxxx.remove();
         } else {
            for (Entry<BlockPos, Integer> ☃xxxxxxxxxxxxxxx : ☃xxxxxxxxxxxxx.entrySet()) {
               BlockPos ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.getKey();
               Integer ☃xxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.getValue();
               if (☃xxxxxxxx.add(☃xxxxxxxxxxxxxxxx)) {
                  RenderGlobal.drawSelectionBoundingBox(
                     new AxisAlignedBB(BlockPos.ORIGIN)
                        .grow(0.002)
                        .shrink(0.0025 * ☃xxxxxxxxxxxxxx)
                        .offset(☃xxxxxxxxxxxxxxxx.getX(), ☃xxxxxxxxxxxxxxxx.getY(), ☃xxxxxxxxxxxxxxxx.getZ())
                        .offset(-☃xx, -☃xxx, -☃xxxx),
                     1.0F,
                     1.0F,
                     1.0F,
                     1.0F
                  );
                  ☃xxxxxxxxx.put(☃xxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxx);
               }
            }
         }
      }

      for (Entry<BlockPos, Integer> ☃xxxxxxxxxxx : ☃xxxxxxxxx.entrySet()) {
         BlockPos ☃xxxxxxxxxxxx = ☃xxxxxxxxxxx.getKey();
         Integer ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxx.getValue();
         DebugRenderer.renderDebugText(String.valueOf(☃xxxxxxxxxxxxx), ☃xxxxxxxxxxxx.getX(), ☃xxxxxxxxxxxx.getY(), ☃xxxxxxxxxxxx.getZ(), ☃, -1);
      }

      GlStateManager.depthMask(true);
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }
}
