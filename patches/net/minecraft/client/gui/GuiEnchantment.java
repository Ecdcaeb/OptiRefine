package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnchantmentNameParts;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import org.lwjgl.util.glu.Project;

public class GuiEnchantment extends GuiContainer {
   private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/enchanting_table.png");
   private static final ResourceLocation ENCHANTMENT_TABLE_BOOK_TEXTURE = new ResourceLocation("textures/entity/enchanting_table_book.png");
   private static final ModelBook MODEL_BOOK = new ModelBook();
   private final InventoryPlayer playerInventory;
   private final Random random = new Random();
   private final ContainerEnchantment container;
   public int ticks;
   public float flip;
   public float oFlip;
   public float flipT;
   public float flipA;
   public float open;
   public float oOpen;
   private ItemStack last = ItemStack.EMPTY;
   private final IWorldNameable nameable;

   public GuiEnchantment(InventoryPlayer var1, World var2, IWorldNameable var3) {
      super(new ContainerEnchantment(☃, ☃));
      this.playerInventory = ☃;
      this.container = (ContainerEnchantment)this.inventorySlots;
      this.nameable = ☃;
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int var1, int var2) {
      this.fontRenderer.drawString(this.nameable.getDisplayName().getUnformattedText(), 12, 5, 4210752);
      this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
      this.tickBook();
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(☃, ☃, ☃);
      int ☃ = (this.width - this.xSize) / 2;
      int ☃x = (this.height - this.ySize) / 2;

      for (int ☃xx = 0; ☃xx < 3; ☃xx++) {
         int ☃xxx = ☃ - (☃ + 60);
         int ☃xxxx = ☃ - (☃x + 14 + 19 * ☃xx);
         if (☃xxx >= 0 && ☃xxxx >= 0 && ☃xxx < 108 && ☃xxxx < 19 && this.container.enchantItem(this.mc.player, ☃xx)) {
            this.mc.playerController.sendEnchantPacket(this.container.windowId, ☃xx);
         }
      }
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
      int ☃ = (this.width - this.xSize) / 2;
      int ☃x = (this.height - this.ySize) / 2;
      this.drawTexturedModalRect(☃, ☃x, 0, 0, this.xSize, this.ySize);
      GlStateManager.pushMatrix();
      GlStateManager.matrixMode(5889);
      GlStateManager.pushMatrix();
      GlStateManager.loadIdentity();
      ScaledResolution ☃xx = new ScaledResolution(this.mc);
      GlStateManager.viewport(
         (☃xx.getScaledWidth() - 320) / 2 * ☃xx.getScaleFactor(),
         (☃xx.getScaledHeight() - 240) / 2 * ☃xx.getScaleFactor(),
         320 * ☃xx.getScaleFactor(),
         240 * ☃xx.getScaleFactor()
      );
      GlStateManager.translate(-0.34F, 0.23F, 0.0F);
      Project.gluPerspective(90.0F, 1.3333334F, 9.0F, 80.0F);
      float ☃xxx = 1.0F;
      GlStateManager.matrixMode(5888);
      GlStateManager.loadIdentity();
      RenderHelper.enableStandardItemLighting();
      GlStateManager.translate(0.0F, 3.3F, -16.0F);
      GlStateManager.scale(1.0F, 1.0F, 1.0F);
      float ☃xxxx = 5.0F;
      GlStateManager.scale(5.0F, 5.0F, 5.0F);
      GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_BOOK_TEXTURE);
      GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
      float ☃xxxxx = this.oOpen + (this.open - this.oOpen) * ☃;
      GlStateManager.translate((1.0F - ☃xxxxx) * 0.2F, (1.0F - ☃xxxxx) * 0.1F, (1.0F - ☃xxxxx) * 0.25F);
      GlStateManager.rotate(-(1.0F - ☃xxxxx) * 90.0F - 90.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
      float ☃xxxxxx = this.oFlip + (this.flip - this.oFlip) * ☃ + 0.25F;
      float ☃xxxxxxx = this.oFlip + (this.flip - this.oFlip) * ☃ + 0.75F;
      ☃xxxxxx = (☃xxxxxx - MathHelper.fastFloor(☃xxxxxx)) * 1.6F - 0.3F;
      ☃xxxxxxx = (☃xxxxxxx - MathHelper.fastFloor(☃xxxxxxx)) * 1.6F - 0.3F;
      if (☃xxxxxx < 0.0F) {
         ☃xxxxxx = 0.0F;
      }

      if (☃xxxxxxx < 0.0F) {
         ☃xxxxxxx = 0.0F;
      }

      if (☃xxxxxx > 1.0F) {
         ☃xxxxxx = 1.0F;
      }

      if (☃xxxxxxx > 1.0F) {
         ☃xxxxxxx = 1.0F;
      }

      GlStateManager.enableRescaleNormal();
      MODEL_BOOK.render(null, 0.0F, ☃xxxxxx, ☃xxxxxxx, ☃xxxxx, 0.0F, 0.0625F);
      GlStateManager.disableRescaleNormal();
      RenderHelper.disableStandardItemLighting();
      GlStateManager.matrixMode(5889);
      GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
      GlStateManager.popMatrix();
      GlStateManager.matrixMode(5888);
      GlStateManager.popMatrix();
      RenderHelper.disableStandardItemLighting();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      EnchantmentNameParts.getInstance().reseedRandomGenerator(this.container.xpSeed);
      int ☃xxxxxxxx = this.container.getLapisAmount();

      for (int ☃xxxxxxxxx = 0; ☃xxxxxxxxx < 3; ☃xxxxxxxxx++) {
         int ☃xxxxxxxxxx = ☃ + 60;
         int ☃xxxxxxxxxxx = ☃xxxxxxxxxx + 20;
         this.zLevel = 0.0F;
         this.mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
         int ☃xxxxxxxxxxxx = this.container.enchantLevels[☃xxxxxxxxx];
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         if (☃xxxxxxxxxxxx == 0) {
            this.drawTexturedModalRect(☃xxxxxxxxxx, ☃x + 14 + 19 * ☃xxxxxxxxx, 0, 185, 108, 19);
         } else {
            String ☃xxxxxxxxxxxxx = "" + ☃xxxxxxxxxxxx;
            int ☃xxxxxxxxxxxxxx = 86 - this.fontRenderer.getStringWidth(☃xxxxxxxxxxxxx);
            String ☃xxxxxxxxxxxxxxx = EnchantmentNameParts.getInstance().generateNewRandomName(this.fontRenderer, ☃xxxxxxxxxxxxxx);
            FontRenderer ☃xxxxxxxxxxxxxxxx = this.mc.standardGalacticFontRenderer;
            int ☃xxxxxxxxxxxxxxxxx = 6839882;
            if ((☃xxxxxxxx < ☃xxxxxxxxx + 1 || this.mc.player.experienceLevel < ☃xxxxxxxxxxxx) && !this.mc.player.capabilities.isCreativeMode) {
               this.drawTexturedModalRect(☃xxxxxxxxxx, ☃x + 14 + 19 * ☃xxxxxxxxx, 0, 185, 108, 19);
               this.drawTexturedModalRect(☃xxxxxxxxxx + 1, ☃x + 15 + 19 * ☃xxxxxxxxx, 16 * ☃xxxxxxxxx, 239, 16, 16);
               ☃xxxxxxxxxxxxxxxx.drawSplitString(
                  ☃xxxxxxxxxxxxxxx, ☃xxxxxxxxxxx, ☃x + 16 + 19 * ☃xxxxxxxxx, ☃xxxxxxxxxxxxxx, (☃xxxxxxxxxxxxxxxxx & 16711422) >> 1
               );
               ☃xxxxxxxxxxxxxxxxx = 4226832;
            } else {
               int ☃xxxxxxxxxxxxxxxxxx = ☃ - (☃ + 60);
               int ☃xxxxxxxxxxxxxxxxxxx = ☃ - (☃x + 14 + 19 * ☃xxxxxxxxx);
               if (☃xxxxxxxxxxxxxxxxxx >= 0 && ☃xxxxxxxxxxxxxxxxxxx >= 0 && ☃xxxxxxxxxxxxxxxxxx < 108 && ☃xxxxxxxxxxxxxxxxxxx < 19) {
                  this.drawTexturedModalRect(☃xxxxxxxxxx, ☃x + 14 + 19 * ☃xxxxxxxxx, 0, 204, 108, 19);
                  ☃xxxxxxxxxxxxxxxxx = 16777088;
               } else {
                  this.drawTexturedModalRect(☃xxxxxxxxxx, ☃x + 14 + 19 * ☃xxxxxxxxx, 0, 166, 108, 19);
               }

               this.drawTexturedModalRect(☃xxxxxxxxxx + 1, ☃x + 15 + 19 * ☃xxxxxxxxx, 16 * ☃xxxxxxxxx, 223, 16, 16);
               ☃xxxxxxxxxxxxxxxx.drawSplitString(☃xxxxxxxxxxxxxxx, ☃xxxxxxxxxxx, ☃x + 16 + 19 * ☃xxxxxxxxx, ☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxx);
               ☃xxxxxxxxxxxxxxxxx = 8453920;
            }

            ☃xxxxxxxxxxxxxxxx = this.mc.fontRenderer;
            ☃xxxxxxxxxxxxxxxx.drawStringWithShadow(
               ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxx + 86 - ☃xxxxxxxxxxxxxxxx.getStringWidth(☃xxxxxxxxxxxxx), ☃x + 16 + 19 * ☃xxxxxxxxx + 7, ☃xxxxxxxxxxxxxxxxx
            );
         }
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      ☃ = this.mc.getTickLength();
      this.drawDefaultBackground();
      super.drawScreen(☃, ☃, ☃);
      this.renderHoveredToolTip(☃, ☃);
      boolean ☃ = this.mc.player.capabilities.isCreativeMode;
      int ☃x = this.container.getLapisAmount();

      for (int ☃xx = 0; ☃xx < 3; ☃xx++) {
         int ☃xxx = this.container.enchantLevels[☃xx];
         Enchantment ☃xxxx = Enchantment.getEnchantmentByID(this.container.enchantClue[☃xx]);
         int ☃xxxxx = this.container.worldClue[☃xx];
         int ☃xxxxxx = ☃xx + 1;
         if (this.isPointInRegion(60, 14 + 19 * ☃xx, 108, 17, ☃, ☃) && ☃xxx > 0 && ☃xxxxx >= 0 && ☃xxxx != null) {
            List<String> ☃xxxxxxx = Lists.newArrayList();
            ☃xxxxxxx.add("" + TextFormatting.WHITE + TextFormatting.ITALIC + I18n.format("container.enchant.clue", ☃xxxx.getTranslatedName(☃xxxxx)));
            if (!☃) {
               ☃xxxxxxx.add("");
               if (this.mc.player.experienceLevel < ☃xxx) {
                  ☃xxxxxxx.add(TextFormatting.RED + I18n.format("container.enchant.level.requirement", this.container.enchantLevels[☃xx]));
               } else {
                  String ☃xxxxxxxx;
                  if (☃xxxxxx == 1) {
                     ☃xxxxxxxx = I18n.format("container.enchant.lapis.one");
                  } else {
                     ☃xxxxxxxx = I18n.format("container.enchant.lapis.many", ☃xxxxxx);
                  }

                  TextFormatting ☃xxxxxxxxx = ☃x >= ☃xxxxxx ? TextFormatting.GRAY : TextFormatting.RED;
                  ☃xxxxxxx.add(☃xxxxxxxxx + "" + ☃xxxxxxxx);
                  if (☃xxxxxx == 1) {
                     ☃xxxxxxxx = I18n.format("container.enchant.level.one");
                  } else {
                     ☃xxxxxxxx = I18n.format("container.enchant.level.many", ☃xxxxxx);
                  }

                  ☃xxxxxxx.add(TextFormatting.GRAY + "" + ☃xxxxxxxx);
               }
            }

            this.drawHoveringText(☃xxxxxxx, ☃, ☃);
            break;
         }
      }
   }

   public void tickBook() {
      ItemStack ☃ = this.inventorySlots.getSlot(0).getStack();
      if (!ItemStack.areItemStacksEqual(☃, this.last)) {
         this.last = ☃;

         do {
            this.flipT = this.flipT + (this.random.nextInt(4) - this.random.nextInt(4));
         } while (this.flip <= this.flipT + 1.0F && this.flip >= this.flipT - 1.0F);
      }

      this.ticks++;
      this.oFlip = this.flip;
      this.oOpen = this.open;
      boolean ☃x = false;

      for (int ☃xx = 0; ☃xx < 3; ☃xx++) {
         if (this.container.enchantLevels[☃xx] != 0) {
            ☃x = true;
         }
      }

      if (☃x) {
         this.open += 0.2F;
      } else {
         this.open -= 0.2F;
      }

      this.open = MathHelper.clamp(this.open, 0.0F, 1.0F);
      float ☃xxx = (this.flipT - this.flip) * 0.4F;
      float ☃xxxx = 0.2F;
      ☃xxx = MathHelper.clamp(☃xxx, -0.2F, 0.2F);
      this.flipA = this.flipA + (☃xxx - this.flipA) * 0.9F;
      this.flip = this.flip + this.flipA;
   }
}
