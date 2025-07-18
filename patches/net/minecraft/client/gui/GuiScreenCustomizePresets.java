package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import org.lwjgl.input.Keyboard;

public class GuiScreenCustomizePresets extends GuiScreen {
   private static final List<GuiScreenCustomizePresets.Info> PRESETS = Lists.newArrayList();
   private GuiScreenCustomizePresets.ListPreset list;
   private GuiButton select;
   private GuiTextField export;
   private final GuiCustomizeWorldScreen parent;
   protected String title = "Customize World Presets";
   private String shareText;
   private String listText;

   public GuiScreenCustomizePresets(GuiCustomizeWorldScreen var1) {
      this.parent = ☃;
   }

   @Override
   public void initGui() {
      this.buttonList.clear();
      Keyboard.enableRepeatEvents(true);
      this.title = I18n.format("createWorld.customize.custom.presets.title");
      this.shareText = I18n.format("createWorld.customize.presets.share");
      this.listText = I18n.format("createWorld.customize.presets.list");
      this.export = new GuiTextField(2, this.fontRenderer, 50, 40, this.width - 100, 20);
      this.list = new GuiScreenCustomizePresets.ListPreset();
      this.export.setMaxStringLength(2000);
      this.export.setText(this.parent.saveValues());
      this.select = this.addButton(new GuiButton(0, this.width / 2 - 102, this.height - 27, 100, 20, I18n.format("createWorld.customize.presets.select")));
      this.buttonList.add(new GuiButton(1, this.width / 2 + 3, this.height - 27, 100, 20, I18n.format("gui.cancel")));
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
      switch (☃.id) {
         case 0:
            this.parent.loadValues(this.export.getText());
            this.mc.displayGuiScreen(this.parent);
            break;
         case 1:
            this.mc.displayGuiScreen(this.parent);
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.list.drawScreen(☃, ☃, ☃);
      this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 8, 16777215);
      this.drawString(this.fontRenderer, this.shareText, 50, 30, 10526880);
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
      this.select.enabled = this.hasValidSelection();
   }

   private boolean hasValidSelection() {
      return this.list.selected > -1 && this.list.selected < PRESETS.size() || this.export.getText().length() > 1;
   }

   static {
      ChunkGeneratorSettings.Factory ☃ = ChunkGeneratorSettings.Factory.jsonToFactory(
         "{ \"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":512.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":5000.0, \"mainNoiseScaleY\":1000.0, \"mainNoiseScaleZ\":5000.0, \"baseSize\":8.5, \"stretchY\":8.0, \"biomeDepthWeight\":2.0, \"biomeDepthOffset\":0.5, \"biomeScaleWeight\":2.0, \"biomeScaleOffset\":0.375, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":255 }"
      );
      ResourceLocation ☃x = new ResourceLocation("textures/gui/presets/water.png");
      PRESETS.add(new GuiScreenCustomizePresets.Info(I18n.format("createWorld.customize.custom.preset.waterWorld"), ☃x, ☃));
      ☃ = ChunkGeneratorSettings.Factory.jsonToFactory(
         "{\"coordinateScale\":3000.0, \"heightScale\":6000.0, \"upperLimitScale\":250.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":80.0, \"mainNoiseScaleY\":160.0, \"mainNoiseScaleZ\":80.0, \"baseSize\":8.5, \"stretchY\":10.0, \"biomeDepthWeight\":1.0, \"biomeDepthOffset\":0.0, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":0.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":63 }"
      );
      ☃x = new ResourceLocation("textures/gui/presets/isles.png");
      PRESETS.add(new GuiScreenCustomizePresets.Info(I18n.format("createWorld.customize.custom.preset.isleLand"), ☃x, ☃));
      ☃ = ChunkGeneratorSettings.Factory.jsonToFactory(
         "{\"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":512.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":5000.0, \"mainNoiseScaleY\":1000.0, \"mainNoiseScaleZ\":5000.0, \"baseSize\":8.5, \"stretchY\":5.0, \"biomeDepthWeight\":2.0, \"biomeDepthOffset\":1.0, \"biomeScaleWeight\":4.0, \"biomeScaleOffset\":1.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":63 }"
      );
      ☃x = new ResourceLocation("textures/gui/presets/delight.png");
      PRESETS.add(new GuiScreenCustomizePresets.Info(I18n.format("createWorld.customize.custom.preset.caveDelight"), ☃x, ☃));
      ☃ = ChunkGeneratorSettings.Factory.jsonToFactory(
         "{\"coordinateScale\":738.41864, \"heightScale\":157.69133, \"upperLimitScale\":801.4267, \"lowerLimitScale\":1254.1643, \"depthNoiseScaleX\":374.93652, \"depthNoiseScaleZ\":288.65228, \"depthNoiseScaleExponent\":1.2092624, \"mainNoiseScaleX\":1355.9908, \"mainNoiseScaleY\":745.5343, \"mainNoiseScaleZ\":1183.464, \"baseSize\":1.8758626, \"stretchY\":1.7137525, \"biomeDepthWeight\":1.7553768, \"biomeDepthOffset\":3.4701107, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":2.535211, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":63 }"
      );
      ☃x = new ResourceLocation("textures/gui/presets/madness.png");
      PRESETS.add(new GuiScreenCustomizePresets.Info(I18n.format("createWorld.customize.custom.preset.mountains"), ☃x, ☃));
      ☃ = ChunkGeneratorSettings.Factory.jsonToFactory(
         "{\"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":512.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":1000.0, \"mainNoiseScaleY\":3000.0, \"mainNoiseScaleZ\":1000.0, \"baseSize\":8.5, \"stretchY\":10.0, \"biomeDepthWeight\":1.0, \"biomeDepthOffset\":0.0, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":0.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":20 }"
      );
      ☃x = new ResourceLocation("textures/gui/presets/drought.png");
      PRESETS.add(new GuiScreenCustomizePresets.Info(I18n.format("createWorld.customize.custom.preset.drought"), ☃x, ☃));
      ☃ = ChunkGeneratorSettings.Factory.jsonToFactory(
         "{\"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":2.0, \"lowerLimitScale\":64.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":80.0, \"mainNoiseScaleY\":160.0, \"mainNoiseScaleZ\":80.0, \"baseSize\":8.5, \"stretchY\":12.0, \"biomeDepthWeight\":1.0, \"biomeDepthOffset\":0.0, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":0.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":false, \"seaLevel\":6 }"
      );
      ☃x = new ResourceLocation("textures/gui/presets/chaos.png");
      PRESETS.add(new GuiScreenCustomizePresets.Info(I18n.format("createWorld.customize.custom.preset.caveChaos"), ☃x, ☃));
      ☃ = ChunkGeneratorSettings.Factory.jsonToFactory(
         "{\"coordinateScale\":684.412, \"heightScale\":684.412, \"upperLimitScale\":512.0, \"lowerLimitScale\":512.0, \"depthNoiseScaleX\":200.0, \"depthNoiseScaleZ\":200.0, \"depthNoiseScaleExponent\":0.5, \"mainNoiseScaleX\":80.0, \"mainNoiseScaleY\":160.0, \"mainNoiseScaleZ\":80.0, \"baseSize\":8.5, \"stretchY\":12.0, \"biomeDepthWeight\":1.0, \"biomeDepthOffset\":0.0, \"biomeScaleWeight\":1.0, \"biomeScaleOffset\":0.0, \"useCaves\":true, \"useDungeons\":true, \"dungeonChance\":8, \"useStrongholds\":true, \"useVillages\":true, \"useMineShafts\":true, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":true, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":true, \"seaLevel\":40 }"
      );
      ☃x = new ResourceLocation("textures/gui/presets/luck.png");
      PRESETS.add(new GuiScreenCustomizePresets.Info(I18n.format("createWorld.customize.custom.preset.goodLuck"), ☃x, ☃));
   }

   static class Info {
      public String name;
      public ResourceLocation texture;
      public ChunkGeneratorSettings.Factory settings;

      public Info(String var1, ResourceLocation var2, ChunkGeneratorSettings.Factory var3) {
         this.name = ☃;
         this.texture = ☃;
         this.settings = ☃;
      }
   }

   class ListPreset extends GuiSlot {
      public int selected = -1;

      public ListPreset() {
         super(
            GuiScreenCustomizePresets.this.mc,
            GuiScreenCustomizePresets.this.width,
            GuiScreenCustomizePresets.this.height,
            80,
            GuiScreenCustomizePresets.this.height - 32,
            38
         );
      }

      @Override
      protected int getSize() {
         return GuiScreenCustomizePresets.PRESETS.size();
      }

      @Override
      protected void elementClicked(int var1, boolean var2, int var3, int var4) {
         this.selected = ☃;
         GuiScreenCustomizePresets.this.updateButtonValidity();
         GuiScreenCustomizePresets.this.export.setText(GuiScreenCustomizePresets.PRESETS.get(GuiScreenCustomizePresets.this.list.selected).settings.toString());
      }

      @Override
      protected boolean isSelected(int var1) {
         return ☃ == this.selected;
      }

      @Override
      protected void drawBackground() {
      }

      private void blitIcon(int var1, int var2, ResourceLocation var3) {
         int ☃ = ☃ + 5;
         GuiScreenCustomizePresets.this.drawHorizontalLine(☃ - 1, ☃ + 32, ☃ - 1, -2039584);
         GuiScreenCustomizePresets.this.drawHorizontalLine(☃ - 1, ☃ + 32, ☃ + 32, -6250336);
         GuiScreenCustomizePresets.this.drawVerticalLine(☃ - 1, ☃ - 1, ☃ + 32, -2039584);
         GuiScreenCustomizePresets.this.drawVerticalLine(☃ + 32, ☃ - 1, ☃ + 32, -6250336);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.mc.getTextureManager().bindTexture(☃);
         int ☃x = 32;
         int ☃xx = 32;
         Tessellator ☃xxx = Tessellator.getInstance();
         BufferBuilder ☃xxxx = ☃xxx.getBuffer();
         ☃xxxx.begin(7, DefaultVertexFormats.POSITION_TEX);
         ☃xxxx.pos(☃ + 0, ☃ + 32, 0.0).tex(0.0, 1.0).endVertex();
         ☃xxxx.pos(☃ + 32, ☃ + 32, 0.0).tex(1.0, 1.0).endVertex();
         ☃xxxx.pos(☃ + 32, ☃ + 0, 0.0).tex(1.0, 0.0).endVertex();
         ☃xxxx.pos(☃ + 0, ☃ + 0, 0.0).tex(0.0, 0.0).endVertex();
         ☃xxx.draw();
      }

      @Override
      protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6, float var7) {
         GuiScreenCustomizePresets.Info ☃ = GuiScreenCustomizePresets.PRESETS.get(☃);
         this.blitIcon(☃, ☃, ☃.texture);
         GuiScreenCustomizePresets.this.fontRenderer.drawString(☃.name, ☃ + 32 + 10, ☃ + 14, 16777215);
      }
   }
}
