package net.minecraft.entity.item;

import net.minecraft.block.BlockFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityMinecartFurnace extends EntityMinecart {
   private static final DataParameter<Boolean> POWERED = EntityDataManager.createKey(EntityMinecartFurnace.class, DataSerializers.BOOLEAN);
   private int fuel;
   public double pushX;
   public double pushZ;

   public EntityMinecartFurnace(World var1) {
      super(☃);
   }

   public EntityMinecartFurnace(World var1, double var2, double var4, double var6) {
      super(☃, ☃, ☃, ☃);
   }

   public static void registerFixesMinecartFurnace(DataFixer var0) {
      EntityMinecart.registerFixesMinecart(☃, EntityMinecartFurnace.class);
   }

   @Override
   public EntityMinecart.Type getType() {
      return EntityMinecart.Type.FURNACE;
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(POWERED, false);
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (this.fuel > 0) {
         this.fuel--;
      }

      if (this.fuel <= 0) {
         this.pushX = 0.0;
         this.pushZ = 0.0;
      }

      this.setMinecartPowered(this.fuel > 0);
      if (this.isMinecartPowered() && this.rand.nextInt(4) == 0) {
         this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY + 0.8, this.posZ, 0.0, 0.0, 0.0);
      }
   }

   @Override
   protected double getMaximumSpeed() {
      return 0.2;
   }

   @Override
   public void killMinecart(DamageSource var1) {
      super.killMinecart(☃);
      if (!☃.isExplosion() && this.world.getGameRules().getBoolean("doEntityDrops")) {
         this.entityDropItem(new ItemStack(Blocks.FURNACE, 1), 0.0F);
      }
   }

   @Override
   protected void moveAlongTrack(BlockPos var1, IBlockState var2) {
      super.moveAlongTrack(☃, ☃);
      double ☃ = this.pushX * this.pushX + this.pushZ * this.pushZ;
      if (☃ > 1.0E-4 && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.001) {
         ☃ = MathHelper.sqrt(☃);
         this.pushX /= ☃;
         this.pushZ /= ☃;
         if (this.pushX * this.motionX + this.pushZ * this.motionZ < 0.0) {
            this.pushX = 0.0;
            this.pushZ = 0.0;
         } else {
            double ☃x = ☃ / this.getMaximumSpeed();
            this.pushX *= ☃x;
            this.pushZ *= ☃x;
         }
      }
   }

   @Override
   protected void applyDrag() {
      double ☃ = this.pushX * this.pushX + this.pushZ * this.pushZ;
      if (☃ > 1.0E-4) {
         ☃ = MathHelper.sqrt(☃);
         this.pushX /= ☃;
         this.pushZ /= ☃;
         double ☃x = 1.0;
         this.motionX *= 0.8F;
         this.motionY *= 0.0;
         this.motionZ *= 0.8F;
         this.motionX = this.motionX + this.pushX * 1.0;
         this.motionZ = this.motionZ + this.pushZ * 1.0;
      } else {
         this.motionX *= 0.98F;
         this.motionY *= 0.0;
         this.motionZ *= 0.98F;
      }

      super.applyDrag();
   }

   @Override
   public boolean processInitialInteract(EntityPlayer var1, EnumHand var2) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (☃.getItem() == Items.COAL && this.fuel + 3600 <= 32000) {
         if (!☃.capabilities.isCreativeMode) {
            ☃.shrink(1);
         }

         this.fuel += 3600;
      }

      this.pushX = this.posX - ☃.posX;
      this.pushZ = this.posZ - ☃.posZ;
      return true;
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(☃);
      ☃.setDouble("PushX", this.pushX);
      ☃.setDouble("PushZ", this.pushZ);
      ☃.setShort("Fuel", (short)this.fuel);
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(☃);
      this.pushX = ☃.getDouble("PushX");
      this.pushZ = ☃.getDouble("PushZ");
      this.fuel = ☃.getShort("Fuel");
   }

   protected boolean isMinecartPowered() {
      return this.dataManager.get(POWERED);
   }

   protected void setMinecartPowered(boolean var1) {
      this.dataManager.set(POWERED, ☃);
   }

   @Override
   public IBlockState getDefaultDisplayTile() {
      return (this.isMinecartPowered() ? Blocks.LIT_FURNACE : Blocks.FURNACE).getDefaultState().withProperty(BlockFurnace.FACING, EnumFacing.NORTH);
   }
}
