package net.minecraft.client.gui.toasts;

import java.util.List;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;

public class AdvancementToast implements IToast {
   private final Advancement advancement;
   private boolean hasPlayedSound = false;

   public AdvancementToast(Advancement var1) {
      this.advancement = ☃;
   }

   @Override
   public IToast.Visibility draw(GuiToast var1, long var2) {
      ☃.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
      GlStateManager.color(1.0F, 1.0F, 1.0F);
      DisplayInfo ☃ = this.advancement.getDisplay();
      ☃.drawTexturedModalRect(0, 0, 0, 0, 160, 32);
      if (☃ != null) {
         List<String> ☃x = ☃.getMinecraft().fontRenderer.listFormattedStringToWidth(☃.getTitle().getFormattedText(), 125);
         int ☃xx = ☃.getFrame() == FrameType.CHALLENGE ? 16746751 : 16776960;
         if (☃x.size() == 1) {
            ☃.getMinecraft().fontRenderer.drawString(I18n.format("advancements.toast." + ☃.getFrame().getName()), 30, 7, ☃xx | 0xFF000000);
            ☃.getMinecraft().fontRenderer.drawString(☃.getTitle().getFormattedText(), 30, 18, -1);
         } else {
            int ☃xxx = 1500;
            float ☃xxxx = 300.0F;
            if (☃ < 1500L) {
               int ☃xxxxx = MathHelper.floor(MathHelper.clamp((float)(1500L - ☃) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
               ☃.getMinecraft().fontRenderer.drawString(I18n.format("advancements.toast." + ☃.getFrame().getName()), 30, 11, ☃xx | ☃xxxxx);
            } else {
               int ☃xxxxx = MathHelper.floor(MathHelper.clamp((float)(☃ - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
               int ☃xxxxxx = 16 - ☃x.size() * ☃.getMinecraft().fontRenderer.FONT_HEIGHT / 2;

               for (String ☃xxxxxxx : ☃x) {
                  ☃.getMinecraft().fontRenderer.drawString(☃xxxxxxx, 30, ☃xxxxxx, 16777215 | ☃xxxxx);
                  ☃xxxxxx += ☃.getMinecraft().fontRenderer.FONT_HEIGHT;
               }
            }
         }

         if (!this.hasPlayedSound && ☃ > 0L) {
            this.hasPlayedSound = true;
            if (☃.getFrame() == FrameType.CHALLENGE) {
               ☃.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getRecord(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F));
            }
         }

         RenderHelper.enableGUIStandardItemLighting();
         ☃.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(null, ☃.getIcon(), 8, 8);
         return ☃ >= 5000L ? IToast.Visibility.HIDE : IToast.Visibility.SHOW;
      } else {
         return IToast.Visibility.HIDE;
      }
   }
}
