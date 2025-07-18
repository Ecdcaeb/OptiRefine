package net.minecraft.client.gui.inventory;

import com.google.common.collect.Lists;
import io.netty.buffer.Unpooled;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class GuiEditStructure extends GuiScreen {
   private static final Logger LOGGER = LogManager.getLogger();
   public static final int[] LEGAL_KEY_CODES = new int[]{203, 205, 14, 211, 199, 207};
   private final TileEntityStructure tileStructure;
   private Mirror mirror = Mirror.NONE;
   private Rotation rotation = Rotation.NONE;
   private TileEntityStructure.Mode mode = TileEntityStructure.Mode.DATA;
   private boolean ignoreEntities;
   private boolean showAir;
   private boolean showBoundingBox;
   private GuiTextField nameEdit;
   private GuiTextField posXEdit;
   private GuiTextField posYEdit;
   private GuiTextField posZEdit;
   private GuiTextField sizeXEdit;
   private GuiTextField sizeYEdit;
   private GuiTextField sizeZEdit;
   private GuiTextField integrityEdit;
   private GuiTextField seedEdit;
   private GuiTextField dataEdit;
   private GuiButton doneButton;
   private GuiButton cancelButton;
   private GuiButton saveButton;
   private GuiButton loadButton;
   private GuiButton rotateZeroDegreesButton;
   private GuiButton rotateNinetyDegreesButton;
   private GuiButton rotate180DegreesButton;
   private GuiButton rotate270DegressButton;
   private GuiButton modeButton;
   private GuiButton detectSizeButton;
   private GuiButton showEntitiesButton;
   private GuiButton mirrorButton;
   private GuiButton showAirButton;
   private GuiButton showBoundingBoxButton;
   private final List<GuiTextField> tabOrder = Lists.newArrayList();
   private final DecimalFormat decimalFormat = new DecimalFormat("0.0###");

   public GuiEditStructure(TileEntityStructure var1) {
      this.tileStructure = ☃;
      this.decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
   }

   @Override
   public void updateScreen() {
      this.nameEdit.updateCursorCounter();
      this.posXEdit.updateCursorCounter();
      this.posYEdit.updateCursorCounter();
      this.posZEdit.updateCursorCounter();
      this.sizeXEdit.updateCursorCounter();
      this.sizeYEdit.updateCursorCounter();
      this.sizeZEdit.updateCursorCounter();
      this.integrityEdit.updateCursorCounter();
      this.seedEdit.updateCursorCounter();
      this.dataEdit.updateCursorCounter();
   }

   @Override
   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      this.doneButton = this.addButton(new GuiButton(0, this.width / 2 - 4 - 150, 210, 150, 20, I18n.format("gui.done")));
      this.cancelButton = this.addButton(new GuiButton(1, this.width / 2 + 4, 210, 150, 20, I18n.format("gui.cancel")));
      this.saveButton = this.addButton(new GuiButton(9, this.width / 2 + 4 + 100, 185, 50, 20, I18n.format("structure_block.button.save")));
      this.loadButton = this.addButton(new GuiButton(10, this.width / 2 + 4 + 100, 185, 50, 20, I18n.format("structure_block.button.load")));
      this.modeButton = this.addButton(new GuiButton(18, this.width / 2 - 4 - 150, 185, 50, 20, "MODE"));
      this.detectSizeButton = this.addButton(new GuiButton(19, this.width / 2 + 4 + 100, 120, 50, 20, I18n.format("structure_block.button.detect_size")));
      this.showEntitiesButton = this.addButton(new GuiButton(20, this.width / 2 + 4 + 100, 160, 50, 20, "ENTITIES"));
      this.mirrorButton = this.addButton(new GuiButton(21, this.width / 2 - 20, 185, 40, 20, "MIRROR"));
      this.showAirButton = this.addButton(new GuiButton(22, this.width / 2 + 4 + 100, 80, 50, 20, "SHOWAIR"));
      this.showBoundingBoxButton = this.addButton(new GuiButton(23, this.width / 2 + 4 + 100, 80, 50, 20, "SHOWBB"));
      this.rotateZeroDegreesButton = this.addButton(new GuiButton(11, this.width / 2 - 1 - 40 - 1 - 40 - 20, 185, 40, 20, "0"));
      this.rotateNinetyDegreesButton = this.addButton(new GuiButton(12, this.width / 2 - 1 - 40 - 20, 185, 40, 20, "90"));
      this.rotate180DegreesButton = this.addButton(new GuiButton(13, this.width / 2 + 1 + 20, 185, 40, 20, "180"));
      this.rotate270DegressButton = this.addButton(new GuiButton(14, this.width / 2 + 1 + 40 + 1 + 20, 185, 40, 20, "270"));
      this.nameEdit = new GuiTextField(2, this.fontRenderer, this.width / 2 - 152, 40, 300, 20);
      this.nameEdit.setMaxStringLength(64);
      this.nameEdit.setText(this.tileStructure.getName());
      this.tabOrder.add(this.nameEdit);
      BlockPos ☃ = this.tileStructure.getPosition();
      this.posXEdit = new GuiTextField(3, this.fontRenderer, this.width / 2 - 152, 80, 80, 20);
      this.posXEdit.setMaxStringLength(15);
      this.posXEdit.setText(Integer.toString(☃.getX()));
      this.tabOrder.add(this.posXEdit);
      this.posYEdit = new GuiTextField(4, this.fontRenderer, this.width / 2 - 72, 80, 80, 20);
      this.posYEdit.setMaxStringLength(15);
      this.posYEdit.setText(Integer.toString(☃.getY()));
      this.tabOrder.add(this.posYEdit);
      this.posZEdit = new GuiTextField(5, this.fontRenderer, this.width / 2 + 8, 80, 80, 20);
      this.posZEdit.setMaxStringLength(15);
      this.posZEdit.setText(Integer.toString(☃.getZ()));
      this.tabOrder.add(this.posZEdit);
      BlockPos ☃x = this.tileStructure.getStructureSize();
      this.sizeXEdit = new GuiTextField(6, this.fontRenderer, this.width / 2 - 152, 120, 80, 20);
      this.sizeXEdit.setMaxStringLength(15);
      this.sizeXEdit.setText(Integer.toString(☃x.getX()));
      this.tabOrder.add(this.sizeXEdit);
      this.sizeYEdit = new GuiTextField(7, this.fontRenderer, this.width / 2 - 72, 120, 80, 20);
      this.sizeYEdit.setMaxStringLength(15);
      this.sizeYEdit.setText(Integer.toString(☃x.getY()));
      this.tabOrder.add(this.sizeYEdit);
      this.sizeZEdit = new GuiTextField(8, this.fontRenderer, this.width / 2 + 8, 120, 80, 20);
      this.sizeZEdit.setMaxStringLength(15);
      this.sizeZEdit.setText(Integer.toString(☃x.getZ()));
      this.tabOrder.add(this.sizeZEdit);
      this.integrityEdit = new GuiTextField(15, this.fontRenderer, this.width / 2 - 152, 120, 80, 20);
      this.integrityEdit.setMaxStringLength(15);
      this.integrityEdit.setText(this.decimalFormat.format(this.tileStructure.getIntegrity()));
      this.tabOrder.add(this.integrityEdit);
      this.seedEdit = new GuiTextField(16, this.fontRenderer, this.width / 2 - 72, 120, 80, 20);
      this.seedEdit.setMaxStringLength(31);
      this.seedEdit.setText(Long.toString(this.tileStructure.getSeed()));
      this.tabOrder.add(this.seedEdit);
      this.dataEdit = new GuiTextField(17, this.fontRenderer, this.width / 2 - 152, 120, 240, 20);
      this.dataEdit.setMaxStringLength(128);
      this.dataEdit.setText(this.tileStructure.getMetadata());
      this.tabOrder.add(this.dataEdit);
      this.mirror = this.tileStructure.getMirror();
      this.updateMirrorButton();
      this.rotation = this.tileStructure.getRotation();
      this.updateDirectionButtons();
      this.mode = this.tileStructure.getMode();
      this.updateMode();
      this.ignoreEntities = this.tileStructure.ignoresEntities();
      this.updateEntitiesButton();
      this.showAir = this.tileStructure.showsAir();
      this.updateToggleAirButton();
      this.showBoundingBox = this.tileStructure.showsBoundingBox();
      this.updateToggleBoundingBox();
   }

   @Override
   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.enabled) {
         if (☃.id == 1) {
            this.tileStructure.setMirror(this.mirror);
            this.tileStructure.setRotation(this.rotation);
            this.tileStructure.setMode(this.mode);
            this.tileStructure.setIgnoresEntities(this.ignoreEntities);
            this.tileStructure.setShowAir(this.showAir);
            this.tileStructure.setShowBoundingBox(this.showBoundingBox);
            this.mc.displayGuiScreen(null);
         } else if (☃.id == 0) {
            if (this.sendToServer(1)) {
               this.mc.displayGuiScreen(null);
            }
         } else if (☃.id == 9) {
            if (this.tileStructure.getMode() == TileEntityStructure.Mode.SAVE) {
               this.sendToServer(2);
               this.mc.displayGuiScreen(null);
            }
         } else if (☃.id == 10) {
            if (this.tileStructure.getMode() == TileEntityStructure.Mode.LOAD) {
               this.sendToServer(3);
               this.mc.displayGuiScreen(null);
            }
         } else if (☃.id == 11) {
            this.tileStructure.setRotation(Rotation.NONE);
            this.updateDirectionButtons();
         } else if (☃.id == 12) {
            this.tileStructure.setRotation(Rotation.CLOCKWISE_90);
            this.updateDirectionButtons();
         } else if (☃.id == 13) {
            this.tileStructure.setRotation(Rotation.CLOCKWISE_180);
            this.updateDirectionButtons();
         } else if (☃.id == 14) {
            this.tileStructure.setRotation(Rotation.COUNTERCLOCKWISE_90);
            this.updateDirectionButtons();
         } else if (☃.id == 18) {
            this.tileStructure.nextMode();
            this.updateMode();
         } else if (☃.id == 19) {
            if (this.tileStructure.getMode() == TileEntityStructure.Mode.SAVE) {
               this.sendToServer(4);
               this.mc.displayGuiScreen(null);
            }
         } else if (☃.id == 20) {
            this.tileStructure.setIgnoresEntities(!this.tileStructure.ignoresEntities());
            this.updateEntitiesButton();
         } else if (☃.id == 22) {
            this.tileStructure.setShowAir(!this.tileStructure.showsAir());
            this.updateToggleAirButton();
         } else if (☃.id == 23) {
            this.tileStructure.setShowBoundingBox(!this.tileStructure.showsBoundingBox());
            this.updateToggleBoundingBox();
         } else if (☃.id == 21) {
            switch (this.tileStructure.getMirror()) {
               case NONE:
                  this.tileStructure.setMirror(Mirror.LEFT_RIGHT);
                  break;
               case LEFT_RIGHT:
                  this.tileStructure.setMirror(Mirror.FRONT_BACK);
                  break;
               case FRONT_BACK:
                  this.tileStructure.setMirror(Mirror.NONE);
            }

            this.updateMirrorButton();
         }
      }
   }

   private void updateEntitiesButton() {
      boolean ☃ = !this.tileStructure.ignoresEntities();
      if (☃) {
         this.showEntitiesButton.displayString = I18n.format("options.on");
      } else {
         this.showEntitiesButton.displayString = I18n.format("options.off");
      }
   }

   private void updateToggleAirButton() {
      boolean ☃ = this.tileStructure.showsAir();
      if (☃) {
         this.showAirButton.displayString = I18n.format("options.on");
      } else {
         this.showAirButton.displayString = I18n.format("options.off");
      }
   }

   private void updateToggleBoundingBox() {
      boolean ☃ = this.tileStructure.showsBoundingBox();
      if (☃) {
         this.showBoundingBoxButton.displayString = I18n.format("options.on");
      } else {
         this.showBoundingBoxButton.displayString = I18n.format("options.off");
      }
   }

   private void updateMirrorButton() {
      Mirror ☃ = this.tileStructure.getMirror();
      switch (☃) {
         case NONE:
            this.mirrorButton.displayString = "|";
            break;
         case LEFT_RIGHT:
            this.mirrorButton.displayString = "< >";
            break;
         case FRONT_BACK:
            this.mirrorButton.displayString = "^ v";
      }
   }

   private void updateDirectionButtons() {
      this.rotateZeroDegreesButton.enabled = true;
      this.rotateNinetyDegreesButton.enabled = true;
      this.rotate180DegreesButton.enabled = true;
      this.rotate270DegressButton.enabled = true;
      switch (this.tileStructure.getRotation()) {
         case NONE:
            this.rotateZeroDegreesButton.enabled = false;
            break;
         case CLOCKWISE_180:
            this.rotate180DegreesButton.enabled = false;
            break;
         case COUNTERCLOCKWISE_90:
            this.rotate270DegressButton.enabled = false;
            break;
         case CLOCKWISE_90:
            this.rotateNinetyDegreesButton.enabled = false;
      }
   }

   private void updateMode() {
      this.nameEdit.setFocused(false);
      this.posXEdit.setFocused(false);
      this.posYEdit.setFocused(false);
      this.posZEdit.setFocused(false);
      this.sizeXEdit.setFocused(false);
      this.sizeYEdit.setFocused(false);
      this.sizeZEdit.setFocused(false);
      this.integrityEdit.setFocused(false);
      this.seedEdit.setFocused(false);
      this.dataEdit.setFocused(false);
      this.nameEdit.setVisible(false);
      this.nameEdit.setFocused(false);
      this.posXEdit.setVisible(false);
      this.posYEdit.setVisible(false);
      this.posZEdit.setVisible(false);
      this.sizeXEdit.setVisible(false);
      this.sizeYEdit.setVisible(false);
      this.sizeZEdit.setVisible(false);
      this.integrityEdit.setVisible(false);
      this.seedEdit.setVisible(false);
      this.dataEdit.setVisible(false);
      this.saveButton.visible = false;
      this.loadButton.visible = false;
      this.detectSizeButton.visible = false;
      this.showEntitiesButton.visible = false;
      this.mirrorButton.visible = false;
      this.rotateZeroDegreesButton.visible = false;
      this.rotateNinetyDegreesButton.visible = false;
      this.rotate180DegreesButton.visible = false;
      this.rotate270DegressButton.visible = false;
      this.showAirButton.visible = false;
      this.showBoundingBoxButton.visible = false;
      switch (this.tileStructure.getMode()) {
         case SAVE:
            this.nameEdit.setVisible(true);
            this.nameEdit.setFocused(true);
            this.posXEdit.setVisible(true);
            this.posYEdit.setVisible(true);
            this.posZEdit.setVisible(true);
            this.sizeXEdit.setVisible(true);
            this.sizeYEdit.setVisible(true);
            this.sizeZEdit.setVisible(true);
            this.saveButton.visible = true;
            this.detectSizeButton.visible = true;
            this.showEntitiesButton.visible = true;
            this.showAirButton.visible = true;
            break;
         case LOAD:
            this.nameEdit.setVisible(true);
            this.nameEdit.setFocused(true);
            this.posXEdit.setVisible(true);
            this.posYEdit.setVisible(true);
            this.posZEdit.setVisible(true);
            this.integrityEdit.setVisible(true);
            this.seedEdit.setVisible(true);
            this.loadButton.visible = true;
            this.showEntitiesButton.visible = true;
            this.mirrorButton.visible = true;
            this.rotateZeroDegreesButton.visible = true;
            this.rotateNinetyDegreesButton.visible = true;
            this.rotate180DegreesButton.visible = true;
            this.rotate270DegressButton.visible = true;
            this.showBoundingBoxButton.visible = true;
            this.updateDirectionButtons();
            break;
         case CORNER:
            this.nameEdit.setVisible(true);
            this.nameEdit.setFocused(true);
            break;
         case DATA:
            this.dataEdit.setVisible(true);
            this.dataEdit.setFocused(true);
      }

      this.modeButton.displayString = I18n.format("structure_block.mode." + this.tileStructure.getMode().getName());
   }

   private boolean sendToServer(int var1) {
      try {
         PacketBuffer ☃ = new PacketBuffer(Unpooled.buffer());
         this.tileStructure.writeCoordinates(☃);
         ☃.writeByte(☃);
         ☃.writeString(this.tileStructure.getMode().toString());
         ☃.writeString(this.nameEdit.getText());
         ☃.writeInt(this.parseCoordinate(this.posXEdit.getText()));
         ☃.writeInt(this.parseCoordinate(this.posYEdit.getText()));
         ☃.writeInt(this.parseCoordinate(this.posZEdit.getText()));
         ☃.writeInt(this.parseCoordinate(this.sizeXEdit.getText()));
         ☃.writeInt(this.parseCoordinate(this.sizeYEdit.getText()));
         ☃.writeInt(this.parseCoordinate(this.sizeZEdit.getText()));
         ☃.writeString(this.tileStructure.getMirror().toString());
         ☃.writeString(this.tileStructure.getRotation().toString());
         ☃.writeString(this.dataEdit.getText());
         ☃.writeBoolean(this.tileStructure.ignoresEntities());
         ☃.writeBoolean(this.tileStructure.showsAir());
         ☃.writeBoolean(this.tileStructure.showsBoundingBox());
         ☃.writeFloat(this.parseIntegrity(this.integrityEdit.getText()));
         ☃.writeVarLong(this.parseSeed(this.seedEdit.getText()));
         this.mc.getConnection().sendPacket(new CPacketCustomPayload("MC|Struct", ☃));
         return true;
      } catch (Exception var3) {
         LOGGER.warn("Could not send structure block info", var3);
         return false;
      }
   }

   private long parseSeed(String var1) {
      try {
         return Long.valueOf(☃);
      } catch (NumberFormatException var3) {
         return 0L;
      }
   }

   private float parseIntegrity(String var1) {
      try {
         return Float.valueOf(☃);
      } catch (NumberFormatException var3) {
         return 1.0F;
      }
   }

   private int parseCoordinate(String var1) {
      try {
         return Integer.parseInt(☃);
      } catch (NumberFormatException var3) {
         return 0;
      }
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      if (this.nameEdit.getVisible() && isValidCharacterForName(☃, ☃)) {
         this.nameEdit.textboxKeyTyped(☃, ☃);
      }

      if (this.posXEdit.getVisible()) {
         this.posXEdit.textboxKeyTyped(☃, ☃);
      }

      if (this.posYEdit.getVisible()) {
         this.posYEdit.textboxKeyTyped(☃, ☃);
      }

      if (this.posZEdit.getVisible()) {
         this.posZEdit.textboxKeyTyped(☃, ☃);
      }

      if (this.sizeXEdit.getVisible()) {
         this.sizeXEdit.textboxKeyTyped(☃, ☃);
      }

      if (this.sizeYEdit.getVisible()) {
         this.sizeYEdit.textboxKeyTyped(☃, ☃);
      }

      if (this.sizeZEdit.getVisible()) {
         this.sizeZEdit.textboxKeyTyped(☃, ☃);
      }

      if (this.integrityEdit.getVisible()) {
         this.integrityEdit.textboxKeyTyped(☃, ☃);
      }

      if (this.seedEdit.getVisible()) {
         this.seedEdit.textboxKeyTyped(☃, ☃);
      }

      if (this.dataEdit.getVisible()) {
         this.dataEdit.textboxKeyTyped(☃, ☃);
      }

      if (☃ == 15) {
         GuiTextField ☃ = null;
         GuiTextField ☃x = null;

         for (GuiTextField ☃xx : this.tabOrder) {
            if (☃ != null && ☃xx.getVisible()) {
               ☃x = ☃xx;
               break;
            }

            if (☃xx.isFocused() && ☃xx.getVisible()) {
               ☃ = ☃xx;
            }
         }

         if (☃ != null && ☃x == null) {
            for (GuiTextField ☃xx : this.tabOrder) {
               if (☃xx.getVisible() && ☃xx != ☃) {
                  ☃x = ☃xx;
                  break;
               }
            }
         }

         if (☃x != null && ☃x != ☃) {
            ☃.setFocused(false);
            ☃x.setFocused(true);
         }
      }

      if (☃ == 28 || ☃ == 156) {
         this.actionPerformed(this.doneButton);
      } else if (☃ == 1) {
         this.actionPerformed(this.cancelButton);
      }
   }

   private static boolean isValidCharacterForName(char var0, int var1) {
      boolean ☃ = true;

      for (int ☃x : LEGAL_KEY_CODES) {
         if (☃x == ☃) {
            return true;
         }
      }

      for (char ☃xx : ChatAllowedCharacters.ILLEGAL_STRUCTURE_CHARACTERS) {
         if (☃xx == ☃) {
            ☃ = false;
            break;
         }
      }

      return ☃;
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(☃, ☃, ☃);
      if (this.nameEdit.getVisible()) {
         this.nameEdit.mouseClicked(☃, ☃, ☃);
      }

      if (this.posXEdit.getVisible()) {
         this.posXEdit.mouseClicked(☃, ☃, ☃);
      }

      if (this.posYEdit.getVisible()) {
         this.posYEdit.mouseClicked(☃, ☃, ☃);
      }

      if (this.posZEdit.getVisible()) {
         this.posZEdit.mouseClicked(☃, ☃, ☃);
      }

      if (this.sizeXEdit.getVisible()) {
         this.sizeXEdit.mouseClicked(☃, ☃, ☃);
      }

      if (this.sizeYEdit.getVisible()) {
         this.sizeYEdit.mouseClicked(☃, ☃, ☃);
      }

      if (this.sizeZEdit.getVisible()) {
         this.sizeZEdit.mouseClicked(☃, ☃, ☃);
      }

      if (this.integrityEdit.getVisible()) {
         this.integrityEdit.mouseClicked(☃, ☃, ☃);
      }

      if (this.seedEdit.getVisible()) {
         this.seedEdit.mouseClicked(☃, ☃, ☃);
      }

      if (this.dataEdit.getVisible()) {
         this.dataEdit.mouseClicked(☃, ☃, ☃);
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      TileEntityStructure.Mode ☃ = this.tileStructure.getMode();
      this.drawCenteredString(this.fontRenderer, I18n.format("tile.structureBlock.name"), this.width / 2, 10, 16777215);
      if (☃ != TileEntityStructure.Mode.DATA) {
         this.drawString(this.fontRenderer, I18n.format("structure_block.structure_name"), this.width / 2 - 153, 30, 10526880);
         this.nameEdit.drawTextBox();
      }

      if (☃ == TileEntityStructure.Mode.LOAD || ☃ == TileEntityStructure.Mode.SAVE) {
         this.drawString(this.fontRenderer, I18n.format("structure_block.position"), this.width / 2 - 153, 70, 10526880);
         this.posXEdit.drawTextBox();
         this.posYEdit.drawTextBox();
         this.posZEdit.drawTextBox();
         String ☃x = I18n.format("structure_block.include_entities");
         int ☃xx = this.fontRenderer.getStringWidth(☃x);
         this.drawString(this.fontRenderer, ☃x, this.width / 2 + 154 - ☃xx, 150, 10526880);
      }

      if (☃ == TileEntityStructure.Mode.SAVE) {
         this.drawString(this.fontRenderer, I18n.format("structure_block.size"), this.width / 2 - 153, 110, 10526880);
         this.sizeXEdit.drawTextBox();
         this.sizeYEdit.drawTextBox();
         this.sizeZEdit.drawTextBox();
         String ☃x = I18n.format("structure_block.detect_size");
         int ☃xx = this.fontRenderer.getStringWidth(☃x);
         this.drawString(this.fontRenderer, ☃x, this.width / 2 + 154 - ☃xx, 110, 10526880);
         String ☃xxx = I18n.format("structure_block.show_air");
         int ☃xxxx = this.fontRenderer.getStringWidth(☃xxx);
         this.drawString(this.fontRenderer, ☃xxx, this.width / 2 + 154 - ☃xxxx, 70, 10526880);
      }

      if (☃ == TileEntityStructure.Mode.LOAD) {
         this.drawString(this.fontRenderer, I18n.format("structure_block.integrity"), this.width / 2 - 153, 110, 10526880);
         this.integrityEdit.drawTextBox();
         this.seedEdit.drawTextBox();
         String ☃x = I18n.format("structure_block.show_boundingbox");
         int ☃xx = this.fontRenderer.getStringWidth(☃x);
         this.drawString(this.fontRenderer, ☃x, this.width / 2 + 154 - ☃xx, 70, 10526880);
      }

      if (☃ == TileEntityStructure.Mode.DATA) {
         this.drawString(this.fontRenderer, I18n.format("structure_block.custom_data"), this.width / 2 - 153, 110, 10526880);
         this.dataEdit.drawTextBox();
      }

      String ☃x = "structure_block.mode_info." + ☃.getName();
      this.drawString(this.fontRenderer, I18n.format(☃x), this.width / 2 - 153, 174, 10526880);
      super.drawScreen(☃, ☃, ☃);
   }

   @Override
   public boolean doesGuiPauseGame() {
      return false;
   }
}
