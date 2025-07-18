package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;
import io.netty.buffer.Unpooled;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class GuiScreenBook extends GuiScreen {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final ResourceLocation BOOK_GUI_TEXTURES = new ResourceLocation("textures/gui/book.png");
   private final EntityPlayer editingPlayer;
   private final ItemStack book;
   private final boolean bookIsUnsigned;
   private boolean bookIsModified;
   private boolean bookGettingSigned;
   private int updateCount;
   private final int bookImageWidth = 192;
   private final int bookImageHeight = 192;
   private int bookTotalPages = 1;
   private int currPage;
   private NBTTagList bookPages;
   private String bookTitle = "";
   private List<ITextComponent> cachedComponents;
   private int cachedPage = -1;
   private GuiScreenBook.NextPageButton buttonNextPage;
   private GuiScreenBook.NextPageButton buttonPreviousPage;
   private GuiButton buttonDone;
   private GuiButton buttonSign;
   private GuiButton buttonFinalize;
   private GuiButton buttonCancel;

   public GuiScreenBook(EntityPlayer var1, ItemStack var2, boolean var3) {
      this.editingPlayer = ☃;
      this.book = ☃;
      this.bookIsUnsigned = ☃;
      if (☃.hasTagCompound()) {
         NBTTagCompound ☃ = ☃.getTagCompound();
         this.bookPages = ☃.getTagList("pages", 8).copy();
         this.bookTotalPages = this.bookPages.tagCount();
         if (this.bookTotalPages < 1) {
            this.bookTotalPages = 1;
         }
      }

      if (this.bookPages == null && ☃) {
         this.bookPages = new NBTTagList();
         this.bookPages.appendTag(new NBTTagString(""));
         this.bookTotalPages = 1;
      }
   }

   @Override
   public void updateScreen() {
      super.updateScreen();
      this.updateCount++;
   }

   @Override
   public void initGui() {
      this.buttonList.clear();
      Keyboard.enableRepeatEvents(true);
      if (this.bookIsUnsigned) {
         this.buttonSign = this.addButton(new GuiButton(3, this.width / 2 - 100, 196, 98, 20, I18n.format("book.signButton")));
         this.buttonDone = this.addButton(new GuiButton(0, this.width / 2 + 2, 196, 98, 20, I18n.format("gui.done")));
         this.buttonFinalize = this.addButton(new GuiButton(5, this.width / 2 - 100, 196, 98, 20, I18n.format("book.finalizeButton")));
         this.buttonCancel = this.addButton(new GuiButton(4, this.width / 2 + 2, 196, 98, 20, I18n.format("gui.cancel")));
      } else {
         this.buttonDone = this.addButton(new GuiButton(0, this.width / 2 - 100, 196, 200, 20, I18n.format("gui.done")));
      }

      int ☃ = (this.width - 192) / 2;
      int ☃x = 2;
      this.buttonNextPage = this.addButton(new GuiScreenBook.NextPageButton(1, ☃ + 120, 156, true));
      this.buttonPreviousPage = this.addButton(new GuiScreenBook.NextPageButton(2, ☃ + 38, 156, false));
      this.updateButtons();
   }

   @Override
   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   private void updateButtons() {
      this.buttonNextPage.visible = !this.bookGettingSigned && (this.currPage < this.bookTotalPages - 1 || this.bookIsUnsigned);
      this.buttonPreviousPage.visible = !this.bookGettingSigned && this.currPage > 0;
      this.buttonDone.visible = !this.bookIsUnsigned || !this.bookGettingSigned;
      if (this.bookIsUnsigned) {
         this.buttonSign.visible = !this.bookGettingSigned;
         this.buttonCancel.visible = this.bookGettingSigned;
         this.buttonFinalize.visible = this.bookGettingSigned;
         this.buttonFinalize.enabled = !this.bookTitle.trim().isEmpty();
      }
   }

   private void sendBookToServer(boolean var1) {
      if (this.bookIsUnsigned && this.bookIsModified) {
         if (this.bookPages != null) {
            while (this.bookPages.tagCount() > 1) {
               String ☃ = this.bookPages.getStringTagAt(this.bookPages.tagCount() - 1);
               if (!☃.isEmpty()) {
                  break;
               }

               this.bookPages.removeTag(this.bookPages.tagCount() - 1);
            }

            if (this.book.hasTagCompound()) {
               NBTTagCompound ☃ = this.book.getTagCompound();
               ☃.setTag("pages", this.bookPages);
            } else {
               this.book.setTagInfo("pages", this.bookPages);
            }

            String ☃ = "MC|BEdit";
            if (☃) {
               ☃ = "MC|BSign";
               this.book.setTagInfo("author", new NBTTagString(this.editingPlayer.getName()));
               this.book.setTagInfo("title", new NBTTagString(this.bookTitle.trim()));
            }

            PacketBuffer ☃x = new PacketBuffer(Unpooled.buffer());
            ☃x.writeItemStack(this.book);
            this.mc.getConnection().sendPacket(new CPacketCustomPayload(☃, ☃x));
         }
      }
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.enabled) {
         if (☃.id == 0) {
            this.mc.displayGuiScreen(null);
            this.sendBookToServer(false);
         } else if (☃.id == 3 && this.bookIsUnsigned) {
            this.bookGettingSigned = true;
         } else if (☃.id == 1) {
            if (this.currPage < this.bookTotalPages - 1) {
               this.currPage++;
            } else if (this.bookIsUnsigned) {
               this.addNewPage();
               if (this.currPage < this.bookTotalPages - 1) {
                  this.currPage++;
               }
            }
         } else if (☃.id == 2) {
            if (this.currPage > 0) {
               this.currPage--;
            }
         } else if (☃.id == 5 && this.bookGettingSigned) {
            this.sendBookToServer(true);
            this.mc.displayGuiScreen(null);
         } else if (☃.id == 4 && this.bookGettingSigned) {
            this.bookGettingSigned = false;
         }

         this.updateButtons();
      }
   }

   private void addNewPage() {
      if (this.bookPages != null && this.bookPages.tagCount() < 50) {
         this.bookPages.appendTag(new NBTTagString(""));
         this.bookTotalPages++;
         this.bookIsModified = true;
      }
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      super.keyTyped(☃, ☃);
      if (this.bookIsUnsigned) {
         if (this.bookGettingSigned) {
            this.keyTypedInTitle(☃, ☃);
         } else {
            this.keyTypedInBook(☃, ☃);
         }
      }
   }

   private void keyTypedInBook(char var1, int var2) {
      if (GuiScreen.isKeyComboCtrlV(☃)) {
         this.pageInsertIntoCurrent(GuiScreen.getClipboardString());
      } else {
         switch (☃) {
            case 14:
               String ☃ = this.pageGetCurrent();
               if (!☃.isEmpty()) {
                  this.pageSetCurrent(☃.substring(0, ☃.length() - 1));
               }

               return;
            case 28:
            case 156:
               this.pageInsertIntoCurrent("\n");
               return;
            default:
               if (ChatAllowedCharacters.isAllowedCharacter(☃)) {
                  this.pageInsertIntoCurrent(Character.toString(☃));
               }
         }
      }
   }

   private void keyTypedInTitle(char var1, int var2) {
      switch (☃) {
         case 14:
            if (!this.bookTitle.isEmpty()) {
               this.bookTitle = this.bookTitle.substring(0, this.bookTitle.length() - 1);
               this.updateButtons();
            }

            return;
         case 28:
         case 156:
            if (!this.bookTitle.isEmpty()) {
               this.sendBookToServer(true);
               this.mc.displayGuiScreen(null);
            }

            return;
         default:
            if (this.bookTitle.length() < 16 && ChatAllowedCharacters.isAllowedCharacter(☃)) {
               this.bookTitle = this.bookTitle + Character.toString(☃);
               this.updateButtons();
               this.bookIsModified = true;
            }
      }
   }

   private String pageGetCurrent() {
      return this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount() ? this.bookPages.getStringTagAt(this.currPage) : "";
   }

   private void pageSetCurrent(String var1) {
      if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount()) {
         this.bookPages.set(this.currPage, new NBTTagString(☃));
         this.bookIsModified = true;
      }
   }

   private void pageInsertIntoCurrent(String var1) {
      String ☃ = this.pageGetCurrent();
      String ☃x = ☃ + ☃;
      int ☃xx = this.fontRenderer.getWordWrappedHeight(☃x + "" + TextFormatting.BLACK + "_", 118);
      if (☃xx <= 128 && ☃x.length() < 256) {
         this.pageSetCurrent(☃x);
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(BOOK_GUI_TEXTURES);
      int ☃ = (this.width - 192) / 2;
      int ☃x = 2;
      this.drawTexturedModalRect(☃, 2, 0, 0, 192, 192);
      if (this.bookGettingSigned) {
         String ☃xx = this.bookTitle;
         if (this.bookIsUnsigned) {
            if (this.updateCount / 6 % 2 == 0) {
               ☃xx = ☃xx + "" + TextFormatting.BLACK + "_";
            } else {
               ☃xx = ☃xx + "" + TextFormatting.GRAY + "_";
            }
         }

         String ☃xxx = I18n.format("book.editTitle");
         int ☃xxxx = this.fontRenderer.getStringWidth(☃xxx);
         this.fontRenderer.drawString(☃xxx, ☃ + 36 + (116 - ☃xxxx) / 2, 34, 0);
         int ☃xxxxx = this.fontRenderer.getStringWidth(☃xx);
         this.fontRenderer.drawString(☃xx, ☃ + 36 + (116 - ☃xxxxx) / 2, 50, 0);
         String ☃xxxxxx = I18n.format("book.byAuthor", this.editingPlayer.getName());
         int ☃xxxxxxx = this.fontRenderer.getStringWidth(☃xxxxxx);
         this.fontRenderer.drawString(TextFormatting.DARK_GRAY + ☃xxxxxx, ☃ + 36 + (116 - ☃xxxxxxx) / 2, 60, 0);
         String ☃xxxxxxxx = I18n.format("book.finalizeWarning");
         this.fontRenderer.drawSplitString(☃xxxxxxxx, ☃ + 36, 82, 116, 0);
      } else {
         String ☃xx = I18n.format("book.pageIndicator", this.currPage + 1, this.bookTotalPages);
         String ☃xxx = "";
         if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount()) {
            ☃xxx = this.bookPages.getStringTagAt(this.currPage);
         }

         if (this.bookIsUnsigned) {
            if (this.fontRenderer.getBidiFlag()) {
               ☃xxx = ☃xxx + "_";
            } else if (this.updateCount / 6 % 2 == 0) {
               ☃xxx = ☃xxx + "" + TextFormatting.BLACK + "_";
            } else {
               ☃xxx = ☃xxx + "" + TextFormatting.GRAY + "_";
            }
         } else if (this.cachedPage != this.currPage) {
            if (ItemWrittenBook.validBookTagContents(this.book.getTagCompound())) {
               try {
                  ITextComponent ☃xxxx = ITextComponent.Serializer.jsonToComponent(☃xxx);
                  this.cachedComponents = ☃xxxx != null ? GuiUtilRenderComponents.splitText(☃xxxx, 116, this.fontRenderer, true, true) : null;
               } catch (JsonParseException var13) {
                  this.cachedComponents = null;
               }
            } else {
               TextComponentString ☃xxxx = new TextComponentString(TextFormatting.DARK_RED + "* Invalid book tag *");
               this.cachedComponents = Lists.newArrayList(☃xxxx);
            }

            this.cachedPage = this.currPage;
         }

         int ☃xxxx = this.fontRenderer.getStringWidth(☃xx);
         this.fontRenderer.drawString(☃xx, ☃ - ☃xxxx + 192 - 44, 18, 0);
         if (this.cachedComponents == null) {
            this.fontRenderer.drawSplitString(☃xxx, ☃ + 36, 34, 116, 0);
         } else {
            int ☃xxxxx = Math.min(128 / this.fontRenderer.FONT_HEIGHT, this.cachedComponents.size());

            for (int ☃xxxxxx = 0; ☃xxxxxx < ☃xxxxx; ☃xxxxxx++) {
               ITextComponent ☃xxxxxxx = this.cachedComponents.get(☃xxxxxx);
               this.fontRenderer.drawString(☃xxxxxxx.getUnformattedText(), ☃ + 36, 34 + ☃xxxxxx * this.fontRenderer.FONT_HEIGHT, 0);
            }

            ITextComponent ☃xxxxxx = this.getClickedComponentAt(☃, ☃);
            if (☃xxxxxx != null) {
               this.handleComponentHover(☃xxxxxx, ☃, ☃);
            }
         }
      }

      super.drawScreen(☃, ☃, ☃);
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      if (☃ == 0) {
         ITextComponent ☃ = this.getClickedComponentAt(☃, ☃);
         if (☃ != null && this.handleComponentClick(☃)) {
            return;
         }
      }

      super.mouseClicked(☃, ☃, ☃);
   }

   @Override
   public boolean handleComponentClick(ITextComponent var1) {
      ClickEvent ☃ = ☃.getStyle().getClickEvent();
      if (☃ == null) {
         return false;
      } else if (☃.getAction() == ClickEvent.Action.CHANGE_PAGE) {
         String ☃x = ☃.getValue();

         try {
            int ☃xx = Integer.parseInt(☃x) - 1;
            if (☃xx >= 0 && ☃xx < this.bookTotalPages && ☃xx != this.currPage) {
               this.currPage = ☃xx;
               this.updateButtons();
               return true;
            }
         } catch (Throwable var5) {
         }

         return false;
      } else {
         boolean ☃x = super.handleComponentClick(☃);
         if (☃x && ☃.getAction() == ClickEvent.Action.RUN_COMMAND) {
            this.mc.displayGuiScreen(null);
         }

         return ☃x;
      }
   }

   @Nullable
   public ITextComponent getClickedComponentAt(int var1, int var2) {
      if (this.cachedComponents == null) {
         return null;
      } else {
         int ☃ = ☃ - (this.width - 192) / 2 - 36;
         int ☃x = ☃ - 2 - 16 - 16;
         if (☃ >= 0 && ☃x >= 0) {
            int ☃xx = Math.min(128 / this.fontRenderer.FONT_HEIGHT, this.cachedComponents.size());
            if (☃ <= 116 && ☃x < this.mc.fontRenderer.FONT_HEIGHT * ☃xx + ☃xx) {
               int ☃xxx = ☃x / this.mc.fontRenderer.FONT_HEIGHT;
               if (☃xxx >= 0 && ☃xxx < this.cachedComponents.size()) {
                  ITextComponent ☃xxxx = this.cachedComponents.get(☃xxx);
                  int ☃xxxxx = 0;

                  for (ITextComponent ☃xxxxxx : ☃xxxx) {
                     if (☃xxxxxx instanceof TextComponentString) {
                        ☃xxxxx += this.mc.fontRenderer.getStringWidth(((TextComponentString)☃xxxxxx).getText());
                        if (☃xxxxx > ☃) {
                           return ☃xxxxxx;
                        }
                     }
                  }
               }

               return null;
            } else {
               return null;
            }
         } else {
            return null;
         }
      }
   }

   static class NextPageButton extends GuiButton {
      private final boolean isForward;

      public NextPageButton(int var1, int var2, int var3, boolean var4) {
         super(☃, ☃, ☃, 23, 13, "");
         this.isForward = ☃;
      }

      @Override
      public void drawButton(Minecraft var1, int var2, int var3, float var4) {
         if (this.visible) {
            boolean ☃ = ☃ >= this.x && ☃ >= this.y && ☃ < this.x + this.width && ☃ < this.y + this.height;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            ☃.getTextureManager().bindTexture(GuiScreenBook.BOOK_GUI_TEXTURES);
            int ☃x = 0;
            int ☃xx = 192;
            if (☃) {
               ☃x += 23;
            }

            if (!this.isForward) {
               ☃xx += 13;
            }

            this.drawTexturedModalRect(this.x, this.y, ☃x, ☃xx, 23, 13);
         }
      }
   }
}
