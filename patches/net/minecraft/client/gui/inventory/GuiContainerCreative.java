package net.minecraft.client.gui.inventory;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.CreativeSettings;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.HotbarSnapshot;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiContainerCreative extends InventoryEffectRenderer {
   private static final ResourceLocation CREATIVE_INVENTORY_TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");
   private static final InventoryBasic basicInventory = new InventoryBasic("tmp", true, 45);
   private static int selectedTabIndex = CreativeTabs.BUILDING_BLOCKS.getIndex();
   private float currentScroll;
   private boolean isScrolling;
   private boolean wasClicking;
   private GuiTextField searchField;
   private List<Slot> originalSlots;
   private Slot destroyItemSlot;
   private boolean clearSearch;
   private CreativeCrafting listener;

   public GuiContainerCreative(EntityPlayer var1) {
      super(new GuiContainerCreative.ContainerCreative(☃));
      ☃.openContainer = this.inventorySlots;
      this.allowUserInput = true;
      this.ySize = 136;
      this.xSize = 195;
   }

   @Override
   public void updateScreen() {
      if (!this.mc.playerController.isInCreativeMode()) {
         this.mc.displayGuiScreen(new GuiInventory(this.mc.player));
      }
   }

   @Override
   protected void handleMouseClick(@Nullable Slot var1, int var2, int var3, ClickType var4) {
      this.clearSearch = true;
      boolean ☃ = ☃ == ClickType.QUICK_MOVE;
      ☃ = ☃ == -999 && ☃ == ClickType.PICKUP ? ClickType.THROW : ☃;
      if (☃ == null && selectedTabIndex != CreativeTabs.INVENTORY.getIndex() && ☃ != ClickType.QUICK_CRAFT) {
         InventoryPlayer ☃x = this.mc.player.inventory;
         if (!☃x.getItemStack().isEmpty()) {
            if (☃ == 0) {
               this.mc.player.dropItem(☃x.getItemStack(), true);
               this.mc.playerController.sendPacketDropItem(☃x.getItemStack());
               ☃x.setItemStack(ItemStack.EMPTY);
            }

            if (☃ == 1) {
               ItemStack ☃xx = ☃x.getItemStack().splitStack(1);
               this.mc.player.dropItem(☃xx, true);
               this.mc.playerController.sendPacketDropItem(☃xx);
            }
         }
      } else {
         if (☃ != null && !☃.canTakeStack(this.mc.player)) {
            return;
         }

         if (☃ == this.destroyItemSlot && ☃) {
            for (int ☃x = 0; ☃x < this.mc.player.inventoryContainer.getInventory().size(); ☃x++) {
               this.mc.playerController.sendSlotPacket(ItemStack.EMPTY, ☃x);
            }
         } else if (selectedTabIndex == CreativeTabs.INVENTORY.getIndex()) {
            if (☃ == this.destroyItemSlot) {
               this.mc.player.inventory.setItemStack(ItemStack.EMPTY);
            } else if (☃ == ClickType.THROW && ☃ != null && ☃.getHasStack()) {
               ItemStack ☃x = ☃.decrStackSize(☃ == 0 ? 1 : ☃.getStack().getMaxStackSize());
               ItemStack ☃xx = ☃.getStack();
               this.mc.player.dropItem(☃x, true);
               this.mc.playerController.sendPacketDropItem(☃x);
               this.mc.playerController.sendSlotPacket(☃xx, ((GuiContainerCreative.CreativeSlot)☃).slot.slotNumber);
            } else if (☃ == ClickType.THROW && !this.mc.player.inventory.getItemStack().isEmpty()) {
               this.mc.player.dropItem(this.mc.player.inventory.getItemStack(), true);
               this.mc.playerController.sendPacketDropItem(this.mc.player.inventory.getItemStack());
               this.mc.player.inventory.setItemStack(ItemStack.EMPTY);
            } else {
               this.mc.player.inventoryContainer.slotClick(☃ == null ? ☃ : ((GuiContainerCreative.CreativeSlot)☃).slot.slotNumber, ☃, ☃, this.mc.player);
               this.mc.player.inventoryContainer.detectAndSendChanges();
            }
         } else if (☃ != ClickType.QUICK_CRAFT && ☃.inventory == basicInventory) {
            InventoryPlayer ☃x = this.mc.player.inventory;
            ItemStack ☃xx = ☃x.getItemStack();
            ItemStack ☃xxx = ☃.getStack();
            if (☃ == ClickType.SWAP) {
               if (!☃xxx.isEmpty() && ☃ >= 0 && ☃ < 9) {
                  ItemStack ☃xxxx = ☃xxx.copy();
                  ☃xxxx.setCount(☃xxxx.getMaxStackSize());
                  this.mc.player.inventory.setInventorySlotContents(☃, ☃xxxx);
                  this.mc.player.inventoryContainer.detectAndSendChanges();
               }

               return;
            }

            if (☃ == ClickType.CLONE) {
               if (☃x.getItemStack().isEmpty() && ☃.getHasStack()) {
                  ItemStack ☃xxxx = ☃.getStack().copy();
                  ☃xxxx.setCount(☃xxxx.getMaxStackSize());
                  ☃x.setItemStack(☃xxxx);
               }

               return;
            }

            if (☃ == ClickType.THROW) {
               if (!☃xxx.isEmpty()) {
                  ItemStack ☃xxxx = ☃xxx.copy();
                  ☃xxxx.setCount(☃ == 0 ? 1 : ☃xxxx.getMaxStackSize());
                  this.mc.player.dropItem(☃xxxx, true);
                  this.mc.playerController.sendPacketDropItem(☃xxxx);
               }

               return;
            }

            if (!☃xx.isEmpty() && !☃xxx.isEmpty() && ☃xx.isItemEqual(☃xxx) && ItemStack.areItemStackTagsEqual(☃xx, ☃xxx)) {
               if (☃ == 0) {
                  if (☃) {
                     ☃xx.setCount(☃xx.getMaxStackSize());
                  } else if (☃xx.getCount() < ☃xx.getMaxStackSize()) {
                     ☃xx.grow(1);
                  }
               } else {
                  ☃xx.shrink(1);
               }
            } else if (!☃xxx.isEmpty() && ☃xx.isEmpty()) {
               ☃x.setItemStack(☃xxx.copy());
               ☃xx = ☃x.getItemStack();
               if (☃) {
                  ☃xx.setCount(☃xx.getMaxStackSize());
               }
            } else if (☃ == 0) {
               ☃x.setItemStack(ItemStack.EMPTY);
            } else {
               ☃x.getItemStack().shrink(1);
            }
         } else if (this.inventorySlots != null) {
            ItemStack ☃xxxx = ☃ == null ? ItemStack.EMPTY : this.inventorySlots.getSlot(☃.slotNumber).getStack();
            this.inventorySlots.slotClick(☃ == null ? ☃ : ☃.slotNumber, ☃, ☃, this.mc.player);
            if (Container.getDragEvent(☃) == 2) {
               for (int ☃xxxxx = 0; ☃xxxxx < 9; ☃xxxxx++) {
                  this.mc.playerController.sendSlotPacket(this.inventorySlots.getSlot(45 + ☃xxxxx).getStack(), 36 + ☃xxxxx);
               }
            } else if (☃ != null) {
               ItemStack ☃xxxxx = this.inventorySlots.getSlot(☃.slotNumber).getStack();
               this.mc.playerController.sendSlotPacket(☃xxxxx, ☃.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
               int ☃xxxxxx = 45 + ☃;
               if (☃ == ClickType.SWAP) {
                  this.mc.playerController.sendSlotPacket(☃xxxx, ☃xxxxxx - this.inventorySlots.inventorySlots.size() + 9 + 36);
               } else if (☃ == ClickType.THROW && !☃xxxx.isEmpty()) {
                  ItemStack ☃xxxxxxx = ☃xxxx.copy();
                  ☃xxxxxxx.setCount(☃ == 0 ? 1 : ☃xxxxxxx.getMaxStackSize());
                  this.mc.player.dropItem(☃xxxxxxx, true);
                  this.mc.playerController.sendPacketDropItem(☃xxxxxxx);
               }

               this.mc.player.inventoryContainer.detectAndSendChanges();
            }
         }
      }
   }

   @Override
   protected void updateActivePotionEffects() {
      int ☃ = this.guiLeft;
      super.updateActivePotionEffects();
      if (this.searchField != null && this.guiLeft != ☃) {
         this.searchField.x = this.guiLeft + 82;
      }
   }

   @Override
   public void initGui() {
      if (this.mc.playerController.isInCreativeMode()) {
         super.initGui();
         this.buttonList.clear();
         Keyboard.enableRepeatEvents(true);
         this.searchField = new GuiTextField(0, this.fontRenderer, this.guiLeft + 82, this.guiTop + 6, 80, this.fontRenderer.FONT_HEIGHT);
         this.searchField.setMaxStringLength(50);
         this.searchField.setEnableBackgroundDrawing(false);
         this.searchField.setVisible(false);
         this.searchField.setTextColor(16777215);
         int ☃ = selectedTabIndex;
         selectedTabIndex = -1;
         this.setCurrentCreativeTab(CreativeTabs.CREATIVE_TAB_ARRAY[☃]);
         this.listener = new CreativeCrafting(this.mc);
         this.mc.player.inventoryContainer.addListener(this.listener);
      } else {
         this.mc.displayGuiScreen(new GuiInventory(this.mc.player));
      }
   }

   @Override
   public void onGuiClosed() {
      super.onGuiClosed();
      if (this.mc.player != null && this.mc.player.inventory != null) {
         this.mc.player.inventoryContainer.removeListener(this.listener);
      }

      Keyboard.enableRepeatEvents(false);
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      if (selectedTabIndex != CreativeTabs.SEARCH.getIndex()) {
         if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindChat)) {
            this.setCurrentCreativeTab(CreativeTabs.SEARCH);
         } else {
            super.keyTyped(☃, ☃);
         }
      } else {
         if (this.clearSearch) {
            this.clearSearch = false;
            this.searchField.setText("");
         }

         if (!this.checkHotbarKeys(☃)) {
            if (this.searchField.textboxKeyTyped(☃, ☃)) {
               this.updateCreativeSearch();
            } else {
               super.keyTyped(☃, ☃);
            }
         }
      }
   }

   private void updateCreativeSearch() {
      GuiContainerCreative.ContainerCreative ☃ = (GuiContainerCreative.ContainerCreative)this.inventorySlots;
      ☃.itemList.clear();
      if (this.searchField.getText().isEmpty()) {
         for (Item ☃x : Item.REGISTRY) {
            ☃x.getSubItems(CreativeTabs.SEARCH, ☃.itemList);
         }
      } else {
         ☃.itemList.addAll(this.mc.getSearchTree(SearchTreeManager.ITEMS).search(this.searchField.getText().toLowerCase(Locale.ROOT)));
      }

      this.currentScroll = 0.0F;
      ☃.scrollTo(0.0F);
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      CreativeTabs ☃ = CreativeTabs.CREATIVE_TAB_ARRAY[selectedTabIndex];
      if (☃.drawInForegroundOfTab()) {
         GlStateManager.disableBlend();
         this.fontRenderer.drawString(I18n.format(☃.getTranslationKey()), 8, 6, 4210752);
      }
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      if (☃ == 0) {
         int ☃ = ☃ - this.guiLeft;
         int ☃x = ☃ - this.guiTop;

         for (CreativeTabs ☃xx : CreativeTabs.CREATIVE_TAB_ARRAY) {
            if (this.isMouseOverTab(☃xx, ☃, ☃x)) {
               return;
            }
         }
      }

      super.mouseClicked(☃, ☃, ☃);
   }

   @Override
   protected void mouseReleased(int var1, int var2, int var3) {
      if (☃ == 0) {
         int ☃ = ☃ - this.guiLeft;
         int ☃x = ☃ - this.guiTop;

         for (CreativeTabs ☃xx : CreativeTabs.CREATIVE_TAB_ARRAY) {
            if (this.isMouseOverTab(☃xx, ☃, ☃x)) {
               this.setCurrentCreativeTab(☃xx);
               return;
            }
         }
      }

      super.mouseReleased(☃, ☃, ☃);
   }

   private boolean needsScrollBars() {
      return selectedTabIndex != CreativeTabs.INVENTORY.getIndex()
         && CreativeTabs.CREATIVE_TAB_ARRAY[selectedTabIndex].hasScrollbar()
         && ((GuiContainerCreative.ContainerCreative)this.inventorySlots).canScroll();
   }

   private void setCurrentCreativeTab(CreativeTabs var1) {
      int ☃ = selectedTabIndex;
      selectedTabIndex = ☃.getIndex();
      GuiContainerCreative.ContainerCreative ☃x = (GuiContainerCreative.ContainerCreative)this.inventorySlots;
      this.dragSplittingSlots.clear();
      ☃x.itemList.clear();
      if (☃ == CreativeTabs.HOTBAR) {
         for (int ☃xx = 0; ☃xx < 9; ☃xx++) {
            HotbarSnapshot ☃xxx = this.mc.creativeSettings.getHotbarSnapshot(☃xx);
            if (☃xxx.isEmpty()) {
               for (int ☃xxxx = 0; ☃xxxx < 9; ☃xxxx++) {
                  if (☃xxxx == ☃xx) {
                     ItemStack ☃xxxxx = new ItemStack(Items.PAPER);
                     ☃xxxxx.getOrCreateSubCompound("CustomCreativeLock");
                     String ☃xxxxxx = GameSettings.getKeyDisplayString(this.mc.gameSettings.keyBindsHotbar[☃xx].getKeyCode());
                     String ☃xxxxxxx = GameSettings.getKeyDisplayString(this.mc.gameSettings.keyBindSaveToolbar.getKeyCode());
                     ☃xxxxx.setStackDisplayName(new TextComponentTranslation("inventory.hotbarInfo", ☃xxxxxxx, ☃xxxxxx).getUnformattedText());
                     ☃x.itemList.add(☃xxxxx);
                  } else {
                     ☃x.itemList.add(ItemStack.EMPTY);
                  }
               }
            } else {
               ☃x.itemList.addAll(☃xxx);
            }
         }
      } else if (☃ != CreativeTabs.SEARCH) {
         ☃.displayAllRelevantItems(☃x.itemList);
      }

      if (☃ == CreativeTabs.INVENTORY) {
         Container ☃xxx = this.mc.player.inventoryContainer;
         if (this.originalSlots == null) {
            this.originalSlots = ☃x.inventorySlots;
         }

         ☃x.inventorySlots = Lists.newArrayList();

         for (int ☃xxxxx = 0; ☃xxxxx < ☃xxx.inventorySlots.size(); ☃xxxxx++) {
            Slot ☃xxxxxx = new GuiContainerCreative.CreativeSlot(☃xxx.inventorySlots.get(☃xxxxx), ☃xxxxx);
            ☃x.inventorySlots.add(☃xxxxxx);
            if (☃xxxxx >= 5 && ☃xxxxx < 9) {
               int ☃xxxxxxx = ☃xxxxx - 5;
               int ☃xxxxxxxx = ☃xxxxxxx / 2;
               int ☃xxxxxxxxx = ☃xxxxxxx % 2;
               ☃xxxxxx.xPos = 54 + ☃xxxxxxxx * 54;
               ☃xxxxxx.yPos = 6 + ☃xxxxxxxxx * 27;
            } else if (☃xxxxx >= 0 && ☃xxxxx < 5) {
               ☃xxxxxx.xPos = -2000;
               ☃xxxxxx.yPos = -2000;
            } else if (☃xxxxx == 45) {
               ☃xxxxxx.xPos = 35;
               ☃xxxxxx.yPos = 20;
            } else if (☃xxxxx < ☃xxx.inventorySlots.size()) {
               int ☃xxxxxxx = ☃xxxxx - 9;
               int ☃xxxxxxxx = ☃xxxxxxx % 9;
               int ☃xxxxxxxxx = ☃xxxxxxx / 9;
               ☃xxxxxx.xPos = 9 + ☃xxxxxxxx * 18;
               if (☃xxxxx >= 36) {
                  ☃xxxxxx.yPos = 112;
               } else {
                  ☃xxxxxx.yPos = 54 + ☃xxxxxxxxx * 18;
               }
            }
         }

         this.destroyItemSlot = new Slot(basicInventory, 0, 173, 112);
         ☃x.inventorySlots.add(this.destroyItemSlot);
      } else if (☃ == CreativeTabs.INVENTORY.getIndex()) {
         ☃x.inventorySlots = this.originalSlots;
         this.originalSlots = null;
      }

      if (this.searchField != null) {
         if (☃ == CreativeTabs.SEARCH) {
            this.searchField.setVisible(true);
            this.searchField.setCanLoseFocus(false);
            this.searchField.setFocused(true);
            this.searchField.setText("");
            this.updateCreativeSearch();
         } else {
            this.searchField.setVisible(false);
            this.searchField.setCanLoseFocus(true);
            this.searchField.setFocused(false);
         }
      }

      this.currentScroll = 0.0F;
      ☃x.scrollTo(0.0F);
   }

   @Override
   public void handleMouseInput() {
      super.handleMouseInput();
      int ☃ = Mouse.getEventDWheel();
      if (☃ != 0 && this.needsScrollBars()) {
         int ☃x = (((GuiContainerCreative.ContainerCreative)this.inventorySlots).itemList.size() + 9 - 1) / 9 - 5;
         if (☃ > 0) {
            ☃ = 1;
         }

         if (☃ < 0) {
            ☃ = -1;
         }

         this.currentScroll = (float)(this.currentScroll - (double)☃ / ☃x);
         this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
         ((GuiContainerCreative.ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      boolean ☃ = Mouse.isButtonDown(0);
      int ☃x = this.guiLeft;
      int ☃xx = this.guiTop;
      int ☃xxx = ☃x + 175;
      int ☃xxxx = ☃xx + 18;
      int ☃xxxxx = ☃xxx + 14;
      int ☃xxxxxx = ☃xxxx + 112;
      if (!this.wasClicking && ☃ && ☃ >= ☃xxx && ☃ >= ☃xxxx && ☃ < ☃xxxxx && ☃ < ☃xxxxxx) {
         this.isScrolling = this.needsScrollBars();
      }

      if (!☃) {
         this.isScrolling = false;
      }

      this.wasClicking = ☃;
      if (this.isScrolling) {
         this.currentScroll = (☃ - ☃xxxx - 7.5F) / (☃xxxxxx - ☃xxxx - 15.0F);
         this.currentScroll = MathHelper.clamp(this.currentScroll, 0.0F, 1.0F);
         ((GuiContainerCreative.ContainerCreative)this.inventorySlots).scrollTo(this.currentScroll);
      }

      super.drawScreen(☃, ☃, ☃);

      for (CreativeTabs ☃xxxxxxx : CreativeTabs.CREATIVE_TAB_ARRAY) {
         if (this.renderCreativeInventoryHoveringText(☃xxxxxxx, ☃, ☃)) {
            break;
         }
      }

      if (this.destroyItemSlot != null
         && selectedTabIndex == CreativeTabs.INVENTORY.getIndex()
         && this.isPointInRegion(this.destroyItemSlot.xPos, this.destroyItemSlot.yPos, 16, 16, ☃, ☃)) {
         this.drawHoveringText(I18n.format("inventory.binSlot"), ☃, ☃);
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableLighting();
      this.renderHoveredToolTip(☃, ☃);
   }

   @Override
   protected void renderToolTip(ItemStack var1, int var2, int var3) {
      if (selectedTabIndex == CreativeTabs.SEARCH.getIndex()) {
         List<String> ☃ = ☃.getTooltip(
            this.mc.player, this.mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL
         );
         CreativeTabs ☃x = ☃.getItem().getCreativeTab();
         if (☃x == null && ☃.getItem() == Items.ENCHANTED_BOOK) {
            Map<Enchantment, Integer> ☃xx = EnchantmentHelper.getEnchantments(☃);
            if (☃xx.size() == 1) {
               Enchantment ☃xxx = ☃xx.keySet().iterator().next();

               for (CreativeTabs ☃xxxx : CreativeTabs.CREATIVE_TAB_ARRAY) {
                  if (☃xxxx.hasRelevantEnchantmentType(☃xxx.type)) {
                     ☃x = ☃xxxx;
                     break;
                  }
               }
            }
         }

         if (☃x != null) {
            ☃.add(1, "" + TextFormatting.BOLD + TextFormatting.BLUE + I18n.format(☃x.getTranslationKey()));
         }

         for (int ☃xx = 0; ☃xx < ☃.size(); ☃xx++) {
            if (☃xx == 0) {
               ☃.set(☃xx, ☃.getRarity().color + ☃.get(☃xx));
            } else {
               ☃.set(☃xx, TextFormatting.GRAY + ☃.get(☃xx));
            }
         }

         this.drawHoveringText(☃, ☃, ☃);
      } else {
         super.renderToolTip(☃, ☃, ☃);
      }
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      RenderHelper.enableGUIStandardItemLighting();
      CreativeTabs ☃ = CreativeTabs.CREATIVE_TAB_ARRAY[selectedTabIndex];

      for (CreativeTabs ☃x : CreativeTabs.CREATIVE_TAB_ARRAY) {
         this.mc.getTextureManager().bindTexture(CREATIVE_INVENTORY_TABS);
         if (☃x.getIndex() != selectedTabIndex) {
            this.drawTab(☃x);
         }
      }

      this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/creative_inventory/tab_" + ☃.getBackgroundImageName()));
      this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
      this.searchField.drawTextBox();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      int ☃xx = this.guiLeft + 175;
      int ☃xxx = this.guiTop + 18;
      int ☃xxxx = ☃xxx + 112;
      this.mc.getTextureManager().bindTexture(CREATIVE_INVENTORY_TABS);
      if (☃.hasScrollbar()) {
         this.drawTexturedModalRect(☃xx, ☃xxx + (int)((☃xxxx - ☃xxx - 17) * this.currentScroll), 232 + (this.needsScrollBars() ? 0 : 12), 0, 12, 15);
      }

      this.drawTab(☃);
      if (☃ == CreativeTabs.INVENTORY) {
         GuiInventory.drawEntityOnScreen(this.guiLeft + 88, this.guiTop + 45, 20, this.guiLeft + 88 - ☃, this.guiTop + 45 - 30 - ☃, this.mc.player);
      }
   }

   protected boolean isMouseOverTab(CreativeTabs var1, int var2, int var3) {
      int ☃ = ☃.getColumn();
      int ☃x = 28 * ☃;
      int ☃xx = 0;
      if (☃.isAlignedRight()) {
         ☃x = this.xSize - 28 * (6 - ☃) + 2;
      } else if (☃ > 0) {
         ☃x += ☃;
      }

      if (☃.isOnTopRow()) {
         ☃xx -= 32;
      } else {
         ☃xx += this.ySize;
      }

      return ☃ >= ☃x && ☃ <= ☃x + 28 && ☃ >= ☃xx && ☃ <= ☃xx + 32;
   }

   protected boolean renderCreativeInventoryHoveringText(CreativeTabs var1, int var2, int var3) {
      int ☃ = ☃.getColumn();
      int ☃x = 28 * ☃;
      int ☃xx = 0;
      if (☃.isAlignedRight()) {
         ☃x = this.xSize - 28 * (6 - ☃) + 2;
      } else if (☃ > 0) {
         ☃x += ☃;
      }

      if (☃.isOnTopRow()) {
         ☃xx -= 32;
      } else {
         ☃xx += this.ySize;
      }

      if (this.isPointInRegion(☃x + 3, ☃xx + 3, 23, 27, ☃, ☃)) {
         this.drawHoveringText(I18n.format(☃.getTranslationKey()), ☃, ☃);
         return true;
      } else {
         return false;
      }
   }

   protected void drawTab(CreativeTabs var1) {
      boolean ☃ = ☃.getIndex() == selectedTabIndex;
      boolean ☃x = ☃.isOnTopRow();
      int ☃xx = ☃.getColumn();
      int ☃xxx = ☃xx * 28;
      int ☃xxxx = 0;
      int ☃xxxxx = this.guiLeft + 28 * ☃xx;
      int ☃xxxxxx = this.guiTop;
      int ☃xxxxxxx = 32;
      if (☃) {
         ☃xxxx += 32;
      }

      if (☃.isAlignedRight()) {
         ☃xxxxx = this.guiLeft + this.xSize - 28 * (6 - ☃xx);
      } else if (☃xx > 0) {
         ☃xxxxx += ☃xx;
      }

      if (☃x) {
         ☃xxxxxx -= 28;
      } else {
         ☃xxxx += 64;
         ☃xxxxxx += this.ySize - 4;
      }

      GlStateManager.disableLighting();
      this.drawTexturedModalRect(☃xxxxx, ☃xxxxxx, ☃xxx, ☃xxxx, 28, 32);
      this.zLevel = 100.0F;
      this.itemRender.zLevel = 100.0F;
      ☃xxxxx += 6;
      ☃xxxxxx += 8 + (☃x ? 1 : -1);
      GlStateManager.enableLighting();
      GlStateManager.enableRescaleNormal();
      ItemStack ☃xxxxxxxx = ☃.getIcon();
      this.itemRender.renderItemAndEffectIntoGUI(☃xxxxxxxx, ☃xxxxx, ☃xxxxxx);
      this.itemRender.renderItemOverlays(this.fontRenderer, ☃xxxxxxxx, ☃xxxxx, ☃xxxxxx);
      GlStateManager.disableLighting();
      this.itemRender.zLevel = 0.0F;
      this.zLevel = 0.0F;
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.id == 1) {
         this.mc.displayGuiScreen(new GuiStats(this, this.mc.player.getStatFileWriter()));
      }
   }

   public int getSelectedTabIndex() {
      return selectedTabIndex;
   }

   public static void handleHotbarSnapshots(Minecraft var0, int var1, boolean var2, boolean var3) {
      EntityPlayerSP ☃ = ☃.player;
      CreativeSettings ☃x = ☃.creativeSettings;
      HotbarSnapshot ☃xx = ☃x.getHotbarSnapshot(☃);
      if (☃) {
         for (int ☃xxx = 0; ☃xxx < InventoryPlayer.getHotbarSize(); ☃xxx++) {
            ItemStack ☃xxxx = ☃xx.get(☃xxx).copy();
            ☃.inventory.setInventorySlotContents(☃xxx, ☃xxxx);
            ☃.playerController.sendSlotPacket(☃xxxx, 36 + ☃xxx);
         }

         ☃.inventoryContainer.detectAndSendChanges();
      } else if (☃) {
         for (int ☃xxx = 0; ☃xxx < InventoryPlayer.getHotbarSize(); ☃xxx++) {
            ☃xx.set(☃xxx, ☃.inventory.getStackInSlot(☃xxx).copy());
         }

         String ☃xxx = GameSettings.getKeyDisplayString(☃.gameSettings.keyBindsHotbar[☃].getKeyCode());
         String ☃xxxx = GameSettings.getKeyDisplayString(☃.gameSettings.keyBindLoadToolbar.getKeyCode());
         ☃.ingameGUI.setOverlayMessage(new TextComponentTranslation("inventory.hotbarSaved", ☃xxxx, ☃xxx), false);
         ☃x.write();
      }
   }

   public static class ContainerCreative extends Container {
      public NonNullList<ItemStack> itemList = NonNullList.create();

      public ContainerCreative(EntityPlayer var1) {
         InventoryPlayer ☃ = ☃.inventory;

         for (int ☃x = 0; ☃x < 5; ☃x++) {
            for (int ☃xx = 0; ☃xx < 9; ☃xx++) {
               this.addSlotToContainer(new GuiContainerCreative.LockedSlot(GuiContainerCreative.basicInventory, ☃x * 9 + ☃xx, 9 + ☃xx * 18, 18 + ☃x * 18));
            }
         }

         for (int ☃x = 0; ☃x < 9; ☃x++) {
            this.addSlotToContainer(new Slot(☃, ☃x, 9 + ☃x * 18, 112));
         }

         this.scrollTo(0.0F);
      }

      @Override
      public boolean canInteractWith(EntityPlayer var1) {
         return true;
      }

      public void scrollTo(float var1) {
         int ☃ = (this.itemList.size() + 9 - 1) / 9 - 5;
         int ☃x = (int)(☃ * ☃ + 0.5);
         if (☃x < 0) {
            ☃x = 0;
         }

         for (int ☃xx = 0; ☃xx < 5; ☃xx++) {
            for (int ☃xxx = 0; ☃xxx < 9; ☃xxx++) {
               int ☃xxxx = ☃xxx + (☃xx + ☃x) * 9;
               if (☃xxxx >= 0 && ☃xxxx < this.itemList.size()) {
                  GuiContainerCreative.basicInventory.setInventorySlotContents(☃xxx + ☃xx * 9, this.itemList.get(☃xxxx));
               } else {
                  GuiContainerCreative.basicInventory.setInventorySlotContents(☃xxx + ☃xx * 9, ItemStack.EMPTY);
               }
            }
         }
      }

      public boolean canScroll() {
         return this.itemList.size() > 45;
      }

      @Override
      public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
         if (☃ >= this.inventorySlots.size() - 9 && ☃ < this.inventorySlots.size()) {
            Slot ☃ = this.inventorySlots.get(☃);
            if (☃ != null && ☃.getHasStack()) {
               ☃.putStack(ItemStack.EMPTY);
            }
         }

         return ItemStack.EMPTY;
      }

      @Override
      public boolean canMergeSlot(ItemStack var1, Slot var2) {
         return ☃.yPos > 90;
      }

      @Override
      public boolean canDragIntoSlot(Slot var1) {
         return ☃.inventory instanceof InventoryPlayer || ☃.yPos > 90 && ☃.xPos <= 162;
      }
   }

   class CreativeSlot extends Slot {
      private final Slot slot;

      public CreativeSlot(Slot var2, int var3) {
         super(☃.inventory, ☃, 0, 0);
         this.slot = ☃;
      }

      @Override
      public ItemStack onTake(EntityPlayer var1, ItemStack var2) {
         this.slot.onTake(☃, ☃);
         return ☃;
      }

      @Override
      public boolean isItemValid(ItemStack var1) {
         return this.slot.isItemValid(☃);
      }

      @Override
      public ItemStack getStack() {
         return this.slot.getStack();
      }

      @Override
      public boolean getHasStack() {
         return this.slot.getHasStack();
      }

      @Override
      public void putStack(ItemStack var1) {
         this.slot.putStack(☃);
      }

      @Override
      public void onSlotChanged() {
         this.slot.onSlotChanged();
      }

      @Override
      public int getSlotStackLimit() {
         return this.slot.getSlotStackLimit();
      }

      @Override
      public int getItemStackLimit(ItemStack var1) {
         return this.slot.getItemStackLimit(☃);
      }

      @Nullable
      @Override
      public String getSlotTexture() {
         return this.slot.getSlotTexture();
      }

      @Override
      public ItemStack decrStackSize(int var1) {
         return this.slot.decrStackSize(☃);
      }

      @Override
      public boolean isHere(IInventory var1, int var2) {
         return this.slot.isHere(☃, ☃);
      }

      @Override
      public boolean isEnabled() {
         return this.slot.isEnabled();
      }

      @Override
      public boolean canTakeStack(EntityPlayer var1) {
         return this.slot.canTakeStack(☃);
      }
   }

   static class LockedSlot extends Slot {
      public LockedSlot(IInventory var1, int var2, int var3, int var4) {
         super(☃, ☃, ☃, ☃);
      }

      @Override
      public boolean canTakeStack(EntityPlayer var1) {
         return super.canTakeStack(☃) && this.getHasStack() ? this.getStack().getSubCompound("CustomCreativeLock") == null : !this.getHasStack();
      }
   }
}
