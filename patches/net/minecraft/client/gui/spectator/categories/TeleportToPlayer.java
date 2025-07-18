package net.minecraft.client.gui.spectator.categories;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;
import net.minecraft.client.gui.spectator.PlayerMenuObject;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;

public class TeleportToPlayer implements ISpectatorMenuView, ISpectatorMenuObject {
   private static final Ordering<NetworkPlayerInfo> PROFILE_ORDER = Ordering.from(new Comparator<NetworkPlayerInfo>() {
      public int compare(NetworkPlayerInfo var1, NetworkPlayerInfo var2) {
         return ComparisonChain.start().compare(☃.getGameProfile().getId(), ☃.getGameProfile().getId()).result();
      }
   });
   private final List<ISpectatorMenuObject> items = Lists.newArrayList();

   public TeleportToPlayer() {
      this(PROFILE_ORDER.sortedCopy(Minecraft.getMinecraft().getConnection().getPlayerInfoMap()));
   }

   public TeleportToPlayer(Collection<NetworkPlayerInfo> var1) {
      for (NetworkPlayerInfo ☃ : PROFILE_ORDER.sortedCopy(☃)) {
         if (☃.getGameType() != GameType.SPECTATOR) {
            this.items.add(new PlayerMenuObject(☃.getGameProfile()));
         }
      }
   }

   @Override
   public List<ISpectatorMenuObject> getItems() {
      return this.items;
   }

   @Override
   public ITextComponent getPrompt() {
      return new TextComponentTranslation("spectatorMenu.teleport.prompt");
   }

   @Override
   public void selectItem(SpectatorMenu var1) {
      ☃.selectCategory(this);
   }

   @Override
   public ITextComponent getSpectatorName() {
      return new TextComponentTranslation("spectatorMenu.teleport");
   }

   @Override
   public void renderIcon(float var1, int var2) {
      Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.SPECTATOR_WIDGETS);
      Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0F, 0.0F, 16, 16, 256.0F, 256.0F);
   }

   @Override
   public boolean isEnabled() {
      return !this.items.isEmpty();
   }
}
