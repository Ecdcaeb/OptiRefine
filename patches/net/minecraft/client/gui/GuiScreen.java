package net.minecraft.client.gui;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public abstract class GuiScreen extends Gui implements GuiYesNoCallback {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final Set<String> PROTOCOLS = Sets.newHashSet(new String[]{"http", "https"});
   private static final Splitter NEWLINE_SPLITTER = Splitter.on('\n');
   protected Minecraft mc;
   protected RenderItem itemRender;
   public int width;
   public int height;
   protected List<GuiButton> buttonList = Lists.newArrayList();
   protected List<GuiLabel> labelList = Lists.newArrayList();
   public boolean allowUserInput;
   protected FontRenderer fontRenderer;
   protected GuiButton selectedButton;
   private int eventButton;
   private long lastMouseEvent;
   private int touchValue;
   private URI clickedLinkURI;
   private boolean focused;

   public void drawScreen(int var1, int var2, float var3) {
      for (int ☃ = 0; ☃ < this.buttonList.size(); ☃++) {
         this.buttonList.get(☃).drawButton(this.mc, ☃, ☃, ☃);
      }

      for (int ☃ = 0; ☃ < this.labelList.size(); ☃++) {
         this.labelList.get(☃).drawLabel(this.mc, ☃, ☃);
      }
   }

   protected void keyTyped(char var1, int var2) {
      if (☃ == 1) {
         this.mc.displayGuiScreen(null);
         if (this.mc.currentScreen == null) {
            this.mc.setIngameFocus();
         }
      }
   }

   protected <T extends GuiButton> T addButton(T var1) {
      this.buttonList.add(☃);
      return ☃;
   }

   public static String getClipboardString() {
      try {
         Transferable ☃ = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
         if (☃ != null && ☃.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return (String)☃.getTransferData(DataFlavor.stringFlavor);
         }
      } catch (Exception var1) {
      }

      return "";
   }

   public static void setClipboardString(String var0) {
      if (!StringUtils.isEmpty(☃)) {
         try {
            StringSelection ☃ = new StringSelection(☃);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(☃, null);
         } catch (Exception var2) {
         }
      }
   }

   protected void renderToolTip(ItemStack var1, int var2, int var3) {
      this.drawHoveringText(this.getItemToolTip(☃), ☃, ☃);
   }

   public List<String> getItemToolTip(ItemStack var1) {
      List<String> ☃ = ☃.getTooltip(
         this.mc.player, this.mc.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL
      );

      for (int ☃x = 0; ☃x < ☃.size(); ☃x++) {
         if (☃x == 0) {
            ☃.set(☃x, ☃.getRarity().color + ☃.get(☃x));
         } else {
            ☃.set(☃x, TextFormatting.GRAY + ☃.get(☃x));
         }
      }

      return ☃;
   }

   public void drawHoveringText(String var1, int var2, int var3) {
      this.drawHoveringText(Arrays.asList(☃), ☃, ☃);
   }

   public void setFocused(boolean var1) {
      this.focused = ☃;
   }

   public boolean isFocused() {
      return this.focused;
   }

   public void drawHoveringText(List<String> var1, int var2, int var3) {
      if (!☃.isEmpty()) {
         GlStateManager.disableRescaleNormal();
         RenderHelper.disableStandardItemLighting();
         GlStateManager.disableLighting();
         GlStateManager.disableDepth();
         int ☃ = 0;

         for (String ☃x : ☃) {
            int ☃xx = this.fontRenderer.getStringWidth(☃x);
            if (☃xx > ☃) {
               ☃ = ☃xx;
            }
         }

         int ☃xx = ☃ + 12;
         int ☃xxx = ☃ - 12;
         int ☃xxxx = 8;
         if (☃.size() > 1) {
            ☃xxxx += 2 + (☃.size() - 1) * 10;
         }

         if (☃xx + ☃ > this.width) {
            ☃xx -= 28 + ☃;
         }

         if (☃xxx + ☃xxxx + 6 > this.height) {
            ☃xxx = this.height - ☃xxxx - 6;
         }

         this.zLevel = 300.0F;
         this.itemRender.zLevel = 300.0F;
         int ☃xxxxx = -267386864;
         this.drawGradientRect(☃xx - 3, ☃xxx - 4, ☃xx + ☃ + 3, ☃xxx - 3, -267386864, -267386864);
         this.drawGradientRect(☃xx - 3, ☃xxx + ☃xxxx + 3, ☃xx + ☃ + 3, ☃xxx + ☃xxxx + 4, -267386864, -267386864);
         this.drawGradientRect(☃xx - 3, ☃xxx - 3, ☃xx + ☃ + 3, ☃xxx + ☃xxxx + 3, -267386864, -267386864);
         this.drawGradientRect(☃xx - 4, ☃xxx - 3, ☃xx - 3, ☃xxx + ☃xxxx + 3, -267386864, -267386864);
         this.drawGradientRect(☃xx + ☃ + 3, ☃xxx - 3, ☃xx + ☃ + 4, ☃xxx + ☃xxxx + 3, -267386864, -267386864);
         int ☃xxxxxx = 1347420415;
         int ☃xxxxxxx = 1344798847;
         this.drawGradientRect(☃xx - 3, ☃xxx - 3 + 1, ☃xx - 3 + 1, ☃xxx + ☃xxxx + 3 - 1, 1347420415, 1344798847);
         this.drawGradientRect(☃xx + ☃ + 2, ☃xxx - 3 + 1, ☃xx + ☃ + 3, ☃xxx + ☃xxxx + 3 - 1, 1347420415, 1344798847);
         this.drawGradientRect(☃xx - 3, ☃xxx - 3, ☃xx + ☃ + 3, ☃xxx - 3 + 1, 1347420415, 1347420415);
         this.drawGradientRect(☃xx - 3, ☃xxx + ☃xxxx + 2, ☃xx + ☃ + 3, ☃xxx + ☃xxxx + 3, 1344798847, 1344798847);

         for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < ☃.size(); ☃xxxxxxxx++) {
            String ☃xxxxxxxxx = ☃.get(☃xxxxxxxx);
            this.fontRenderer.drawStringWithShadow(☃xxxxxxxxx, ☃xx, ☃xxx, -1);
            if (☃xxxxxxxx == 0) {
               ☃xxx += 2;
            }

            ☃xxx += 10;
         }

         this.zLevel = 0.0F;
         this.itemRender.zLevel = 0.0F;
         GlStateManager.enableLighting();
         GlStateManager.enableDepth();
         RenderHelper.enableStandardItemLighting();
         GlStateManager.enableRescaleNormal();
      }
   }

   protected void handleComponentHover(ITextComponent var1, int var2, int var3) {
      if (☃ != null && ☃.getStyle().getHoverEvent() != null) {
         HoverEvent ☃ = ☃.getStyle().getHoverEvent();
         if (☃.getAction() == HoverEvent.Action.SHOW_ITEM) {
            ItemStack ☃x = ItemStack.EMPTY;

            try {
               NBTBase ☃xx = JsonToNBT.getTagFromJson(☃.getValue().getUnformattedText());
               if (☃xx instanceof NBTTagCompound) {
                  ☃x = new ItemStack((NBTTagCompound)☃xx);
               }
            } catch (NBTException var9) {
            }

            if (☃x.isEmpty()) {
               this.drawHoveringText(TextFormatting.RED + "Invalid Item!", ☃, ☃);
            } else {
               this.renderToolTip(☃x, ☃, ☃);
            }
         } else if (☃.getAction() == HoverEvent.Action.SHOW_ENTITY) {
            if (this.mc.gameSettings.advancedItemTooltips) {
               try {
                  NBTTagCompound ☃x = JsonToNBT.getTagFromJson(☃.getValue().getUnformattedText());
                  List<String> ☃xx = Lists.newArrayList();
                  ☃xx.add(☃x.getString("name"));
                  if (☃x.hasKey("type", 8)) {
                     String ☃xxx = ☃x.getString("type");
                     ☃xx.add("Type: " + ☃xxx);
                  }

                  ☃xx.add(☃x.getString("id"));
                  this.drawHoveringText(☃xx, ☃, ☃);
               } catch (NBTException var8) {
                  this.drawHoveringText(TextFormatting.RED + "Invalid Entity!", ☃, ☃);
               }
            }
         } else if (☃.getAction() == HoverEvent.Action.SHOW_TEXT) {
            this.drawHoveringText(this.mc.fontRenderer.listFormattedStringToWidth(☃.getValue().getFormattedText(), Math.max(this.width / 2, 200)), ☃, ☃);
         }

         GlStateManager.disableLighting();
      }
   }

   protected void setText(String var1, boolean var2) {
   }

   public boolean handleComponentClick(ITextComponent var1) {
      if (☃ == null) {
         return false;
      } else {
         ClickEvent ☃ = ☃.getStyle().getClickEvent();
         if (isShiftKeyDown()) {
            if (☃.getStyle().getInsertion() != null) {
               this.setText(☃.getStyle().getInsertion(), false);
            }
         } else if (☃ != null) {
            if (☃.getAction() == ClickEvent.Action.OPEN_URL) {
               if (!this.mc.gameSettings.chatLinks) {
                  return false;
               }

               try {
                  URI ☃x = new URI(☃.getValue());
                  String ☃xx = ☃x.getScheme();
                  if (☃xx == null) {
                     throw new URISyntaxException(☃.getValue(), "Missing protocol");
                  }

                  if (!PROTOCOLS.contains(☃xx.toLowerCase(Locale.ROOT))) {
                     throw new URISyntaxException(☃.getValue(), "Unsupported protocol: " + ☃xx.toLowerCase(Locale.ROOT));
                  }

                  if (this.mc.gameSettings.chatLinksPrompt) {
                     this.clickedLinkURI = ☃x;
                     this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, ☃.getValue(), 31102009, false));
                  } else {
                     this.openWebLink(☃x);
                  }
               } catch (URISyntaxException var5) {
                  LOGGER.error("Can't open url for {}", ☃, var5);
               }
            } else if (☃.getAction() == ClickEvent.Action.OPEN_FILE) {
               URI ☃xxx = new File(☃.getValue()).toURI();
               this.openWebLink(☃xxx);
            } else if (☃.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
               this.setText(☃.getValue(), true);
            } else if (☃.getAction() == ClickEvent.Action.RUN_COMMAND) {
               this.sendChatMessage(☃.getValue(), false);
            } else {
               LOGGER.error("Don't know how to handle {}", ☃);
            }

            return true;
         }

         return false;
      }
   }

   public void sendChatMessage(String var1) {
      this.sendChatMessage(☃, true);
   }

   public void sendChatMessage(String var1, boolean var2) {
      if (☃) {
         this.mc.ingameGUI.getChatGUI().addToSentMessages(☃);
      }

      this.mc.player.sendChatMessage(☃);
   }

   protected void mouseClicked(int var1, int var2, int var3) {
      if (☃ == 0) {
         for (int ☃ = 0; ☃ < this.buttonList.size(); ☃++) {
            GuiButton ☃x = this.buttonList.get(☃);
            if (☃x.mousePressed(this.mc, ☃, ☃)) {
               this.selectedButton = ☃x;
               ☃x.playPressSound(this.mc.getSoundHandler());
               this.actionPerformed(☃x);
            }
         }
      }
   }

   protected void mouseReleased(int var1, int var2, int var3) {
      if (this.selectedButton != null && ☃ == 0) {
         this.selectedButton.mouseReleased(☃, ☃);
         this.selectedButton = null;
      }
   }

   protected void mouseClickMove(int var1, int var2, int var3, long var4) {
   }

   protected void actionPerformed(GuiButton var1) {
   }

   public void setWorldAndResolution(Minecraft var1, int var2, int var3) {
      this.mc = ☃;
      this.itemRender = ☃.getRenderItem();
      this.fontRenderer = ☃.fontRenderer;
      this.width = ☃;
      this.height = ☃;
      this.buttonList.clear();
      this.initGui();
   }

   public void setGuiSize(int var1, int var2) {
      this.width = ☃;
      this.height = ☃;
   }

   public void initGui() {
   }

   public void handleInput() {
      if (Mouse.isCreated()) {
         while (Mouse.next()) {
            this.handleMouseInput();
         }
      }

      if (Keyboard.isCreated()) {
         while (Keyboard.next()) {
            this.handleKeyboardInput();
         }
      }
   }

   public void handleMouseInput() {
      int ☃ = Mouse.getEventX() * this.width / this.mc.displayWidth;
      int ☃x = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
      int ☃xx = Mouse.getEventButton();
      if (Mouse.getEventButtonState()) {
         if (this.mc.gameSettings.touchscreen && this.touchValue++ > 0) {
            return;
         }

         this.eventButton = ☃xx;
         this.lastMouseEvent = Minecraft.getSystemTime();
         this.mouseClicked(☃, ☃x, this.eventButton);
      } else if (☃xx != -1) {
         if (this.mc.gameSettings.touchscreen && --this.touchValue > 0) {
            return;
         }

         this.eventButton = -1;
         this.mouseReleased(☃, ☃x, ☃xx);
      } else if (this.eventButton != -1 && this.lastMouseEvent > 0L) {
         long ☃xxx = Minecraft.getSystemTime() - this.lastMouseEvent;
         this.mouseClickMove(☃, ☃x, this.eventButton, ☃xxx);
      }
   }

   public void handleKeyboardInput() {
      char ☃ = Keyboard.getEventCharacter();
      if (Keyboard.getEventKey() == 0 && ☃ >= ' ' || Keyboard.getEventKeyState()) {
         this.keyTyped(☃, Keyboard.getEventKey());
      }

      this.mc.dispatchKeypresses();
   }

   public void updateScreen() {
   }

   public void onGuiClosed() {
   }

   public void drawDefaultBackground() {
      this.drawWorldBackground(0);
   }

   public void drawWorldBackground(int var1) {
      if (this.mc.world != null) {
         this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
      } else {
         this.drawBackground(☃);
      }
   }

   public void drawBackground(int var1) {
      GlStateManager.disableLighting();
      GlStateManager.disableFog();
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      this.mc.getTextureManager().bindTexture(OPTIONS_BACKGROUND);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      float ☃xx = 32.0F;
      ☃x.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
      ☃x.pos(0.0, this.height, 0.0).tex(0.0, this.height / 32.0F + ☃).color(64, 64, 64, 255).endVertex();
      ☃x.pos(this.width, this.height, 0.0).tex(this.width / 32.0F, this.height / 32.0F + ☃).color(64, 64, 64, 255).endVertex();
      ☃x.pos(this.width, 0.0, 0.0).tex(this.width / 32.0F, ☃).color(64, 64, 64, 255).endVertex();
      ☃x.pos(0.0, 0.0, 0.0).tex(0.0, ☃).color(64, 64, 64, 255).endVertex();
      ☃.draw();
   }

   public boolean doesGuiPauseGame() {
      return true;
   }

   @Override
   public void confirmClicked(boolean var1, int var2) {
      if (☃ == 31102009) {
         if (☃) {
            this.openWebLink(this.clickedLinkURI);
         }

         this.clickedLinkURI = null;
         this.mc.displayGuiScreen(this);
      }
   }

   private void openWebLink(URI var1) {
      try {
         Class<?> ☃ = Class.forName("java.awt.Desktop");
         Object ☃x = ☃.getMethod("getDesktop").invoke(null);
         ☃.getMethod("browse", URI.class).invoke(☃x, ☃);
      } catch (Throwable var4) {
         Throwable ☃xx = var4.getCause();
         LOGGER.error("Couldn't open link: {}", ☃xx == null ? "<UNKNOWN>" : ☃xx.getMessage());
      }
   }

   public static boolean isCtrlKeyDown() {
      return Minecraft.IS_RUNNING_ON_MAC ? Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220) : Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
   }

   public static boolean isShiftKeyDown() {
      return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
   }

   public static boolean isAltKeyDown() {
      return Keyboard.isKeyDown(56) || Keyboard.isKeyDown(184);
   }

   public static boolean isKeyComboCtrlX(int var0) {
      return ☃ == 45 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown();
   }

   public static boolean isKeyComboCtrlV(int var0) {
      return ☃ == 47 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown();
   }

   public static boolean isKeyComboCtrlC(int var0) {
      return ☃ == 46 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown();
   }

   public static boolean isKeyComboCtrlA(int var0) {
      return ☃ == 30 && isCtrlKeyDown() && !isShiftKeyDown() && !isAltKeyDown();
   }

   public void onResize(Minecraft var1, int var2, int var3) {
      this.setWorldAndResolution(☃, ☃, ☃);
   }
}
