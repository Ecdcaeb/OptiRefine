package net.minecraft.entity.player;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMapBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketCamera;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketSignEditorOpen;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.network.play.server.SPacketWindowProperty;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraft.stats.RecipeBookServer;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.CooldownTrackerServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.GameType;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.ILootContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityPlayerMP extends EntityPlayer implements IContainerListener {
   private static final Logger LOGGER = LogManager.getLogger();
   private String language = "en_US";
   public NetHandlerPlayServer connection;
   public final MinecraftServer server;
   public final PlayerInteractionManager interactionManager;
   public double managedPosX;
   public double managedPosZ;
   private final List<Integer> entityRemoveQueue = Lists.newLinkedList();
   private final PlayerAdvancements advancements;
   private final StatisticsManagerServer statsFile;
   private float lastHealthScore = Float.MIN_VALUE;
   private int lastFoodScore = Integer.MIN_VALUE;
   private int lastAirScore = Integer.MIN_VALUE;
   private int lastArmorScore = Integer.MIN_VALUE;
   private int lastLevelScore = Integer.MIN_VALUE;
   private int lastExperienceScore = Integer.MIN_VALUE;
   private float lastHealth = -1.0E8F;
   private int lastFoodLevel = -99999999;
   private boolean wasHungry = true;
   private int lastExperience = -99999999;
   private int respawnInvulnerabilityTicks = 60;
   private EntityPlayer.EnumChatVisibility chatVisibility;
   private boolean chatColours = true;
   private long playerLastActiveTime = System.currentTimeMillis();
   private Entity spectatingEntity;
   private boolean invulnerableDimensionChange;
   private boolean seenCredits;
   private final RecipeBookServer recipeBook = new RecipeBookServer();
   private Vec3d levitationStartPos;
   private int levitatingSince;
   private boolean disconnected;
   private Vec3d enteredNetherPosition;
   private int currentWindowId;
   public boolean isChangingQuantityOnly;
   public int ping;
   public boolean queuedEndExit;

   public EntityPlayerMP(MinecraftServer var1, WorldServer var2, GameProfile var3, PlayerInteractionManager var4) {
      super(☃, ☃);
      ☃.player = this;
      this.interactionManager = ☃;
      BlockPos ☃ = ☃.getSpawnPoint();
      if (☃.provider.hasSkyLight() && ☃.getWorldInfo().getGameType() != GameType.ADVENTURE) {
         int ☃x = Math.max(0, ☃.getSpawnRadius(☃));
         int ☃xx = MathHelper.floor(☃.getWorldBorder().getClosestDistance(☃.getX(), ☃.getZ()));
         if (☃xx < ☃x) {
            ☃x = ☃xx;
         }

         if (☃xx <= 1) {
            ☃x = 1;
         }

         ☃ = ☃.getTopSolidOrLiquidBlock(☃.add(this.rand.nextInt(☃x * 2 + 1) - ☃x, 0, this.rand.nextInt(☃x * 2 + 1) - ☃x));
      }

      this.server = ☃;
      this.statsFile = ☃.getPlayerList().getPlayerStatsFile(this);
      this.advancements = ☃.getPlayerList().getPlayerAdvancements(this);
      this.stepHeight = 1.0F;
      this.moveToBlockPosAndAngles(☃, 0.0F, 0.0F);

      while (!☃.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && this.posY < 255.0) {
         this.setPosition(this.posX, this.posY + 1.0, this.posZ);
      }
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      if (☃.hasKey("playerGameType", 99)) {
         if (this.getServer().getForceGamemode()) {
            this.interactionManager.setGameType(this.getServer().getGameType());
         } else {
            this.interactionManager.setGameType(GameType.getByID(☃.getInteger("playerGameType")));
         }
      }

      if (☃.hasKey("enteredNetherPosition", 10)) {
         NBTTagCompound ☃ = ☃.getCompoundTag("enteredNetherPosition");
         this.enteredNetherPosition = new Vec3d(☃.getDouble("x"), ☃.getDouble("y"), ☃.getDouble("z"));
      }

      this.seenCredits = ☃.getBoolean("seenCredits");
      if (☃.hasKey("recipeBook", 10)) {
         this.recipeBook.read(☃.getCompoundTag("recipeBook"));
      }
   }

   public static void registerFixesPlayerMP(DataFixer var0) {
      ☃.registerWalker(FixTypes.PLAYER, new IDataWalker() {
         @Override
         public NBTTagCompound process(IDataFixer var1, NBTTagCompound var2, int var3) {
            if (☃.hasKey("RootVehicle", 10)) {
               NBTTagCompound ☃ = ☃.getCompoundTag("RootVehicle");
               if (☃.hasKey("Entity", 10)) {
                  ☃.setTag("Entity", ☃.process(FixTypes.ENTITY, ☃.getCompoundTag("Entity"), ☃));
               }
            }

            return ☃;
         }
      });
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setInteger("playerGameType", this.interactionManager.getGameType().getID());
      ☃.setBoolean("seenCredits", this.seenCredits);
      if (this.enteredNetherPosition != null) {
         NBTTagCompound ☃ = new NBTTagCompound();
         ☃.setDouble("x", this.enteredNetherPosition.x);
         ☃.setDouble("y", this.enteredNetherPosition.y);
         ☃.setDouble("z", this.enteredNetherPosition.z);
         ☃.setTag("enteredNetherPosition", ☃);
      }

      Entity ☃ = this.getLowestRidingEntity();
      Entity ☃x = this.getRidingEntity();
      if (☃x != null && ☃ != this && ☃.getRecursivePassengersByType(EntityPlayerMP.class).size() == 1) {
         NBTTagCompound ☃xx = new NBTTagCompound();
         NBTTagCompound ☃xxx = new NBTTagCompound();
         ☃.writeToNBTOptional(☃xxx);
         ☃xx.setUniqueId("Attach", ☃x.getUniqueID());
         ☃xx.setTag("Entity", ☃xxx);
         ☃.setTag("RootVehicle", ☃xx);
      }

      ☃.setTag("recipeBook", this.recipeBook.write());
   }

   @Override
   public void addExperienceLevel(int var1) {
      super.addExperienceLevel(☃);
      this.lastExperience = -1;
   }

   @Override
   public void onEnchant(ItemStack var1, int var2) {
      super.onEnchant(☃, ☃);
      this.lastExperience = -1;
   }

   public void addSelfToInternalCraftingInventory() {
      this.openContainer.addListener(this);
   }

   @Override
   public void sendEnterCombat() {
      super.sendEnterCombat();
      this.connection.sendPacket(new SPacketCombatEvent(this.getCombatTracker(), SPacketCombatEvent.Event.ENTER_COMBAT));
   }

   @Override
   public void sendEndCombat() {
      super.sendEndCombat();
      this.connection.sendPacket(new SPacketCombatEvent(this.getCombatTracker(), SPacketCombatEvent.Event.END_COMBAT));
   }

   @Override
   protected void onInsideBlock(IBlockState var1) {
      CriteriaTriggers.ENTER_BLOCK.trigger(this, ☃);
   }

   @Override
   protected CooldownTracker createCooldownTracker() {
      return new CooldownTrackerServer(this);
   }

   @Override
   public void onUpdate() {
      this.interactionManager.updateBlockRemoving();
      this.respawnInvulnerabilityTicks--;
      if (this.hurtResistantTime > 0) {
         this.hurtResistantTime--;
      }

      this.openContainer.detectAndSendChanges();
      if (!this.world.isRemote && !this.openContainer.canInteractWith(this)) {
         this.closeScreen();
         this.openContainer = this.inventoryContainer;
      }

      while (!this.entityRemoveQueue.isEmpty()) {
         int ☃ = Math.min(this.entityRemoveQueue.size(), Integer.MAX_VALUE);
         int[] ☃x = new int[☃];
         Iterator<Integer> ☃xx = this.entityRemoveQueue.iterator();
         int ☃xxx = 0;

         while (☃xx.hasNext() && ☃xxx < ☃) {
            ☃x[☃xxx++] = ☃xx.next();
            ☃xx.remove();
         }

         this.connection.sendPacket(new SPacketDestroyEntities(☃x));
      }

      Entity ☃ = this.getSpectatingEntity();
      if (☃ != this) {
         if (☃.isEntityAlive()) {
            this.setPositionAndRotation(☃.posX, ☃.posY, ☃.posZ, ☃.rotationYaw, ☃.rotationPitch);
            this.server.getPlayerList().serverUpdateMovingPlayer(this);
            if (this.isSneaking()) {
               this.setSpectatingEntity(this);
            }
         } else {
            this.setSpectatingEntity(this);
         }
      }

      CriteriaTriggers.TICK.trigger(this);
      if (this.levitationStartPos != null) {
         CriteriaTriggers.LEVITATION.trigger(this, this.levitationStartPos, this.ticksExisted - this.levitatingSince);
      }

      this.advancements.flushDirty(this);
   }

   public void onUpdateEntity() {
      try {
         super.onUpdate();

         for (int ☃ = 0; ☃ < this.inventory.getSizeInventory(); ☃++) {
            ItemStack ☃x = this.inventory.getStackInSlot(☃);
            if (!☃x.isEmpty() && ☃x.getItem().isMap()) {
               Packet<?> ☃xx = ((ItemMapBase)☃x.getItem()).createMapDataPacket(☃x, this.world, this);
               if (☃xx != null) {
                  this.connection.sendPacket(☃xx);
               }
            }
         }

         if (this.getHealth() != this.lastHealth
            || this.lastFoodLevel != this.foodStats.getFoodLevel()
            || this.foodStats.getSaturationLevel() == 0.0F != this.wasHungry) {
            this.connection.sendPacket(new SPacketUpdateHealth(this.getHealth(), this.foodStats.getFoodLevel(), this.foodStats.getSaturationLevel()));
            this.lastHealth = this.getHealth();
            this.lastFoodLevel = this.foodStats.getFoodLevel();
            this.wasHungry = this.foodStats.getSaturationLevel() == 0.0F;
         }

         if (this.getHealth() + this.getAbsorptionAmount() != this.lastHealthScore) {
            this.lastHealthScore = this.getHealth() + this.getAbsorptionAmount();
            this.updateScorePoints(IScoreCriteria.HEALTH, MathHelper.ceil(this.lastHealthScore));
         }

         if (this.foodStats.getFoodLevel() != this.lastFoodScore) {
            this.lastFoodScore = this.foodStats.getFoodLevel();
            this.updateScorePoints(IScoreCriteria.FOOD, MathHelper.ceil((float)this.lastFoodScore));
         }

         if (this.getAir() != this.lastAirScore) {
            this.lastAirScore = this.getAir();
            this.updateScorePoints(IScoreCriteria.AIR, MathHelper.ceil((float)this.lastAirScore));
         }

         if (this.getTotalArmorValue() != this.lastArmorScore) {
            this.lastArmorScore = this.getTotalArmorValue();
            this.updateScorePoints(IScoreCriteria.ARMOR, MathHelper.ceil((float)this.lastArmorScore));
         }

         if (this.experienceTotal != this.lastExperienceScore) {
            this.lastExperienceScore = this.experienceTotal;
            this.updateScorePoints(IScoreCriteria.XP, MathHelper.ceil((float)this.lastExperienceScore));
         }

         if (this.experienceLevel != this.lastLevelScore) {
            this.lastLevelScore = this.experienceLevel;
            this.updateScorePoints(IScoreCriteria.LEVEL, MathHelper.ceil((float)this.lastLevelScore));
         }

         if (this.experienceTotal != this.lastExperience) {
            this.lastExperience = this.experienceTotal;
            this.connection.sendPacket(new SPacketSetExperience(this.experience, this.experienceTotal, this.experienceLevel));
         }

         if (this.ticksExisted % 20 == 0) {
            CriteriaTriggers.LOCATION.trigger(this);
         }
      } catch (Throwable var4) {
         CrashReport ☃x = CrashReport.makeCrashReport(var4, "Ticking player");
         CrashReportCategory ☃xx = ☃x.makeCategory("Player being ticked");
         this.addEntityCrashInfo(☃xx);
         throw new ReportedException(☃x);
      }
   }

   private void updateScorePoints(IScoreCriteria var1, int var2) {
      for (ScoreObjective ☃ : this.getWorldScoreboard().getObjectivesFromCriteria(☃)) {
         Score ☃x = this.getWorldScoreboard().getOrCreateScore(this.getName(), ☃);
         ☃x.setScorePoints(☃);
      }
   }

   @Override
   public void onDeath(DamageSource var1) {
      boolean ☃ = this.world.getGameRules().getBoolean("showDeathMessages");
      this.connection.sendPacket(new SPacketCombatEvent(this.getCombatTracker(), SPacketCombatEvent.Event.ENTITY_DIED, ☃));
      if (☃) {
         Team ☃x = this.getTeam();
         if (☃x == null || ☃x.getDeathMessageVisibility() == Team.EnumVisible.ALWAYS) {
            this.server.getPlayerList().sendMessage(this.getCombatTracker().getDeathMessage());
         } else if (☃x.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OTHER_TEAMS) {
            this.server.getPlayerList().sendMessageToAllTeamMembers(this, this.getCombatTracker().getDeathMessage());
         } else if (☃x.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OWN_TEAM) {
            this.server.getPlayerList().sendMessageToTeamOrAllPlayers(this, this.getCombatTracker().getDeathMessage());
         }
      }

      this.spawnShoulderEntities();
      if (!this.world.getGameRules().getBoolean("keepInventory") && !this.isSpectator()) {
         this.destroyVanishingCursedItems();
         this.inventory.dropAllItems();
      }

      for (ScoreObjective ☃x : this.world.getScoreboard().getObjectivesFromCriteria(IScoreCriteria.DEATH_COUNT)) {
         Score ☃xx = this.getWorldScoreboard().getOrCreateScore(this.getName(), ☃x);
         ☃xx.incrementScore();
      }

      EntityLivingBase ☃x = this.getAttackingEntity();
      if (☃x != null) {
         EntityList.EntityEggInfo ☃xx = EntityList.ENTITY_EGGS.get(EntityList.getKey(☃x));
         if (☃xx != null) {
            this.addStat(☃xx.entityKilledByStat);
         }

         ☃x.awardKillScore(this, this.scoreValue, ☃);
      }

      this.addStat(StatList.DEATHS);
      this.takeStat(StatList.TIME_SINCE_DEATH);
      this.extinguish();
      this.setFlag(0, false);
      this.getCombatTracker().reset();
   }

   @Override
   public void awardKillScore(Entity var1, int var2, DamageSource var3) {
      if (☃ != this) {
         super.awardKillScore(☃, ☃, ☃);
         this.addScore(☃);
         Collection<ScoreObjective> ☃ = this.getWorldScoreboard().getObjectivesFromCriteria(IScoreCriteria.TOTAL_KILL_COUNT);
         if (☃ instanceof EntityPlayer) {
            this.addStat(StatList.PLAYER_KILLS);
            ☃.addAll(this.getWorldScoreboard().getObjectivesFromCriteria(IScoreCriteria.PLAYER_KILL_COUNT));
         } else {
            this.addStat(StatList.MOB_KILLS);
         }

         ☃.addAll(this.awardTeamKillScores(☃));

         for (ScoreObjective ☃x : ☃) {
            this.getWorldScoreboard().getOrCreateScore(this.getName(), ☃x).incrementScore();
         }

         CriteriaTriggers.PLAYER_KILLED_ENTITY.trigger(this, ☃, ☃);
      }
   }

   private Collection<ScoreObjective> awardTeamKillScores(Entity var1) {
      String ☃ = ☃ instanceof EntityPlayer ? ☃.getName() : ☃.getCachedUniqueIdString();
      ScorePlayerTeam ☃x = this.getWorldScoreboard().getPlayersTeam(this.getName());
      if (☃x != null) {
         int ☃xx = ☃x.getColor().getColorIndex();
         if (☃xx >= 0 && ☃xx < IScoreCriteria.KILLED_BY_TEAM.length) {
            for (ScoreObjective ☃xxx : this.getWorldScoreboard().getObjectivesFromCriteria(IScoreCriteria.KILLED_BY_TEAM[☃xx])) {
               Score ☃xxxx = this.getWorldScoreboard().getOrCreateScore(☃, ☃xxx);
               ☃xxxx.incrementScore();
            }
         }
      }

      ScorePlayerTeam ☃xx = this.getWorldScoreboard().getPlayersTeam(☃);
      if (☃xx != null) {
         int ☃xxx = ☃xx.getColor().getColorIndex();
         if (☃xxx >= 0 && ☃xxx < IScoreCriteria.TEAM_KILL.length) {
            return this.getWorldScoreboard().getObjectivesFromCriteria(IScoreCriteria.TEAM_KILL[☃xxx]);
         }
      }

      return Lists.newArrayList();
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, float var2) {
      if (this.isEntityInvulnerable(☃)) {
         return false;
      } else {
         boolean ☃ = this.server.isDedicatedServer() && this.canPlayersAttack() && "fall".equals(☃.damageType);
         if (!☃ && this.respawnInvulnerabilityTicks > 0 && ☃ != DamageSource.OUT_OF_WORLD) {
            return false;
         } else {
            if (☃ instanceof EntityDamageSource) {
               Entity ☃x = ☃.getTrueSource();
               if (☃x instanceof EntityPlayer && !this.canAttackPlayer((EntityPlayer)☃x)) {
                  return false;
               }

               if (☃x instanceof EntityArrow) {
                  EntityArrow ☃xx = (EntityArrow)☃x;
                  if (☃xx.shootingEntity instanceof EntityPlayer && !this.canAttackPlayer((EntityPlayer)☃xx.shootingEntity)) {
                     return false;
                  }
               }
            }

            return super.attackEntityFrom(☃, ☃);
         }
      }
   }

   @Override
   public boolean canAttackPlayer(EntityPlayer var1) {
      return !this.canPlayersAttack() ? false : super.canAttackPlayer(☃);
   }

   private boolean canPlayersAttack() {
      return this.server.isPVPEnabled();
   }

   @Nullable
   @Override
   public Entity changeDimension(int var1) {
      this.invulnerableDimensionChange = true;
      if (this.dimension == 0 && ☃ == -1) {
         this.enteredNetherPosition = new Vec3d(this.posX, this.posY, this.posZ);
      } else if (this.dimension != -1 && ☃ != 0) {
         this.enteredNetherPosition = null;
      }

      if (this.dimension == 1 && ☃ == 1) {
         this.world.removeEntity(this);
         if (!this.queuedEndExit) {
            this.queuedEndExit = true;
            this.connection.sendPacket(new SPacketChangeGameState(4, this.seenCredits ? 0.0F : 1.0F));
            this.seenCredits = true;
         }

         return this;
      } else {
         if (this.dimension == 0 && ☃ == 1) {
            ☃ = 1;
         }

         this.server.getPlayerList().changePlayerDimension(this, ☃);
         this.connection.sendPacket(new SPacketEffect(1032, BlockPos.ORIGIN, 0, false));
         this.lastExperience = -1;
         this.lastHealth = -1.0F;
         this.lastFoodLevel = -1;
         return this;
      }
   }

   @Override
   public boolean isSpectatedByPlayer(EntityPlayerMP var1) {
      if (☃.isSpectator()) {
         return this.getSpectatingEntity() == this;
      } else {
         return this.isSpectator() ? false : super.isSpectatedByPlayer(☃);
      }
   }

   private void sendTileEntityUpdate(TileEntity var1) {
      if (☃ != null) {
         SPacketUpdateTileEntity ☃ = ☃.getUpdatePacket();
         if (☃ != null) {
            this.connection.sendPacket(☃);
         }
      }
   }

   @Override
   public void onItemPickup(Entity var1, int var2) {
      super.onItemPickup(☃, ☃);
      this.openContainer.detectAndSendChanges();
   }

   @Override
   public EntityPlayer.SleepResult trySleep(BlockPos var1) {
      EntityPlayer.SleepResult ☃ = super.trySleep(☃);
      if (☃ == EntityPlayer.SleepResult.OK) {
         this.addStat(StatList.SLEEP_IN_BED);
         Packet<?> ☃x = new SPacketUseBed(this, ☃);
         this.getServerWorld().getEntityTracker().sendToTracking(this, ☃x);
         this.connection.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
         this.connection.sendPacket(☃x);
         CriteriaTriggers.SLEPT_IN_BED.trigger(this);
      }

      return ☃;
   }

   @Override
   public void wakeUpPlayer(boolean var1, boolean var2, boolean var3) {
      if (this.isPlayerSleeping()) {
         this.getServerWorld().getEntityTracker().sendToTrackingAndSelf(this, new SPacketAnimation(this, 2));
      }

      super.wakeUpPlayer(☃, ☃, ☃);
      if (this.connection != null) {
         this.connection.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
      }
   }

   @Override
   public boolean startRiding(Entity var1, boolean var2) {
      Entity ☃ = this.getRidingEntity();
      if (!super.startRiding(☃, ☃)) {
         return false;
      } else {
         Entity ☃x = this.getRidingEntity();
         if (☃x != ☃ && this.connection != null) {
            this.connection.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
         }

         return true;
      }
   }

   @Override
   public void dismountRidingEntity() {
      Entity ☃ = this.getRidingEntity();
      super.dismountRidingEntity();
      Entity ☃x = this.getRidingEntity();
      if (☃x != ☃ && this.connection != null) {
         this.connection.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
      }
   }

   @Override
   public boolean isEntityInvulnerable(DamageSource var1) {
      return super.isEntityInvulnerable(☃) || this.isInvulnerableDimensionChange();
   }

   @Override
   protected void updateFallState(double var1, boolean var3, IBlockState var4, BlockPos var5) {
   }

   @Override
   protected void frostWalk(BlockPos var1) {
      if (!this.isSpectator()) {
         super.frostWalk(☃);
      }
   }

   public void handleFalling(double var1, boolean var3) {
      int ☃ = MathHelper.floor(this.posX);
      int ☃x = MathHelper.floor(this.posY - 0.2F);
      int ☃xx = MathHelper.floor(this.posZ);
      BlockPos ☃xxx = new BlockPos(☃, ☃x, ☃xx);
      IBlockState ☃xxxx = this.world.getBlockState(☃xxx);
      if (☃xxxx.getMaterial() == Material.AIR) {
         BlockPos ☃xxxxx = ☃xxx.down();
         IBlockState ☃xxxxxx = this.world.getBlockState(☃xxxxx);
         Block ☃xxxxxxx = ☃xxxxxx.getBlock();
         if (☃xxxxxxx instanceof BlockFence || ☃xxxxxxx instanceof BlockWall || ☃xxxxxxx instanceof BlockFenceGate) {
            ☃xxx = ☃xxxxx;
            ☃xxxx = ☃xxxxxx;
         }
      }

      super.updateFallState(☃, ☃, ☃xxxx, ☃xxx);
   }

   @Override
   public void openEditSign(TileEntitySign var1) {
      ☃.setPlayer(this);
      this.connection.sendPacket(new SPacketSignEditorOpen(☃.getPos()));
   }

   private void getNextWindowId() {
      this.currentWindowId = this.currentWindowId % 100 + 1;
   }

   @Override
   public void displayGui(IInteractionObject var1) {
      if (☃ instanceof ILootContainer && ((ILootContainer)☃).getLootTable() != null && this.isSpectator()) {
         this.sendStatusMessage(new TextComponentTranslation("container.spectatorCantOpen").setStyle(new Style().setColor(TextFormatting.RED)), true);
      } else {
         this.getNextWindowId();
         this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, ☃.getGuiID(), ☃.getDisplayName()));
         this.openContainer = ☃.createContainer(this.inventory, this);
         this.openContainer.windowId = this.currentWindowId;
         this.openContainer.addListener(this);
      }
   }

   @Override
   public void displayGUIChest(IInventory var1) {
      if (☃ instanceof ILootContainer && ((ILootContainer)☃).getLootTable() != null && this.isSpectator()) {
         this.sendStatusMessage(new TextComponentTranslation("container.spectatorCantOpen").setStyle(new Style().setColor(TextFormatting.RED)), true);
      } else {
         if (this.openContainer != this.inventoryContainer) {
            this.closeScreen();
         }

         if (☃ instanceof ILockableContainer) {
            ILockableContainer ☃ = (ILockableContainer)☃;
            if (☃.isLocked() && !this.canOpen(☃.getLockCode()) && !this.isSpectator()) {
               this.connection.sendPacket(new SPacketChat(new TextComponentTranslation("container.isLocked", ☃.getDisplayName()), ChatType.GAME_INFO));
               this.connection
                  .sendPacket(new SPacketSoundEffect(SoundEvents.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS, this.posX, this.posY, this.posZ, 1.0F, 1.0F));
               return;
            }
         }

         this.getNextWindowId();
         if (☃ instanceof IInteractionObject) {
            this.connection
               .sendPacket(new SPacketOpenWindow(this.currentWindowId, ((IInteractionObject)☃).getGuiID(), ☃.getDisplayName(), ☃.getSizeInventory()));
            this.openContainer = ((IInteractionObject)☃).createContainer(this.inventory, this);
         } else {
            this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, "minecraft:container", ☃.getDisplayName(), ☃.getSizeInventory()));
            this.openContainer = new ContainerChest(this.inventory, ☃, this);
         }

         this.openContainer.windowId = this.currentWindowId;
         this.openContainer.addListener(this);
      }
   }

   @Override
   public void displayVillagerTradeGui(IMerchant var1) {
      this.getNextWindowId();
      this.openContainer = new ContainerMerchant(this.inventory, ☃, this.world);
      this.openContainer.windowId = this.currentWindowId;
      this.openContainer.addListener(this);
      IInventory ☃ = ((ContainerMerchant)this.openContainer).getMerchantInventory();
      ITextComponent ☃x = ☃.getDisplayName();
      this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, "minecraft:villager", ☃x, ☃.getSizeInventory()));
      MerchantRecipeList ☃xx = ☃.getRecipes(this);
      if (☃xx != null) {
         PacketBuffer ☃xxx = new PacketBuffer(Unpooled.buffer());
         ☃xxx.writeInt(this.currentWindowId);
         ☃xx.writeToBuf(☃xxx);
         this.connection.sendPacket(new SPacketCustomPayload("MC|TrList", ☃xxx));
      }
   }

   @Override
   public void openGuiHorseInventory(AbstractHorse var1, IInventory var2) {
      if (this.openContainer != this.inventoryContainer) {
         this.closeScreen();
      }

      this.getNextWindowId();
      this.connection.sendPacket(new SPacketOpenWindow(this.currentWindowId, "EntityHorse", ☃.getDisplayName(), ☃.getSizeInventory(), ☃.getEntityId()));
      this.openContainer = new ContainerHorseInventory(this.inventory, ☃, ☃, this);
      this.openContainer.windowId = this.currentWindowId;
      this.openContainer.addListener(this);
   }

   @Override
   public void openBook(ItemStack var1, EnumHand var2) {
      Item ☃ = ☃.getItem();
      if (☃ == Items.WRITTEN_BOOK) {
         PacketBuffer ☃x = new PacketBuffer(Unpooled.buffer());
         ☃x.writeEnumValue(☃);
         this.connection.sendPacket(new SPacketCustomPayload("MC|BOpen", ☃x));
      }
   }

   @Override
   public void displayGuiCommandBlock(TileEntityCommandBlock var1) {
      ☃.setSendToClient(true);
      this.sendTileEntityUpdate(☃);
   }

   @Override
   public void sendSlotContents(Container var1, int var2, ItemStack var3) {
      if (!(☃.getSlot(☃) instanceof SlotCrafting)) {
         if (☃ == this.inventoryContainer) {
            CriteriaTriggers.INVENTORY_CHANGED.trigger(this, this.inventory);
         }

         if (!this.isChangingQuantityOnly) {
            this.connection.sendPacket(new SPacketSetSlot(☃.windowId, ☃, ☃));
         }
      }
   }

   public void sendContainerToPlayer(Container var1) {
      this.sendAllContents(☃, ☃.getInventory());
   }

   @Override
   public void sendAllContents(Container var1, NonNullList<ItemStack> var2) {
      this.connection.sendPacket(new SPacketWindowItems(☃.windowId, ☃));
      this.connection.sendPacket(new SPacketSetSlot(-1, -1, this.inventory.getItemStack()));
   }

   @Override
   public void sendWindowProperty(Container var1, int var2, int var3) {
      this.connection.sendPacket(new SPacketWindowProperty(☃.windowId, ☃, ☃));
   }

   @Override
   public void sendAllWindowProperties(Container var1, IInventory var2) {
      for (int ☃ = 0; ☃ < ☃.getFieldCount(); ☃++) {
         this.connection.sendPacket(new SPacketWindowProperty(☃.windowId, ☃, ☃.getField(☃)));
      }
   }

   @Override
   public void closeScreen() {
      this.connection.sendPacket(new SPacketCloseWindow(this.openContainer.windowId));
      this.closeContainer();
   }

   public void updateHeldItem() {
      if (!this.isChangingQuantityOnly) {
         this.connection.sendPacket(new SPacketSetSlot(-1, -1, this.inventory.getItemStack()));
      }
   }

   public void closeContainer() {
      this.openContainer.onContainerClosed(this);
      this.openContainer = this.inventoryContainer;
   }

   public void setEntityActionState(float var1, float var2, boolean var3, boolean var4) {
      if (this.isRiding()) {
         if (☃ >= -1.0F && ☃ <= 1.0F) {
            this.moveStrafing = ☃;
         }

         if (☃ >= -1.0F && ☃ <= 1.0F) {
            this.moveForward = ☃;
         }

         this.isJumping = ☃;
         this.setSneaking(☃);
      }
   }

   @Override
   public void addStat(StatBase var1, int var2) {
      if (☃ != null) {
         this.statsFile.increaseStat(this, ☃, ☃);

         for (ScoreObjective ☃ : this.getWorldScoreboard().getObjectivesFromCriteria(☃.getCriteria())) {
            this.getWorldScoreboard().getOrCreateScore(this.getName(), ☃).increaseScore(☃);
         }
      }
   }

   @Override
   public void takeStat(StatBase var1) {
      if (☃ != null) {
         this.statsFile.unlockAchievement(this, ☃, 0);

         for (ScoreObjective ☃ : this.getWorldScoreboard().getObjectivesFromCriteria(☃.getCriteria())) {
            this.getWorldScoreboard().getOrCreateScore(this.getName(), ☃).setScorePoints(0);
         }
      }
   }

   @Override
   public void unlockRecipes(List<IRecipe> var1) {
      this.recipeBook.add(☃, this);
   }

   @Override
   public void unlockRecipes(ResourceLocation[] var1) {
      List<IRecipe> ☃ = Lists.newArrayList();

      for (ResourceLocation ☃x : ☃) {
         ☃.add(CraftingManager.getRecipe(☃x));
      }

      this.unlockRecipes(☃);
   }

   @Override
   public void resetRecipes(List<IRecipe> var1) {
      this.recipeBook.remove(☃, this);
   }

   public void mountEntityAndWakeUp() {
      this.disconnected = true;
      this.removePassengers();
      if (this.sleeping) {
         this.wakeUpPlayer(true, false, false);
      }
   }

   public boolean hasDisconnected() {
      return this.disconnected;
   }

   public void setPlayerHealthUpdated() {
      this.lastHealth = -1.0E8F;
   }

   @Override
   public void sendStatusMessage(ITextComponent var1, boolean var2) {
      this.connection.sendPacket(new SPacketChat(☃, ☃ ? ChatType.GAME_INFO : ChatType.CHAT));
   }

   @Override
   protected void onItemUseFinish() {
      if (!this.activeItemStack.isEmpty() && this.isHandActive()) {
         this.connection.sendPacket(new SPacketEntityStatus(this, (byte)9));
         super.onItemUseFinish();
      }
   }

   public void copyFrom(EntityPlayerMP var1, boolean var2) {
      if (☃) {
         this.inventory.copyInventory(☃.inventory);
         this.setHealth(☃.getHealth());
         this.foodStats = ☃.foodStats;
         this.experienceLevel = ☃.experienceLevel;
         this.experienceTotal = ☃.experienceTotal;
         this.experience = ☃.experience;
         this.setScore(☃.getScore());
         this.lastPortalPos = ☃.lastPortalPos;
         this.lastPortalVec = ☃.lastPortalVec;
         this.teleportDirection = ☃.teleportDirection;
      } else if (this.world.getGameRules().getBoolean("keepInventory") || ☃.isSpectator()) {
         this.inventory.copyInventory(☃.inventory);
         this.experienceLevel = ☃.experienceLevel;
         this.experienceTotal = ☃.experienceTotal;
         this.experience = ☃.experience;
         this.setScore(☃.getScore());
      }

      this.xpSeed = ☃.xpSeed;
      this.enderChest = ☃.enderChest;
      this.getDataManager().set(PLAYER_MODEL_FLAG, ☃.getDataManager().get(PLAYER_MODEL_FLAG));
      this.lastExperience = -1;
      this.lastHealth = -1.0F;
      this.lastFoodLevel = -1;
      this.recipeBook.copyFrom(☃.recipeBook);
      this.entityRemoveQueue.addAll(☃.entityRemoveQueue);
      this.seenCredits = ☃.seenCredits;
      this.enteredNetherPosition = ☃.enteredNetherPosition;
      this.setLeftShoulderEntity(☃.getLeftShoulderEntity());
      this.setRightShoulderEntity(☃.getRightShoulderEntity());
   }

   @Override
   protected void onNewPotionEffect(PotionEffect var1) {
      super.onNewPotionEffect(☃);
      this.connection.sendPacket(new SPacketEntityEffect(this.getEntityId(), ☃));
      if (☃.getPotion() == MobEffects.LEVITATION) {
         this.levitatingSince = this.ticksExisted;
         this.levitationStartPos = new Vec3d(this.posX, this.posY, this.posZ);
      }

      CriteriaTriggers.EFFECTS_CHANGED.trigger(this);
   }

   @Override
   protected void onChangedPotionEffect(PotionEffect var1, boolean var2) {
      super.onChangedPotionEffect(☃, ☃);
      this.connection.sendPacket(new SPacketEntityEffect(this.getEntityId(), ☃));
      CriteriaTriggers.EFFECTS_CHANGED.trigger(this);
   }

   @Override
   protected void onFinishedPotionEffect(PotionEffect var1) {
      super.onFinishedPotionEffect(☃);
      this.connection.sendPacket(new SPacketRemoveEntityEffect(this.getEntityId(), ☃.getPotion()));
      if (☃.getPotion() == MobEffects.LEVITATION) {
         this.levitationStartPos = null;
      }

      CriteriaTriggers.EFFECTS_CHANGED.trigger(this);
   }

   @Override
   public void setPositionAndUpdate(double var1, double var3, double var5) {
      this.connection.setPlayerLocation(☃, ☃, ☃, this.rotationYaw, this.rotationPitch);
   }

   @Override
   public void onCriticalHit(Entity var1) {
      this.getServerWorld().getEntityTracker().sendToTrackingAndSelf(this, new SPacketAnimation(☃, 4));
   }

   @Override
   public void onEnchantmentCritical(Entity var1) {
      this.getServerWorld().getEntityTracker().sendToTrackingAndSelf(this, new SPacketAnimation(☃, 5));
   }

   @Override
   public void sendPlayerAbilities() {
      if (this.connection != null) {
         this.connection.sendPacket(new SPacketPlayerAbilities(this.capabilities));
         this.updatePotionMetadata();
      }
   }

   public WorldServer getServerWorld() {
      return (WorldServer)this.world;
   }

   @Override
   public void setGameType(GameType var1) {
      this.interactionManager.setGameType(☃);
      this.connection.sendPacket(new SPacketChangeGameState(3, ☃.getID()));
      if (☃ == GameType.SPECTATOR) {
         this.spawnShoulderEntities();
         this.dismountRidingEntity();
      } else {
         this.setSpectatingEntity(this);
      }

      this.sendPlayerAbilities();
      this.markPotionsDirty();
   }

   @Override
   public boolean isSpectator() {
      return this.interactionManager.getGameType() == GameType.SPECTATOR;
   }

   @Override
   public boolean isCreative() {
      return this.interactionManager.getGameType() == GameType.CREATIVE;
   }

   @Override
   public void sendMessage(ITextComponent var1) {
      this.connection.sendPacket(new SPacketChat(☃));
   }

   @Override
   public boolean canUseCommand(int var1, String var2) {
      if ("seed".equals(☃) && !this.server.isDedicatedServer()) {
         return true;
      } else if (!"tell".equals(☃) && !"help".equals(☃) && !"me".equals(☃) && !"trigger".equals(☃)) {
         if (this.server.getPlayerList().canSendCommands(this.getGameProfile())) {
            UserListOpsEntry ☃ = this.server.getPlayerList().getOppedPlayers().getEntry(this.getGameProfile());
            return ☃ != null ? ☃.getPermissionLevel() >= ☃ : this.server.getOpPermissionLevel() >= ☃;
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   public String getPlayerIP() {
      String ☃ = this.connection.netManager.getRemoteAddress().toString();
      ☃ = ☃.substring(☃.indexOf("/") + 1);
      return ☃.substring(0, ☃.indexOf(":"));
   }

   public void handleClientSettings(CPacketClientSettings var1) {
      this.language = ☃.getLang();
      this.chatVisibility = ☃.getChatVisibility();
      this.chatColours = ☃.isColorsEnabled();
      this.getDataManager().set(PLAYER_MODEL_FLAG, (byte)☃.getModelPartFlags());
      this.getDataManager().set(MAIN_HAND, (byte)(☃.getMainHand() == EnumHandSide.LEFT ? 0 : 1));
   }

   public EntityPlayer.EnumChatVisibility getChatVisibility() {
      return this.chatVisibility;
   }

   public void loadResourcePack(String var1, String var2) {
      this.connection.sendPacket(new SPacketResourcePackSend(☃, ☃));
   }

   @Override
   public BlockPos getPosition() {
      return new BlockPos(this.posX, this.posY + 0.5, this.posZ);
   }

   public void markPlayerActive() {
      this.playerLastActiveTime = MinecraftServer.getCurrentTimeMillis();
   }

   public StatisticsManagerServer getStatFile() {
      return this.statsFile;
   }

   public RecipeBookServer getRecipeBook() {
      return this.recipeBook;
   }

   public void removeEntity(Entity var1) {
      if (☃ instanceof EntityPlayer) {
         this.connection.sendPacket(new SPacketDestroyEntities(☃.getEntityId()));
      } else {
         this.entityRemoveQueue.add(☃.getEntityId());
      }
   }

   public void addEntity(Entity var1) {
      this.entityRemoveQueue.remove(Integer.valueOf(☃.getEntityId()));
   }

   @Override
   protected void updatePotionMetadata() {
      if (this.isSpectator()) {
         this.resetPotionEffectMetadata();
         this.setInvisible(true);
      } else {
         super.updatePotionMetadata();
      }

      this.getServerWorld().getEntityTracker().updateVisibility(this);
   }

   public Entity getSpectatingEntity() {
      return (Entity)(this.spectatingEntity == null ? this : this.spectatingEntity);
   }

   public void setSpectatingEntity(Entity var1) {
      Entity ☃ = this.getSpectatingEntity();
      this.spectatingEntity = (Entity)(☃ == null ? this : ☃);
      if (☃ != this.spectatingEntity) {
         this.connection.sendPacket(new SPacketCamera(this.spectatingEntity));
         this.setPositionAndUpdate(this.spectatingEntity.posX, this.spectatingEntity.posY, this.spectatingEntity.posZ);
      }
   }

   @Override
   protected void decrementTimeUntilPortal() {
      if (this.timeUntilPortal > 0 && !this.invulnerableDimensionChange) {
         this.timeUntilPortal--;
      }
   }

   @Override
   public void attackTargetEntityWithCurrentItem(Entity var1) {
      if (this.interactionManager.getGameType() == GameType.SPECTATOR) {
         this.setSpectatingEntity(☃);
      } else {
         super.attackTargetEntityWithCurrentItem(☃);
      }
   }

   public long getLastActiveTime() {
      return this.playerLastActiveTime;
   }

   @Nullable
   public ITextComponent getTabListDisplayName() {
      return null;
   }

   @Override
   public void swingArm(EnumHand var1) {
      super.swingArm(☃);
      this.resetCooldown();
   }

   public boolean isInvulnerableDimensionChange() {
      return this.invulnerableDimensionChange;
   }

   public void clearInvulnerableDimensionChange() {
      this.invulnerableDimensionChange = false;
   }

   public void setElytraFlying() {
      this.setFlag(7, true);
   }

   public void clearElytraFlying() {
      this.setFlag(7, true);
      this.setFlag(7, false);
   }

   public PlayerAdvancements getAdvancements() {
      return this.advancements;
   }

   @Nullable
   public Vec3d getEnteredNetherPosition() {
      return this.enteredNetherPosition;
   }
}
