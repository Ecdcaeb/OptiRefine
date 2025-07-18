package net.minecraft.client.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class EntityOtherPlayerMP extends AbstractClientPlayer {
   private int otherPlayerMPPosRotationIncrements;
   private double otherPlayerMPX;
   private double otherPlayerMPY;
   private double otherPlayerMPZ;
   private double otherPlayerMPYaw;
   private double otherPlayerMPPitch;

   public EntityOtherPlayerMP(World var1, GameProfile var2) {
      super(☃, ☃);
      this.stepHeight = 1.0F;
      this.noClip = true;
      this.renderOffsetY = 0.25F;
   }

   @Override
   public boolean isInRangeToRenderDist(double var1) {
      double ☃ = this.getEntityBoundingBox().getAverageEdgeLength() * 10.0;
      if (Double.isNaN(☃)) {
         ☃ = 1.0;
      }

      ☃ *= 64.0 * getRenderDistanceWeight();
      return ☃ < ☃ * ☃;
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      return true;
   }

   @Override
   public void setPositionAndRotationDirect(double var1, double var3, double var5, float var7, float var8, int var9, boolean var10) {
      this.otherPlayerMPX = ☃;
      this.otherPlayerMPY = ☃;
      this.otherPlayerMPZ = ☃;
      this.otherPlayerMPYaw = ☃;
      this.otherPlayerMPPitch = ☃;
      this.otherPlayerMPPosRotationIncrements = ☃;
   }

   @Override
   public void onUpdate() {
      this.renderOffsetY = 0.0F;
      super.onUpdate();
      this.prevLimbSwingAmount = this.limbSwingAmount;
      double ☃ = this.posX - this.prevPosX;
      double ☃x = this.posZ - this.prevPosZ;
      float ☃xx = MathHelper.sqrt(☃ * ☃ + ☃x * ☃x) * 4.0F;
      if (☃xx > 1.0F) {
         ☃xx = 1.0F;
      }

      this.limbSwingAmount = this.limbSwingAmount + (☃xx - this.limbSwingAmount) * 0.4F;
      this.limbSwing = this.limbSwing + this.limbSwingAmount;
   }

   @Override
   public void onLivingUpdate() {
      if (this.otherPlayerMPPosRotationIncrements > 0) {
         double ☃ = this.posX + (this.otherPlayerMPX - this.posX) / this.otherPlayerMPPosRotationIncrements;
         double ☃x = this.posY + (this.otherPlayerMPY - this.posY) / this.otherPlayerMPPosRotationIncrements;
         double ☃xx = this.posZ + (this.otherPlayerMPZ - this.posZ) / this.otherPlayerMPPosRotationIncrements;
         double ☃xxx = this.otherPlayerMPYaw - this.rotationYaw;

         while (☃xxx < -180.0) {
            ☃xxx += 360.0;
         }

         while (☃xxx >= 180.0) {
            ☃xxx -= 360.0;
         }

         this.rotationYaw = (float)(this.rotationYaw + ☃xxx / this.otherPlayerMPPosRotationIncrements);
         this.rotationPitch = (float)(this.rotationPitch + (this.otherPlayerMPPitch - this.rotationPitch) / this.otherPlayerMPPosRotationIncrements);
         this.otherPlayerMPPosRotationIncrements--;
         this.setPosition(☃, ☃x, ☃xx);
         this.setRotation(this.rotationYaw, this.rotationPitch);
      }

      this.prevCameraYaw = this.cameraYaw;
      this.updateArmSwingProgress();
      float ☃ = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
      float ☃x = (float)Math.atan(-this.motionY * 0.2F) * 15.0F;
      if (☃ > 0.1F) {
         ☃ = 0.1F;
      }

      if (!this.onGround || this.getHealth() <= 0.0F) {
         ☃ = 0.0F;
      }

      if (this.onGround || this.getHealth() <= 0.0F) {
         ☃x = 0.0F;
      }

      this.cameraYaw = this.cameraYaw + (☃ - this.cameraYaw) * 0.4F;
      this.cameraPitch = this.cameraPitch + (☃x - this.cameraPitch) * 0.8F;
      this.world.profiler.startSection("push");
      this.collideWithNearbyEntities();
      this.world.profiler.endSection();
   }

   @Override
   public void sendMessage(ITextComponent var1) {
      Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(☃);
   }

   @Override
   public boolean canUseCommand(int var1, String var2) {
      return false;
   }

   @Override
   public BlockPos getPosition() {
      return new BlockPos(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5);
   }
}
