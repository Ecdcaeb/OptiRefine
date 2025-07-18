package net.minecraft.client.gui.inventory;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.input.Keyboard;

public class GuiEditSign extends GuiScreen {
   private final TileEntitySign tileSign;
   private int updateCounter;
   private int editLine;
   private GuiButton doneBtn;

   public GuiEditSign(TileEntitySign var1) {
      this.tileSign = ☃;
   }

   @Override
   public void initGui() {
      this.buttonList.clear();
      Keyboard.enableRepeatEvents(true);
      this.doneBtn = this.addButton(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, I18n.format("gui.done")));
      this.tileSign.setEditable(false);
   }

   @Override
   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      NetHandlerPlayClient ☃ = this.mc.getConnection();
      if (☃ != null) {
         ☃.sendPacket(new CPacketUpdateSign(this.tileSign.getPos(), this.tileSign.signText));
      }

      this.tileSign.setEditable(true);
   }

   @Override
   public void updateScreen() {
      this.updateCounter++;
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.enabled) {
         if (☃.id == 0) {
            this.tileSign.markDirty();
            this.mc.displayGuiScreen(null);
         }
      }
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      if (☃ == 200) {
         this.editLine = this.editLine - 1 & 3;
      }

      if (☃ == 208 || ☃ == 28 || ☃ == 156) {
         this.editLine = this.editLine + 1 & 3;
      }

      String ☃ = this.tileSign.signText[this.editLine].getUnformattedText();
      if (☃ == 14 && !☃.isEmpty()) {
         ☃ = ☃.substring(0, ☃.length() - 1);
      }

      if (ChatAllowedCharacters.isAllowedCharacter(☃) && this.fontRenderer.getStringWidth(☃ + ☃) <= 90) {
         ☃ = ☃ + ☃;
      }

      this.tileSign.signText[this.editLine] = new TextComponentString(☃);
      if (☃ == 1) {
         this.actionPerformed(this.doneBtn);
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRenderer, I18n.format("sign.edit"), this.width / 2, 40, 16777215);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)(this.width / 2), 0.0F, 50.0F);
      float ☃ = 93.75F;
      GlStateManager.scale(-93.75F, -93.75F, -93.75F);
      GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
      Block ☃x = this.tileSign.getBlockType();
      if (☃x == Blocks.STANDING_SIGN) {
         float ☃xx = this.tileSign.getBlockMetadata() * 360 / 16.0F;
         GlStateManager.rotate(☃xx, 0.0F, 1.0F, 0.0F);
         GlStateManager.translate(0.0F, -1.0625F, 0.0F);
      } else {
         int ☃xx = this.tileSign.getBlockMetadata();
         float ☃xxx = 0.0F;
         if (☃xx == 2) {
            ☃xxx = 180.0F;
         }

         if (☃xx == 4) {
            ☃xxx = 90.0F;
         }

         if (☃xx == 5) {
            ☃xxx = -90.0F;
         }

         GlStateManager.rotate(☃xxx, 0.0F, 1.0F, 0.0F);
         GlStateManager.translate(0.0F, -1.0625F, 0.0F);
      }

      if (this.updateCounter / 6 % 2 == 0) {
         this.tileSign.lineBeingEdited = this.editLine;
      }

      TileEntityRendererDispatcher.instance.render(this.tileSign, -0.5, -0.75, -0.5, 0.0F);
      this.tileSign.lineBeingEdited = -1;
      GlStateManager.popMatrix();
      super.drawScreen(☃, ☃, ☃);
   }
}
