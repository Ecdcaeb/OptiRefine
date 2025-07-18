package net.minecraft.client.gui.toasts;

import com.google.common.collect.Queues;
import java.util.Arrays;
import java.util.Deque;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.math.MathHelper;

public class GuiToast extends Gui {
   private final Minecraft mc;
   private final GuiToast.ToastInstance<?>[] visible = new GuiToast.ToastInstance[5];
   private final Deque<IToast> toastsQueue = Queues.newArrayDeque();

   public GuiToast(Minecraft var1) {
      this.mc = ☃;
   }

   public void drawToast(ScaledResolution var1) {
      if (!this.mc.gameSettings.hideGUI) {
         RenderHelper.disableStandardItemLighting();

         for (int ☃ = 0; ☃ < this.visible.length; ☃++) {
            GuiToast.ToastInstance<?> ☃x = this.visible[☃];
            if (☃x != null && ☃x.render(☃.getScaledWidth(), ☃)) {
               this.visible[☃] = null;
            }

            if (this.visible[☃] == null && !this.toastsQueue.isEmpty()) {
               this.visible[☃] = new GuiToast.ToastInstance(this.toastsQueue.removeFirst());
            }
         }
      }
   }

   @Nullable
   public <T extends IToast> T getToast(Class<? extends T> var1, Object var2) {
      for (GuiToast.ToastInstance<?> ☃ : this.visible) {
         if (☃ != null && ☃.isAssignableFrom(☃.getToast().getClass()) && ☃.getToast().getType().equals(☃)) {
            return (T)☃.getToast();
         }
      }

      for (IToast ☃x : this.toastsQueue) {
         if (☃.isAssignableFrom(☃x.getClass()) && ☃x.getType().equals(☃)) {
            return (T)☃x;
         }
      }

      return null;
   }

   public void clear() {
      Arrays.fill(this.visible, null);
      this.toastsQueue.clear();
   }

   public void add(IToast var1) {
      this.toastsQueue.add(☃);
   }

   public Minecraft getMinecraft() {
      return this.mc;
   }

   class ToastInstance<T extends IToast> {
      private final T toast;
      private long animationTime = -1L;
      private long visibleTime = -1L;
      private IToast.Visibility visibility = IToast.Visibility.SHOW;

      private ToastInstance(T var2) {
         this.toast = ☃;
      }

      public T getToast() {
         return this.toast;
      }

      private float getVisibility(long var1) {
         float ☃ = MathHelper.clamp((float)(☃ - this.animationTime) / 600.0F, 0.0F, 1.0F);
         ☃ *= ☃;
         return this.visibility == IToast.Visibility.HIDE ? 1.0F - ☃ : ☃;
      }

      public boolean render(int var1, int var2) {
         long ☃ = Minecraft.getSystemTime();
         if (this.animationTime == -1L) {
            this.animationTime = ☃;
            this.visibility.playSound(GuiToast.this.mc.getSoundHandler());
         }

         if (this.visibility == IToast.Visibility.SHOW && ☃ - this.animationTime <= 600L) {
            this.visibleTime = ☃;
         }

         GlStateManager.pushMatrix();
         GlStateManager.translate(☃ - 160.0F * this.getVisibility(☃), (float)(☃ * 32), (float)(500 + ☃));
         IToast.Visibility ☃x = this.toast.draw(GuiToast.this, ☃ - this.visibleTime);
         GlStateManager.popMatrix();
         if (☃x != this.visibility) {
            this.animationTime = ☃ - (int)((1.0F - this.getVisibility(☃)) * 600.0F);
            this.visibility = ☃x;
            this.visibility.playSound(GuiToast.this.mc.getSoundHandler());
         }

         return this.visibility == IToast.Visibility.HIDE && ☃ - this.animationTime > 600L;
      }
   }
}
