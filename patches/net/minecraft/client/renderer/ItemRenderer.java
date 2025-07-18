package net.minecraft.client.renderer;

import com.google.common.base.MoreObjects;
import java.util.Objects;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.MapData;

public class ItemRenderer {
   private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
   private static final ResourceLocation RES_UNDERWATER_OVERLAY = new ResourceLocation("textures/misc/underwater.png");
   private final Minecraft mc;
   private ItemStack itemStackMainHand = ItemStack.EMPTY;
   private ItemStack itemStackOffHand = ItemStack.EMPTY;
   private float equippedProgressMainHand;
   private float prevEquippedProgressMainHand;
   private float equippedProgressOffHand;
   private float prevEquippedProgressOffHand;
   private final RenderManager renderManager;
   private final RenderItem itemRenderer;

   public ItemRenderer(Minecraft var1) {
      this.mc = ☃;
      this.renderManager = ☃.getRenderManager();
      this.itemRenderer = ☃.getRenderItem();
   }

   public void renderItem(EntityLivingBase var1, ItemStack var2, ItemCameraTransforms.TransformType var3) {
      this.renderItemSide(☃, ☃, ☃, false);
   }

   public void renderItemSide(EntityLivingBase var1, ItemStack var2, ItemCameraTransforms.TransformType var3, boolean var4) {
      if (!☃.isEmpty()) {
         Item ☃ = ☃.getItem();
         Block ☃x = Block.getBlockFromItem(☃);
         GlStateManager.pushMatrix();
         boolean ☃xx = this.itemRenderer.shouldRenderItemIn3D(☃) && ☃x.getRenderLayer() == BlockRenderLayer.TRANSLUCENT;
         if (☃xx) {
            GlStateManager.depthMask(false);
         }

         this.itemRenderer.renderItem(☃, ☃, ☃, ☃);
         if (☃xx) {
            GlStateManager.depthMask(true);
         }

         GlStateManager.popMatrix();
      }
   }

   private void rotateArroundXAndY(float var1, float var2) {
      GlStateManager.pushMatrix();
      GlStateManager.rotate(☃, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(☃, 0.0F, 1.0F, 0.0F);
      RenderHelper.enableStandardItemLighting();
      GlStateManager.popMatrix();
   }

   private void setLightmap() {
      AbstractClientPlayer ☃ = this.mc.player;
      int ☃x = this.mc.world.getCombinedLight(new BlockPos(☃.posX, ☃.posY + ☃.getEyeHeight(), ☃.posZ), 0);
      float ☃xx = ☃x & 65535;
      float ☃xxx = ☃x >> 16;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, ☃xx, ☃xxx);
   }

   private void rotateArm(float var1) {
      EntityPlayerSP ☃ = this.mc.player;
      float ☃x = ☃.prevRenderArmPitch + (☃.renderArmPitch - ☃.prevRenderArmPitch) * ☃;
      float ☃xx = ☃.prevRenderArmYaw + (☃.renderArmYaw - ☃.prevRenderArmYaw) * ☃;
      GlStateManager.rotate((☃.rotationPitch - ☃x) * 0.1F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate((☃.rotationYaw - ☃xx) * 0.1F, 0.0F, 1.0F, 0.0F);
   }

   private float getMapAngleFromPitch(float var1) {
      float ☃ = 1.0F - ☃ / 45.0F + 0.1F;
      ☃ = MathHelper.clamp(☃, 0.0F, 1.0F);
      return -MathHelper.cos(☃ * (float) Math.PI) * 0.5F + 0.5F;
   }

   private void renderArms() {
      if (!this.mc.player.isInvisible()) {
         GlStateManager.disableCull();
         GlStateManager.pushMatrix();
         GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
         this.renderArm(EnumHandSide.RIGHT);
         this.renderArm(EnumHandSide.LEFT);
         GlStateManager.popMatrix();
         GlStateManager.enableCull();
      }
   }

   private void renderArm(EnumHandSide var1) {
      this.mc.getTextureManager().bindTexture(this.mc.player.getLocationSkin());
      Render<AbstractClientPlayer> ☃ = this.renderManager.getEntityRenderObject(this.mc.player);
      RenderPlayer ☃x = (RenderPlayer)☃;
      GlStateManager.pushMatrix();
      float ☃xx = ☃ == EnumHandSide.RIGHT ? 1.0F : -1.0F;
      GlStateManager.rotate(92.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(☃xx * -41.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.translate(☃xx * 0.3F, -1.1F, 0.45F);
      if (☃ == EnumHandSide.RIGHT) {
         ☃x.renderRightArm(this.mc.player);
      } else {
         ☃x.renderLeftArm(this.mc.player);
      }

      GlStateManager.popMatrix();
   }

   private void renderMapFirstPersonSide(float var1, EnumHandSide var2, float var3, ItemStack var4) {
      float ☃ = ☃ == EnumHandSide.RIGHT ? 1.0F : -1.0F;
      GlStateManager.translate(☃ * 0.125F, -0.125F, 0.0F);
      if (!this.mc.player.isInvisible()) {
         GlStateManager.pushMatrix();
         GlStateManager.rotate(☃ * 10.0F, 0.0F, 0.0F, 1.0F);
         this.renderArmFirstPerson(☃, ☃, ☃);
         GlStateManager.popMatrix();
      }

      GlStateManager.pushMatrix();
      GlStateManager.translate(☃ * 0.51F, -0.08F + ☃ * -1.2F, -0.75F);
      float ☃x = MathHelper.sqrt(☃);
      float ☃xx = MathHelper.sin(☃x * (float) Math.PI);
      float ☃xxx = -0.5F * ☃xx;
      float ☃xxxx = 0.4F * MathHelper.sin(☃x * (float) (Math.PI * 2));
      float ☃xxxxx = -0.3F * MathHelper.sin(☃ * (float) Math.PI);
      GlStateManager.translate(☃ * ☃xxx, ☃xxxx - 0.3F * ☃xx, ☃xxxxx);
      GlStateManager.rotate(☃xx * -45.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(☃ * ☃xx * -30.0F, 0.0F, 1.0F, 0.0F);
      this.renderMapFirstPerson(☃);
      GlStateManager.popMatrix();
   }

   private void renderMapFirstPerson(float var1, float var2, float var3) {
      float ☃ = MathHelper.sqrt(☃);
      float ☃x = -0.2F * MathHelper.sin(☃ * (float) Math.PI);
      float ☃xx = -0.4F * MathHelper.sin(☃ * (float) Math.PI);
      GlStateManager.translate(0.0F, -☃x / 2.0F, ☃xx);
      float ☃xxx = this.getMapAngleFromPitch(☃);
      GlStateManager.translate(0.0F, 0.04F + ☃ * -1.2F + ☃xxx * -0.5F, -0.72F);
      GlStateManager.rotate(☃xxx * -85.0F, 1.0F, 0.0F, 0.0F);
      this.renderArms();
      float ☃xxxx = MathHelper.sin(☃ * (float) Math.PI);
      GlStateManager.rotate(☃xxxx * 20.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.scale(2.0F, 2.0F, 2.0F);
      this.renderMapFirstPerson(this.itemStackMainHand);
   }

   private void renderMapFirstPerson(ItemStack var1) {
      GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.scale(0.38F, 0.38F, 0.38F);
      GlStateManager.disableLighting();
      this.mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      GlStateManager.translate(-0.5F, -0.5F, 0.0F);
      GlStateManager.scale(0.0078125F, 0.0078125F, 0.0078125F);
      ☃x.begin(7, DefaultVertexFormats.POSITION_TEX);
      ☃x.pos(-7.0, 135.0, 0.0).tex(0.0, 1.0).endVertex();
      ☃x.pos(135.0, 135.0, 0.0).tex(1.0, 1.0).endVertex();
      ☃x.pos(135.0, -7.0, 0.0).tex(1.0, 0.0).endVertex();
      ☃x.pos(-7.0, -7.0, 0.0).tex(0.0, 0.0).endVertex();
      ☃.draw();
      MapData ☃xx = Items.FILLED_MAP.getMapData(☃, this.mc.world);
      if (☃xx != null) {
         this.mc.entityRenderer.getMapItemRenderer().renderMap(☃xx, false);
      }

      GlStateManager.enableLighting();
   }

   private void renderArmFirstPerson(float var1, float var2, EnumHandSide var3) {
      boolean ☃ = ☃ != EnumHandSide.LEFT;
      float ☃x = ☃ ? 1.0F : -1.0F;
      float ☃xx = MathHelper.sqrt(☃);
      float ☃xxx = -0.3F * MathHelper.sin(☃xx * (float) Math.PI);
      float ☃xxxx = 0.4F * MathHelper.sin(☃xx * (float) (Math.PI * 2));
      float ☃xxxxx = -0.4F * MathHelper.sin(☃ * (float) Math.PI);
      GlStateManager.translate(☃x * (☃xxx + 0.64000005F), ☃xxxx + -0.6F + ☃ * -0.6F, ☃xxxxx + -0.71999997F);
      GlStateManager.rotate(☃x * 45.0F, 0.0F, 1.0F, 0.0F);
      float ☃xxxxxx = MathHelper.sin(☃ * ☃ * (float) Math.PI);
      float ☃xxxxxxx = MathHelper.sin(☃xx * (float) Math.PI);
      GlStateManager.rotate(☃x * ☃xxxxxxx * 70.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(☃x * ☃xxxxxx * -20.0F, 0.0F, 0.0F, 1.0F);
      AbstractClientPlayer ☃xxxxxxxx = this.mc.player;
      this.mc.getTextureManager().bindTexture(☃xxxxxxxx.getLocationSkin());
      GlStateManager.translate(☃x * -1.0F, 3.6F, 3.5F);
      GlStateManager.rotate(☃x * 120.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate(200.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(☃x * -135.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.translate(☃x * 5.6F, 0.0F, 0.0F);
      RenderPlayer ☃xxxxxxxxx = (RenderPlayer)this.renderManager.<AbstractClientPlayer>getEntityRenderObject(☃xxxxxxxx);
      GlStateManager.disableCull();
      if (☃) {
         ☃xxxxxxxxx.renderRightArm(☃xxxxxxxx);
      } else {
         ☃xxxxxxxxx.renderLeftArm(☃xxxxxxxx);
      }

      GlStateManager.enableCull();
   }

   private void transformEatFirstPerson(float var1, EnumHandSide var2, ItemStack var3) {
      float ☃ = this.mc.player.getItemInUseCount() - ☃ + 1.0F;
      float ☃x = ☃ / ☃.getMaxItemUseDuration();
      if (☃x < 0.8F) {
         float ☃xx = MathHelper.abs(MathHelper.cos(☃ / 4.0F * (float) Math.PI) * 0.1F);
         GlStateManager.translate(0.0F, ☃xx, 0.0F);
      }

      float ☃xx = 1.0F - (float)Math.pow(☃x, 27.0);
      int ☃xxx = ☃ == EnumHandSide.RIGHT ? 1 : -1;
      GlStateManager.translate(☃xx * 0.6F * ☃xxx, ☃xx * -0.5F, ☃xx * 0.0F);
      GlStateManager.rotate(☃xxx * ☃xx * 90.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(☃xx * 10.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(☃xxx * ☃xx * 30.0F, 0.0F, 0.0F, 1.0F);
   }

   private void transformFirstPerson(EnumHandSide var1, float var2) {
      int ☃ = ☃ == EnumHandSide.RIGHT ? 1 : -1;
      float ☃x = MathHelper.sin(☃ * ☃ * (float) Math.PI);
      GlStateManager.rotate(☃ * (45.0F + ☃x * -20.0F), 0.0F, 1.0F, 0.0F);
      float ☃xx = MathHelper.sin(MathHelper.sqrt(☃) * (float) Math.PI);
      GlStateManager.rotate(☃ * ☃xx * -20.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate(☃xx * -80.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(☃ * -45.0F, 0.0F, 1.0F, 0.0F);
   }

   private void transformSideFirstPerson(EnumHandSide var1, float var2) {
      int ☃ = ☃ == EnumHandSide.RIGHT ? 1 : -1;
      GlStateManager.translate(☃ * 0.56F, -0.52F + ☃ * -0.6F, -0.72F);
   }

   public void renderItemInFirstPerson(float var1) {
      AbstractClientPlayer ☃ = this.mc.player;
      float ☃x = ☃.getSwingProgress(☃);
      EnumHand ☃xx = (EnumHand)MoreObjects.firstNonNull(☃.swingingHand, EnumHand.MAIN_HAND);
      float ☃xxx = ☃.prevRotationPitch + (☃.rotationPitch - ☃.prevRotationPitch) * ☃;
      float ☃xxxx = ☃.prevRotationYaw + (☃.rotationYaw - ☃.prevRotationYaw) * ☃;
      boolean ☃xxxxx = true;
      boolean ☃xxxxxx = true;
      if (☃.isHandActive()) {
         ItemStack ☃xxxxxxx = ☃.getActiveItemStack();
         if (☃xxxxxxx.getItem() == Items.BOW) {
            EnumHand ☃xxxxxxxx = ☃.getActiveHand();
            ☃xxxxx = ☃xxxxxxxx == EnumHand.MAIN_HAND;
            ☃xxxxxx = !☃xxxxx;
         }
      }

      this.rotateArroundXAndY(☃xxx, ☃xxxx);
      this.setLightmap();
      this.rotateArm(☃);
      GlStateManager.enableRescaleNormal();
      if (☃xxxxx) {
         float ☃xxxxxxx = ☃xx == EnumHand.MAIN_HAND ? ☃x : 0.0F;
         float ☃xxxxxxxx = 1.0F - (this.prevEquippedProgressMainHand + (this.equippedProgressMainHand - this.prevEquippedProgressMainHand) * ☃);
         this.renderItemInFirstPerson(☃, ☃, ☃xxx, EnumHand.MAIN_HAND, ☃xxxxxxx, this.itemStackMainHand, ☃xxxxxxxx);
      }

      if (☃xxxxxx) {
         float ☃xxxxxxx = ☃xx == EnumHand.OFF_HAND ? ☃x : 0.0F;
         float ☃xxxxxxxx = 1.0F - (this.prevEquippedProgressOffHand + (this.equippedProgressOffHand - this.prevEquippedProgressOffHand) * ☃);
         this.renderItemInFirstPerson(☃, ☃, ☃xxx, EnumHand.OFF_HAND, ☃xxxxxxx, this.itemStackOffHand, ☃xxxxxxxx);
      }

      GlStateManager.disableRescaleNormal();
      RenderHelper.disableStandardItemLighting();
   }

   public void renderItemInFirstPerson(AbstractClientPlayer var1, float var2, float var3, EnumHand var4, float var5, ItemStack var6, float var7) {
      boolean ☃ = ☃ == EnumHand.MAIN_HAND;
      EnumHandSide ☃x = ☃ ? ☃.getPrimaryHand() : ☃.getPrimaryHand().opposite();
      GlStateManager.pushMatrix();
      if (☃.isEmpty()) {
         if (☃ && !☃.isInvisible()) {
            this.renderArmFirstPerson(☃, ☃, ☃x);
         }
      } else if (☃.getItem() == Items.FILLED_MAP) {
         if (☃ && this.itemStackOffHand.isEmpty()) {
            this.renderMapFirstPerson(☃, ☃, ☃);
         } else {
            this.renderMapFirstPersonSide(☃, ☃x, ☃, ☃);
         }
      } else {
         boolean ☃xx = ☃x == EnumHandSide.RIGHT;
         if (☃.isHandActive() && ☃.getItemInUseCount() > 0 && ☃.getActiveHand() == ☃) {
            int ☃xxx = ☃xx ? 1 : -1;
            switch (☃.getItemUseAction()) {
               case NONE:
                  this.transformSideFirstPerson(☃x, ☃);
                  break;
               case EAT:
               case DRINK:
                  this.transformEatFirstPerson(☃, ☃x, ☃);
                  this.transformSideFirstPerson(☃x, ☃);
                  break;
               case BLOCK:
                  this.transformSideFirstPerson(☃x, ☃);
                  break;
               case BOW:
                  this.transformSideFirstPerson(☃x, ☃);
                  GlStateManager.translate(☃xxx * -0.2785682F, 0.18344387F, 0.15731531F);
                  GlStateManager.rotate(-13.935F, 1.0F, 0.0F, 0.0F);
                  GlStateManager.rotate(☃xxx * 35.3F, 0.0F, 1.0F, 0.0F);
                  GlStateManager.rotate(☃xxx * -9.785F, 0.0F, 0.0F, 1.0F);
                  float ☃xxxx = ☃.getMaxItemUseDuration() - (this.mc.player.getItemInUseCount() - ☃ + 1.0F);
                  float ☃xxxxx = ☃xxxx / 20.0F;
                  ☃xxxxx = (☃xxxxx * ☃xxxxx + ☃xxxxx * 2.0F) / 3.0F;
                  if (☃xxxxx > 1.0F) {
                     ☃xxxxx = 1.0F;
                  }

                  if (☃xxxxx > 0.1F) {
                     float ☃xxxxxx = MathHelper.sin((☃xxxx - 0.1F) * 1.3F);
                     float ☃xxxxxxx = ☃xxxxx - 0.1F;
                     float ☃xxxxxxxx = ☃xxxxxx * ☃xxxxxxx;
                     GlStateManager.translate(☃xxxxxxxx * 0.0F, ☃xxxxxxxx * 0.004F, ☃xxxxxxxx * 0.0F);
                  }

                  GlStateManager.translate(☃xxxxx * 0.0F, ☃xxxxx * 0.0F, ☃xxxxx * 0.04F);
                  GlStateManager.scale(1.0F, 1.0F, 1.0F + ☃xxxxx * 0.2F);
                  GlStateManager.rotate(☃xxx * 45.0F, 0.0F, -1.0F, 0.0F);
            }
         } else {
            float ☃xxx = -0.4F * MathHelper.sin(MathHelper.sqrt(☃) * (float) Math.PI);
            float ☃xxxxxx = 0.2F * MathHelper.sin(MathHelper.sqrt(☃) * (float) (Math.PI * 2));
            float ☃xxxxxxx = -0.2F * MathHelper.sin(☃ * (float) Math.PI);
            int ☃xxxxxxxx = ☃xx ? 1 : -1;
            GlStateManager.translate(☃xxxxxxxx * ☃xxx, ☃xxxxxx, ☃xxxxxxx);
            this.transformSideFirstPerson(☃x, ☃);
            this.transformFirstPerson(☃x, ☃);
         }

         this.renderItemSide(
            ☃, ☃, ☃xx ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !☃xx
         );
      }

      GlStateManager.popMatrix();
   }

   public void renderOverlays(float var1) {
      GlStateManager.disableAlpha();
      if (this.mc.player.isEntityInsideOpaqueBlock()) {
         IBlockState ☃ = this.mc.world.getBlockState(new BlockPos(this.mc.player));
         EntityPlayer ☃x = this.mc.player;

         for (int ☃xx = 0; ☃xx < 8; ☃xx++) {
            double ☃xxx = ☃x.posX + ((☃xx >> 0) % 2 - 0.5F) * ☃x.width * 0.8F;
            double ☃xxxx = ☃x.posY + ((☃xx >> 1) % 2 - 0.5F) * 0.1F;
            double ☃xxxxx = ☃x.posZ + ((☃xx >> 2) % 2 - 0.5F) * ☃x.width * 0.8F;
            BlockPos ☃xxxxxx = new BlockPos(☃xxx, ☃xxxx + ☃x.getEyeHeight(), ☃xxxxx);
            IBlockState ☃xxxxxxx = this.mc.world.getBlockState(☃xxxxxx);
            if (☃xxxxxxx.causesSuffocation()) {
               ☃ = ☃xxxxxxx;
            }
         }

         if (☃.getRenderType() != EnumBlockRenderType.INVISIBLE) {
            this.renderSuffocationOverlay(this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(☃));
         }
      }

      if (!this.mc.player.isSpectator()) {
         if (this.mc.player.isInsideOfMaterial(Material.WATER)) {
            this.renderWaterOverlayTexture(☃);
         }

         if (this.mc.player.isBurning()) {
            this.renderFireInFirstPerson();
         }
      }

      GlStateManager.enableAlpha();
   }

   private void renderSuffocationOverlay(TextureAtlasSprite var1) {
      this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      float ☃xx = 0.1F;
      GlStateManager.color(0.1F, 0.1F, 0.1F, 0.5F);
      GlStateManager.pushMatrix();
      float ☃xxx = -1.0F;
      float ☃xxxx = 1.0F;
      float ☃xxxxx = -1.0F;
      float ☃xxxxxx = 1.0F;
      float ☃xxxxxxx = -0.5F;
      float ☃xxxxxxxx = ☃.getMinU();
      float ☃xxxxxxxxx = ☃.getMaxU();
      float ☃xxxxxxxxxx = ☃.getMinV();
      float ☃xxxxxxxxxxx = ☃.getMaxV();
      ☃x.begin(7, DefaultVertexFormats.POSITION_TEX);
      ☃x.pos(-1.0, -1.0, -0.5).tex(☃xxxxxxxxx, ☃xxxxxxxxxxx).endVertex();
      ☃x.pos(1.0, -1.0, -0.5).tex(☃xxxxxxxx, ☃xxxxxxxxxxx).endVertex();
      ☃x.pos(1.0, 1.0, -0.5).tex(☃xxxxxxxx, ☃xxxxxxxxxx).endVertex();
      ☃x.pos(-1.0, 1.0, -0.5).tex(☃xxxxxxxxx, ☃xxxxxxxxxx).endVertex();
      ☃.draw();
      GlStateManager.popMatrix();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private void renderWaterOverlayTexture(float var1) {
      this.mc.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      float ☃xx = this.mc.player.getBrightness();
      GlStateManager.color(☃xx, ☃xx, ☃xx, 0.5F);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      GlStateManager.pushMatrix();
      float ☃xxx = 4.0F;
      float ☃xxxx = -1.0F;
      float ☃xxxxx = 1.0F;
      float ☃xxxxxx = -1.0F;
      float ☃xxxxxxx = 1.0F;
      float ☃xxxxxxxx = -0.5F;
      float ☃xxxxxxxxx = -this.mc.player.rotationYaw / 64.0F;
      float ☃xxxxxxxxxx = this.mc.player.rotationPitch / 64.0F;
      ☃x.begin(7, DefaultVertexFormats.POSITION_TEX);
      ☃x.pos(-1.0, -1.0, -0.5).tex(4.0F + ☃xxxxxxxxx, 4.0F + ☃xxxxxxxxxx).endVertex();
      ☃x.pos(1.0, -1.0, -0.5).tex(0.0F + ☃xxxxxxxxx, 4.0F + ☃xxxxxxxxxx).endVertex();
      ☃x.pos(1.0, 1.0, -0.5).tex(0.0F + ☃xxxxxxxxx, 0.0F + ☃xxxxxxxxxx).endVertex();
      ☃x.pos(-1.0, 1.0, -0.5).tex(4.0F + ☃xxxxxxxxx, 0.0F + ☃xxxxxxxxxx).endVertex();
      ☃.draw();
      GlStateManager.popMatrix();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableBlend();
   }

   private void renderFireInFirstPerson() {
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 0.9F);
      GlStateManager.depthFunc(519);
      GlStateManager.depthMask(false);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      float ☃xx = 1.0F;

      for (int ☃xxx = 0; ☃xxx < 2; ☃xxx++) {
         GlStateManager.pushMatrix();
         TextureAtlasSprite ☃xxxx = this.mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/fire_layer_1");
         this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
         float ☃xxxxx = ☃xxxx.getMinU();
         float ☃xxxxxx = ☃xxxx.getMaxU();
         float ☃xxxxxxx = ☃xxxx.getMinV();
         float ☃xxxxxxxx = ☃xxxx.getMaxV();
         float ☃xxxxxxxxx = -0.5F;
         float ☃xxxxxxxxxx = 0.5F;
         float ☃xxxxxxxxxxx = -0.5F;
         float ☃xxxxxxxxxxxx = 0.5F;
         float ☃xxxxxxxxxxxxx = -0.5F;
         GlStateManager.translate(-(☃xxx * 2 - 1) * 0.24F, -0.3F, 0.0F);
         GlStateManager.rotate((☃xxx * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
         ☃x.begin(7, DefaultVertexFormats.POSITION_TEX);
         ☃x.pos(-0.5, -0.5, -0.5).tex(☃xxxxxx, ☃xxxxxxxx).endVertex();
         ☃x.pos(0.5, -0.5, -0.5).tex(☃xxxxx, ☃xxxxxxxx).endVertex();
         ☃x.pos(0.5, 0.5, -0.5).tex(☃xxxxx, ☃xxxxxxx).endVertex();
         ☃x.pos(-0.5, 0.5, -0.5).tex(☃xxxxxx, ☃xxxxxxx).endVertex();
         ☃.draw();
         GlStateManager.popMatrix();
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableBlend();
      GlStateManager.depthMask(true);
      GlStateManager.depthFunc(515);
   }

   public void updateEquippedItem() {
      this.prevEquippedProgressMainHand = this.equippedProgressMainHand;
      this.prevEquippedProgressOffHand = this.equippedProgressOffHand;
      EntityPlayerSP ☃ = this.mc.player;
      ItemStack ☃x = ☃.getHeldItemMainhand();
      ItemStack ☃xx = ☃.getHeldItemOffhand();
      if (☃.isRowingBoat()) {
         this.equippedProgressMainHand = MathHelper.clamp(this.equippedProgressMainHand - 0.4F, 0.0F, 1.0F);
         this.equippedProgressOffHand = MathHelper.clamp(this.equippedProgressOffHand - 0.4F, 0.0F, 1.0F);
      } else {
         float ☃xxx = ☃.getCooledAttackStrength(1.0F);
         this.equippedProgressMainHand = this.equippedProgressMainHand
            + MathHelper.clamp((Objects.equals(this.itemStackMainHand, ☃x) ? ☃xxx * ☃xxx * ☃xxx : 0.0F) - this.equippedProgressMainHand, -0.4F, 0.4F);
         this.equippedProgressOffHand = this.equippedProgressOffHand
            + MathHelper.clamp((Objects.equals(this.itemStackOffHand, ☃xx) ? 1 : 0) - this.equippedProgressOffHand, -0.4F, 0.4F);
      }

      if (this.equippedProgressMainHand < 0.1F) {
         this.itemStackMainHand = ☃x;
      }

      if (this.equippedProgressOffHand < 0.1F) {
         this.itemStackOffHand = ☃xx;
      }
   }

   public void resetEquippedProgress(EnumHand var1) {
      if (☃ == EnumHand.MAIN_HAND) {
         this.equippedProgressMainHand = 0.0F;
      } else {
         this.equippedProgressOffHand = 0.0F;
      }
   }
}
