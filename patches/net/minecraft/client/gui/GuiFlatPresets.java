package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;
import org.lwjgl.input.Keyboard;

public class GuiFlatPresets extends GuiScreen {
   private static final List<GuiFlatPresets.LayerItem> FLAT_WORLD_PRESETS = Lists.newArrayList();
   private final GuiCreateFlatWorld parentScreen;
   private String presetsTitle;
   private String presetsShare;
   private String listText;
   private GuiFlatPresets.ListSlot list;
   private GuiButton btnSelect;
   private GuiTextField export;

   public GuiFlatPresets(GuiCreateFlatWorld var1) {
      this.parentScreen = ☃;
   }

   @Override
   public void initGui() {
      this.buttonList.clear();
      Keyboard.enableRepeatEvents(true);
      this.presetsTitle = I18n.format("createWorld.customize.presets.title");
      this.presetsShare = I18n.format("createWorld.customize.presets.share");
      this.listText = I18n.format("createWorld.customize.presets.list");
      this.export = new GuiTextField(2, this.fontRenderer, 50, 40, this.width - 100, 20);
      this.list = new GuiFlatPresets.ListSlot();
      this.export.setMaxStringLength(1230);
      this.export.setText(this.parentScreen.getPreset());
      this.btnSelect = this.addButton(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, I18n.format("createWorld.customize.presets.select")));
      this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, I18n.format("gui.cancel")));
      this.updateButtonValidity();
   }

   @Override
   public void handleMouseInput() {
      super.handleMouseInput();
      this.list.handleMouseInput();
   }

   @Override
   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      this.export.mouseClicked(☃, ☃, ☃);
      super.mouseClicked(☃, ☃, ☃);
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      if (!this.export.textboxKeyTyped(☃, ☃)) {
         super.keyTyped(☃, ☃);
      }
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (☃.id == 0 && this.hasValidSelection()) {
         this.parentScreen.setPreset(this.export.getText());
         this.mc.displayGuiScreen(this.parentScreen);
      } else if (☃.id == 1) {
         this.mc.displayGuiScreen(this.parentScreen);
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.list.drawScreen(☃, ☃, ☃);
      this.drawCenteredString(this.fontRenderer, this.presetsTitle, this.width / 2, 8, 16777215);
      this.drawString(this.fontRenderer, this.presetsShare, 50, 30, 10526880);
      this.drawString(this.fontRenderer, this.listText, 50, 70, 10526880);
      this.export.drawTextBox();
      super.drawScreen(☃, ☃, ☃);
   }

   @Override
   public void updateScreen() {
      this.export.updateCursorCounter();
      super.updateScreen();
   }

   public void updateButtonValidity() {
      this.btnSelect.enabled = this.hasValidSelection();
   }

   private boolean hasValidSelection() {
      return this.list.selected > -1 && this.list.selected < FLAT_WORLD_PRESETS.size() || this.export.getText().length() > 1;
   }

   private static void registerPreset(String var0, Item var1, Biome var2, List<String> var3, FlatLayerInfo... var4) {
      registerPreset(☃, ☃, 0, ☃, ☃, ☃);
   }

   private static void registerPreset(String var0, Item var1, int var2, Biome var3, List<String> var4, FlatLayerInfo... var5) {
      FlatGeneratorInfo ☃ = new FlatGeneratorInfo();

      for (int ☃x = ☃.length - 1; ☃x >= 0; ☃x--) {
         ☃.getFlatLayers().add(☃[☃x]);
      }

      ☃.setBiome(Biome.getIdForBiome(☃));
      ☃.updateLayers();

      for (String ☃x : ☃) {
         ☃.getWorldFeatures().put(☃x, Maps.newHashMap());
      }

      FLAT_WORLD_PRESETS.add(new GuiFlatPresets.LayerItem(☃, ☃, ☃, ☃.toString()));
   }

   static {
      registerPreset(
         I18n.format("createWorld.customize.preset.classic_flat"),
         Item.getItemFromBlock(Blocks.GRASS),
         Biomes.PLAINS,
         Arrays.asList("village"),
         new FlatLayerInfo(1, Blocks.GRASS),
         new FlatLayerInfo(2, Blocks.DIRT),
         new FlatLayerInfo(1, Blocks.BEDROCK)
      );
      registerPreset(
         I18n.format("createWorld.customize.preset.tunnelers_dream"),
         Item.getItemFromBlock(Blocks.STONE),
         Biomes.EXTREME_HILLS,
         Arrays.asList("biome_1", "dungeon", "decoration", "stronghold", "mineshaft"),
         new FlatLayerInfo(1, Blocks.GRASS),
         new FlatLayerInfo(5, Blocks.DIRT),
         new FlatLayerInfo(230, Blocks.STONE),
         new FlatLayerInfo(1, Blocks.BEDROCK)
      );
      registerPreset(
         I18n.format("createWorld.customize.preset.water_world"),
         Items.WATER_BUCKET,
         Biomes.DEEP_OCEAN,
         Arrays.asList("biome_1", "oceanmonument"),
         new FlatLayerInfo(90, Blocks.WATER),
         new FlatLayerInfo(5, Blocks.SAND),
         new FlatLayerInfo(5, Blocks.DIRT),
         new FlatLayerInfo(5, Blocks.STONE),
         new FlatLayerInfo(1, Blocks.BEDROCK)
      );
      registerPreset(
         I18n.format("createWorld.customize.preset.overworld"),
         Item.getItemFromBlock(Blocks.TALLGRASS),
         BlockTallGrass.EnumType.GRASS.getMeta(),
         Biomes.PLAINS,
         Arrays.asList("village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon", "lake", "lava_lake"),
         new FlatLayerInfo(1, Blocks.GRASS),
         new FlatLayerInfo(3, Blocks.DIRT),
         new FlatLayerInfo(59, Blocks.STONE),
         new FlatLayerInfo(1, Blocks.BEDROCK)
      );
      registerPreset(
         I18n.format("createWorld.customize.preset.snowy_kingdom"),
         Item.getItemFromBlock(Blocks.SNOW_LAYER),
         Biomes.ICE_PLAINS,
         Arrays.asList("village", "biome_1"),
         new FlatLayerInfo(1, Blocks.SNOW_LAYER),
         new FlatLayerInfo(1, Blocks.GRASS),
         new FlatLayerInfo(3, Blocks.DIRT),
         new FlatLayerInfo(59, Blocks.STONE),
         new FlatLayerInfo(1, Blocks.BEDROCK)
      );
      registerPreset(
         I18n.format("createWorld.customize.preset.bottomless_pit"),
         Items.FEATHER,
         Biomes.PLAINS,
         Arrays.asList("village", "biome_1"),
         new FlatLayerInfo(1, Blocks.GRASS),
         new FlatLayerInfo(3, Blocks.DIRT),
         new FlatLayerInfo(2, Blocks.COBBLESTONE)
      );
      registerPreset(
         I18n.format("createWorld.customize.preset.desert"),
         Item.getItemFromBlock(Blocks.SAND),
         Biomes.DESERT,
         Arrays.asList("village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon"),
         new FlatLayerInfo(8, Blocks.SAND),
         new FlatLayerInfo(52, Blocks.SANDSTONE),
         new FlatLayerInfo(3, Blocks.STONE),
         new FlatLayerInfo(1, Blocks.BEDROCK)
      );
      registerPreset(
         I18n.format("createWorld.customize.preset.redstone_ready"),
         Items.REDSTONE,
         Biomes.DESERT,
         Collections.emptyList(),
         new FlatLayerInfo(52, Blocks.SANDSTONE),
         new FlatLayerInfo(3, Blocks.STONE),
         new FlatLayerInfo(1, Blocks.BEDROCK)
      );
      registerPreset(
         I18n.format("createWorld.customize.preset.the_void"),
         Item.getItemFromBlock(Blocks.BARRIER),
         Biomes.VOID,
         Arrays.asList("decoration"),
         new FlatLayerInfo(1, Blocks.AIR)
      );
   }

   static class LayerItem {
      public Item icon;
      public int iconMetadata;
      public String name;
      public String generatorInfo;

      public LayerItem(Item var1, int var2, String var3, String var4) {
         this.icon = ☃;
         this.iconMetadata = ☃;
         this.name = ☃;
         this.generatorInfo = ☃;
      }
   }

   class ListSlot extends GuiSlot {
      public int selected = -1;

      public ListSlot() {
         super(GuiFlatPresets.this.mc, GuiFlatPresets.this.width, GuiFlatPresets.this.height, 80, GuiFlatPresets.this.height - 37, 24);
      }

      private void renderIcon(int var1, int var2, Item var3, int var4) {
         this.blitSlotBg(☃ + 1, ☃ + 1);
         GlStateManager.enableRescaleNormal();
         RenderHelper.enableGUIStandardItemLighting();
         GuiFlatPresets.this.itemRender.renderItemIntoGUI(new ItemStack(☃, 1, ☃.getHasSubtypes() ? ☃ : 0), ☃ + 2, ☃ + 2);
         RenderHelper.disableStandardItemLighting();
         GlStateManager.disableRescaleNormal();
      }

      private void blitSlotBg(int var1, int var2) {
         this.blitSlotIcon(☃, ☃, 0, 0);
      }

      private void blitSlotIcon(int var1, int var2, int var3, int var4) {
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.mc.getTextureManager().bindTexture(Gui.STAT_ICONS);
         float ☃ = 0.0078125F;
         float ☃x = 0.0078125F;
         int ☃xx = 18;
         int ☃xxx = 18;
         Tessellator ☃xxxx = Tessellator.getInstance();
         BufferBuilder ☃xxxxx = ☃xxxx.getBuffer();
         ☃xxxxx.begin(7, DefaultVertexFormats.POSITION_TEX);
         ☃xxxxx.pos(☃ + 0, ☃ + 18, GuiFlatPresets.this.zLevel).tex((☃ + 0) * 0.0078125F, (☃ + 18) * 0.0078125F).endVertex();
         ☃xxxxx.pos(☃ + 18, ☃ + 18, GuiFlatPresets.this.zLevel).tex((☃ + 18) * 0.0078125F, (☃ + 18) * 0.0078125F).endVertex();
         ☃xxxxx.pos(☃ + 18, ☃ + 0, GuiFlatPresets.this.zLevel).tex((☃ + 18) * 0.0078125F, (☃ + 0) * 0.0078125F).endVertex();
         ☃xxxxx.pos(☃ + 0, ☃ + 0, GuiFlatPresets.this.zLevel).tex((☃ + 0) * 0.0078125F, (☃ + 0) * 0.0078125F).endVertex();
         ☃xxxx.draw();
      }

      @Override
      protected int getSize() {
         return GuiFlatPresets.FLAT_WORLD_PRESETS.size();
      }

      @Override
      protected void elementClicked(int var1, boolean var2, int var3, int var4) {
         this.selected = ☃;
         GuiFlatPresets.this.updateButtonValidity();
         GuiFlatPresets.this.export.setText(GuiFlatPresets.FLAT_WORLD_PRESETS.get(GuiFlatPresets.this.list.selected).generatorInfo);
      }

      @Override
      protected boolean isSelected(int var1) {
         return ☃ == this.selected;
      }

      @Override
      protected void drawBackground() {
      }

      @Override
      protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6, float var7) {
         GuiFlatPresets.LayerItem ☃ = GuiFlatPresets.FLAT_WORLD_PRESETS.get(☃);
         this.renderIcon(☃, ☃, ☃.icon, ☃.iconMetadata);
         GuiFlatPresets.this.fontRenderer.drawString(☃.name, ☃ + 18 + 5, ☃ + 6, 16777215);
      }
   }
}
