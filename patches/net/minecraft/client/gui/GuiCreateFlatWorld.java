package net.minecraft.client.gui;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;

public class GuiCreateFlatWorld extends GuiScreen {
   private final GuiCreateWorld createWorldGui;
   private FlatGeneratorInfo generatorInfo = FlatGeneratorInfo.getDefaultFlatGenerator();
   private String flatWorldTitle;
   private String materialText;
   private String heightText;
   private GuiCreateFlatWorld.Details createFlatWorldListSlotGui;
   private GuiButton addLayerButton;
   private GuiButton editLayerButton;
   private GuiButton removeLayerButton;

   public GuiCreateFlatWorld(GuiCreateWorld var1, String var2) {
      this.createWorldGui = ☃;
      this.setPreset(☃);
   }

   public String getPreset() {
      return this.generatorInfo.toString();
   }

   public void setPreset(String var1) {
      this.generatorInfo = FlatGeneratorInfo.createFlatGeneratorFromString(☃);
   }

   @Override
   public void initGui() {
      this.buttonList.clear();
      this.flatWorldTitle = I18n.format("createWorld.customize.flat.title");
      this.materialText = I18n.format("createWorld.customize.flat.tile");
      this.heightText = I18n.format("createWorld.customize.flat.height");
      this.createFlatWorldListSlotGui = new GuiCreateFlatWorld.Details();
      this.addLayerButton = this.addButton(
         new GuiButton(2, this.width / 2 - 154, this.height - 52, 100, 20, I18n.format("createWorld.customize.flat.addLayer") + " (NYI)")
      );
      this.editLayerButton = this.addButton(
         new GuiButton(3, this.width / 2 - 50, this.height - 52, 100, 20, I18n.format("createWorld.customize.flat.editLayer") + " (NYI)")
      );
      this.removeLayerButton = this.addButton(
         new GuiButton(4, this.width / 2 - 155, this.height - 52, 150, 20, I18n.format("createWorld.customize.flat.removeLayer"))
      );
      this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, I18n.format("gui.done")));
      this.buttonList.add(new GuiButton(5, this.width / 2 + 5, this.height - 52, 150, 20, I18n.format("createWorld.customize.presets")));
      this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, I18n.format("gui.cancel")));
      this.addLayerButton.visible = false;
      this.editLayerButton.visible = false;
      this.generatorInfo.updateLayers();
      this.onLayersChanged();
   }

   @Override
   public void handleMouseInput() {
      super.handleMouseInput();
      this.createFlatWorldListSlotGui.handleMouseInput();
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      int ☃ = this.generatorInfo.getFlatLayers().size() - this.createFlatWorldListSlotGui.selectedLayer - 1;
      if (☃.id == 1) {
         this.mc.displayGuiScreen(this.createWorldGui);
      } else if (☃.id == 0) {
         this.createWorldGui.chunkProviderSettingsJson = this.getPreset();
         this.mc.displayGuiScreen(this.createWorldGui);
      } else if (☃.id == 5) {
         this.mc.displayGuiScreen(new GuiFlatPresets(this));
      } else if (☃.id == 4 && this.hasSelectedLayer()) {
         this.generatorInfo.getFlatLayers().remove(☃);
         this.createFlatWorldListSlotGui.selectedLayer = Math.min(this.createFlatWorldListSlotGui.selectedLayer, this.generatorInfo.getFlatLayers().size() - 1);
      }

      this.generatorInfo.updateLayers();
      this.onLayersChanged();
   }

   public void onLayersChanged() {
      boolean ☃ = this.hasSelectedLayer();
      this.removeLayerButton.enabled = ☃;
      this.editLayerButton.enabled = ☃;
      this.editLayerButton.enabled = false;
      this.addLayerButton.enabled = false;
   }

   private boolean hasSelectedLayer() {
      return this.createFlatWorldListSlotGui.selectedLayer > -1 && this.createFlatWorldListSlotGui.selectedLayer < this.generatorInfo.getFlatLayers().size();
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.createFlatWorldListSlotGui.drawScreen(☃, ☃, ☃);
      this.drawCenteredString(this.fontRenderer, this.flatWorldTitle, this.width / 2, 8, 16777215);
      int ☃ = this.width / 2 - 92 - 16;
      this.drawString(this.fontRenderer, this.materialText, ☃, 32, 16777215);
      this.drawString(this.fontRenderer, this.heightText, ☃ + 2 + 213 - this.fontRenderer.getStringWidth(this.heightText), 32, 16777215);
      super.drawScreen(☃, ☃, ☃);
   }

   class Details extends GuiSlot {
      public int selectedLayer = -1;

      public Details() {
         super(GuiCreateFlatWorld.this.mc, GuiCreateFlatWorld.this.width, GuiCreateFlatWorld.this.height, 43, GuiCreateFlatWorld.this.height - 60, 24);
      }

      private void drawItem(int var1, int var2, ItemStack var3) {
         this.drawItemBackground(☃ + 1, ☃ + 1);
         GlStateManager.enableRescaleNormal();
         if (!☃.isEmpty()) {
            RenderHelper.enableGUIStandardItemLighting();
            GuiCreateFlatWorld.this.itemRender.renderItemIntoGUI(☃, ☃ + 2, ☃ + 2);
            RenderHelper.disableStandardItemLighting();
         }

         GlStateManager.disableRescaleNormal();
      }

      private void drawItemBackground(int var1, int var2) {
         this.drawItemBackground(☃, ☃, 0, 0);
      }

      private void drawItemBackground(int var1, int var2, int var3, int var4) {
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.mc.getTextureManager().bindTexture(Gui.STAT_ICONS);
         float ☃ = 0.0078125F;
         float ☃x = 0.0078125F;
         int ☃xx = 18;
         int ☃xxx = 18;
         Tessellator ☃xxxx = Tessellator.getInstance();
         BufferBuilder ☃xxxxx = ☃xxxx.getBuffer();
         ☃xxxxx.begin(7, DefaultVertexFormats.POSITION_TEX);
         ☃xxxxx.pos(☃ + 0, ☃ + 18, GuiCreateFlatWorld.this.zLevel).tex((☃ + 0) * 0.0078125F, (☃ + 18) * 0.0078125F).endVertex();
         ☃xxxxx.pos(☃ + 18, ☃ + 18, GuiCreateFlatWorld.this.zLevel).tex((☃ + 18) * 0.0078125F, (☃ + 18) * 0.0078125F).endVertex();
         ☃xxxxx.pos(☃ + 18, ☃ + 0, GuiCreateFlatWorld.this.zLevel).tex((☃ + 18) * 0.0078125F, (☃ + 0) * 0.0078125F).endVertex();
         ☃xxxxx.pos(☃ + 0, ☃ + 0, GuiCreateFlatWorld.this.zLevel).tex((☃ + 0) * 0.0078125F, (☃ + 0) * 0.0078125F).endVertex();
         ☃xxxx.draw();
      }

      @Override
      protected int getSize() {
         return GuiCreateFlatWorld.this.generatorInfo.getFlatLayers().size();
      }

      @Override
      protected void elementClicked(int var1, boolean var2, int var3, int var4) {
         this.selectedLayer = ☃;
         GuiCreateFlatWorld.this.onLayersChanged();
      }

      @Override
      protected boolean isSelected(int var1) {
         return ☃ == this.selectedLayer;
      }

      @Override
      protected void drawBackground() {
      }

      @Override
      protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6, float var7) {
         FlatLayerInfo ☃ = GuiCreateFlatWorld.this.generatorInfo.getFlatLayers().get(GuiCreateFlatWorld.this.generatorInfo.getFlatLayers().size() - ☃ - 1);
         IBlockState ☃x = ☃.getLayerMaterial();
         Block ☃xx = ☃x.getBlock();
         Item ☃xxx = Item.getItemFromBlock(☃xx);
         if (☃xxx == Items.AIR) {
            if (☃xx == Blocks.WATER || ☃xx == Blocks.FLOWING_WATER) {
               ☃xxx = Items.WATER_BUCKET;
            } else if (☃xx == Blocks.LAVA || ☃xx == Blocks.FLOWING_LAVA) {
               ☃xxx = Items.LAVA_BUCKET;
            }
         }

         ItemStack ☃xxxx = new ItemStack(☃xxx, 1, ☃xxx.getHasSubtypes() ? ☃xx.getMetaFromState(☃x) : 0);
         String ☃xxxxx = ☃xxx.getItemStackDisplayName(☃xxxx);
         this.drawItem(☃, ☃, ☃xxxx);
         GuiCreateFlatWorld.this.fontRenderer.drawString(☃xxxxx, ☃ + 18 + 5, ☃ + 3, 16777215);
         String ☃xxxxxx;
         if (☃ == 0) {
            ☃xxxxxx = I18n.format("createWorld.customize.flat.layer.top", ☃.getLayerCount());
         } else if (☃ == GuiCreateFlatWorld.this.generatorInfo.getFlatLayers().size() - 1) {
            ☃xxxxxx = I18n.format("createWorld.customize.flat.layer.bottom", ☃.getLayerCount());
         } else {
            ☃xxxxxx = I18n.format("createWorld.customize.flat.layer", ☃.getLayerCount());
         }

         GuiCreateFlatWorld.this.fontRenderer.drawString(☃xxxxxx, ☃ + 2 + 213 - GuiCreateFlatWorld.this.fontRenderer.getStringWidth(☃xxxxxx), ☃ + 3, 16777215);
      }

      @Override
      protected int getScrollBarX() {
         return this.width - 70;
      }
   }
}
