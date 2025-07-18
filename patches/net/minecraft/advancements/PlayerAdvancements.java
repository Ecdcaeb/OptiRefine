package net.minecraft.advancements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraft.network.play.server.SPacketSelectAdvancementsTab;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerAdvancements {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final Gson GSON = new GsonBuilder()
      .registerTypeAdapter(AdvancementProgress.class, new AdvancementProgress.Serializer())
      .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
      .setPrettyPrinting()
      .create();
   private static final TypeToken<Map<ResourceLocation, AdvancementProgress>> MAP_TOKEN = new TypeToken<Map<ResourceLocation, AdvancementProgress>>() {};
   private final MinecraftServer server;
   private final File progressFile;
   private final Map<Advancement, AdvancementProgress> progress = Maps.newLinkedHashMap();
   private final Set<Advancement> visible = Sets.newLinkedHashSet();
   private final Set<Advancement> visibilityChanged = Sets.newLinkedHashSet();
   private final Set<Advancement> progressChanged = Sets.newLinkedHashSet();
   private EntityPlayerMP player;
   @Nullable
   private Advancement lastSelectedTab;
   private boolean isFirstPacket = true;

   public PlayerAdvancements(MinecraftServer var1, File var2, EntityPlayerMP var3) {
      this.server = ☃;
      this.progressFile = ☃;
      this.player = ☃;
      this.load();
   }

   public void setPlayer(EntityPlayerMP var1) {
      this.player = ☃;
   }

   public void dispose() {
      for (ICriterionTrigger<?> ☃ : CriteriaTriggers.getAll()) {
         ☃.removeAllListeners(this);
      }
   }

   public void reload() {
      this.dispose();
      this.progress.clear();
      this.visible.clear();
      this.visibilityChanged.clear();
      this.progressChanged.clear();
      this.isFirstPacket = true;
      this.lastSelectedTab = null;
      this.load();
   }

   private void registerListeners() {
      for (Advancement ☃ : this.server.getAdvancementManager().getAdvancements()) {
         this.registerListeners(☃);
      }
   }

   private void ensureAllVisible() {
      List<Advancement> ☃ = Lists.newArrayList();

      for (Entry<Advancement, AdvancementProgress> ☃x : this.progress.entrySet()) {
         if (☃x.getValue().isDone()) {
            ☃.add(☃x.getKey());
            this.progressChanged.add(☃x.getKey());
         }
      }

      for (Advancement ☃xx : ☃) {
         this.ensureVisibility(☃xx);
      }
   }

   private void checkForAutomaticTriggers() {
      for (Advancement ☃ : this.server.getAdvancementManager().getAdvancements()) {
         if (☃.getCriteria().isEmpty()) {
            this.grantCriterion(☃, "");
            ☃.getRewards().apply(this.player);
         }
      }
   }

   private void load() {
      if (this.progressFile.isFile()) {
         try {
            String ☃ = Files.toString(this.progressFile, StandardCharsets.UTF_8);
            Map<ResourceLocation, AdvancementProgress> ☃x = JsonUtils.gsonDeserialize(GSON, ☃, MAP_TOKEN.getType());
            if (☃x == null) {
               throw new JsonParseException("Found null for advancements");
            }

            Stream<Entry<ResourceLocation, AdvancementProgress>> ☃xx = ☃x.entrySet().stream().sorted(Comparator.comparing(Entry::getValue));

            for (Entry<ResourceLocation, AdvancementProgress> ☃xxx : ☃xx.collect(Collectors.toList())) {
               Advancement ☃xxxx = this.server.getAdvancementManager().getAdvancement(☃xxx.getKey());
               if (☃xxxx == null) {
                  LOGGER.warn("Ignored advancement '" + ☃xxx.getKey() + "' in progress file " + this.progressFile + " - it doesn't exist anymore?");
               } else {
                  this.startProgress(☃xxxx, ☃xxx.getValue());
               }
            }
         } catch (JsonParseException var7) {
            LOGGER.error("Couldn't parse player advancements in " + this.progressFile, var7);
         } catch (IOException var8) {
            LOGGER.error("Couldn't access player advancements in " + this.progressFile, var8);
         }
      }

      this.checkForAutomaticTriggers();
      this.ensureAllVisible();
      this.registerListeners();
   }

   public void save() {
      Map<ResourceLocation, AdvancementProgress> ☃ = Maps.newHashMap();

      for (Entry<Advancement, AdvancementProgress> ☃x : this.progress.entrySet()) {
         AdvancementProgress ☃xx = ☃x.getValue();
         if (☃xx.hasProgress()) {
            ☃.put(☃x.getKey().getId(), ☃xx);
         }
      }

      if (this.progressFile.getParentFile() != null) {
         this.progressFile.getParentFile().mkdirs();
      }

      try {
         Files.write(GSON.toJson(☃), this.progressFile, StandardCharsets.UTF_8);
      } catch (IOException var5) {
         LOGGER.error("Couldn't save player advancements to " + this.progressFile, var5);
      }
   }

   public boolean grantCriterion(Advancement var1, String var2) {
      boolean ☃ = false;
      AdvancementProgress ☃x = this.getProgress(☃);
      boolean ☃xx = ☃x.isDone();
      if (☃x.grantCriterion(☃)) {
         this.unregisterListeners(☃);
         this.progressChanged.add(☃);
         ☃ = true;
         if (!☃xx && ☃x.isDone()) {
            ☃.getRewards().apply(this.player);
            if (☃.getDisplay() != null && ☃.getDisplay().shouldAnnounceToChat() && this.player.world.getGameRules().getBoolean("announceAdvancements")) {
               this.server
                  .getPlayerList()
                  .sendMessage(
                     new TextComponentTranslation(
                        "chat.type.advancement." + ☃.getDisplay().getFrame().getName(), this.player.getDisplayName(), ☃.getDisplayText()
                     )
                  );
            }
         }
      }

      if (☃x.isDone()) {
         this.ensureVisibility(☃);
      }

      return ☃;
   }

   public boolean revokeCriterion(Advancement var1, String var2) {
      boolean ☃ = false;
      AdvancementProgress ☃x = this.getProgress(☃);
      if (☃x.revokeCriterion(☃)) {
         this.registerListeners(☃);
         this.progressChanged.add(☃);
         ☃ = true;
      }

      if (!☃x.hasProgress()) {
         this.ensureVisibility(☃);
      }

      return ☃;
   }

   private void registerListeners(Advancement var1) {
      AdvancementProgress ☃ = this.getProgress(☃);
      if (!☃.isDone()) {
         for (Entry<String, Criterion> ☃x : ☃.getCriteria().entrySet()) {
            CriterionProgress ☃xx = ☃.getCriterionProgress(☃x.getKey());
            if (☃xx != null && !☃xx.isObtained()) {
               ICriterionInstance ☃xxx = ☃x.getValue().getCriterionInstance();
               if (☃xxx != null) {
                  ICriterionTrigger<ICriterionInstance> ☃xxxx = CriteriaTriggers.get(☃xxx.getId());
                  if (☃xxxx != null) {
                     ☃xxxx.addListener(this, new ICriterionTrigger.Listener<>(☃xxx, ☃, ☃x.getKey()));
                  }
               }
            }
         }
      }
   }

   private void unregisterListeners(Advancement var1) {
      AdvancementProgress ☃ = this.getProgress(☃);

      for (Entry<String, Criterion> ☃x : ☃.getCriteria().entrySet()) {
         CriterionProgress ☃xx = ☃.getCriterionProgress(☃x.getKey());
         if (☃xx != null && (☃xx.isObtained() || ☃.isDone())) {
            ICriterionInstance ☃xxx = ☃x.getValue().getCriterionInstance();
            if (☃xxx != null) {
               ICriterionTrigger<ICriterionInstance> ☃xxxx = CriteriaTriggers.get(☃xxx.getId());
               if (☃xxxx != null) {
                  ☃xxxx.removeListener(this, new ICriterionTrigger.Listener<>(☃xxx, ☃, ☃x.getKey()));
               }
            }
         }
      }
   }

   public void flushDirty(EntityPlayerMP var1) {
      if (!this.visibilityChanged.isEmpty() || !this.progressChanged.isEmpty()) {
         Map<ResourceLocation, AdvancementProgress> ☃ = Maps.newHashMap();
         Set<Advancement> ☃x = Sets.newLinkedHashSet();
         Set<ResourceLocation> ☃xx = Sets.newLinkedHashSet();

         for (Advancement ☃xxx : this.progressChanged) {
            if (this.visible.contains(☃xxx)) {
               ☃.put(☃xxx.getId(), this.progress.get(☃xxx));
            }
         }

         for (Advancement ☃xxxx : this.visibilityChanged) {
            if (this.visible.contains(☃xxxx)) {
               ☃x.add(☃xxxx);
            } else {
               ☃xx.add(☃xxxx.getId());
            }
         }

         if (!☃.isEmpty() || !☃x.isEmpty() || !☃xx.isEmpty()) {
            ☃.connection.sendPacket(new SPacketAdvancementInfo(this.isFirstPacket, ☃x, ☃xx, ☃));
            this.visibilityChanged.clear();
            this.progressChanged.clear();
         }
      }

      this.isFirstPacket = false;
   }

   public void setSelectedTab(@Nullable Advancement var1) {
      Advancement ☃ = this.lastSelectedTab;
      if (☃ != null && ☃.getParent() == null && ☃.getDisplay() != null) {
         this.lastSelectedTab = ☃;
      } else {
         this.lastSelectedTab = null;
      }

      if (☃ != this.lastSelectedTab) {
         this.player.connection.sendPacket(new SPacketSelectAdvancementsTab(this.lastSelectedTab == null ? null : this.lastSelectedTab.getId()));
      }
   }

   public AdvancementProgress getProgress(Advancement var1) {
      AdvancementProgress ☃ = this.progress.get(☃);
      if (☃ == null) {
         ☃ = new AdvancementProgress();
         this.startProgress(☃, ☃);
      }

      return ☃;
   }

   private void startProgress(Advancement var1, AdvancementProgress var2) {
      ☃.update(☃.getCriteria(), ☃.getRequirements());
      this.progress.put(☃, ☃);
   }

   private void ensureVisibility(Advancement var1) {
      boolean ☃ = this.shouldBeVisible(☃);
      boolean ☃x = this.visible.contains(☃);
      if (☃ && !☃x) {
         this.visible.add(☃);
         this.visibilityChanged.add(☃);
         if (this.progress.containsKey(☃)) {
            this.progressChanged.add(☃);
         }
      } else if (!☃ && ☃x) {
         this.visible.remove(☃);
         this.visibilityChanged.add(☃);
      }

      if (☃ != ☃x && ☃.getParent() != null) {
         this.ensureVisibility(☃.getParent());
      }

      for (Advancement ☃xx : ☃.getChildren()) {
         this.ensureVisibility(☃xx);
      }
   }

   private boolean shouldBeVisible(Advancement var1) {
      for (int ☃ = 0; ☃ != null && ☃ <= 2; ☃++) {
         if (☃ == 0 && this.hasCompletedChildrenOrSelf(☃)) {
            return true;
         }

         if (☃.getDisplay() == null) {
            return false;
         }

         AdvancementProgress ☃x = this.getProgress(☃);
         if (☃x.isDone()) {
            return true;
         }

         if (☃.getDisplay().isHidden()) {
            return false;
         }

         ☃ = ☃.getParent();
      }

      return false;
   }

   private boolean hasCompletedChildrenOrSelf(Advancement var1) {
      AdvancementProgress ☃ = this.getProgress(☃);
      if (☃.isDone()) {
         return true;
      } else {
         for (Advancement ☃x : ☃.getChildren()) {
            if (this.hasCompletedChildrenOrSelf(☃x)) {
               return true;
            }
         }

         return false;
      }
   }
}
