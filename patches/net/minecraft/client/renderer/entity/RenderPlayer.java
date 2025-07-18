package net.minecraft.client.renderer.entity;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RenderPlayer extends RenderLivingBase<AbstractClientPlayer> {
   private final boolean smallArms;

   public RenderPlayer(RenderManager var1) {
      this(☃, false);
   }

   public RenderPlayer(RenderManager var1, boolean var2) {
      super(☃, new ModelPlayer(0.0F, ☃), 0.5F);
      this.smallArms = ☃;
      this.addLayer(new LayerBipedArmor(this));
      this.addLayer(new LayerHeldItem(this));
      this.addLayer(new LayerArrow(this));
      this.addLayer(new LayerDeadmau5Head(this));
      this.addLayer(new LayerCape(this));
      this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
      this.addLayer(new LayerElytra(this));
      this.addLayer(new LayerEntityOnShoulder(☃));
   }

   public ModelPlayer getMainModel() {
      return (ModelPlayer)super.getMainModel();
   }

   public void doRender(AbstractClientPlayer var1, double var2, double var4, double var6, float var8, float var9) {
      if (!☃.isUser() || this.renderManager.renderViewEntity == ☃) {
         double ☃ = ☃;
         if (☃.isSneaking()) {
            ☃ = ☃ - 0.125;
         }

         this.setModelVisibilities(☃);
         GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
         super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
         GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
      }
   }

   private void setModelVisibilities(AbstractClientPlayer var1) {
      ModelPlayer ☃ = this.getMainModel();
      if (☃.isSpectator()) {
         ☃.setVisible(false);
         ☃.bipedHead.showModel = true;
         ☃.bipedHeadwear.showModel = true;
      } else {
         ItemStack ☃x = ☃.getHeldItemMainhand();
         ItemStack ☃xx = ☃.getHeldItemOffhand();
         ☃.setVisible(true);
         ☃.bipedHeadwear.showModel = ☃.isWearing(EnumPlayerModelParts.HAT);
         ☃.bipedBodyWear.showModel = ☃.isWearing(EnumPlayerModelParts.JACKET);
         ☃.bipedLeftLegwear.showModel = ☃.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
         ☃.bipedRightLegwear.showModel = ☃.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
         ☃.bipedLeftArmwear.showModel = ☃.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
         ☃.bipedRightArmwear.showModel = ☃.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
         ☃.isSneak = ☃.isSneaking();
         ModelBiped.ArmPose ☃xxx = ModelBiped.ArmPose.EMPTY;
         ModelBiped.ArmPose ☃xxxx = ModelBiped.ArmPose.EMPTY;
         if (!☃x.isEmpty()) {
            ☃xxx = ModelBiped.ArmPose.ITEM;
            if (☃.getItemInUseCount() > 0) {
               EnumAction ☃xxxxx = ☃x.getItemUseAction();
               if (☃xxxxx == EnumAction.BLOCK) {
                  ☃xxx = ModelBiped.ArmPose.BLOCK;
               } else if (☃xxxxx == EnumAction.BOW) {
                  ☃xxx = ModelBiped.ArmPose.BOW_AND_ARROW;
               }
            }
         }

         if (!☃xx.isEmpty()) {
            ☃xxxx = ModelBiped.ArmPose.ITEM;
            if (☃.getItemInUseCount() > 0) {
               EnumAction ☃xxxxx = ☃xx.getItemUseAction();
               if (☃xxxxx == EnumAction.BLOCK) {
                  ☃xxxx = ModelBiped.ArmPose.BLOCK;
               }
            }
         }

         if (☃.getPrimaryHand() == EnumHandSide.RIGHT) {
            ☃.rightArmPose = ☃xxx;
            ☃.leftArmPose = ☃xxxx;
         } else {
            ☃.rightArmPose = ☃xxxx;
            ☃.leftArmPose = ☃xxx;
         }
      }
   }

   public ResourceLocation getEntityTexture(AbstractClientPlayer var1) {
      return ☃.getLocationSkin();
   }

   @Override
   public void transformHeldFull3DItemLayer() {
      GlStateManager.translate(0.0F, 0.1875F, 0.0F);
   }

   protected void preRenderCallback(AbstractClientPlayer var1, float var2) {
      float ☃ = 0.9375F;
      GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
   }

   protected void renderEntityName(AbstractClientPlayer var1, double var2, double var4, double var6, String var8, double var9) {
      if (☃ < 100.0) {
         Scoreboard ☃ = ☃.getWorldScoreboard();
         ScoreObjective ☃x = ☃.getObjectiveInDisplaySlot(2);
         if (☃x != null) {
            Score ☃xx = ☃.getOrCreateScore(☃.getName(), ☃x);
            this.renderLivingLabel(☃, ☃xx.getScorePoints() + " " + ☃x.getDisplayName(), ☃, ☃, ☃, 64);
            ☃ += this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * 0.025F;
         }
      }

      super.renderEntityName(☃, ☃, ☃, ☃, ☃, ☃);
   }

   public void renderRightArm(AbstractClientPlayer var1) {
      float ☃ = 1.0F;
      GlStateManager.color(1.0F, 1.0F, 1.0F);
      float ☃x = 0.0625F;
      ModelPlayer ☃xx = this.getMainModel();
      this.setModelVisibilities(☃);
      GlStateManager.enableBlend();
      ☃xx.swingProgress = 0.0F;
      ☃xx.isSneak = false;
      ☃xx.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, ☃);
      ☃xx.bipedRightArm.rotateAngleX = 0.0F;
      ☃xx.bipedRightArm.render(0.0625F);
      ☃xx.bipedRightArmwear.rotateAngleX = 0.0F;
      ☃xx.bipedRightArmwear.render(0.0625F);
      GlStateManager.disableBlend();
   }

   public void renderLeftArm(AbstractClientPlayer var1) {
      float ☃ = 1.0F;
      GlStateManager.color(1.0F, 1.0F, 1.0F);
      float ☃x = 0.0625F;
      ModelPlayer ☃xx = this.getMainModel();
      this.setModelVisibilities(☃);
      GlStateManager.enableBlend();
      ☃xx.isSneak = false;
      ☃xx.swingProgress = 0.0F;
      ☃xx.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, ☃);
      ☃xx.bipedLeftArm.rotateAngleX = 0.0F;
      ☃xx.bipedLeftArm.render(0.0625F);
      ☃xx.bipedLeftArmwear.rotateAngleX = 0.0F;
      ☃xx.bipedLeftArmwear.render(0.0625F);
      GlStateManager.disableBlend();
   }

   protected void renderLivingAt(AbstractClientPlayer var1, double var2, double var4, double var6) {
      if (☃.isEntityAlive() && ☃.isPlayerSleeping()) {
         super.renderLivingAt(☃, ☃ + ☃.renderOffsetX, ☃ + ☃.renderOffsetY, ☃ + ☃.renderOffsetZ);
      } else {
         super.renderLivingAt(☃, ☃, ☃, ☃);
      }
   }

   protected void applyRotations(AbstractClientPlayer var1, float var2, float var3, float var4) {
      if (☃.isEntityAlive() && ☃.isPlayerSleeping()) {
         GlStateManager.rotate(☃.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(this.getDeathMaxRotation(☃), 0.0F, 0.0F, 1.0F);
         GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
      } else if (☃.isElytraFlying()) {
         super.applyRotations(☃, ☃, ☃, ☃);
         float ☃ = ☃.getTicksElytraFlying() + ☃;
         float ☃x = MathHelper.clamp(☃ * ☃ / 100.0F, 0.0F, 1.0F);
         GlStateManager.rotate(☃x * (-90.0F - ☃.rotationPitch), 1.0F, 0.0F, 0.0F);
         Vec3d ☃xx = ☃.getLook(☃);
         double ☃xxx = ☃.motionX * ☃.motionX + ☃.motionZ * ☃.motionZ;
         double ☃xxxx = ☃xx.x * ☃xx.x + ☃xx.z * ☃xx.z;
         if (☃xxx > 0.0 && ☃xxxx > 0.0) {
            double ☃xxxxx = (☃.motionX * ☃xx.x + ☃.motionZ * ☃xx.z) / (Math.sqrt(☃xxx) * Math.sqrt(☃xxxx));
            double ☃xxxxxx = ☃.motionX * ☃xx.z - ☃.motionZ * ☃xx.x;
            GlStateManager.rotate((float)(Math.signum(☃xxxxxx) * Math.acos(☃xxxxx)) * 180.0F / (float) Math.PI, 0.0F, 1.0F, 0.0F);
         }
      } else {
         super.applyRotations(☃, ☃, ☃, ☃);
      }
   }
}
