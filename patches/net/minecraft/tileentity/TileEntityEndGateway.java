package net.minecraft.tileentity;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenEndGateway;
import net.minecraft.world.gen.feature.WorldGenEndIsland;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TileEntityEndGateway extends TileEntityEndPortal implements ITickable {
   private static final Logger LOGGER = LogManager.getLogger();
   private long age;
   private int teleportCooldown;
   private BlockPos exitPortal;
   private boolean exactTeleport;

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(☃);
      ☃.setLong("Age", this.age);
      if (this.exitPortal != null) {
         ☃.setTag("ExitPortal", NBTUtil.createPosTag(this.exitPortal));
      }

      if (this.exactTeleport) {
         ☃.setBoolean("ExactTeleport", this.exactTeleport);
      }

      return ☃;
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(☃);
      this.age = ☃.getLong("Age");
      if (☃.hasKey("ExitPortal", 10)) {
         this.exitPortal = NBTUtil.getPosFromTag(☃.getCompoundTag("ExitPortal"));
      }

      this.exactTeleport = ☃.getBoolean("ExactTeleport");
   }

   @Override
   public double getMaxRenderDistanceSquared() {
      return 65536.0;
   }

   @Override
   public void update() {
      boolean ☃ = this.isSpawning();
      boolean ☃x = this.isCoolingDown();
      this.age++;
      if (☃x) {
         this.teleportCooldown--;
      } else if (!this.world.isRemote) {
         List<Entity> ☃xx = this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(this.getPos()));
         if (!☃xx.isEmpty()) {
            this.teleportEntity(☃xx.get(0));
         }

         if (this.age % 2400L == 0L) {
            this.triggerCooldown();
         }
      }

      if (☃ != this.isSpawning() || ☃x != this.isCoolingDown()) {
         this.markDirty();
      }
   }

   public boolean isSpawning() {
      return this.age < 200L;
   }

   public boolean isCoolingDown() {
      return this.teleportCooldown > 0;
   }

   public float getSpawnPercent(float var1) {
      return MathHelper.clamp(((float)this.age + ☃) / 200.0F, 0.0F, 1.0F);
   }

   public float getCooldownPercent(float var1) {
      return 1.0F - MathHelper.clamp((this.teleportCooldown - ☃) / 40.0F, 0.0F, 1.0F);
   }

   @Nullable
   @Override
   public SPacketUpdateTileEntity getUpdatePacket() {
      return new SPacketUpdateTileEntity(this.pos, 8, this.getUpdateTag());
   }

   @Override
   public NBTTagCompound getUpdateTag() {
      return this.writeToNBT(new NBTTagCompound());
   }

   public void triggerCooldown() {
      if (!this.world.isRemote) {
         this.teleportCooldown = 40;
         this.world.addBlockEvent(this.getPos(), this.getBlockType(), 1, 0);
         this.markDirty();
      }
   }

   @Override
   public boolean receiveClientEvent(int var1, int var2) {
      if (☃ == 1) {
         this.teleportCooldown = 40;
         return true;
      } else {
         return super.receiveClientEvent(☃, ☃);
      }
   }

   public void teleportEntity(Entity var1) {
      if (!this.world.isRemote && !this.isCoolingDown()) {
         this.teleportCooldown = 100;
         if (this.exitPortal == null && this.world.provider instanceof WorldProviderEnd) {
            this.findExitPortal();
         }

         if (this.exitPortal != null) {
            BlockPos ☃ = this.exactTeleport ? this.exitPortal : this.findExitPosition();
            ☃.setPositionAndUpdate(☃.getX() + 0.5, ☃.getY() + 0.5, ☃.getZ() + 0.5);
         }

         this.triggerCooldown();
      }
   }

   private BlockPos findExitPosition() {
      BlockPos ☃ = findHighestBlock(this.world, this.exitPortal, 5, false);
      LOGGER.debug("Best exit position for portal at {} is {}", this.exitPortal, ☃);
      return ☃.up();
   }

   private void findExitPortal() {
      Vec3d ☃ = new Vec3d(this.getPos().getX(), 0.0, this.getPos().getZ()).normalize();
      Vec3d ☃x = ☃.scale(1024.0);

      for (int ☃xx = 16; getChunk(this.world, ☃x).getTopFilledSegment() > 0 && ☃xx-- > 0; ☃x = ☃x.add(☃.scale(-16.0))) {
         LOGGER.debug("Skipping backwards past nonempty chunk at {}", ☃x);
      }

      for (int var5 = 16; getChunk(this.world, ☃x).getTopFilledSegment() == 0 && var5-- > 0; ☃x = ☃x.add(☃.scale(16.0))) {
         LOGGER.debug("Skipping forward past empty chunk at {}", ☃x);
      }

      LOGGER.debug("Found chunk at {}", ☃x);
      Chunk ☃xx = getChunk(this.world, ☃x);
      this.exitPortal = findSpawnpointInChunk(☃xx);
      if (this.exitPortal == null) {
         this.exitPortal = new BlockPos(☃x.x + 0.5, 75.0, ☃x.z + 0.5);
         LOGGER.debug("Failed to find suitable block, settling on {}", this.exitPortal);
         new WorldGenEndIsland().generate(this.world, new Random(this.exitPortal.toLong()), this.exitPortal);
      } else {
         LOGGER.debug("Found block at {}", this.exitPortal);
      }

      this.exitPortal = findHighestBlock(this.world, this.exitPortal, 16, true);
      LOGGER.debug("Creating portal at {}", this.exitPortal);
      this.exitPortal = this.exitPortal.up(10);
      this.createExitPortal(this.exitPortal);
      this.markDirty();
   }

   private static BlockPos findHighestBlock(World var0, BlockPos var1, int var2, boolean var3) {
      BlockPos ☃ = null;

      for (int ☃x = -☃; ☃x <= ☃; ☃x++) {
         for (int ☃xx = -☃; ☃xx <= ☃; ☃xx++) {
            if (☃x != 0 || ☃xx != 0 || ☃) {
               for (int ☃xxx = 255; ☃xxx > (☃ == null ? 0 : ☃.getY()); ☃xxx--) {
                  BlockPos ☃xxxx = new BlockPos(☃.getX() + ☃x, ☃xxx, ☃.getZ() + ☃xx);
                  IBlockState ☃xxxxx = ☃.getBlockState(☃xxxx);
                  if (☃xxxxx.isBlockNormalCube() && (☃ || ☃xxxxx.getBlock() != Blocks.BEDROCK)) {
                     ☃ = ☃xxxx;
                     break;
                  }
               }
            }
         }
      }

      return ☃ == null ? ☃ : ☃;
   }

   private static Chunk getChunk(World var0, Vec3d var1) {
      return ☃.getChunk(MathHelper.floor(☃.x / 16.0), MathHelper.floor(☃.z / 16.0));
   }

   @Nullable
   private static BlockPos findSpawnpointInChunk(Chunk var0) {
      BlockPos ☃ = new BlockPos(☃.x * 16, 30, ☃.z * 16);
      int ☃x = ☃.getTopFilledSegment() + 16 - 1;
      BlockPos ☃xx = new BlockPos(☃.x * 16 + 16 - 1, ☃x, ☃.z * 16 + 16 - 1);
      BlockPos ☃xxx = null;
      double ☃xxxx = 0.0;

      for (BlockPos ☃xxxxx : BlockPos.getAllInBox(☃, ☃xx)) {
         IBlockState ☃xxxxxx = ☃.getBlockState(☃xxxxx);
         if (☃xxxxxx.getBlock() == Blocks.END_STONE && !☃.getBlockState(☃xxxxx.up(1)).isBlockNormalCube() && !☃.getBlockState(☃xxxxx.up(2)).isBlockNormalCube()
            )
          {
            double ☃xxxxxxx = ☃xxxxx.distanceSqToCenter(0.0, 0.0, 0.0);
            if (☃xxx == null || ☃xxxxxxx < ☃xxxx) {
               ☃xxx = ☃xxxxx;
               ☃xxxx = ☃xxxxxxx;
            }
         }
      }

      return ☃xxx;
   }

   private void createExitPortal(BlockPos var1) {
      new WorldGenEndGateway().generate(this.world, new Random(), ☃);
      TileEntity ☃ = this.world.getTileEntity(☃);
      if (☃ instanceof TileEntityEndGateway) {
         TileEntityEndGateway ☃x = (TileEntityEndGateway)☃;
         ☃x.exitPortal = new BlockPos(this.getPos());
         ☃x.markDirty();
      } else {
         LOGGER.warn("Couldn't save exit portal at {}", ☃);
      }
   }

   @Override
   public boolean shouldRenderFace(EnumFacing var1) {
      return this.getBlockType().getDefaultState().shouldSideBeRendered(this.world, this.getPos(), ☃);
   }

   public int getParticleAmount() {
      int ☃ = 0;

      for (EnumFacing ☃x : EnumFacing.values()) {
         ☃ += this.shouldRenderFace(☃x) ? 1 : 0;
      }

      return ☃;
   }

   public void setExactPosition(BlockPos var1) {
      this.exactTeleport = true;
      this.exitPortal = ☃;
   }
}
