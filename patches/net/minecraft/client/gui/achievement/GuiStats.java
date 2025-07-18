package net.minecraft.client.gui.achievement;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityList;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatCrafting;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsManager;
import org.lwjgl.input.Mouse;

public class GuiStats extends GuiScreen implements IProgressMeter {
   protected GuiScreen parentScreen;
   protected String screenTitle = "Select world";
   private GuiStats.StatsGeneral generalStats;
   private GuiStats.StatsItem itemStats;
   private GuiStats.StatsBlock blockStats;
   private GuiStats.StatsMobsList mobStats;
   private final StatisticsManager stats;
   private GuiSlot displaySlot;
   private boolean doesGuiPauseGame = true;

   public GuiStats(GuiScreen var1, StatisticsManager var2) {
      this.parentScreen = ☃;
      this.stats = ☃;
   }

   @Override
   public void initGui() {
      this.screenTitle = I18n.format("gui.stats");
      this.doesGuiPauseGame = true;
      this.mc.getConnection().sendPacket(new CPacketClientStatus(CPacketClientStatus.State.REQUEST_STATS));
   }

   @Override
   public void handleMouseInput() {
      super.handleMouseInput();
      if (this.displaySlot != null) {
         this.displaySlot.handleMouseInput();
      }
   }

   public void initLists() {
      this.generalStats = new GuiStats.StatsGeneral(this.mc);
      this.generalStats.registerScrollButtons(1, 1);
      this.itemStats = new GuiStats.StatsItem(this.mc);
      this.itemStats.registerScrollButtons(1, 1);
      this.blockStats = new GuiStats.StatsBlock(this.mc);
      this.blockStats.registerScrollButtons(1, 1);
      this.mobStats = new GuiStats.StatsMobsList(this.mc);
      this.mobStats.registerScrollButtons(1, 1);
   }

   public void initButtons() {
      this.buttonList.add(new GuiButton(0, this.width / 2 + 4, this.height - 28, 150, 20, I18n.format("gui.done")));
      this.buttonList.add(new GuiButton(1, this.width / 2 - 160, this.height - 52, 80, 20, I18n.format("stat.generalButton")));
      GuiButton ☃ = this.addButton(new GuiButton(2, this.width / 2 - 80, this.height - 52, 80, 20, I18n.format("stat.blocksButton")));
      GuiButton ☃x = this.addButton(new GuiButton(3, this.width / 2, this.height - 52, 80, 20, I18n.format("stat.itemsButton")));
      GuiButton ☃xx = this.addButton(new GuiButton(4, this.width / 2 + 80, this.height - 52, 80, 20, I18n.format("stat.mobsButton")));
      if (this.blockStats.getSize() == 0) {
         ☃.enabled = false;
      }

      if (this.itemStats.getSize() == 0) {
         ☃x.enabled = false;
      }

      if (this.mobStats.getSize() == 0) {
         ☃xx.enabled = false;
      }
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.enabled) {
         if (☃.id == 0) {
            this.mc.displayGuiScreen(this.parentScreen);
         } else if (☃.id == 1) {
            this.displaySlot = this.generalStats;
         } else if (☃.id == 3) {
            this.displaySlot = this.itemStats;
         } else if (☃.id == 2) {
            this.displaySlot = this.blockStats;
         } else if (☃.id == 4) {
            this.displaySlot = this.mobStats;
         } else {
            this.displaySlot.actionPerformed(☃);
         }
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      if (this.doesGuiPauseGame) {
         this.drawDefaultBackground();
         this.drawCenteredString(this.fontRenderer, I18n.format("multiplayer.downloadingStats"), this.width / 2, this.height / 2, 16777215);
         this.drawCenteredString(
            this.fontRenderer,
            LOADING_STRINGS[(int)(Minecraft.getSystemTime() / 150L % LOADING_STRINGS.length)],
            this.width / 2,
            this.height / 2 + this.fontRenderer.FONT_HEIGHT * 2,
            16777215
         );
      } else {
         this.displaySlot.drawScreen(☃, ☃, ☃);
         this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 20, 16777215);
         super.drawScreen(☃, ☃, ☃);
      }
   }

   @Override
   public void onStatsUpdated() {
      if (this.doesGuiPauseGame) {
         this.initLists();
         this.initButtons();
         this.displaySlot = this.generalStats;
         this.doesGuiPauseGame = false;
      }
   }

   @Override
   public boolean doesGuiPauseGame() {
      return !this.doesGuiPauseGame;
   }

   private void drawStatsScreen(int var1, int var2, Item var3) {
      this.drawButtonBackground(☃ + 1, ☃ + 1);
      GlStateManager.enableRescaleNormal();
      RenderHelper.enableGUIStandardItemLighting();
      this.itemRender.renderItemIntoGUI(☃.getDefaultInstance(), ☃ + 2, ☃ + 2);
      RenderHelper.disableStandardItemLighting();
      GlStateManager.disableRescaleNormal();
   }

   private void drawButtonBackground(int var1, int var2) {
      this.drawSprite(☃, ☃, 0, 0);
   }

   private void drawSprite(int var1, int var2, int var3, int var4) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(STAT_ICONS);
      float ☃ = 0.0078125F;
      float ☃x = 0.0078125F;
      int ☃xx = 18;
      int ☃xxx = 18;
      Tessellator ☃xxxx = Tessellator.getInstance();
      BufferBuilder ☃xxxxx = ☃xxxx.getBuffer();
      ☃xxxxx.begin(7, DefaultVertexFormats.POSITION_TEX);
      ☃xxxxx.pos(☃ + 0, ☃ + 18, this.zLevel).tex((☃ + 0) * 0.0078125F, (☃ + 18) * 0.0078125F).endVertex();
      ☃xxxxx.pos(☃ + 18, ☃ + 18, this.zLevel).tex((☃ + 18) * 0.0078125F, (☃ + 18) * 0.0078125F).endVertex();
      ☃xxxxx.pos(☃ + 18, ☃ + 0, this.zLevel).tex((☃ + 18) * 0.0078125F, (☃ + 0) * 0.0078125F).endVertex();
      ☃xxxxx.pos(☃ + 0, ☃ + 0, this.zLevel).tex((☃ + 0) * 0.0078125F, (☃ + 0) * 0.0078125F).endVertex();
      ☃xxxx.draw();
   }

   abstract class Stats extends GuiSlot {
      protected int headerPressed = -1;
      protected List<StatCrafting> statsHolder;
      protected Comparator<StatCrafting> statSorter;
      protected int sortColumn = -1;
      protected int sortOrder;

      protected Stats(Minecraft var2) {
         super(☃, GuiStats.this.width, GuiStats.this.height, 32, GuiStats.this.height - 64, 20);
         this.setShowSelectionBox(false);
         this.setHasListHeader(true, 20);
      }

      @Override
      protected void elementClicked(int var1, boolean var2, int var3, int var4) {
      }

      @Override
      protected boolean isSelected(int var1) {
         return false;
      }

      @Override
      public int getListWidth() {
         return 375;
      }

      @Override
      protected int getScrollBarX() {
         return this.width / 2 + 140;
      }

      @Override
      protected void drawBackground() {
         GuiStats.this.drawDefaultBackground();
      }

      @Override
      protected void drawListHeader(int var1, int var2, Tessellator var3) {
         if (!Mouse.isButtonDown(0)) {
            this.headerPressed = -1;
         }

         if (this.headerPressed == 0) {
            GuiStats.this.drawSprite(☃ + 115 - 18, ☃ + 1, 0, 0);
         } else {
            GuiStats.this.drawSprite(☃ + 115 - 18, ☃ + 1, 0, 18);
         }

         if (this.headerPressed == 1) {
            GuiStats.this.drawSprite(☃ + 165 - 18, ☃ + 1, 0, 0);
         } else {
            GuiStats.this.drawSprite(☃ + 165 - 18, ☃ + 1, 0, 18);
         }

         if (this.headerPressed == 2) {
            GuiStats.this.drawSprite(☃ + 215 - 18, ☃ + 1, 0, 0);
         } else {
            GuiStats.this.drawSprite(☃ + 215 - 18, ☃ + 1, 0, 18);
         }

         if (this.headerPressed == 3) {
            GuiStats.this.drawSprite(☃ + 265 - 18, ☃ + 1, 0, 0);
         } else {
            GuiStats.this.drawSprite(☃ + 265 - 18, ☃ + 1, 0, 18);
         }

         if (this.headerPressed == 4) {
            GuiStats.this.drawSprite(☃ + 315 - 18, ☃ + 1, 0, 0);
         } else {
            GuiStats.this.drawSprite(☃ + 315 - 18, ☃ + 1, 0, 18);
         }

         if (this.sortColumn != -1) {
            int ☃ = 79;
            int ☃x = 18;
            if (this.sortColumn == 1) {
               ☃ = 129;
            } else if (this.sortColumn == 2) {
               ☃ = 179;
            } else if (this.sortColumn == 3) {
               ☃ = 229;
            } else if (this.sortColumn == 4) {
               ☃ = 279;
            }

            if (this.sortOrder == 1) {
               ☃x = 36;
            }

            GuiStats.this.drawSprite(☃ + ☃, ☃ + 1, ☃x, 0);
         }
      }

      @Override
      protected void clickedHeader(int var1, int var2) {
         this.headerPressed = -1;
         if (☃ >= 79 && ☃ < 115) {
            this.headerPressed = 0;
         } else if (☃ >= 129 && ☃ < 165) {
            this.headerPressed = 1;
         } else if (☃ >= 179 && ☃ < 215) {
            this.headerPressed = 2;
         } else if (☃ >= 229 && ☃ < 265) {
            this.headerPressed = 3;
         } else if (☃ >= 279 && ☃ < 315) {
            this.headerPressed = 4;
         }

         if (this.headerPressed >= 0) {
            this.sortByColumn(this.headerPressed);
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         }
      }

      @Override
      protected final int getSize() {
         return this.statsHolder.size();
      }

      protected final StatCrafting getSlotStat(int var1) {
         return this.statsHolder.get(☃);
      }

      protected abstract String getHeaderDescriptionId(int var1);

      protected void renderStat(StatBase var1, int var2, int var3, boolean var4) {
         if (☃ != null) {
            String ☃ = ☃.format(GuiStats.this.stats.readStat(☃));
            GuiStats.this.drawString(GuiStats.this.fontRenderer, ☃, ☃ - GuiStats.this.fontRenderer.getStringWidth(☃), ☃ + 5, ☃ ? 16777215 : 9474192);
         } else {
            String ☃ = "-";
            GuiStats.this.drawString(GuiStats.this.fontRenderer, "-", ☃ - GuiStats.this.fontRenderer.getStringWidth("-"), ☃ + 5, ☃ ? 16777215 : 9474192);
         }
      }

      @Override
      protected void renderDecorations(int var1, int var2) {
         if (☃ >= this.top && ☃ <= this.bottom) {
            int ☃ = this.getSlotIndexFromScreenCoords(☃, ☃);
            int ☃x = (this.width - this.getListWidth()) / 2;
            if (☃ >= 0) {
               if (☃ < ☃x + 40 || ☃ > ☃x + 40 + 20) {
                  return;
               }

               StatCrafting ☃xx = this.getSlotStat(☃);
               this.renderMouseHoverToolTip(☃xx, ☃, ☃);
            } else {
               String ☃xx;
               if (☃ >= ☃x + 115 - 18 && ☃ <= ☃x + 115) {
                  ☃xx = this.getHeaderDescriptionId(0);
               } else if (☃ >= ☃x + 165 - 18 && ☃ <= ☃x + 165) {
                  ☃xx = this.getHeaderDescriptionId(1);
               } else if (☃ >= ☃x + 215 - 18 && ☃ <= ☃x + 215) {
                  ☃xx = this.getHeaderDescriptionId(2);
               } else if (☃ >= ☃x + 265 - 18 && ☃ <= ☃x + 265) {
                  ☃xx = this.getHeaderDescriptionId(3);
               } else {
                  if (☃ < ☃x + 315 - 18 || ☃ > ☃x + 315) {
                     return;
                  }

                  ☃xx = this.getHeaderDescriptionId(4);
               }

               ☃xx = ("" + I18n.format(☃xx)).trim();
               if (!☃xx.isEmpty()) {
                  int ☃xxx = ☃ + 12;
                  int ☃xxxx = ☃ - 12;
                  int ☃xxxxx = GuiStats.this.fontRenderer.getStringWidth(☃xx);
                  GuiStats.this.drawGradientRect(☃xxx - 3, ☃xxxx - 3, ☃xxx + ☃xxxxx + 3, ☃xxxx + 8 + 3, -1073741824, -1073741824);
                  GuiStats.this.fontRenderer.drawStringWithShadow(☃xx, ☃xxx, ☃xxxx, -1);
               }
            }
         }
      }

      protected void renderMouseHoverToolTip(StatCrafting var1, int var2, int var3) {
         if (☃ != null) {
            Item ☃ = ☃.getItem();
            ItemStack ☃x = new ItemStack(☃);
            String ☃xx = ☃x.getTranslationKey();
            String ☃xxx = ("" + I18n.format(☃xx + ".name")).trim();
            if (!☃xxx.isEmpty()) {
               int ☃xxxx = ☃ + 12;
               int ☃xxxxx = ☃ - 12;
               int ☃xxxxxx = GuiStats.this.fontRenderer.getStringWidth(☃xxx);
               GuiStats.this.drawGradientRect(☃xxxx - 3, ☃xxxxx - 3, ☃xxxx + ☃xxxxxx + 3, ☃xxxxx + 8 + 3, -1073741824, -1073741824);
               GuiStats.this.fontRenderer.drawStringWithShadow(☃xxx, ☃xxxx, ☃xxxxx, -1);
            }
         }
      }

      protected void sortByColumn(int var1) {
         if (☃ != this.sortColumn) {
            this.sortColumn = ☃;
            this.sortOrder = -1;
         } else if (this.sortOrder == -1) {
            this.sortOrder = 1;
         } else {
            this.sortColumn = -1;
            this.sortOrder = 0;
         }

         Collections.sort(this.statsHolder, this.statSorter);
      }
   }

   class StatsBlock extends GuiStats.Stats {
      public StatsBlock(Minecraft var2) {
         super(☃);
         this.statsHolder = Lists.newArrayList();

         for (StatCrafting ☃ : StatList.MINE_BLOCK_STATS) {
            boolean ☃x = false;
            Item ☃xx = ☃.getItem();
            if (GuiStats.this.stats.readStat(☃) > 0) {
               ☃x = true;
            } else if (StatList.getObjectUseStats(☃xx) != null && GuiStats.this.stats.readStat(StatList.getObjectUseStats(☃xx)) > 0) {
               ☃x = true;
            } else if (StatList.getCraftStats(☃xx) != null && GuiStats.this.stats.readStat(StatList.getCraftStats(☃xx)) > 0) {
               ☃x = true;
            } else if (StatList.getObjectsPickedUpStats(☃xx) != null && GuiStats.this.stats.readStat(StatList.getObjectsPickedUpStats(☃xx)) > 0) {
               ☃x = true;
            } else if (StatList.getDroppedObjectStats(☃xx) != null && GuiStats.this.stats.readStat(StatList.getDroppedObjectStats(☃xx)) > 0) {
               ☃x = true;
            }

            if (☃x) {
               this.statsHolder.add(☃);
            }
         }

         this.statSorter = new Comparator<StatCrafting>() {
            public int compare(StatCrafting var1, StatCrafting var2x) {
               Item ☃ = ☃.getItem();
               Item ☃x = var2x.getItem();
               StatBase ☃xx = null;
               StatBase ☃xxx = null;
               if (StatsBlock.this.sortColumn == 2) {
                  ☃xx = StatList.getBlockStats(Block.getBlockFromItem(☃));
                  ☃xxx = StatList.getBlockStats(Block.getBlockFromItem(☃x));
               } else if (StatsBlock.this.sortColumn == 0) {
                  ☃xx = StatList.getCraftStats(☃);
                  ☃xxx = StatList.getCraftStats(☃x);
               } else if (StatsBlock.this.sortColumn == 1) {
                  ☃xx = StatList.getObjectUseStats(☃);
                  ☃xxx = StatList.getObjectUseStats(☃x);
               } else if (StatsBlock.this.sortColumn == 3) {
                  ☃xx = StatList.getObjectsPickedUpStats(☃);
                  ☃xxx = StatList.getObjectsPickedUpStats(☃x);
               } else if (StatsBlock.this.sortColumn == 4) {
                  ☃xx = StatList.getDroppedObjectStats(☃);
                  ☃xxx = StatList.getDroppedObjectStats(☃x);
               }

               if (☃xx != null || ☃xxx != null) {
                  if (☃xx == null) {
                     return 1;
                  }

                  if (☃xxx == null) {
                     return -1;
                  }

                  int ☃xxxx = GuiStats.this.stats.readStat(☃xx);
                  int ☃xxxxx = GuiStats.this.stats.readStat(☃xxx);
                  if (☃xxxx != ☃xxxxx) {
                     return (☃xxxx - ☃xxxxx) * StatsBlock.this.sortOrder;
                  }
               }

               return Item.getIdFromItem(☃) - Item.getIdFromItem(☃x);
            }
         };
      }

      @Override
      protected void drawListHeader(int var1, int var2, Tessellator var3) {
         super.drawListHeader(☃, ☃, ☃);
         if (this.headerPressed == 0) {
            GuiStats.this.drawSprite(☃ + 115 - 18 + 1, ☃ + 1 + 1, 18, 18);
         } else {
            GuiStats.this.drawSprite(☃ + 115 - 18, ☃ + 1, 18, 18);
         }

         if (this.headerPressed == 1) {
            GuiStats.this.drawSprite(☃ + 165 - 18 + 1, ☃ + 1 + 1, 36, 18);
         } else {
            GuiStats.this.drawSprite(☃ + 165 - 18, ☃ + 1, 36, 18);
         }

         if (this.headerPressed == 2) {
            GuiStats.this.drawSprite(☃ + 215 - 18 + 1, ☃ + 1 + 1, 54, 18);
         } else {
            GuiStats.this.drawSprite(☃ + 215 - 18, ☃ + 1, 54, 18);
         }

         if (this.headerPressed == 3) {
            GuiStats.this.drawSprite(☃ + 265 - 18 + 1, ☃ + 1 + 1, 90, 18);
         } else {
            GuiStats.this.drawSprite(☃ + 265 - 18, ☃ + 1, 90, 18);
         }

         if (this.headerPressed == 4) {
            GuiStats.this.drawSprite(☃ + 315 - 18 + 1, ☃ + 1 + 1, 108, 18);
         } else {
            GuiStats.this.drawSprite(☃ + 315 - 18, ☃ + 1, 108, 18);
         }
      }

      @Override
      protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6, float var7) {
         StatCrafting ☃ = this.getSlotStat(☃);
         Item ☃x = ☃.getItem();
         GuiStats.this.drawStatsScreen(☃ + 40, ☃, ☃x);
         this.renderStat(StatList.getCraftStats(☃x), ☃ + 115, ☃, ☃ % 2 == 0);
         this.renderStat(StatList.getObjectUseStats(☃x), ☃ + 165, ☃, ☃ % 2 == 0);
         this.renderStat(☃, ☃ + 215, ☃, ☃ % 2 == 0);
         this.renderStat(StatList.getObjectsPickedUpStats(☃x), ☃ + 265, ☃, ☃ % 2 == 0);
         this.renderStat(StatList.getDroppedObjectStats(☃x), ☃ + 315, ☃, ☃ % 2 == 0);
      }

      @Override
      protected String getHeaderDescriptionId(int var1) {
         if (☃ == 0) {
            return "stat.crafted";
         } else if (☃ == 1) {
            return "stat.used";
         } else if (☃ == 3) {
            return "stat.pickup";
         } else {
            return ☃ == 4 ? "stat.dropped" : "stat.mined";
         }
      }
   }

   class StatsGeneral extends GuiSlot {
      public StatsGeneral(Minecraft var2) {
         super(☃, GuiStats.this.width, GuiStats.this.height, 32, GuiStats.this.height - 64, 10);
         this.setShowSelectionBox(false);
      }

      @Override
      protected int getSize() {
         return StatList.BASIC_STATS.size();
      }

      @Override
      protected void elementClicked(int var1, boolean var2, int var3, int var4) {
      }

      @Override
      protected boolean isSelected(int var1) {
         return false;
      }

      @Override
      protected int getContentHeight() {
         return this.getSize() * 10;
      }

      @Override
      protected void drawBackground() {
         GuiStats.this.drawDefaultBackground();
      }

      @Override
      protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6, float var7) {
         StatBase ☃ = StatList.BASIC_STATS.get(☃);
         GuiStats.this.drawString(GuiStats.this.fontRenderer, ☃.getStatName().getUnformattedText(), ☃ + 2, ☃ + 1, ☃ % 2 == 0 ? 16777215 : 9474192);
         String ☃x = ☃.format(GuiStats.this.stats.readStat(☃));
         GuiStats.this.drawString(
            GuiStats.this.fontRenderer, ☃x, ☃ + 2 + 213 - GuiStats.this.fontRenderer.getStringWidth(☃x), ☃ + 1, ☃ % 2 == 0 ? 16777215 : 9474192
         );
      }
   }

   class StatsItem extends GuiStats.Stats {
      public StatsItem(Minecraft var2) {
         super(☃);
         this.statsHolder = Lists.newArrayList();

         for (StatCrafting ☃ : StatList.USE_ITEM_STATS) {
            boolean ☃x = false;
            Item ☃xx = ☃.getItem();
            if (GuiStats.this.stats.readStat(☃) > 0) {
               ☃x = true;
            } else if (StatList.getObjectBreakStats(☃xx) != null && GuiStats.this.stats.readStat(StatList.getObjectBreakStats(☃xx)) > 0) {
               ☃x = true;
            } else if (StatList.getCraftStats(☃xx) != null && GuiStats.this.stats.readStat(StatList.getCraftStats(☃xx)) > 0) {
               ☃x = true;
            } else if (StatList.getObjectsPickedUpStats(☃xx) != null && GuiStats.this.stats.readStat(StatList.getObjectsPickedUpStats(☃xx)) > 0) {
               ☃x = true;
            } else if (StatList.getDroppedObjectStats(☃xx) != null && GuiStats.this.stats.readStat(StatList.getDroppedObjectStats(☃xx)) > 0) {
               ☃x = true;
            }

            if (☃x) {
               this.statsHolder.add(☃);
            }
         }

         this.statSorter = new Comparator<StatCrafting>() {
            public int compare(StatCrafting var1, StatCrafting var2x) {
               Item ☃ = ☃.getItem();
               Item ☃x = var2x.getItem();
               int ☃xx = Item.getIdFromItem(☃);
               int ☃xxx = Item.getIdFromItem(☃x);
               StatBase ☃xxxx = null;
               StatBase ☃xxxxx = null;
               if (StatsItem.this.sortColumn == 0) {
                  ☃xxxx = StatList.getObjectBreakStats(☃);
                  ☃xxxxx = StatList.getObjectBreakStats(☃x);
               } else if (StatsItem.this.sortColumn == 1) {
                  ☃xxxx = StatList.getCraftStats(☃);
                  ☃xxxxx = StatList.getCraftStats(☃x);
               } else if (StatsItem.this.sortColumn == 2) {
                  ☃xxxx = StatList.getObjectUseStats(☃);
                  ☃xxxxx = StatList.getObjectUseStats(☃x);
               } else if (StatsItem.this.sortColumn == 3) {
                  ☃xxxx = StatList.getObjectsPickedUpStats(☃);
                  ☃xxxxx = StatList.getObjectsPickedUpStats(☃x);
               } else if (StatsItem.this.sortColumn == 4) {
                  ☃xxxx = StatList.getDroppedObjectStats(☃);
                  ☃xxxxx = StatList.getDroppedObjectStats(☃x);
               }

               if (☃xxxx != null || ☃xxxxx != null) {
                  if (☃xxxx == null) {
                     return 1;
                  }

                  if (☃xxxxx == null) {
                     return -1;
                  }

                  int ☃xxxxxx = GuiStats.this.stats.readStat(☃xxxx);
                  int ☃xxxxxxx = GuiStats.this.stats.readStat(☃xxxxx);
                  if (☃xxxxxx != ☃xxxxxxx) {
                     return (☃xxxxxx - ☃xxxxxxx) * StatsItem.this.sortOrder;
                  }
               }

               return ☃xx - ☃xxx;
            }
         };
      }

      @Override
      protected void drawListHeader(int var1, int var2, Tessellator var3) {
         super.drawListHeader(☃, ☃, ☃);
         if (this.headerPressed == 0) {
            GuiStats.this.drawSprite(☃ + 115 - 18 + 1, ☃ + 1 + 1, 72, 18);
         } else {
            GuiStats.this.drawSprite(☃ + 115 - 18, ☃ + 1, 72, 18);
         }

         if (this.headerPressed == 1) {
            GuiStats.this.drawSprite(☃ + 165 - 18 + 1, ☃ + 1 + 1, 18, 18);
         } else {
            GuiStats.this.drawSprite(☃ + 165 - 18, ☃ + 1, 18, 18);
         }

         if (this.headerPressed == 2) {
            GuiStats.this.drawSprite(☃ + 215 - 18 + 1, ☃ + 1 + 1, 36, 18);
         } else {
            GuiStats.this.drawSprite(☃ + 215 - 18, ☃ + 1, 36, 18);
         }

         if (this.headerPressed == 3) {
            GuiStats.this.drawSprite(☃ + 265 - 18 + 1, ☃ + 1 + 1, 90, 18);
         } else {
            GuiStats.this.drawSprite(☃ + 265 - 18, ☃ + 1, 90, 18);
         }

         if (this.headerPressed == 4) {
            GuiStats.this.drawSprite(☃ + 315 - 18 + 1, ☃ + 1 + 1, 108, 18);
         } else {
            GuiStats.this.drawSprite(☃ + 315 - 18, ☃ + 1, 108, 18);
         }
      }

      @Override
      protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6, float var7) {
         StatCrafting ☃ = this.getSlotStat(☃);
         Item ☃x = ☃.getItem();
         GuiStats.this.drawStatsScreen(☃ + 40, ☃, ☃x);
         this.renderStat(StatList.getObjectBreakStats(☃x), ☃ + 115, ☃, ☃ % 2 == 0);
         this.renderStat(StatList.getCraftStats(☃x), ☃ + 165, ☃, ☃ % 2 == 0);
         this.renderStat(☃, ☃ + 215, ☃, ☃ % 2 == 0);
         this.renderStat(StatList.getObjectsPickedUpStats(☃x), ☃ + 265, ☃, ☃ % 2 == 0);
         this.renderStat(StatList.getDroppedObjectStats(☃x), ☃ + 315, ☃, ☃ % 2 == 0);
      }

      @Override
      protected String getHeaderDescriptionId(int var1) {
         if (☃ == 1) {
            return "stat.crafted";
         } else if (☃ == 2) {
            return "stat.used";
         } else if (☃ == 3) {
            return "stat.pickup";
         } else {
            return ☃ == 4 ? "stat.dropped" : "stat.depleted";
         }
      }
   }

   class StatsMobsList extends GuiSlot {
      private final List<EntityList.EntityEggInfo> mobs = Lists.newArrayList();

      public StatsMobsList(Minecraft var2) {
         super(☃, GuiStats.this.width, GuiStats.this.height, 32, GuiStats.this.height - 64, GuiStats.this.fontRenderer.FONT_HEIGHT * 4);
         this.setShowSelectionBox(false);

         for (EntityList.EntityEggInfo ☃ : EntityList.ENTITY_EGGS.values()) {
            if (GuiStats.this.stats.readStat(☃.killEntityStat) > 0 || GuiStats.this.stats.readStat(☃.entityKilledByStat) > 0) {
               this.mobs.add(☃);
            }
         }
      }

      @Override
      protected int getSize() {
         return this.mobs.size();
      }

      @Override
      protected void elementClicked(int var1, boolean var2, int var3, int var4) {
      }

      @Override
      protected boolean isSelected(int var1) {
         return false;
      }

      @Override
      protected int getContentHeight() {
         return this.getSize() * GuiStats.this.fontRenderer.FONT_HEIGHT * 4;
      }

      @Override
      protected void drawBackground() {
         GuiStats.this.drawDefaultBackground();
      }

      @Override
      protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6, float var7) {
         EntityList.EntityEggInfo ☃ = this.mobs.get(☃);
         String ☃x = I18n.format("entity." + EntityList.getTranslationName(☃.spawnedID) + ".name");
         int ☃xx = GuiStats.this.stats.readStat(☃.killEntityStat);
         int ☃xxx = GuiStats.this.stats.readStat(☃.entityKilledByStat);
         String ☃xxxx = I18n.format("stat.entityKills", ☃xx, ☃x);
         String ☃xxxxx = I18n.format("stat.entityKilledBy", ☃x, ☃xxx);
         if (☃xx == 0) {
            ☃xxxx = I18n.format("stat.entityKills.none", ☃x);
         }

         if (☃xxx == 0) {
            ☃xxxxx = I18n.format("stat.entityKilledBy.none", ☃x);
         }

         GuiStats.this.drawString(GuiStats.this.fontRenderer, ☃x, ☃ + 2 - 10, ☃ + 1, 16777215);
         GuiStats.this.drawString(GuiStats.this.fontRenderer, ☃xxxx, ☃ + 2, ☃ + 1 + GuiStats.this.fontRenderer.FONT_HEIGHT, ☃xx == 0 ? 6316128 : 9474192);
         GuiStats.this.drawString(GuiStats.this.fontRenderer, ☃xxxxx, ☃ + 2, ☃ + 1 + GuiStats.this.fontRenderer.FONT_HEIGHT * 2, ☃xxx == 0 ? 6316128 : 9474192);
      }
   }
}
