package net.minecraft.client.gui;

import io.netty.buffer.Unpooled;
import javax.annotation.Nullable;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.ITabCompleter;
import net.minecraft.util.TabCompleter;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;

public class GuiCommandBlock extends GuiScreen implements ITabCompleter {
   private GuiTextField commandTextField;
   private GuiTextField previousOutputTextField;
   private final TileEntityCommandBlock commandBlock;
   private GuiButton doneBtn;
   private GuiButton cancelBtn;
   private GuiButton outputBtn;
   private GuiButton modeBtn;
   private GuiButton conditionalBtn;
   private GuiButton autoExecBtn;
   private boolean trackOutput;
   private TileEntityCommandBlock.Mode commandBlockMode = TileEntityCommandBlock.Mode.REDSTONE;
   private TabCompleter tabCompleter;
   private boolean conditional;
   private boolean automatic;

   public GuiCommandBlock(TileEntityCommandBlock var1) {
      this.commandBlock = ☃;
   }

   @Override
   public void updateScreen() {
      this.commandTextField.updateCursorCounter();
   }

   @Override
   public void initGui() {
      final CommandBlockBaseLogic ☃ = this.commandBlock.getCommandBlockLogic();
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      this.doneBtn = this.addButton(new GuiButton(0, this.width / 2 - 4 - 150, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.done")));
      this.cancelBtn = this.addButton(new GuiButton(1, this.width / 2 + 4, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.cancel")));
      this.outputBtn = this.addButton(new GuiButton(4, this.width / 2 + 150 - 20, 135, 20, 20, "O"));
      this.modeBtn = this.addButton(new GuiButton(5, this.width / 2 - 50 - 100 - 4, 165, 100, 20, I18n.format("advMode.mode.sequence")));
      this.conditionalBtn = this.addButton(new GuiButton(6, this.width / 2 - 50, 165, 100, 20, I18n.format("advMode.mode.unconditional")));
      this.autoExecBtn = this.addButton(new GuiButton(7, this.width / 2 + 50 + 4, 165, 100, 20, I18n.format("advMode.mode.redstoneTriggered")));
      this.commandTextField = new GuiTextField(2, this.fontRenderer, this.width / 2 - 150, 50, 300, 20);
      this.commandTextField.setMaxStringLength(32500);
      this.commandTextField.setFocused(true);
      this.previousOutputTextField = new GuiTextField(3, this.fontRenderer, this.width / 2 - 150, 135, 276, 20);
      this.previousOutputTextField.setMaxStringLength(32500);
      this.previousOutputTextField.setEnabled(false);
      this.previousOutputTextField.setText("-");
      this.doneBtn.enabled = false;
      this.outputBtn.enabled = false;
      this.modeBtn.enabled = false;
      this.conditionalBtn.enabled = false;
      this.autoExecBtn.enabled = false;
      this.tabCompleter = new TabCompleter(this.commandTextField, true) {
         @Nullable
         @Override
         public BlockPos getTargetBlockPos() {
            return ☃.getPosition();
         }
      };
   }

   public void updateGui() {
      CommandBlockBaseLogic ☃ = this.commandBlock.getCommandBlockLogic();
      this.commandTextField.setText(☃.getCommand());
      this.trackOutput = ☃.shouldTrackOutput();
      this.commandBlockMode = this.commandBlock.getMode();
      this.conditional = this.commandBlock.isConditional();
      this.automatic = this.commandBlock.isAuto();
      this.updateCmdOutput();
      this.updateMode();
      this.updateConditional();
      this.updateAutoExec();
      this.doneBtn.enabled = true;
      this.outputBtn.enabled = true;
      this.modeBtn.enabled = true;
      this.conditionalBtn.enabled = true;
      this.autoExecBtn.enabled = true;
   }

   @Override
   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.enabled) {
         CommandBlockBaseLogic ☃ = this.commandBlock.getCommandBlockLogic();
         if (☃.id == 1) {
            ☃.setTrackOutput(this.trackOutput);
            this.mc.displayGuiScreen(null);
         } else if (☃.id == 0) {
            PacketBuffer ☃x = new PacketBuffer(Unpooled.buffer());
            ☃.fillInInfo(☃x);
            ☃x.writeString(this.commandTextField.getText());
            ☃x.writeBoolean(☃.shouldTrackOutput());
            ☃x.writeString(this.commandBlockMode.name());
            ☃x.writeBoolean(this.conditional);
            ☃x.writeBoolean(this.automatic);
            this.mc.getConnection().sendPacket(new CPacketCustomPayload("MC|AutoCmd", ☃x));
            if (!☃.shouldTrackOutput()) {
               ☃.setLastOutput(null);
            }

            this.mc.displayGuiScreen(null);
         } else if (☃.id == 4) {
            ☃.setTrackOutput(!☃.shouldTrackOutput());
            this.updateCmdOutput();
         } else if (☃.id == 5) {
            this.nextMode();
            this.updateMode();
         } else if (☃.id == 6) {
            this.conditional = !this.conditional;
            this.updateConditional();
         } else if (☃.id == 7) {
            this.automatic = !this.automatic;
            this.updateAutoExec();
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

      this.commandTextField.textboxKeyTyped(☃, ☃);
      this.previousOutputTextField.textboxKeyTyped(☃, ☃);
      if (☃ == 28 || ☃ == 156) {
         this.actionPerformed(this.doneBtn);
      } else if (☃ == 1) {
         this.actionPerformed(this.cancelBtn);
      }
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(☃, ☃, ☃);
      this.commandTextField.mouseClicked(☃, ☃, ☃);
      this.previousOutputTextField.mouseClicked(☃, ☃, ☃);
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRenderer, I18n.format("advMode.setCommand"), this.width / 2, 20, 16777215);
      this.drawString(this.fontRenderer, I18n.format("advMode.command"), this.width / 2 - 150, 40, 10526880);
      this.commandTextField.drawTextBox();
      int ☃ = 75;
      int ☃x = 0;
      this.drawString(this.fontRenderer, I18n.format("advMode.nearestPlayer"), this.width / 2 - 140, ☃ + ☃x++ * this.fontRenderer.FONT_HEIGHT, 10526880);
      this.drawString(this.fontRenderer, I18n.format("advMode.randomPlayer"), this.width / 2 - 140, ☃ + ☃x++ * this.fontRenderer.FONT_HEIGHT, 10526880);
      this.drawString(this.fontRenderer, I18n.format("advMode.allPlayers"), this.width / 2 - 140, ☃ + ☃x++ * this.fontRenderer.FONT_HEIGHT, 10526880);
      this.drawString(this.fontRenderer, I18n.format("advMode.allEntities"), this.width / 2 - 140, ☃ + ☃x++ * this.fontRenderer.FONT_HEIGHT, 10526880);
      this.drawString(this.fontRenderer, I18n.format("advMode.self"), this.width / 2 - 140, ☃ + ☃x++ * this.fontRenderer.FONT_HEIGHT, 10526880);
      if (!this.previousOutputTextField.getText().isEmpty()) {
         ☃ += ☃x * this.fontRenderer.FONT_HEIGHT + 1;
         this.drawString(this.fontRenderer, I18n.format("advMode.previousOutput"), this.width / 2 - 150, ☃ + 4, 10526880);
         this.previousOutputTextField.drawTextBox();
      }

      super.drawScreen(☃, ☃, ☃);
   }

   private void updateCmdOutput() {
      CommandBlockBaseLogic ☃ = this.commandBlock.getCommandBlockLogic();
      if (☃.shouldTrackOutput()) {
         this.outputBtn.displayString = "O";
         if (☃.getLastOutput() != null) {
            this.previousOutputTextField.setText(☃.getLastOutput().getUnformattedText());
         }
      } else {
         this.outputBtn.displayString = "X";
         this.previousOutputTextField.setText("-");
      }
   }

   private void updateMode() {
      switch (this.commandBlockMode) {
         case SEQUENCE:
            this.modeBtn.displayString = I18n.format("advMode.mode.sequence");
            break;
         case AUTO:
            this.modeBtn.displayString = I18n.format("advMode.mode.auto");
            break;
         case REDSTONE:
            this.modeBtn.displayString = I18n.format("advMode.mode.redstone");
      }
   }

   private void nextMode() {
      switch (this.commandBlockMode) {
         case SEQUENCE:
            this.commandBlockMode = TileEntityCommandBlock.Mode.AUTO;
            break;
         case AUTO:
            this.commandBlockMode = TileEntityCommandBlock.Mode.REDSTONE;
            break;
         case REDSTONE:
            this.commandBlockMode = TileEntityCommandBlock.Mode.SEQUENCE;
      }
   }

   private void updateConditional() {
      if (this.conditional) {
         this.conditionalBtn.displayString = I18n.format("advMode.mode.conditional");
      } else {
         this.conditionalBtn.displayString = I18n.format("advMode.mode.unconditional");
      }
   }

   private void updateAutoExec() {
      if (this.automatic) {
         this.autoExecBtn.displayString = I18n.format("advMode.mode.autoexec.bat");
      } else {
         this.autoExecBtn.displayString = I18n.format("advMode.mode.redstoneTriggered");
      }
   }

   @Override
   public void setCompletions(String... var1) {
      this.tabCompleter.setCompletions(☃);
   }
}
