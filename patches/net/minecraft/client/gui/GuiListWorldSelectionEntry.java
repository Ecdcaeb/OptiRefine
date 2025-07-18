package net.minecraft.client.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSummary;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiListWorldSelectionEntry implements GuiListExtended.IGuiListEntry {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final DateFormat DATE_FORMAT = new SimpleDateFormat();
   private static final ResourceLocation ICON_MISSING = new ResourceLocation("textures/misc/unknown_server.png");
   private static final ResourceLocation ICON_OVERLAY_LOCATION = new ResourceLocation("textures/gui/world_selection.png");
   private final Minecraft client;
   private final GuiWorldSelection worldSelScreen;
   private final WorldSummary worldSummary;
   private final ResourceLocation iconLocation;
   private final GuiListWorldSelection containingListSel;
   private File iconFile;
   private DynamicTexture icon;
   private long lastClickTime;

   public GuiListWorldSelectionEntry(GuiListWorldSelection var1, WorldSummary var2, ISaveFormat var3) {
      this.containingListSel = ☃;
      this.worldSelScreen = ☃.getGuiWorldSelection();
      this.worldSummary = ☃;
      this.client = Minecraft.getMinecraft();
      this.iconLocation = new ResourceLocation("worlds/" + ☃.getFileName() + "/icon");
      this.iconFile = ☃.getFile(☃.getFileName(), "icon.png");
      if (!this.iconFile.isFile()) {
         this.iconFile = null;
      }

      this.loadServerIcon();
   }

   @Override
   public void drawEntry(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8, float var9) {
      String ☃ = this.worldSummary.getDisplayName();
      String ☃x = this.worldSummary.getFileName() + " (" + DATE_FORMAT.format(new Date(this.worldSummary.getLastTimePlayed())) + ")";
      String ☃xx = "";
      if (StringUtils.isEmpty(☃)) {
         ☃ = I18n.format("selectWorld.world") + " " + (☃ + 1);
      }

      if (this.worldSummary.requiresConversion()) {
         ☃xx = I18n.format("selectWorld.conversion") + " " + ☃xx;
      } else {
         ☃xx = I18n.format("gameMode." + this.worldSummary.getEnumGameType().getName());
         if (this.worldSummary.isHardcoreModeEnabled()) {
            ☃xx = TextFormatting.DARK_RED + I18n.format("gameMode.hardcore") + TextFormatting.RESET;
         }

         if (this.worldSummary.getCheatsEnabled()) {
            ☃xx = ☃xx + ", " + I18n.format("selectWorld.cheats");
         }

         String ☃xxx = this.worldSummary.getVersionName();
         if (this.worldSummary.markVersionInList()) {
            if (this.worldSummary.askToOpenWorld()) {
               ☃xx = ☃xx + ", " + I18n.format("selectWorld.version") + " " + TextFormatting.RED + ☃xxx + TextFormatting.RESET;
            } else {
               ☃xx = ☃xx + ", " + I18n.format("selectWorld.version") + " " + TextFormatting.ITALIC + ☃xxx + TextFormatting.RESET;
            }
         } else {
            ☃xx = ☃xx + ", " + I18n.format("selectWorld.version") + " " + ☃xxx;
         }
      }

      this.client.fontRenderer.drawString(☃, ☃ + 32 + 3, ☃ + 1, 16777215);
      this.client.fontRenderer.drawString(☃x, ☃ + 32 + 3, ☃ + this.client.fontRenderer.FONT_HEIGHT + 3, 8421504);
      this.client.fontRenderer.drawString(☃xx, ☃ + 32 + 3, ☃ + this.client.fontRenderer.FONT_HEIGHT + this.client.fontRenderer.FONT_HEIGHT + 3, 8421504);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.client.getTextureManager().bindTexture(this.icon != null ? this.iconLocation : ICON_MISSING);
      GlStateManager.enableBlend();
      Gui.drawModalRectWithCustomSizedTexture(☃, ☃, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
      GlStateManager.disableBlend();
      if (this.client.gameSettings.touchscreen || ☃) {
         this.client.getTextureManager().bindTexture(ICON_OVERLAY_LOCATION);
         Gui.drawRect(☃, ☃, ☃ + 32, ☃ + 32, -1601138544);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         int ☃xxx = ☃ - ☃;
         int ☃xxxx = ☃xxx < 32 ? 32 : 0;
         if (this.worldSummary.markVersionInList()) {
            Gui.drawModalRectWithCustomSizedTexture(☃, ☃, 32.0F, ☃xxxx, 32, 32, 256.0F, 256.0F);
            if (this.worldSummary.askToOpenWorld()) {
               Gui.drawModalRectWithCustomSizedTexture(☃, ☃, 96.0F, ☃xxxx, 32, 32, 256.0F, 256.0F);
               if (☃xxx < 32) {
                  this.worldSelScreen
                     .setVersionTooltip(
                        TextFormatting.RED
                           + I18n.format("selectWorld.tooltip.fromNewerVersion1")
                           + "\n"
                           + TextFormatting.RED
                           + I18n.format("selectWorld.tooltip.fromNewerVersion2")
                     );
               }
            } else {
               Gui.drawModalRectWithCustomSizedTexture(☃, ☃, 64.0F, ☃xxxx, 32, 32, 256.0F, 256.0F);
               if (☃xxx < 32) {
                  this.worldSelScreen
                     .setVersionTooltip(
                        TextFormatting.GOLD
                           + I18n.format("selectWorld.tooltip.snapshot1")
                           + "\n"
                           + TextFormatting.GOLD
                           + I18n.format("selectWorld.tooltip.snapshot2")
                     );
               }
            }
         } else {
            Gui.drawModalRectWithCustomSizedTexture(☃, ☃, 0.0F, ☃xxxx, 32, 32, 256.0F, 256.0F);
         }
      }
   }

   @Override
   public boolean mousePressed(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.containingListSel.selectWorld(☃);
      if (☃ <= 32 && ☃ < 32) {
         this.joinWorld();
         return true;
      } else if (Minecraft.getSystemTime() - this.lastClickTime < 250L) {
         this.joinWorld();
         return true;
      } else {
         this.lastClickTime = Minecraft.getSystemTime();
         return false;
      }
   }

   public void joinWorld() {
      if (this.worldSummary.askToOpenWorld()) {
         this.client
            .displayGuiScreen(
               new GuiYesNo(
                  new GuiYesNoCallback() {
                     @Override
                     public void confirmClicked(boolean var1, int var2) {
                        if (☃) {
                           GuiListWorldSelectionEntry.this.loadWorld();
                        } else {
                           GuiListWorldSelectionEntry.this.client.displayGuiScreen(GuiListWorldSelectionEntry.this.worldSelScreen);
                        }
                     }
                  },
                  I18n.format("selectWorld.versionQuestion"),
                  I18n.format("selectWorld.versionWarning", this.worldSummary.getVersionName()),
                  I18n.format("selectWorld.versionJoinButton"),
                  I18n.format("gui.cancel"),
                  0
               )
            );
      } else {
         this.loadWorld();
      }
   }

   public void deleteWorld() {
      this.client
         .displayGuiScreen(
            new GuiYesNo(
               new GuiYesNoCallback() {
                  @Override
                  public void confirmClicked(boolean var1, int var2) {
                     if (☃) {
                        GuiListWorldSelectionEntry.this.client.displayGuiScreen(new GuiScreenWorking());
                        ISaveFormat ☃ = GuiListWorldSelectionEntry.this.client.getSaveLoader();
                        ☃.flushCache();
                        ☃.deleteWorldDirectory(GuiListWorldSelectionEntry.this.worldSummary.getFileName());
                        GuiListWorldSelectionEntry.this.containingListSel.refreshList();
                     }

                     GuiListWorldSelectionEntry.this.client.displayGuiScreen(GuiListWorldSelectionEntry.this.worldSelScreen);
                  }
               },
               I18n.format("selectWorld.deleteQuestion"),
               "'" + this.worldSummary.getDisplayName() + "' " + I18n.format("selectWorld.deleteWarning"),
               I18n.format("selectWorld.deleteButton"),
               I18n.format("gui.cancel"),
               0
            )
         );
   }

   public void editWorld() {
      this.client.displayGuiScreen(new GuiWorldEdit(this.worldSelScreen, this.worldSummary.getFileName()));
   }

   public void recreateWorld() {
      this.client.displayGuiScreen(new GuiScreenWorking());
      GuiCreateWorld ☃ = new GuiCreateWorld(this.worldSelScreen);
      ISaveHandler ☃x = this.client.getSaveLoader().getSaveLoader(this.worldSummary.getFileName(), false);
      WorldInfo ☃xx = ☃x.loadWorldInfo();
      ☃x.flush();
      if (☃xx != null) {
         ☃.recreateFromExistingWorld(☃xx);
         this.client.displayGuiScreen(☃);
      }
   }

   private void loadWorld() {
      this.client.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
      if (this.client.getSaveLoader().canLoadWorld(this.worldSummary.getFileName())) {
         this.client.launchIntegratedServer(this.worldSummary.getFileName(), this.worldSummary.getDisplayName(), null);
      }
   }

   private void loadServerIcon() {
      boolean ☃ = this.iconFile != null && this.iconFile.isFile();
      if (☃) {
         BufferedImage ☃x;
         try {
            ☃x = ImageIO.read(this.iconFile);
            Validate.validState(☃x.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
            Validate.validState(☃x.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
         } catch (Throwable var4) {
            LOGGER.error("Invalid icon for world {}", this.worldSummary.getFileName(), var4);
            this.iconFile = null;
            return;
         }

         if (this.icon == null) {
            this.icon = new DynamicTexture(☃x.getWidth(), ☃x.getHeight());
            this.client.getTextureManager().loadTexture(this.iconLocation, this.icon);
         }

         ☃x.getRGB(0, 0, ☃x.getWidth(), ☃x.getHeight(), this.icon.getTextureData(), 0, ☃x.getWidth());
         this.icon.updateDynamicTexture();
      } else if (!☃) {
         this.client.getTextureManager().deleteTexture(this.iconLocation);
         this.icon = null;
      }
   }

   @Override
   public void mouseReleased(int var1, int var2, int var3, int var4, int var5, int var6) {
   }

   @Override
   public void updatePosition(int var1, int var2, int var3, float var4) {
   }
}
