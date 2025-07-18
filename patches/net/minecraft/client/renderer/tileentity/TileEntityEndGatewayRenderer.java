package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class TileEntityEndGatewayRenderer extends TileEntityEndPortalRenderer {
   private static final ResourceLocation END_GATEWAY_BEAM_TEXTURE = new ResourceLocation("textures/entity/end_gateway_beam.png");

   @Override
   public void render(TileEntityEndPortal var1, double var2, double var4, double var6, float var8, int var9, float var10) {
      GlStateManager.disableFog();
      TileEntityEndGateway ☃ = (TileEntityEndGateway)☃;
      if (☃.isSpawning() || ☃.isCoolingDown()) {
         GlStateManager.alphaFunc(516, 0.1F);
         this.bindTexture(END_GATEWAY_BEAM_TEXTURE);
         float ☃x = ☃.isSpawning() ? ☃.getSpawnPercent(☃) : ☃.getCooldownPercent(☃);
         double ☃xx = ☃.isSpawning() ? 256.0 - ☃ : 50.0;
         ☃x = MathHelper.sin(☃x * (float) Math.PI);
         int ☃xxx = MathHelper.floor(☃x * ☃xx);
         float[] ☃xxxx = ☃.isSpawning() ? EnumDyeColor.MAGENTA.getColorComponentValues() : EnumDyeColor.PURPLE.getColorComponentValues();
         TileEntityBeaconRenderer.renderBeamSegment(☃, ☃, ☃, ☃, ☃x, ☃.getWorld().getTotalWorldTime(), 0, ☃xxx, ☃xxxx, 0.15, 0.175);
         TileEntityBeaconRenderer.renderBeamSegment(☃, ☃, ☃, ☃, ☃x, ☃.getWorld().getTotalWorldTime(), 0, -☃xxx, ☃xxxx, 0.15, 0.175);
      }

      super.render(☃, ☃, ☃, ☃, ☃, ☃, ☃);
      GlStateManager.enableFog();
   }

   @Override
   protected int getPasses(double var1) {
      return super.getPasses(☃) + 1;
   }

   @Override
   protected float getOffset() {
      return 1.0F;
   }
}
