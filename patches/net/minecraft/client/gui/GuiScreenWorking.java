package net.minecraft.client.gui;

import net.minecraft.util.IProgressUpdate;

public class GuiScreenWorking extends GuiScreen implements IProgressUpdate {
   private String title = "";
   private String stage = "";
   private int progress;
   private boolean doneWorking;

   @Override
   public void displaySavingString(String var1) {
      this.resetProgressAndMessage(☃);
   }

   @Override
   public void resetProgressAndMessage(String var1) {
      this.title = ☃;
      this.displayLoadingString("Working...");
   }

   @Override
   public void displayLoadingString(String var1) {
      this.stage = ☃;
      this.setLoadingProgress(0);
   }

   @Override
   public void setLoadingProgress(int var1) {
      this.progress = ☃;
   }

   @Override
   public void setDoneWorking() {
      this.doneWorking = true;
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      if (this.doneWorking) {
         if (!this.mc.isConnectedToRealms()) {
            this.mc.displayGuiScreen(null);
         }
      } else {
         this.drawDefaultBackground();
         this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 70, 16777215);
         this.drawCenteredString(this.fontRenderer, this.stage + " " + this.progress + "%", this.width / 2, 90, 16777215);
         super.drawScreen(☃, ☃, ☃);
      }
   }
}
