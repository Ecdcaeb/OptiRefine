package net.minecraft.client.gui.inventory;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiBeacon extends GuiContainer {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final ResourceLocation BEACON_GUI_TEXTURES = new ResourceLocation("textures/gui/container/beacon.png");
   private final IInventory tileBeacon;
   private GuiBeacon.ConfirmButton beaconConfirmButton;
   private boolean buttonsNotDrawn;

   public GuiBeacon(InventoryPlayer var1, IInventory var2) {
      super(new ContainerBeacon(☃, ☃));
      this.tileBeacon = ☃;
      this.xSize = 230;
      this.ySize = 219;
   }

   @Override
   public void initGui() {
      super.initGui();
      this.beaconConfirmButton = new GuiBeacon.ConfirmButton(-1, this.guiLeft + 164, this.guiTop + 107);
      this.buttonList.add(this.beaconConfirmButton);
      this.buttonList.add(new GuiBeacon.CancelButton(-2, this.guiLeft + 190, this.guiTop + 107));
      this.buttonsNotDrawn = true;
      this.beaconConfirmButton.enabled = false;
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
      int ☃ = this.tileBeacon.getField(0);
      Potion ☃x = Potion.getPotionById(this.tileBeacon.getField(1));
      Potion ☃xx = Potion.getPotionById(this.tileBeacon.getField(2));
      if (this.buttonsNotDrawn && ☃ >= 0) {
         this.buttonsNotDrawn = false;
         int ☃xxx = 100;

         for (int ☃xxxx = 0; ☃xxxx <= 2; ☃xxxx++) {
            int ☃xxxxx = TileEntityBeacon.EFFECTS_LIST[☃xxxx].length;
            int ☃xxxxxx = ☃xxxxx * 22 + (☃xxxxx - 1) * 2;

            for (int ☃xxxxxxx = 0; ☃xxxxxxx < ☃xxxxx; ☃xxxxxxx++) {
               Potion ☃xxxxxxxx = TileEntityBeacon.EFFECTS_LIST[☃xxxx][☃xxxxxxx];
               GuiBeacon.PowerButton ☃xxxxxxxxx = new GuiBeacon.PowerButton(
                  ☃xxx++, this.guiLeft + 76 + ☃xxxxxxx * 24 - ☃xxxxxx / 2, this.guiTop + 22 + ☃xxxx * 25, ☃xxxxxxxx, ☃xxxx
               );
               this.buttonList.add(☃xxxxxxxxx);
               if (☃xxxx >= ☃) {
                  ☃xxxxxxxxx.enabled = false;
               } else if (☃xxxxxxxx == ☃x) {
                  ☃xxxxxxxxx.setSelected(true);
               }
            }
         }

         int ☃xxxx = 3;
         int ☃xxxxx = TileEntityBeacon.EFFECTS_LIST[3].length + 1;
         int ☃xxxxxx = ☃xxxxx * 22 + (☃xxxxx - 1) * 2;

         for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < ☃xxxxx - 1; ☃xxxxxxxx++) {
            Potion ☃xxxxxxxxx = TileEntityBeacon.EFFECTS_LIST[3][☃xxxxxxxx];
            GuiBeacon.PowerButton ☃xxxxxxxxxx = new GuiBeacon.PowerButton(
               ☃xxx++, this.guiLeft + 167 + ☃xxxxxxxx * 24 - ☃xxxxxx / 2, this.guiTop + 47, ☃xxxxxxxxx, 3
            );
            this.buttonList.add(☃xxxxxxxxxx);
            if (3 >= ☃) {
               ☃xxxxxxxxxx.enabled = false;
            } else if (☃xxxxxxxxx == ☃xx) {
               ☃xxxxxxxxxx.setSelected(true);
            }
         }

         if (☃x != null) {
            GuiBeacon.PowerButton ☃xxxxxxxxx = new GuiBeacon.PowerButton(☃xxx++, this.guiLeft + 167 + (☃xxxxx - 1) * 24 - ☃xxxxxx / 2, this.guiTop + 47, ☃x, 3);
            this.buttonList.add(☃xxxxxxxxx);
            if (3 >= ☃) {
               ☃xxxxxxxxx.enabled = false;
            } else if (☃x == ☃xx) {
               ☃xxxxxxxxx.setSelected(true);
            }
         }
      }

      this.beaconConfirmButton.enabled = !this.tileBeacon.getStackInSlot(0).isEmpty() && ☃x != null;
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.id == -2) {
         this.mc.player.connection.sendPacket(new CPacketCloseWindow(this.mc.player.openContainer.windowId));
         this.mc.displayGuiScreen(null);
      } else if (☃.id == -1) {
         String ☃ = "MC|Beacon";
         PacketBuffer ☃x = new PacketBuffer(Unpooled.buffer());
         ☃x.writeInt(this.tileBeacon.getField(1));
         ☃x.writeInt(this.tileBeacon.getField(2));
         this.mc.getConnection().sendPacket(new CPacketCustomPayload("MC|Beacon", ☃x));
         this.mc.player.connection.sendPacket(new CPacketCloseWindow(this.mc.player.openContainer.windowId));
         this.mc.displayGuiScreen(null);
      } else if (☃ instanceof GuiBeacon.PowerButton) {
         GuiBeacon.PowerButton ☃ = (GuiBeacon.PowerButton)☃;
         if (☃.isSelected()) {
            return;
         }

         int ☃x = Potion.getIdFromPotion(☃.effect);
         if (☃.tier < 3) {
            this.tileBeacon.setField(1, ☃x);
         } else {
            this.tileBeacon.setField(2, ☃x);
         }

         this.buttonList.clear();
         this.initGui();
         this.updateScreen();
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      super.drawScreen(☃, ☃, ☃);
      this.renderHoveredToolTip(☃, ☃);
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      RenderHelper.disableStandardItemLighting();
      this.drawCenteredString(this.fontRenderer, I18n.format("tile.beacon.primary"), 62, 10, 14737632);
      this.drawCenteredString(this.fontRenderer, I18n.format("tile.beacon.secondary"), 169, 10, 14737632);

      for (GuiButton ☃ : this.buttonList) {
         if (☃.isMouseOver()) {
            ☃.drawButtonForegroundLayer(☃ - this.guiLeft, ☃ - this.guiTop);
            break;
         }
      }

      RenderHelper.enableGUIStandardItemLighting();
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(BEACON_GUI_TEXTURES);
      int ☃ = (this.width - this.xSize) / 2;
      int ☃x = (this.height - this.ySize) / 2;
      this.drawTexturedModalRect(☃, ☃x, 0, 0, this.xSize, this.ySize);
      this.itemRender.zLevel = 100.0F;
      this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.EMERALD), ☃ + 42, ☃x + 109);
      this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.DIAMOND), ☃ + 42 + 22, ☃x + 109);
      this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.GOLD_INGOT), ☃ + 42 + 44, ☃x + 109);
      this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.IRON_INGOT), ☃ + 42 + 66, ☃x + 109);
      this.itemRender.zLevel = 0.0F;
   }

   static class Button extends GuiButton {
      private final ResourceLocation iconTexture;
      private final int iconX;
      private final int iconY;
      private boolean selected;

      protected Button(int var1, int var2, int var3, ResourceLocation var4, int var5, int var6) {
         super(☃, ☃, ☃, 22, 22, "");
         this.iconTexture = ☃;
         this.iconX = ☃;
         this.iconY = ☃;
      }

      @Override
      public void drawButton(Minecraft var1, int var2, int var3, float var4) {
         if (this.visible) {
            ☃.getTextureManager().bindTexture(GuiBeacon.BEACON_GUI_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = ☃ >= this.x && ☃ >= this.y && ☃ < this.x + this.width && ☃ < this.y + this.height;
            int ☃ = 219;
            int ☃x = 0;
            if (!this.enabled) {
               ☃x += this.width * 2;
            } else if (this.selected) {
               ☃x += this.width * 1;
            } else if (this.hovered) {
               ☃x += this.width * 3;
            }

            this.drawTexturedModalRect(this.x, this.y, ☃x, 219, this.width, this.height);
            if (!GuiBeacon.BEACON_GUI_TEXTURES.equals(this.iconTexture)) {
               ☃.getTextureManager().bindTexture(this.iconTexture);
            }

            this.drawTexturedModalRect(this.x + 2, this.y + 2, this.iconX, this.iconY, 18, 18);
         }
      }

      public boolean isSelected() {
         return this.selected;
      }

      public void setSelected(boolean var1) {
         this.selected = ☃;
      }
   }

   class CancelButton extends GuiBeacon.Button {
      public CancelButton(int var2, int var3, int var4) {
         super(☃, ☃, ☃, GuiBeacon.BEACON_GUI_TEXTURES, 112, 220);
      }

      @Override
      public void drawButtonForegroundLayer(int var1, int var2) {
         GuiBeacon.this.drawHoveringText(I18n.format("gui.cancel"), ☃, ☃);
      }
   }

   class ConfirmButton extends GuiBeacon.Button {
      public ConfirmButton(int var2, int var3, int var4) {
         super(☃, ☃, ☃, GuiBeacon.BEACON_GUI_TEXTURES, 90, 220);
      }

      @Override
      public void drawButtonForegroundLayer(int var1, int var2) {
         GuiBeacon.this.drawHoveringText(I18n.format("gui.done"), ☃, ☃);
      }
   }

   class PowerButton extends GuiBeacon.Button {
      private final Potion effect;
      private final int tier;

      public PowerButton(int var2, int var3, int var4, Potion var5, int var6) {
         super(☃, ☃, ☃, GuiContainer.INVENTORY_BACKGROUND, ☃.getStatusIconIndex() % 8 * 18, 198 + ☃.getStatusIconIndex() / 8 * 18);
         this.effect = ☃;
         this.tier = ☃;
      }

      @Override
      public void drawButtonForegroundLayer(int var1, int var2) {
         String ☃ = I18n.format(this.effect.getName());
         if (this.tier >= 3 && this.effect != MobEffects.REGENERATION) {
            ☃ = ☃ + " II";
         }

         GuiBeacon.this.drawHoveringText(☃, ☃, ☃);
      }
   }
}
