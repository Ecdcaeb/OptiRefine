package net.minecraft.network;

import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.util.concurrent.Futures;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketEnchantItem;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlaceRecipe;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketSeenAdvancements;
import net.minecraft.network.play.client.CPacketSpectate;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketKeepAlive;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.ServerRecipeBookHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldServer;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetHandlerPlayServer implements INetHandlerPlayServer, ITickable {
   private static final Logger LOGGER = LogManager.getLogger();
   public final NetworkManager netManager;
   private final MinecraftServer server;
   public EntityPlayerMP player;
   private int networkTickCount;
   private long field_194402_f;
   private boolean field_194403_g;
   private long field_194404_h;
   private int chatSpamThresholdCount;
   private int itemDropThreshold;
   private final IntHashMap<Short> pendingTransactions = new IntHashMap<>();
   private double firstGoodX;
   private double firstGoodY;
   private double firstGoodZ;
   private double lastGoodX;
   private double lastGoodY;
   private double lastGoodZ;
   private Entity lowestRiddenEnt;
   private double lowestRiddenX;
   private double lowestRiddenY;
   private double lowestRiddenZ;
   private double lowestRiddenX1;
   private double lowestRiddenY1;
   private double lowestRiddenZ1;
   private Vec3d targetPos;
   private int teleportId;
   private int lastPositionUpdate;
   private boolean floating;
   private int floatingTickCount;
   private boolean vehicleFloating;
   private int vehicleFloatingTickCount;
   private int movePacketCounter;
   private int lastMovePacketCounter;
   private ServerRecipeBookHelper field_194309_H = new ServerRecipeBookHelper();

   public NetHandlerPlayServer(MinecraftServer var1, NetworkManager var2, EntityPlayerMP var3) {
      this.server = ☃;
      this.netManager = ☃;
      ☃.setNetHandler(this);
      this.player = ☃;
      ☃.connection = this;
   }

   @Override
   public void update() {
      this.captureCurrentPosition();
      this.player.onUpdateEntity();
      this.player.setPositionAndRotation(this.firstGoodX, this.firstGoodY, this.firstGoodZ, this.player.rotationYaw, this.player.rotationPitch);
      this.networkTickCount++;
      this.lastMovePacketCounter = this.movePacketCounter;
      if (this.floating) {
         if (++this.floatingTickCount > 80) {
            LOGGER.warn("{} was kicked for floating too long!", this.player.getName());
            this.disconnect(new TextComponentTranslation("multiplayer.disconnect.flying"));
            return;
         }
      } else {
         this.floating = false;
         this.floatingTickCount = 0;
      }

      this.lowestRiddenEnt = this.player.getLowestRidingEntity();
      if (this.lowestRiddenEnt != this.player && this.lowestRiddenEnt.getControllingPassenger() == this.player) {
         this.lowestRiddenX = this.lowestRiddenEnt.posX;
         this.lowestRiddenY = this.lowestRiddenEnt.posY;
         this.lowestRiddenZ = this.lowestRiddenEnt.posZ;
         this.lowestRiddenX1 = this.lowestRiddenEnt.posX;
         this.lowestRiddenY1 = this.lowestRiddenEnt.posY;
         this.lowestRiddenZ1 = this.lowestRiddenEnt.posZ;
         if (this.vehicleFloating && this.player.getLowestRidingEntity().getControllingPassenger() == this.player) {
            if (++this.vehicleFloatingTickCount > 80) {
               LOGGER.warn("{} was kicked for floating a vehicle too long!", this.player.getName());
               this.disconnect(new TextComponentTranslation("multiplayer.disconnect.flying"));
               return;
            }
         } else {
            this.vehicleFloating = false;
            this.vehicleFloatingTickCount = 0;
         }
      } else {
         this.lowestRiddenEnt = null;
         this.vehicleFloating = false;
         this.vehicleFloatingTickCount = 0;
      }

      this.server.profiler.startSection("keepAlive");
      long ☃ = this.currentTimeMillis();
      if (☃ - this.field_194402_f >= 15000L) {
         if (this.field_194403_g) {
            this.disconnect(new TextComponentTranslation("disconnect.timeout"));
         } else {
            this.field_194403_g = true;
            this.field_194402_f = ☃;
            this.field_194404_h = ☃;
            this.sendPacket(new SPacketKeepAlive(this.field_194404_h));
         }
      }

      this.server.profiler.endSection();
      if (this.chatSpamThresholdCount > 0) {
         this.chatSpamThresholdCount--;
      }

      if (this.itemDropThreshold > 0) {
         this.itemDropThreshold--;
      }

      if (this.player.getLastActiveTime() > 0L
         && this.server.getMaxPlayerIdleMinutes() > 0
         && MinecraftServer.getCurrentTimeMillis() - this.player.getLastActiveTime() > this.server.getMaxPlayerIdleMinutes() * 1000 * 60) {
         this.disconnect(new TextComponentTranslation("multiplayer.disconnect.idling"));
      }
   }

   private void captureCurrentPosition() {
      this.firstGoodX = this.player.posX;
      this.firstGoodY = this.player.posY;
      this.firstGoodZ = this.player.posZ;
      this.lastGoodX = this.player.posX;
      this.lastGoodY = this.player.posY;
      this.lastGoodZ = this.player.posZ;
   }

   public NetworkManager getNetworkManager() {
      return this.netManager;
   }

   public void disconnect(final ITextComponent var1) {
      this.netManager.sendPacket(new SPacketDisconnect(☃), new GenericFutureListener<Future<? super Void>>() {
         public void operationComplete(Future<? super Void> var1x) throws Exception {
            NetHandlerPlayServer.this.netManager.closeChannel(☃);
         }
      });
      this.netManager.disableAutoRead();
      Futures.getUnchecked(this.server.addScheduledTask(new Runnable() {
         @Override
         public void run() {
            NetHandlerPlayServer.this.netManager.handleDisconnection();
         }
      }));
   }

   @Override
   public void processInput(CPacketInput var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      this.player.setEntityActionState(☃.getStrafeSpeed(), ☃.getForwardSpeed(), ☃.isJumping(), ☃.isSneaking());
   }

   private static boolean isMovePlayerPacketInvalid(CPacketPlayer var0) {
      return Doubles.isFinite(☃.getX(0.0))
            && Doubles.isFinite(☃.getY(0.0))
            && Doubles.isFinite(☃.getZ(0.0))
            && Floats.isFinite(☃.getPitch(0.0F))
            && Floats.isFinite(☃.getYaw(0.0F))
         ? Math.abs(☃.getX(0.0)) > 3.0E7 || Math.abs(☃.getY(0.0)) > 3.0E7 || Math.abs(☃.getZ(0.0)) > 3.0E7
         : true;
   }

   private static boolean isMoveVehiclePacketInvalid(CPacketVehicleMove var0) {
      return !Doubles.isFinite(☃.getX())
         || !Doubles.isFinite(☃.getY())
         || !Doubles.isFinite(☃.getZ())
         || !Floats.isFinite(☃.getPitch())
         || !Floats.isFinite(☃.getYaw());
   }

   @Override
   public void processVehicleMove(CPacketVehicleMove var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      if (isMoveVehiclePacketInvalid(☃)) {
         this.disconnect(new TextComponentTranslation("multiplayer.disconnect.invalid_vehicle_movement"));
      } else {
         Entity ☃ = this.player.getLowestRidingEntity();
         if (☃ != this.player && ☃.getControllingPassenger() == this.player && ☃ == this.lowestRiddenEnt) {
            WorldServer ☃x = this.player.getServerWorld();
            double ☃xx = ☃.posX;
            double ☃xxx = ☃.posY;
            double ☃xxxx = ☃.posZ;
            double ☃xxxxx = ☃.getX();
            double ☃xxxxxx = ☃.getY();
            double ☃xxxxxxx = ☃.getZ();
            float ☃xxxxxxxx = ☃.getYaw();
            float ☃xxxxxxxxx = ☃.getPitch();
            double ☃xxxxxxxxxx = ☃xxxxx - this.lowestRiddenX;
            double ☃xxxxxxxxxxx = ☃xxxxxx - this.lowestRiddenY;
            double ☃xxxxxxxxxxxx = ☃xxxxxxx - this.lowestRiddenZ;
            double ☃xxxxxxxxxxxxx = ☃.motionX * ☃.motionX + ☃.motionY * ☃.motionY + ☃.motionZ * ☃.motionZ;
            double ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxx * ☃xxxxxxxxxx + ☃xxxxxxxxxxx * ☃xxxxxxxxxxx + ☃xxxxxxxxxxxx * ☃xxxxxxxxxxxx;
            if (☃xxxxxxxxxxxxxx - ☃xxxxxxxxxxxxx > 100.0 && (!this.server.isSinglePlayer() || !this.server.getServerOwner().equals(☃.getName()))) {
               LOGGER.warn("{} (vehicle of {}) moved too quickly! {},{},{}", ☃.getName(), this.player.getName(), ☃xxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxx);
               this.netManager.sendPacket(new SPacketMoveVehicle(☃));
               return;
            }

            boolean ☃xxxxxxxxxxxxxxx = ☃x.getCollisionBoxes(☃, ☃.getEntityBoundingBox().shrink(0.0625)).isEmpty();
            ☃xxxxxxxxxx = ☃xxxxx - this.lowestRiddenX1;
            ☃xxxxxxxxxxx = ☃xxxxxx - this.lowestRiddenY1 - 1.0E-6;
            ☃xxxxxxxxxxxx = ☃xxxxxxx - this.lowestRiddenZ1;
            ☃.move(MoverType.PLAYER, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxx);
            ☃xxxxxxxxxx = ☃xxxxx - ☃.posX;
            ☃xxxxxxxxxxx = ☃xxxxxx - ☃.posY;
            if (☃xxxxxxxxxxx > -0.5 || ☃xxxxxxxxxxx < 0.5) {
               ☃xxxxxxxxxxx = 0.0;
            }

            ☃xxxxxxxxxxxx = ☃xxxxxxx - ☃.posZ;
            ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxx * ☃xxxxxxxxxx + ☃xxxxxxxxxxx * ☃xxxxxxxxxxx + ☃xxxxxxxxxxxx * ☃xxxxxxxxxxxx;
            boolean ☃xxxxxxxxxxxxxxxx = false;
            if (☃xxxxxxxxxxxxxx > 0.0625) {
               ☃xxxxxxxxxxxxxxxx = true;
               LOGGER.warn("{} moved wrongly!", ☃.getName());
            }

            ☃.setPositionAndRotation(☃xxxxx, ☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxx);
            boolean ☃xxxxxxxxxxxxxxxxx = ☃x.getCollisionBoxes(☃, ☃.getEntityBoundingBox().shrink(0.0625)).isEmpty();
            if (☃xxxxxxxxxxxxxxx && (☃xxxxxxxxxxxxxxxx || !☃xxxxxxxxxxxxxxxxx)) {
               ☃.setPositionAndRotation(☃xx, ☃xxx, ☃xxxx, ☃xxxxxxxx, ☃xxxxxxxxx);
               this.netManager.sendPacket(new SPacketMoveVehicle(☃));
               return;
            }

            this.server.getPlayerList().serverUpdateMovingPlayer(this.player);
            this.player.addMovementStat(this.player.posX - ☃xx, this.player.posY - ☃xxx, this.player.posZ - ☃xxxx);
            this.vehicleFloating = ☃xxxxxxxxxxx >= -0.03125
               && !this.server.isFlightAllowed()
               && !☃x.checkBlockCollision(☃.getEntityBoundingBox().grow(0.0625).expand(0.0, -0.55, 0.0));
            this.lowestRiddenX1 = ☃.posX;
            this.lowestRiddenY1 = ☃.posY;
            this.lowestRiddenZ1 = ☃.posZ;
         }
      }
   }

   @Override
   public void processConfirmTeleport(CPacketConfirmTeleport var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      if (☃.getTeleportId() == this.teleportId) {
         this.player.setPositionAndRotation(this.targetPos.x, this.targetPos.y, this.targetPos.z, this.player.rotationYaw, this.player.rotationPitch);
         if (this.player.isInvulnerableDimensionChange()) {
            this.lastGoodX = this.targetPos.x;
            this.lastGoodY = this.targetPos.y;
            this.lastGoodZ = this.targetPos.z;
            this.player.clearInvulnerableDimensionChange();
         }

         this.targetPos = null;
      }
   }

   @Override
   public void handleRecipeBookUpdate(CPacketRecipeInfo var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      if (☃.getPurpose() == CPacketRecipeInfo.Purpose.SHOWN) {
         this.player.getRecipeBook().markSeen(☃.getRecipe());
      } else if (☃.getPurpose() == CPacketRecipeInfo.Purpose.SETTINGS) {
         this.player.getRecipeBook().setGuiOpen(☃.isGuiOpen());
         this.player.getRecipeBook().setFilteringCraftable(☃.isFilteringCraftable());
      }
   }

   @Override
   public void handleSeenAdvancements(CPacketSeenAdvancements var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      if (☃.getAction() == CPacketSeenAdvancements.Action.OPENED_TAB) {
         ResourceLocation ☃ = ☃.getTab();
         Advancement ☃x = this.server.getAdvancementManager().getAdvancement(☃);
         if (☃x != null) {
            this.player.getAdvancements().setSelectedTab(☃x);
         }
      }
   }

   @Override
   public void processPlayer(CPacketPlayer var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      if (isMovePlayerPacketInvalid(☃)) {
         this.disconnect(new TextComponentTranslation("multiplayer.disconnect.invalid_player_movement"));
      } else {
         WorldServer ☃ = this.server.getWorld(this.player.dimension);
         if (!this.player.queuedEndExit) {
            if (this.networkTickCount == 0) {
               this.captureCurrentPosition();
            }

            if (this.targetPos != null) {
               if (this.networkTickCount - this.lastPositionUpdate > 20) {
                  this.lastPositionUpdate = this.networkTickCount;
                  this.setPlayerLocation(this.targetPos.x, this.targetPos.y, this.targetPos.z, this.player.rotationYaw, this.player.rotationPitch);
               }
            } else {
               this.lastPositionUpdate = this.networkTickCount;
               if (this.player.isRiding()) {
                  this.player
                     .setPositionAndRotation(
                        this.player.posX, this.player.posY, this.player.posZ, ☃.getYaw(this.player.rotationYaw), ☃.getPitch(this.player.rotationPitch)
                     );
                  this.server.getPlayerList().serverUpdateMovingPlayer(this.player);
               } else {
                  double ☃x = this.player.posX;
                  double ☃xx = this.player.posY;
                  double ☃xxx = this.player.posZ;
                  double ☃xxxx = this.player.posY;
                  double ☃xxxxx = ☃.getX(this.player.posX);
                  double ☃xxxxxx = ☃.getY(this.player.posY);
                  double ☃xxxxxxx = ☃.getZ(this.player.posZ);
                  float ☃xxxxxxxx = ☃.getYaw(this.player.rotationYaw);
                  float ☃xxxxxxxxx = ☃.getPitch(this.player.rotationPitch);
                  double ☃xxxxxxxxxx = ☃xxxxx - this.firstGoodX;
                  double ☃xxxxxxxxxxx = ☃xxxxxx - this.firstGoodY;
                  double ☃xxxxxxxxxxxx = ☃xxxxxxx - this.firstGoodZ;
                  double ☃xxxxxxxxxxxxx = this.player.motionX * this.player.motionX
                     + this.player.motionY * this.player.motionY
                     + this.player.motionZ * this.player.motionZ;
                  double ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxx * ☃xxxxxxxxxx + ☃xxxxxxxxxxx * ☃xxxxxxxxxxx + ☃xxxxxxxxxxxx * ☃xxxxxxxxxxxx;
                  if (this.player.isPlayerSleeping()) {
                     if (☃xxxxxxxxxxxxxx > 1.0) {
                        this.setPlayerLocation(
                           this.player.posX, this.player.posY, this.player.posZ, ☃.getYaw(this.player.rotationYaw), ☃.getPitch(this.player.rotationPitch)
                        );
                     }
                  } else {
                     this.movePacketCounter++;
                     int ☃xxxxxxxxxxxxxxx = this.movePacketCounter - this.lastMovePacketCounter;
                     if (☃xxxxxxxxxxxxxxx > 5) {
                        LOGGER.debug("{} is sending move packets too frequently ({} packets since last tick)", this.player.getName(), ☃xxxxxxxxxxxxxxx);
                        ☃xxxxxxxxxxxxxxx = 1;
                     }

                     if (!this.player.isInvulnerableDimensionChange()
                        && (!this.player.getServerWorld().getGameRules().getBoolean("disableElytraMovementCheck") || !this.player.isElytraFlying())) {
                        float ☃xxxxxxxxxxxxxxxx = this.player.isElytraFlying() ? 300.0F : 100.0F;
                        if (☃xxxxxxxxxxxxxx - ☃xxxxxxxxxxxxx > ☃xxxxxxxxxxxxxxxx * ☃xxxxxxxxxxxxxxx
                           && (!this.server.isSinglePlayer() || !this.server.getServerOwner().equals(this.player.getName()))) {
                           LOGGER.warn("{} moved too quickly! {},{},{}", this.player.getName(), ☃xxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxx);
                           this.setPlayerLocation(this.player.posX, this.player.posY, this.player.posZ, this.player.rotationYaw, this.player.rotationPitch);
                           return;
                        }
                     }

                     boolean ☃xxxxxxxxxxxxxxxx = ☃.getCollisionBoxes(this.player, this.player.getEntityBoundingBox().shrink(0.0625)).isEmpty();
                     ☃xxxxxxxxxx = ☃xxxxx - this.lastGoodX;
                     ☃xxxxxxxxxxx = ☃xxxxxx - this.lastGoodY;
                     ☃xxxxxxxxxxxx = ☃xxxxxxx - this.lastGoodZ;
                     if (this.player.onGround && !☃.isOnGround() && ☃xxxxxxxxxxx > 0.0) {
                        this.player.jump();
                     }

                     this.player.move(MoverType.PLAYER, ☃xxxxxxxxxx, ☃xxxxxxxxxxx, ☃xxxxxxxxxxxx);
                     this.player.onGround = ☃.isOnGround();
                     ☃xxxxxxxxxx = ☃xxxxx - this.player.posX;
                     ☃xxxxxxxxxxx = ☃xxxxxx - this.player.posY;
                     if (☃xxxxxxxxxxx > -0.5 || ☃xxxxxxxxxxx < 0.5) {
                        ☃xxxxxxxxxxx = 0.0;
                     }

                     ☃xxxxxxxxxxxx = ☃xxxxxxx - this.player.posZ;
                     ☃xxxxxxxxxxxxxx = ☃xxxxxxxxxx * ☃xxxxxxxxxx + ☃xxxxxxxxxxx * ☃xxxxxxxxxxx + ☃xxxxxxxxxxxx * ☃xxxxxxxxxxxx;
                     boolean ☃xxxxxxxxxxxxxxxxx = false;
                     if (!this.player.isInvulnerableDimensionChange()
                        && ☃xxxxxxxxxxxxxx > 0.0625
                        && !this.player.isPlayerSleeping()
                        && !this.player.interactionManager.isCreative()
                        && this.player.interactionManager.getGameType() != GameType.SPECTATOR) {
                        ☃xxxxxxxxxxxxxxxxx = true;
                        LOGGER.warn("{} moved wrongly!", this.player.getName());
                     }

                     this.player.setPositionAndRotation(☃xxxxx, ☃xxxxxx, ☃xxxxxxx, ☃xxxxxxxx, ☃xxxxxxxxx);
                     this.player.addMovementStat(this.player.posX - ☃x, this.player.posY - ☃xx, this.player.posZ - ☃xxx);
                     if (!this.player.noClip && !this.player.isPlayerSleeping()) {
                        boolean ☃xxxxxxxxxxxxxxxxxx = ☃.getCollisionBoxes(this.player, this.player.getEntityBoundingBox().shrink(0.0625)).isEmpty();
                        if (☃xxxxxxxxxxxxxxxx && (☃xxxxxxxxxxxxxxxxx || !☃xxxxxxxxxxxxxxxxxx)) {
                           this.setPlayerLocation(☃x, ☃xx, ☃xxx, ☃xxxxxxxx, ☃xxxxxxxxx);
                           return;
                        }
                     }

                     this.floating = ☃xxxxxxxxxxx >= -0.03125;
                     this.floating = this.floating & (!this.server.isFlightAllowed() && !this.player.capabilities.allowFlying);
                     this.floating = this.floating
                        & (
                           !this.player.isPotionActive(MobEffects.LEVITATION)
                              && !this.player.isElytraFlying()
                              && !☃.checkBlockCollision(this.player.getEntityBoundingBox().grow(0.0625).expand(0.0, -0.55, 0.0))
                        );
                     this.player.onGround = ☃.isOnGround();
                     this.server.getPlayerList().serverUpdateMovingPlayer(this.player);
                     this.player.handleFalling(this.player.posY - ☃xxxx, ☃.isOnGround());
                     this.lastGoodX = this.player.posX;
                     this.lastGoodY = this.player.posY;
                     this.lastGoodZ = this.player.posZ;
                  }
               }
            }
         }
      }
   }

   public void setPlayerLocation(double var1, double var3, double var5, float var7, float var8) {
      this.setPlayerLocation(☃, ☃, ☃, ☃, ☃, Collections.emptySet());
   }

   public void setPlayerLocation(double var1, double var3, double var5, float var7, float var8, Set<SPacketPlayerPosLook.EnumFlags> var9) {
      double ☃ = ☃.contains(SPacketPlayerPosLook.EnumFlags.X) ? this.player.posX : 0.0;
      double ☃x = ☃.contains(SPacketPlayerPosLook.EnumFlags.Y) ? this.player.posY : 0.0;
      double ☃xx = ☃.contains(SPacketPlayerPosLook.EnumFlags.Z) ? this.player.posZ : 0.0;
      this.targetPos = new Vec3d(☃ + ☃, ☃ + ☃x, ☃ + ☃xx);
      float ☃xxx = ☃;
      float ☃xxxx = ☃;
      if (☃.contains(SPacketPlayerPosLook.EnumFlags.Y_ROT)) {
         ☃xxx = ☃ + this.player.rotationYaw;
      }

      if (☃.contains(SPacketPlayerPosLook.EnumFlags.X_ROT)) {
         ☃xxxx = ☃ + this.player.rotationPitch;
      }

      if (++this.teleportId == Integer.MAX_VALUE) {
         this.teleportId = 0;
      }

      this.lastPositionUpdate = this.networkTickCount;
      this.player.setPositionAndRotation(this.targetPos.x, this.targetPos.y, this.targetPos.z, ☃xxx, ☃xxxx);
      this.player.connection.sendPacket(new SPacketPlayerPosLook(☃, ☃, ☃, ☃, ☃, ☃, this.teleportId));
   }

   @Override
   public void processPlayerDigging(CPacketPlayerDigging var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      WorldServer ☃ = this.server.getWorld(this.player.dimension);
      BlockPos ☃x = ☃.getPosition();
      this.player.markPlayerActive();
      switch (☃.getAction()) {
         case SWAP_HELD_ITEMS:
            if (!this.player.isSpectator()) {
               ItemStack ☃xx = this.player.getHeldItem(EnumHand.OFF_HAND);
               this.player.setHeldItem(EnumHand.OFF_HAND, this.player.getHeldItem(EnumHand.MAIN_HAND));
               this.player.setHeldItem(EnumHand.MAIN_HAND, ☃xx);
            }

            return;
         case DROP_ITEM:
            if (!this.player.isSpectator()) {
               this.player.dropItem(false);
            }

            return;
         case DROP_ALL_ITEMS:
            if (!this.player.isSpectator()) {
               this.player.dropItem(true);
            }

            return;
         case RELEASE_USE_ITEM:
            this.player.stopActiveHand();
            return;
         case START_DESTROY_BLOCK:
         case ABORT_DESTROY_BLOCK:
         case STOP_DESTROY_BLOCK:
            double ☃xx = this.player.posX - (☃x.getX() + 0.5);
            double ☃xxx = this.player.posY - (☃x.getY() + 0.5) + 1.5;
            double ☃xxxx = this.player.posZ - (☃x.getZ() + 0.5);
            double ☃xxxxx = ☃xx * ☃xx + ☃xxx * ☃xxx + ☃xxxx * ☃xxxx;
            if (☃xxxxx > 36.0) {
               return;
            } else if (☃x.getY() >= this.server.getBuildLimit()) {
               return;
            } else {
               if (☃.getAction() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                  if (!this.server.isBlockProtected(☃, ☃x, this.player) && ☃.getWorldBorder().contains(☃x)) {
                     this.player.interactionManager.onBlockClicked(☃x, ☃.getFacing());
                  } else {
                     this.player.connection.sendPacket(new SPacketBlockChange(☃, ☃x));
                  }
               } else {
                  if (☃.getAction() == CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                     this.player.interactionManager.blockRemoving(☃x);
                  } else if (☃.getAction() == CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK) {
                     this.player.interactionManager.cancelDestroyingBlock();
                  }

                  if (☃.getBlockState(☃x).getMaterial() != Material.AIR) {
                     this.player.connection.sendPacket(new SPacketBlockChange(☃, ☃x));
                  }
               }

               return;
            }
         default:
            throw new IllegalArgumentException("Invalid player action");
      }
   }

   @Override
   public void processTryUseItemOnBlock(CPacketPlayerTryUseItemOnBlock var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      WorldServer ☃ = this.server.getWorld(this.player.dimension);
      EnumHand ☃x = ☃.getHand();
      ItemStack ☃xx = this.player.getHeldItem(☃x);
      BlockPos ☃xxx = ☃.getPos();
      EnumFacing ☃xxxx = ☃.getDirection();
      this.player.markPlayerActive();
      if (☃xxx.getY() < this.server.getBuildLimit() - 1 || ☃xxxx != EnumFacing.UP && ☃xxx.getY() < this.server.getBuildLimit()) {
         if (this.targetPos == null
            && this.player.getDistanceSq(☃xxx.getX() + 0.5, ☃xxx.getY() + 0.5, ☃xxx.getZ() + 0.5) < 64.0
            && !this.server.isBlockProtected(☃, ☃xxx, this.player)
            && ☃.getWorldBorder().contains(☃xxx)) {
            this.player.interactionManager.processRightClickBlock(this.player, ☃, ☃xx, ☃x, ☃xxx, ☃xxxx, ☃.getFacingX(), ☃.getFacingY(), ☃.getFacingZ());
         }
      } else {
         TextComponentTranslation ☃xxxxx = new TextComponentTranslation("build.tooHigh", this.server.getBuildLimit());
         ☃xxxxx.getStyle().setColor(TextFormatting.RED);
         this.player.connection.sendPacket(new SPacketChat(☃xxxxx, ChatType.GAME_INFO));
      }

      this.player.connection.sendPacket(new SPacketBlockChange(☃, ☃xxx));
      this.player.connection.sendPacket(new SPacketBlockChange(☃, ☃xxx.offset(☃xxxx)));
   }

   @Override
   public void processTryUseItem(CPacketPlayerTryUseItem var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      WorldServer ☃ = this.server.getWorld(this.player.dimension);
      EnumHand ☃x = ☃.getHand();
      ItemStack ☃xx = this.player.getHeldItem(☃x);
      this.player.markPlayerActive();
      if (!☃xx.isEmpty()) {
         this.player.interactionManager.processRightClick(this.player, ☃, ☃xx, ☃x);
      }
   }

   @Override
   public void handleSpectate(CPacketSpectate var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      if (this.player.isSpectator()) {
         Entity ☃ = null;

         for (WorldServer ☃x : this.server.worlds) {
            if (☃x != null) {
               ☃ = ☃.getEntity(☃x);
               if (☃ != null) {
                  break;
               }
            }
         }

         if (☃ != null) {
            this.player.setSpectatingEntity(this.player);
            this.player.dismountRidingEntity();
            if (☃.world == this.player.world) {
               this.player.setPositionAndUpdate(☃.posX, ☃.posY, ☃.posZ);
            } else {
               WorldServer ☃xx = this.player.getServerWorld();
               WorldServer ☃xxx = (WorldServer)☃.world;
               this.player.dimension = ☃.dimension;
               this.sendPacket(
                  new SPacketRespawn(
                     this.player.dimension, ☃xx.getDifficulty(), ☃xx.getWorldInfo().getTerrainType(), this.player.interactionManager.getGameType()
                  )
               );
               this.server.getPlayerList().updatePermissionLevel(this.player);
               ☃xx.removeEntityDangerously(this.player);
               this.player.isDead = false;
               this.player.setLocationAndAngles(☃.posX, ☃.posY, ☃.posZ, ☃.rotationYaw, ☃.rotationPitch);
               if (this.player.isEntityAlive()) {
                  ☃xx.updateEntityWithOptionalForce(this.player, false);
                  ☃xxx.spawnEntity(this.player);
                  ☃xxx.updateEntityWithOptionalForce(this.player, false);
               }

               this.player.setWorld(☃xxx);
               this.server.getPlayerList().preparePlayer(this.player, ☃xx);
               this.player.setPositionAndUpdate(☃.posX, ☃.posY, ☃.posZ);
               this.player.interactionManager.setWorld(☃xxx);
               this.server.getPlayerList().updateTimeAndWeatherForPlayer(this.player, ☃xxx);
               this.server.getPlayerList().syncPlayerInventory(this.player);
            }
         }
      }
   }

   @Override
   public void handleResourcePackStatus(CPacketResourcePackStatus var1) {
   }

   @Override
   public void processSteerBoat(CPacketSteerBoat var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      Entity ☃ = this.player.getRidingEntity();
      if (☃ instanceof EntityBoat) {
         ((EntityBoat)☃).setPaddleState(☃.getLeft(), ☃.getRight());
      }
   }

   @Override
   public void onDisconnect(ITextComponent var1) {
      LOGGER.info("{} lost connection: {}", this.player.getName(), ☃.getUnformattedText());
      this.server.refreshStatusNextTick();
      TextComponentTranslation ☃ = new TextComponentTranslation("multiplayer.player.left", this.player.getDisplayName());
      ☃.getStyle().setColor(TextFormatting.YELLOW);
      this.server.getPlayerList().sendMessage(☃);
      this.player.mountEntityAndWakeUp();
      this.server.getPlayerList().playerLoggedOut(this.player);
      if (this.server.isSinglePlayer() && this.player.getName().equals(this.server.getServerOwner())) {
         LOGGER.info("Stopping singleplayer server as player logged out");
         this.server.initiateShutdown();
      }
   }

   public void sendPacket(final Packet<?> var1) {
      if (☃ instanceof SPacketChat) {
         SPacketChat ☃ = (SPacketChat)☃;
         EntityPlayer.EnumChatVisibility ☃x = this.player.getChatVisibility();
         if (☃x == EntityPlayer.EnumChatVisibility.HIDDEN && ☃.getType() != ChatType.GAME_INFO) {
            return;
         }

         if (☃x == EntityPlayer.EnumChatVisibility.SYSTEM && !☃.isSystem()) {
            return;
         }
      }

      try {
         this.netManager.sendPacket(☃);
      } catch (Throwable var5) {
         CrashReport ☃xx = CrashReport.makeCrashReport(var5, "Sending packet");
         CrashReportCategory ☃xxx = ☃xx.makeCategory("Packet being sent");
         ☃xxx.addDetail("Packet class", new ICrashReportDetail<String>() {
            public String call() throws Exception {
               return ☃.getClass().getCanonicalName();
            }
         });
         throw new ReportedException(☃xx);
      }
   }

   @Override
   public void processHeldItemChange(CPacketHeldItemChange var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      if (☃.getSlotId() >= 0 && ☃.getSlotId() < InventoryPlayer.getHotbarSize()) {
         this.player.inventory.currentItem = ☃.getSlotId();
         this.player.markPlayerActive();
      } else {
         LOGGER.warn("{} tried to set an invalid carried item", this.player.getName());
      }
   }

   @Override
   public void processChatMessage(CPacketChatMessage var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      if (this.player.getChatVisibility() == EntityPlayer.EnumChatVisibility.HIDDEN) {
         TextComponentTranslation ☃ = new TextComponentTranslation("chat.cannotSend");
         ☃.getStyle().setColor(TextFormatting.RED);
         this.sendPacket(new SPacketChat(☃));
      } else {
         this.player.markPlayerActive();
         String ☃ = ☃.getMessage();
         ☃ = StringUtils.normalizeSpace(☃);

         for (int ☃x = 0; ☃x < ☃.length(); ☃x++) {
            if (!ChatAllowedCharacters.isAllowedCharacter(☃.charAt(☃x))) {
               this.disconnect(new TextComponentTranslation("multiplayer.disconnect.illegal_characters"));
               return;
            }
         }

         if (☃.startsWith("/")) {
            this.handleSlashCommand(☃);
         } else {
            ITextComponent ☃xx = new TextComponentTranslation("chat.type.text", this.player.getDisplayName(), ☃);
            this.server.getPlayerList().sendMessage(☃xx, false);
         }

         this.chatSpamThresholdCount += 20;
         if (this.chatSpamThresholdCount > 200 && !this.server.getPlayerList().canSendCommands(this.player.getGameProfile())) {
            this.disconnect(new TextComponentTranslation("disconnect.spam"));
         }
      }
   }

   private void handleSlashCommand(String var1) {
      this.server.getCommandManager().executeCommand(this.player, ☃);
   }

   @Override
   public void handleAnimation(CPacketAnimation var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      this.player.markPlayerActive();
      this.player.swingArm(☃.getHand());
   }

   @Override
   public void processEntityAction(CPacketEntityAction var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      this.player.markPlayerActive();
      switch (☃.getAction()) {
         case START_SNEAKING:
            this.player.setSneaking(true);
            break;
         case STOP_SNEAKING:
            this.player.setSneaking(false);
            break;
         case START_SPRINTING:
            this.player.setSprinting(true);
            break;
         case STOP_SPRINTING:
            this.player.setSprinting(false);
            break;
         case STOP_SLEEPING:
            if (this.player.isPlayerSleeping()) {
               this.player.wakeUpPlayer(false, true, true);
               this.targetPos = new Vec3d(this.player.posX, this.player.posY, this.player.posZ);
            }
            break;
         case START_RIDING_JUMP:
            if (this.player.getRidingEntity() instanceof IJumpingMount) {
               IJumpingMount ☃ = (IJumpingMount)this.player.getRidingEntity();
               int ☃x = ☃.getAuxData();
               if (☃.canJump() && ☃x > 0) {
                  ☃.handleStartJump(☃x);
               }
            }
            break;
         case STOP_RIDING_JUMP:
            if (this.player.getRidingEntity() instanceof IJumpingMount) {
               IJumpingMount ☃ = (IJumpingMount)this.player.getRidingEntity();
               ☃.handleStopJump();
            }
            break;
         case OPEN_INVENTORY:
            if (this.player.getRidingEntity() instanceof AbstractHorse) {
               ((AbstractHorse)this.player.getRidingEntity()).openGUI(this.player);
            }
            break;
         case START_FALL_FLYING:
            if (!this.player.onGround && this.player.motionY < 0.0 && !this.player.isElytraFlying() && !this.player.isInWater()) {
               ItemStack ☃ = this.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
               if (☃.getItem() == Items.ELYTRA && ItemElytra.isUsable(☃)) {
                  this.player.setElytraFlying();
               }
            } else {
               this.player.clearElytraFlying();
            }
            break;
         default:
            throw new IllegalArgumentException("Invalid client command!");
      }
   }

   @Override
   public void processUseEntity(CPacketUseEntity var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      WorldServer ☃ = this.server.getWorld(this.player.dimension);
      Entity ☃x = ☃.getEntityFromWorld(☃);
      this.player.markPlayerActive();
      if (☃x != null) {
         boolean ☃xx = this.player.canEntityBeSeen(☃x);
         double ☃xxx = 36.0;
         if (!☃xx) {
            ☃xxx = 9.0;
         }

         if (this.player.getDistanceSq(☃x) < ☃xxx) {
            if (☃.getAction() == CPacketUseEntity.Action.INTERACT) {
               EnumHand ☃xxxx = ☃.getHand();
               this.player.interactOn(☃x, ☃xxxx);
            } else if (☃.getAction() == CPacketUseEntity.Action.INTERACT_AT) {
               EnumHand ☃xxxx = ☃.getHand();
               ☃x.applyPlayerInteraction(this.player, ☃.getHitVec(), ☃xxxx);
            } else if (☃.getAction() == CPacketUseEntity.Action.ATTACK) {
               if (☃x instanceof EntityItem || ☃x instanceof EntityXPOrb || ☃x instanceof EntityArrow || ☃x == this.player) {
                  this.disconnect(new TextComponentTranslation("multiplayer.disconnect.invalid_entity_attacked"));
                  this.server.logWarning("Player " + this.player.getName() + " tried to attack an invalid entity");
                  return;
               }

               this.player.attackTargetEntityWithCurrentItem(☃x);
            }
         }
      }
   }

   @Override
   public void processClientStatus(CPacketClientStatus var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      this.player.markPlayerActive();
      CPacketClientStatus.State ☃ = ☃.getStatus();
      switch (☃) {
         case PERFORM_RESPAWN:
            if (this.player.queuedEndExit) {
               this.player.queuedEndExit = false;
               this.player = this.server.getPlayerList().recreatePlayerEntity(this.player, 0, true);
               CriteriaTriggers.CHANGED_DIMENSION.trigger(this.player, DimensionType.THE_END, DimensionType.OVERWORLD);
            } else {
               if (this.player.getHealth() > 0.0F) {
                  return;
               }

               this.player = this.server.getPlayerList().recreatePlayerEntity(this.player, 0, false);
               if (this.server.isHardcore()) {
                  this.player.setGameType(GameType.SPECTATOR);
                  this.player.getServerWorld().getGameRules().setOrCreateGameRule("spectatorsGenerateChunks", "false");
               }
            }
            break;
         case REQUEST_STATS:
            this.player.getStatFile().sendStats(this.player);
      }
   }

   @Override
   public void processCloseWindow(CPacketCloseWindow var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      this.player.closeContainer();
   }

   @Override
   public void processClickWindow(CPacketClickWindow var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      this.player.markPlayerActive();
      if (this.player.openContainer.windowId == ☃.getWindowId() && this.player.openContainer.getCanCraft(this.player)) {
         if (this.player.isSpectator()) {
            NonNullList<ItemStack> ☃ = NonNullList.create();

            for (int ☃x = 0; ☃x < this.player.openContainer.inventorySlots.size(); ☃x++) {
               ☃.add(this.player.openContainer.inventorySlots.get(☃x).getStack());
            }

            this.player.sendAllContents(this.player.openContainer, ☃);
         } else {
            ItemStack ☃ = this.player.openContainer.slotClick(☃.getSlotId(), ☃.getUsedButton(), ☃.getClickType(), this.player);
            if (ItemStack.areItemStacksEqual(☃.getClickedItem(), ☃)) {
               this.player.connection.sendPacket(new SPacketConfirmTransaction(☃.getWindowId(), ☃.getActionNumber(), true));
               this.player.isChangingQuantityOnly = true;
               this.player.openContainer.detectAndSendChanges();
               this.player.updateHeldItem();
               this.player.isChangingQuantityOnly = false;
            } else {
               this.pendingTransactions.addKey(this.player.openContainer.windowId, ☃.getActionNumber());
               this.player.connection.sendPacket(new SPacketConfirmTransaction(☃.getWindowId(), ☃.getActionNumber(), false));
               this.player.openContainer.setCanCraft(this.player, false);
               NonNullList<ItemStack> ☃x = NonNullList.create();

               for (int ☃xx = 0; ☃xx < this.player.openContainer.inventorySlots.size(); ☃xx++) {
                  ItemStack ☃xxx = this.player.openContainer.inventorySlots.get(☃xx).getStack();
                  ItemStack ☃xxxx = ☃xxx.isEmpty() ? ItemStack.EMPTY : ☃xxx;
                  ☃x.add(☃xxxx);
               }

               this.player.sendAllContents(this.player.openContainer, ☃x);
            }
         }
      }
   }

   @Override
   public void func_194308_a(CPacketPlaceRecipe var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      this.player.markPlayerActive();
      if (!this.player.isSpectator() && this.player.openContainer.windowId == ☃.func_194318_a() && this.player.openContainer.getCanCraft(this.player)) {
         this.field_194309_H.func_194327_a(this.player, ☃.func_194317_b(), ☃.func_194319_c());
      }
   }

   @Override
   public void processEnchantItem(CPacketEnchantItem var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      this.player.markPlayerActive();
      if (this.player.openContainer.windowId == ☃.getWindowId() && this.player.openContainer.getCanCraft(this.player) && !this.player.isSpectator()) {
         this.player.openContainer.enchantItem(this.player, ☃.getButton());
         this.player.openContainer.detectAndSendChanges();
      }
   }

   @Override
   public void processCreativeInventoryAction(CPacketCreativeInventoryAction var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      if (this.player.interactionManager.isCreative()) {
         boolean ☃ = ☃.getSlotId() < 0;
         ItemStack ☃x = ☃.getStack();
         if (!☃x.isEmpty() && ☃x.hasTagCompound() && ☃x.getTagCompound().hasKey("BlockEntityTag", 10)) {
            NBTTagCompound ☃xx = ☃x.getTagCompound().getCompoundTag("BlockEntityTag");
            if (☃xx.hasKey("x") && ☃xx.hasKey("y") && ☃xx.hasKey("z")) {
               BlockPos ☃xxx = new BlockPos(☃xx.getInteger("x"), ☃xx.getInteger("y"), ☃xx.getInteger("z"));
               TileEntity ☃xxxx = this.player.world.getTileEntity(☃xxx);
               if (☃xxxx != null) {
                  NBTTagCompound ☃xxxxx = ☃xxxx.writeToNBT(new NBTTagCompound());
                  ☃xxxxx.removeTag("x");
                  ☃xxxxx.removeTag("y");
                  ☃xxxxx.removeTag("z");
                  ☃x.setTagInfo("BlockEntityTag", ☃xxxxx);
               }
            }
         }

         boolean ☃xx = ☃.getSlotId() >= 1 && ☃.getSlotId() <= 45;
         boolean ☃xxx = ☃x.isEmpty() || ☃x.getMetadata() >= 0 && ☃x.getCount() <= 64 && !☃x.isEmpty();
         if (☃xx && ☃xxx) {
            if (☃x.isEmpty()) {
               this.player.inventoryContainer.putStackInSlot(☃.getSlotId(), ItemStack.EMPTY);
            } else {
               this.player.inventoryContainer.putStackInSlot(☃.getSlotId(), ☃x);
            }

            this.player.inventoryContainer.setCanCraft(this.player, true);
         } else if (☃ && ☃xxx && this.itemDropThreshold < 200) {
            this.itemDropThreshold += 20;
            EntityItem ☃xxxx = this.player.dropItem(☃x, true);
            if (☃xxxx != null) {
               ☃xxxx.setAgeToCreativeDespawnTime();
            }
         }
      }
   }

   @Override
   public void processConfirmTransaction(CPacketConfirmTransaction var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      Short ☃ = this.pendingTransactions.lookup(this.player.openContainer.windowId);
      if (☃ != null
         && ☃.getUid() == ☃
         && this.player.openContainer.windowId == ☃.getWindowId()
         && !this.player.openContainer.getCanCraft(this.player)
         && !this.player.isSpectator()) {
         this.player.openContainer.setCanCraft(this.player, true);
      }
   }

   @Override
   public void processUpdateSign(CPacketUpdateSign var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      this.player.markPlayerActive();
      WorldServer ☃ = this.server.getWorld(this.player.dimension);
      BlockPos ☃x = ☃.getPosition();
      if (☃.isBlockLoaded(☃x)) {
         IBlockState ☃xx = ☃.getBlockState(☃x);
         TileEntity ☃xxx = ☃.getTileEntity(☃x);
         if (!(☃xxx instanceof TileEntitySign)) {
            return;
         }

         TileEntitySign ☃xxxx = (TileEntitySign)☃xxx;
         if (!☃xxxx.getIsEditable() || ☃xxxx.getPlayer() != this.player) {
            this.server.logWarning("Player " + this.player.getName() + " just tried to change non-editable sign");
            return;
         }

         String[] ☃xxxxx = ☃.getLines();

         for (int ☃xxxxxx = 0; ☃xxxxxx < ☃xxxxx.length; ☃xxxxxx++) {
            ☃xxxx.signText[☃xxxxxx] = new TextComponentString(TextFormatting.getTextWithoutFormattingCodes(☃xxxxx[☃xxxxxx]));
         }

         ☃xxxx.markDirty();
         ☃.notifyBlockUpdate(☃x, ☃xx, ☃xx, 3);
      }
   }

   @Override
   public void processKeepAlive(CPacketKeepAlive var1) {
      if (this.field_194403_g && ☃.getKey() == this.field_194404_h) {
         int ☃ = (int)(this.currentTimeMillis() - this.field_194402_f);
         this.player.ping = (this.player.ping * 3 + ☃) / 4;
         this.field_194403_g = false;
      } else if (!this.player.getName().equals(this.server.getServerOwner())) {
         this.disconnect(new TextComponentTranslation("disconnect.timeout"));
      }
   }

   private long currentTimeMillis() {
      return System.nanoTime() / 1000000L;
   }

   @Override
   public void processPlayerAbilities(CPacketPlayerAbilities var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      this.player.capabilities.isFlying = ☃.isFlying() && this.player.capabilities.allowFlying;
   }

   @Override
   public void processTabComplete(CPacketTabComplete var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      List<String> ☃ = Lists.newArrayList();

      for (String ☃x : this.server.getTabCompletions(this.player, ☃.getMessage(), ☃.getTargetBlock(), ☃.hasTargetBlock())) {
         ☃.add(☃x);
      }

      this.player.connection.sendPacket(new SPacketTabComplete(☃.toArray(new String[☃.size()])));
   }

   @Override
   public void processClientSettings(CPacketClientSettings var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      this.player.handleClientSettings(☃);
   }

   @Override
   public void processCustomPayload(CPacketCustomPayload var1) {
      PacketThreadUtil.checkThreadAndEnqueue(☃, this, this.player.getServerWorld());
      String ☃ = ☃.getChannelName();
      if ("MC|BEdit".equals(☃)) {
         PacketBuffer ☃x = ☃.getBufferData();

         try {
            ItemStack ☃xx = ☃x.readItemStack();
            if (☃xx.isEmpty()) {
               return;
            }

            if (!ItemWritableBook.isNBTValid(☃xx.getTagCompound())) {
               throw new IOException("Invalid book tag!");
            }

            ItemStack ☃xxx = this.player.getHeldItemMainhand();
            if (☃xxx.isEmpty()) {
               return;
            }

            if (☃xx.getItem() == Items.WRITABLE_BOOK && ☃xx.getItem() == ☃xxx.getItem()) {
               ☃xxx.setTagInfo("pages", ☃xx.getTagCompound().getTagList("pages", 8));
            }
         } catch (Exception var25) {
            LOGGER.error("Couldn't handle book info", var25);
         }
      } else if ("MC|BSign".equals(☃)) {
         PacketBuffer ☃x = ☃.getBufferData();

         try {
            ItemStack ☃xxxx = ☃x.readItemStack();
            if (☃xxxx.isEmpty()) {
               return;
            }

            if (!ItemWrittenBook.validBookTagContents(☃xxxx.getTagCompound())) {
               throw new IOException("Invalid book tag!");
            }

            ItemStack ☃xxxxx = this.player.getHeldItemMainhand();
            if (☃xxxxx.isEmpty()) {
               return;
            }

            if (☃xxxx.getItem() == Items.WRITABLE_BOOK && ☃xxxxx.getItem() == Items.WRITABLE_BOOK) {
               ItemStack ☃xxxxxx = new ItemStack(Items.WRITTEN_BOOK);
               ☃xxxxxx.setTagInfo("author", new NBTTagString(this.player.getName()));
               ☃xxxxxx.setTagInfo("title", new NBTTagString(☃xxxx.getTagCompound().getString("title")));
               NBTTagList ☃xxxxxxx = ☃xxxx.getTagCompound().getTagList("pages", 8);

               for (int ☃xxxxxxxx = 0; ☃xxxxxxxx < ☃xxxxxxx.tagCount(); ☃xxxxxxxx++) {
                  String ☃xxxxxxxxx = ☃xxxxxxx.getStringTagAt(☃xxxxxxxx);
                  ITextComponent ☃xxxxxxxxxx = new TextComponentString(☃xxxxxxxxx);
                  ☃xxxxxxxxx = ITextComponent.Serializer.componentToJson(☃xxxxxxxxxx);
                  ☃xxxxxxx.set(☃xxxxxxxx, new NBTTagString(☃xxxxxxxxx));
               }

               ☃xxxxxx.setTagInfo("pages", ☃xxxxxxx);
               this.player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ☃xxxxxx);
            }
         } catch (Exception var26) {
            LOGGER.error("Couldn't sign book", var26);
         }
      } else if ("MC|TrSel".equals(☃)) {
         try {
            int ☃x = ☃.getBufferData().readInt();
            Container ☃xxxxxx = this.player.openContainer;
            if (☃xxxxxx instanceof ContainerMerchant) {
               ((ContainerMerchant)☃xxxxxx).setCurrentRecipeIndex(☃x);
            }
         } catch (Exception var24) {
            LOGGER.error("Couldn't select trade", var24);
         }
      } else if ("MC|AdvCmd".equals(☃)) {
         if (!this.server.isCommandBlockEnabled()) {
            this.player.sendMessage(new TextComponentTranslation("advMode.notEnabled"));
            return;
         }

         if (!this.player.canUseCommandBlock()) {
            this.player.sendMessage(new TextComponentTranslation("advMode.notAllowed"));
            return;
         }

         PacketBuffer ☃x = ☃.getBufferData();

         try {
            int ☃xxxxxx = ☃x.readByte();
            CommandBlockBaseLogic ☃xxxxxxx = null;
            if (☃xxxxxx == 0) {
               TileEntity ☃xxxxxxxx = this.player.world.getTileEntity(new BlockPos(☃x.readInt(), ☃x.readInt(), ☃x.readInt()));
               if (☃xxxxxxxx instanceof TileEntityCommandBlock) {
                  ☃xxxxxxx = ((TileEntityCommandBlock)☃xxxxxxxx).getCommandBlockLogic();
               }
            } else if (☃xxxxxx == 1) {
               Entity ☃xxxxxxxx = this.player.world.getEntityByID(☃x.readInt());
               if (☃xxxxxxxx instanceof EntityMinecartCommandBlock) {
                  ☃xxxxxxx = ((EntityMinecartCommandBlock)☃xxxxxxxx).getCommandBlockLogic();
               }
            }

            String ☃xxxxxxxx = ☃x.readString(☃x.readableBytes());
            boolean ☃xxxxxxxxx = ☃x.readBoolean();
            if (☃xxxxxxx != null) {
               ☃xxxxxxx.setCommand(☃xxxxxxxx);
               ☃xxxxxxx.setTrackOutput(☃xxxxxxxxx);
               if (!☃xxxxxxxxx) {
                  ☃xxxxxxx.setLastOutput(null);
               }

               ☃xxxxxxx.updateCommand();
               this.player.sendMessage(new TextComponentTranslation("advMode.setCommand.success", ☃xxxxxxxx));
            }
         } catch (Exception var23) {
            LOGGER.error("Couldn't set command block", var23);
         }
      } else if ("MC|AutoCmd".equals(☃)) {
         if (!this.server.isCommandBlockEnabled()) {
            this.player.sendMessage(new TextComponentTranslation("advMode.notEnabled"));
            return;
         }

         if (!this.player.canUseCommandBlock()) {
            this.player.sendMessage(new TextComponentTranslation("advMode.notAllowed"));
            return;
         }

         PacketBuffer ☃x = ☃.getBufferData();

         try {
            CommandBlockBaseLogic ☃xxxxxxxx = null;
            TileEntityCommandBlock ☃xxxxxxxxx = null;
            BlockPos ☃xxxxxxxxxx = new BlockPos(☃x.readInt(), ☃x.readInt(), ☃x.readInt());
            TileEntity ☃xxxxxxxxxxx = this.player.world.getTileEntity(☃xxxxxxxxxx);
            if (☃xxxxxxxxxxx instanceof TileEntityCommandBlock) {
               ☃xxxxxxxxx = (TileEntityCommandBlock)☃xxxxxxxxxxx;
               ☃xxxxxxxx = ☃xxxxxxxxx.getCommandBlockLogic();
            }

            String ☃xxxxxxxxxxxx = ☃x.readString(☃x.readableBytes());
            boolean ☃xxxxxxxxxxxxx = ☃x.readBoolean();
            TileEntityCommandBlock.Mode ☃xxxxxxxxxxxxxx = TileEntityCommandBlock.Mode.valueOf(☃x.readString(16));
            boolean ☃xxxxxxxxxxxxxxx = ☃x.readBoolean();
            boolean ☃xxxxxxxxxxxxxxxx = ☃x.readBoolean();
            if (☃xxxxxxxx != null) {
               EnumFacing ☃xxxxxxxxxxxxxxxxx = this.player.world.getBlockState(☃xxxxxxxxxx).getValue(BlockCommandBlock.FACING);
               switch (☃xxxxxxxxxxxxxx) {
                  case SEQUENCE:
                     IBlockState ☃xxxxxxxxxxxxxxxxxx = Blocks.CHAIN_COMMAND_BLOCK.getDefaultState();
                     this.player
                        .world
                        .setBlockState(
                           ☃xxxxxxxxxx,
                           ☃xxxxxxxxxxxxxxxxxx.withProperty(BlockCommandBlock.FACING, ☃xxxxxxxxxxxxxxxxx)
                              .withProperty(BlockCommandBlock.CONDITIONAL, ☃xxxxxxxxxxxxxxx),
                           2
                        );
                     break;
                  case AUTO:
                     IBlockState ☃xxxxxxxxxxxxxxxxxxx = Blocks.REPEATING_COMMAND_BLOCK.getDefaultState();
                     this.player
                        .world
                        .setBlockState(
                           ☃xxxxxxxxxx,
                           ☃xxxxxxxxxxxxxxxxxxx.withProperty(BlockCommandBlock.FACING, ☃xxxxxxxxxxxxxxxxx)
                              .withProperty(BlockCommandBlock.CONDITIONAL, ☃xxxxxxxxxxxxxxx),
                           2
                        );
                     break;
                  case REDSTONE:
                     IBlockState ☃xxxxxxxxxxxxxxxxxxxx = Blocks.COMMAND_BLOCK.getDefaultState();
                     this.player
                        .world
                        .setBlockState(
                           ☃xxxxxxxxxx,
                           ☃xxxxxxxxxxxxxxxxxxxx.withProperty(BlockCommandBlock.FACING, ☃xxxxxxxxxxxxxxxxx)
                              .withProperty(BlockCommandBlock.CONDITIONAL, ☃xxxxxxxxxxxxxxx),
                           2
                        );
               }

               ☃xxxxxxxxxxx.validate();
               this.player.world.setTileEntity(☃xxxxxxxxxx, ☃xxxxxxxxxxx);
               ☃xxxxxxxx.setCommand(☃xxxxxxxxxxxx);
               ☃xxxxxxxx.setTrackOutput(☃xxxxxxxxxxxxx);
               if (!☃xxxxxxxxxxxxx) {
                  ☃xxxxxxxx.setLastOutput(null);
               }

               ☃xxxxxxxxx.setAuto(☃xxxxxxxxxxxxxxxx);
               ☃xxxxxxxx.updateCommand();
               if (!net.minecraft.util.StringUtils.isNullOrEmpty(☃xxxxxxxxxxxx)) {
                  this.player.sendMessage(new TextComponentTranslation("advMode.setCommand.success", ☃xxxxxxxxxxxx));
               }
            }
         } catch (Exception var22) {
            LOGGER.error("Couldn't set command block", var22);
         }
      } else if ("MC|Beacon".equals(☃)) {
         if (this.player.openContainer instanceof ContainerBeacon) {
            try {
               PacketBuffer ☃x = ☃.getBufferData();
               int ☃xxxxxxxxxxxx = ☃x.readInt();
               int ☃xxxxxxxxxxxxx = ☃x.readInt();
               ContainerBeacon ☃xxxxxxxxxxxxxx = (ContainerBeacon)this.player.openContainer;
               Slot ☃xxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxx.getSlot(0);
               if (☃xxxxxxxxxxxxxxx.getHasStack()) {
                  ☃xxxxxxxxxxxxxxx.decrStackSize(1);
                  IInventory ☃xxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxx.getTileEntity();
                  ☃xxxxxxxxxxxxxxxx.setField(1, ☃xxxxxxxxxxxx);
                  ☃xxxxxxxxxxxxxxxx.setField(2, ☃xxxxxxxxxxxxx);
                  ☃xxxxxxxxxxxxxxxx.markDirty();
               }
            } catch (Exception var21) {
               LOGGER.error("Couldn't set beacon", var21);
            }
         }
      } else if ("MC|ItemName".equals(☃)) {
         if (this.player.openContainer instanceof ContainerRepair) {
            ContainerRepair ☃x = (ContainerRepair)this.player.openContainer;
            if (☃.getBufferData() != null && ☃.getBufferData().readableBytes() >= 1) {
               String ☃xxxxxxxxxxxx = ChatAllowedCharacters.filterAllowedCharacters(☃.getBufferData().readString(32767));
               if (☃xxxxxxxxxxxx.length() <= 35) {
                  ☃x.updateItemName(☃xxxxxxxxxxxx);
               }
            } else {
               ☃x.updateItemName("");
            }
         }
      } else if ("MC|Struct".equals(☃)) {
         if (!this.player.canUseCommandBlock()) {
            return;
         }

         PacketBuffer ☃x = ☃.getBufferData();

         try {
            BlockPos ☃xxxxxxxxxxxx = new BlockPos(☃x.readInt(), ☃x.readInt(), ☃x.readInt());
            IBlockState ☃xxxxxxxxxxxxx = this.player.world.getBlockState(☃xxxxxxxxxxxx);
            TileEntity ☃xxxxxxxxxxxxxx = this.player.world.getTileEntity(☃xxxxxxxxxxxx);
            if (☃xxxxxxxxxxxxxx instanceof TileEntityStructure) {
               TileEntityStructure ☃xxxxxxxxxxxxxxx = (TileEntityStructure)☃xxxxxxxxxxxxxx;
               int ☃xxxxxxxxxxxxxxxx = ☃x.readByte();
               String ☃xxxxxxxxxxxxxxxxx = ☃x.readString(32);
               ☃xxxxxxxxxxxxxxx.setMode(TileEntityStructure.Mode.valueOf(☃xxxxxxxxxxxxxxxxx));
               ☃xxxxxxxxxxxxxxx.setName(☃x.readString(64));
               int ☃xxxxxxxxxxxxxxxxxx = MathHelper.clamp(☃x.readInt(), -32, 32);
               int ☃xxxxxxxxxxxxxxxxxxx = MathHelper.clamp(☃x.readInt(), -32, 32);
               int ☃xxxxxxxxxxxxxxxxxxxx = MathHelper.clamp(☃x.readInt(), -32, 32);
               ☃xxxxxxxxxxxxxxx.setPosition(new BlockPos(☃xxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxx));
               int ☃xxxxxxxxxxxxxxxxxxxxx = MathHelper.clamp(☃x.readInt(), 0, 32);
               int ☃xxxxxxxxxxxxxxxxxxxxxx = MathHelper.clamp(☃x.readInt(), 0, 32);
               int ☃xxxxxxxxxxxxxxxxxxxxxxx = MathHelper.clamp(☃x.readInt(), 0, 32);
               ☃xxxxxxxxxxxxxxx.setSize(new BlockPos(☃xxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxx, ☃xxxxxxxxxxxxxxxxxxxxxxx));
               String ☃xxxxxxxxxxxxxxxxxxxxxxxx = ☃x.readString(32);
               ☃xxxxxxxxxxxxxxx.setMirror(Mirror.valueOf(☃xxxxxxxxxxxxxxxxxxxxxxxx));
               String ☃xxxxxxxxxxxxxxxxxxxxxxxxx = ☃x.readString(32);
               ☃xxxxxxxxxxxxxxx.setRotation(Rotation.valueOf(☃xxxxxxxxxxxxxxxxxxxxxxxxx));
               ☃xxxxxxxxxxxxxxx.setMetadata(☃x.readString(128));
               ☃xxxxxxxxxxxxxxx.setIgnoresEntities(☃x.readBoolean());
               ☃xxxxxxxxxxxxxxx.setShowAir(☃x.readBoolean());
               ☃xxxxxxxxxxxxxxx.setShowBoundingBox(☃x.readBoolean());
               ☃xxxxxxxxxxxxxxx.setIntegrity(MathHelper.clamp(☃x.readFloat(), 0.0F, 1.0F));
               ☃xxxxxxxxxxxxxxx.setSeed(☃x.readVarLong());
               String ☃xxxxxxxxxxxxxxxxxxxxxxxxxx = ☃xxxxxxxxxxxxxxx.getName();
               if (☃xxxxxxxxxxxxxxxx == 2) {
                  if (☃xxxxxxxxxxxxxxx.save()) {
                     this.player.sendStatusMessage(new TextComponentTranslation("structure_block.save_success", ☃xxxxxxxxxxxxxxxxxxxxxxxxxx), false);
                  } else {
                     this.player.sendStatusMessage(new TextComponentTranslation("structure_block.save_failure", ☃xxxxxxxxxxxxxxxxxxxxxxxxxx), false);
                  }
               } else if (☃xxxxxxxxxxxxxxxx == 3) {
                  if (!☃xxxxxxxxxxxxxxx.isStructureLoadable()) {
                     this.player.sendStatusMessage(new TextComponentTranslation("structure_block.load_not_found", ☃xxxxxxxxxxxxxxxxxxxxxxxxxx), false);
                  } else if (☃xxxxxxxxxxxxxxx.load()) {
                     this.player.sendStatusMessage(new TextComponentTranslation("structure_block.load_success", ☃xxxxxxxxxxxxxxxxxxxxxxxxxx), false);
                  } else {
                     this.player.sendStatusMessage(new TextComponentTranslation("structure_block.load_prepare", ☃xxxxxxxxxxxxxxxxxxxxxxxxxx), false);
                  }
               } else if (☃xxxxxxxxxxxxxxxx == 4) {
                  if (☃xxxxxxxxxxxxxxx.detectSize()) {
                     this.player.sendStatusMessage(new TextComponentTranslation("structure_block.size_success", ☃xxxxxxxxxxxxxxxxxxxxxxxxxx), false);
                  } else {
                     this.player.sendStatusMessage(new TextComponentTranslation("structure_block.size_failure"), false);
                  }
               }

               ☃xxxxxxxxxxxxxxx.markDirty();
               this.player.world.notifyBlockUpdate(☃xxxxxxxxxxxx, ☃xxxxxxxxxxxxx, ☃xxxxxxxxxxxxx, 3);
            }
         } catch (Exception var20) {
            LOGGER.error("Couldn't set structure block", var20);
         }
      } else if ("MC|PickItem".equals(☃)) {
         PacketBuffer ☃x = ☃.getBufferData();

         try {
            int ☃xxxxxxxxxxxx = ☃x.readVarInt();
            this.player.inventory.pickItem(☃xxxxxxxxxxxx);
            this.player
               .connection
               .sendPacket(new SPacketSetSlot(-2, this.player.inventory.currentItem, this.player.inventory.getStackInSlot(this.player.inventory.currentItem)));
            this.player.connection.sendPacket(new SPacketSetSlot(-2, ☃xxxxxxxxxxxx, this.player.inventory.getStackInSlot(☃xxxxxxxxxxxx)));
            this.player.connection.sendPacket(new SPacketHeldItemChange(this.player.inventory.currentItem));
         } catch (Exception var19) {
            LOGGER.error("Couldn't pick item", var19);
         }
      }
   }
}
