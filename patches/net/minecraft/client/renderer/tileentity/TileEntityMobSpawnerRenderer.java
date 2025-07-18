package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityMobSpawner;

public class TileEntityMobSpawnerRenderer extends TileEntitySpecialRenderer<TileEntityMobSpawner> {
   public void render(TileEntityMobSpawner var1, double var2, double var4, double var6, float var8, int var9, float var10) {
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)☃ + 0.5F, (float)☃, (float)☃ + 0.5F);
      renderMob(☃.getSpawnerBaseLogic(), ☃, ☃, ☃, ☃);
      GlStateManager.popMatrix();
   }

   public static void renderMob(MobSpawnerBaseLogic var0, double var1, double var3, double var5, float var7) {
      Entity ☃ = ☃.getCachedEntity();
      if (☃ != null) {
         float ☃x = 0.53125F;
         float ☃xx = Math.max(☃.width, ☃.height);
         if (☃xx > 1.0) {
            ☃x /= ☃xx;
         }

         GlStateManager.translate(0.0F, 0.4F, 0.0F);
         GlStateManager.rotate((float)(☃.getPrevMobRotation() + (☃.getMobRotation() - ☃.getPrevMobRotation()) * ☃) * 10.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.translate(0.0F, -0.2F, 0.0F);
         GlStateManager.rotate(-30.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.scale(☃x, ☃x, ☃x);
         ☃.setLocationAndAngles(☃, ☃, ☃, 0.0F, 0.0F);
         Minecraft.getMinecraft().getRenderManager().renderEntity(☃, 0.0, 0.0, 0.0, 0.0F, ☃, false);
      }
   }
}
