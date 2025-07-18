package net.minecraft.client.gui;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public class GuiRepair extends GuiContainer implements IContainerListener {
   private static final ResourceLocation ANVIL_RESOURCE = new ResourceLocation("textures/gui/container/anvil.png");
   private final ContainerRepair anvil;
   private GuiTextField nameField;
   private final InventoryPlayer playerInventory;

   public GuiRepair(InventoryPlayer var1, World var2) {
      super(new ContainerRepair(☃, ☃, Minecraft.getMinecraft().player));
      this.playerInventory = ☃;
      this.anvil = (ContainerRepair)this.inventorySlots;
   }

   @Override
   public void initGui() {
      super.initGui();
      Keyboard.enableRepeatEvents(true);
      int ☃ = (this.width - this.xSize) / 2;
      int ☃x = (this.height - this.ySize) / 2;
      this.nameField = new GuiTextField(0, this.fontRenderer, ☃ + 62, ☃x + 24, 103, 12);
      this.nameField.setTextColor(-1);
      this.nameField.setDisabledTextColour(-1);
      this.nameField.setEnableBackgroundDrawing(false);
      this.nameField.setMaxStringLength(35);
      this.inventorySlots.removeListener(this);
      this.inventorySlots.addListener(this);
   }

   @Override
   public void onGuiClosed() {
      super.onGuiClosed();
      Keyboard.enableRepeatEvents(false);
      this.inventorySlots.removeListener(this);
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      GlStateManager.disableLighting();
      GlStateManager.disableBlend();
      this.fontRenderer.drawString(I18n.format("container.repair"), 60, 6, 4210752);
      if (this.anvil.maximumCost > 0) {
         int ☃ = 8453920;
         boolean ☃x = true;
         String ☃xx = I18n.format("container.repair.cost", this.anvil.maximumCost);
         if (this.anvil.maximumCost >= 40 && !this.mc.player.capabilities.isCreativeMode) {
            ☃xx = I18n.format("container.repair.expensive");
            ☃ = 16736352;
         } else if (!this.anvil.getSlot(2).getHasStack()) {
            ☃x = false;
         } else if (!this.anvil.getSlot(2).canTakeStack(this.playerInventory.player)) {
            ☃ = 16736352;
         }

         if (☃x) {
            int ☃xxx = 0xFF000000 | (☃ & 16579836) >> 2 | ☃ & 0xFF000000;
            int ☃xxxx = this.xSize - 8 - this.fontRenderer.getStringWidth(☃xx);
            int ☃xxxxx = 67;
            if (this.fontRenderer.getUnicodeFlag()) {
               drawRect(☃xxxx - 3, 65, this.xSize - 7, 77, -16777216);
               drawRect(☃xxxx - 2, 66, this.xSize - 8, 76, -12895429);
            } else {
               this.fontRenderer.drawString(☃xx, ☃xxxx, 68, ☃xxx);
               this.fontRenderer.drawString(☃xx, ☃xxxx + 1, 67, ☃xxx);
               this.fontRenderer.drawString(☃xx, ☃xxxx + 1, 68, ☃xxx);
            }

            this.fontRenderer.drawString(☃xx, ☃xxxx, 67, ☃);
         }
      }

      GlStateManager.enableLighting();
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      if (this.nameField.textboxKeyTyped(☃, ☃)) {
         this.renameItem();
      } else {
         super.keyTyped(☃, ☃);
      }
   }

   private void renameItem() {
      String ☃ = this.nameField.getText();
      Slot ☃x = this.anvil.getSlot(0);
      if (☃x != null && ☃x.getHasStack() && !☃x.getStack().hasDisplayName() && ☃.equals(☃x.getStack().getDisplayName())) {
         ☃ = "";
      }

      this.anvil.updateItemName(☃);
      this.mc.player.connection.sendPacket(new CPacketCustomPayload("MC|ItemName", new PacketBuffer(Unpooled.buffer()).writeString(☃)));
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(☃, ☃, ☃);
      this.nameField.mouseClicked(☃, ☃, ☃);
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      super.drawScreen(☃, ☃, ☃);
      this.renderHoveredToolTip(☃, ☃);
      GlStateManager.disableLighting();
      GlStateManager.disableBlend();
      this.nameField.drawTextBox();
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(ANVIL_RESOURCE);
      int ☃ = (this.width - this.xSize) / 2;
      int ☃x = (this.height - this.ySize) / 2;
      this.drawTexturedModalRect(☃, ☃x, 0, 0, this.xSize, this.ySize);
      this.drawTexturedModalRect(☃ + 59, ☃x + 20, 0, this.ySize + (this.anvil.getSlot(0).getHasStack() ? 0 : 16), 110, 16);
      if ((this.anvil.getSlot(0).getHasStack() || this.anvil.getSlot(1).getHasStack()) && !this.anvil.getSlot(2).getHasStack()) {
         this.drawTexturedModalRect(☃ + 99, ☃x + 45, this.xSize, 0, 28, 21);
      }
   }

   @Override
   public void sendAllContents(Container var1, NonNullList<ItemStack> var2) {
      this.sendSlotContents(☃, 0, ☃.getSlot(0).getStack());
   }

   @Override
   public void sendSlotContents(Container var1, int var2, ItemStack var3) {
      if (☃ == 0) {
         this.nameField.setText(☃.isEmpty() ? "" : ☃.getDisplayName());
         this.nameField.setEnabled(!☃.isEmpty());
         if (!☃.isEmpty()) {
            this.renameItem();
         }
      }
   }

   @Override
   public void sendWindowProperty(Container var1, int var2, int var3) {
   }

   @Override
   public void sendAllWindowProperties(Container var1, IInventory var2) {
   }
}
