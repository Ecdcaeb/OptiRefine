package net.minecraft.client.network;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.block.Block;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.GuardianSound;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.GuiScreenDemo;
import net.minecraft.client.gui.GuiScreenRealmsProxy;
import net.minecraft.client.gui.GuiWinGame;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.toasts.RecipeToast;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.ParticleItemPickup;
import net.minecraft.client.player.inventory.ContainerLocalMenu;
import net.minecraft.client.player.inventory.LocalBlockIntercommunication;
import net.minecraft.client.renderer.debug.DebugRendererNeighborsUpdate;
import net.minecraft.client.renderer.debug.DebugRendererPathfinding;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.NpcMerchant;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketCamera;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketCooldown;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.network.play.server.SPacketEntityHeadLook;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketKeepAlive;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketPlaceGhostRecipe;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.network.play.server.SPacketSelectAdvancementsTab;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketSignEditorOpen;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketStatistics;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.network.play.server.SPacketUnloadChunk;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.network.play.server.SPacketWindowProperty;
import net.minecraft.network.play.server.SPacketWorldBorder;
import net.minecraft.pathfinding.Path;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.realms.DisconnectedRealmsScreen;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITabCompleter;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetHandlerPlayClient implements INetHandlerPlayClient {
   private static final Logger LOGGER = LogManager.getLogger();
   private final NetworkManager netManager;
   private final GameProfile profile;
   private final GuiScreen guiScreenServer;
   private Minecraft client;
   private WorldClient world;
   private boolean doneLoadingTerrain;
   private final Map<UUID, NetworkPlayerInfo> playerInfoMap = Maps.newHashMap();
   public int currentServerMaxPlayers = 20;
   private boolean hasStatistics;
   private final ClientAdvancementManager advancementManager;
   private final Random avRandomizer = new Random();

   public NetHandlerPlayClient(Minecraft var1, GuiScreen var2, NetworkManager var3, GameProfile var4) {
      this.client = ☃;
      this.guiScreenServer = ☃;
      this.netManager = ☃;
      this.profile = ☃;
      this.advancementManager = new ClientAdvancementManager(☃);
   }

   public void cleanup() {
      this.world = null;
   }

   @Override
   public void handleJoinGame(SPacketJoinGame var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      this.client.playerController = new PlayerControllerMP(this.client, this);
      this.world = new WorldClient(
         this, new WorldSettings(0L, ☃.getGameType(), false, ☃.isHardcoreMode(), ☃.getWorldType()), ☃.getDimension(), ☃.getDifficulty(), this.client.profiler
      );
      this.client.gameSettings.difficulty = ☃.getDifficulty();
      this.client.loadWorld(this.world);
      this.client.player.dimension = ☃.getDimension();
      this.client.displayGuiScreen(new GuiDownloadTerrain());
      this.client.player.setEntityId(☃.getPlayerId());
      this.currentServerMaxPlayers = ☃.getMaxPlayers();
      this.client.player.setReducedDebug(☃.isReducedDebugInfo());
      this.client.playerController.setGameType(☃.getGameType());
      this.client.gameSettings.sendSettingsToServer();
      this.netManager
         .sendPacket(new CPacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).writeString(ClientBrandRetriever.getClientModName())));
   }

   @Override
   public void handleSpawnObject(SPacketSpawnObject var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      double ☃ = ☃.getX();
      double ☃x = ☃.getY();
      double ☃xx = ☃.getZ();
      Entity ☃xxx = null;
      if (☃.getType() == 10) {
         ☃xxx = EntityMinecart.create(this.world, ☃, ☃x, ☃xx, EntityMinecart.Type.getById(☃.getData()));
      } else if (☃.getType() == 90) {
         Entity ☃xxxx = this.world.getEntityByID(☃.getData());
         if (☃xxxx instanceof EntityPlayer) {
            ☃xxx = new EntityFishHook(this.world, (EntityPlayer)☃xxxx, ☃, ☃x, ☃xx);
         }

         ☃.setData(0);
      } else if (☃.getType() == 60) {
         ☃xxx = new EntityTippedArrow(this.world, ☃, ☃x, ☃xx);
      } else if (☃.getType() == 91) {
         ☃xxx = new EntitySpectralArrow(this.world, ☃, ☃x, ☃xx);
      } else if (☃.getType() == 61) {
         ☃xxx = new EntitySnowball(this.world, ☃, ☃x, ☃xx);
      } else if (☃.getType() == 68) {
         ☃xxx = new EntityLlamaSpit(this.world, ☃, ☃x, ☃xx, ☃.getSpeedX() / 8000.0, ☃.getSpeedY() / 8000.0, ☃.getSpeedZ() / 8000.0);
      } else if (☃.getType() == 71) {
         ☃xxx = new EntityItemFrame(this.world, new BlockPos(☃, ☃x, ☃xx), EnumFacing.byHorizontalIndex(☃.getData()));
         ☃.setData(0);
      } else if (☃.getType() == 77) {
         ☃xxx = new EntityLeashKnot(this.world, new BlockPos(MathHelper.floor(☃), MathHelper.floor(☃x), MathHelper.floor(☃xx)));
         ☃.setData(0);
      } else if (☃.getType() == 65) {
         ☃xxx = new EntityEnderPearl(this.world, ☃, ☃x, ☃xx);
      } else if (☃.getType() == 72) {
         ☃xxx = new EntityEnderEye(this.world, ☃, ☃x, ☃xx);
      } else if (☃.getType() == 76) {
         ☃xxx = new EntityFireworkRocket(this.world, ☃, ☃x, ☃xx, ItemStack.EMPTY);
      } else if (☃.getType() == 63) {
         ☃xxx = new EntityLargeFireball(this.world, ☃, ☃x, ☃xx, ☃.getSpeedX() / 8000.0, ☃.getSpeedY() / 8000.0, ☃.getSpeedZ() / 8000.0);
         ☃.setData(0);
      } else if (☃.getType() == 93) {
         ☃xxx = new EntityDragonFireball(this.world, ☃, ☃x, ☃xx, ☃.getSpeedX() / 8000.0, ☃.getSpeedY() / 8000.0, ☃.getSpeedZ() / 8000.0);
         ☃.setData(0);
      } else if (☃.getType() == 64) {
         ☃xxx = new EntitySmallFireball(this.world, ☃, ☃x, ☃xx, ☃.getSpeedX() / 8000.0, ☃.getSpeedY() / 8000.0, ☃.getSpeedZ() / 8000.0);
         ☃.setData(0);
      } else if (☃.getType() == 66) {
         ☃xxx = new EntityWitherSkull(this.world, ☃, ☃x, ☃xx, ☃.getSpeedX() / 8000.0, ☃.getSpeedY() / 8000.0, ☃.getSpeedZ() / 8000.0);
         ☃.setData(0);
      } else if (☃.getType() == 67) {
         ☃xxx = new EntityShulkerBullet(this.world, ☃, ☃x, ☃xx, ☃.getSpeedX() / 8000.0, ☃.getSpeedY() / 8000.0, ☃.getSpeedZ() / 8000.0);
         ☃.setData(0);
      } else if (☃.getType() == 62) {
         ☃xxx = new EntityEgg(this.world, ☃, ☃x, ☃xx);
      } else if (☃.getType() == 79) {
         ☃xxx = new EntityEvokerFangs(this.world, ☃, ☃x, ☃xx, 0.0F, 0, null);
      } else if (☃.getType() == 73) {
         ☃xxx = new EntityPotion(this.world, ☃, ☃x, ☃xx, ItemStack.EMPTY);
         ☃.setData(0);
      } else if (☃.getType() == 75) {
         ☃xxx = new EntityExpBottle(this.world, ☃, ☃x, ☃xx);
         ☃.setData(0);
      } else if (☃.getType() == 1) {
         ☃xxx = new EntityBoat(this.world, ☃, ☃x, ☃xx);
      } else if (☃.getType() == 50) {
         ☃xxx = new EntityTNTPrimed(this.world, ☃, ☃x, ☃xx, null);
      } else if (☃.getType() == 78) {
         ☃xxx = new EntityArmorStand(this.world, ☃, ☃x, ☃xx);
      } else if (☃.getType() == 51) {
         ☃xxx = new EntityEnderCrystal(this.world, ☃, ☃x, ☃xx);
      } else if (☃.getType() == 2) {
         ☃xxx = new EntityItem(this.world, ☃, ☃x, ☃xx);
      } else if (☃.getType() == 70) {
         ☃xxx = new EntityFallingBlock(this.world, ☃, ☃x, ☃xx, Block.getStateById(☃.getData() & 65535));
         ☃.setData(0);
      } else if (☃.getType() == 3) {
         ☃xxx = new EntityAreaEffectCloud(this.world, ☃, ☃x, ☃xx);
      }

      if (☃xxx != null) {
         EntityTracker.updateServerPosition(☃xxx, ☃, ☃x, ☃xx);
         ☃xxx.rotationPitch = ☃.getPitch() * 360 / 256.0F;
         ☃xxx.rotationYaw = ☃.getYaw() * 360 / 256.0F;
         Entity[] ☃xxxx = ☃xxx.getParts();
         if (☃xxxx != null) {
            int ☃xxxxx = ☃.getEntityID() - ☃xxx.getEntityId();

            for (Entity ☃xxxxxx : ☃xxxx) {
               ☃xxxxxx.setEntityId(☃xxxxxx.getEntityId() + ☃xxxxx);
            }
         }

         ☃xxx.setEntityId(☃.getEntityID());
         ☃xxx.setUniqueId(☃.getUniqueId());
         this.world.addEntityToWorld(☃.getEntityID(), ☃xxx);
         if (☃.getData() > 0) {
            if (☃.getType() == 60 || ☃.getType() == 91) {
               Entity ☃xxxxx = this.world.getEntityByID(☃.getData() - 1);
               if (☃xxxxx instanceof EntityLivingBase && ☃xxx instanceof EntityArrow) {
                  ((EntityArrow)☃xxx).shootingEntity = ☃xxxxx;
               }
            }

            ☃xxx.setVelocity(☃.getSpeedX() / 8000.0, ☃.getSpeedY() / 8000.0, ☃.getSpeedZ() / 8000.0);
         }
      }
   }

   @Override
   public void handleSpawnExperienceOrb(SPacketSpawnExperienceOrb var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      double ☃ = ☃.getX();
      double ☃x = ☃.getY();
      double ☃xx = ☃.getZ();
      Entity ☃xxx = new EntityXPOrb(this.world, ☃, ☃x, ☃xx, ☃.getXPValue());
      EntityTracker.updateServerPosition(☃xxx, ☃, ☃x, ☃xx);
      ☃xxx.rotationYaw = 0.0F;
      ☃xxx.rotationPitch = 0.0F;
      ☃xxx.setEntityId(☃.getEntityID());
      this.world.addEntityToWorld(☃.getEntityID(), ☃xxx);
   }

   @Override
   public void handleSpawnGlobalEntity(SPacketSpawnGlobalEntity var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      double ☃ = ☃.getX();
      double ☃x = ☃.getY();
      double ☃xx = ☃.getZ();
      Entity ☃xxx = null;
      if (☃.getType() == 1) {
         ☃xxx = new EntityLightningBolt(this.world, ☃, ☃x, ☃xx, false);
      }

      if (☃xxx != null) {
         EntityTracker.updateServerPosition(☃xxx, ☃, ☃x, ☃xx);
         ☃xxx.rotationYaw = 0.0F;
         ☃xxx.rotationPitch = 0.0F;
         ☃xxx.setEntityId(☃.getEntityId());
         this.world.addWeatherEffect(☃xxx);
      }
   }

   @Override
   public void handleSpawnPainting(SPacketSpawnPainting var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      EntityPainting ☃ = new EntityPainting(this.world, ☃.getPosition(), ☃.getFacing(), ☃.getTitle());
      ☃.setUniqueId(☃.getUniqueId());
      this.world.addEntityToWorld(☃.getEntityID(), ☃);
   }

   @Override
   public void handleEntityVelocity(SPacketEntityVelocity var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      Entity ☃ = this.world.getEntityByID(☃.getEntityID());
      if (☃ != null) {
         ☃.setVelocity(☃.getMotionX() / 8000.0, ☃.getMotionY() / 8000.0, ☃.getMotionZ() / 8000.0);
      }
   }

   @Override
   public void handleEntityMetadata(SPacketEntityMetadata var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      Entity ☃ = this.world.getEntityByID(☃.getEntityId());
      if (☃ != null && ☃.getDataManagerEntries() != null) {
         ☃.getDataManager().setEntryValues(☃.getDataManagerEntries());
      }
   }

   @Override
   public void handleSpawnPlayer(SPacketSpawnPlayer var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      double ☃ = ☃.getX();
      double ☃x = ☃.getY();
      double ☃xx = ☃.getZ();
      float ☃xxx = ☃.getYaw() * 360 / 256.0F;
      float ☃xxxx = ☃.getPitch() * 360 / 256.0F;
      EntityOtherPlayerMP ☃xxxxx = new EntityOtherPlayerMP(this.client.world, this.getPlayerInfo(☃.getUniqueId()).getGameProfile());
      ☃xxxxx.prevPosX = ☃;
      ☃xxxxx.lastTickPosX = ☃;
      ☃xxxxx.prevPosY = ☃x;
      ☃xxxxx.lastTickPosY = ☃x;
      ☃xxxxx.prevPosZ = ☃xx;
      ☃xxxxx.lastTickPosZ = ☃xx;
      EntityTracker.updateServerPosition(☃xxxxx, ☃, ☃x, ☃xx);
      ☃xxxxx.setPositionAndRotation(☃, ☃x, ☃xx, ☃xxx, ☃xxxx);
      this.world.addEntityToWorld(☃.getEntityID(), ☃xxxxx);
      List<EntityDataManager.DataEntry<?>> ☃xxxxxx = ☃.getDataManagerEntries();
      if (☃xxxxxx != null) {
         ☃xxxxx.getDataManager().setEntryValues(☃xxxxxx);
      }
   }

   @Override
   public void handleEntityTeleport(SPacketEntityTeleport var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      Entity ☃ = this.world.getEntityByID(☃.getEntityId());
      if (☃ != null) {
         double ☃x = ☃.getX();
         double ☃xx = ☃.getY();
         double ☃xxx = ☃.getZ();
         EntityTracker.updateServerPosition(☃, ☃x, ☃xx, ☃xxx);
         if (!☃.canPassengerSteer()) {
            float ☃xxxx = ☃.getYaw() * 360 / 256.0F;
            float ☃xxxxx = ☃.getPitch() * 360 / 256.0F;
            if (!(Math.abs(☃.posX - ☃x) >= 0.03125) && !(Math.abs(☃.posY - ☃xx) >= 0.015625) && !(Math.abs(☃.posZ - ☃xxx) >= 0.03125)) {
               ☃.setPositionAndRotationDirect(☃.posX, ☃.posY, ☃.posZ, ☃xxxx, ☃xxxxx, 0, true);
            } else {
               ☃.setPositionAndRotationDirect(☃x, ☃xx, ☃xxx, ☃xxxx, ☃xxxxx, 3, true);
            }

            ☃.onGround = ☃.getOnGround();
         }
      }
   }

   @Override
   public void handleHeldItemChange(SPacketHeldItemChange var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      if (InventoryPlayer.isHotbar(☃.getHeldItemHotbarIndex())) {
         this.client.player.inventory.currentItem = ☃.getHeldItemHotbarIndex();
      }
   }

   @Override
   public void handleEntityMovement(SPacketEntity var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      Entity ☃ = ☃.getEntity(this.world);
      if (☃ != null) {
         ☃.serverPosX = ☃.serverPosX + ☃.getX();
         ☃.serverPosY = ☃.serverPosY + ☃.getY();
         ☃.serverPosZ = ☃.serverPosZ + ☃.getZ();
         double ☃x = ☃.serverPosX / 4096.0;
         double ☃xx = ☃.serverPosY / 4096.0;
         double ☃xxx = ☃.serverPosZ / 4096.0;
         if (!☃.canPassengerSteer()) {
            float ☃xxxx = ☃.isRotating() ? ☃.getYaw() * 360 / 256.0F : ☃.rotationYaw;
            float ☃xxxxx = ☃.isRotating() ? ☃.getPitch() * 360 / 256.0F : ☃.rotationPitch;
            ☃.setPositionAndRotationDirect(☃x, ☃xx, ☃xxx, ☃xxxx, ☃xxxxx, 3, false);
            ☃.onGround = ☃.getOnGround();
         }
      }
   }

   @Override
   public void handleEntityHeadLook(SPacketEntityHeadLook var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      Entity ☃ = ☃.getEntity(this.world);
      if (☃ != null) {
         float ☃x = ☃.getYaw() * 360 / 256.0F;
         ☃.setRotationYawHead(☃x);
      }
   }

   @Override
   public void handleDestroyEntities(SPacketDestroyEntities var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);

      for (int ☃ = 0; ☃ < ☃.getEntityIDs().length; ☃++) {
         this.world.removeEntityFromWorld(☃.getEntityIDs()[☃]);
      }
   }

   @Override
   public void handlePlayerPosLook(SPacketPlayerPosLook var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      EntityPlayer ☃ = this.client.player;
      double ☃x = ☃.getX();
      double ☃xx = ☃.getY();
      double ☃xxx = ☃.getZ();
      float ☃xxxx = ☃.getYaw();
      float ☃xxxxx = ☃.getPitch();
      if (☃.getFlags().contains(SPacketPlayerPosLook.EnumFlags.X)) {
         ☃x += ☃.posX;
      } else {
         ☃.motionX = 0.0;
      }

      if (☃.getFlags().contains(SPacketPlayerPosLook.EnumFlags.Y)) {
         ☃xx += ☃.posY;
      } else {
         ☃.motionY = 0.0;
      }

      if (☃.getFlags().contains(SPacketPlayerPosLook.EnumFlags.Z)) {
         ☃xxx += ☃.posZ;
      } else {
         ☃.motionZ = 0.0;
      }

      if (☃.getFlags().contains(SPacketPlayerPosLook.EnumFlags.X_ROT)) {
         ☃xxxxx += ☃.rotationPitch;
      }

      if (☃.getFlags().contains(SPacketPlayerPosLook.EnumFlags.Y_ROT)) {
         ☃xxxx += ☃.rotationYaw;
      }

      ☃.setPositionAndRotation(☃x, ☃xx, ☃xxx, ☃xxxx, ☃xxxxx);
      this.netManager.sendPacket(new CPacketConfirmTeleport(☃.getTeleportId()));
      this.netManager.sendPacket(new CPacketPlayer.PositionRotation(☃.posX, ☃.getEntityBoundingBox().minY, ☃.posZ, ☃.rotationYaw, ☃.rotationPitch, false));
      if (!this.doneLoadingTerrain) {
         this.client.player.prevPosX = this.client.player.posX;
         this.client.player.prevPosY = this.client.player.posY;
         this.client.player.prevPosZ = this.client.player.posZ;
         this.doneLoadingTerrain = true;
         this.client.displayGuiScreen(null);
      }
   }

   @Override
   public void handleMultiBlockChange(SPacketMultiBlockChange var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);

      for (SPacketMultiBlockChange.BlockUpdateData ☃ : ☃.getChangedBlocks()) {
         this.world.invalidateRegionAndSetBlock(☃.getPos(), ☃.getBlockState());
      }
   }

   @Override
   public void handleChunkData(SPacketChunkData var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      if (☃.isFullChunk()) {
         this.world.doPreChunk(☃.getChunkX(), ☃.getChunkZ(), true);
      }

      this.world.invalidateBlockReceiveRegion(☃.getChunkX() << 4, 0, ☃.getChunkZ() << 4, (☃.getChunkX() << 4) + 15, 256, (☃.getChunkZ() << 4) + 15);
      Chunk ☃ = this.world.getChunk(☃.getChunkX(), ☃.getChunkZ());
      ☃.read(☃.getReadBuffer(), ☃.getExtractedSize(), ☃.isFullChunk());
      this.world.markBlockRangeForRenderUpdate(☃.getChunkX() << 4, 0, ☃.getChunkZ() << 4, (☃.getChunkX() << 4) + 15, 256, (☃.getChunkZ() << 4) + 15);
      if (!☃.isFullChunk() || !(this.world.provider instanceof WorldProviderSurface)) {
         ☃.resetRelightChecks();
      }

      for (NBTTagCompound ☃x : ☃.getTileEntityTags()) {
         BlockPos ☃xx = new BlockPos(☃x.getInteger("x"), ☃x.getInteger("y"), ☃x.getInteger("z"));
         TileEntity ☃xxx = this.world.getTileEntity(☃xx);
         if (☃xxx != null) {
            ☃xxx.readFromNBT(☃x);
         }
      }
   }

   @Override
   public void processChunkUnload(SPacketUnloadChunk var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      this.world.doPreChunk(☃.getX(), ☃.getZ(), false);
   }

   @Override
   public void handleBlockChange(SPacketBlockChange var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      this.world.invalidateRegionAndSetBlock(☃.getBlockPosition(), ☃.getBlockState());
   }

   @Override
   public void handleDisconnect(SPacketDisconnect var1) {
      this.netManager.closeChannel(☃.getReason());
   }

   @Override
   public void onDisconnect(ITextComponent var1) {
      this.client.loadWorld(null);
      if (this.guiScreenServer != null) {
         if (this.guiScreenServer instanceof GuiScreenRealmsProxy) {
            this.client
               .displayGuiScreen(new DisconnectedRealmsScreen(((GuiScreenRealmsProxy)this.guiScreenServer).getProxy(), "disconnect.lost", ☃).getProxy());
         } else {
            this.client.displayGuiScreen(new GuiDisconnected(this.guiScreenServer, "disconnect.lost", ☃));
         }
      } else {
         this.client.displayGuiScreen(new GuiDisconnected(new GuiMultiplayer(new GuiMainMenu()), "disconnect.lost", ☃));
      }
   }

   public void sendPacket(Packet<?> var1) {
      this.netManager.sendPacket(☃);
   }

   @Override
   public void handleCollectItem(SPacketCollectItem var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      Entity ☃ = this.world.getEntityByID(☃.getCollectedItemEntityID());
      EntityLivingBase ☃x = (EntityLivingBase)this.world.getEntityByID(☃.getEntityID());
      if (☃x == null) {
         ☃x = this.client.player;
      }

      if (☃ != null) {
         if (☃ instanceof EntityXPOrb) {
            this.world
               .playSound(
                  ☃.posX,
                  ☃.posY,
                  ☃.posZ,
                  SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                  SoundCategory.PLAYERS,
                  0.1F,
                  (this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.35F + 0.9F,
                  false
               );
         } else {
            this.world
               .playSound(
                  ☃.posX,
                  ☃.posY,
                  ☃.posZ,
                  SoundEvents.ENTITY_ITEM_PICKUP,
                  SoundCategory.PLAYERS,
                  0.2F,
                  (this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 1.4F + 2.0F,
                  false
               );
         }

         if (☃ instanceof EntityItem) {
            ((EntityItem)☃).getItem().setCount(☃.getAmount());
         }

         this.client.effectRenderer.addEffect(new ParticleItemPickup(this.world, ☃, ☃x, 0.5F));
         this.world.removeEntityFromWorld(☃.getCollectedItemEntityID());
      }
   }

   @Override
   public void handleChat(SPacketChat var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      this.client.ingameGUI.addChatMessage(☃.getType(), ☃.getChatComponent());
   }

   @Override
   public void handleAnimation(SPacketAnimation var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      Entity ☃ = this.world.getEntityByID(☃.getEntityID());
      if (☃ != null) {
         if (☃.getAnimationType() == 0) {
            EntityLivingBase ☃x = (EntityLivingBase)☃;
            ☃x.swingArm(EnumHand.MAIN_HAND);
         } else if (☃.getAnimationType() == 3) {
            EntityLivingBase ☃x = (EntityLivingBase)☃;
            ☃x.swingArm(EnumHand.OFF_HAND);
         } else if (☃.getAnimationType() == 1) {
            ☃.performHurtAnimation();
         } else if (☃.getAnimationType() == 2) {
            EntityPlayer ☃x = (EntityPlayer)☃;
            ☃x.wakeUpPlayer(false, false, false);
         } else if (☃.getAnimationType() == 4) {
            this.client.effectRenderer.emitParticleAtEntity(☃, EnumParticleTypes.CRIT);
         } else if (☃.getAnimationType() == 5) {
            this.client.effectRenderer.emitParticleAtEntity(☃, EnumParticleTypes.CRIT_MAGIC);
         }
      }
   }

   @Override
   public void handleUseBed(SPacketUseBed var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      ☃.getPlayer(this.world).trySleep(☃.getBedPosition());
   }

   @Override
   public void handleSpawnMob(SPacketSpawnMob var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      double ☃ = ☃.getX();
      double ☃x = ☃.getY();
      double ☃xx = ☃.getZ();
      float ☃xxx = ☃.getYaw() * 360 / 256.0F;
      float ☃xxxx = ☃.getPitch() * 360 / 256.0F;
      EntityLivingBase ☃xxxxx = (EntityLivingBase)EntityList.createEntityByID(☃.getEntityType(), this.client.world);
      if (☃xxxxx != null) {
         EntityTracker.updateServerPosition(☃xxxxx, ☃, ☃x, ☃xx);
         ☃xxxxx.renderYawOffset = ☃.getHeadPitch() * 360 / 256.0F;
         ☃xxxxx.rotationYawHead = ☃.getHeadPitch() * 360 / 256.0F;
         Entity[] ☃xxxxxx = ☃xxxxx.getParts();
         if (☃xxxxxx != null) {
            int ☃xxxxxxx = ☃.getEntityID() - ☃xxxxx.getEntityId();

            for (Entity ☃xxxxxxxx : ☃xxxxxx) {
               ☃xxxxxxxx.setEntityId(☃xxxxxxxx.getEntityId() + ☃xxxxxxx);
            }
         }

         ☃xxxxx.setEntityId(☃.getEntityID());
         ☃xxxxx.setUniqueId(☃.getUniqueId());
         ☃xxxxx.setPositionAndRotation(☃, ☃x, ☃xx, ☃xxx, ☃xxxx);
         ☃xxxxx.motionX = ☃.getVelocityX() / 8000.0F;
         ☃xxxxx.motionY = ☃.getVelocityY() / 8000.0F;
         ☃xxxxx.motionZ = ☃.getVelocityZ() / 8000.0F;
         this.world.addEntityToWorld(☃.getEntityID(), ☃xxxxx);
         List<EntityDataManager.DataEntry<?>> ☃xxxxxxx = ☃.getDataManagerEntries();
         if (☃xxxxxxx != null) {
            ☃xxxxx.getDataManager().setEntryValues(☃xxxxxxx);
         }
      } else {
         LOGGER.warn("Skipping Entity with id {}", ☃.getEntityType());
      }
   }

   @Override
   public void handleTimeUpdate(SPacketTimeUpdate var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      this.client.world.setTotalWorldTime(☃.getTotalWorldTime());
      this.client.world.setWorldTime(☃.getWorldTime());
   }

   @Override
   public void handleSpawnPosition(SPacketSpawnPosition var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      this.client.player.setSpawnPoint(☃.getSpawnPos(), true);
      this.client.world.getWorldInfo().setSpawn(☃.getSpawnPos());
   }

   @Override
   public void handleSetPassengers(SPacketSetPassengers var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      Entity ☃ = this.world.getEntityByID(☃.getEntityId());
      if (☃ == null) {
         LOGGER.warn("Received passengers for unknown entity");
      } else {
         boolean ☃x = ☃.isRidingOrBeingRiddenBy(this.client.player);
         ☃.removePassengers();

         for (int ☃xx : ☃.getPassengerIds()) {
            Entity ☃xxx = this.world.getEntityByID(☃xx);
            if (☃xxx != null) {
               ☃xxx.startRiding(☃, true);
               if (☃xxx == this.client.player && !☃x) {
                  this.client
                     .ingameGUI
                     .setOverlayMessage(
                        I18n.format("mount.onboard", GameSettings.getKeyDisplayString(this.client.gameSettings.keyBindSneak.getKeyCode())), false
                     );
               }
            }
         }
      }
   }

   @Override
   public void handleEntityAttach(SPacketEntityAttach var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      Entity ☃ = this.world.getEntityByID(☃.getEntityId());
      Entity ☃x = this.world.getEntityByID(☃.getVehicleEntityId());
      if (☃ instanceof EntityLiving) {
         if (☃x != null) {
            ((EntityLiving)☃).setLeashHolder(☃x, false);
         } else {
            ((EntityLiving)☃).clearLeashed(false, false);
         }
      }
   }

   @Override
   public void handleEntityStatus(SPacketEntityStatus var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      Entity ☃ = ☃.getEntity(this.world);
      if (☃ != null) {
         if (☃.getOpCode() == 21) {
            this.client.getSoundHandler().playSound(new GuardianSound((EntityGuardian)☃));
         } else if (☃.getOpCode() == 35) {
            int ☃x = 40;
            this.client.effectRenderer.emitParticleAtEntity(☃, EnumParticleTypes.TOTEM, 30);
            this.world.playSound(☃.posX, ☃.posY, ☃.posZ, SoundEvents.ITEM_TOTEM_USE, ☃.getSoundCategory(), 1.0F, 1.0F, false);
            if (☃ == this.client.player) {
               this.client.entityRenderer.displayItemActivation(new ItemStack(Items.TOTEM_OF_UNDYING));
            }
         } else {
            ☃.handleStatusUpdate(☃.getOpCode());
         }
      }
   }

   @Override
   public void handleUpdateHealth(SPacketUpdateHealth var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      this.client.player.setPlayerSPHealth(☃.getHealth());
      this.client.player.getFoodStats().setFoodLevel(☃.getFoodLevel());
      this.client.player.getFoodStats().setFoodSaturationLevel(☃.getSaturationLevel());
   }

   @Override
   public void handleSetExperience(SPacketSetExperience var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      this.client.player.setXPStats(☃.getExperienceBar(), ☃.getTotalExperience(), ☃.getLevel());
   }

   @Override
   public void handleRespawn(SPacketRespawn var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      if (☃.getDimensionID() != this.client.player.dimension) {
         this.doneLoadingTerrain = false;
         Scoreboard ☃ = this.world.getScoreboard();
         this.world = new WorldClient(
            this,
            new WorldSettings(0L, ☃.getGameType(), false, this.client.world.getWorldInfo().isHardcoreModeEnabled(), ☃.getWorldType()),
            ☃.getDimensionID(),
            ☃.getDifficulty(),
            this.client.profiler
         );
         this.world.setWorldScoreboard(☃);
         this.client.loadWorld(this.world);
         this.client.player.dimension = ☃.getDimensionID();
         this.client.displayGuiScreen(new GuiDownloadTerrain());
      }

      this.client.setDimensionAndSpawnPlayer(☃.getDimensionID());
      this.client.playerController.setGameType(☃.getGameType());
   }

   @Override
   public void handleExplosion(SPacketExplosion var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      Explosion ☃ = new Explosion(this.client.world, null, ☃.getX(), ☃.getY(), ☃.getZ(), ☃.getStrength(), ☃.getAffectedBlockPositions());
      ☃.doExplosionB(true);
      this.client.player.motionX = this.client.player.motionX + ☃.getMotionX();
      this.client.player.motionY = this.client.player.motionY + ☃.getMotionY();
      this.client.player.motionZ = this.client.player.motionZ + ☃.getMotionZ();
   }

   @Override
   public void handleOpenWindow(SPacketOpenWindow var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      EntityPlayerSP ☃ = this.client.player;
      if ("minecraft:container".equals(☃.getGuiId())) {
         ☃.displayGUIChest(new InventoryBasic(☃.getWindowTitle(), ☃.getSlotCount()));
         ☃.openContainer.windowId = ☃.getWindowId();
      } else if ("minecraft:villager".equals(☃.getGuiId())) {
         ☃.displayVillagerTradeGui(new NpcMerchant(☃, ☃.getWindowTitle()));
         ☃.openContainer.windowId = ☃.getWindowId();
      } else if ("EntityHorse".equals(☃.getGuiId())) {
         Entity ☃x = this.world.getEntityByID(☃.getEntityId());
         if (☃x instanceof AbstractHorse) {
            ☃.openGuiHorseInventory((AbstractHorse)☃x, new ContainerHorseChest(☃.getWindowTitle(), ☃.getSlotCount()));
            ☃.openContainer.windowId = ☃.getWindowId();
         }
      } else if (!☃.hasSlots()) {
         ☃.displayGui(new LocalBlockIntercommunication(☃.getGuiId(), ☃.getWindowTitle()));
         ☃.openContainer.windowId = ☃.getWindowId();
      } else {
         IInventory ☃x = new ContainerLocalMenu(☃.getGuiId(), ☃.getWindowTitle(), ☃.getSlotCount());
         ☃.displayGUIChest(☃x);
         ☃.openContainer.windowId = ☃.getWindowId();
      }
   }

   @Override
   public void handleSetSlot(SPacketSetSlot var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      EntityPlayer ☃ = this.client.player;
      ItemStack ☃x = ☃.getStack();
      int ☃xx = ☃.getSlot();
      this.client.getTutorial().handleSetSlot(☃x);
      if (☃.getWindowId() == -1) {
         ☃.inventory.setItemStack(☃x);
      } else if (☃.getWindowId() == -2) {
         ☃.inventory.setInventorySlotContents(☃xx, ☃x);
      } else {
         boolean ☃xxx = false;
         if (this.client.currentScreen instanceof GuiContainerCreative) {
            GuiContainerCreative ☃xxxx = (GuiContainerCreative)this.client.currentScreen;
            ☃xxx = ☃xxxx.getSelectedTabIndex() != CreativeTabs.INVENTORY.getIndex();
         }

         if (☃.getWindowId() == 0 && ☃.getSlot() >= 36 && ☃xx < 45) {
            if (!☃x.isEmpty()) {
               ItemStack ☃xxxx = ☃.inventoryContainer.getSlot(☃xx).getStack();
               if (☃xxxx.isEmpty() || ☃xxxx.getCount() < ☃x.getCount()) {
                  ☃x.setAnimationsToGo(5);
               }
            }

            ☃.inventoryContainer.putStackInSlot(☃xx, ☃x);
         } else if (☃.getWindowId() == ☃.openContainer.windowId && (☃.getWindowId() != 0 || !☃xxx)) {
            ☃.openContainer.putStackInSlot(☃xx, ☃x);
         }
      }
   }

   @Override
   public void handleConfirmTransaction(SPacketConfirmTransaction var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      Container ☃ = null;
      EntityPlayer ☃x = this.client.player;
      if (☃.getWindowId() == 0) {
         ☃ = ☃x.inventoryContainer;
      } else if (☃.getWindowId() == ☃x.openContainer.windowId) {
         ☃ = ☃x.openContainer;
      }

      if (☃ != null && !☃.wasAccepted()) {
         this.sendPacket(new CPacketConfirmTransaction(☃.getWindowId(), ☃.getActionNumber(), true));
      }
   }

   @Override
   public void handleWindowItems(SPacketWindowItems var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      EntityPlayer ☃ = this.client.player;
      if (☃.getWindowId() == 0) {
         ☃.inventoryContainer.setAll(☃.getItemStacks());
      } else if (☃.getWindowId() == ☃.openContainer.windowId) {
         ☃.openContainer.setAll(☃.getItemStacks());
      }
   }

   @Override
   public void handleSignEditorOpen(SPacketSignEditorOpen var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      TileEntity ☃ = this.world.getTileEntity(☃.getSignPosition());
      if (!(☃ instanceof TileEntitySign)) {
         ☃ = new TileEntitySign();
         ☃.setWorld(this.world);
         ☃.setPos(☃.getSignPosition());
      }

      this.client.player.openEditSign((TileEntitySign)☃);
   }

   @Override
   public void handleUpdateTileEntity(SPacketUpdateTileEntity var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      if (this.client.world.isBlockLoaded(☃.getPos())) {
         TileEntity ☃ = this.client.world.getTileEntity(☃.getPos());
         int ☃x = ☃.getTileEntityType();
         boolean ☃xx = ☃x == 2 && ☃ instanceof TileEntityCommandBlock;
         if (☃x == 1 && ☃ instanceof TileEntityMobSpawner
            || ☃xx
            || ☃x == 3 && ☃ instanceof TileEntityBeacon
            || ☃x == 4 && ☃ instanceof TileEntitySkull
            || ☃x == 5 && ☃ instanceof TileEntityFlowerPot
            || ☃x == 6 && ☃ instanceof TileEntityBanner
            || ☃x == 7 && ☃ instanceof TileEntityStructure
            || ☃x == 8 && ☃ instanceof TileEntityEndGateway
            || ☃x == 9 && ☃ instanceof TileEntitySign
            || ☃x == 10 && ☃ instanceof TileEntityShulkerBox
            || ☃x == 11 && ☃ instanceof TileEntityBed) {
            ☃.readFromNBT(☃.getNbtCompound());
         }

         if (☃xx && this.client.currentScreen instanceof GuiCommandBlock) {
            ((GuiCommandBlock)this.client.currentScreen).updateGui();
         }
      }
   }

   @Override
   public void handleWindowProperty(SPacketWindowProperty var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      EntityPlayer ☃ = this.client.player;
      if (☃.openContainer != null && ☃.openContainer.windowId == ☃.getWindowId()) {
         ☃.openContainer.updateProgressBar(☃.getProperty(), ☃.getValue());
      }
   }

   @Override
   public void handleEntityEquipment(SPacketEntityEquipment var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      Entity ☃ = this.world.getEntityByID(☃.getEntityID());
      if (☃ != null) {
         ☃.setItemStackToSlot(☃.getEquipmentSlot(), ☃.getItemStack());
      }
   }

   @Override
   public void handleCloseWindow(SPacketCloseWindow var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      this.client.player.closeScreenAndDropStack();
   }

   @Override
   public void handleBlockAction(SPacketBlockAction var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      this.client.world.addBlockEvent(☃.getBlockPosition(), ☃.getBlockType(), ☃.getData1(), ☃.getData2());
   }

   @Override
   public void handleBlockBreakAnim(SPacketBlockBreakAnim var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      this.client.world.sendBlockBreakProgress(☃.getBreakerId(), ☃.getPosition(), ☃.getProgress());
   }

   @Override
   public void handleChangeGameState(SPacketChangeGameState var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      EntityPlayer ☃ = this.client.player;
      int ☃x = ☃.getGameState();
      float ☃xx = ☃.getValue();
      int ☃xxx = MathHelper.floor(☃xx + 0.5F);
      if (☃x >= 0 && ☃x < SPacketChangeGameState.MESSAGE_NAMES.length && SPacketChangeGameState.MESSAGE_NAMES[☃x] != null) {
         ☃.sendStatusMessage(new TextComponentTranslation(SPacketChangeGameState.MESSAGE_NAMES[☃x]), false);
      }

      if (☃x == 1) {
         this.world.getWorldInfo().setRaining(true);
         this.world.setRainStrength(0.0F);
      } else if (☃x == 2) {
         this.world.getWorldInfo().setRaining(false);
         this.world.setRainStrength(1.0F);
      } else if (☃x == 3) {
         this.client.playerController.setGameType(GameType.getByID(☃xxx));
      } else if (☃x == 4) {
         if (☃xxx == 0) {
            this.client.player.connection.sendPacket(new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN));
            this.client.displayGuiScreen(new GuiDownloadTerrain());
         } else if (☃xxx == 1) {
            this.client
               .displayGuiScreen(
                  new GuiWinGame(true, () -> this.client.player.connection.sendPacket(new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN)))
               );
         }
      } else if (☃x == 5) {
         GameSettings ☃xxxx = this.client.gameSettings;
         if (☃xx == 0.0F) {
            this.client.displayGuiScreen(new GuiScreenDemo());
         } else if (☃xx == 101.0F) {
            this.client
               .ingameGUI
               .getChatGUI()
               .printChatMessage(
                  new TextComponentTranslation(
                     "demo.help.movement",
                     GameSettings.getKeyDisplayString(☃xxxx.keyBindForward.getKeyCode()),
                     GameSettings.getKeyDisplayString(☃xxxx.keyBindLeft.getKeyCode()),
                     GameSettings.getKeyDisplayString(☃xxxx.keyBindBack.getKeyCode()),
                     GameSettings.getKeyDisplayString(☃xxxx.keyBindRight.getKeyCode())
                  )
               );
         } else if (☃xx == 102.0F) {
            this.client
               .ingameGUI
               .getChatGUI()
               .printChatMessage(new TextComponentTranslation("demo.help.jump", GameSettings.getKeyDisplayString(☃xxxx.keyBindJump.getKeyCode())));
         } else if (☃xx == 103.0F) {
            this.client
               .ingameGUI
               .getChatGUI()
               .printChatMessage(new TextComponentTranslation("demo.help.inventory", GameSettings.getKeyDisplayString(☃xxxx.keyBindInventory.getKeyCode())));
         }
      } else if (☃x == 6) {
         this.world.playSound(☃, ☃.posX, ☃.posY + ☃.getEyeHeight(), ☃.posZ, SoundEvents.ENTITY_ARROW_HIT_PLAYER, SoundCategory.PLAYERS, 0.18F, 0.45F);
      } else if (☃x == 7) {
         this.world.setRainStrength(☃xx);
      } else if (☃x == 8) {
         this.world.setThunderStrength(☃xx);
      } else if (☃x == 10) {
         this.world.spawnParticle(EnumParticleTypes.MOB_APPEARANCE, ☃.posX, ☃.posY, ☃.posZ, 0.0, 0.0, 0.0, new int[0]);
         this.world.playSound(☃, ☃.posX, ☃.posY, ☃.posZ, SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.HOSTILE, 1.0F, 1.0F);
      }
   }

   @Override
   public void handleMaps(SPacketMaps var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      MapItemRenderer ☃ = this.client.entityRenderer.getMapItemRenderer();
      MapData ☃x = ItemMap.loadMapData(☃.getMapId(), this.client.world);
      if (☃x == null) {
         String ☃xx = "map_" + ☃.getMapId();
         ☃x = new MapData(☃xx);
         if (☃.getMapInstanceIfExists(☃xx) != null) {
            MapData ☃xxx = ☃.getData(☃.getMapInstanceIfExists(☃xx));
            if (☃xxx != null) {
               ☃x = ☃xxx;
            }
         }

         this.client.world.setData(☃xx, ☃x);
      }

      ☃.setMapdataTo(☃x);
      ☃.updateMapTexture(☃x);
   }

   @Override
   public void handleEffect(SPacketEffect var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      if (☃.isSoundServerwide()) {
         this.client.world.playBroadcastSound(☃.getSoundType(), ☃.getSoundPos(), ☃.getSoundData());
      } else {
         this.client.world.playEvent(☃.getSoundType(), ☃.getSoundPos(), ☃.getSoundData());
      }
   }

   @Override
   public void handleAdvancementInfo(SPacketAdvancementInfo var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      this.advancementManager.read(☃);
   }

   @Override
   public void handleSelectAdvancementsTab(SPacketSelectAdvancementsTab var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      ResourceLocation ☃ = ☃.getTab();
      if (☃ == null) {
         this.advancementManager.setSelectedTab(null, false);
      } else {
         Advancement ☃x = this.advancementManager.getAdvancementList().getAdvancement(☃);
         this.advancementManager.setSelectedTab(☃x, false);
      }
   }

   @Override
   public void handleStatistics(SPacketStatistics var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);

      for (Entry<StatBase, Integer> ☃ : ☃.getStatisticMap().entrySet()) {
         StatBase ☃x = ☃.getKey();
         int ☃xx = ☃.getValue();
         this.client.player.getStatFileWriter().unlockAchievement(this.client.player, ☃x, ☃xx);
      }

      this.hasStatistics = true;
      if (this.client.currentScreen instanceof IProgressMeter) {
         ((IProgressMeter)this.client.currentScreen).onStatsUpdated();
      }
   }

   @Override
   public void handleRecipeBook(SPacketRecipeBook var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      RecipeBook ☃ = this.client.player.getRecipeBook();
      ☃.setGuiOpen(☃.isGuiOpen());
      ☃.setFilteringCraftable(☃.isFilteringCraftable());
      SPacketRecipeBook.State ☃x = ☃.getState();
      switch (☃x) {
         case REMOVE:
            for (IRecipe ☃xx : ☃.getRecipes()) {
               ☃.lock(☃xx);
            }
            break;
         case INIT:
            ☃.getRecipes().forEach(☃::unlock);
            ☃.getDisplayedRecipes().forEach(☃::markNew);
            break;
         case ADD:
            ☃.getRecipes().forEach(var2x -> {
               ☃.unlock(var2x);
               ☃.markNew(var2x);
               RecipeToast.addOrUpdate(this.client.getToastGui(), var2x);
            });
      }

      RecipeBookClient.ALL_RECIPES.forEach(var1x -> var1x.updateKnownRecipes(☃));
      if (this.client.currentScreen instanceof IRecipeShownListener) {
         ((IRecipeShownListener)this.client.currentScreen).recipesUpdated();
      }
   }

   @Override
   public void handleEntityEffect(SPacketEntityEffect var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      Entity ☃ = this.world.getEntityByID(☃.getEntityId());
      if (☃ instanceof EntityLivingBase) {
         Potion ☃x = Potion.getPotionById(☃.getEffectId());
         if (☃x != null) {
            PotionEffect ☃xx = new PotionEffect(☃x, ☃.getDuration(), ☃.getAmplifier(), ☃.getIsAmbient(), ☃.doesShowParticles());
            ☃xx.setPotionDurationMax(☃.isMaxDuration());
            ((EntityLivingBase)☃).addPotionEffect(☃xx);
         }
      }
   }

   @Override
   public void handleCombatEvent(SPacketCombatEvent var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      if (☃.eventType == SPacketCombatEvent.Event.ENTITY_DIED) {
         Entity ☃ = this.world.getEntityByID(☃.playerId);
         if (☃ == this.client.player) {
            this.client.displayGuiScreen(new GuiGameOver(☃.deathMessage));
         }
      }
   }

   @Override
   public void handleServerDifficulty(SPacketServerDifficulty var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      this.client.world.getWorldInfo().setDifficulty(☃.getDifficulty());
      this.client.world.getWorldInfo().setDifficultyLocked(☃.isDifficultyLocked());
   }

   @Override
   public void handleCamera(SPacketCamera var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      Entity ☃ = ☃.getEntity(this.world);
      if (☃ != null) {
         this.client.setRenderViewEntity(☃);
      }
   }

   @Override
   public void handleWorldBorder(SPacketWorldBorder var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      ☃.apply(this.world.getWorldBorder());
   }

   @Override
   public void handleTitle(SPacketTitle var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      SPacketTitle.Type ☃ = ☃.getType();
      String ☃x = null;
      String ☃xx = null;
      String ☃xxx = ☃.getMessage() != null ? ☃.getMessage().getFormattedText() : "";
      switch (☃) {
         case TITLE:
            ☃x = ☃xxx;
            break;
         case SUBTITLE:
            ☃xx = ☃xxx;
            break;
         case ACTIONBAR:
            this.client.ingameGUI.setOverlayMessage(☃xxx, false);
            return;
         case RESET:
            this.client.ingameGUI.displayTitle("", "", -1, -1, -1);
            this.client.ingameGUI.setDefaultTitlesTimes();
            return;
      }

      this.client.ingameGUI.displayTitle(☃x, ☃xx, ☃.getFadeInTime(), ☃.getDisplayTime(), ☃.getFadeOutTime());
   }

   @Override
   public void handlePlayerListHeaderFooter(SPacketPlayerListHeaderFooter var1) {
      this.client.ingameGUI.getTabList().setHeader(☃.getHeader().getFormattedText().isEmpty() ? null : ☃.getHeader());
      this.client.ingameGUI.getTabList().setFooter(☃.getFooter().getFormattedText().isEmpty() ? null : ☃.getFooter());
   }

   @Override
   public void handleRemoveEntityEffect(SPacketRemoveEntityEffect var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      Entity ☃ = ☃.getEntity(this.world);
      if (☃ instanceof EntityLivingBase) {
         ((EntityLivingBase)☃).removeActivePotionEffect(☃.getPotion());
      }
   }

   @Override
   public void handlePlayerListItem(SPacketPlayerListItem var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);

      for (SPacketPlayerListItem.AddPlayerData ☃ : ☃.getEntries()) {
         if (☃.getAction() == SPacketPlayerListItem.Action.REMOVE_PLAYER) {
            this.playerInfoMap.remove(☃.getProfile().getId());
         } else {
            NetworkPlayerInfo ☃x = this.playerInfoMap.get(☃.getProfile().getId());
            if (☃.getAction() == SPacketPlayerListItem.Action.ADD_PLAYER) {
               ☃x = new NetworkPlayerInfo(☃);
               this.playerInfoMap.put(☃x.getGameProfile().getId(), ☃x);
            }

            if (☃x != null) {
               switch (☃.getAction()) {
                  case ADD_PLAYER:
                     ☃x.setGameType(☃.getGameMode());
                     ☃x.setResponseTime(☃.getPing());
                     break;
                  case UPDATE_GAME_MODE:
                     ☃x.setGameType(☃.getGameMode());
                     break;
                  case UPDATE_LATENCY:
                     ☃x.setResponseTime(☃.getPing());
                     break;
                  case UPDATE_DISPLAY_NAME:
                     ☃x.setDisplayName(☃.getDisplayName());
               }
            }
         }
      }
   }

   @Override
   public void handleKeepAlive(SPacketKeepAlive var1) {
      this.sendPacket(new CPacketKeepAlive(☃.getId()));
   }

   @Override
   public void handlePlayerAbilities(SPacketPlayerAbilities var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      EntityPlayer ☃ = this.client.player;
      ☃.capabilities.isFlying = ☃.isFlying();
      ☃.capabilities.isCreativeMode = ☃.isCreativeMode();
      ☃.capabilities.disableDamage = ☃.isInvulnerable();
      ☃.capabilities.allowFlying = ☃.isAllowFlying();
      ☃.capabilities.setFlySpeed(☃.getFlySpeed());
      ☃.capabilities.setPlayerWalkSpeed(☃.getWalkSpeed());
   }

   @Override
   public void handleTabComplete(SPacketTabComplete var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      String[] ☃ = ☃.getMatches();
      Arrays.sort((Object[])☃);
      if (this.client.currentScreen instanceof ITabCompleter) {
         ((ITabCompleter)this.client.currentScreen).setCompletions(☃);
      }
   }

   @Override
   public void handleSoundEffect(SPacketSoundEffect var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      this.client.world.playSound(this.client.player, ☃.getX(), ☃.getY(), ☃.getZ(), ☃.getSound(), ☃.getCategory(), ☃.getVolume(), ☃.getPitch());
   }

   @Override
   public void handleCustomSound(SPacketCustomSound var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      this.client
         .getSoundHandler()
         .playSound(
            new PositionedSoundRecord(
               new ResourceLocation(☃.getSoundName()),
               ☃.getCategory(),
               ☃.getVolume(),
               ☃.getPitch(),
               false,
               0,
               ISound.AttenuationType.LINEAR,
               (float)☃.getX(),
               (float)☃.getY(),
               (float)☃.getZ()
            )
         );
   }

   @Override
   public void handleResourcePack(SPacketResourcePackSend var1) {
      final String ☃ = ☃.getURL();
      final String ☃x = ☃.getHash();
      if (this.validateResourcePackUrl(☃)) {
         if (☃.startsWith("level://")) {
            try {
               String ☃xx = URLDecoder.decode(☃.substring("level://".length()), StandardCharsets.UTF_8.toString());
               File ☃xxx = new File(this.client.gameDir, "saves");
               File ☃xxxx = new File(☃xxx, ☃xx);
               if (☃xxxx.isFile()) {
                  this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.ACCEPTED));
                  Futures.addCallback(this.client.getResourcePackRepository().setServerResourcePack(☃xxxx), this.createDownloadCallback());
                  return;
               }
            } catch (UnsupportedEncodingException var7) {
            }

            this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.FAILED_DOWNLOAD));
         } else {
            ServerData ☃xx = this.client.getCurrentServerData();
            if (☃xx != null && ☃xx.getResourceMode() == ServerData.ServerResourceMode.ENABLED) {
               this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.ACCEPTED));
               Futures.addCallback(this.client.getResourcePackRepository().downloadResourcePack(☃, ☃x), this.createDownloadCallback());
            } else if (☃xx != null && ☃xx.getResourceMode() != ServerData.ServerResourceMode.PROMPT) {
               this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.DECLINED));
            } else {
               this.client
                  .addScheduledTask(
                     new Runnable() {
                        @Override
                        public void run() {
                           NetHandlerPlayClient.this.client
                              .displayGuiScreen(
                                 new GuiYesNo(
                                    new GuiYesNoCallback() {
                                       @Override
                                       public void confirmClicked(boolean var1, int var2x) {
                                          NetHandlerPlayClient.this.client = Minecraft.getMinecraft();
                                          ServerData ☃xxx = NetHandlerPlayClient.this.client.getCurrentServerData();
                                          if (☃) {
                                             if (☃xxx != null) {
                                                ☃xxx.setResourceMode(ServerData.ServerResourceMode.ENABLED);
                                             }

                                             NetHandlerPlayClient.this.netManager
                                                .sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.ACCEPTED));
                                             Futures.addCallback(
                                                NetHandlerPlayClient.this.client.getResourcePackRepository().downloadResourcePack(☃, ☃),
                                                NetHandlerPlayClient.this.createDownloadCallback()
                                             );
                                          } else {
                                             if (☃xxx != null) {
                                                ☃xxx.setResourceMode(ServerData.ServerResourceMode.DISABLED);
                                             }

                                             NetHandlerPlayClient.this.netManager
                                                .sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.DECLINED));
                                          }

                                          ServerList.saveSingleServer(☃xxx);
                                          NetHandlerPlayClient.this.client.displayGuiScreen(null);
                                       }
                                    },
                                    I18n.format("multiplayer.texturePrompt.line1"),
                                    I18n.format("multiplayer.texturePrompt.line2"),
                                    0
                                 )
                              );
                        }
                     }
                  );
            }
         }
      }
   }

   private boolean validateResourcePackUrl(String var1) {
      try {
         URI ☃ = new URI(☃);
         String ☃x = ☃.getScheme();
         boolean ☃xx = "level".equals(☃x);
         if (!"http".equals(☃x) && !"https".equals(☃x) && !☃xx) {
            throw new URISyntaxException(☃, "Wrong protocol");
         } else if (!☃xx || !☃.contains("..") && ☃.endsWith("/resources.zip")) {
            return true;
         } else {
            throw new URISyntaxException(☃, "Invalid levelstorage resourcepack path");
         }
      } catch (URISyntaxException var5) {
         this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.FAILED_DOWNLOAD));
         return false;
      }
   }

   private FutureCallback<Object> createDownloadCallback() {
      return new FutureCallback<Object>() {
         public void onSuccess(@Nullable Object var1) {
            NetHandlerPlayClient.this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
         }

         public void onFailure(Throwable var1) {
            NetHandlerPlayClient.this.netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.FAILED_DOWNLOAD));
         }
      };
   }

   @Override
   public void handleUpdateBossInfo(SPacketUpdateBossInfo var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      this.client.ingameGUI.getBossOverlay().read(☃);
   }

   @Override
   public void handleCooldown(SPacketCooldown var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      if (☃.getTicks() == 0) {
         this.client.player.getCooldownTracker().removeCooldown(☃.getItem());
      } else {
         this.client.player.getCooldownTracker().setCooldown(☃.getItem(), ☃.getTicks());
      }
   }

   @Override
   public void handleMoveVehicle(SPacketMoveVehicle var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      Entity ☃ = this.client.player.getLowestRidingEntity();
      if (☃ != this.client.player && ☃.canPassengerSteer()) {
         ☃.setPositionAndRotation(☃.getX(), ☃.getY(), ☃.getZ(), ☃.getYaw(), ☃.getPitch());
         this.netManager.sendPacket(new CPacketVehicleMove(☃));
      }
   }

   @Override
   public void handleCustomPayload(SPacketCustomPayload var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      if ("MC|TrList".equals(☃.getChannelName())) {
         PacketBuffer ☃ = ☃.getBufferData();

         try {
            int ☃x = ☃.readInt();
            GuiScreen ☃xx = this.client.currentScreen;
            if (☃xx != null && ☃xx instanceof GuiMerchant && ☃x == this.client.player.openContainer.windowId) {
               IMerchant ☃xxx = ((GuiMerchant)☃xx).getMerchant();
               MerchantRecipeList ☃xxxx = MerchantRecipeList.readFromBuf(☃);
               ☃xxx.setRecipes(☃xxxx);
            }
         } catch (IOException var10) {
            LOGGER.error("Couldn't load trade info", var10);
         } finally {
            ☃.release();
         }
      } else if ("MC|Brand".equals(☃.getChannelName())) {
         this.client.player.setServerBrand(☃.getBufferData().readString(32767));
      } else if ("MC|BOpen".equals(☃.getChannelName())) {
         EnumHand ☃ = ☃.getBufferData().readEnumValue(EnumHand.class);
         ItemStack ☃x = ☃ == EnumHand.OFF_HAND ? this.client.player.getHeldItemOffhand() : this.client.player.getHeldItemMainhand();
         if (☃x.getItem() == Items.WRITTEN_BOOK) {
            this.client.displayGuiScreen(new GuiScreenBook(this.client.player, ☃x, false));
         }
      } else if ("MC|DebugPath".equals(☃.getChannelName())) {
         PacketBuffer ☃ = ☃.getBufferData();
         int ☃x = ☃.readInt();
         float ☃xx = ☃.readFloat();
         Path ☃xxx = Path.read(☃);
         ((DebugRendererPathfinding)this.client.debugRenderer.pathfinding).addPath(☃x, ☃xxx, ☃xx);
      } else if ("MC|DebugNeighborsUpdate".equals(☃.getChannelName())) {
         PacketBuffer ☃ = ☃.getBufferData();
         long ☃x = ☃.readVarLong();
         BlockPos ☃xx = ☃.readBlockPos();
         ((DebugRendererNeighborsUpdate)this.client.debugRenderer.neighborsUpdate).addUpdate(☃x, ☃xx);
      } else if ("MC|StopSound".equals(☃.getChannelName())) {
         PacketBuffer ☃ = ☃.getBufferData();
         String ☃x = ☃.readString(32767);
         String ☃xx = ☃.readString(256);
         this.client.getSoundHandler().stop(☃xx, SoundCategory.getByName(☃x));
      }
   }

   @Override
   public void handleScoreboardObjective(SPacketScoreboardObjective var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      Scoreboard ☃ = this.world.getScoreboard();
      if (☃.getAction() == 0) {
         ScoreObjective ☃x = ☃.addScoreObjective(☃.getObjectiveName(), IScoreCriteria.DUMMY);
         ☃x.setDisplayName(☃.getObjectiveValue());
         ☃x.setRenderType(☃.getRenderType());
      } else {
         ScoreObjective ☃x = ☃.getObjective(☃.getObjectiveName());
         if (☃.getAction() == 1) {
            ☃.removeObjective(☃x);
         } else if (☃.getAction() == 2) {
            ☃x.setDisplayName(☃.getObjectiveValue());
            ☃x.setRenderType(☃.getRenderType());
         }
      }
   }

   @Override
   public void handleUpdateScore(SPacketUpdateScore var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      Scoreboard ☃ = this.world.getScoreboard();
      ScoreObjective ☃x = ☃.getObjective(☃.getObjectiveName());
      if (☃.getScoreAction() == SPacketUpdateScore.Action.CHANGE) {
         Score ☃xx = ☃.getOrCreateScore(☃.getPlayerName(), ☃x);
         ☃xx.setScorePoints(☃.getScoreValue());
      } else if (☃.getScoreAction() == SPacketUpdateScore.Action.REMOVE) {
         if (StringUtils.isNullOrEmpty(☃.getObjectiveName())) {
            ☃.removeObjectiveFromEntity(☃.getPlayerName(), null);
         } else if (☃x != null) {
            ☃.removeObjectiveFromEntity(☃.getPlayerName(), ☃x);
         }
      }
   }

   @Override
   public void handleDisplayObjective(SPacketDisplayObjective var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      Scoreboard ☃ = this.world.getScoreboard();
      if (☃.getName().isEmpty()) {
         ☃.setObjectiveInDisplaySlot(☃.getPosition(), null);
      } else {
         ScoreObjective ☃x = ☃.getObjective(☃.getName());
         ☃.setObjectiveInDisplaySlot(☃.getPosition(), ☃x);
      }
   }

   @Override
   public void handleTeams(SPacketTeams var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      Scoreboard ☃ = this.world.getScoreboard();
      ScorePlayerTeam ☃x;
      if (☃.getAction() == 0) {
         ☃x = ☃.createTeam(☃.getName());
      } else {
         ☃x = ☃.getTeam(☃.getName());
      }

      if (☃.getAction() == 0 || ☃.getAction() == 2) {
         ☃x.setDisplayName(☃.getDisplayName());
         ☃x.setPrefix(☃.getPrefix());
         ☃x.setSuffix(☃.getSuffix());
         ☃x.setColor(TextFormatting.fromColorIndex(☃.getColor()));
         ☃x.setFriendlyFlags(☃.getFriendlyFlags());
         Team.EnumVisible ☃xx = Team.EnumVisible.getByName(☃.getNameTagVisibility());
         if (☃xx != null) {
            ☃x.setNameTagVisibility(☃xx);
         }

         Team.CollisionRule ☃xxx = Team.CollisionRule.getByName(☃.getCollisionRule());
         if (☃xxx != null) {
            ☃x.setCollisionRule(☃xxx);
         }
      }

      if (☃.getAction() == 0 || ☃.getAction() == 3) {
         for (String ☃xxx : ☃.getPlayers()) {
            ☃.addPlayerToTeam(☃xxx, ☃.getName());
         }
      }

      if (☃.getAction() == 4) {
         for (String ☃xxx : ☃.getPlayers()) {
            ☃.removePlayerFromTeam(☃xxx, ☃x);
         }
      }

      if (☃.getAction() == 1) {
         ☃.removeTeam(☃x);
      }
   }

   @Override
   public void handleParticles(SPacketParticles var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      if (☃.getParticleCount() == 0) {
         double ☃ = ☃.getParticleSpeed() * ☃.getXOffset();
         double ☃x = ☃.getParticleSpeed() * ☃.getYOffset();
         double ☃xx = ☃.getParticleSpeed() * ☃.getZOffset();

         try {
            this.world
               .spawnParticle(
                  ☃.getParticleType(), ☃.isLongDistance(), ☃.getXCoordinate(), ☃.getYCoordinate(), ☃.getZCoordinate(), ☃, ☃x, ☃xx, ☃.getParticleArgs()
               );
         } catch (Throwable var17) {
            LOGGER.warn("Could not spawn particle effect {}", ☃.getParticleType());
         }
      } else {
         for (int ☃ = 0; ☃ < ☃.getParticleCount(); ☃++) {
            double ☃x = this.avRandomizer.nextGaussian() * ☃.getXOffset();
            double ☃xx = this.avRandomizer.nextGaussian() * ☃.getYOffset();
            double ☃xxx = this.avRandomizer.nextGaussian() * ☃.getZOffset();
            double ☃xxxx = this.avRandomizer.nextGaussian() * ☃.getParticleSpeed();
            double ☃xxxxx = this.avRandomizer.nextGaussian() * ☃.getParticleSpeed();
            double ☃xxxxxx = this.avRandomizer.nextGaussian() * ☃.getParticleSpeed();

            try {
               this.world
                  .spawnParticle(
                     ☃.getParticleType(),
                     ☃.isLongDistance(),
                     ☃.getXCoordinate() + ☃x,
                     ☃.getYCoordinate() + ☃xx,
                     ☃.getZCoordinate() + ☃xxx,
                     ☃xxxx,
                     ☃xxxxx,
                     ☃xxxxxx,
                     ☃.getParticleArgs()
                  );
            } catch (Throwable var16) {
               LOGGER.warn("Could not spawn particle effect {}", ☃.getParticleType());
               return;
            }
         }
      }
   }

   @Override
   public void handleEntityProperties(SPacketEntityProperties var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      Entity ☃ = this.world.getEntityByID(☃.getEntityId());
      if (☃ != null) {
         if (!(☃ instanceof EntityLivingBase)) {
            throw new IllegalStateException("Server tried to update attributes of a non-living entity (actually: " + ☃ + ")");
         } else {
            AbstractAttributeMap ☃x = ((EntityLivingBase)☃).getAttributeMap();

            for (SPacketEntityProperties.Snapshot ☃xx : ☃.getSnapshots()) {
               IAttributeInstance ☃xxx = ☃x.getAttributeInstanceByName(☃xx.getName());
               if (☃xxx == null) {
                  ☃xxx = ☃x.registerAttribute(new RangedAttribute(null, ☃xx.getName(), 0.0, Double.MIN_NORMAL, Double.MAX_VALUE));
               }

               ☃xxx.setBaseValue(☃xx.getBaseValue());
               ☃xxx.removeAllModifiers();

               for (AttributeModifier ☃xxxx : ☃xx.getModifiers()) {
                  ☃xxx.applyModifier(☃xxxx);
               }
            }
         }
      }
   }

   @Override
   public void func_194307_a(SPacketPlaceGhostRecipe var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.client);
      Container ☃ = this.client.player.openContainer;
      if (☃.windowId == ☃.func_194313_b() && ☃.getCanCraft(this.client.player)) {
         if (this.client.currentScreen instanceof IRecipeShownListener) {
            GuiRecipeBook ☃x = ((IRecipeShownListener)this.client.currentScreen).func_194310_f();
            ☃x.setupGhostRecipe(☃.func_194311_a(), ☃.inventorySlots);
         }
      }
   }

   public NetworkManager getNetworkManager() {
      return this.netManager;
   }

   public Collection<NetworkPlayerInfo> getPlayerInfoMap() {
      return this.playerInfoMap.values();
   }

   public NetworkPlayerInfo getPlayerInfo(UUID var1) {
      return this.playerInfoMap.get(☃);
   }

   @Nullable
   public NetworkPlayerInfo getPlayerInfo(String var1) {
      for (NetworkPlayerInfo ☃ : this.playerInfoMap.values()) {
         if (☃.getGameProfile().getName().equals(☃)) {
            return ☃;
         }
      }

      return null;
   }

   public GameProfile getGameProfile() {
      return this.profile;
   }

   public ClientAdvancementManager getAdvancementManager() {
      return this.advancementManager;
   }
}
