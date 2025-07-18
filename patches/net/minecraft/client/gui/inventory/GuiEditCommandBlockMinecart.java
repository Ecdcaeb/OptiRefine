package net.minecraft.client.gui.inventory;

import io.netty.buffer.Unpooled;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.ITabCompleter;
import net.minecraft.util.TabCompleter;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;

public class GuiEditCommandBlockMinecart extends GuiScreen implements ITabCompleter {
   private GuiTextField commandField;
   private GuiTextField previousEdit;
   private final CommandBlockBaseLogic commandBlockLogic;
   private GuiButton doneButton;
   private GuiButton cancelButton;
   private GuiButton outputButton;
   private boolean trackOutput;
   private TabCompleter tabCompleter;

   public GuiEditCommandBlockMinecart(CommandBlockBaseLogic var1) {
      this.commandBlockLogic = ☃;
   }

   @Override
   public void updateScreen() {
      this.commandField.updateCursorCounter();
   }

   @Override
   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      this.doneButton = this.addButton(new GuiButton(0, this.width / 2 - 4 - 150, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.done")));
      this.cancelButton = this.addButton(new GuiButton(1, this.width / 2 + 4, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.cancel")));
      this.outputButton = this.addButton(new GuiButton(4, this.width / 2 + 150 - 20, 150, 20, 20, "O"));
      this.commandField = new GuiTextField(2, this.fontRenderer, this.width / 2 - 150, 50, 300, 20);
      this.commandField.setMaxStringLength(32500);
      this.commandField.setFocused(true);
      this.commandField.setText(this.commandBlockLogic.getCommand());
      this.previousEdit = new GuiTextField(3, this.fontRenderer, this.width / 2 - 150, 150, 276, 20);
      this.previousEdit.setMaxStringLength(32500);
      this.previousEdit.setEnabled(false);
      this.previousEdit.setText("-");
      this.trackOutput = this.commandBlockLogic.shouldTrackOutput();
      this.updateCommandOutput();
      this.doneButton.enabled = !this.commandField.getText().trim().isEmpty();
      this.tabCompleter = new TabCompleter(this.commandField, true) {
         @Nullable
         @Override
         public BlockPos getTargetBlockPos() {
            return GuiEditCommandBlockMinecart.this.commandBlockLogic.getPosition();
         }
      };
   }

   @Override
   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.enabled) {
         if (☃.id == 1) {
            this.commandBlockLogic.setTrackOutput(this.trackOutput);
            this.mc.displayGuiScreen(null);
         } else if (☃.id == 0) {
            PacketBuffer ☃ = new PacketBuffer(Unpooled.buffer());
            ☃.writeByte(this.commandBlockLogic.getCommandBlockType());
            this.commandBlockLogic.fillInInfo(☃);
            ☃.writeString(this.commandField.getText());
            ☃.writeBoolean(this.commandBlockLogic.shouldTrackOutput());
            this.mc.getConnection().sendPacket(new CPacketCustomPayload("MC|AdvCmd", ☃));
            if (!this.commandBlockLogic.shouldTrackOutput()) {
               this.commandBlockLogic.setLastOutput(null);
            }

            this.mc.displayGuiScreen(null);
         } else if (☃.id == 4) {
            this.commandBlockLogic.setTrackOutput(!this.commandBlockLogic.shouldTrackOutput());
            this.updateCommandOutput();
         }
      }
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      this.tabCompleter.resetRequested();
      if (☃ == 15) {
         this.tabCompleter.complete();
      } else {
         this.tabCompleter.resetDidComplete();
      }

      this.commandField.textboxKeyTyped(☃, ☃);
      this.previousEdit.textboxKeyTyped(☃, ☃);
      this.doneButton.enabled = !this.commandField.getText().trim().isEmpty();
      if (☃ == 28 || ☃ == 156) {
         this.actionPerformed(this.doneButton);
      } else if (☃ == 1) {
         this.actionPerformed(this.cancelButton);
      }
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(☃, ☃, ☃);
      this.commandField.mouseClicked(☃, ☃, ☃);
      this.previousEdit.mouseClicked(☃, ☃, ☃);
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRenderer, I18n.format("advMode.setCommand"), this.width / 2, 20, 16777215);
      this.drawString(this.fontRenderer, I18n.format("advMode.command"), this.width / 2 - 150, 40, 10526880);
      this.commandField.drawTextBox();
      int ☃ = 75;
      int ☃x = 0;
      this.drawString(this.fontRenderer, I18n.format("advMode.nearestPlayer"), this.width / 2 - 140, ☃ + ☃x++ * this.fontRenderer.FONT_HEIGHT, 10526880);
      this.drawString(this.fontRenderer, I18n.format("advMode.randomPlayer"), this.width / 2 - 140, ☃ + ☃x++ * this.fontRenderer.FONT_HEIGHT, 10526880);
      this.drawString(this.fontRenderer, I18n.format("advMode.allPlayers"), this.width / 2 - 140, ☃ + ☃x++ * this.fontRenderer.FONT_HEIGHT, 10526880);
      this.drawString(this.fontRenderer, I18n.format("advMode.allEntities"), this.width / 2 - 140, ☃ + ☃x++ * this.fontRenderer.FONT_HEIGHT, 10526880);
      this.drawString(this.fontRenderer, I18n.format("advMode.self"), this.width / 2 - 140, ☃ + ☃x++ * this.fontRenderer.FONT_HEIGHT, 10526880);
      if (!this.previousEdit.getText().isEmpty()) {
         ☃ += ☃x * this.fontRenderer.FONT_HEIGHT + 20;
         this.drawString(this.fontRenderer, I18n.format("advMode.previousOutput"), this.width / 2 - 150, ☃, 10526880);
         this.previousEdit.drawTextBox();
      }

      super.drawScreen(☃, ☃, ☃);
   }

   private void updateCommandOutput() {
      if (this.commandBlockLogic.shouldTrackOutput()) {
         this.outputButton.displayString = "O";
         if (this.commandBlockLogic.getLastOutput() != null) {
            this.previousEdit.setText(this.commandBlockLogic.getLastOutput().getUnformattedText());
         }
      } else {
         this.outputButton.displayString = "X";
         this.previousEdit.setText("-");
      }
   }

   @Override
   public void setCompletions(String... var1) {
      this.tabCompleter.setCompletions(☃);
   }
}
