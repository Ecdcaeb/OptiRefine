package net.minecraft.client.renderer.tileentity;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class TileEntityRendererDispatcher {
   private final Map<Class<? extends TileEntity>, TileEntitySpecialRenderer<? extends TileEntity>> renderers = Maps.newHashMap();
   public static TileEntityRendererDispatcher instance = new TileEntityRendererDispatcher();
   private FontRenderer fontRenderer;
   public static double staticPlayerX;
   public static double staticPlayerY;
   public static double staticPlayerZ;
   public TextureManager renderEngine;
   public World world;
   public Entity entity;
   public float entityYaw;
   public float entityPitch;
   public RayTraceResult cameraHitResult;
   public double entityX;
   public double entityY;
   public double entityZ;

   private TileEntityRendererDispatcher() {
      this.renderers.put(TileEntitySign.class, new TileEntitySignRenderer());
      this.renderers.put(TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
      this.renderers.put(TileEntityPiston.class, new TileEntityPistonRenderer());
      this.renderers.put(TileEntityChest.class, new TileEntityChestRenderer());
      this.renderers.put(TileEntityEnderChest.class, new TileEntityEnderChestRenderer());
      this.renderers.put(TileEntityEnchantmentTable.class, new TileEntityEnchantmentTableRenderer());
      this.renderers.put(TileEntityEndPortal.class, new TileEntityEndPortalRenderer());
      this.renderers.put(TileEntityEndGateway.class, new TileEntityEndGatewayRenderer());
      this.renderers.put(TileEntityBeacon.class, new TileEntityBeaconRenderer());
      this.renderers.put(TileEntitySkull.class, new TileEntitySkullRenderer());
      this.renderers.put(TileEntityBanner.class, new TileEntityBannerRenderer());
      this.renderers.put(TileEntityStructure.class, new TileEntityStructureRenderer());
      this.renderers.put(TileEntityShulkerBox.class, new TileEntityShulkerBoxRenderer(new ModelShulker()));
      this.renderers.put(TileEntityBed.class, new TileEntityBedRenderer());

      for (TileEntitySpecialRenderer<?> ☃ : this.renderers.values()) {
         ☃.setRendererDispatcher(this);
      }
   }

   public <T extends TileEntity> TileEntitySpecialRenderer<T> getRenderer(Class<? extends TileEntity> var1) {
      TileEntitySpecialRenderer<? extends TileEntity> ☃ = this.renderers.get(☃);
      if (☃ == null && ☃ != TileEntity.class) {
         ☃ = this.getRenderer((Class<? extends TileEntity>)☃.getSuperclass());
         this.renderers.put(☃, ☃);
      }

      return (TileEntitySpecialRenderer<T>)☃;
   }

   @Nullable
   public <T extends TileEntity> TileEntitySpecialRenderer<T> getRenderer(@Nullable TileEntity var1) {
      return ☃ == null ? null : this.getRenderer((Class<? extends TileEntity>)☃.getClass());
   }

   public void prepare(World var1, TextureManager var2, FontRenderer var3, Entity var4, RayTraceResult var5, float var6) {
      if (this.world != ☃) {
         this.setWorld(☃);
      }

      this.renderEngine = ☃;
      this.entity = ☃;
      this.fontRenderer = ☃;
      this.cameraHitResult = ☃;
      this.entityYaw = ☃.prevRotationYaw + (☃.rotationYaw - ☃.prevRotationYaw) * ☃;
      this.entityPitch = ☃.prevRotationPitch + (☃.rotationPitch - ☃.prevRotationPitch) * ☃;
      this.entityX = ☃.lastTickPosX + (☃.posX - ☃.lastTickPosX) * ☃;
      this.entityY = ☃.lastTickPosY + (☃.posY - ☃.lastTickPosY) * ☃;
      this.entityZ = ☃.lastTickPosZ + (☃.posZ - ☃.lastTickPosZ) * ☃;
   }

   public void render(TileEntity var1, float var2, int var3) {
      if (☃.getDistanceSq(this.entityX, this.entityY, this.entityZ) < ☃.getMaxRenderDistanceSquared()) {
         RenderHelper.enableStandardItemLighting();
         int ☃ = this.world.getCombinedLight(☃.getPos(), 0);
         int ☃x = ☃ % 65536;
         int ☃xx = ☃ / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, ☃x, ☃xx);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         BlockPos ☃xxx = ☃.getPos();
         this.render(☃, ☃xxx.getX() - staticPlayerX, ☃xxx.getY() - staticPlayerY, ☃xxx.getZ() - staticPlayerZ, ☃, ☃, 1.0F);
      }
   }

   public void render(TileEntity var1, double var2, double var4, double var6, float var8) {
      this.render(☃, ☃, ☃, ☃, ☃, 1.0F);
   }

   public void render(TileEntity var1, double var2, double var4, double var6, float var8, float var9) {
      this.render(☃, ☃, ☃, ☃, ☃, -1, ☃);
   }

   public void render(TileEntity var1, double var2, double var4, double var6, float var8, int var9, float var10) {
      TileEntitySpecialRenderer<TileEntity> ☃ = this.getRenderer(☃);
      if (☃ != null) {
         try {
            ☃.render(☃, ☃, ☃, ☃, ☃, ☃, ☃);
         } catch (Throwable var15) {
            CrashReport ☃x = CrashReport.makeCrashReport(var15, "Rendering Block Entity");
            CrashReportCategory ☃xx = ☃x.makeCategory("Block Entity Details");
            ☃.addInfoToCrashReport(☃xx);
            throw new ReportedException(☃x);
         }
      }
   }

   public void setWorld(@Nullable World var1) {
      this.world = ☃;
      if (☃ == null) {
         this.entity = null;
      }
   }

   public FontRenderer getFontRenderer() {
      return this.fontRenderer;
   }
}
