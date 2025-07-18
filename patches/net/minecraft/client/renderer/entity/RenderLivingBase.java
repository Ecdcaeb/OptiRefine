package net.minecraft.client.renderer.entity;

import com.google.common.collect.Lists;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class RenderLivingBase<T extends EntityLivingBase> extends Render<T> {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final DynamicTexture TEXTURE_BRIGHTNESS = new DynamicTexture(16, 16);
   protected ModelBase mainModel;
   protected FloatBuffer brightnessBuffer = GLAllocation.createDirectFloatBuffer(4);
   protected List<LayerRenderer<T>> layerRenderers = Lists.newArrayList();
   protected boolean renderMarker;

   public RenderLivingBase(RenderManager var1, ModelBase var2, float var3) {
      super(☃);
      this.mainModel = ☃;
      this.shadowSize = ☃;
   }

   protected <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean addLayer(U var1) {
      return this.layerRenderers.add(☃);
   }

   public ModelBase getMainModel() {
      return this.mainModel;
   }

   protected float interpolateRotation(float var1, float var2, float var3) {
      float ☃ = ☃ - ☃;

      while (☃ < -180.0F) {
         ☃ += 360.0F;
      }

      while (☃ >= 180.0F) {
         ☃ -= 360.0F;
      }

      return ☃ + ☃ * ☃;
   }

   public void transformHeldFull3DItemLayer() {
   }

   public void doRender(T var1, double var2, double var4, double var6, float var8, float var9) {
      GlStateManager.pushMatrix();
      GlStateManager.disableCull();
      this.mainModel.swingProgress = this.getSwingProgress(☃, ☃);
      this.mainModel.isRiding = ☃.isRiding();
      this.mainModel.isChild = ☃.isChild();

      try {
         float ☃ = this.interpolateRotation(☃.prevRenderYawOffset, ☃.renderYawOffset, ☃);
         float ☃x = this.interpolateRotation(☃.prevRotationYawHead, ☃.rotationYawHead, ☃);
         float ☃xx = ☃x - ☃;
         if (☃.isRiding() && ☃.getRidingEntity() instanceof EntityLivingBase) {
            EntityLivingBase ☃xxx = (EntityLivingBase)☃.getRidingEntity();
            ☃ = this.interpolateRotation(☃xxx.prevRenderYawOffset, ☃xxx.renderYawOffset, ☃);
            ☃xx = ☃x - ☃;
            float ☃xxxx = MathHelper.wrapDegrees(☃xx);
            if (☃xxxx < -85.0F) {
               ☃xxxx = -85.0F;
            }

            if (☃xxxx >= 85.0F) {
               ☃xxxx = 85.0F;
            }

            ☃ = ☃x - ☃xxxx;
            if (☃xxxx * ☃xxxx > 2500.0F) {
               ☃ += ☃xxxx * 0.2F;
            }

            ☃xx = ☃x - ☃;
         }

         float ☃xxxxx = ☃.prevRotationPitch + (☃.rotationPitch - ☃.prevRotationPitch) * ☃;
         this.renderLivingAt(☃, ☃, ☃, ☃);
         float ☃xxxxxx = this.handleRotationFloat(☃, ☃);
         this.applyRotations(☃, ☃xxxxxx, ☃, ☃);
         float ☃xxxxxxx = this.prepareScale(☃, ☃);
         float ☃xxxxxxxx = 0.0F;
         float ☃xxxxxxxxx = 0.0F;
         if (!☃.isRiding()) {
            ☃xxxxxxxx = ☃.prevLimbSwingAmount + (☃.limbSwingAmount - ☃.prevLimbSwingAmount) * ☃;
            ☃xxxxxxxxx = ☃.limbSwing - ☃.limbSwingAmount * (1.0F - ☃);
            if (☃.isChild()) {
               ☃xxxxxxxxx *= 3.0F;
            }

            if (☃xxxxxxxx > 1.0F) {
               ☃xxxxxxxx = 1.0F;
            }
         }

         GlStateManager.enableAlpha();
         this.mainModel.setLivingAnimations(☃, ☃xxxxxxxxx, ☃xxxxxxxx, ☃);
         this.mainModel.setRotationAngles(☃xxxxxxxxx, ☃xxxxxxxx, ☃xxxxxx, ☃xx, ☃xxxxx, ☃xxxxxxx, ☃);
         if (this.renderOutlines) {
            boolean ☃xxxxxxxxxx = this.setScoreTeamColor(☃);
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(☃));
            if (!this.renderMarker) {
               this.renderModel(☃, ☃xxxxxxxxx, ☃xxxxxxxx, ☃xxxxxx, ☃xx, ☃xxxxx, ☃xxxxxxx);
            }

            if (!(☃ instanceof EntityPlayer) || !((EntityPlayer)☃).isSpectator()) {
               this.renderLayers(☃, ☃xxxxxxxxx, ☃xxxxxxxx, ☃, ☃xxxxxx, ☃xx, ☃xxxxx, ☃xxxxxxx);
            }

            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
            if (☃xxxxxxxxxx) {
               this.unsetScoreTeamColor();
            }
         } else {
            boolean ☃xxxxxxxxxxx = this.setDoRenderBrightness(☃, ☃);
            this.renderModel(☃, ☃xxxxxxxxx, ☃xxxxxxxx, ☃xxxxxx, ☃xx, ☃xxxxx, ☃xxxxxxx);
            if (☃xxxxxxxxxxx) {
               this.unsetBrightness();
            }

            GlStateManager.depthMask(true);
            if (!(☃ instanceof EntityPlayer) || !((EntityPlayer)☃).isSpectator()) {
               this.renderLayers(☃, ☃xxxxxxxxx, ☃xxxxxxxx, ☃, ☃xxxxxx, ☃xx, ☃xxxxx, ☃xxxxxxx);
            }
         }

         GlStateManager.disableRescaleNormal();
      } catch (Exception var19) {
         LOGGER.error("Couldn't render entity", var19);
      }

      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.enableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
      GlStateManager.enableCull();
      GlStateManager.popMatrix();
      super.doRender(☃, ☃, ☃, ☃, ☃, ☃);
   }

   public float prepareScale(T var1, float var2) {
      GlStateManager.enableRescaleNormal();
      GlStateManager.scale(-1.0F, -1.0F, 1.0F);
      this.preRenderCallback(☃, ☃);
      float ☃ = 0.0625F;
      GlStateManager.translate(0.0F, -1.501F, 0.0F);
      return 0.0625F;
   }

   protected boolean setScoreTeamColor(T var1) {
      GlStateManager.disableLighting();
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.disableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
      return true;
   }

   protected void unsetScoreTeamColor() {
      GlStateManager.enableLighting();
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.enableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
   }

   protected void renderModel(T var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      boolean ☃ = this.isVisible(☃);
      boolean ☃x = !☃ && !☃.isInvisibleToPlayer(Minecraft.getMinecraft().player);
      if (☃ || ☃x) {
         if (!this.bindEntityTexture(☃)) {
            return;
         }

         if (☃x) {
            GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
         }

         this.mainModel.render(☃, ☃, ☃, ☃, ☃, ☃, ☃);
         if (☃x) {
            GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
         }
      }
   }

   protected boolean isVisible(T var1) {
      return !☃.isInvisible() || this.renderOutlines;
   }

   protected boolean setDoRenderBrightness(T var1, float var2) {
      return this.setBrightness(☃, ☃, true);
   }

   protected boolean setBrightness(T var1, float var2, boolean var3) {
      float ☃ = ☃.getBrightness();
      int ☃x = this.getColorMultiplier(☃, ☃, ☃);
      boolean ☃xx = (☃x >> 24 & 0xFF) > 0;
      boolean ☃xxx = ☃.hurtTime > 0 || ☃.deathTime > 0;
      if (!☃xx && !☃xxx) {
         return false;
      } else if (!☃xx && !☃) {
         return false;
      } else {
         GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
         GlStateManager.enableTexture2D();
         GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
         GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
         GlStateManager.enableTexture2D();
         GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, OpenGlHelper.GL_INTERPOLATE);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_CONSTANT);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE2_RGB, OpenGlHelper.GL_CONSTANT);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND2_RGB, 770);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
         ((Buffer)this.brightnessBuffer).position(0);
         if (☃xxx) {
            this.brightnessBuffer.put(1.0F);
            this.brightnessBuffer.put(0.0F);
            this.brightnessBuffer.put(0.0F);
            this.brightnessBuffer.put(0.3F);
         } else {
            float ☃xxxx = (☃x >> 24 & 0xFF) / 255.0F;
            float ☃xxxxx = (☃x >> 16 & 0xFF) / 255.0F;
            float ☃xxxxxx = (☃x >> 8 & 0xFF) / 255.0F;
            float ☃xxxxxxx = (☃x & 0xFF) / 255.0F;
            this.brightnessBuffer.put(☃xxxxx);
            this.brightnessBuffer.put(☃xxxxxx);
            this.brightnessBuffer.put(☃xxxxxxx);
            this.brightnessBuffer.put(1.0F - ☃xxxx);
         }

         ((Buffer)this.brightnessBuffer).flip();
         GlStateManager.glTexEnv(8960, 8705, this.brightnessBuffer);
         GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
         GlStateManager.enableTexture2D();
         GlStateManager.bindTexture(TEXTURE_BRIGHTNESS.getGlTextureId());
         GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_PREVIOUS);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.lightmapTexUnit);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
         GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
         GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
         return true;
      }
   }

   protected void unsetBrightness() {
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
      GlStateManager.enableTexture2D();
      GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_ALPHA, OpenGlHelper.GL_PRIMARY_COLOR);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_ALPHA, 770);
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
      GlStateManager.disableTexture2D();
      GlStateManager.bindTexture(0);
      GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
      GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
   }

   protected void renderLivingAt(T var1, double var2, double var4, double var6) {
      GlStateManager.translate((float)☃, (float)☃, (float)☃);
   }

   protected void applyRotations(T var1, float var2, float var3, float var4) {
      GlStateManager.rotate(180.0F - ☃, 0.0F, 1.0F, 0.0F);
      if (☃.deathTime > 0) {
         float ☃ = (☃.deathTime + ☃ - 1.0F) / 20.0F * 1.6F;
         ☃ = MathHelper.sqrt(☃);
         if (☃ > 1.0F) {
            ☃ = 1.0F;
         }

         GlStateManager.rotate(☃ * this.getDeathMaxRotation(☃), 0.0F, 0.0F, 1.0F);
      } else {
         String ☃ = TextFormatting.getTextWithoutFormattingCodes(☃.getName());
         if (☃ != null
            && ("Dinnerbone".equals(☃) || "Grumm".equals(☃))
            && (!(☃ instanceof EntityPlayer) || ((EntityPlayer)☃).isWearing(EnumPlayerModelParts.CAPE))) {
            GlStateManager.translate(0.0F, ☃.height + 0.1F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
         }
      }
   }

   protected float getSwingProgress(T var1, float var2) {
      return ☃.getSwingProgress(☃);
   }

   protected float handleRotationFloat(T var1, float var2) {
      return ☃.ticksExisted + ☃;
   }

   protected void renderLayers(T var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      for (LayerRenderer<T> ☃ : this.layerRenderers) {
         boolean ☃x = this.setBrightness(☃, ☃, ☃.shouldCombineTextures());
         ☃.doRenderLayer(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
         if (☃x) {
            this.unsetBrightness();
         }
      }
   }

   protected float getDeathMaxRotation(T var1) {
      return 90.0F;
   }

   protected int getColorMultiplier(T var1, float var2, float var3) {
      return 0;
   }

   protected void preRenderCallback(T var1, float var2) {
   }

   public void renderName(T var1, double var2, double var4, double var6) {
      if (this.canRenderName(☃)) {
         double ☃ = ☃.getDistanceSq(this.renderManager.renderViewEntity);
         float ☃x = ☃.isSneaking() ? 32.0F : 64.0F;
         if (!(☃ >= ☃x * ☃x)) {
            String ☃xx = ☃.getDisplayName().getFormattedText();
            GlStateManager.alphaFunc(516, 0.1F);
            this.renderEntityName(☃, ☃, ☃, ☃, ☃xx, ☃);
         }
      }
   }

   protected boolean canRenderName(T var1) {
      EntityPlayerSP ☃ = Minecraft.getMinecraft().player;
      boolean ☃x = !☃.isInvisibleToPlayer(☃);
      if (☃ != ☃) {
         Team ☃xx = ☃.getTeam();
         Team ☃xxx = ☃.getTeam();
         if (☃xx != null) {
            Team.EnumVisible ☃xxxx = ☃xx.getNameTagVisibility();
            switch (☃xxxx) {
               case ALWAYS:
                  return ☃x;
               case NEVER:
                  return false;
               case HIDE_FOR_OTHER_TEAMS:
                  return ☃xxx == null ? ☃x : ☃xx.isSameTeam(☃xxx) && (☃xx.getSeeFriendlyInvisiblesEnabled() || ☃x);
               case HIDE_FOR_OWN_TEAM:
                  return ☃xxx == null ? ☃x : !☃xx.isSameTeam(☃xxx) && ☃x;
               default:
                  return true;
            }
         }
      }

      return Minecraft.isGuiEnabled() && ☃ != this.renderManager.renderViewEntity && ☃x && !☃.isBeingRidden();
   }

   static {
      int[] ☃ = TEXTURE_BRIGHTNESS.getTextureData();

      for (int ☃x = 0; ☃x < 256; ☃x++) {
         ☃[☃x] = -1;
      }

      TEXTURE_BRIGHTNESS.updateDynamicTexture();
   }
}
