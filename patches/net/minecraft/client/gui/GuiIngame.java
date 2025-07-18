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
import net.minecraft.block.state.IBlockState;
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
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.border.WorldBorder;
import net.optifine.CustomColors;
import net.optifine.CustomItems;
import net.optifine.TextureAnimations;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorForge;

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

   public GuiIngame(Minecraft mcIn) {
      this.mc = mcIn;
      this.itemRenderer = mcIn.getRenderItem();
      this.overlayDebug = new GuiOverlayDebug(mcIn);
      this.spectatorGui = new GuiSpectator(mcIn);
      this.persistantChatGUI = new GuiNewChat(mcIn);
      this.overlayPlayerList = new GuiPlayerTabOverlay(mcIn, this);
      this.overlayBoss = new GuiBossOverlay(mcIn);
      this.overlaySubtitle = new GuiSubtitleOverlay(mcIn);

      for (ChatType chattype : ChatType.values()) {
         this.chatListeners.put(chattype, Lists.newArrayList());
      }

      IChatListener ichatlistener = NarratorChatListener.INSTANCE;
      this.chatListeners.get(ChatType.CHAT).add(new NormalChatListener(mcIn));
      this.chatListeners.get(ChatType.CHAT).add(ichatlistener);
      this.chatListeners.get(ChatType.SYSTEM).add(new NormalChatListener(mcIn));
      this.chatListeners.get(ChatType.SYSTEM).add(ichatlistener);
      this.chatListeners.get(ChatType.GAME_INFO).add(new OverlayChatListener(mcIn));
      this.setDefaultTitlesTimes();
   }

   public void setDefaultTitlesTimes() {
      this.titleFadeIn = 10;
      this.titleDisplayTime = 70;
      this.titleFadeOut = 20;
   }

   public void renderGameOverlay(float partialTicks) {
      ScaledResolution scaledresolution = new ScaledResolution(this.mc);
      int i = scaledresolution.getScaledWidth();
      int j = scaledresolution.getScaledHeight();
      FontRenderer fontrenderer = this.getFontRenderer();
      GlStateManager.enableBlend();
      if (Config.isVignetteEnabled()) {
         this.renderVignette(this.mc.player.getBrightness(), scaledresolution);
      } else {
         GlStateManager.enableDepth();
         GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
         );
      }

      ItemStack itemstack = this.mc.player.inventory.armorItemInSlot(3);
      if (this.mc.gameSettings.thirdPersonView == 0 && itemstack.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN)) {
         this.renderPumpkinOverlay(scaledresolution);
      }

      if (!this.mc.player.isPotionActive(MobEffects.NAUSEA)) {
         float f = this.mc.player.prevTimeInPortal + (this.mc.player.timeInPortal - this.mc.player.prevTimeInPortal) * partialTicks;
         if (f > 0.0F) {
            this.renderPortal(f, scaledresolution);
         }
      }

      if (this.mc.playerController.isSpectator()) {
         this.spectatorGui.renderTooltip(scaledresolution, partialTicks);
      } else {
         this.renderHotbar(scaledresolution, partialTicks);
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(ICONS);
      GlStateManager.enableBlend();
      this.renderAttackIndicator(partialTicks, scaledresolution);
      GlStateManager.enableAlpha();
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      this.mc.profiler.startSection("bossHealth");
      this.overlayBoss.renderBossHealth();
      this.mc.profiler.endSection();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(ICONS);
      if (this.mc.playerController.shouldDrawHUD()) {
         this.renderPlayerStats(scaledresolution);
      }

      this.renderMountHealth(scaledresolution);
      GlStateManager.disableBlend();
      if (this.mc.player.getSleepTimer() > 0) {
         this.mc.profiler.startSection("sleep");
         GlStateManager.disableDepth();
         GlStateManager.disableAlpha();
         int j1 = this.mc.player.getSleepTimer();
         float f1 = j1 / 100.0F;
         if (f1 > 1.0F) {
            f1 = 1.0F - (j1 - 100) / 10.0F;
         }

         int k = (int)(220.0F * f1) << 24 | 1052704;
         drawRect(0, 0, i, j, k);
         GlStateManager.enableAlpha();
         GlStateManager.enableDepth();
         this.mc.profiler.endSection();
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      int k1 = i / 2 - 91;
      if (this.mc.player.isRidingHorse()) {
         this.renderHorseJumpBar(scaledresolution, k1);
      } else if (this.mc.playerController.gameIsSurvivalOrAdventure()) {
         this.renderExpBar(scaledresolution, k1);
      }

      if (this.mc.gameSettings.heldItemTooltips && !this.mc.playerController.isSpectator()) {
         this.renderSelectedItem(scaledresolution);
      } else if (this.mc.player.isSpectator()) {
         this.spectatorGui.renderSelectedItem(scaledresolution);
      }

      if (this.mc.isDemo()) {
         this.renderDemo(scaledresolution);
      }

      this.renderPotionEffects(scaledresolution);
      if (this.mc.gameSettings.showDebugInfo) {
         this.overlayDebug.renderDebugInfo(scaledresolution);
      }

      if (this.overlayMessageTime > 0) {
         this.mc.profiler.startSection("overlayMessage");
         float f2 = this.overlayMessageTime - partialTicks;
         int l1 = (int)(f2 * 255.0F / 20.0F);
         if (l1 > 255) {
            l1 = 255;
         }

         if (l1 > 8) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(i / 2), (float)(j - 68), 0.0F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(
               GlStateManager.SourceFactor.SRC_ALPHA,
               GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
               GlStateManager.SourceFactor.ONE,
               GlStateManager.DestFactor.ZERO
            );
            int l = 16777215;
            if (this.animateOverlayMessageColor) {
               l = MathHelper.hsvToRGB(f2 / 50.0F, 0.7F, 0.6F) & 16777215;
            }

            fontrenderer.drawString(this.overlayMessage, -fontrenderer.getStringWidth(this.overlayMessage) / 2, -4, l + (l1 << 24 & 0xFF000000));
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
         }

         this.mc.profiler.endSection();
      }

      this.overlaySubtitle.renderSubtitles(scaledresolution);
      if (this.titlesTimer > 0) {
         this.mc.profiler.startSection("titleAndSubtitle");
         float f3 = this.titlesTimer - partialTicks;
         int i2 = 255;
         if (this.titlesTimer > this.titleFadeOut + this.titleDisplayTime) {
            float f4 = this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut - f3;
            i2 = (int)(f4 * 255.0F / this.titleFadeIn);
         }

         if (this.titlesTimer <= this.titleFadeOut) {
            i2 = (int)(f3 * 255.0F / this.titleFadeOut);
         }

         i2 = MathHelper.clamp(i2, 0, 255);
         if (i2 > 8) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(i / 2), (float)(j / 2), 0.0F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(
               GlStateManager.SourceFactor.SRC_ALPHA,
               GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
               GlStateManager.SourceFactor.ONE,
               GlStateManager.DestFactor.ZERO
            );
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 4.0F);
            int j2 = i2 << 24 & 0xFF000000;
            fontrenderer.drawString(this.displayedTitle, -fontrenderer.getStringWidth(this.displayedTitle) / 2, -10.0F, 16777215 | j2, true);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(2.0F, 2.0F, 2.0F);
            fontrenderer.drawString(this.displayedSubTitle, -fontrenderer.getStringWidth(this.displayedSubTitle) / 2, 5.0F, 16777215 | j2, true);
            GlStateManager.popMatrix();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
         }

         this.mc.profiler.endSection();
      }

      Scoreboard scoreboard = this.mc.world.getScoreboard();
      ScoreObjective scoreobjective = null;
      ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(this.mc.player.getName());
      if (scoreplayerteam != null) {
         int i1 = scoreplayerteam.getColor().getColorIndex();
         if (i1 >= 0) {
            scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
         }
      }

      ScoreObjective scoreobjective1 = scoreobjective != null ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);
      if (scoreobjective1 != null) {
         this.renderScoreboard(scoreobjective1, scaledresolution);
      }

      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      GlStateManager.disableAlpha();
      GlStateManager.pushMatrix();
      GlStateManager.translate(0.0F, (float)(j - 48), 0.0F);
      this.mc.profiler.startSection("chat");
      this.persistantChatGUI.drawChat(this.updateCounter);
      this.mc.profiler.endSection();
      GlStateManager.popMatrix();
      scoreobjective1 = scoreboard.getObjectiveInDisplaySlot(0);
      if (this.mc.gameSettings.keyBindPlayerList.isKeyDown()
         && (!this.mc.isIntegratedServerRunning() || this.mc.player.connection.getPlayerInfoMap().size() > 1 || scoreobjective1 != null)) {
         this.overlayPlayerList.updatePlayerList(true);
         this.overlayPlayerList.renderPlayerlist(i, scoreboard, scoreobjective1);
      } else {
         this.overlayPlayerList.updatePlayerList(false);
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableLighting();
      GlStateManager.enableAlpha();
   }

   private void renderAttackIndicator(float p_184045_1_, ScaledResolution p_184045_2_) {
      GameSettings gamesettings = this.mc.gameSettings;
      if (gamesettings.thirdPersonView == 0) {
         if (this.mc.playerController.isSpectator() && this.mc.pointedEntity == null) {
            RayTraceResult raytraceresult = this.mc.objectMouseOver;
            if (raytraceresult == null || raytraceresult.typeOfHit != Type.BLOCK) {
               return;
            }

            BlockPos blockpos = raytraceresult.getBlockPos();
            IBlockState state = this.mc.world.getBlockState(blockpos);
            if (!ReflectorForge.blockHasTileEntity(state) || !(this.mc.world.getTileEntity(blockpos) instanceof IInventory)) {
               return;
            }
         }

         int l = p_184045_2_.getScaledWidth();
         int i1 = p_184045_2_.getScaledHeight();
         if (gamesettings.showDebugInfo && !gamesettings.hideGUI && !this.mc.player.hasReducedDebug() && !gamesettings.reducedDebugInfo) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(l / 2), (float)(i1 / 2), this.zLevel);
            Entity entity = this.mc.getRenderViewEntity();
            GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * p_184045_1_, -1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * p_184045_1_, 0.0F, 1.0F, 0.0F);
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
            this.drawTexturedModalRect(l / 2 - 7, i1 / 2 - 7, 0, 0, 16, 16);
            if (this.mc.gameSettings.attackIndicator == 1) {
               float f = this.mc.player.getCooledAttackStrength(0.0F);
               boolean flag = false;
               if (this.mc.pointedEntity != null && this.mc.pointedEntity instanceof EntityLivingBase && f >= 1.0F) {
                  flag = this.mc.player.getCooldownPeriod() > 5.0F;
                  flag &= ((EntityLivingBase)this.mc.pointedEntity).isEntityAlive();
               }

               int i = i1 / 2 - 7 + 16;
               int j = l / 2 - 8;
               if (flag) {
                  this.drawTexturedModalRect(j, i, 68, 94, 16, 16);
               } else if (f < 1.0F) {
                  int k = (int)(f * 17.0F);
                  this.drawTexturedModalRect(j, i, 36, 94, 16, 4);
                  this.drawTexturedModalRect(j, i, 52, 94, k, 4);
               }
            }
         }
      }
   }

   protected void renderPotionEffects(ScaledResolution resolution) {
      Collection<PotionEffect> collection = this.mc.player.getActivePotionEffects();
      if (!collection.isEmpty()) {
         this.mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
         GlStateManager.enableBlend();
         int i = 0;
         int j = 0;

         for (PotionEffect potioneffect : Ordering.natural().reverse().sortedCopy(collection)) {
            Potion potion = potioneffect.getPotion();
            boolean potionHasIcon = potion.hasStatusIcon();
            if (Reflector.ForgePotion_shouldRenderHUD.exists()) {
               if (!Reflector.callBoolean(potion, Reflector.ForgePotion_shouldRenderHUD, new Object[]{potioneffect})) {
                  continue;
               }

               this.mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
               potionHasIcon = true;
            }

            if (potionHasIcon && potioneffect.doesShowParticles()) {
               int k = resolution.getScaledWidth();
               int l = 1;
               if (this.mc.isDemo()) {
                  l += 15;
               }

               int i1 = potion.getStatusIconIndex();
               if (potion.isBeneficial()) {
                  i++;
                  k -= 25 * i;
               } else {
                  j++;
                  k -= 25 * j;
                  l += 26;
               }

               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               float f = 1.0F;
               if (potioneffect.getIsAmbient()) {
                  this.drawTexturedModalRect(k, l, 165, 166, 24, 24);
               } else {
                  this.drawTexturedModalRect(k, l, 141, 166, 24, 24);
                  if (potioneffect.getDuration() <= 200) {
                     int j1 = 10 - potioneffect.getDuration() / 20;
                     f = MathHelper.clamp(potioneffect.getDuration() / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F)
                        + MathHelper.cos(potioneffect.getDuration() * (float) Math.PI / 5.0F) * MathHelper.clamp(j1 / 10.0F * 0.25F, 0.0F, 0.25F);
                  }
               }

               GlStateManager.color(1.0F, 1.0F, 1.0F, f);
               if (Reflector.ForgePotion_renderHUDEffect.exists()) {
                  if (potion.hasStatusIcon()) {
                     this.drawTexturedModalRect(k + 3, l + 3, i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
                  }

                  Reflector.call(potion, Reflector.ForgePotion_renderHUDEffect, new Object[]{potioneffect, this, k, l, this.zLevel, f});
               } else {
                  this.drawTexturedModalRect(k + 3, l + 3, i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
               }
            }
         }
      }
   }

   protected void renderHotbar(ScaledResolution sr, float partialTicks) {
      if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.mc.getTextureManager().bindTexture(WIDGETS_TEX_PATH);
         EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
         ItemStack itemstack = entityplayer.getHeldItemOffhand();
         EnumHandSide enumhandside = entityplayer.getPrimaryHand().opposite();
         int i = sr.getScaledWidth() / 2;
         float f = this.zLevel;
         int j = 182;
         int k = 91;
         this.zLevel = -90.0F;
         this.drawTexturedModalRect(i - 91, sr.getScaledHeight() - 22, 0, 0, 182, 22);
         this.drawTexturedModalRect(i - 91 - 1 + entityplayer.inventory.currentItem * 20, sr.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
         if (!itemstack.isEmpty()) {
            if (enumhandside == EnumHandSide.LEFT) {
               this.drawTexturedModalRect(i - 91 - 29, sr.getScaledHeight() - 23, 24, 22, 29, 24);
            } else {
               this.drawTexturedModalRect(i + 91, sr.getScaledHeight() - 23, 53, 22, 29, 24);
            }
         }

         this.zLevel = f;
         GlStateManager.enableRescaleNormal();
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
         );
         RenderHelper.enableGUIStandardItemLighting();
         CustomItems.setRenderOffHand(false);

         for (int l = 0; l < 9; l++) {
            int i1 = i - 90 + l * 20 + 2;
            int j1 = sr.getScaledHeight() - 16 - 3;
            this.renderHotbarItem(i1, j1, partialTicks, entityplayer, (ItemStack)entityplayer.inventory.mainInventory.get(l));
         }

         if (!itemstack.isEmpty()) {
            CustomItems.setRenderOffHand(true);
            int l1 = sr.getScaledHeight() - 16 - 3;
            if (enumhandside == EnumHandSide.LEFT) {
               this.renderHotbarItem(i - 91 - 26, l1, partialTicks, entityplayer, itemstack);
            } else {
               this.renderHotbarItem(i + 91 + 10, l1, partialTicks, entityplayer, itemstack);
            }

            CustomItems.setRenderOffHand(false);
         }

         if (this.mc.gameSettings.attackIndicator == 2) {
            float f1 = this.mc.player.getCooledAttackStrength(0.0F);
            if (f1 < 1.0F) {
               int i2 = sr.getScaledHeight() - 20;
               int j2 = i + 91 + 6;
               if (enumhandside == EnumHandSide.RIGHT) {
                  j2 = i - 91 - 22;
               }

               this.mc.getTextureManager().bindTexture(Gui.ICONS);
               int k1 = (int)(f1 * 19.0F);
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               this.drawTexturedModalRect(j2, i2, 0, 94, 18, 18);
               this.drawTexturedModalRect(j2, i2 + 18 - k1, 18, 112 - k1, 18, k1);
            }
         }

         RenderHelper.disableStandardItemLighting();
         GlStateManager.disableRescaleNormal();
         GlStateManager.disableBlend();
      }
   }

   public void renderHorseJumpBar(ScaledResolution scaledRes, int x) {
      this.mc.profiler.startSection("jumpBar");
      this.mc.getTextureManager().bindTexture(Gui.ICONS);
      float f = this.mc.player.getHorseJumpPower();
      int i = 182;
      int j = (int)(f * 183.0F);
      int k = scaledRes.getScaledHeight() - 32 + 3;
      this.drawTexturedModalRect(x, k, 0, 84, 182, 5);
      if (j > 0) {
         this.drawTexturedModalRect(x, k, 0, 89, j, 5);
      }

      this.mc.profiler.endSection();
   }

   public void renderExpBar(ScaledResolution scaledRes, int x) {
      this.mc.profiler.startSection("expBar");
      this.mc.getTextureManager().bindTexture(Gui.ICONS);
      int i = this.mc.player.xpBarCap();
      if (i > 0) {
         int j = 182;
         int k = (int)(this.mc.player.experience * 183.0F);
         int l = scaledRes.getScaledHeight() - 32 + 3;
         this.drawTexturedModalRect(x, l, 0, 64, 182, 5);
         if (k > 0) {
            this.drawTexturedModalRect(x, l, 0, 69, k, 5);
         }
      }

      this.mc.profiler.endSection();
      if (this.mc.player.experienceLevel > 0) {
         this.mc.profiler.startSection("expLevel");
         int col = 8453920;
         if (Config.isCustomColors()) {
            col = CustomColors.getExpBarTextColor(col);
         }

         String s = "" + this.mc.player.experienceLevel;
         int i1 = (scaledRes.getScaledWidth() - this.getFontRenderer().getStringWidth(s)) / 2;
         int j1 = scaledRes.getScaledHeight() - 31 - 4;
         this.getFontRenderer().drawString(s, i1 + 1, j1, 0);
         this.getFontRenderer().drawString(s, i1 - 1, j1, 0);
         this.getFontRenderer().drawString(s, i1, j1 + 1, 0);
         this.getFontRenderer().drawString(s, i1, j1 - 1, 0);
         this.getFontRenderer().drawString(s, i1, j1, col);
         this.mc.profiler.endSection();
      }
   }

   public void renderSelectedItem(ScaledResolution scaledRes) {
      this.mc.profiler.startSection("selectedItemName");
      if (this.remainingHighlightTicks > 0 && !this.highlightingItemStack.isEmpty()) {
         String s = this.highlightingItemStack.getDisplayName();
         if (this.highlightingItemStack.hasDisplayName()) {
            s = TextFormatting.ITALIC + s;
         }

         int i = (scaledRes.getScaledWidth() - this.getFontRenderer().getStringWidth(s)) / 2;
         int j = scaledRes.getScaledHeight() - 59;
         if (!this.mc.playerController.shouldDrawHUD()) {
            j += 14;
         }

         int k = (int)(this.remainingHighlightTicks * 256.0F / 10.0F);
         if (k > 255) {
            k = 255;
         }

         if (k > 0) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(
               GlStateManager.SourceFactor.SRC_ALPHA,
               GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
               GlStateManager.SourceFactor.ONE,
               GlStateManager.DestFactor.ZERO
            );
            this.getFontRenderer().drawStringWithShadow(s, i, j, 16777215 + (k << 24));
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
         }
      }

      this.mc.profiler.endSection();
   }

   public void renderDemo(ScaledResolution scaledRes) {
      this.mc.profiler.startSection("demo");
      String s;
      if (this.mc.world.getTotalWorldTime() >= 120500L) {
         s = I18n.format("demo.demoExpired");
      } else {
         s = I18n.format("demo.remainingTime", StringUtils.ticksToElapsedTime((int)(120500L - this.mc.world.getTotalWorldTime())));
      }

      int i = this.getFontRenderer().getStringWidth(s);
      this.getFontRenderer().drawStringWithShadow(s, scaledRes.getScaledWidth() - i - 10, 5.0F, 16777215);
      this.mc.profiler.endSection();
   }

   private void renderScoreboard(ScoreObjective objective, ScaledResolution scaledRes) {
      Scoreboard scoreboard = objective.getScoreboard();
      Collection<Score> collection = scoreboard.getSortedScores(objective);
      List<Score> list = Lists.newArrayList(Iterables.filter(collection, new Predicate<Score>() {
         public boolean apply(@Nullable Score p_apply_1_) {
            return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
         }
      }));
      if (list.size() > 15) {
         collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
      } else {
         collection = list;
      }

      int i = this.getFontRenderer().getStringWidth(objective.getDisplayName());

      for (Score score : collection) {
         ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
         String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + TextFormatting.RED + score.getScorePoints();
         i = Math.max(i, this.getFontRenderer().getStringWidth(s));
      }

      int i1 = collection.size() * this.getFontRenderer().FONT_HEIGHT;
      int j1 = scaledRes.getScaledHeight() / 2 + i1 / 3;
      int k1 = 3;
      int l1 = scaledRes.getScaledWidth() - i - 3;
      int j = 0;

      for (Score score1 : collection) {
         j++;
         ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
         String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
         String s2 = TextFormatting.RED + "" + score1.getScorePoints();
         int k = j1 - j * this.getFontRenderer().FONT_HEIGHT;
         int l = scaledRes.getScaledWidth() - 3 + 2;
         drawRect(l1 - 2, k, l, k + this.getFontRenderer().FONT_HEIGHT, 1342177280);
         this.getFontRenderer().drawString(s1, l1, k, 553648127);
         this.getFontRenderer().drawString(s2, l - this.getFontRenderer().getStringWidth(s2), k, 553648127);
         if (j == collection.size()) {
            String s3 = objective.getDisplayName();
            drawRect(l1 - 2, k - this.getFontRenderer().FONT_HEIGHT - 1, l, k - 1, 1610612736);
            drawRect(l1 - 2, k - 1, l, k, 1342177280);
            this.getFontRenderer()
               .drawString(s3, l1 + i / 2 - this.getFontRenderer().getStringWidth(s3) / 2, k - this.getFontRenderer().FONT_HEIGHT, 553648127);
         }
      }
   }

   private void renderPlayerStats(ScaledResolution scaledRes) {
      if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
         EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
         int i = MathHelper.ceil(entityplayer.getHealth());
         boolean flag = this.healthUpdateCounter > this.updateCounter && (this.healthUpdateCounter - this.updateCounter) / 3L % 2L == 1L;
         if (i < this.playerHealth && entityplayer.hurtResistantTime > 0) {
            this.lastSystemTime = Minecraft.getSystemTime();
            this.healthUpdateCounter = this.updateCounter + 20;
         } else if (i > this.playerHealth && entityplayer.hurtResistantTime > 0) {
            this.lastSystemTime = Minecraft.getSystemTime();
            this.healthUpdateCounter = this.updateCounter + 10;
         }

         if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L) {
            this.playerHealth = i;
            this.lastPlayerHealth = i;
            this.lastSystemTime = Minecraft.getSystemTime();
         }

         this.playerHealth = i;
         int j = this.lastPlayerHealth;
         this.rand.setSeed(this.updateCounter * 312871);
         FoodStats foodstats = entityplayer.getFoodStats();
         int k = foodstats.getFoodLevel();
         IAttributeInstance iattributeinstance = entityplayer.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
         int l = scaledRes.getScaledWidth() / 2 - 91;
         int i1 = scaledRes.getScaledWidth() / 2 + 91;
         int j1 = scaledRes.getScaledHeight() - 39;
         float f = (float)iattributeinstance.getAttributeValue();
         int k1 = MathHelper.ceil(entityplayer.getAbsorptionAmount());
         int l1 = MathHelper.ceil((f + k1) / 2.0F / 10.0F);
         int i2 = Math.max(10 - (l1 - 2), 3);
         int j2 = j1 - (l1 - 1) * i2 - 10;
         int k2 = j1 - 10;
         int l2 = k1;
         int i3 = entityplayer.getTotalArmorValue();
         int j3 = -1;
         if (entityplayer.isPotionActive(MobEffects.REGENERATION)) {
            j3 = this.updateCounter % MathHelper.ceil(f + 5.0F);
         }

         this.mc.profiler.startSection("armor");

         for (int k3 = 0; k3 < 10; k3++) {
            if (i3 > 0) {
               int l3 = l + k3 * 8;
               if (k3 * 2 + 1 < i3) {
                  this.drawTexturedModalRect(l3, j2, 34, 9, 9, 9);
               }

               if (k3 * 2 + 1 == i3) {
                  this.drawTexturedModalRect(l3, j2, 25, 9, 9, 9);
               }

               if (k3 * 2 + 1 > i3) {
                  this.drawTexturedModalRect(l3, j2, 16, 9, 9, 9);
               }
            }
         }

         this.mc.profiler.endStartSection("health");

         for (int j5 = MathHelper.ceil((f + k1) / 2.0F) - 1; j5 >= 0; j5--) {
            int k5 = 16;
            if (entityplayer.isPotionActive(MobEffects.POISON)) {
               k5 += 36;
            } else if (entityplayer.isPotionActive(MobEffects.WITHER)) {
               k5 += 72;
            }

            int i4 = 0;
            if (flag) {
               i4 = 1;
            }

            int j4 = MathHelper.ceil((j5 + 1) / 10.0F) - 1;
            int k4 = l + j5 % 10 * 8;
            int l4 = j1 - j4 * i2;
            if (i <= 4) {
               l4 += this.rand.nextInt(2);
            }

            if (l2 <= 0 && j5 == j3) {
               l4 -= 2;
            }

            int i5 = 0;
            if (entityplayer.world.getWorldInfo().isHardcoreModeEnabled()) {
               i5 = 5;
            }

            this.drawTexturedModalRect(k4, l4, 16 + i4 * 9, 9 * i5, 9, 9);
            if (flag) {
               if (j5 * 2 + 1 < j) {
                  this.drawTexturedModalRect(k4, l4, k5 + 54, 9 * i5, 9, 9);
               }

               if (j5 * 2 + 1 == j) {
                  this.drawTexturedModalRect(k4, l4, k5 + 63, 9 * i5, 9, 9);
               }
            }

            if (l2 > 0) {
               if (l2 == k1 && k1 % 2 == 1) {
                  this.drawTexturedModalRect(k4, l4, k5 + 153, 9 * i5, 9, 9);
                  l2--;
               } else {
                  this.drawTexturedModalRect(k4, l4, k5 + 144, 9 * i5, 9, 9);
                  l2 -= 2;
               }
            } else {
               if (j5 * 2 + 1 < i) {
                  this.drawTexturedModalRect(k4, l4, k5 + 36, 9 * i5, 9, 9);
               }

               if (j5 * 2 + 1 == i) {
                  this.drawTexturedModalRect(k4, l4, k5 + 45, 9 * i5, 9, 9);
               }
            }
         }

         Entity entity = entityplayer.getRidingEntity();
         if (entity == null || !(entity instanceof EntityLivingBase)) {
            this.mc.profiler.endStartSection("food");

            for (int l5 = 0; l5 < 10; l5++) {
               int j6 = j1;
               int l6 = 16;
               int j7 = 0;
               if (entityplayer.isPotionActive(MobEffects.HUNGER)) {
                  l6 += 36;
                  j7 = 13;
               }

               if (entityplayer.getFoodStats().getSaturationLevel() <= 0.0F && this.updateCounter % (k * 3 + 1) == 0) {
                  j6 = j1 + (this.rand.nextInt(3) - 1);
               }

               int l7 = i1 - l5 * 8 - 9;
               this.drawTexturedModalRect(l7, j6, 16 + j7 * 9, 27, 9, 9);
               if (l5 * 2 + 1 < k) {
                  this.drawTexturedModalRect(l7, j6, l6 + 36, 27, 9, 9);
               }

               if (l5 * 2 + 1 == k) {
                  this.drawTexturedModalRect(l7, j6, l6 + 45, 27, 9, 9);
               }
            }
         }

         this.mc.profiler.endStartSection("air");
         if (entityplayer.isInsideOfMaterial(Material.WATER)) {
            int i6 = this.mc.player.getAir();
            int k6 = MathHelper.ceil((i6 - 2) * 10.0 / 300.0);
            int i7 = MathHelper.ceil(i6 * 10.0 / 300.0) - k6;

            for (int k7 = 0; k7 < k6 + i7; k7++) {
               if (k7 < k6) {
                  this.drawTexturedModalRect(i1 - k7 * 8 - 9, k2, 16, 18, 9, 9);
               } else {
                  this.drawTexturedModalRect(i1 - k7 * 8 - 9, k2, 25, 18, 9, 9);
               }
            }
         }

         this.mc.profiler.endSection();
      }
   }

   private void renderMountHealth(ScaledResolution p_184047_1_) {
      if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
         EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
         Entity entity = entityplayer.getRidingEntity();
         if (entity instanceof EntityLivingBase) {
            this.mc.profiler.endStartSection("mountHealth");
            EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
            int i = (int)Math.ceil(entitylivingbase.getHealth());
            float f = entitylivingbase.getMaxHealth();
            int j = (int)(f + 0.5F) / 2;
            if (j > 30) {
               j = 30;
            }

            int k = p_184047_1_.getScaledHeight() - 39;
            int l = p_184047_1_.getScaledWidth() / 2 + 91;
            int i1 = k;
            int j1 = 0;

            for (boolean flag = false; j > 0; j1 += 20) {
               int k1 = Math.min(j, 10);
               j -= k1;

               for (int l1 = 0; l1 < k1; l1++) {
                  int i2 = 52;
                  int j2 = 0;
                  int k2 = l - l1 * 8 - 9;
                  this.drawTexturedModalRect(k2, i1, 52 + j2 * 9, 9, 9, 9);
                  if (l1 * 2 + 1 + j1 < i) {
                     this.drawTexturedModalRect(k2, i1, 88, 9, 9, 9);
                  }

                  if (l1 * 2 + 1 + j1 == i) {
                     this.drawTexturedModalRect(k2, i1, 97, 9, 9, 9);
                  }
               }

               i1 -= 10;
            }
         }
      }
   }

   private void renderPumpkinOverlay(ScaledResolution scaledRes) {
      GlStateManager.disableDepth();
      GlStateManager.depthMask(false);
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableAlpha();
      this.mc.getTextureManager().bindTexture(PUMPKIN_BLUR_TEX_PATH);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuffer();
      bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
      bufferbuilder.pos(0.0, scaledRes.getScaledHeight(), -90.0).tex(0.0, 1.0).endVertex();
      bufferbuilder.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0).tex(1.0, 1.0).endVertex();
      bufferbuilder.pos(scaledRes.getScaledWidth(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
      bufferbuilder.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
      tessellator.draw();
      GlStateManager.depthMask(true);
      GlStateManager.enableDepth();
      GlStateManager.enableAlpha();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private void renderVignette(float lightLevel, ScaledResolution scaledRes) {
      if (!Config.isVignetteEnabled()) {
         GlStateManager.enableDepth();
         GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
         );
      } else {
         lightLevel = 1.0F - lightLevel;
         lightLevel = MathHelper.clamp(lightLevel, 0.0F, 1.0F);
         WorldBorder worldborder = this.mc.world.getWorldBorder();
         float f = (float)worldborder.getClosestDistance(this.mc.player);
         double d0 = Math.min(
            worldborder.getResizeSpeed() * worldborder.getWarningTime() * 1000.0, Math.abs(worldborder.getTargetSize() - worldborder.getDiameter())
         );
         double d1 = Math.max((double)worldborder.getWarningDistance(), d0);
         if (f < d1) {
            f = 1.0F - (float)(f / d1);
         } else {
            f = 0.0F;
         }

         this.prevVignetteBrightness = (float)(this.prevVignetteBrightness + (lightLevel - this.prevVignetteBrightness) * 0.01);
         GlStateManager.disableDepth();
         GlStateManager.depthMask(false);
         GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
         );
         if (f > 0.0F) {
            GlStateManager.color(0.0F, f, f, 1.0F);
         } else {
            GlStateManager.color(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0F);
         }

         this.mc.getTextureManager().bindTexture(VIGNETTE_TEX_PATH);
         Tessellator tessellator = Tessellator.getInstance();
         BufferBuilder bufferbuilder = tessellator.getBuffer();
         bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
         bufferbuilder.pos(0.0, scaledRes.getScaledHeight(), -90.0).tex(0.0, 1.0).endVertex();
         bufferbuilder.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0).tex(1.0, 1.0).endVertex();
         bufferbuilder.pos(scaledRes.getScaledWidth(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
         bufferbuilder.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
         tessellator.draw();
         GlStateManager.depthMask(true);
         GlStateManager.enableDepth();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
         );
      }
   }

   private void renderPortal(float timeInPortal, ScaledResolution scaledRes) {
      if (timeInPortal < 1.0F) {
         timeInPortal *= timeInPortal;
         timeInPortal *= timeInPortal;
         timeInPortal = timeInPortal * 0.8F + 0.2F;
      }

      GlStateManager.disableAlpha();
      GlStateManager.disableDepth();
      GlStateManager.depthMask(false);
      GlStateManager.tryBlendFuncSeparate(
         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
      );
      GlStateManager.color(1.0F, 1.0F, 1.0F, timeInPortal);
      this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      TextureAtlasSprite textureatlassprite = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.PORTAL.t());
      float f = textureatlassprite.getMinU();
      float f1 = textureatlassprite.getMinV();
      float f2 = textureatlassprite.getMaxU();
      float f3 = textureatlassprite.getMaxV();
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuffer();
      bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
      bufferbuilder.pos(0.0, scaledRes.getScaledHeight(), -90.0).tex(f, f3).endVertex();
      bufferbuilder.pos(scaledRes.getScaledWidth(), scaledRes.getScaledHeight(), -90.0).tex(f2, f3).endVertex();
      bufferbuilder.pos(scaledRes.getScaledWidth(), 0.0, -90.0).tex(f2, f1).endVertex();
      bufferbuilder.pos(0.0, 0.0, -90.0).tex(f, f1).endVertex();
      tessellator.draw();
      GlStateManager.depthMask(true);
      GlStateManager.enableDepth();
      GlStateManager.enableAlpha();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
   }

   private void renderHotbarItem(int p_184044_1_, int p_184044_2_, float p_184044_3_, EntityPlayer player, ItemStack stack) {
      if (!stack.isEmpty()) {
         float f = stack.getAnimationsToGo() - p_184044_3_;
         if (f > 0.0F) {
            GlStateManager.pushMatrix();
            float f1 = 1.0F + f / 5.0F;
            GlStateManager.translate((float)(p_184044_1_ + 8), (float)(p_184044_2_ + 12), 0.0F);
            GlStateManager.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
            GlStateManager.translate((float)(-(p_184044_1_ + 8)), (float)(-(p_184044_2_ + 12)), 0.0F);
         }

         this.itemRenderer.renderItemAndEffectIntoGUI(player, stack, p_184044_1_, p_184044_2_);
         if (f > 0.0F) {
            GlStateManager.popMatrix();
         }

         this.itemRenderer.renderItemOverlays(this.mc.fontRenderer, stack, p_184044_1_, p_184044_2_);
      }
   }

   public void updateTick() {
      if (this.mc.world == null) {
         TextureAnimations.updateAnimations();
      }

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
         ItemStack itemstack = this.mc.player.inventory.getCurrentItem();
         if (itemstack.isEmpty()) {
            this.remainingHighlightTicks = 0;
         } else if (!this.highlightingItemStack.isEmpty()
            && itemstack.getItem() == this.highlightingItemStack.getItem()
            && ItemStack.areItemStackTagsEqual(itemstack, this.highlightingItemStack)
            && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == this.highlightingItemStack.getMetadata())) {
            if (this.remainingHighlightTicks > 0) {
               this.remainingHighlightTicks--;
            }
         } else {
            this.remainingHighlightTicks = 40;
         }

         this.highlightingItemStack = itemstack;
      }
   }

   public void setRecordPlayingMessage(String recordName) {
      this.setOverlayMessage(I18n.format("record.nowPlaying", recordName), true);
   }

   public void setOverlayMessage(String message, boolean animateColor) {
      this.overlayMessage = message;
      this.overlayMessageTime = 60;
      this.animateOverlayMessageColor = animateColor;
   }

   public void displayTitle(String title, String subTitle, int timeFadeIn, int displayTime, int timeFadeOut) {
      if (title == null && subTitle == null && timeFadeIn < 0 && displayTime < 0 && timeFadeOut < 0) {
         this.displayedTitle = "";
         this.displayedSubTitle = "";
         this.titlesTimer = 0;
      } else if (title != null) {
         this.displayedTitle = title;
         this.titlesTimer = this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut;
      } else if (subTitle != null) {
         this.displayedSubTitle = subTitle;
      } else {
         if (timeFadeIn >= 0) {
            this.titleFadeIn = timeFadeIn;
         }

         if (displayTime >= 0) {
            this.titleDisplayTime = displayTime;
         }

         if (timeFadeOut >= 0) {
            this.titleFadeOut = timeFadeOut;
         }

         if (this.titlesTimer > 0) {
            this.titlesTimer = this.titleFadeIn + this.titleDisplayTime + this.titleFadeOut;
         }
      }
   }

   public void setOverlayMessage(ITextComponent component, boolean animateColor) {
      this.setOverlayMessage(component.getUnformattedText(), animateColor);
   }

   public void addChatMessage(ChatType p_191742_1_, ITextComponent p_191742_2_) {
      for (IChatListener ichatlistener : this.chatListeners.get(p_191742_1_)) {
         ichatlistener.say(p_191742_1_, p_191742_2_);
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
