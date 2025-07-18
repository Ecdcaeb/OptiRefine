package net.minecraft.world.end;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeEndDecorator;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenEndGateway;
import net.minecraft.world.gen.feature.WorldGenEndPodium;
import net.minecraft.world.gen.feature.WorldGenSpikes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DragonFightManager {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final Predicate<EntityPlayerMP> VALID_PLAYER = Predicates.and(EntitySelectors.IS_ALIVE, EntitySelectors.withinRange(0.0, 128.0, 0.0, 192.0));
   private final BossInfoServer bossInfo = (BossInfoServer)new BossInfoServer(
         new TextComponentTranslation("entity.EnderDragon.name"), BossInfo.Color.PINK, BossInfo.Overlay.PROGRESS
      )
      .setPlayEndBossMusic(true)
      .setCreateFog(true);
   private final WorldServer world;
   private final List<Integer> gateways = Lists.newArrayList();
   private final BlockPattern portalPattern;
   private int ticksSinceDragonSeen;
   private int aliveCrystals;
   private int ticksSinceCrystalsScanned;
   private int ticksSinceLastPlayerScan;
   private boolean dragonKilled;
   private boolean previouslyKilled;
   private UUID dragonUniqueId;
   private boolean scanForLegacyFight = true;
   private BlockPos exitPortalLocation;
   private DragonSpawnManager respawnState;
   private int respawnStateTicks;
   private List<EntityEnderCrystal> crystals;

   public DragonFightManager(WorldServer var1, NBTTagCompound var2) {
      this.world = ☃;
      if (☃.hasKey("DragonKilled", 99)) {
         if (☃.hasUniqueId("DragonUUID")) {
            this.dragonUniqueId = ☃.getUniqueId("DragonUUID");
         }

         this.dragonKilled = ☃.getBoolean("DragonKilled");
         this.previouslyKilled = ☃.getBoolean("PreviouslyKilled");
         if (☃.getBoolean("IsRespawning")) {
            this.respawnState = DragonSpawnManager.START;
         }

         if (☃.hasKey("ExitPortalLocation", 10)) {
            this.exitPortalLocation = NBTUtil.getPosFromTag(☃.getCompoundTag("ExitPortalLocation"));
         }
      } else {
         this.dragonKilled = true;
         this.previouslyKilled = true;
      }

      if (☃.hasKey("Gateways", 9)) {
         NBTTagList ☃ = ☃.getTagList("Gateways", 3);

         for (int ☃x = 0; ☃x < ☃.tagCount(); ☃x++) {
            this.gateways.add(☃.getIntAt(☃x));
         }
      } else {
         this.gateways.addAll(ContiguousSet.create(Range.closedOpen(0, 20), DiscreteDomain.integers()));
         Collections.shuffle(this.gateways, new Random(☃.getSeed()));
      }

      this.portalPattern = FactoryBlockPattern.start()
         .aisle("       ", "       ", "       ", "   #   ", "       ", "       ", "       ")
         .aisle("       ", "       ", "       ", "   #   ", "       ", "       ", "       ")
         .aisle("       ", "       ", "       ", "   #   ", "       ", "       ", "       ")
         .aisle("  ###  ", " #   # ", "#     #", "#  #  #", "#     #", " #   # ", "  ###  ")
         .aisle("       ", "  ###  ", " ##### ", " ##### ", " ##### ", "  ###  ", "       ")
         .where('#', BlockWorldState.hasState(BlockMatcher.forBlock(Blocks.BEDROCK)))
         .build();
   }

   public NBTTagCompound getCompound() {
      NBTTagCompound ☃ = new NBTTagCompound();
      if (this.dragonUniqueId != null) {
         ☃.setUniqueId("DragonUUID", this.dragonUniqueId);
      }

      ☃.setBoolean("DragonKilled", this.dragonKilled);
      ☃.setBoolean("PreviouslyKilled", this.previouslyKilled);
      if (this.exitPortalLocation != null) {
         ☃.setTag("ExitPortalLocation", NBTUtil.createPosTag(this.exitPortalLocation));
      }

      NBTTagList ☃x = new NBTTagList();

      for (int ☃xx : this.gateways) {
         ☃x.appendTag(new NBTTagInt(☃xx));
      }

      ☃.setTag("Gateways", ☃x);
      return ☃;
   }

   public void tick() {
      this.bossInfo.setVisible(!this.dragonKilled);
      if (++this.ticksSinceLastPlayerScan >= 20) {
         this.updatePlayers();
         this.ticksSinceLastPlayerScan = 0;
      }

      if (!this.bossInfo.getPlayers().isEmpty()) {
         if (this.scanForLegacyFight) {
            LOGGER.info("Scanning for legacy world dragon fight...");
            this.loadChunks();
            this.scanForLegacyFight = false;
            boolean ☃ = this.hasDragonBeenKilled();
            if (☃) {
               LOGGER.info("Found that the dragon has been killed in this world already.");
               this.previouslyKilled = true;
            } else {
               LOGGER.info("Found that the dragon has not yet been killed in this world.");
               this.previouslyKilled = false;
               this.generatePortal(false);
            }

            List<EntityDragon> ☃x = this.world.getEntities(EntityDragon.class, EntitySelectors.IS_ALIVE);
            if (☃x.isEmpty()) {
               this.dragonKilled = true;
            } else {
               EntityDragon ☃xx = ☃x.get(0);
               this.dragonUniqueId = ☃xx.getUniqueID();
               LOGGER.info("Found that there's a dragon still alive ({})", ☃xx);
               this.dragonKilled = false;
               if (!☃) {
                  LOGGER.info("But we didn't have a portal, let's remove it.");
                  ☃xx.setDead();
                  this.dragonUniqueId = null;
               }
            }

            if (!this.previouslyKilled && this.dragonKilled) {
               this.dragonKilled = false;
            }
         }

         if (this.respawnState != null) {
            if (this.crystals == null) {
               this.respawnState = null;
               this.respawnDragon();
            }

            this.respawnState.process(this.world, this, this.crystals, this.respawnStateTicks++, this.exitPortalLocation);
         }

         if (!this.dragonKilled) {
            if (this.dragonUniqueId == null || ++this.ticksSinceDragonSeen >= 1200) {
               this.loadChunks();
               List<EntityDragon> ☃xx = this.world.getEntities(EntityDragon.class, EntitySelectors.IS_ALIVE);
               if (☃xx.isEmpty()) {
                  LOGGER.debug("Haven't seen the dragon, respawning it");
                  this.createNewDragon();
               } else {
                  LOGGER.debug("Haven't seen our dragon, but found another one to use.");
                  this.dragonUniqueId = ☃xx.get(0).getUniqueID();
               }

               this.ticksSinceDragonSeen = 0;
            }

            if (++this.ticksSinceCrystalsScanned >= 100) {
               this.findAliveCrystals();
               this.ticksSinceCrystalsScanned = 0;
            }
         }
      }
   }

   protected void setRespawnState(DragonSpawnManager var1) {
      if (this.respawnState == null) {
         throw new IllegalStateException("Dragon respawn isn't in progress, can't skip ahead in the animation.");
      } else {
         this.respawnStateTicks = 0;
         if (☃ == DragonSpawnManager.END) {
            this.respawnState = null;
            this.dragonKilled = false;
            EntityDragon ☃ = this.createNewDragon();

            for (EntityPlayerMP ☃x : this.bossInfo.getPlayers()) {
               CriteriaTriggers.SUMMONED_ENTITY.trigger(☃x, ☃);
            }
         } else {
            this.respawnState = ☃;
         }
      }
   }

   private boolean hasDragonBeenKilled() {
      for (int ☃ = -8; ☃ <= 8; ☃++) {
         for (int ☃x = -8; ☃x <= 8; ☃x++) {
            Chunk ☃xx = this.world.getChunk(☃, ☃x);

            for (TileEntity ☃xxx : ☃xx.getTileEntityMap().values()) {
               if (☃xxx instanceof TileEntityEndPortal) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   @Nullable
   private BlockPattern.PatternHelper findExitPortal() {
      for (int ☃ = -8; ☃ <= 8; ☃++) {
         for (int ☃x = -8; ☃x <= 8; ☃x++) {
            Chunk ☃xx = this.world.getChunk(☃, ☃x);

            for (TileEntity ☃xxx : ☃xx.getTileEntityMap().values()) {
               if (☃xxx instanceof TileEntityEndPortal) {
                  BlockPattern.PatternHelper ☃xxxx = this.portalPattern.match(this.world, ☃xxx.getPos());
                  if (☃xxxx != null) {
                     BlockPos ☃xxxxx = ☃xxxx.translateOffset(3, 3, 3).getPos();
                     if (this.exitPortalLocation == null && ☃xxxxx.getX() == 0 && ☃xxxxx.getZ() == 0) {
                        this.exitPortalLocation = ☃xxxxx;
                     }

                     return ☃xxxx;
                  }
               }
            }
         }
      }

      int ☃ = this.world.getHeight(WorldGenEndPodium.END_PODIUM_LOCATION).getY();

      for (int ☃x = ☃; ☃x >= 0; ☃x--) {
         BlockPattern.PatternHelper ☃xx = this.portalPattern
            .match(this.world, new BlockPos(WorldGenEndPodium.END_PODIUM_LOCATION.getX(), ☃x, WorldGenEndPodium.END_PODIUM_LOCATION.getZ()));
         if (☃xx != null) {
            if (this.exitPortalLocation == null) {
               this.exitPortalLocation = ☃xx.translateOffset(3, 3, 3).getPos();
            }

            return ☃xx;
         }
      }

      return null;
   }

   private void loadChunks() {
      for (int ☃ = -8; ☃ <= 8; ☃++) {
         for (int ☃x = -8; ☃x <= 8; ☃x++) {
            this.world.getChunk(☃, ☃x);
         }
      }
   }

   private void updatePlayers() {
      Set<EntityPlayerMP> ☃ = Sets.newHashSet();

      for (EntityPlayerMP ☃x : this.world.getPlayers(EntityPlayerMP.class, VALID_PLAYER)) {
         this.bossInfo.addPlayer(☃x);
         ☃.add(☃x);
      }

      Set<EntityPlayerMP> ☃x = Sets.newHashSet(this.bossInfo.getPlayers());
      ☃x.removeAll(☃);

      for (EntityPlayerMP ☃xx : ☃x) {
         this.bossInfo.removePlayer(☃xx);
      }
   }

   private void findAliveCrystals() {
      this.ticksSinceCrystalsScanned = 0;
      this.aliveCrystals = 0;

      for (WorldGenSpikes.EndSpike ☃ : BiomeEndDecorator.getSpikesForWorld(this.world)) {
         this.aliveCrystals = this.aliveCrystals + this.world.getEntitiesWithinAABB(EntityEnderCrystal.class, ☃.getTopBoundingBox()).size();
      }

      LOGGER.debug("Found {} end crystals still alive", this.aliveCrystals);
   }

   public void processDragonDeath(EntityDragon var1) {
      if (☃.getUniqueID().equals(this.dragonUniqueId)) {
         this.bossInfo.setPercent(0.0F);
         this.bossInfo.setVisible(false);
         this.generatePortal(true);
         this.spawnNewGateway();
         if (!this.previouslyKilled) {
            this.world.setBlockState(this.world.getHeight(WorldGenEndPodium.END_PODIUM_LOCATION), Blocks.DRAGON_EGG.getDefaultState());
         }

         this.previouslyKilled = true;
         this.dragonKilled = true;
      }
   }

   private void spawnNewGateway() {
      if (!this.gateways.isEmpty()) {
         int ☃ = this.gateways.remove(this.gateways.size() - 1);
         int ☃x = (int)(96.0 * Math.cos(2.0 * (-Math.PI + (Math.PI / 20) * ☃)));
         int ☃xx = (int)(96.0 * Math.sin(2.0 * (-Math.PI + (Math.PI / 20) * ☃)));
         this.generateGateway(new BlockPos(☃x, 75, ☃xx));
      }
   }

   private void generateGateway(BlockPos var1) {
      this.world.playEvent(3000, ☃, 0);
      new WorldGenEndGateway().generate(this.world, new Random(), ☃);
   }

   private void generatePortal(boolean var1) {
      WorldGenEndPodium ☃ = new WorldGenEndPodium(☃);
      if (this.exitPortalLocation == null) {
         this.exitPortalLocation = this.world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION).down();

         while (this.world.getBlockState(this.exitPortalLocation).getBlock() == Blocks.BEDROCK && this.exitPortalLocation.getY() > this.world.getSeaLevel()) {
            this.exitPortalLocation = this.exitPortalLocation.down();
         }
      }

      ☃.generate(this.world, new Random(), this.exitPortalLocation);
   }

   private EntityDragon createNewDragon() {
      this.world.getChunk(new BlockPos(0, 128, 0));
      EntityDragon ☃ = new EntityDragon(this.world);
      ☃.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
      ☃.setLocationAndAngles(0.0, 128.0, 0.0, this.world.rand.nextFloat() * 360.0F, 0.0F);
      this.world.spawnEntity(☃);
      this.dragonUniqueId = ☃.getUniqueID();
      return ☃;
   }

   public void dragonUpdate(EntityDragon var1) {
      if (☃.getUniqueID().equals(this.dragonUniqueId)) {
         this.bossInfo.setPercent(☃.getHealth() / ☃.getMaxHealth());
         this.ticksSinceDragonSeen = 0;
         if (☃.hasCustomName()) {
            this.bossInfo.setName(☃.getDisplayName());
         }
      }
   }

   public int getNumAliveCrystals() {
      return this.aliveCrystals;
   }

   public void onCrystalDestroyed(EntityEnderCrystal var1, DamageSource var2) {
      if (this.respawnState != null && this.crystals.contains(☃)) {
         LOGGER.debug("Aborting respawn sequence");
         this.respawnState = null;
         this.respawnStateTicks = 0;
         this.resetSpikeCrystals();
         this.generatePortal(true);
      } else {
         this.findAliveCrystals();
         Entity ☃ = this.world.getEntityFromUuid(this.dragonUniqueId);
         if (☃ instanceof EntityDragon) {
            ((EntityDragon)☃).onCrystalDestroyed(☃, new BlockPos(☃), ☃);
         }
      }
   }

   public boolean hasPreviouslyKilledDragon() {
      return this.previouslyKilled;
   }

   public void respawnDragon() {
      if (this.dragonKilled && this.respawnState == null) {
         BlockPos ☃ = this.exitPortalLocation;
         if (☃ == null) {
            LOGGER.debug("Tried to respawn, but need to find the portal first.");
            BlockPattern.PatternHelper ☃x = this.findExitPortal();
            if (☃x == null) {
               LOGGER.debug("Couldn't find a portal, so we made one.");
               this.generatePortal(true);
            } else {
               LOGGER.debug("Found the exit portal & temporarily using it.");
            }

            ☃ = this.exitPortalLocation;
         }

         List<EntityEnderCrystal> ☃x = Lists.newArrayList();
         BlockPos ☃xx = ☃.up(1);

         for (EnumFacing ☃xxx : EnumFacing.Plane.HORIZONTAL) {
            List<EntityEnderCrystal> ☃xxxx = this.world.getEntitiesWithinAABB(EntityEnderCrystal.class, new AxisAlignedBB(☃xx.offset(☃xxx, 2)));
            if (☃xxxx.isEmpty()) {
               return;
            }

            ☃x.addAll(☃xxxx);
         }

         LOGGER.debug("Found all crystals, respawning dragon.");
         this.respawnDragon(☃x);
      }
   }

   private void respawnDragon(List<EntityEnderCrystal> var1) {
      if (this.dragonKilled && this.respawnState == null) {
         for (BlockPattern.PatternHelper ☃ = this.findExitPortal(); ☃ != null; ☃ = this.findExitPortal()) {
            for (int ☃x = 0; ☃x < this.portalPattern.getPalmLength(); ☃x++) {
               for (int ☃xx = 0; ☃xx < this.portalPattern.getThumbLength(); ☃xx++) {
                  for (int ☃xxx = 0; ☃xxx < this.portalPattern.getFingerLength(); ☃xxx++) {
                     BlockWorldState ☃xxxx = ☃.translateOffset(☃x, ☃xx, ☃xxx);
                     if (☃xxxx.getBlockState().getBlock() == Blocks.BEDROCK || ☃xxxx.getBlockState().getBlock() == Blocks.END_PORTAL) {
                        this.world.setBlockState(☃xxxx.getPos(), Blocks.END_STONE.getDefaultState());
                     }
                  }
               }
            }
         }

         this.respawnState = DragonSpawnManager.START;
         this.respawnStateTicks = 0;
         this.generatePortal(false);
         this.crystals = ☃;
      }
   }

   public void resetSpikeCrystals() {
      for (WorldGenSpikes.EndSpike ☃ : BiomeEndDecorator.getSpikesForWorld(this.world)) {
         for (EntityEnderCrystal ☃x : this.world.getEntitiesWithinAABB(EntityEnderCrystal.class, ☃.getTopBoundingBox())) {
            ☃x.setEntityInvulnerable(false);
            ☃x.setBeamTarget(null);
         }
      }
   }
}
