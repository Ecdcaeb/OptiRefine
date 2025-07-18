package net.minecraft.entity.item;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityFallingBlock extends Entity {
   private IBlockState fallTile;
   public int fallTime;
   public boolean shouldDropItem = true;
   private boolean dontSetBlock;
   private boolean hurtEntities;
   private int fallHurtMax = 40;
   private float fallHurtAmount = 2.0F;
   public NBTTagCompound tileEntityData;
   protected static final DataParameter<BlockPos> ORIGIN = EntityDataManager.createKey(EntityFallingBlock.class, DataSerializers.BLOCK_POS);

   public EntityFallingBlock(World var1) {
      super(☃);
   }

   public EntityFallingBlock(World var1, double var2, double var4, double var6, IBlockState var8) {
      super(☃);
      this.fallTile = ☃;
      this.preventEntitySpawning = true;
      this.setSize(0.98F, 0.98F);
      this.setPosition(☃, ☃ + (1.0F - this.height) / 2.0F, ☃);
      this.motionX = 0.0;
      this.motionY = 0.0;
      this.motionZ = 0.0;
      this.prevPosX = ☃;
      this.prevPosY = ☃;
      this.prevPosZ = ☃;
      this.setOrigin(new BlockPos(this));
   }

   @Override
   public boolean canBeAttackedWithItem() {
      return false;
   }

   public void setOrigin(BlockPos var1) {
      this.dataManager.set(ORIGIN, ☃);
   }

   public BlockPos getOrigin() {
      return this.dataManager.get(ORIGIN);
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   @Override
   protected void entityInit() {
      this.dataManager.register(ORIGIN, BlockPos.ORIGIN);
   }

   @Override
   public boolean canBeCollidedWith() {
      return !this.isDead;
   }

   @Override
   public void onUpdate() {
      Block ☃ = this.fallTile.getBlock();
      if (this.fallTile.getMaterial() == Material.AIR) {
         this.setDead();
      } else {
         this.prevPosX = this.posX;
         this.prevPosY = this.posY;
         this.prevPosZ = this.posZ;
         if (this.fallTime++ == 0) {
            BlockPos ☃x = new BlockPos(this);
            if (this.world.getBlockState(☃x).getBlock() == ☃) {
               this.world.setBlockToAir(☃x);
            } else if (!this.world.isRemote) {
               this.setDead();
               return;
            }
         }

         if (!this.hasNoGravity()) {
            this.motionY -= 0.04F;
         }

         this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
         if (!this.world.isRemote) {
            BlockPos ☃x = new BlockPos(this);
            boolean ☃xx = this.fallTile.getBlock() == Blocks.CONCRETE_POWDER;
            boolean ☃xxx = ☃xx && this.world.getBlockState(☃x).getMaterial() == Material.WATER;
            double ☃xxxx = this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ;
            if (☃xx && ☃xxxx > 1.0) {
               RayTraceResult ☃xxxxx = this.world
                  .rayTraceBlocks(new Vec3d(this.prevPosX, this.prevPosY, this.prevPosZ), new Vec3d(this.posX, this.posY, this.posZ), true);
               if (☃xxxxx != null && this.world.getBlockState(☃xxxxx.getBlockPos()).getMaterial() == Material.WATER) {
                  ☃x = ☃xxxxx.getBlockPos();
                  ☃xxx = true;
               }
            }

            if (!this.onGround && !☃xxx) {
               if (this.fallTime > 100 && !this.world.isRemote && (☃x.getY() < 1 || ☃x.getY() > 256) || this.fallTime > 600) {
                  if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops")) {
                     this.entityDropItem(new ItemStack(☃, 1, ☃.damageDropped(this.fallTile)), 0.0F);
                  }

                  this.setDead();
               }
            } else {
               IBlockState ☃xxxxx = this.world.getBlockState(☃x);
               if (!☃xxx && BlockFalling.canFallThrough(this.world.getBlockState(new BlockPos(this.posX, this.posY - 0.01F, this.posZ)))) {
                  this.onGround = false;
                  return;
               }

               this.motionX *= 0.7F;
               this.motionZ *= 0.7F;
               this.motionY *= -0.5;
               if (☃xxxxx.getBlock() != Blocks.PISTON_EXTENSION) {
                  this.setDead();
                  if (!this.dontSetBlock) {
                     if (this.world.mayPlace(☃, ☃x, true, EnumFacing.UP, null)
                        && (☃xxx || !BlockFalling.canFallThrough(this.world.getBlockState(☃x.down())))
                        && this.world.setBlockState(☃x, this.fallTile, 3)) {
                        if (☃ instanceof BlockFalling) {
                           ((BlockFalling)☃).onEndFalling(this.world, ☃x, this.fallTile, ☃xxxxx);
                        }

                        if (this.tileEntityData != null && ☃ instanceof ITileEntityProvider) {
                           TileEntity ☃xxxxxx = this.world.getTileEntity(☃x);
                           if (☃xxxxxx != null) {
                              NBTTagCompound ☃xxxxxxx = ☃xxxxxx.writeToNBT(new NBTTagCompound());

                              for (String ☃xxxxxxxx : this.tileEntityData.getKeySet()) {
                                 NBTBase ☃xxxxxxxxx = this.tileEntityData.getTag(☃xxxxxxxx);
                                 if (!"x".equals(☃xxxxxxxx) && !"y".equals(☃xxxxxxxx) && !"z".equals(☃xxxxxxxx)) {
                                    ☃xxxxxxx.setTag(☃xxxxxxxx, ☃xxxxxxxxx.copy());
                                 }
                              }

                              ☃xxxxxx.readFromNBT(☃xxxxxxx);
                              ☃xxxxxx.markDirty();
                           }
                        }
                     } else if (this.shouldDropItem && this.world.getGameRules().getBoolean("doEntityDrops")) {
                        this.entityDropItem(new ItemStack(☃, 1, ☃.damageDropped(this.fallTile)), 0.0F);
                     }
                  } else if (☃ instanceof BlockFalling) {
                     ((BlockFalling)☃).onBroken(this.world, ☃x);
                  }
               }
            }
         }

         this.motionX *= 0.98F;
         this.motionY *= 0.98F;
         this.motionZ *= 0.98F;
      }
   }

   @Override
   public void fall(float var1, float var2) {
      Block ☃ = this.fallTile.getBlock();
      if (this.hurtEntities) {
         int ☃x = MathHelper.ceil(☃ - 1.0F);
         if (☃x > 0) {
            List<Entity> ☃xx = Lists.newArrayList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()));
            boolean ☃xxx = ☃ == Blocks.ANVIL;
            DamageSource ☃xxxx = ☃xxx ? DamageSource.ANVIL : DamageSource.FALLING_BLOCK;

            for (Entity ☃xxxxx : ☃xx) {
               ☃xxxxx.attackEntityFrom(☃xxxx, Math.min(MathHelper.floor(☃x * this.fallHurtAmount), this.fallHurtMax));
            }

            if (☃xxx && this.rand.nextFloat() < 0.05F + ☃x * 0.05) {
               int ☃xxxxx = this.fallTile.getValue(BlockAnvil.DAMAGE);
               if (++☃xxxxx > 2) {
                  this.dontSetBlock = true;
               } else {
                  this.fallTile = this.fallTile.withProperty(BlockAnvil.DAMAGE, ☃xxxxx);
               }
            }
         }
      }
   }

   public static void registerFixesFallingBlock(DataFixer var0) {
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound var1) {
      Block ☃ = this.fallTile != null ? this.fallTile.getBlock() : Blocks.AIR;
      ResourceLocation ☃x = Block.REGISTRY.getNameForObject(☃);
      ☃.setString("Block", ☃x == null ? "" : ☃x.toString());
      ☃.setByte("Data", (byte)☃.getMetaFromState(this.fallTile));
      ☃.setInteger("Time", this.fallTime);
      ☃.setBoolean("DropItem", this.shouldDropItem);
      ☃.setBoolean("HurtEntities", this.hurtEntities);
      ☃.setFloat("FallHurtAmount", this.fallHurtAmount);
      ☃.setInteger("FallHurtMax", this.fallHurtMax);
      if (this.tileEntityData != null) {
         ☃.setTag("TileEntityData", this.tileEntityData);
      }
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound var1) {
      int ☃ = ☃.getByte("Data") & 255;
      if (☃.hasKey("Block", 8)) {
         this.fallTile = Block.getBlockFromName(☃.getString("Block")).getStateFromMeta(☃);
      } else if (☃.hasKey("TileID", 99)) {
         this.fallTile = Block.getBlockById(☃.getInteger("TileID")).getStateFromMeta(☃);
      } else {
         this.fallTile = Block.getBlockById(☃.getByte("Tile") & 255).getStateFromMeta(☃);
      }

      this.fallTime = ☃.getInteger("Time");
      Block ☃x = this.fallTile.getBlock();
      if (☃.hasKey("HurtEntities", 99)) {
         this.hurtEntities = ☃.getBoolean("HurtEntities");
         this.fallHurtAmount = ☃.getFloat("FallHurtAmount");
         this.fallHurtMax = ☃.getInteger("FallHurtMax");
      } else if (☃x == Blocks.ANVIL) {
         this.hurtEntities = true;
      }

      if (☃.hasKey("DropItem", 99)) {
         this.shouldDropItem = ☃.getBoolean("DropItem");
      }

      if (☃.hasKey("TileEntityData", 10)) {
         this.tileEntityData = ☃.getCompoundTag("TileEntityData");
      }

      if (☃x == null || ☃x.getDefaultState().getMaterial() == Material.AIR) {
         this.fallTile = Blocks.SAND.getDefaultState();
      }
   }

   public World getWorldObj() {
      return this.world;
   }

   public void setHurtEntities(boolean var1) {
      this.hurtEntities = ☃;
   }

   @Override
   public boolean canRenderOnFire() {
      return false;
   }

   @Override
   public void addEntityCrashInfo(CrashReportCategory var1) {
      super.addEntityCrashInfo(☃);
      if (this.fallTile != null) {
         Block ☃ = this.fallTile.getBlock();
         ☃.addCrashSection("Immitating block ID", Block.getIdFromBlock(☃));
         ☃.addCrashSection("Immitating block data", ☃.getMetaFromState(this.fallTile));
      }
   }

   @Nullable
   public IBlockState getBlock() {
      return this.fallTile;
   }

   @Override
   public boolean ignoreItemEntityData() {
      return true;
   }
}
