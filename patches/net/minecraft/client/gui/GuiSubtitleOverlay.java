package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.ISoundEventListener;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class GuiSubtitleOverlay extends Gui implements ISoundEventListener {
   private final Minecraft client;
   private final List<GuiSubtitleOverlay.Subtitle> subtitles = Lists.newArrayList();
   private boolean enabled;

   public GuiSubtitleOverlay(Minecraft var1) {
      this.client = ☃;
   }

   public void renderSubtitles(ScaledResolution var1) {
      if (!this.enabled && this.client.gameSettings.showSubtitles) {
         this.client.getSoundHandler().addListener(this);
         this.enabled = true;
      } else if (this.enabled && !this.client.gameSettings.showSubtitles) {
         this.client.getSoundHandler().removeListener(this);
         this.enabled = false;
      }

      if (this.enabled && !this.subtitles.isEmpty()) {
         GlStateManager.pushMatrix();
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
         );
         Vec3d ☃ = new Vec3d(this.client.player.posX, this.client.player.posY + this.client.player.getEyeHeight(), this.client.player.posZ);
         Vec3d ☃x = new Vec3d(0.0, 0.0, -1.0)
            .rotatePitch(-this.client.player.rotationPitch * (float) (Math.PI / 180.0))
            .rotateYaw(-this.client.player.rotationYaw * (float) (Math.PI / 180.0));
         Vec3d ☃xx = new Vec3d(0.0, 1.0, 0.0)
            .rotatePitch(-this.client.player.rotationPitch * (float) (Math.PI / 180.0))
            .rotateYaw(-this.client.player.rotationYaw * (float) (Math.PI / 180.0));
         Vec3d ☃xxx = ☃x.crossProduct(☃xx);
         int ☃xxxx = 0;
         int ☃xxxxx = 0;
         Iterator<GuiSubtitleOverlay.Subtitle> ☃xxxxxx = this.subtitles.iterator();

         while (☃xxxxxx.hasNext()) {
            GuiSubtitleOverlay.Subtitle ☃xxxxxxx = ☃xxxxxx.next();
            if (☃xxxxxxx.getStartTime() + 3000L <= Minecraft.getSystemTime()) {
               ☃xxxxxx.remove();
            } else {
               ☃xxxxx = Math.max(☃xxxxx, this.client.fontRenderer.getStringWidth(☃xxxxxxx.getString()));
            }
         }

         ☃xxxxx += this.client.fontRenderer.getStringWidth("<")
            + this.client.fontRenderer.getStringWidth(" ")
            + this.client.fontRenderer.getStringWidth(">")
            + this.client.fontRenderer.getStringWidth(" ");

         for (GuiSubtitleOverlay.Subtitle ☃xxxxxxx : this.subtitles) {
            int ☃xxxxxxxx = 255;
            String ☃xxxxxxxxx = ☃xxxxxxx.getString();
            Vec3d ☃xxxxxxxxxx = ☃xxxxxxx.getLocation().subtract(☃).normalize();
            double ☃xxxxxxxxxxx = -☃xxx.dotProduct(☃xxxxxxxxxx);
            double ☃xxxxxxxxxxxx = -☃x.dotProduct(☃xxxxxxxxxx);
            boolean ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxxx > 0.5;
            int ☃xxxxxxxxxxxxxx = ☃xxxxx / 2;
            int ☃xxxxxxxxxxxxxxx = this.client.fontRenderer.FONT_HEIGHT;
            int ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx / 2;
            float ☃xxxxxxxxxxxxxxxxx = 1.0F;
            int ☃xxxxxxxxxxxxxxxxxx = this.client.fontRenderer.getStringWidth(☃xxxxxxxxx);
            int ☃xxxxxxxxxxxxxxxxxxx = MathHelper.floor(
               MathHelper.clampedLerp(255.0, 75.0, (float)(Minecraft.getSystemTime() - ☃xxxxxxx.getStartTime()) / 3000.0F)
            );
            int ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxxxxxx << 16 | ☃xxxxxxxxxxxxxxxxxxx << 8 | ☃xxxxxxxxxxxxxxxxxxx;
            GlStateManager.pushMatrix();
            GlStateManager.translate(☃.getScaledWidth() - ☃xxxxxxxxxxxxxx * 1.0F - 2.0F, ☃.getScaledHeight() - 30 - ☃xxxx * (☃xxxxxxxxxxxxxxx + 1) * 1.0F, 0.0F);
            GlStateManager.scale(1.0F, 1.0F, 1.0F);
            drawRect(-☃xxxxxxxxxxxxxx - 1, -☃xxxxxxxxxxxxxxxx - 1, ☃xxxxxxxxxxxxxx + 1, ☃xxxxxxxxxxxxxxxx + 1, -872415232);
            GlStateManager.enableBlend();
            if (!☃xxxxxxxxxxxxx) {
               if (☃xxxxxxxxxxx > 0.0) {
                  this.client
                     .fontRenderer
                     .drawString(">", ☃xxxxxxxxxxxxxx - this.client.fontRenderer.getStringWidth(">"), -☃xxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx + -16777216);
               } else if (☃xxxxxxxxxxx < 0.0) {
                  this.client.fontRenderer.drawString("<", -☃xxxxxxxxxxxxxx, -☃xxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx + -16777216);
               }
            }

            this.client.fontRenderer.drawString(☃xxxxxxxxx, -☃xxxxxxxxxxxxxxxxxx / 2, -☃xxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx + -16777216);
            GlStateManager.popMatrix();
            ☃xxxx++;
         }

         GlStateManager.disableBlend();
         GlStateManager.popMatrix();
      }
   }

   @Override
   public void soundPlay(ISound var1, SoundEventAccessor var2) {
      if (☃.getSubtitle() != null) {
         String ☃ = ☃.getSubtitle().getFormattedText();
         if (!this.subtitles.isEmpty()) {
            for (GuiSubtitleOverlay.Subtitle ☃x : this.subtitles) {
               if (☃x.getString().equals(☃)) {
                  ☃x.refresh(new Vec3d(☃.getXPosF(), ☃.getYPosF(), ☃.getZPosF()));
                  return;
               }
            }
         }

         this.subtitles.add(new GuiSubtitleOverlay.Subtitle(☃, new Vec3d(☃.getXPosF(), ☃.getYPosF(), ☃.getZPosF())));
      }
   }

   public class Subtitle {
      private final String subtitle;
      private long startTime;
      private Vec3d location;

      public Subtitle(String var2, Vec3d var3) {
         this.subtitle = ☃;
         this.location = ☃;
         this.startTime = Minecraft.getSystemTime();
      }

      public String getString() {
         return this.subtitle;
      }

      public long getStartTime() {
         return this.startTime;
      }

      public Vec3d getLocation() {
         return this.location;
      }

      public void refresh(Vec3d var1) {
         this.location = ☃;
         this.startTime = Minecraft.getSystemTime();
      }
   }
}
