package net.minecraft.client.gui.spectator.categories;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class TeleportToTeam implements ISpectatorMenuView, ISpectatorMenuObject {
   private final List<ISpectatorMenuObject> items = Lists.newArrayList();

   public TeleportToTeam() {
      Minecraft ☃ = Minecraft.getMinecraft();

      for (ScorePlayerTeam ☃x : ☃.world.getScoreboard().getTeams()) {
         this.items.add(new TeleportToTeam.TeamSelectionObject(☃x));
      }
   }

   @Override
   public List<ISpectatorMenuObject> getItems() {
      return this.items;
   }

   @Override
   public ITextComponent getPrompt() {
      return new TextComponentTranslation("spectatorMenu.team_teleport.prompt");
   }

   @Override
   public void selectItem(SpectatorMenu var1) {
      ☃.selectCategory(this);
   }

   @Override
   public ITextComponent getSpectatorName() {
      return new TextComponentTranslation("spectatorMenu.team_teleport");
   }

   @Override
   public void renderIcon(float var1, int var2) {
      Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.SPECTATOR_WIDGETS);
      Gui.drawModalRectWithCustomSizedTexture(0, 0, 16.0F, 0.0F, 16, 16, 256.0F, 256.0F);
   }

   @Override
   public boolean isEnabled() {
      for (ISpectatorMenuObject ☃ : this.items) {
         if (☃.isEnabled()) {
            return true;
         }
      }

      return false;
   }

   class TeamSelectionObject implements ISpectatorMenuObject {
      private final ScorePlayerTeam team;
      private final ResourceLocation location;
      private final List<NetworkPlayerInfo> players;

      public TeamSelectionObject(ScorePlayerTeam var2) {
         this.team = ☃;
         this.players = Lists.newArrayList();

         for (String ☃ : ☃.getMembershipCollection()) {
            NetworkPlayerInfo ☃x = Minecraft.getMinecraft().getConnection().getPlayerInfo(☃);
            if (☃x != null) {
               this.players.add(☃x);
            }
         }

         if (this.players.isEmpty()) {
            this.location = DefaultPlayerSkin.getDefaultSkinLegacy();
         } else {
            String ☃x = this.players.get(new Random().nextInt(this.players.size())).getGameProfile().getName();
            this.location = AbstractClientPlayer.getLocationSkin(☃x);
            AbstractClientPlayer.getDownloadImageSkin(this.location, ☃x);
         }
      }

      @Override
      public void selectItem(SpectatorMenu var1) {
         ☃.selectCategory(new TeleportToPlayer(this.players));
      }

      @Override
      public ITextComponent getSpectatorName() {
         return new TextComponentString(this.team.getDisplayName());
      }

      @Override
      public void renderIcon(float var1, int var2) {
         int ☃ = -1;
         String ☃x = FontRenderer.getFormatFromString(this.team.getPrefix());
         if (☃x.length() >= 2) {
            ☃ = Minecraft.getMinecraft().fontRenderer.getColorCode(☃x.charAt(1));
         }

         if (☃ >= 0) {
            float ☃xx = (☃ >> 16 & 0xFF) / 255.0F;
            float ☃xxx = (☃ >> 8 & 0xFF) / 255.0F;
            float ☃xxxx = (☃ & 0xFF) / 255.0F;
            Gui.drawRect(1, 1, 15, 15, MathHelper.rgb(☃xx * ☃, ☃xxx * ☃, ☃xxxx * ☃) | ☃ << 24);
         }

         Minecraft.getMinecraft().getTextureManager().bindTexture(this.location);
         GlStateManager.color(☃, ☃, ☃, ☃ / 255.0F);
         Gui.drawScaledCustomSizeModalRect(2, 2, 8.0F, 8.0F, 8, 8, 12, 12, 64.0F, 64.0F);
         Gui.drawScaledCustomSizeModalRect(2, 2, 40.0F, 8.0F, 8, 8, 12, 12, 64.0F, 64.0F);
      }

      @Override
      public boolean isEnabled() {
         return !this.players.isEmpty();
      }
   }
}
