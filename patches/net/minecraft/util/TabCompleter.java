package net.minecraft.util;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.util.math.BlockPos;

public abstract class TabCompleter {
   protected final GuiTextField textField;
   protected final boolean hasTargetBlock;
   protected boolean didComplete;
   protected boolean requestedCompletions;
   protected int completionIdx;
   protected List<String> completions = Lists.newArrayList();

   public TabCompleter(GuiTextField var1, boolean var2) {
      this.textField = ☃;
      this.hasTargetBlock = ☃;
   }

   public void complete() {
      if (this.didComplete) {
         this.textField.deleteFromCursor(0);
         this.textField
            .deleteFromCursor(this.textField.getNthWordFromPosWS(-1, this.textField.getCursorPosition(), false) - this.textField.getCursorPosition());
         if (this.completionIdx >= this.completions.size()) {
            this.completionIdx = 0;
         }
      } else {
         int ☃ = this.textField.getNthWordFromPosWS(-1, this.textField.getCursorPosition(), false);
         this.completions.clear();
         this.completionIdx = 0;
         String ☃x = this.textField.getText().substring(0, this.textField.getCursorPosition());
         this.requestCompletions(☃x);
         if (this.completions.isEmpty()) {
            return;
         }

         this.didComplete = true;
         this.textField.deleteFromCursor(☃ - this.textField.getCursorPosition());
      }

      this.textField.writeText(this.completions.get(this.completionIdx++));
   }

   private void requestCompletions(String var1) {
      if (☃.length() >= 1) {
         Minecraft.getMinecraft().player.connection.sendPacket(new CPacketTabComplete(☃, this.getTargetBlockPos(), this.hasTargetBlock));
         this.requestedCompletions = true;
      }
   }

   @Nullable
   public abstract BlockPos getTargetBlockPos();

   public void setCompletions(String... var1) {
      if (this.requestedCompletions) {
         this.didComplete = false;
         this.completions.clear();

         for (String ☃ : ☃) {
            if (!☃.isEmpty()) {
               this.completions.add(☃);
            }
         }

         String ☃x = this.textField.getText().substring(this.textField.getNthWordFromPosWS(-1, this.textField.getCursorPosition(), false));
         String ☃xx = org.apache.commons.lang3.StringUtils.getCommonPrefix(☃);
         if (!☃xx.isEmpty() && !☃x.equalsIgnoreCase(☃xx)) {
            this.textField.deleteFromCursor(0);
            this.textField
               .deleteFromCursor(this.textField.getNthWordFromPosWS(-1, this.textField.getCursorPosition(), false) - this.textField.getCursorPosition());
            this.textField.writeText(☃xx);
         } else if (!this.completions.isEmpty()) {
            this.didComplete = true;
            this.complete();
         }
      }
   }

   public void resetDidComplete() {
      this.didComplete = false;
   }

   public void resetRequested() {
      this.requestedCompletions = false;
   }
}
