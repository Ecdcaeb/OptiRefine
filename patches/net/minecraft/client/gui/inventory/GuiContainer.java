package net.minecraft.client.gui.inventory;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

public abstract class GuiContainer extends GuiScreen {
   public static final ResourceLocation INVENTORY_BACKGROUND = new ResourceLocation("textures/gui/container/inventory.png");
   protected int xSize = 176;
   protected int ySize = 166;
   public Container inventorySlots;
   protected int guiLeft;
   protected int guiTop;
   private Slot hoveredSlot;
   private Slot clickedSlot;
   private boolean isRightMouseClick;
   private ItemStack draggedStack = ItemStack.EMPTY;
   private int touchUpX;
   private int touchUpY;
   private Slot returningStackDestSlot;
   private long returningStackTime;
   private ItemStack returningStack = ItemStack.EMPTY;
   private Slot currentDragTargetSlot;
   private long dragItemDropDelay;
   protected final Set<Slot> dragSplittingSlots = Sets.newHashSet();
   protected boolean dragSplitting;
   private int dragSplittingLimit;
   private int dragSplittingButton;
   private boolean ignoreMouseUp;
   private int dragSplittingRemnant;
   private long lastClickTime;
   private Slot lastClickSlot;
   private int lastClickButton;
   private boolean doubleClick;
   private ItemStack shiftClickedSlot = ItemStack.EMPTY;

   public GuiContainer(Container var1) {
      this.inventorySlots = ☃;
      this.ignoreMouseUp = true;
   }

   @Override
   public void initGui() {
      super.initGui();
      this.mc.player.openContainer = this.inventorySlots;
      this.guiLeft = (this.width - this.xSize) / 2;
      this.guiTop = (this.height - this.ySize) / 2;
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      int ☃ = this.guiLeft;
      int ☃x = this.guiTop;
      this.drawGuiContainerBackgroundLayer(☃, ☃, ☃);
      GlStateManager.disableRescaleNormal();
      RenderHelper.disableStandardItemLighting();
      GlStateManager.disableLighting();
      GlStateManager.disableDepth();
      super.drawScreen(☃, ☃, ☃);
      RenderHelper.enableGUIStandardItemLighting();
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)☃, (float)☃x, 0.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableRescaleNormal();
      this.hoveredSlot = null;
      int ☃xx = 240;
      int ☃xxx = 240;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

      for (int ☃xxxx = 0; ☃xxxx < this.inventorySlots.inventorySlots.size(); ☃xxxx++) {
         Slot ☃xxxxx = this.inventorySlots.inventorySlots.get(☃xxxx);
         if (☃xxxxx.isEnabled()) {
            this.drawSlot(☃xxxxx);
         }

         if (this.isMouseOverSlot(☃xxxxx, ☃, ☃) && ☃xxxxx.isEnabled()) {
            this.hoveredSlot = ☃xxxxx;
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int ☃xxxxxx = ☃xxxxx.xPos;
            int ☃xxxxxxx = ☃xxxxx.yPos;
            GlStateManager.colorMask(true, true, true, false);
            this.drawGradientRect(☃xxxxxx, ☃xxxxxxx, ☃xxxxxx + 16, ☃xxxxxxx + 16, -2130706433, -2130706433);
            GlStateManager.colorMask(true, true, true, true);
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
         }
      }

      RenderHelper.disableStandardItemLighting();
      this.drawGuiContainerForegroundLayer(☃, ☃);
      RenderHelper.enableGUIStandardItemLighting();
      InventoryPlayer ☃xxxx = this.mc.player.inventory;
      ItemStack ☃xxxxxx = this.draggedStack.isEmpty() ? ☃xxxx.getItemStack() : this.draggedStack;
      if (!☃xxxxxx.isEmpty()) {
         int ☃xxxxxxx = 8;
         int ☃xxxxxxxx = this.draggedStack.isEmpty() ? 8 : 16;
         String ☃xxxxxxxxx = null;
         if (!this.draggedStack.isEmpty() && this.isRightMouseClick) {
            ☃xxxxxx = ☃xxxxxx.copy();
            ☃xxxxxx.setCount(MathHelper.ceil(☃xxxxxx.getCount() / 2.0F));
         } else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
            ☃xxxxxx = ☃xxxxxx.copy();
            ☃xxxxxx.setCount(this.dragSplittingRemnant);
            if (☃xxxxxx.isEmpty()) {
               ☃xxxxxxxxx = "" + TextFormatting.YELLOW + "0";
            }
         }

         this.drawItemStack(☃xxxxxx, ☃ - ☃ - 8, ☃ - ☃x - ☃xxxxxxxx, ☃xxxxxxxxx);
      }

      if (!this.returningStack.isEmpty()) {
         float ☃xxxxxxx = (float)(Minecraft.getSystemTime() - this.returningStackTime) / 100.0F;
         if (☃xxxxxxx >= 1.0F) {
            ☃xxxxxxx = 1.0F;
            this.returningStack = ItemStack.EMPTY;
         }

         int ☃xxxxxxxx = this.returningStackDestSlot.xPos - this.touchUpX;
         int ☃xxxxxxxxx = this.returningStackDestSlot.yPos - this.touchUpY;
         int ☃xxxxxxxxxx = this.touchUpX + (int)(☃xxxxxxxx * ☃xxxxxxx);
         int ☃xxxxxxxxxxx = this.touchUpY + (int)(☃xxxxxxxxx * ☃xxxxxxx);
         this.drawItemStack(this.returningStack, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, null);
      }

      GlStateManager.popMatrix();
      GlStateManager.enableLighting();
      GlStateManager.enableDepth();
      RenderHelper.enableStandardItemLighting();
   }

   protected void renderHoveredToolTip(int var1, int var2) {
      if (this.mc.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {
         this.renderToolTip(this.hoveredSlot.getStack(), ☃, ☃);
      }
   }

   private void drawItemStack(ItemStack var1, int var2, int var3, String var4) {
      GlStateManager.translate(0.0F, 0.0F, 32.0F);
      this.zLevel = 200.0F;
      this.itemRender.zLevel = 200.0F;
      this.itemRender.renderItemAndEffectIntoGUI(☃, ☃, ☃);
      this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, ☃, ☃, ☃ - (this.draggedStack.isEmpty() ? 0 : 8), ☃);
      this.zLevel = 0.0F;
      this.itemRender.zLevel = 0.0F;
   }

   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
   }

   protected abstract void drawGuiContainerBackgroundLayer(float var1, int var2, int var3);

   private void drawSlot(Slot var1) {
      int ☃ = ☃.xPos;
      int ☃x = ☃.yPos;
      ItemStack ☃xx = ☃.getStack();
      boolean ☃xxx = false;
      boolean ☃xxxx = ☃ == this.clickedSlot && !this.draggedStack.isEmpty() && !this.isRightMouseClick;
      ItemStack ☃xxxxx = this.mc.player.inventory.getItemStack();
      String ☃xxxxxx = null;
      if (☃ == this.clickedSlot && !this.draggedStack.isEmpty() && this.isRightMouseClick && !☃xx.isEmpty()) {
         ☃xx = ☃xx.copy();
         ☃xx.setCount(☃xx.getCount() / 2);
      } else if (this.dragSplitting && this.dragSplittingSlots.contains(☃) && !☃xxxxx.isEmpty()) {
         if (this.dragSplittingSlots.size() == 1) {
            return;
         }

         if (Container.canAddItemToSlot(☃, ☃xxxxx, true) && this.inventorySlots.canDragIntoSlot(☃)) {
            ☃xx = ☃xxxxx.copy();
            ☃xxx = true;
            Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, ☃xx, ☃.getStack().isEmpty() ? 0 : ☃.getStack().getCount());
            int ☃xxxxxxx = Math.min(☃xx.getMaxStackSize(), ☃.getItemStackLimit(☃xx));
            if (☃xx.getCount() > ☃xxxxxxx) {
               ☃xxxxxx = TextFormatting.YELLOW.toString() + ☃xxxxxxx;
               ☃xx.setCount(☃xxxxxxx);
            }
         } else {
            this.dragSplittingSlots.remove(☃);
            this.updateDragSplitting();
         }
      }

      this.zLevel = 100.0F;
      this.itemRender.zLevel = 100.0F;
      if (☃xx.isEmpty() && ☃.isEnabled()) {
         String ☃xxxxxxx = ☃.getSlotTexture();
         if (☃xxxxxxx != null) {
            TextureAtlasSprite ☃xxxxxxxx = this.mc.getTextureMapBlocks().getAtlasSprite(☃xxxxxxx);
            GlStateManager.disableLighting();
            this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            this.drawTexturedModalRect(☃, ☃x, ☃xxxxxxxx, 16, 16);
            GlStateManager.enableLighting();
            ☃xxxx = true;
         }
      }

      if (!☃xxxx) {
         if (☃xxx) {
            drawRect(☃, ☃x, ☃ + 16, ☃x + 16, -2130706433);
         }

         GlStateManager.enableDepth();
         this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, ☃xx, ☃, ☃x);
         this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, ☃xx, ☃, ☃x, ☃xxxxxx);
      }

      this.itemRender.zLevel = 0.0F;
      this.zLevel = 0.0F;
   }

   private void updateDragSplitting() {
      ItemStack ☃ = this.mc.player.inventory.getItemStack();
      if (!☃.isEmpty() && this.dragSplitting) {
         if (this.dragSplittingLimit == 2) {
            this.dragSplittingRemnant = ☃.getMaxStackSize();
         } else {
            this.dragSplittingRemnant = ☃.getCount();

            for (Slot ☃x : this.dragSplittingSlots) {
               ItemStack ☃xx = ☃.copy();
               ItemStack ☃xxx = ☃x.getStack();
               int ☃xxxx = ☃xxx.isEmpty() ? 0 : ☃xxx.getCount();
               Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, ☃xx, ☃xxxx);
               int ☃xxxxx = Math.min(☃xx.getMaxStackSize(), ☃x.getItemStackLimit(☃xx));
               if (☃xx.getCount() > ☃xxxxx) {
                  ☃xx.setCount(☃xxxxx);
               }

               this.dragSplittingRemnant = this.dragSplittingRemnant - (☃xx.getCount() - ☃xxxx);
            }
         }
      }
   }

   private Slot getSlotAtPosition(int var1, int var2) {
      for (int ☃ = 0; ☃ < this.inventorySlots.inventorySlots.size(); ☃++) {
         Slot ☃x = this.inventorySlots.inventorySlots.get(☃);
         if (this.isMouseOverSlot(☃x, ☃, ☃) && ☃x.isEnabled()) {
            return ☃x;
         }
      }

      return null;
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(☃, ☃, ☃);
      boolean ☃ = ☃ == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100;
      Slot ☃x = this.getSlotAtPosition(☃, ☃);
      long ☃xx = Minecraft.getSystemTime();
      this.doubleClick = this.lastClickSlot == ☃x && ☃xx - this.lastClickTime < 250L && this.lastClickButton == ☃;
      this.ignoreMouseUp = false;
      if (☃ == 0 || ☃ == 1 || ☃) {
         int ☃xxx = this.guiLeft;
         int ☃xxxx = this.guiTop;
         boolean ☃xxxxx = this.hasClickedOutside(☃, ☃, ☃xxx, ☃xxxx);
         int ☃xxxxxx = -1;
         if (☃x != null) {
            ☃xxxxxx = ☃x.slotNumber;
         }

         if (☃xxxxx) {
            ☃xxxxxx = -999;
         }

         if (this.mc.gameSettings.touchscreen && ☃xxxxx && this.mc.player.inventory.getItemStack().isEmpty()) {
            this.mc.displayGuiScreen(null);
            return;
         }

         if (☃xxxxxx != -1) {
            if (this.mc.gameSettings.touchscreen) {
               if (☃x != null && ☃x.getHasStack()) {
                  this.clickedSlot = ☃x;
                  this.draggedStack = ItemStack.EMPTY;
                  this.isRightMouseClick = ☃ == 1;
               } else {
                  this.clickedSlot = null;
               }
            } else if (!this.dragSplitting) {
               if (this.mc.player.inventory.getItemStack().isEmpty()) {
                  if (☃ == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
                     this.handleMouseClick(☃x, ☃xxxxxx, ☃, ClickType.CLONE);
                  } else {
                     boolean ☃xxxxxxx = ☃xxxxxx != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                     ClickType ☃xxxxxxxx = ClickType.PICKUP;
                     if (☃xxxxxxx) {
                        this.shiftClickedSlot = ☃x != null && ☃x.getHasStack() ? ☃x.getStack().copy() : ItemStack.EMPTY;
                        ☃xxxxxxxx = ClickType.QUICK_MOVE;
                     } else if (☃xxxxxx == -999) {
                        ☃xxxxxxxx = ClickType.THROW;
                     }

                     this.handleMouseClick(☃x, ☃xxxxxx, ☃, ☃xxxxxxxx);
                  }

                  this.ignoreMouseUp = true;
               } else {
                  this.dragSplitting = true;
                  this.dragSplittingButton = ☃;
                  this.dragSplittingSlots.clear();
                  if (☃ == 0) {
                     this.dragSplittingLimit = 0;
                  } else if (☃ == 1) {
                     this.dragSplittingLimit = 1;
                  } else if (☃ == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
                     this.dragSplittingLimit = 2;
                  }
               }
            }
         }
      }

      this.lastClickSlot = ☃x;
      this.lastClickTime = ☃xx;
      this.lastClickButton = ☃;
   }

   protected boolean hasClickedOutside(int var1, int var2, int var3, int var4) {
      return ☃ < ☃ || ☃ < ☃ || ☃ >= ☃ + this.xSize || ☃ >= ☃ + this.ySize;
   }

   @Override
   protected void mouseClickMove(int var1, int var2, int var3, long var4) {
      Slot ☃ = this.getSlotAtPosition(☃, ☃);
      ItemStack ☃x = this.mc.player.inventory.getItemStack();
      if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
         if (☃ == 0 || ☃ == 1) {
            if (this.draggedStack.isEmpty()) {
               if (☃ != this.clickedSlot && !this.clickedSlot.getStack().isEmpty()) {
                  this.draggedStack = this.clickedSlot.getStack().copy();
               }
            } else if (this.draggedStack.getCount() > 1 && ☃ != null && Container.canAddItemToSlot(☃, this.draggedStack, false)) {
               long ☃xx = Minecraft.getSystemTime();
               if (this.currentDragTargetSlot == ☃) {
                  if (☃xx - this.dragItemDropDelay > 500L) {
                     this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, ClickType.PICKUP);
                     this.handleMouseClick(☃, ☃.slotNumber, 1, ClickType.PICKUP);
                     this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, ClickType.PICKUP);
                     this.dragItemDropDelay = ☃xx + 750L;
                     this.draggedStack.shrink(1);
                  }
               } else {
                  this.currentDragTargetSlot = ☃;
                  this.dragItemDropDelay = ☃xx;
               }
            }
         }
      } else if (this.dragSplitting
         && ☃ != null
         && !☃x.isEmpty()
         && (☃x.getCount() > this.dragSplittingSlots.size() || this.dragSplittingLimit == 2)
         && Container.canAddItemToSlot(☃, ☃x, true)
         && ☃.isItemValid(☃x)
         && this.inventorySlots.canDragIntoSlot(☃)) {
         this.dragSplittingSlots.add(☃);
         this.updateDragSplitting();
      }
   }

   @Override
   protected void mouseReleased(int var1, int var2, int var3) {
      Slot ☃ = this.getSlotAtPosition(☃, ☃);
      int ☃x = this.guiLeft;
      int ☃xx = this.guiTop;
      boolean ☃xxx = this.hasClickedOutside(☃, ☃, ☃x, ☃xx);
      int ☃xxxx = -1;
      if (☃ != null) {
         ☃xxxx = ☃.slotNumber;
      }

      if (☃xxx) {
         ☃xxxx = -999;
      }

      if (this.doubleClick && ☃ != null && ☃ == 0 && this.inventorySlots.canMergeSlot(ItemStack.EMPTY, ☃)) {
         if (isShiftKeyDown()) {
            if (!this.shiftClickedSlot.isEmpty()) {
               for (Slot ☃xxxxx : this.inventorySlots.inventorySlots) {
                  if (☃xxxxx != null
                     && ☃xxxxx.canTakeStack(this.mc.player)
                     && ☃xxxxx.getHasStack()
                     && ☃xxxxx.inventory == ☃.inventory
                     && Container.canAddItemToSlot(☃xxxxx, this.shiftClickedSlot, true)) {
                     this.handleMouseClick(☃xxxxx, ☃xxxxx.slotNumber, ☃, ClickType.QUICK_MOVE);
                  }
               }
            }
         } else {
            this.handleMouseClick(☃, ☃xxxx, ☃, ClickType.PICKUP_ALL);
         }

         this.doubleClick = false;
         this.lastClickTime = 0L;
      } else {
         if (this.dragSplitting && this.dragSplittingButton != ☃) {
            this.dragSplitting = false;
            this.dragSplittingSlots.clear();
            this.ignoreMouseUp = true;
            return;
         }

         if (this.ignoreMouseUp) {
            this.ignoreMouseUp = false;
            return;
         }

         if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
            if (☃ == 0 || ☃ == 1) {
               if (this.draggedStack.isEmpty() && ☃ != this.clickedSlot) {
                  this.draggedStack = this.clickedSlot.getStack();
               }

               boolean ☃xxxxxx = Container.canAddItemToSlot(☃, this.draggedStack, false);
               if (☃xxxx != -1 && !this.draggedStack.isEmpty() && ☃xxxxxx) {
                  this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, ☃, ClickType.PICKUP);
                  this.handleMouseClick(☃, ☃xxxx, 0, ClickType.PICKUP);
                  if (this.mc.player.inventory.getItemStack().isEmpty()) {
                     this.returningStack = ItemStack.EMPTY;
                  } else {
                     this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, ☃, ClickType.PICKUP);
                     this.touchUpX = ☃ - ☃x;
                     this.touchUpY = ☃ - ☃xx;
                     this.returningStackDestSlot = this.clickedSlot;
                     this.returningStack = this.draggedStack;
                     this.returningStackTime = Minecraft.getSystemTime();
                  }
               } else if (!this.draggedStack.isEmpty()) {
                  this.touchUpX = ☃ - ☃x;
                  this.touchUpY = ☃ - ☃xx;
                  this.returningStackDestSlot = this.clickedSlot;
                  this.returningStack = this.draggedStack;
                  this.returningStackTime = Minecraft.getSystemTime();
               }

               this.draggedStack = ItemStack.EMPTY;
               this.clickedSlot = null;
            }
         } else if (this.dragSplitting && !this.dragSplittingSlots.isEmpty()) {
            this.handleMouseClick(null, -999, Container.getQuickcraftMask(0, this.dragSplittingLimit), ClickType.QUICK_CRAFT);

            for (Slot ☃xxxxxx : this.dragSplittingSlots) {
               this.handleMouseClick(☃xxxxxx, ☃xxxxxx.slotNumber, Container.getQuickcraftMask(1, this.dragSplittingLimit), ClickType.QUICK_CRAFT);
            }

            this.handleMouseClick(null, -999, Container.getQuickcraftMask(2, this.dragSplittingLimit), ClickType.QUICK_CRAFT);
         } else if (!this.mc.player.inventory.getItemStack().isEmpty()) {
            if (☃ == this.mc.gameSettings.keyBindPickBlock.getKeyCode() + 100) {
               this.handleMouseClick(☃, ☃xxxx, ☃, ClickType.CLONE);
            } else {
               boolean ☃xxxxxx = ☃xxxx != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
               if (☃xxxxxx) {
                  this.shiftClickedSlot = ☃ != null && ☃.getHasStack() ? ☃.getStack().copy() : ItemStack.EMPTY;
               }

               this.handleMouseClick(☃, ☃xxxx, ☃, ☃xxxxxx ? ClickType.QUICK_MOVE : ClickType.PICKUP);
            }
         }
      }

      if (this.mc.player.inventory.getItemStack().isEmpty()) {
         this.lastClickTime = 0L;
      }

      this.dragSplitting = false;
   }

   private boolean isMouseOverSlot(Slot var1, int var2, int var3) {
      return this.isPointInRegion(☃.xPos, ☃.yPos, 16, 16, ☃, ☃);
   }

   protected boolean isPointInRegion(int var1, int var2, int var3, int var4, int var5, int var6) {
      int ☃ = this.guiLeft;
      int ☃x = this.guiTop;
      ☃ -= ☃;
      ☃ -= ☃x;
      return ☃ >= ☃ - 1 && ☃ < ☃ + ☃ + 1 && ☃ >= ☃ - 1 && ☃ < ☃ + ☃ + 1;
   }

   protected void handleMouseClick(Slot var1, int var2, int var3, ClickType var4) {
      if (☃ != null) {
         ☃ = ☃.slotNumber;
      }

      this.mc.playerController.windowClick(this.inventorySlots.windowId, ☃, ☃, ☃, this.mc.player);
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      if (☃ == 1 || ☃ == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
         this.mc.player.closeScreen();
      }

      this.checkHotbarKeys(☃);
      if (this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {
         if (☃ == this.mc.gameSettings.keyBindPickBlock.getKeyCode()) {
            this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, 0, ClickType.CLONE);
         } else if (☃ == this.mc.gameSettings.keyBindDrop.getKeyCode()) {
            this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, ClickType.THROW);
         }
      }
   }

   protected boolean checkHotbarKeys(int var1) {
      if (this.mc.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null) {
         for (int ☃ = 0; ☃ < 9; ☃++) {
            if (☃ == this.mc.gameSettings.keyBindsHotbar[☃].getKeyCode()) {
               this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, ☃, ClickType.SWAP);
               return true;
            }
         }
      }

      return false;
   }

   @Override
   public void onGuiClosed() {
      if (this.mc.player != null) {
         this.inventorySlots.onContainerClosed(this.mc.player);
      }
   }

   @Override
   public boolean doesGuiPauseGame() {
      return false;
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
      if (!this.mc.player.isEntityAlive() || this.mc.player.isDead) {
         this.mc.player.closeScreen();
      }
   }
}
