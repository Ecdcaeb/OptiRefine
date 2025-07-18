package net.minecraft.client.gui;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.FrameTimer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.opengl.Display;

public class GuiOverlayDebug extends Gui {
   private final Minecraft mc;
   private final FontRenderer fontRenderer;

   public GuiOverlayDebug(Minecraft var1) {
      this.mc = ☃;
      this.fontRenderer = ☃.fontRenderer;
   }

   public void renderDebugInfo(ScaledResolution var1) {
      this.mc.profiler.startSection("debug");
      GlStateManager.pushMatrix();
      this.renderDebugInfoLeft();
      this.renderDebugInfoRight(☃);
      GlStateManager.popMatrix();
      if (this.mc.gameSettings.showLagometer) {
         this.renderLagometer();
      }

      this.mc.profiler.endSection();
   }

   protected void renderDebugInfoLeft() {
      List<String> ☃ = this.call();
      ☃.add("");
      ☃.add(
         "Debug: Pie [shift]: "
            + (this.mc.gameSettings.showDebugProfilerChart ? "visible" : "hidden")
            + " FPS [alt]: "
            + (this.mc.gameSettings.showLagometer ? "visible" : "hidden")
      );
      ☃.add("For help: press F3 + Q");

      for (int ☃x = 0; ☃x < ☃.size(); ☃x++) {
         String ☃xx = ☃.get(☃x);
         if (!Strings.isNullOrEmpty(☃xx)) {
            int ☃xxx = this.fontRenderer.FONT_HEIGHT;
            int ☃xxxx = this.fontRenderer.getStringWidth(☃xx);
            int ☃xxxxx = 2;
            int ☃xxxxxx = 2 + ☃xxx * ☃x;
            drawRect(1, ☃xxxxxx - 1, 2 + ☃xxxx + 1, ☃xxxxxx + ☃xxx - 1, -1873784752);
            this.fontRenderer.drawString(☃xx, 2, ☃xxxxxx, 14737632);
         }
      }
   }

   protected void renderDebugInfoRight(ScaledResolution var1) {
      List<String> ☃ = this.getDebugInfoRight();

      for (int ☃x = 0; ☃x < ☃.size(); ☃x++) {
         String ☃xx = ☃.get(☃x);
         if (!Strings.isNullOrEmpty(☃xx)) {
            int ☃xxx = this.fontRenderer.FONT_HEIGHT;
            int ☃xxxx = this.fontRenderer.getStringWidth(☃xx);
            int ☃xxxxx = ☃.getScaledWidth() - 2 - ☃xxxx;
            int ☃xxxxxx = 2 + ☃xxx * ☃x;
            drawRect(☃xxxxx - 1, ☃xxxxxx - 1, ☃xxxxx + ☃xxxx + 1, ☃xxxxxx + ☃xxx - 1, -1873784752);
            this.fontRenderer.drawString(☃xx, ☃xxxxx, ☃xxxxxx, 14737632);
         }
      }
   }

   protected List<String> call() {
      BlockPos ☃ = new BlockPos(
         this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ
      );
      if (this.mc.isReducedDebug()) {
         return Lists.newArrayList(
            new String[]{
               "Minecraft 1.12.2 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ")",
               this.mc.debug,
               this.mc.renderGlobal.getDebugInfoRenders(),
               this.mc.renderGlobal.getDebugInfoEntities(),
               "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.world.getDebugLoadedEntities(),
               this.mc.world.getProviderName(),
               "",
               String.format("Chunk-relative: %d %d %d", ☃.getX() & 15, ☃.getY() & 15, ☃.getZ() & 15)
            }
         );
      } else {
         Entity ☃x = this.mc.getRenderViewEntity();
         EnumFacing ☃xx = ☃x.getHorizontalFacing();
         String ☃xxx = "Invalid";
         switch (☃xx) {
            case NORTH:
               ☃xxx = "Towards negative Z";
               break;
            case SOUTH:
               ☃xxx = "Towards positive Z";
               break;
            case WEST:
               ☃xxx = "Towards negative X";
               break;
            case EAST:
               ☃xxx = "Towards positive X";
         }

         List<String> ☃x = Lists.newArrayList(
            new String[]{
               "Minecraft 1.12.2 ("
                  + this.mc.getVersion()
                  + "/"
                  + ClientBrandRetriever.getClientModName()
                  + ("release".equalsIgnoreCase(this.mc.getVersionType()) ? "" : "/" + this.mc.getVersionType())
                  + ")",
               this.mc.debug,
               this.mc.renderGlobal.getDebugInfoRenders(),
               this.mc.renderGlobal.getDebugInfoEntities(),
               "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.world.getDebugLoadedEntities(),
               this.mc.world.getProviderName(),
               "",
               String.format(
                  "XYZ: %.3f / %.5f / %.3f",
                  this.mc.getRenderViewEntity().posX,
                  this.mc.getRenderViewEntity().getEntityBoundingBox().minY,
                  this.mc.getRenderViewEntity().posZ
               ),
               String.format("Block: %d %d %d", ☃.getX(), ☃.getY(), ☃.getZ()),
               String.format("Chunk: %d %d %d in %d %d %d", ☃.getX() & 15, ☃.getY() & 15, ☃.getZ() & 15, ☃.getX() >> 4, ☃.getY() >> 4, ☃.getZ() >> 4),
               String.format("Facing: %s (%s) (%.1f / %.1f)", ☃xx, ☃xxx, MathHelper.wrapDegrees(☃x.rotationYaw), MathHelper.wrapDegrees(☃x.rotationPitch))
            }
         );
         if (this.mc.world != null) {
            Chunk ☃xx = this.mc.world.getChunk(☃);
            if (!this.mc.world.isBlockLoaded(☃) || ☃.getY() < 0 || ☃.getY() >= 256) {
               ☃x.add("Outside of world...");
            } else if (!☃xx.isEmpty()) {
               ☃x.add("Biome: " + ☃xx.getBiome(☃, this.mc.world.getBiomeProvider()).getBiomeName());
               ☃x.add(
                  "Light: "
                     + ☃xx.getLightSubtracted(☃, 0)
                     + " ("
                     + ☃xx.getLightFor(EnumSkyBlock.SKY, ☃)
                     + " sky, "
                     + ☃xx.getLightFor(EnumSkyBlock.BLOCK, ☃)
                     + " block)"
               );
               DifficultyInstance ☃xxx = this.mc.world.getDifficultyForLocation(☃);
               if (this.mc.isIntegratedServerRunning() && this.mc.getIntegratedServer() != null) {
                  EntityPlayerMP ☃xxxx = this.mc.getIntegratedServer().getPlayerList().getPlayerByUUID(this.mc.player.getUniqueID());
                  if (☃xxxx != null) {
                     ☃xxx = ☃xxxx.world.getDifficultyForLocation(new BlockPos(☃xxxx));
                  }
               }

               ☃x.add(
                  String.format(
                     "Local Difficulty: %.2f // %.2f (Day %d)",
                     ☃xxx.getAdditionalDifficulty(),
                     ☃xxx.getClampedAdditionalDifficulty(),
                     this.mc.world.getWorldTime() / 24000L
                  )
               );
            } else {
               ☃x.add("Waiting for chunk...");
            }
         }

         if (this.mc.entityRenderer != null && this.mc.entityRenderer.isShaderActive()) {
            ☃x.add("Shader: " + this.mc.entityRenderer.getShaderGroup().getShaderGroupName());
         }

         if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && this.mc.objectMouseOver.getBlockPos() != null
            )
          {
            BlockPos ☃xx = this.mc.objectMouseOver.getBlockPos();
            ☃x.add(String.format("Looking at: %d %d %d", ☃xx.getX(), ☃xx.getY(), ☃xx.getZ()));
         }

         return ☃x;
      }
   }

   protected <T extends Comparable<T>> List<String> getDebugInfoRight() {
      long ☃ = Runtime.getRuntime().maxMemory();
      long ☃x = Runtime.getRuntime().totalMemory();
      long ☃xx = Runtime.getRuntime().freeMemory();
      long ☃xxx = ☃x - ☃xx;
      List<String> ☃xxxx = Lists.newArrayList(
         new String[]{
            String.format("Java: %s %dbit", System.getProperty("java.version"), this.mc.isJava64bit() ? 64 : 32),
            String.format("Mem: % 2d%% %03d/%03dMB", ☃xxx * 100L / ☃, bytesToMb(☃xxx), bytesToMb(☃)),
            String.format("Allocated: % 2d%% %03dMB", ☃x * 100L / ☃, bytesToMb(☃x)),
            "",
            String.format("CPU: %s", OpenGlHelper.getCpu()),
            "",
            String.format("Display: %dx%d (%s)", Display.getWidth(), Display.getHeight(), GlStateManager.glGetString(7936)),
            GlStateManager.glGetString(7937),
            GlStateManager.glGetString(7938)
         }
      );
      if (this.mc.isReducedDebug()) {
         return ☃xxxx;
      } else {
         if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && this.mc.objectMouseOver.getBlockPos() != null
            )
          {
            BlockPos ☃xxxxx = this.mc.objectMouseOver.getBlockPos();
            IBlockState ☃xxxxxx = this.mc.world.getBlockState(☃xxxxx);
            if (this.mc.world.getWorldType() != WorldType.DEBUG_ALL_BLOCK_STATES) {
               ☃xxxxxx = ☃xxxxxx.getActualState(this.mc.world, ☃xxxxx);
            }

            ☃xxxx.add("");
            ☃xxxx.add(String.valueOf(Block.REGISTRY.getNameForObject(☃xxxxxx.getBlock())));
            UnmodifiableIterator var12 = ☃xxxxxx.getProperties().entrySet().iterator();

            while (var12.hasNext()) {
               Entry<IProperty<?>, Comparable<?>> ☃xxxxxxx = (Entry<IProperty<?>, Comparable<?>>)var12.next();
               IProperty<T> ☃xxxxxxxx = (IProperty<T>)☃xxxxxxx.getKey();
               T ☃xxxxxxxxx = (T)☃xxxxxxx.getValue();
               String ☃xxxxxxxxxx = ☃xxxxxxxx.getName(☃xxxxxxxxx);
               if (Boolean.TRUE.equals(☃xxxxxxxxx)) {
                  ☃xxxxxxxxxx = TextFormatting.GREEN + ☃xxxxxxxxxx;
               } else if (Boolean.FALSE.equals(☃xxxxxxxxx)) {
                  ☃xxxxxxxxxx = TextFormatting.RED + ☃xxxxxxxxxx;
               }

               ☃xxxx.add(☃xxxxxxxx.getName() + ": " + ☃xxxxxxxxxx);
            }
         }

         return ☃xxxx;
      }
   }

   private void renderLagometer() {
      GlStateManager.disableDepth();
      FrameTimer ☃ = this.mc.getFrameTimer();
      int ☃x = ☃.getLastIndex();
      int ☃xx = ☃.getIndex();
      long[] ☃xxx = ☃.getFrames();
      ScaledResolution ☃xxxx = new ScaledResolution(this.mc);
      int ☃xxxxx = ☃x;
      int ☃xxxxxx = 0;
      drawRect(0, ☃xxxx.getScaledHeight() - 60, 240, ☃xxxx.getScaledHeight(), -1873784752);

      while (☃xxxxx != ☃xx) {
         int ☃xxxxxxx = ☃.getLagometerValue(☃xxx[☃xxxxx], 30);
         int ☃xxxxxxxx = this.getFrameColor(MathHelper.clamp(☃xxxxxxx, 0, 60), 0, 30, 60);
         this.drawVerticalLine(☃xxxxxx, ☃xxxx.getScaledHeight(), ☃xxxx.getScaledHeight() - ☃xxxxxxx, ☃xxxxxxxx);
         ☃xxxxxx++;
         ☃xxxxx = ☃.parseIndex(☃xxxxx + 1);
      }

      drawRect(1, ☃xxxx.getScaledHeight() - 30 + 1, 14, ☃xxxx.getScaledHeight() - 30 + 10, -1873784752);
      this.fontRenderer.drawString("60", 2, ☃xxxx.getScaledHeight() - 30 + 2, 14737632);
      this.drawHorizontalLine(0, 239, ☃xxxx.getScaledHeight() - 30, -1);
      drawRect(1, ☃xxxx.getScaledHeight() - 60 + 1, 14, ☃xxxx.getScaledHeight() - 60 + 10, -1873784752);
      this.fontRenderer.drawString("30", 2, ☃xxxx.getScaledHeight() - 60 + 2, 14737632);
      this.drawHorizontalLine(0, 239, ☃xxxx.getScaledHeight() - 60, -1);
      this.drawHorizontalLine(0, 239, ☃xxxx.getScaledHeight() - 1, -1);
      this.drawVerticalLine(0, ☃xxxx.getScaledHeight() - 60, ☃xxxx.getScaledHeight(), -1);
      this.drawVerticalLine(239, ☃xxxx.getScaledHeight() - 60, ☃xxxx.getScaledHeight(), -1);
      if (this.mc.gameSettings.limitFramerate <= 120) {
         this.drawHorizontalLine(0, 239, ☃xxxx.getScaledHeight() - 60 + this.mc.gameSettings.limitFramerate / 2, -16711681);
      }

      GlStateManager.enableDepth();
   }

   private int getFrameColor(int var1, int var2, int var3, int var4) {
      return ☃ < ☃ ? this.blendColors(-16711936, -256, (float)☃ / ☃) : this.blendColors(-256, -65536, (float)(☃ - ☃) / (☃ - ☃));
   }

   private int blendColors(int var1, int var2, float var3) {
      int ☃ = ☃ >> 24 & 0xFF;
      int ☃x = ☃ >> 16 & 0xFF;
      int ☃xx = ☃ >> 8 & 0xFF;
      int ☃xxx = ☃ & 0xFF;
      int ☃xxxx = ☃ >> 24 & 0xFF;
      int ☃xxxxx = ☃ >> 16 & 0xFF;
      int ☃xxxxxx = ☃ >> 8 & 0xFF;
      int ☃xxxxxxx = ☃ & 0xFF;
      int ☃xxxxxxxx = MathHelper.clamp((int)(☃ + (☃xxxx - ☃) * ☃), 0, 255);
      int ☃xxxxxxxxx = MathHelper.clamp((int)(☃x + (☃xxxxx - ☃x) * ☃), 0, 255);
      int ☃xxxxxxxxxx = MathHelper.clamp((int)(☃xx + (☃xxxxxx - ☃xx) * ☃), 0, 255);
      int ☃xxxxxxxxxxx = MathHelper.clamp((int)(☃xxx + (☃xxxxxxx - ☃xxx) * ☃), 0, 255);
      return ☃xxxxxxxx << 24 | ☃xxxxxxxxx << 16 | ☃xxxxxxxxxx << 8 | ☃xxxxxxxxxxx;
   }

   private static long bytesToMb(long var0) {
      return ☃ / 1024L / 1024L;
   }
}
