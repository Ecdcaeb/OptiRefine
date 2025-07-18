package net.minecraft.client.gui;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.BossInfo;

public class GuiBossOverlay extends Gui {
   private static final ResourceLocation GUI_BARS_TEXTURES = new ResourceLocation("textures/gui/bars.png");
   private final Minecraft client;
   private final Map<UUID, BossInfoClient> mapBossInfos = Maps.newLinkedHashMap();

   public GuiBossOverlay(Minecraft var1) {
      this.client = ☃;
   }

   public void renderBossHealth() {
      if (!this.mapBossInfos.isEmpty()) {
         ScaledResolution ☃ = new ScaledResolution(this.client);
         int ☃x = ☃.getScaledWidth();
         int ☃xx = 12;

         for (BossInfoClient ☃xxx : this.mapBossInfos.values()) {
            int ☃xxxx = ☃x / 2 - 91;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.client.getTextureManager().bindTexture(GUI_BARS_TEXTURES);
            this.render(☃xxxx, ☃xx, ☃xxx);
            String ☃xxxxx = ☃xxx.getName().getFormattedText();
            this.client.fontRenderer.drawStringWithShadow(☃xxxxx, ☃x / 2 - this.client.fontRenderer.getStringWidth(☃xxxxx) / 2, ☃xx - 9, 16777215);
            ☃xx += 10 + this.client.fontRenderer.FONT_HEIGHT;
            if (☃xx >= ☃.getScaledHeight() / 3) {
               break;
            }
         }
      }
   }

   private void render(int var1, int var2, BossInfo var3) {
      this.drawTexturedModalRect(☃, ☃, 0, ☃.getColor().ordinal() * 5 * 2, 182, 5);
      if (☃.getOverlay() != BossInfo.Overlay.PROGRESS) {
         this.drawTexturedModalRect(☃, ☃, 0, 80 + (☃.getOverlay().ordinal() - 1) * 5 * 2, 182, 5);
      }

      int ☃ = (int)(☃.getPercent() * 183.0F);
      if (☃ > 0) {
         this.drawTexturedModalRect(☃, ☃, 0, ☃.getColor().ordinal() * 5 * 2 + 5, ☃, 5);
         if (☃.getOverlay() != BossInfo.Overlay.PROGRESS) {
            this.drawTexturedModalRect(☃, ☃, 0, 80 + (☃.getOverlay().ordinal() - 1) * 5 * 2 + 5, ☃, 5);
         }
      }
   }

   public void read(SPacketUpdateBossInfo var1) {
      if (☃.getOperation() == SPacketUpdateBossInfo.Operation.ADD) {
         this.mapBossInfos.put(☃.getUniqueId(), new BossInfoClient(☃));
      } else if (☃.getOperation() == SPacketUpdateBossInfo.Operation.REMOVE) {
         this.mapBossInfos.remove(☃.getUniqueId());
      } else {
         this.mapBossInfos.get(☃.getUniqueId()).updateFromPacket(☃);
      }
   }

   public void clearBossInfos() {
      this.mapBossInfos.clear();
   }

   public boolean shouldPlayEndBossMusic() {
      if (!this.mapBossInfos.isEmpty()) {
         for (BossInfo ☃ : this.mapBossInfos.values()) {
            if (☃.shouldPlayEndBossMusic()) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean shouldDarkenSky() {
      if (!this.mapBossInfos.isEmpty()) {
         for (BossInfo ☃ : this.mapBossInfos.values()) {
            if (☃.shouldDarkenSky()) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean shouldCreateFog() {
      if (!this.mapBossInfos.isEmpty()) {
         for (BossInfo ☃ : this.mapBossInfos.values()) {
            if (☃.shouldCreateFog()) {
               return true;
            }
         }
      }

      return false;
   }
}
