package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ParticleItemPickup extends Particle {
   private final Entity item;
   private final Entity target;
   private int age;
   private final int maxAge;
   private final float yOffset;
   private final RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();

   public ParticleItemPickup(World var1, Entity var2, Entity var3, float var4) {
      super(☃, ☃.posX, ☃.posY, ☃.posZ, ☃.motionX, ☃.motionY, ☃.motionZ);
      this.item = ☃;
      this.target = ☃;
      this.maxAge = 3;
      this.yOffset = ☃;
   }

   @Override
   public void renderParticle(BufferBuilder var1, Entity var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float ☃ = (this.age + ☃) / this.maxAge;
      ☃ *= ☃;
      double ☃x = this.item.posX;
      double ☃xx = this.item.posY;
      double ☃xxx = this.item.posZ;
      double ☃xxxx = this.target.lastTickPosX + (this.target.posX - this.target.lastTickPosX) * ☃;
      double ☃xxxxx = this.target.lastTickPosY + (this.target.posY - this.target.lastTickPosY) * ☃ + this.yOffset;
      double ☃xxxxxx = this.target.lastTickPosZ + (this.target.posZ - this.target.lastTickPosZ) * ☃;
      double ☃xxxxxxx = ☃x + (☃xxxx - ☃x) * ☃;
      double ☃xxxxxxxx = ☃xx + (☃xxxxx - ☃xx) * ☃;
      double ☃xxxxxxxxx = ☃xxx + (☃xxxxxx - ☃xxx) * ☃;
      int ☃xxxxxxxxxx = this.getBrightnessForRender(☃);
      int ☃xxxxxxxxxxx = ☃xxxxxxxxxx % 65536;
      int ☃xxxxxxxxxxxx = ☃xxxxxxxxxx / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxx);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      ☃xxxxxxx -= interpPosX;
      ☃xxxxxxxx -= interpPosY;
      ☃xxxxxxxxx -= interpPosZ;
      GlStateManager.enableLighting();
      this.renderManager.renderEntity(this.item, ☃xxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxx, this.item.rotationYaw, ☃, false);
   }

   @Override
   public void onUpdate() {
      this.age++;
      if (this.age == this.maxAge) {
         this.setExpired();
      }
   }

   @Override
   public int getFXLayer() {
      return 3;
   }
}
