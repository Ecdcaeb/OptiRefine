package net.minecraft.server.management;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.io.File;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketWorldBorder;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.WorldInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class PlayerList {
   public static final File FILE_PLAYERBANS = new File("banned-players.json");
   public static final File FILE_IPBANS = new File("banned-ips.json");
   public static final File FILE_OPS = new File("ops.json");
   public static final File FILE_WHITELIST = new File("whitelist.json");
   private static final Logger LOGGER = LogManager.getLogger();
   private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
   private final MinecraftServer server;
   private final List<EntityPlayerMP> playerEntityList = Lists.newArrayList();
   private final Map<UUID, EntityPlayerMP> uuidToPlayerMap = Maps.newHashMap();
   private final UserListBans bannedPlayers = new UserListBans(FILE_PLAYERBANS);
   private final UserListIPBans bannedIPs = new UserListIPBans(FILE_IPBANS);
   private final UserListOps ops = new UserListOps(FILE_OPS);
   private final UserListWhitelist whiteListedPlayers = new UserListWhitelist(FILE_WHITELIST);
   private final Map<UUID, StatisticsManagerServer> playerStatFiles = Maps.newHashMap();
   private final Map<UUID, PlayerAdvancements> advancements = Maps.newHashMap();
   private IPlayerFileData playerDataManager;
   private boolean whiteListEnforced;
   protected int maxPlayers;
   private int viewDistance;
   private GameType gameType;
   private boolean commandsAllowedForAll;
   private int playerPingIndex;

   public PlayerList(MinecraftServer var1) {
      this.server = ☃;
      this.bannedPlayers.setLanServer(false);
      this.bannedIPs.setLanServer(false);
      this.maxPlayers = 8;
   }

   public void initializeConnectionToPlayer(NetworkManager var1, EntityPlayerMP var2) {
      GameProfile ☃ = ☃.getGameProfile();
      PlayerProfileCache ☃x = this.server.getPlayerProfileCache();
      GameProfile ☃xx = ☃x.getProfileByUUID(☃.getId());
      String ☃xxx = ☃xx == null ? ☃.getName() : ☃xx.getName();
      ☃x.addEntry(☃);
      NBTTagCompound ☃xxxx = this.readPlayerDataFromFile(☃);
      ☃.setWorld(this.server.getWorld(☃.dimension));
      ☃.interactionManager.setWorld((WorldServer)☃.world);
      String ☃xxxxx = "local";
      if (☃.getRemoteAddress() != null) {
         ☃xxxxx = ☃.getRemoteAddress().toString();
      }

      LOGGER.info("{}[{}] logged in with entity id {} at ({}, {}, {})", ☃.getName(), ☃xxxxx, ☃.getEntityId(), ☃.posX, ☃.posY, ☃.posZ);
      WorldServer ☃xxxxxx = this.server.getWorld(☃.dimension);
      WorldInfo ☃xxxxxxx = ☃xxxxxx.getWorldInfo();
      this.setPlayerGameTypeBasedOnOther(☃, null, ☃xxxxxx);
      NetHandlerPlayServer ☃xxxxxxxx = new NetHandlerPlayServer(this.server, ☃, ☃);
      ☃xxxxxxxx.sendPacket(
         new SPacketJoinGame(
            ☃.getEntityId(),
            ☃.interactionManager.getGameType(),
            ☃xxxxxxx.isHardcoreModeEnabled(),
            ☃xxxxxx.provider.getDimensionType().getId(),
            ☃xxxxxx.getDifficulty(),
            this.getMaxPlayers(),
            ☃xxxxxxx.getTerrainType(),
            ☃xxxxxx.getGameRules().getBoolean("reducedDebugInfo")
         )
      );
      ☃xxxxxxxx.sendPacket(new SPacketCustomPayload("MC|Brand", new PacketBuffer(Unpooled.buffer()).writeString(this.getServerInstance().getServerModName())));
      ☃xxxxxxxx.sendPacket(new SPacketServerDifficulty(☃xxxxxxx.getDifficulty(), ☃xxxxxxx.isDifficultyLocked()));
      ☃xxxxxxxx.sendPacket(new SPacketPlayerAbilities(☃.capabilities));
      ☃xxxxxxxx.sendPacket(new SPacketHeldItemChange(☃.inventory.currentItem));
      this.updatePermissionLevel(☃);
      ☃.getStatFile().markAllDirty();
      ☃.getRecipeBook().init(☃);
      this.sendScoreboard((ServerScoreboard)☃xxxxxx.getScoreboard(), ☃);
      this.server.refreshStatusNextTick();
      TextComponentTranslation ☃xxxxxxxxx;
      if (☃.getName().equalsIgnoreCase(☃xxx)) {
         ☃xxxxxxxxx = new TextComponentTranslation("multiplayer.player.joined", ☃.getDisplayName());
      } else {
         ☃xxxxxxxxx = new TextComponentTranslation("multiplayer.player.joined.renamed", ☃.getDisplayName(), ☃xxx);
      }

      ☃xxxxxxxxx.getStyle().setColor(TextFormatting.YELLOW);
      this.sendMessage(☃xxxxxxxxx);
      this.playerLoggedIn(☃);
      ☃xxxxxxxx.setPlayerLocation(☃.posX, ☃.posY, ☃.posZ, ☃.rotationYaw, ☃.rotationPitch);
      this.updateTimeAndWeatherForPlayer(☃, ☃xxxxxx);
      if (!this.server.getResourcePackUrl().isEmpty()) {
         ☃.loadResourcePack(this.server.getResourcePackUrl(), this.server.getResourcePackHash());
      }

      for (PotionEffect ☃xxxxxxxxxx : ☃.getActivePotionEffects()) {
         ☃xxxxxxxx.sendPacket(new SPacketEntityEffect(☃.getEntityId(), ☃xxxxxxxxxx));
      }

      if (☃xxxx != null && ☃xxxx.hasKey("RootVehicle", 10)) {
         NBTTagCompound ☃xxxxxxxxxx = ☃xxxx.getCompoundTag("RootVehicle");
         Entity ☃xxxxxxxxxxx = AnvilChunkLoader.readWorldEntity(☃xxxxxxxxxx.getCompoundTag("Entity"), ☃xxxxxx, true);
         if (☃xxxxxxxxxxx != null) {
            UUID ☃xxxxxxxxxxxx = ☃xxxxxxxxxx.getUniqueId("Attach");
            if (☃xxxxxxxxxxx.getUniqueID().equals(☃xxxxxxxxxxxx)) {
               ☃.startRiding(☃xxxxxxxxxxx, true);
            } else {
               for (Entity ☃xxxxxxxxxxxxx : ☃xxxxxxxxxxx.getRecursivePassengers()) {
                  if (☃xxxxxxxxxxxxx.getUniqueID().equals(☃xxxxxxxxxxxx)) {
                     ☃.startRiding(☃xxxxxxxxxxxxx, true);
                     break;
                  }
               }
            }

            if (!☃.isRiding()) {
               LOGGER.warn("Couldn't reattach entity to player");
               ☃xxxxxx.removeEntityDangerously(☃xxxxxxxxxxx);

               for (Entity ☃xxxxxxxxxxxxxx : ☃xxxxxxxxxxx.getRecursivePassengers()) {
                  ☃xxxxxx.removeEntityDangerously(☃xxxxxxxxxxxxxx);
               }
            }
         }
      }

      ☃.addSelfToInternalCraftingInventory();
   }

   protected void sendScoreboard(ServerScoreboard var1, EntityPlayerMP var2) {
      Set<ScoreObjective> ☃ = Sets.newHashSet();

      for (ScorePlayerTeam ☃x : ☃.getTeams()) {
         ☃.connection.sendPacket(new SPacketTeams(☃x, 0));
      }

      for (int ☃x = 0; ☃x < 19; ☃x++) {
         ScoreObjective ☃xx = ☃.getObjectiveInDisplaySlot(☃x);
         if (☃xx != null && !☃.contains(☃xx)) {
            for (Packet<?> ☃xxx : ☃.getCreatePackets(☃xx)) {
               ☃.connection.sendPacket(☃xxx);
            }

            ☃.add(☃xx);
         }
      }
   }

   public void setPlayerManager(WorldServer[] var1) {
      this.playerDataManager = ☃[0].getSaveHandler().getPlayerNBTManager();
      ☃[0].getWorldBorder().addListener(new IBorderListener() {
         @Override
         public void onSizeChanged(WorldBorder var1, double var2) {
            PlayerList.this.sendPacketToAllPlayers(new SPacketWorldBorder(☃, SPacketWorldBorder.Action.SET_SIZE));
         }

         @Override
         public void onTransitionStarted(WorldBorder var1, double var2, double var4, long var6) {
            PlayerList.this.sendPacketToAllPlayers(new SPacketWorldBorder(☃, SPacketWorldBorder.Action.LERP_SIZE));
         }

         @Override
         public void onCenterChanged(WorldBorder var1, double var2, double var4) {
            PlayerList.this.sendPacketToAllPlayers(new SPacketWorldBorder(☃, SPacketWorldBorder.Action.SET_CENTER));
         }

         @Override
         public void onWarningTimeChanged(WorldBorder var1, int var2) {
            PlayerList.this.sendPacketToAllPlayers(new SPacketWorldBorder(☃, SPacketWorldBorder.Action.SET_WARNING_TIME));
         }

         @Override
         public void onWarningDistanceChanged(WorldBorder var1, int var2) {
            PlayerList.this.sendPacketToAllPlayers(new SPacketWorldBorder(☃, SPacketWorldBorder.Action.SET_WARNING_BLOCKS));
         }

         @Override
         public void onDamageAmountChanged(WorldBorder var1, double var2) {
         }

         @Override
         public void onDamageBufferChanged(WorldBorder var1, double var2) {
         }
      });
   }

   public void preparePlayer(EntityPlayerMP var1, @Nullable WorldServer var2) {
      WorldServer ☃ = ☃.getServerWorld();
      if (☃ != null) {
         ☃.getPlayerChunkMap().removePlayer(☃);
      }

      ☃.getPlayerChunkMap().addPlayer(☃);
      ☃.getChunkProvider().provideChunk((int)☃.posX >> 4, (int)☃.posZ >> 4);
      if (☃ != null) {
         CriteriaTriggers.CHANGED_DIMENSION.trigger(☃, ☃.provider.getDimensionType(), ☃.provider.getDimensionType());
         if (☃.provider.getDimensionType() == DimensionType.NETHER
            && ☃.world.provider.getDimensionType() == DimensionType.OVERWORLD
            && ☃.getEnteredNetherPosition() != null) {
            CriteriaTriggers.NETHER_TRAVEL.trigger(☃, ☃.getEnteredNetherPosition());
         }
      }
   }

   public int getEntityViewDistance() {
      return PlayerChunkMap.getFurthestViewableBlock(this.getViewDistance());
   }

   @Nullable
   public NBTTagCompound readPlayerDataFromFile(EntityPlayerMP var1) {
      NBTTagCompound ☃ = this.server.worlds[0].getWorldInfo().getPlayerNBTTagCompound();
      NBTTagCompound ☃x;
      if (☃.getName().equals(this.server.getServerOwner()) && ☃ != null) {
         ☃x = ☃;
         ☃.readFromNBT(☃);
         LOGGER.debug("loading single player");
      } else {
         ☃x = this.playerDataManager.readPlayerData(☃);
      }

      return ☃x;
   }

   protected void writePlayerData(EntityPlayerMP var1) {
      this.playerDataManager.writePlayerData(☃);
      StatisticsManagerServer ☃ = this.playerStatFiles.get(☃.getUniqueID());
      if (☃ != null) {
         ☃.saveStatFile();
      }

      PlayerAdvancements ☃x = this.advancements.get(☃.getUniqueID());
      if (☃x != null) {
         ☃x.save();
      }
   }

   public void playerLoggedIn(EntityPlayerMP var1) {
      this.playerEntityList.add(☃);
      this.uuidToPlayerMap.put(☃.getUniqueID(), ☃);
      this.sendPacketToAllPlayers(new SPacketPlayerListItem(SPacketPlayerListItem.Action.ADD_PLAYER, ☃));
      WorldServer ☃ = this.server.getWorld(☃.dimension);

      for (int ☃x = 0; ☃x < this.playerEntityList.size(); ☃x++) {
         ☃.connection.sendPacket(new SPacketPlayerListItem(SPacketPlayerListItem.Action.ADD_PLAYER, this.playerEntityList.get(☃x)));
      }

      ☃.spawnEntity(☃);
      this.preparePlayer(☃, null);
   }

   public void serverUpdateMovingPlayer(EntityPlayerMP var1) {
      ☃.getServerWorld().getPlayerChunkMap().updateMovingPlayer(☃);
   }

   public void playerLoggedOut(EntityPlayerMP var1) {
      WorldServer ☃ = ☃.getServerWorld();
      ☃.addStat(StatList.LEAVE_GAME);
      this.writePlayerData(☃);
      if (☃.isRiding()) {
         Entity ☃x = ☃.getLowestRidingEntity();
         if (☃x.getRecursivePassengersByType(EntityPlayerMP.class).size() == 1) {
            LOGGER.debug("Removing player mount");
            ☃.dismountRidingEntity();
            ☃.removeEntityDangerously(☃x);

            for (Entity ☃xx : ☃x.getRecursivePassengers()) {
               ☃.removeEntityDangerously(☃xx);
            }

            ☃.getChunk(☃.chunkCoordX, ☃.chunkCoordZ).markDirty();
         }
      }

      ☃.removeEntity(☃);
      ☃.getPlayerChunkMap().removePlayer(☃);
      ☃.getAdvancements().dispose();
      this.playerEntityList.remove(☃);
      UUID ☃x = ☃.getUniqueID();
      EntityPlayerMP ☃xx = this.uuidToPlayerMap.get(☃x);
      if (☃xx == ☃) {
         this.uuidToPlayerMap.remove(☃x);
         this.playerStatFiles.remove(☃x);
         this.advancements.remove(☃x);
      }

      this.sendPacketToAllPlayers(new SPacketPlayerListItem(SPacketPlayerListItem.Action.REMOVE_PLAYER, ☃));
   }

   public String allowUserToConnect(SocketAddress var1, GameProfile var2) {
      if (this.bannedPlayers.isBanned(☃)) {
         UserListBansEntry ☃ = this.bannedPlayers.getEntry(☃);
         String ☃x = "You are banned from this server!\nReason: " + ☃.getBanReason();
         if (☃.getBanEndDate() != null) {
            ☃x = ☃x + "\nYour ban will be removed on " + DATE_FORMAT.format(☃.getBanEndDate());
         }

         return ☃x;
      } else if (!this.canJoin(☃)) {
         return "You are not white-listed on this server!";
      } else if (this.bannedIPs.isBanned(☃)) {
         UserListIPBansEntry ☃ = this.bannedIPs.getBanEntry(☃);
         String ☃x = "Your IP address is banned from this server!\nReason: " + ☃.getBanReason();
         if (☃.getBanEndDate() != null) {
            ☃x = ☃x + "\nYour ban will be removed on " + DATE_FORMAT.format(☃.getBanEndDate());
         }

         return ☃x;
      } else {
         return this.playerEntityList.size() >= this.maxPlayers && !this.bypassesPlayerLimit(☃) ? "The server is full!" : null;
      }
   }

   public EntityPlayerMP createPlayerForUser(GameProfile var1) {
      UUID ☃ = EntityPlayer.getUUID(☃);
      List<EntityPlayerMP> ☃x = Lists.newArrayList();

      for (int ☃xx = 0; ☃xx < this.playerEntityList.size(); ☃xx++) {
         EntityPlayerMP ☃xxx = this.playerEntityList.get(☃xx);
         if (☃xxx.getUniqueID().equals(☃)) {
            ☃x.add(☃xxx);
         }
      }

      EntityPlayerMP ☃xxx = this.uuidToPlayerMap.get(☃.getId());
      if (☃xxx != null && !☃x.contains(☃xxx)) {
         ☃x.add(☃xxx);
      }

      for (EntityPlayerMP ☃xxxx : ☃x) {
         ☃xxxx.connection.disconnect(new TextComponentTranslation("multiplayer.disconnect.duplicate_login"));
      }

      PlayerInteractionManager ☃xxxx;
      if (this.server.isDemo()) {
         ☃xxxx = new DemoPlayerInteractionManager(this.server.getWorld(0));
      } else {
         ☃xxxx = new PlayerInteractionManager(this.server.getWorld(0));
      }

      return new EntityPlayerMP(this.server, this.server.getWorld(0), ☃, ☃xxxx);
   }

   public EntityPlayerMP recreatePlayerEntity(EntityPlayerMP var1, int var2, boolean var3) {
      ☃.getServerWorld().getEntityTracker().removePlayerFromTrackers(☃);
      ☃.getServerWorld().getEntityTracker().untrack(☃);
      ☃.getServerWorld().getPlayerChunkMap().removePlayer(☃);
      this.playerEntityList.remove(☃);
      this.server.getWorld(☃.dimension).removeEntityDangerously(☃);
      BlockPos ☃ = ☃.getBedLocation();
      boolean ☃x = ☃.isSpawnForced();
      ☃.dimension = ☃;
      PlayerInteractionManager ☃xx;
      if (this.server.isDemo()) {
         ☃xx = new DemoPlayerInteractionManager(this.server.getWorld(☃.dimension));
      } else {
         ☃xx = new PlayerInteractionManager(this.server.getWorld(☃.dimension));
      }

      EntityPlayerMP ☃xxx = new EntityPlayerMP(this.server, this.server.getWorld(☃.dimension), ☃.getGameProfile(), ☃xx);
      ☃xxx.connection = ☃.connection;
      ☃xxx.copyFrom(☃, ☃);
      ☃xxx.setEntityId(☃.getEntityId());
      ☃xxx.setCommandStats(☃);
      ☃xxx.setPrimaryHand(☃.getPrimaryHand());

      for (String ☃xxxx : ☃.getTags()) {
         ☃xxx.addTag(☃xxxx);
      }

      WorldServer ☃xxxx = this.server.getWorld(☃.dimension);
      this.setPlayerGameTypeBasedOnOther(☃xxx, ☃, ☃xxxx);
      if (☃ != null) {
         BlockPos ☃xxxxx = EntityPlayer.getBedSpawnLocation(this.server.getWorld(☃.dimension), ☃, ☃x);
         if (☃xxxxx != null) {
            ☃xxx.setLocationAndAngles(☃xxxxx.getX() + 0.5F, ☃xxxxx.getY() + 0.1F, ☃xxxxx.getZ() + 0.5F, 0.0F, 0.0F);
            ☃xxx.setSpawnPoint(☃, ☃x);
         } else {
            ☃xxx.connection.sendPacket(new SPacketChangeGameState(0, 0.0F));
         }
      }

      ☃xxxx.getChunkProvider().provideChunk((int)☃xxx.posX >> 4, (int)☃xxx.posZ >> 4);

      while (!☃xxxx.getCollisionBoxes(☃xxx, ☃xxx.getEntityBoundingBox()).isEmpty() && ☃xxx.posY < 256.0) {
         ☃xxx.setPosition(☃xxx.posX, ☃xxx.posY + 1.0, ☃xxx.posZ);
      }

      ☃xxx.connection
         .sendPacket(
            new SPacketRespawn(☃xxx.dimension, ☃xxx.world.getDifficulty(), ☃xxx.world.getWorldInfo().getTerrainType(), ☃xxx.interactionManager.getGameType())
         );
      BlockPos ☃xxxxx = ☃xxxx.getSpawnPoint();
      ☃xxx.connection.setPlayerLocation(☃xxx.posX, ☃xxx.posY, ☃xxx.posZ, ☃xxx.rotationYaw, ☃xxx.rotationPitch);
      ☃xxx.connection.sendPacket(new SPacketSpawnPosition(☃xxxxx));
      ☃xxx.connection.sendPacket(new SPacketSetExperience(☃xxx.experience, ☃xxx.experienceTotal, ☃xxx.experienceLevel));
      this.updateTimeAndWeatherForPlayer(☃xxx, ☃xxxx);
      this.updatePermissionLevel(☃xxx);
      ☃xxxx.getPlayerChunkMap().addPlayer(☃xxx);
      ☃xxxx.spawnEntity(☃xxx);
      this.playerEntityList.add(☃xxx);
      this.uuidToPlayerMap.put(☃xxx.getUniqueID(), ☃xxx);
      ☃xxx.addSelfToInternalCraftingInventory();
      ☃xxx.setHealth(☃xxx.getHealth());
      return ☃xxx;
   }

   public void updatePermissionLevel(EntityPlayerMP var1) {
      GameProfile ☃ = ☃.getGameProfile();
      int ☃x = this.canSendCommands(☃) ? this.ops.getPermissionLevel(☃) : 0;
      ☃x = this.server.isSinglePlayer() && this.server.worlds[0].getWorldInfo().areCommandsAllowed() ? 4 : ☃x;
      ☃x = this.commandsAllowedForAll ? 4 : ☃x;
      this.sendPlayerPermissionLevel(☃, ☃x);
   }

   public void changePlayerDimension(EntityPlayerMP var1, int var2) {
      int ☃ = ☃.dimension;
      WorldServer ☃x = this.server.getWorld(☃.dimension);
      ☃.dimension = ☃;
      WorldServer ☃xx = this.server.getWorld(☃.dimension);
      ☃.connection
         .sendPacket(new SPacketRespawn(☃.dimension, ☃.world.getDifficulty(), ☃.world.getWorldInfo().getTerrainType(), ☃.interactionManager.getGameType()));
      this.updatePermissionLevel(☃);
      ☃x.removeEntityDangerously(☃);
      ☃.isDead = false;
      this.transferEntityToWorld(☃, ☃, ☃x, ☃xx);
      this.preparePlayer(☃, ☃x);
      ☃.connection.setPlayerLocation(☃.posX, ☃.posY, ☃.posZ, ☃.rotationYaw, ☃.rotationPitch);
      ☃.interactionManager.setWorld(☃xx);
      ☃.connection.sendPacket(new SPacketPlayerAbilities(☃.capabilities));
      this.updateTimeAndWeatherForPlayer(☃, ☃xx);
      this.syncPlayerInventory(☃);

      for (PotionEffect ☃xxx : ☃.getActivePotionEffects()) {
         ☃.connection.sendPacket(new SPacketEntityEffect(☃.getEntityId(), ☃xxx));
      }
   }

   public void transferEntityToWorld(Entity var1, int var2, WorldServer var3, WorldServer var4) {
      double ☃ = ☃.posX;
      double ☃x = ☃.posZ;
      double ☃xx = 8.0;
      float ☃xxx = ☃.rotationYaw;
      ☃.profiler.startSection("moving");
      if (☃.dimension == -1) {
         ☃ = MathHelper.clamp(☃ / 8.0, ☃.getWorldBorder().minX() + 16.0, ☃.getWorldBorder().maxX() - 16.0);
         ☃x = MathHelper.clamp(☃x / 8.0, ☃.getWorldBorder().minZ() + 16.0, ☃.getWorldBorder().maxZ() - 16.0);
         ☃.setLocationAndAngles(☃, ☃.posY, ☃x, ☃.rotationYaw, ☃.rotationPitch);
         if (☃.isEntityAlive()) {
            ☃.updateEntityWithOptionalForce(☃, false);
         }
      } else if (☃.dimension == 0) {
         ☃ = MathHelper.clamp(☃ * 8.0, ☃.getWorldBorder().minX() + 16.0, ☃.getWorldBorder().maxX() - 16.0);
         ☃x = MathHelper.clamp(☃x * 8.0, ☃.getWorldBorder().minZ() + 16.0, ☃.getWorldBorder().maxZ() - 16.0);
         ☃.setLocationAndAngles(☃, ☃.posY, ☃x, ☃.rotationYaw, ☃.rotationPitch);
         if (☃.isEntityAlive()) {
            ☃.updateEntityWithOptionalForce(☃, false);
         }
      } else {
         BlockPos ☃xxxx;
         if (☃ == 1) {
            ☃xxxx = ☃.getSpawnPoint();
         } else {
            ☃xxxx = ☃.getSpawnCoordinate();
         }

         ☃ = ☃xxxx.getX();
         ☃.posY = ☃xxxx.getY();
         ☃x = ☃xxxx.getZ();
         ☃.setLocationAndAngles(☃, ☃.posY, ☃x, 90.0F, 0.0F);
         if (☃.isEntityAlive()) {
            ☃.updateEntityWithOptionalForce(☃, false);
         }
      }

      ☃.profiler.endSection();
      if (☃ != 1) {
         ☃.profiler.startSection("placing");
         ☃ = MathHelper.clamp((int)☃, -29999872, 29999872);
         ☃x = MathHelper.clamp((int)☃x, -29999872, 29999872);
         if (☃.isEntityAlive()) {
            ☃.setLocationAndAngles(☃, ☃.posY, ☃x, ☃.rotationYaw, ☃.rotationPitch);
            ☃.getDefaultTeleporter().placeInPortal(☃, ☃xxx);
            ☃.spawnEntity(☃);
            ☃.updateEntityWithOptionalForce(☃, false);
         }

         ☃.profiler.endSection();
      }

      ☃.setWorld(☃);
   }

   public void onTick() {
      if (++this.playerPingIndex > 600) {
         this.sendPacketToAllPlayers(new SPacketPlayerListItem(SPacketPlayerListItem.Action.UPDATE_LATENCY, this.playerEntityList));
         this.playerPingIndex = 0;
      }
   }

   public void sendPacketToAllPlayers(Packet<?> var1) {
      for (int ☃ = 0; ☃ < this.playerEntityList.size(); ☃++) {
         this.playerEntityList.get(☃).connection.sendPacket(☃);
      }
   }

   public void sendPacketToAllPlayersInDimension(Packet<?> var1, int var2) {
      for (int ☃ = 0; ☃ < this.playerEntityList.size(); ☃++) {
         EntityPlayerMP ☃x = this.playerEntityList.get(☃);
         if (☃x.dimension == ☃) {
            ☃x.connection.sendPacket(☃);
         }
      }
   }

   public void sendMessageToAllTeamMembers(EntityPlayer var1, ITextComponent var2) {
      Team ☃ = ☃.getTeam();
      if (☃ != null) {
         for (String ☃x : ☃.getMembershipCollection()) {
            EntityPlayerMP ☃xx = this.getPlayerByUsername(☃x);
            if (☃xx != null && ☃xx != ☃) {
               ☃xx.sendMessage(☃);
            }
         }
      }
   }

   public void sendMessageToTeamOrAllPlayers(EntityPlayer var1, ITextComponent var2) {
      Team ☃ = ☃.getTeam();
      if (☃ == null) {
         this.sendMessage(☃);
      } else {
         for (int ☃x = 0; ☃x < this.playerEntityList.size(); ☃x++) {
            EntityPlayerMP ☃xx = this.playerEntityList.get(☃x);
            if (☃xx.getTeam() != ☃) {
               ☃xx.sendMessage(☃);
            }
         }
      }
   }

   public String getFormattedListOfPlayers(boolean var1) {
      String ☃ = "";
      List<EntityPlayerMP> ☃x = Lists.newArrayList(this.playerEntityList);

      for (int ☃xx = 0; ☃xx < ☃x.size(); ☃xx++) {
         if (☃xx > 0) {
            ☃ = ☃ + ", ";
         }

         ☃ = ☃ + ☃x.get(☃xx).getName();
         if (☃) {
            ☃ = ☃ + " (" + ☃x.get(☃xx).getCachedUniqueIdString() + ")";
         }
      }

      return ☃;
   }

   public String[] getOnlinePlayerNames() {
      String[] ☃ = new String[this.playerEntityList.size()];

      for (int ☃x = 0; ☃x < this.playerEntityList.size(); ☃x++) {
         ☃[☃x] = this.playerEntityList.get(☃x).getName();
      }

      return ☃;
   }

   public GameProfile[] getOnlinePlayerProfiles() {
      GameProfile[] ☃ = new GameProfile[this.playerEntityList.size()];

      for (int ☃x = 0; ☃x < this.playerEntityList.size(); ☃x++) {
         ☃[☃x] = this.playerEntityList.get(☃x).getGameProfile();
      }

      return ☃;
   }

   public UserListBans getBannedPlayers() {
      return this.bannedPlayers;
   }

   public UserListIPBans getBannedIPs() {
      return this.bannedIPs;
   }

   public void addOp(GameProfile var1) {
      int ☃ = this.server.getOpPermissionLevel();
      this.ops.addEntry(new UserListOpsEntry(☃, this.server.getOpPermissionLevel(), this.ops.bypassesPlayerLimit(☃)));
      this.sendPlayerPermissionLevel(this.getPlayerByUUID(☃.getId()), ☃);
   }

   public void removeOp(GameProfile var1) {
      this.ops.removeEntry(☃);
      this.sendPlayerPermissionLevel(this.getPlayerByUUID(☃.getId()), 0);
   }

   private void sendPlayerPermissionLevel(EntityPlayerMP var1, int var2) {
      if (☃ != null && ☃.connection != null) {
         byte ☃;
         if (☃ <= 0) {
            ☃ = 24;
         } else if (☃ >= 4) {
            ☃ = 28;
         } else {
            ☃ = (byte)(24 + ☃);
         }

         ☃.connection.sendPacket(new SPacketEntityStatus(☃, ☃));
      }
   }

   public boolean canJoin(GameProfile var1) {
      return !this.whiteListEnforced || this.ops.hasEntry(☃) || this.whiteListedPlayers.hasEntry(☃);
   }

   public boolean canSendCommands(GameProfile var1) {
      return this.ops.hasEntry(☃)
         || this.server.isSinglePlayer()
            && this.server.worlds[0].getWorldInfo().areCommandsAllowed()
            && this.server.getServerOwner().equalsIgnoreCase(☃.getName())
         || this.commandsAllowedForAll;
   }

   @Nullable
   public EntityPlayerMP getPlayerByUsername(String var1) {
      for (EntityPlayerMP ☃ : this.playerEntityList) {
         if (☃.getName().equalsIgnoreCase(☃)) {
            return ☃;
         }
      }

      return null;
   }

   public void sendToAllNearExcept(@Nullable EntityPlayer var1, double var2, double var4, double var6, double var8, int var10, Packet<?> var11) {
      for (int ☃ = 0; ☃ < this.playerEntityList.size(); ☃++) {
         EntityPlayerMP ☃x = this.playerEntityList.get(☃);
         if (☃x != ☃ && ☃x.dimension == ☃) {
            double ☃xx = ☃ - ☃x.posX;
            double ☃xxx = ☃ - ☃x.posY;
            double ☃xxxx = ☃ - ☃x.posZ;
            if (☃xx * ☃xx + ☃xxx * ☃xxx + ☃xxxx * ☃xxxx < ☃ * ☃) {
               ☃x.connection.sendPacket(☃);
            }
         }
      }
   }

   public void saveAllPlayerData() {
      for (int ☃ = 0; ☃ < this.playerEntityList.size(); ☃++) {
         this.writePlayerData(this.playerEntityList.get(☃));
      }
   }

   public void addWhitelistedPlayer(GameProfile var1) {
      this.whiteListedPlayers.addEntry(new UserListWhitelistEntry(☃));
   }

   public void removePlayerFromWhitelist(GameProfile var1) {
      this.whiteListedPlayers.removeEntry(☃);
   }

   public UserListWhitelist getWhitelistedPlayers() {
      return this.whiteListedPlayers;
   }

   public String[] getWhitelistedPlayerNames() {
      return this.whiteListedPlayers.getKeys();
   }

   public UserListOps getOppedPlayers() {
      return this.ops;
   }

   public String[] getOppedPlayerNames() {
      return this.ops.getKeys();
   }

   public void reloadWhitelist() {
   }

   public void updateTimeAndWeatherForPlayer(EntityPlayerMP var1, WorldServer var2) {
      WorldBorder ☃ = this.server.worlds[0].getWorldBorder();
      ☃.connection.sendPacket(new SPacketWorldBorder(☃, SPacketWorldBorder.Action.INITIALIZE));
      ☃.connection.sendPacket(new SPacketTimeUpdate(☃.getTotalWorldTime(), ☃.getWorldTime(), ☃.getGameRules().getBoolean("doDaylightCycle")));
      BlockPos ☃x = ☃.getSpawnPoint();
      ☃.connection.sendPacket(new SPacketSpawnPosition(☃x));
      if (☃.isRaining()) {
         ☃.connection.sendPacket(new SPacketChangeGameState(1, 0.0F));
         ☃.connection.sendPacket(new SPacketChangeGameState(7, ☃.getRainStrength(1.0F)));
         ☃.connection.sendPacket(new SPacketChangeGameState(8, ☃.getThunderStrength(1.0F)));
      }
   }

   public void syncPlayerInventory(EntityPlayerMP var1) {
      ☃.sendContainerToPlayer(☃.inventoryContainer);
      ☃.setPlayerHealthUpdated();
      ☃.connection.sendPacket(new SPacketHeldItemChange(☃.inventory.currentItem));
   }

   public int getCurrentPlayerCount() {
      return this.playerEntityList.size();
   }

   public int getMaxPlayers() {
      return this.maxPlayers;
   }

   public String[] getAvailablePlayerDat() {
      return this.server.worlds[0].getSaveHandler().getPlayerNBTManager().getAvailablePlayerDat();
   }

   public void setWhiteListEnabled(boolean var1) {
      this.whiteListEnforced = ☃;
   }

   public List<EntityPlayerMP> getPlayersMatchingAddress(String var1) {
      List<EntityPlayerMP> ☃ = Lists.newArrayList();

      for (EntityPlayerMP ☃x : this.playerEntityList) {
         if (☃x.getPlayerIP().equals(☃)) {
            ☃.add(☃x);
         }
      }

      return ☃;
   }

   public int getViewDistance() {
      return this.viewDistance;
   }

   public MinecraftServer getServerInstance() {
      return this.server;
   }

   public NBTTagCompound getHostPlayerData() {
      return null;
   }

   public void setGameType(GameType var1) {
      this.gameType = ☃;
   }

   private void setPlayerGameTypeBasedOnOther(EntityPlayerMP var1, EntityPlayerMP var2, World var3) {
      if (☃ != null) {
         ☃.interactionManager.setGameType(☃.interactionManager.getGameType());
      } else if (this.gameType != null) {
         ☃.interactionManager.setGameType(this.gameType);
      }

      ☃.interactionManager.initializeGameType(☃.getWorldInfo().getGameType());
   }

   public void setCommandsAllowedForAll(boolean var1) {
      this.commandsAllowedForAll = ☃;
   }

   public void removeAllPlayers() {
      for (int ☃ = 0; ☃ < this.playerEntityList.size(); ☃++) {
         this.playerEntityList.get(☃).connection.disconnect(new TextComponentTranslation("multiplayer.disconnect.server_shutdown"));
      }
   }

   public void sendMessage(ITextComponent var1, boolean var2) {
      this.server.sendMessage(☃);
      ChatType ☃ = ☃ ? ChatType.SYSTEM : ChatType.CHAT;
      this.sendPacketToAllPlayers(new SPacketChat(☃, ☃));
   }

   public void sendMessage(ITextComponent var1) {
      this.sendMessage(☃, true);
   }

   public StatisticsManagerServer getPlayerStatsFile(EntityPlayer var1) {
      UUID ☃ = ☃.getUniqueID();
      StatisticsManagerServer ☃x = ☃ == null ? null : this.playerStatFiles.get(☃);
      if (☃x == null) {
         File ☃xx = new File(this.server.getWorld(0).getSaveHandler().getWorldDirectory(), "stats");
         File ☃xxx = new File(☃xx, ☃ + ".json");
         if (!☃xxx.exists()) {
            File ☃xxxx = new File(☃xx, ☃.getName() + ".json");
            if (☃xxxx.exists() && ☃xxxx.isFile()) {
               ☃xxxx.renameTo(☃xxx);
            }
         }

         ☃x = new StatisticsManagerServer(this.server, ☃xxx);
         ☃x.readStatFile();
         this.playerStatFiles.put(☃, ☃x);
      }

      return ☃x;
   }

   public PlayerAdvancements getPlayerAdvancements(EntityPlayerMP var1) {
      UUID ☃ = ☃.getUniqueID();
      PlayerAdvancements ☃x = this.advancements.get(☃);
      if (☃x == null) {
         File ☃xx = new File(this.server.getWorld(0).getSaveHandler().getWorldDirectory(), "advancements");
         File ☃xxx = new File(☃xx, ☃ + ".json");
         ☃x = new PlayerAdvancements(this.server, ☃xxx, ☃);
         this.advancements.put(☃, ☃x);
      }

      ☃x.setPlayer(☃);
      return ☃x;
   }

   public void setViewDistance(int var1) {
      this.viewDistance = ☃;
      if (this.server.worlds != null) {
         for (WorldServer ☃ : this.server.worlds) {
            if (☃ != null) {
               ☃.getPlayerChunkMap().setPlayerViewRadius(☃);
               ☃.getEntityTracker().setViewDistance(☃);
            }
         }
      }
   }

   public List<EntityPlayerMP> getPlayers() {
      return this.playerEntityList;
   }

   public EntityPlayerMP getPlayerByUUID(UUID var1) {
      return this.uuidToPlayerMap.get(☃);
   }

   public boolean bypassesPlayerLimit(GameProfile var1) {
      return false;
   }

   public void reloadResources() {
      for (PlayerAdvancements ☃ : this.advancements.values()) {
         ☃.reload();
      }
   }
}
