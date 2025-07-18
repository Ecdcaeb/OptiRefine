package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;

public class RenderFireball extends Render<EntityFireball> {
   private final float scale;

   public RenderFireball(RenderManager var1, float var2) {
      super(☃);
      this.scale = ☃;
   }

   public void doRender(EntityFireball var1, double var2, double var4, double var6, float var8, float var9) {
      GlStateManager.pushMatrix();
      this.bindEntityTexture(☃);
      GlStateManager.translate((float)☃, (float)☃, (float)☃);
      GlStateManager.enableRescaleNormal();
      GlStateManager.scale(this.scale, this.scale, this.scale);
      TextureAtlasSprite ☃ = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(Items.FIRE_CHARGE);
      Tessellator ☃x = Tessellator.getInstance();
      BufferBuilder ☃xx = ☃x.getBuffer();
      float ☃xxx = ☃.getMinU();
      float ☃xxxx = ☃.getMaxU();
      float ☃xxxxx = ☃.getMinV();
      float ☃xxxxxx = ☃.getMaxV();
      float ☃xxxxxxx = 1.0F;
      float ☃xxxxxxxx = 0.5F;
      float ☃xxxxxxxxx = 0.25F;
      GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate((this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      if (this.renderOutlines) {
         GlStateManager.enableColorMaterial();
         GlStateManager.enableOutlineMode(this.getTeamColor(☃));
      }

      ☃xx.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
      ☃xx.pos(-0.5, -0.25, 0.0).tex(☃xxx, ☃xxxxxx).normal(0.0F, 1.0F, 0.0F).endVertex();
      ☃xx.pos(0.5, -0.25, 0.0).tex(☃xxxx, ☃xxxxxx).normal(0.0F, 1.0F, 0.0F).endVertex();
      ☃xx.pos(0.5, 0.75, 0.0).tex(☃xxxx, ☃xxxxx).normal(0.0F, 1.0F, 0.0F).endVertex();
      ☃xx.pos(-0.5, 0.75, 0.0).tex(☃xxx, ☃xxxxx).normal(0.0F, 1.0F, 0.0F).endVertex();
      ☃x.draw();
      if (this.renderOutlines) {
         GlStateManager.disableOutlineMode();
         GlStateManager.disableColorMaterial();
      }

      GlStateManager.disableRescaleNormal();
      GlStateManager.popMatrix();
      super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
   }

   protected ResourceLocation getEntityTexture(EntityFireball var1) {
      return TextureMap.LOCATION_BLOCKS_TEXTURE;
   }
}
