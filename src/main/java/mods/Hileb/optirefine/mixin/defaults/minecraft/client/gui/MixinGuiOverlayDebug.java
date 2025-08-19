package mods.Hileb.optirefine.mixin.defaults.minecraft.client.gui;

import mods.Hileb.optirefine.library.cursedmixinextensions.annotations.AccessibleOperation;
import mods.Hileb.optirefine.optifine.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.client.renderer.texture.TextureMap;
import net.optifine.SmartAnimations;
import net.optifine.TextureAnimations;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(GuiOverlayDebug.class)
public abstract class MixinGuiOverlayDebug extends Gui {
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private String debugOF = null;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    private List<String> debugInfoLeft = null;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    private List<String> debugInfoRight = null;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    private long updateInfoLeftTimeMs = 0L;

    @SuppressWarnings({"unused", "AddedMixinMembersNamePattern"})
    @Unique
    private long updateInfoRightTimeMs = 0L;

    @SuppressWarnings("unused")
    @Unique
    @AccessibleOperation.Reference(TextureMap.class)
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureMap getCountAnimationsActive ()I")
    private native static int _acc_TextureMap_getCountAnimationsActive_(TextureMap instance);

    @SuppressWarnings("unused")
    @Unique
    @AccessibleOperation(opcode = Opcodes.INVOKEVIRTUAL, desc = "net.minecraft.client.renderer.texture.TextureMap getCountAnimations ()I")
    private native static int _acc_TextureMap_getCountAnimations_(TextureMap instance);

    @Inject(method = "call", at = @At("RETURN"), cancellable = true)
    public void injectCall(CallbackInfoReturnable<List<String>> cir){
        if (Minecraft.getMinecraft().debug.equals(this.debugOF)) {
            StringBuilder sb = new StringBuilder(Minecraft.getMinecraft().debug);
            int fpsMin = Config.getFpsMin();
            int posFps = Minecraft.getMinecraft().debug.indexOf(" fps ");
            if (posFps >= 0) {
                sb.insert(posFps, "/" + fpsMin);
            }

            if (Config.isSmoothFps()) {
                sb.append(" sf");
            }

            if (Config.isFastRender()) {
                sb.append(" fr");
            }

            if (Config.isAnisotropicFiltering()) {
                sb.append(" af");
            }

            if (Config.isAntialiasing()) {
                sb.append(" aa");
            }

            if (Config.isRenderRegions()) {
                sb.append(" reg");
            }

            if (Config.isShaders()) {
                sb.append(" sh");
            }

            Minecraft.getMinecraft().debug = sb.toString();
            this.debugOF = Minecraft.getMinecraft().debug;
        }

        StringBuilder sbx = new StringBuilder();
        TextureMap tm = Config.getTextureMap();
        sbx.append(", A: ");
        if (SmartAnimations.isActive()) {
            sbx.append(_acc_TextureMap_getCountAnimationsActive_(tm) + TextureAnimations.getCountAnimationsActive());
            sbx.append("/");
        }

        sbx.append(_acc_TextureMap_getCountAnimations_(tm) + TextureAnimations.getCountAnimations());
        String ofInfo = sbx.toString();

        cir.setReturnValue(cir.getReturnValue().stream().map((s) -> s.startsWith("P: ") ? s + ofInfo : s).toList());
    }
}
/*
+++ net/minecraft/client/gui/GuiOverlayDebug.java	Tue Aug 19 14:59:58 2025
@@ -1,38 +1,49 @@
 package net.minecraft.client.gui;

 import com.google.common.base.Strings;
 import com.google.common.collect.Lists;
 import com.google.common.collect.UnmodifiableIterator;
 import java.util.ArrayList;
+import java.util.Collection;
 import java.util.List;
 import java.util.Map.Entry;
 import net.minecraft.block.Block;
 import net.minecraft.block.properties.IProperty;
 import net.minecraft.block.state.IBlockState;
 import net.minecraft.client.ClientBrandRetriever;
 import net.minecraft.client.Minecraft;
 import net.minecraft.client.renderer.GlStateManager;
 import net.minecraft.client.renderer.OpenGlHelper;
+import net.minecraft.client.renderer.texture.TextureMap;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.player.EntityPlayerMP;
 import net.minecraft.util.EnumFacing;
-import net.minecraft.util.FrameTimer;
 import net.minecraft.util.math.BlockPos;
 import net.minecraft.util.math.MathHelper;
-import net.minecraft.util.math.RayTraceResult;
+import net.minecraft.util.math.RayTraceResult.Type;
 import net.minecraft.util.text.TextFormatting;
 import net.minecraft.world.DifficultyInstance;
 import net.minecraft.world.EnumSkyBlock;
 import net.minecraft.world.WorldType;
 import net.minecraft.world.chunk.Chunk;
+import net.optifine.SmartAnimations;
+import net.optifine.TextureAnimations;
+import net.optifine.reflect.Reflector;
+import net.optifine.util.MemoryMonitor;
+import net.optifine.util.NativeMemory;
 import org.lwjgl.opengl.Display;

 public class GuiOverlayDebug extends Gui {
    private final Minecraft mc;
    private final FontRenderer fontRenderer;
+   private String debugOF = null;
+   private List<String> debugInfoLeft = null;
+   private List<String> debugInfoRight = null;
+   private long updateInfoLeftTimeMs = 0L;
+   private long updateInfoRightTimeMs = 0L;

    public GuiOverlayDebug(Minecraft var1) {
       this.mc = var1;
       this.fontRenderer = var1.fontRenderer;
    }

@@ -47,21 +58,26 @@
       }

       this.mc.profiler.endSection();
    }

    protected void renderDebugInfoLeft() {
-      List var1 = this.call();
-      var1.add("");
-      var1.add(
-         "Debug: Pie [shift]: "
-            + (this.mc.gameSettings.showDebugProfilerChart ? "visible" : "hidden")
-            + " FPS [alt]: "
-            + (this.mc.gameSettings.showLagometer ? "visible" : "hidden")
-      );
-      var1.add("For help: press F3 + Q");
+      List var1 = this.debugInfoLeft;
+      if (var1 == null || System.currentTimeMillis() > this.updateInfoLeftTimeMs) {
+         var1 = this.call();
+         var1.add("");
+         var1.add(
+            "Debug: Pie [shift]: "
+               + (this.mc.gameSettings.showDebugProfilerChart ? "visible" : "hidden")
+               + " FPS [alt]: "
+               + (this.mc.gameSettings.showLagometer ? "visible" : "hidden")
+         );
+         var1.add("For help: press F3 + Q");
+         this.debugInfoLeft = var1;
+         this.updateInfoLeftTimeMs = System.currentTimeMillis() + 100L;
+      }

       for (int var2 = 0; var2 < var1.size(); var2++) {
          String var3 = (String)var1.get(var2);
          if (!Strings.isNullOrEmpty(var3)) {
             int var4 = this.fontRenderer.FONT_HEIGHT;
             int var5 = this.fontRenderer.getStringWidth(var3);
@@ -71,13 +87,18 @@
             this.fontRenderer.drawString(var3, 2, var7, 14737632);
          }
       }
    }

    protected void renderDebugInfoRight(ScaledResolution var1) {
-      List var2 = this.getDebugInfoRight();
+      List var2 = this.debugInfoRight;
+      if (var2 == null || System.currentTimeMillis() > this.updateInfoRightTimeMs) {
+         var2 = this.getDebugInfoRight();
+         this.debugInfoRight = var2;
+         this.updateInfoRightTimeMs = System.currentTimeMillis() + 100L;
+      }

       for (int var3 = 0; var3 < var2.size(); var3++) {
          String var4 = (String)var2.get(var3);
          if (!Strings.isNullOrEmpty(var4)) {
             int var5 = this.fontRenderer.FONT_HEIGHT;
             int var6 = this.fontRenderer.getStringWidth(var4);
@@ -90,118 +111,165 @@
    }

    protected List<String> call() {
       BlockPos var1 = new BlockPos(
          this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ
       );
+      if (this.mc.debug != this.debugOF) {
+         StringBuffer var2 = new StringBuffer(this.mc.debug);
+         int var3 = Config.getFpsMin();
+         int var4 = this.mc.debug.indexOf(" fps ");
+         if (var4 >= 0) {
+            var2.insert(var4, "/" + var3);
+         }
+
+         if (Config.isSmoothFps()) {
+            var2.append(" sf");
+         }
+
+         if (Config.isFastRender()) {
+            var2.append(" fr");
+         }
+
+         if (Config.isAnisotropicFiltering()) {
+            var2.append(" af");
+         }
+
+         if (Config.isAntialiasing()) {
+            var2.append(" aa");
+         }
+
+         if (Config.isRenderRegions()) {
+            var2.append(" reg");
+         }
+
+         if (Config.isShaders()) {
+            var2.append(" sh");
+         }
+
+         this.mc.debug = var2.toString();
+         this.debugOF = this.mc.debug;
+      }
+
+      StringBuilder var13 = new StringBuilder();
+      TextureMap var14 = Config.getTextureMap();
+      var13.append(", A: ");
+      if (SmartAnimations.isActive()) {
+         var13.append(var14.getCountAnimationsActive() + TextureAnimations.getCountAnimationsActive());
+         var13.append("/");
+      }
+
+      var13.append(var14.getCountAnimations() + TextureAnimations.getCountAnimations());
+      String var15 = var13.toString();
       if (this.mc.isReducedDebug()) {
          return Lists.newArrayList(
             new String[]{
                "Minecraft 1.12.2 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ")",
                this.mc.debug,
                this.mc.renderGlobal.getDebugInfoRenders(),
                this.mc.renderGlobal.getDebugInfoEntities(),
-               "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.world.getDebugLoadedEntities(),
+               "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.world.getDebugLoadedEntities() + var15,
                this.mc.world.getProviderName(),
                "",
                String.format("Chunk-relative: %d %d %d", var1.getX() & 15, var1.getY() & 15, var1.getZ() & 15)
             }
          );
       } else {
-         Entity var2 = this.mc.getRenderViewEntity();
-         EnumFacing var3 = var2.getHorizontalFacing();
-         String var4 = "Invalid";
-         switch (var3) {
+         Entity var5 = this.mc.getRenderViewEntity();
+         EnumFacing var6 = var5.getHorizontalFacing();
+         String var7 = "Invalid";
+         switch (var6) {
             case NORTH:
-               var4 = "Towards negative Z";
+               var7 = "Towards negative Z";
                break;
             case SOUTH:
-               var4 = "Towards positive Z";
+               var7 = "Towards positive Z";
                break;
             case WEST:
-               var4 = "Towards negative X";
+               var7 = "Towards negative X";
                break;
             case EAST:
-               var4 = "Towards positive X";
+               var7 = "Towards positive X";
          }

-         ArrayList var5 = Lists.newArrayList(
+         ArrayList var8 = Lists.newArrayList(
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
-               "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.world.getDebugLoadedEntities(),
+               "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.world.getDebugLoadedEntities() + var15,
                this.mc.world.getProviderName(),
                "",
                String.format(
                   "XYZ: %.3f / %.5f / %.3f",
                   this.mc.getRenderViewEntity().posX,
                   this.mc.getRenderViewEntity().getEntityBoundingBox().minY,
                   this.mc.getRenderViewEntity().posZ
                ),
                String.format("Block: %d %d %d", var1.getX(), var1.getY(), var1.getZ()),
                String.format(
                   "Chunk: %d %d %d in %d %d %d", var1.getX() & 15, var1.getY() & 15, var1.getZ() & 15, var1.getX() >> 4, var1.getY() >> 4, var1.getZ() >> 4
                ),
-               String.format("Facing: %s (%s) (%.1f / %.1f)", var3, var4, MathHelper.wrapDegrees(var2.rotationYaw), MathHelper.wrapDegrees(var2.rotationPitch))
+               String.format("Facing: %s (%s) (%.1f / %.1f)", var6, var7, MathHelper.wrapDegrees(var5.rotationYaw), MathHelper.wrapDegrees(var5.rotationPitch))
             }
          );
          if (this.mc.world != null) {
-            Chunk var6 = this.mc.world.getChunk(var1);
+            Chunk var9 = this.mc.world.getChunk(var1);
             if (!this.mc.world.isBlockLoaded(var1) || var1.getY() < 0 || var1.getY() >= 256) {
-               var5.add("Outside of world...");
-            } else if (!var6.isEmpty()) {
-               var5.add("Biome: " + var6.getBiome(var1, this.mc.world.getBiomeProvider()).getBiomeName());
-               var5.add(
+               var8.add("Outside of world...");
+            } else if (!var9.isEmpty()) {
+               var8.add("Biome: " + var9.getBiome(var1, this.mc.world.getBiomeProvider()).getBiomeName());
+               var8.add(
                   "Light: "
-                     + var6.getLightSubtracted(var1, 0)
+                     + var9.getLightSubtracted(var1, 0)
                      + " ("
-                     + var6.getLightFor(EnumSkyBlock.SKY, var1)
+                     + var9.getLightFor(EnumSkyBlock.SKY, var1)
                      + " sky, "
-                     + var6.getLightFor(EnumSkyBlock.BLOCK, var1)
+                     + var9.getLightFor(EnumSkyBlock.BLOCK, var1)
                      + " block)"
                );
-               DifficultyInstance var7 = this.mc.world.getDifficultyForLocation(var1);
+               DifficultyInstance var10 = this.mc.world.getDifficultyForLocation(var1);
                if (this.mc.isIntegratedServerRunning() && this.mc.getIntegratedServer() != null) {
-                  EntityPlayerMP var8 = this.mc.getIntegratedServer().getPlayerList().getPlayerByUUID(this.mc.player.getUniqueID());
-                  if (var8 != null) {
-                     var7 = var8.world.getDifficultyForLocation(new BlockPos(var8));
+                  EntityPlayerMP var11 = this.mc.getIntegratedServer().getPlayerList().getPlayerByUUID(this.mc.player.getUniqueID());
+                  if (var11 != null) {
+                     DifficultyInstance var12 = this.mc.getIntegratedServer().getDifficultyAsync(var11.world, new BlockPos(var11));
+                     if (var12 != null) {
+                        var10 = var12;
+                     }
                   }
                }

-               var5.add(
+               var8.add(
                   String.format(
                      "Local Difficulty: %.2f // %.2f (Day %d)",
-                     var7.getAdditionalDifficulty(),
-                     var7.getClampedAdditionalDifficulty(),
+                     var10.getAdditionalDifficulty(),
+                     var10.getClampedAdditionalDifficulty(),
                      this.mc.world.getWorldTime() / 24000L
                   )
                );
             } else {
-               var5.add("Waiting for chunk...");
+               var8.add("Waiting for chunk...");
             }
          }

          if (this.mc.entityRenderer != null && this.mc.entityRenderer.isShaderActive()) {
-            var5.add("Shader: " + this.mc.entityRenderer.getShaderGroup().getShaderGroupName());
+            var8.add("Shader: " + this.mc.entityRenderer.getShaderGroup().getShaderGroupName());
          }

-         if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && this.mc.objectMouseOver.getBlockPos() != null
-            )
-          {
-            BlockPos var9 = this.mc.objectMouseOver.getBlockPos();
-            var5.add(String.format("Looking at: %d %d %d", var9.getX(), var9.getY(), var9.getZ()));
+         if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == Type.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
+            BlockPos var16 = this.mc.objectMouseOver.getBlockPos();
+            var8.add(String.format("Looking at: %d %d %d", var16.getX(), var16.getY(), var16.getZ()));
          }

-         return var5;
+         return var8;
       }
    }

    protected <T extends Comparable<T>> List<String> getDebugInfoRight() {
       long var1 = Runtime.getRuntime().maxMemory();
       long var3 = Runtime.getRuntime().totalMemory();
@@ -217,80 +285,57 @@
             "",
             String.format("Display: %dx%d (%s)", Display.getWidth(), Display.getHeight(), GlStateManager.glGetString(7936)),
             GlStateManager.glGetString(7937),
             GlStateManager.glGetString(7938)
          }
       );
+      long var10 = NativeMemory.getBufferAllocated();
+      long var12 = NativeMemory.getBufferMaximum();
+      String var14 = "Native: " + bytesToMb(var10) + "/" + bytesToMb(var12) + "MB";
+      var9.add(4, var14);
+      var9.set(5, "GC: " + MemoryMonitor.getAllocationRateMb() + "MB/s");
+      if (Reflector.FMLCommonHandler_getBrandings.exists()) {
+         Object var15 = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
+         var9.add("");
+         var9.addAll((Collection)Reflector.call(var15, Reflector.FMLCommonHandler_getBrandings, new Object[]{false}));
+      }
+
       if (this.mc.isReducedDebug()) {
          return var9;
       } else {
-         if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && this.mc.objectMouseOver.getBlockPos() != null
-            )
-          {
-            BlockPos var10 = this.mc.objectMouseOver.getBlockPos();
-            IBlockState var11 = this.mc.world.getBlockState(var10);
+         if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == Type.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
+            BlockPos var22 = this.mc.objectMouseOver.getBlockPos();
+            IBlockState var16 = this.mc.world.getBlockState(var22);
             if (this.mc.world.getWorldType() != WorldType.DEBUG_ALL_BLOCK_STATES) {
-               var11 = var11.getActualState(this.mc.world, var10);
+               var16 = var16.c(this.mc.world, var22);
             }

             var9.add("");
-            var9.add(String.valueOf(Block.REGISTRY.getNameForObject(var11.getBlock())));
-            UnmodifiableIterator var12 = var11.getProperties().entrySet().iterator();
+            var9.add(String.valueOf(Block.REGISTRY.getNameForObject(var16.getBlock())));
+            UnmodifiableIterator var19 = var16.getProperties().entrySet().iterator();

-            while (var12.hasNext()) {
-               Entry var13 = (Entry)var12.next();
-               IProperty var14 = (IProperty)var13.getKey();
-               Comparable var15 = (Comparable)var13.getValue();
-               Object var16 = var14.getName((T)var15);
-               if (Boolean.TRUE.equals(var15)) {
-                  var16 = TextFormatting.GREEN + var16;
-               } else if (Boolean.FALSE.equals(var15)) {
-                  var16 = TextFormatting.RED + var16;
+            while (var19.hasNext()) {
+               Entry var20 = (Entry)var19.next();
+               IProperty var17 = (IProperty)var20.getKey();
+               Comparable var21 = (Comparable)var20.getValue();
+               Object var18 = var17.getName(var21);
+               if (Boolean.TRUE.equals(var21)) {
+                  var18 = TextFormatting.GREEN + var18;
+               } else if (Boolean.FALSE.equals(var21)) {
+                  var18 = TextFormatting.RED + var18;
                }

-               var9.add(var14.getName() + ": " + var16);
+               var9.add(var17.getName() + ": " + var18);
             }
          }

          return var9;
       }
    }

    private void renderLagometer() {
-      GlStateManager.disableDepth();
-      FrameTimer var1 = this.mc.getFrameTimer();
-      int var2 = var1.getLastIndex();
-      int var3 = var1.getIndex();
-      long[] var4 = var1.getFrames();
-      ScaledResolution var5 = new ScaledResolution(this.mc);
-      int var6 = var2;
-      int var7 = 0;
-      drawRect(0, var5.getScaledHeight() - 60, 240, var5.getScaledHeight(), -1873784752);
-
-      while (var6 != var3) {
-         int var8 = var1.getLagometerValue(var4[var6], 30);
-         int var9 = this.getFrameColor(MathHelper.clamp(var8, 0, 60), 0, 30, 60);
-         this.drawVerticalLine(var7, var5.getScaledHeight(), var5.getScaledHeight() - var8, var9);
-         var7++;
-         var6 = var1.parseIndex(var6 + 1);
-      }
-
-      drawRect(1, var5.getScaledHeight() - 30 + 1, 14, var5.getScaledHeight() - 30 + 10, -1873784752);
-      this.fontRenderer.drawString("60", 2, var5.getScaledHeight() - 30 + 2, 14737632);
-      this.drawHorizontalLine(0, 239, var5.getScaledHeight() - 30, -1);
-      drawRect(1, var5.getScaledHeight() - 60 + 1, 14, var5.getScaledHeight() - 60 + 10, -1873784752);
-      this.fontRenderer.drawString("30", 2, var5.getScaledHeight() - 60 + 2, 14737632);
-      this.drawHorizontalLine(0, 239, var5.getScaledHeight() - 60, -1);
-      this.drawHorizontalLine(0, 239, var5.getScaledHeight() - 1, -1);
-      this.drawVerticalLine(0, var5.getScaledHeight() - 60, var5.getScaledHeight(), -1);
-      this.drawVerticalLine(239, var5.getScaledHeight() - 60, var5.getScaledHeight(), -1);
-      if (this.mc.gameSettings.limitFramerate <= 120) {
-         this.drawHorizontalLine(0, 239, var5.getScaledHeight() - 60 + this.mc.gameSettings.limitFramerate / 2, -16711681);
-      }
-
-      GlStateManager.enableDepth();
    }

    private int getFrameColor(int var1, int var2, int var3, int var4) {
       return var1 < var3 ? this.blendColors(-16711936, -256, (float)var1 / var3) : this.blendColors(-256, -65536, (float)(var1 - var3) / (var4 - var3));
    }

 */