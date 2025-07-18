package net.minecraft.client.multiplayer;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.AdvancementToast;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketSeenAdvancements;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientAdvancementManager {
   private static final Logger LOGGER = LogManager.getLogger();
   private final Minecraft mc;
   private final AdvancementList advancementList = new AdvancementList();
   private final Map<Advancement, AdvancementProgress> advancementToProgress = Maps.newHashMap();
   @Nullable
   private ClientAdvancementManager.IListener listener;
   @Nullable
   private Advancement selectedTab;

   public ClientAdvancementManager(Minecraft var1) {
      this.mc = ☃;
   }

   public void read(SPacketAdvancementInfo var1) {
      if (☃.isFirstSync()) {
         this.advancementList.clear();
         this.advancementToProgress.clear();
      }

      this.advancementList.removeAll(☃.getAdvancementsToRemove());
      this.advancementList.loadAdvancements(☃.getAdvancementsToAdd());

      for (Entry<ResourceLocation, AdvancementProgress> ☃ : ☃.getProgressUpdates().entrySet()) {
         Advancement ☃x = this.advancementList.getAdvancement(☃.getKey());
         if (☃x != null) {
            AdvancementProgress ☃xx = ☃.getValue();
            ☃xx.update(☃x.getCriteria(), ☃x.getRequirements());
            this.advancementToProgress.put(☃x, ☃xx);
            if (this.listener != null) {
               this.listener.onUpdateAdvancementProgress(☃x, ☃xx);
            }

            if (!☃.isFirstSync() && ☃xx.isDone() && ☃x.getDisplay() != null && ☃x.getDisplay().shouldShowToast()) {
               this.mc.getToastGui().add(new AdvancementToast(☃x));
            }
         } else {
            LOGGER.warn("Server informed client about progress for unknown advancement " + ☃.getKey());
         }
      }
   }

   public AdvancementList getAdvancementList() {
      return this.advancementList;
   }

   public void setSelectedTab(@Nullable Advancement var1, boolean var2) {
      NetHandlerPlayClient ☃ = this.mc.getConnection();
      if (☃ != null && ☃ != null && ☃) {
         ☃.sendPacket(CPacketSeenAdvancements.openedTab(☃));
      }

      if (this.selectedTab != ☃) {
         this.selectedTab = ☃;
         if (this.listener != null) {
            this.listener.setSelectedTab(☃);
         }
      }
   }

   public void setListener(@Nullable ClientAdvancementManager.IListener var1) {
      this.listener = ☃;
      this.advancementList.setListener(☃);
      if (☃ != null) {
         for (Entry<Advancement, AdvancementProgress> ☃ : this.advancementToProgress.entrySet()) {
            ☃.onUpdateAdvancementProgress(☃.getKey(), ☃.getValue());
         }

         ☃.setSelectedTab(this.selectedTab);
      }
   }

   public interface IListener extends AdvancementList.Listener {
      void onUpdateAdvancementProgress(Advancement var1, AdvancementProgress var2);

      void setSelectedTab(@Nullable Advancement var1);
   }
}
