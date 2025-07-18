package net.minecraft.client.gui;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.chat.IChatListener;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.chat.NormalChatListener;
import net.minecraft.client.gui.chat.OverlayChatListener;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.border.WorldBorder;

public class GuiIngame extends Gui {
   private static final ResourceLocation VIGNETTE_TEX_PATH = new ResourceLocation("textures/misc/vignette.png");
   private static final ResourceLocation WIDGETS_TEX_PATH = new ResourceLocation("textures/gui/widgets.png");
   private static final ResourceLocation PUMPKIN_BLUR_TEX_PATH = new ResourceLocation("textures/misc/pumpkinblur.png");
   private final Random rand = new Random();
   private final Minecraft mc;
   private final RenderItem itemRenderer;
   private final GuiNewChat persistantChatGUI;
   private int updateCounter;
   private String overlayMessage = "";
   private int overlayMessageTime;
   private boolean animateOverlayMessageColor;
   public float prevVignetteBrightness = 1.0F;
   private int remainingHighlightTicks;
   private ItemStack highlightingItemStack = ItemStack.EMPTY;
   private final GuiOverlayDebug overlayDebug;
   private final GuiSubtitleOverlay overlaySubtitle;
   private final GuiSpectator spectatorGui;
   private final GuiPlayerTabOverlay overlayPlayerList;
   private final GuiBossOverlay overlayBoss;
   private int titlesTimer;
   private String displayedTitle = "";
   private String displayedSubTitle = "";
   private int titleFadeIn;
   private int titleDisplayTime;
   private int titleFadeOut;
   private int playerHealth;
   private int lastPlayerHealth;
   private long lastSystemTime;
   private long healthUpdateCounter;
   private final Map<ChatType, List<IChatListener>> chatListeners = Maps.newHashMap();

   public GuiIngame(Minecraft var1) {
      this.mc = ☃;
      this.itemRenderer = ☃.getRenderItem();
      this.overlayDebug = new GuiOverlayDebug(☃);
      this.spectatorGui = new GuiSpectator(☃);
      this.persistantChatGUI = new GuiNewChat(☃);
      this.overlayPlayerList = new GuiPlayerTabOverlay(☃, this);
      this.overlayBoss = new GuiBossOverlay(☃);
      this.overlaySubtitle = new GuiSubtitleOverlay(☃);

      for (ChatType ☃ : ChatType.values()) {
         this.chatListeners.put(☃, Lists.newArrayList());
      }

      IChatListener ☃ = NarratorChatListener.INSTANCE;
      this.chatListeners.get(ChatType.CHAT).add(new NormalChatListener(☃));
      this.chatListeners.get(ChatType.CHAT).add(☃);
      this.chatListeners.get(ChatType.SYSTEM).add(new NormalChatListener(☃));
      this.chatListeners.get(ChatType.SYSTEM).add(☃);
      this.chatListeners.get(ChatType.GAME_INFO).add(new OverlayChatListener(☃));
      this.setDefaultTitlesTimes();
   }

   public void setDefaultTitlesTimes() {
      this.titleFadeIn = 10;
      this.titleDisplayTime = 70;
      this.titleFadeOut = 20;
   }

   public void renderGameOverlay(float var1) {
      ScaledResolution ☃ = new ScaledResolution(this.mc);
      int ☃x = ☃.getScaledWidth();
      int ☃xx = ☃.getScaledHeight();
      FontRenderer ☃xxx = this.getFontRenderer();
      GlStateManager.enableBlend();
      if (Minecraft.isFancyGraphicsEnabled()) {
         this.renderVignette(this.mc.player.getBrightness(), ☃);
      } else {
         GlStateManager.enableDepth();
         GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
         );
      }

      ItemStack ☃xxxx = this.mc.player.inventory.armorItemInSlot(3);
      if (this.mc.gameSettings.thirdPersonView == 0 && ☃xxxx.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN)) {
         this.renderPumpkinOverlay(☃);
      }

      if (!this.mc.player.isPotionActive(MobEffects.NAUSEA)) {
         float ☃xxxxx = this.mc.player.prevTimeInPortal + (this.mc.player.timeInPortal - this.mc.player.prevTimeInPortal) * ☃;
         if (☃xxxxx > 0.0F) {
            this.renderPortal(☃xxxxx, ☃);
         }
      }

      if (this.mc.playerController.isSpectator()) {
         this.spectatorGui.renderTooltip(☃, ☃);
      } else {
         this.renderHotbar(☃, ☃);
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(ICONS);
      GlStateManager.enableBlend();
      this.renderAttackIndicator(☃, ☃);
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      this.mc.profiler.startSection("bossHealth");
      this.overlayBoss.renderBossHealth();
      this.mc.profiler.endSection();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(ICONS);
      if (this.mc.playerController.shouldDrawHUD()) {
         this.renderPlayerStats(☃);
      }

      this.renderMountHealth(☃);
      GlStateManager.disableBlend();
      if (this.mc.player.getSleepTimer() > 0) {
         this.mc.profiler.startSection("sleep");
         GlStateManager.disableDepth();
         GlStateManager.disableAlpha();
         int ☃xxxxx = this.mc.player.getSleepTimer();
         float ☃xxxxxx = ☃xxxxx / 100.0F;
         if (☃xxxxxx > 1.0F) {
            ☃xxxxxx = 1.0F - (☃xxxxx - 100) / 10.0F;
         }

         int ☃xxxxxxx = (int)(220.0F * ☃xxxxxx) << 24 | 1052704;
         drawRect(0, 0, ☃x, ☃xx, ☃xxxxxxx);
         GlStateManager.enableAlpha();
         GlStateManager.enableDepth();
         this.mc.profiler.endSection();
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      int ☃xxxxx = ☃x / 2 - 91;
      if (this.mc.player.isRidingHorse()) {
         this.renderHorseJumpBar(☃, ☃xxxxx);
      } else if (this.mc.playerController.gameIsSurvivalOrAdventure()) {
         this.renderExpBar(☃, ☃xxxxx);
      }

      if (this.mc.gameSettings.heldItemTooltips && !this.mc.playerController.isSpectator()) {
         this.renderSelectedItem(☃);
      } else if (this.mc.player.isSpectator()) {
         this.spectatorGui.renderSelectedItem(☃);
      }

      if (this.mc.isDemo()) {
         this.renderDemo(☃);
      }

      this.renderPotionEffects(☃);
      if (this.mc.gameSettings.showDebugInfo) {
         this.overlayDebug.renderDebugInfo(☃);
      }

      if (this.overlayMessageTime > 0) {
         this.mc.profiler.startSection("overlayMessage");
         float ☃xxxxxx = this.overlayMessageTime - ☃;
         int ☃xxxxxxx = (int)(☃xxxxxx * 255.0F / 20.0F);
         if (☃xxxxxxx > 255) {
            ☃xxxxxxx = 255;
         }

         if (☃xxxxxxx > 8) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(☃x / 2), (float)(☃xx - 68), 0.0F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(
               GlStateManager.SourceFactor.SRC_ALPHA,
               GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
               GlStateManager.SourceFactor.ONE,
               GlStateManager.DestFactor.ZERO
            );
            int ☃xxxxxxxx = 16777215;
            if (this.animateOverlayMessageColor) {
               ☃xxxxxxxx = MathHelper.hsvToRGB(☃xxxxxx / 50.0F, 0.7F, 0.6F) & 16777215;
            }

            ☃xxx.drawString(this.overlayMessage, -☃xxx.getStringWidth(this.overlayMessage) / 2, -4, ☃xxxxxxxx + (☃xxxxxxx << 24 & 0xFF000000));
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
         }

         this.mc.profiler.endSection();
      }

      this.overlaySubtitle.renderSubtitles(☃);
      if (this.titlesTimer > 0) {
         this.mc.profiler.startSection("titleAndSubtitle");
         float ☃xxxxxxxx = this.titlesTimer - ☃;
         int ☃xxxxxxxxx = 255;
         if (this.titlesTimer > this.titleFadeOut + this.titleDisplayTime) {
            float ☃xxxxxxxxxx = this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut - ☃xxxxxxxx;
            ☃xxxxxxxxx = (int)(☃xxxxxxxxxx * 255.0F / this.titleFadeIn);
         }

         if (this.titlesTimer <= this.titleFadeOut) {
            ☃xxxxxxxxx = (int)(☃xxxxxxxx * 255.0F / this.titleFadeOut);
         }

         ☃xxxxxxxxx = MathHelper.clamp(☃xxxxxxxxx, 0, 255);
         if (☃xxxxxxxxx > 8) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(☃x / 2), (float)(☃xx / 2), 0.0F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(
               GlStateManager.SourceFactor.SRC_ALPHA,
               GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
               GlStateManager.SourceFactor.ONE,
               GlStateManager.DestFactor.ZERO
            );
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 4.0F);
            int ☃xxxxxxxxxx = ☃xxxxxxxxx << 24 & 0xFF000000;
            ☃xxx.drawString(this.displayedTitle, -☃xxx.getStringWidth(this.displayedTitle) / 2, -10.0F, 16777215 | ☃xxxxxxxxxx, true);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(2.0F, 2.0F, 2.0F);
            ☃xxx.drawString(this.displayedSubTitle, -☃xxx.getStringWidth(this.displayedSubTitle) / 2, 5.0F, 16777215 | ☃xxxxxxxxxx, true);
            GlStateManager.popMatrix();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
         }

         this.mc.profiler.endSection();
      }

      Scoreboard ☃xxxxxxxxxx = this.mc.world.getScoreboard();
      ScoreObjective ☃xxxxxxxxxxx = null;
      ScorePlayerTeam ☃xxxxxxxxxxxx = ☃xxxxxxxxxx.getPlayersTeam(this.mc.player.getName());
      if (☃xxxxxxxxxxxx != null) {
         int ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxxx.getColor().getColorIndex();
         if (☃xxxxxxxxxxxxx >= 0) {
            ☃xxxxxxxxxxx = ☃xxxxxxxxxx.getObjectiveInDisplaySlot(3 + ☃xxxxxxxxxxxxx);
         }
      }

      ScoreObjective ☃xxxxxxxxxxxxx = ☃xxxxxxxxxxx != null ? ☃xxxxxxxxxxx : ☃xxxxxxxxxx.getObjectiveInDisplaySlot(1);
      if (☃xxxxxxxxxxxxx != null) {
         this.renderScoreboard(☃xxxxxxxxxxxxx, ☃);
      }

      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      GlStateManager.disableAlpha();
      GlStateManager.pushMatrix();
      GlStateManager.translate(0.0F, (float)(☃xx - 48), 0.0F);
      this.mc.profiler.startSection("chat");
      this.persistantChatGUI.drawChat(this.updateCounter);
      this.mc.profiler.endSection();
      GlStateManager.popMatrix();
      ☃xxxxxxxxxxxxx = ☃xxxxxxxxxx.getObjectiveInDisplaySlot(0);
      if (!this.mc.gameSettings.keyBindPlayerList.isKeyDown()
         || this.mc.isIntegratedServerRunning() && this.mc.player.connection.getPlayerInfoMap().size() <= 1 && ☃xxxxxxxxxxxxx == null) {
         this.overlayPlayerList.updatePlayerList(false);
      } else {
         this.overlayPlayerList.updatePlayerList(true);
         this.overlayPlayerList.renderPlayerlist(☃x, ☃xxxxxxxxxx, ☃xxxxxxxxxxxxx);
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableLighting();
      GlStateManager.enableAlpha();
   }

   private void renderAttackIndicator(float var1, ScaledResolution var2) {
      GameSettings ☃ = this.mc.gameSettings;
      if (☃.thirdPersonView == 0) {
         if (this.mc.playerController.isSpectator() && this.mc.pointedEntity == null) {
            RayTraceResult ☃x = this.mc.objectMouseOver;
            if (☃x == null || ☃x.typeOfHit != RayTraceResult.Type.BLOCK) {
               return;
            }

            BlockPos ☃xx = ☃x.getBlockPos();
            if (!this.mc.world.getBlockState(☃xx).getBlock().hasTileEntity() || !(this.mc.world.getTileEntity(☃xx) instanceof IInventory)) {
               return;
            }
         }

         int ☃xx = ☃.getScaledWidth();
         int ☃xxx = ☃.getScaledHeight();
         if (☃.showDebugInfo && !☃.hideGUI && !this.mc.player.hasReducedDebug() && !☃.reducedDebugInfo) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(☃xx / 2), (float)(☃xxx / 2), this.zLevel);
            Entity ☃xxxx = this.mc.getRenderViewEntity();
            GlStateManager.rotate(☃xxxx.prevRotationPitch + (☃xxxx.rotationPitch - ☃xxxx.prevRotationPitch) * ☃, -1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(☃xxxx.prevRotationYaw + (☃xxxx.rotationYaw - ☃xxxx.prevRotationYaw) * ☃, 0.0F, 1.0F, 0.0F);
            GlStateManager.scale(-1.0F, -1.0F, -1.0F);
            OpenGlHelper.renderDirections(10);
            GlStateManager.popMatrix();
         } else {
            GlStateManager.tryBlendFuncSeparate(
               GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
               GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
               GlStateManager.SourceFactor.ONE,
               GlStateManager.DestFactor.ZERO
            );
            GlStateManager.enableAlpha();
            this.drawTexturedModalRect(☃xx / 2 - 7, ☃xxx / 2 - 7, 0, 0, 16, 16);
            if (this.mc.gameSettings.attackIndicator == 1) {
               float ☃xxxx = this.mc.player.getCooledAttackStrength(0.0F);
               boolean ☃xxxxx = false;
               if (this.mc.pointedEntity != null && this.mc.pointedEntity instanceof EntityLivingBase && ☃xxxx >= 1.0F) {
                  ☃xxxxx = this.mc.player.getCooldownPeriod() > 5.0F;
                  ☃xxxxx &= ((EntityLivingBase)this.mc.pointedEntity).isEntityAlive();
               }

               int ☃xxxxxx = ☃xxx / 2 - 7 + 16;
               int ☃xxxxxxx = ☃xx / 2 - 8;
               if (☃xxxxx) {
                  this.drawTexturedModalRect(☃xxxxxxx, ☃xxxxxx, 68, 94, 16, 16);
               } else if (☃xxxx < 1.0F) {
                  int ☃xxxxxxxx = (int)(☃xxxx * 17.0F);
                  this.drawTexturedModalRect(☃xxxxxxx, ☃xxxxxx, 36, 94, 16, 4);
                  this.drawTexturedModalRect(☃xxxxxxx, ☃xxxxxx, 52, 94, ☃xxxxxxxx, 4);
               }
            }
         }
      }
   }

   protected void renderPotionEffects(ScaledResolution var1) {
      Collection<PotionEffect> ☃ = this.mc.player.getActivePotionEffects();
      if (!☃.isEmpty()) {
         this.mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
         GlStateManager.enableBlend();
         int ☃x = 0;
         int ☃xx = 0;

         for (PotionEffect ☃xxx : Ordering.natural().reverse().sortedCopy(☃)) {
            Potion ☃xxxx = ☃xxx.getPotion();
            if (☃xxxx.hasStatusIcon() && ☃xxx.doesShowParticles()) {
               int ☃xxxxx = ☃.getScaledWidth();
               int ☃xxxxxx = 1;
               if (this.mc.isDemo()) {
                  ☃xxxxxx += 15;
               }

               int ☃xxxxxxx = ☃xxxx.getStatusIconIndex();
               if (☃xxxx.isBeneficial()) {
                  ☃x++;
                  ☃xxxxx -= 25 * ☃x;
               } else {
                  ☃xx++;
                  ☃xxxxx -= 25 * ☃xx;
                  ☃xxxxxx += 26;
               }

               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               float ☃xxxxxxxx = 1.0F;
               if (☃xxx.getIsAmbient()) {
                  this.drawTexturedModalRect(☃xxxxx, ☃xxxxxx, 165, 166, 24, 24);
               } else {
                  this.drawTexturedModalRect(☃xxxxx, ☃xxxxxx, 141, 166, 24, 24);
                  if (☃xxx.getDuration() <= 200) {
                     int ☃xxxxxxxxx = 10 - ☃xxx.getDuration() / 20;
                     ☃xxxxxxxx = MathHelper.clamp(☃xxx.getDuration() / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F)
                        + MathHelper.cos(☃xxx.getDuration() * (float) Math.PI / 5.0F) * MathHelper.clamp(☃xxxxxxxxx / 10.0F * 0.25F, 0.0F, 0.25F);
                  }
               }

               GlStateManager.color(1.0F, 1.0F, 1.0F, ☃xxxxxxxx);
               this.drawTexturedModalRect(☃xxxxx + 3, ☃xxxxxx + 3, ☃xxxxxxx % 8 * 18, 198 + ☃xxxxxxx / 8 * 18, 18, 18);
            }
         }
      }
   }

   protected void renderHotbar(ScaledResolution var1, float var2) {
      if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.mc.getTextureManager().bindTexture(WIDGETS_TEX_PATH);
         EntityPlayer ☃ = (EntityPlayer)this.mc.getRenderViewEntity();
         ItemStack ☃x = ☃.getHeldItemOffhand();
         EnumHandSide ☃xx = ☃.getPrimaryHand().opposite();
         int ☃xxx = ☃.getScaledWidth() / 2;
         float ☃xxxx = this.zLevel;
         int ☃xxxxx = 182;
         int ☃xxxxxx = 91;
         this.zLevel = -90.0F;
         this.drawTexturedModalRect(☃xxx - 91, ☃.getScaledHeight() - 22, 0, 0, 182, 22);
         this.drawTexturedModalRect(☃xxx - 91 - 1 + ☃.inventory.currentItem * 20, ☃.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
         if (!☃x.isEmpty()) {
            if (☃xx == EnumHandSide.LEFT) {
               this.drawTexturedModalRect(☃xxx - 91 - 29, ☃.getScaledHeight() - 23, 24, 22, 29, 24);
            } else {
               this.drawTexturedModalRect(☃xxx + 91, ☃.getScaledHeight() - 23, 53, 22, 29, 24);
            }
         }

         this.zLevel = ☃xxxx;
         GlStateManager.enableRescaleNormal();
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
         );
         RenderHelper.enableGUIStandardItemLighting();

         for (int ☃xxxxxxx = 0; ☃xxxxxxx < 9; ☃xxxxxxx++) {
            int ☃xxxxxxxx = ☃xxx - 90 + ☃xxxxxxx * 20 + 2;
            int ☃xxxxxxxxx = ☃.getScaledHeight() - 16 - 3;
            this.renderHotbarItem(☃xxxxxxxx, ☃xxxxxxxxx, ☃, ☃, ☃.inventory.mainInventory.get(☃xxxxxxx));
         }

         if (!☃x.isEmpty()) {
            int ☃xxxxxxx = ☃.getScaledHeight() - 16 - 3;
            if (☃xx == EnumHandSide.LEFT) {
               this.renderHotbarItem(☃xxx - 91 - 26, ☃xxxxxxx, ☃, ☃, ☃x);
            } else {
               this.renderHotbarItem(☃xxx + 91 + 10, ☃xxxxxxx, ☃, ☃, ☃x);
            }
         }

         if (this.mc.gameSettings.attackIndicator == 2) {
            float ☃xxxxxxx = this.mc.player.getCooledAttackStrength(0.0F);
            if (☃xxxxxxx < 1.0F) {
               int ☃xxxxxxxx = ☃.getScaledHeight() - 20;
               int ☃xxxxxxxxx = ☃xxx + 91 + 6;
               if (☃xx == EnumHandSide.RIGHT) {
                  ☃xxxxxxxxx = ☃xxx - 91 - 22;
               }

               this.mc.getTextureManager().bindTexture(Gui.ICONS);
               int ☃xxxxxxxxxx = (int)(☃xxxxxxx * 19.0F);
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               this.drawTexturedModalRect(☃xxxxxxxxx, ☃xxxxxxxx, 0, 94, 18, 18);
               this.drawTexturedModalRect(☃xxxxxxxxx, ☃xxxxxxxx + 18 - ☃xxxxxxxxxx, 18, 112 - ☃xxxxxxxxxx, 18, ☃xxxxxxxxxx);
            }
         }

         RenderHelper.disableStandardItemLighting();
         GlStateManager.disableRescaleNormal();
         GlStateManager.disableBlend();
      }
   }

   public void renderHorseJumpBar(ScaledResolution var1, int var2) {
      this.mc.profiler.startSection("jumpBar");
      this.mc.getTextureManager().bindTexture(Gui.ICONS);
      float ☃ = this.mc.player.getHorseJumpPower();
      int ☃x = 182;
      int ☃xx = (int)(☃ * 183.0F);
      int ☃xxx = ☃.getScaledHeight() - 32 + 3;
      this.drawTexturedModalRect(☃, ☃xxx, 0, 84, 182, 5);
      if (☃xx > 0) {
         this.drawTexturedModalRect(☃, ☃xxx, 0, 89, ☃xx, 5);
      }

      this.mc.profiler.endSection();
   }

   public void renderExpBar(ScaledResolution var1, int var2) {
      this.mc.profiler.startSection("expBar");
      this.mc.getTextureManager().bindTexture(Gui.ICONS);
      int ☃ = this.mc.player.xpBarCap();
      if (☃ > 0) {
         int ☃x = 182;
         int ☃xx = (int)(this.mc.player.experience * 183.0F);
         int ☃xxx = ☃.getScaledHeight() - 32 + 3;
         this.drawTexturedModalRect(☃, ☃xxx, 0, 64, 182, 5);
         if (☃xx > 0) {
            this.drawTexturedModalRect(☃, ☃xxx, 0, 69, ☃xx, 5);
         }
      }

      this.mc.profiler.endSection();
      if (this.mc.player.experienceLevel > 0) {
         this.mc.profiler.startSection("expLevel");
         String ☃x = "" + this.mc.player.experienceLevel;
         int ☃xx = (☃.getScaledWidth() - this.getFontRenderer().getStringWidth(☃x)) / 2;
         int ☃xxx = ☃.getScaledHeight() - 31 - 4;
         this.getFontRenderer().drawString(☃x, ☃xx + 1, ☃xxx, 0);
         this.getFontRenderer().drawString(☃x, ☃xx - 1, ☃xxx, 0);
         this.getFontRenderer().drawString(☃x, ☃xx, ☃xxx + 1, 0);
         this.getFontRenderer().drawString(☃x, ☃xx, ☃xxx - 1, 0);
         this.getFontRenderer().drawString(☃x, ☃xx, ☃xxx, 8453920);
         this.mc.profiler.endSection();
      }
   }

   public void renderSelectedItem(ScaledResolution var1) {
      this.mc.profiler.startSection("selectedItemName");
      if (this.remainingHighlightTicks > 0 && !this.highlightingItemStack.isEmpty()) {
         String ☃ = this.highlightingItemStack.getDisplayName();
         if (this.highlightingItemStack.hasDisplayName()) {
            ☃ = TextFormatting.ITALIC + ☃;
         }

         int ☃x = (☃.getScaledWidth() - this.getFontRenderer().getStringWidth(☃)) / 2;
         int ☃xx = ☃.getScaledHeight() - 59;
         if (!this.mc.playerController.shouldDrawHUD()) {
            ☃xx += 14;
         }

         int ☃xxx = (int)(this.remainingHighlightTicks * 256.0F / 10.0F);
         if (☃xxx > 255) {
            ☃xxx = 255;
         }

         if (☃xxx > 0) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(
               GlStateManager.SourceFactor.SRC_ALPHA,
               GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
               GlStateManager.SourceFactor.ONE,
               GlStateManager.DestFactor.ZERO
            );
            this.getFontRenderer().drawStringWithShadow(☃, ☃x, ☃xx, 16777215 + (☃xxx << 24));
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
         }
      }

      this.mc.profiler.endSection();
   }

   public void renderDemo(ScaledResolution var1) {
      this.mc.profiler.startSection("demo");
      String ☃;
      if (this.mc.world.getTotalWorldTime() >= 120500L) {
         ☃ = I18n.format("demo.demoExpired");
      } else {
         ☃ = I18n.format("demo.remainingTime", StringUtils.ticksToElapsedTime((int)(120500L - this.mc.world.getTotalWorldTime())));
      }

      int ☃x = this.getFontRenderer().getStringWidth(☃);
      this.getFontRenderer().drawStringWithShadow(☃, ☃.getScaledWidth() - ☃x - 10, 5.0F, 16777215);
      this.mc.profiler.endSection();
   }

   private void renderScoreboard(ScoreObjective var1, ScaledResolution var2) {
      Scoreboard ☃ = ☃.getScoreboard();
      Collection<Score> ☃x = ☃.getSortedScores(☃);
      List<Score> ☃xx = Lists.newArrayList(Iterables.filter(☃x, new Predicate<Score>() {
         public boolean apply(@Nullable Score var1) {
            return ☃.getPlayerName() != null && !☃.getPlayerName().startsWith("#");
         }
      }));
      if (☃xx.size() > 15) {
         ☃x = Lists.newArrayList(Iterables.skip(☃xx, ☃x.size() - 15));
      } else {
         ☃x = ☃xx;
      }

      int ☃xxx = this.getFontRenderer().getStringWidth(☃.getDisplayName());

      for (Score ☃xxxx : ☃x) {
         ScorePlayerTeam ☃xxxxx = ☃.getPlayersTeam(☃xxxx.getPlayerName());
         String ☃xxxxxx = ScorePlayerTeam.formatPlayerName(☃xxxxx, ☃xxxx.getPlayerName()) + ": " + TextFormatting.RED + ☃xxxx.getScorePoints();
         ☃xxx = Math.max(☃xxx, this.getFontRenderer().getStringWidth(☃xxxxxx));
      }

      int ☃xxxx = ☃x.size() * this.getFontRenderer().FONT_HEIGHT;
      int ☃xxxxx = ☃.getScaledHeight() / 2 + ☃xxxx / 3;
      int ☃xxxxxx = 3;
      int ☃xxxxxxx = ☃.getScaledWidth() - ☃xxx - 3;
      int ☃xxxxxxxx = 0;

      for (Score ☃xxxxxxxxx : ☃x) {
         ☃xxxxxxxx++;
         ScorePlayerTeam ☃xxxxxxxxxx = ☃.getPlayersTeam(☃xxxxxxxxx.getPlayerName());
         String ☃xxxxxxxxxxx = ScorePlayerTeam.formatPlayerName(☃xxxxxxxxxx, ☃xxxxxxxxx.getPlayerName());
         String ☃xxxxxxxxxxxx = TextFormatting.RED + "" + ☃xxxxxxxxx.getScorePoints();
         int ☃xxxxxxxxxxxxx = ☃xxxxx - ☃xxxxxxxx * this.getFontRenderer().FONT_HEIGHT;
         int ☃xxxxxxxxxxxxxx = ☃.getScaledWidth() - 3 + 2;
         drawRect(☃xxxxxxx - 2, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxx + this.getFontRenderer().FONT_HEIGHT, 1342177280);
         this.getFontRenderer().drawString(☃xxxxxxxxxxx, ☃xxxxxxx, ☃xxxxxxxxxxxxx, 553648127);
         this.getFontRenderer().drawString(☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxxx - this.getFontRenderer().getStringWidth(☃xxxxxxxxxxxx), ☃xxxxxxxxxxxxx, 553648127);
         if (☃xxxxxxxx == ☃x.size()) {
            String ☃xxxxxxxxxxxxxxx = ☃.getDisplayName();
            drawRect(☃xxxxxxx - 2, ☃xxxxxxxxxxxxx - this.getFontRenderer().FONT_HEIGHT - 1, ☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxx - 1, 1610612736);
            drawRect(☃xxxxxxx - 2, ☃xxxxxxxxxxxxx - 1, ☃xxxxxxxxxxxxxx, ☃xxxxxxxxxxxxx, 1342177280);
            this.getFontRenderer()
               .drawString(
                  ☃xxxxxxxxxxxxxxx,
                  ☃xxxxxxx + ☃xxx / 2 - this.getFontRenderer().getStringWidth(☃xxxxxxxxxxxxxxx) / 2,
                  ☃xxxxxxxxxxxxx - this.getFontRenderer().FONT_HEIGHT,
                  553648127
               );
         }
      }
   }

   private void renderPlayerStats(ScaledResolution var1) {
      if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
         EntityPlayer ☃ = (EntityPlayer)this.mc.getRenderViewEntity();
         int ☃x = MathHelper.ceil(☃.getHealth());
         boolean ☃xx = this.healthUpdateCounter > this.updateCounter && (this.healthUpdateCounter - this.updateCounter) / 3L % 2L == 1L;
         if (☃x < this.playerHealth && ☃.hurtResistantTime > 0) {
            this.lastSystemTime = Minecraft.getSystemTime();
            this.healthUpdateCounter = this.updateCounter + 20;
         } else if (☃x > this.playerHealth && ☃.hurtResistantTime > 0) {
            this.lastSystemTime = Minecraft.getSystemTime();
            this.healthUpdateCounter = this.updateCounter + 10;
         }

         if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L) {
            this.playerHealth = ☃x;
            this.lastPlayerHealth = ☃x;
            this.lastSystemTime = Minecraft.getSystemTime();
         }

         this.playerHealth = ☃x;
         int ☃xxx = this.lastPlayerHealth;
         this.rand.setSeed(this.updateCounter * 312871);
         FoodStats ☃xxxx = ☃.getFoodStats();
         int ☃xxxxx = ☃xxxx.getFoodLevel();
         IAttributeInstance ☃xxxxxx = ☃.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
         int ☃xxxxxxx = ☃.getScaledWidth() / 2 - 91;
         int ☃xxxxxxxx = ☃.getScaledWidth() / 2 + 91;
         int ☃xxxxxxxxx = ☃.getScaledHeight() - 39;
         float ☃xxxxxxxxxx = (float)☃xxxxxx.getAttributeValue();
         int ☃xxxxxxxxxxx = MathHelper.ceil(☃.getAbsorptionAmount());
         int ☃xxxxxxxxxxxx = MathHelper.ceil((☃xxxxxxxxxx + ☃xxxxxxxxxxx) / 2.0F / 10.0F);
         int ☃xxxxxxxxxxxxx = Math.max(10 - (☃xxxxxxxxxxxx - 2), 3);
         int ☃xxxxxxxxxxxxxx = ☃xxxxxxxxx - (☃xxxxxxxxxxxx - 1) * ☃xxxxxxxxxxxxx - 10;
         int ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxx - 10;
         int ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxx;
         int ☃xxxxxxxxxxxxxxxxx = ☃.getTotalArmorValue();
         int ☃xxxxxxxxxxxxxxxxxx = -1;
         if (☃.isPotionActive(MobEffects.REGENERATION)) {
            ☃xxxxxxxxxxxxxxxxxx = this.updateCounter % MathHelper.ceil(☃xxxxxxxxxx + 5.0F);
         }

         this.mc.profiler.startSection("armor");

         for (int ☃xxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxx < 10; ☃xxxxxxxxxxxxxxxxxxx++) {
            if (☃xxxxxxxxxxxxxxxxx > 0) {
               int ☃xxxxxxxxxxxxxxxxxxxx = ☃xxxxxxx + ☃xxxxxxxxxxxxxxxxxxx * 8;
               if (☃xxxxxxxxxxxxxxxxxxx * 2 + 1 < ☃xxxxxxxxxxxxxxxxx) {
                  this.drawTexturedModalRect(☃xxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxx, 34, 9, 9, 9);
               }

               if (☃xxxxxxxxxxxxxxxxxxx * 2 + 1 == ☃xxxxxxxxxxxxxxxxx) {
                  this.drawTexturedModalRect(☃xxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxx, 25, 9, 9, 9);
               }

               if (☃xxxxxxxxxxxxxxxxxxx * 2 + 1 > ☃xxxxxxxxxxxxxxxxx) {
                  this.drawTexturedModalRect(☃xxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxx, 16, 9, 9, 9);
               }
            }
         }

         this.mc.profiler.endStartSection("health");

         for (int ☃xxxxxxxxxxxxxxxxxxxxx = MathHelper.ceil((☃xxxxxxxxxx + ☃xxxxxxxxxxx) / 2.0F) - 1; ☃xxxxxxxxxxxxxxxxxxxxx >= 0; ☃xxxxxxxxxxxxxxxxxxxxx--) {
            int ☃xxxxxxxxxxxxxxxxxxxxxx = 16;
            if (☃.isPotionActive(MobEffects.POISON)) {
               ☃xxxxxxxxxxxxxxxxxxxxxx += 36;
            } else if (☃.isPotionActive(MobEffects.WITHER)) {
               ☃xxxxxxxxxxxxxxxxxxxxxx += 72;
            }

            int ☃xxxxxxxxxxxxxxxxxxxxxxx = 0;
            if (☃xx) {
               ☃xxxxxxxxxxxxxxxxxxxxxxx = 1;
            }

            int ☃xxxxxxxxxxxxxxxxxxxxxxxx = MathHelper.ceil((☃xxxxxxxxxxxxxxxxxxxxx + 1) / 10.0F) - 1;
            int ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxx % 10 * 8;
            int ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxx;
            if (☃x <= 4) {
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxx += this.rand.nextInt(2);
            }

            if (☃xxxxxxxxxxxxxxxx <= 0 && ☃xxxxxxxxxxxxxxxxxxxxx == ☃xxxxxxxxxxxxxxxxxx) {
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxx -= 2;
            }

            int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = 0;
            if (☃.world.getWorldInfo().isHardcoreModeEnabled()) {
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx = 5;
            }

            this.drawTexturedModalRect(
               ☃xxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxx, 16 + ☃xxxxxxxxxxxxxxxxxxxxxxx * 9, 9 * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx, 9, 9
            );
            if (☃xx) {
               if (☃xxxxxxxxxxxxxxxxxxxxx * 2 + 1 < ☃xxx) {
                  this.drawTexturedModalRect(
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx + 54, 9 * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx, 9, 9
                  );
               }

               if (☃xxxxxxxxxxxxxxxxxxxxx * 2 + 1 == ☃xxx) {
                  this.drawTexturedModalRect(
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx + 63, 9 * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx, 9, 9
                  );
               }
            }

            if (☃xxxxxxxxxxxxxxxx > 0) {
               if (☃xxxxxxxxxxxxxxxx == ☃xxxxxxxxxxx && ☃xxxxxxxxxxx % 2 == 1) {
                  this.drawTexturedModalRect(
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx + 153, 9 * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx, 9, 9
                  );
                  ☃xxxxxxxxxxxxxxxx--;
               } else {
                  this.drawTexturedModalRect(
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx + 144, 9 * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx, 9, 9
                  );
                  ☃xxxxxxxxxxxxxxxx -= 2;
               }
            } else {
               if (☃xxxxxxxxxxxxxxxxxxxxx * 2 + 1 < ☃x) {
                  this.drawTexturedModalRect(
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx + 36, 9 * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx, 9, 9
                  );
               }

               if (☃xxxxxxxxxxxxxxxxxxxxx * 2 + 1 == ☃x) {
                  this.drawTexturedModalRect(
                     ☃xxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx + 45, 9 * ☃xxxxxxxxxxxxxxxxxxxxxxxxxxx, 9, 9
                  );
               }
            }
         }

         Entity ☃xxxxxxxxxxxxxxxxxxxxx = ☃.getRidingEntity();
         if (☃xxxxxxxxxxxxxxxxxxxxx == null || !(☃xxxxxxxxxxxxxxxxxxxxx instanceof EntityLivingBase)) {
            this.mc.profiler.endStartSection("food");

            for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0; ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx < 10; ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx++) {
               int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxx;
               int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 16;
               int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0;
               if (☃.isPotionActive(MobEffects.HUNGER)) {
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx += 36;
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 13;
               }

               if (☃.getFoodStats().getSaturationLevel() <= 0.0F && this.updateCounter % (☃xxxxx * 3 + 1) == 0) {
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxx + (this.rand.nextInt(3) - 1);
               }

               int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx * 8 - 9;
               this.drawTexturedModalRect(
                  ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx, 16 + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * 9, 27, 9, 9
               );
               if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx * 2 + 1 < ☃xxxxx) {
                  this.drawTexturedModalRect(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 36, 27, 9, 9);
               }

               if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx * 2 + 1 == ☃xxxxx) {
                  this.drawTexturedModalRect(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + 45, 27, 9, 9);
               }
            }
         }

         this.mc.profiler.endStartSection("air");
         if (☃.isInsideOfMaterial(Material.WATER)) {
            int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx = this.mc.player.getAir();
            int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = MathHelper.ceil((☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx - 2) * 10.0 / 300.0);
            int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = MathHelper.ceil(☃xxxxxxxxxxxxxxxxxxxxxxxxxxxx * 10.0 / 300.0) - ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;

            for (int ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx = 0;
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx + ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
               ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx++
            ) {
               if (☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx < ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx) {
                  this.drawTexturedModalRect(☃xxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * 8 - 9, ☃xxxxxxxxxxxxxxx, 16, 18, 9, 9);
               } else {
                  this.drawTexturedModalRect(☃xxxxxxxx - ☃xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx * 8 - 9, ☃xxxxxxxxxxxxxxx, 25, 18, 9, 9);
               }
            }
         }

         this.mc.profiler.endSection();
      }
   }

   private void renderMountHealth(ScaledResolution var1) {
      if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
         EntityPlayer ☃ = (EntityPlayer)this.mc.getRenderViewEntity();
         Entity ☃x = ☃.getRidingEntity();
         if (☃x instanceof EntityLivingBase) {
            this.mc.profiler.endStartSection("mountHealth");
            EntityLivingBase ☃xx = (EntityLivingBase)☃x;
            int ☃xxx = (int)Math.ceil(☃xx.getHealth());
            float ☃xxxx = ☃xx.getMaxHealth();
            int ☃xxxxx = (int)(☃xxxx + 0.5F) / 2;
            if (☃xxxxx > 30) {
               ☃xxxxx = 30;
            }

            int ☃xxxxxx = ☃.getScaledHeight() - 39;
            int ☃xxxxxxx = ☃.getScaledWidth() / 2 + 91;
            int ☃xxxxxxxx = ☃xxxxxx;
            int ☃xxxxxxxxx = 0;

            for (boolean ☃xxxxxxxxxx = false; ☃xxxxx > 0; ☃xxxxxxxxx += 20) {
               int ☃xxxxxxxxxxx = Math.min(☃xxxxx, 10);
               ☃xxxxx -= ☃xxxxxxxxxxx;

               for (int ☃xxxxxxxxxxxx = 0; ☃xxxxxxxxxxxx < ☃xxxxxxxxxxx; ☃xxxxxxxxxxxx++) {
                  int ☃xxxxxxxxxxxxx = 52;
                  int ☃xxxxxxxxxxxxxx = 0;
                  int ☃xxxxxxxxxxxxxxx = ☃xxxxxxx - ☃xxxxxxxxxxxx * 8 - 9;
                  this.drawTexturedModalRect(☃xxxxxxxxxxxxxxx, ☃xxxxxxxx, 52 + ☃xxxxxxxxxxxxxx * 9, 9, 9, 9);
                  if (☃xxxxxxxxxxxx * 2 + 1 + ☃xxxxxxxxx < ☃xxx) {
                     this.drawTexturedModalRect(☃xxxxxxxxxxxxxxx, ☃xxxxxxxx, 88, 9, 9, 9);
                  }

                  if (☃xxxxxxxxxxxx * 2 + 1 + ☃xxxxxxxxx == ☃xxx) {
                     this.drawTexturedModalRect(☃xxxxxxxxxxxxxxx, ☃xxxxxxxx, 97, 9, 9, 9);
                  }
               }

               ☃xxxxxxxx -= 10;
            }
         }
      }
   }

   private void renderPumpkinOverlay(ScaledResolution var1) {
      GlStateManager.disableDepth();
      GlStateManager.depthMask(false);
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableAlpha();
      this.mc.getTextureManager().bindTexture(PUMPKIN_BLUR_TEX_PATH);
      Tessellator ☃ = Tessellator.getInstance();
      BufferBuilder ☃x = ☃.getBuffer();
      ☃x.begin(7, DefaultVertexFormats.POSITION_TEX);
      ☃x.pos(0.0, ☃.getScaledHeight(), -90.0).tex(0.0, 1.0).endVertex();
      ☃x.pos(☃.getScaledWidth(), ☃.getScaledHeight(), -90.0).tex(1.0, 1.0).endVertex();
      ☃x.pos(☃.getScaledWidth(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
      ☃x.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
      ☃.draw();
      GlStateManager.depthMask(true);
      GlStateManager.enableDepth();
      GlStateManager.enableAlpha();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private void renderVignette(float var1, ScaledResolution var2) {
      ☃ = 1.0F - ☃;
      ☃ = MathHelper.clamp(☃, 0.0F, 1.0F);
      WorldBorder ☃ = this.mc.world.getWorldBorder();
      float ☃x = (float)☃.getClosestDistance(this.mc.player);
      double ☃xx = Math.min(☃.getResizeSpeed() * ☃.getWarningTime() * 1000.0, Math.abs(☃.getTargetSize() - ☃.getDiameter()));
      double ☃xxx = Math.max((double)☃.getWarningDistance(), ☃xx);
      if (☃x < ☃xxx) {
         ☃x = 1.0F - (float)(☃x / ☃xxx);
      } else {
         ☃x = 0.0F;
      }

      this.prevVignetteBrightness = (float)(this.prevVignetteBrightness + (☃ - this.prevVignetteBrightness) * 0.01);
      GlStateManager.disableDepth();
      GlStateManager.depthMask(false);
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      if (☃x > 0.0F) {
         GlStateManager.color(0.0F, ☃x, ☃x, 1.0F);
      } else {
         GlStateManager.color(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0F);
      }

      this.mc.getTextureManager().bindTexture(VIGNETTE_TEX_PATH);
      Tessellator ☃xxxx = Tessellator.getInstance();
      BufferBuilder ☃xxxxx = ☃xxxx.getBuffer();
      ☃xxxxx.begin(7, DefaultVertexFormats.POSITION_TEX);
      ☃xxxxx.pos(0.0, ☃.getScaledHeight(), -90.0).tex(0.0, 1.0).endVertex();
      ☃xxxxx.pos(☃.getScaledWidth(), ☃.getScaledHeight(), -90.0).tex(1.0, 1.0).endVertex();
      ☃xxxxx.pos(☃.getScaledWidth(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
      ☃xxxxx.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
      ☃xxxx.draw();
      GlStateManager.depthMask(true);
      GlStateManager.enableDepth();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
   }

   private void renderPortal(float var1, ScaledResolution var2) {
      if (☃ < 1.0F) {
         ☃ *= ☃;
         ☃ *= ☃;
         ☃ = ☃ * 0.8F + 0.2F;
      }

      GlStateManager.disableAlpha();
      GlStateManager.disableDepth();
      GlStateManager.depthMask(false);
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      GlStateManager.color(1.0F, 1.0F, 1.0F, ☃);
      this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      TextureAtlasSprite ☃ = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.PORTAL.getDefaultState());
      float ☃x = ☃.getMinU();
      float ☃xx = ☃.getMinV();
      float ☃xxx = ☃.getMaxU();
      float ☃xxxx = ☃.getMaxV();
      Tessellator ☃xxxxx = Tessellator.getInstance();
      BufferBuilder ☃xxxxxx = ☃xxxxx.getBuffer();
      ☃xxxxxx.begin(7, DefaultVertexFormats.POSITION_TEX);
      ☃xxxxxx.pos(0.0, ☃.getScaledHeight(), -90.0).tex(☃x, ☃xxxx).endVertex();
      ☃xxxxxx.pos(☃.getScaledWidth(), ☃.getScaledHeight(), -90.0).tex(☃xxx, ☃xxxx).endVertex();
      ☃xxxxxx.pos(☃.getScaledWidth(), 0.0, -90.0).tex(☃xxx, ☃xx).endVertex();
      ☃xxxxxx.pos(0.0, 0.0, -90.0).tex(☃x, ☃xx).endVertex();
      ☃xxxxx.draw();
      GlStateManager.depthMask(true);
      GlStateManager.enableDepth();
      GlStateManager.enableAlpha();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private void renderHotbarItem(int var1, int var2, float var3, EntityPlayer var4, ItemStack var5) {
      if (!☃.isEmpty()) {
         float ☃ = ☃.getAnimationsToGo() - ☃;
         if (☃ > 0.0F) {
            GlStateManager.pushMatrix();
            float ☃x = 1.0F + ☃ / 5.0F;
            GlStateManager.translate((float)(☃ + 8), (float)(☃ + 12), 0.0F);
            GlStateManager.scale(1.0F / ☃x, (☃x + 1.0F) / 2.0F, 1.0F);
            GlStateManager.translate((float)(-(☃ + 8)), (float)(-(☃ + 12)), 0.0F);
         }

         this.itemRenderer.renderItemAndEffectIntoGUI(☃, ☃, ☃, ☃);
         if (☃ > 0.0F) {
            GlStateManager.popMatrix();
         }

         this.itemRenderer.renderItemOverlays(this.mc.fontRenderer, ☃, ☃, ☃);
      }
   }

   public void updateTick() {
      if (this.overlayMessageTime > 0) {
         this.overlayMessageTime--;
      }

      if (this.titlesTimer > 0) {
         this.titlesTimer--;
         if (this.titlesTimer <= 0) {
            this.displayedTitle = "";
            this.displayedSubTitle = "";
         }
      }

      this.updateCounter++;
      if (this.mc.player != null) {
         ItemStack ☃ = this.mc.player.inventory.getCurrentItem();
         if (☃.isEmpty()) {
            this.remainingHighlightTicks = 0;
         } else if (!this.highlightingItemStack.isEmpty()
            && ☃.getItem() == this.highlightingItemStack.getItem()
            && ItemStack.areItemStackTagsEqual(☃, this.highlightingItemStack)
            && (☃.isItemStackDamageable() || ☃.getMetadata() == this.highlightingItemStack.getMetadata())) {
            if (this.remainingHighlightTicks > 0) {
               this.remainingHighlightTicks--;
            }
         } else {
            this.remainingHighlightTicks = 40;
         }

         this.highlightingItemStack = ☃;
      }
   }

   public void setRecordPlayingMessage(String var1) {
      this.setOverlayMessage(I18n.format("record.nowPlaying", ☃), true);
   }

   public void setOverlayMessage(String var1, boolean var2) {
      this.overlayMessage = ☃;
      this.overlayMessageTime = 60;
      this.animateOverlayMessageColor = ☃;
   }

   public void displayTitle(String var1, String var2, int var3, int var4, int var5) {
      if (☃ == null && ☃ == null && ☃ < 0 && ☃ < 0 && ☃ < 0) {
         this.displayedTitle = "";
         this.displayedSubTitle = "";
         this.titlesTimer = 0;
      } else if (☃ != null) {
         this.displayedTitle = ☃;
         this.titlesTimer = this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut;
      } else if (☃ != null) {
         this.displayedSubTitle = ☃;
      } else {
         if (☃ >= 0) {
            this.titleFadeIn = ☃;
         }

         if (☃ >= 0) {
            this.titleDisplayTime = ☃;
         }

         if (☃ >= 0) {
            this.titleFadeOut = ☃;
         }

         if (this.titlesTimer > 0) {
            this.titlesTimer = this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut;
         }
      }
   }

   public void setOverlayMessage(ITextComponent var1, boolean var2) {
      this.setOverlayMessage(☃.getUnformattedText(), ☃);
   }

   public void addChatMessage(ChatType var1, ITextComponent var2) {
      for (IChatListener ☃ : this.chatListeners.get(☃)) {
         ☃.say(☃, ☃);
      }
   }

   public GuiNewChat getChatGUI() {
      return this.persistantChatGUI;
   }

   public int getUpdateCounter() {
      return this.updateCounter;
   }

   public FontRenderer getFontRenderer() {
      return this.mc.fontRenderer;
   }

   public GuiSpectator getSpectatorGui() {
      return this.spectatorGui;
   }

   public GuiPlayerTabOverlay getTabList() {
      return this.overlayPlayerList;
   }

   public void resetPlayersOverlayFooterHeader() {
      this.overlayPlayerList.resetFooterHeader();
      this.overlayBoss.clearBossInfos();
      this.mc.getToastGui().clear();
   }

   public GuiBossOverlay getBossOverlay() {
      return this.overlayBoss;
   }
}
