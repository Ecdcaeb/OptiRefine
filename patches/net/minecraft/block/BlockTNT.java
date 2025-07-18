package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockTNT extends Block {
   public static final PropertyBool EXPLODE = PropertyBool.create("explode");

   public BlockTNT() {
      super(Material.TNT);
      this.setDefaultState(this.blockState.getBaseState().withProperty(EXPLODE, false));
      this.setCreativeTab(CreativeTabs.REDSTONE);
   }

   @Override
   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      super.onBlockAdded(☃, ☃, ☃);
      if (☃.isBlockPowered(☃)) {
         this.onPlayerDestroy(☃, ☃, ☃.withProperty(EXPLODE, true));
         ☃.setBlockToAir(☃);
      }
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (☃.isBlockPowered(☃)) {
         this.onPlayerDestroy(☃, ☃, ☃.withProperty(EXPLODE, true));
         ☃.setBlockToAir(☃);
      }
   }

   @Override
   public void onExplosionDestroy(World var1, BlockPos var2, Explosion var3) {
      if (!☃.isRemote) {
         EntityTNTPrimed ☃ = new EntityTNTPrimed(☃, ☃.getX() + 0.5F, ☃.getY(), ☃.getZ() + 0.5F, ☃.getExplosivePlacedBy());
         ☃.setFuse((short)(☃.rand.nextInt(☃.getFuse() / 4) + ☃.getFuse() / 8));
         ☃.spawnEntity(☃);
      }
   }

   @Override
   public void onPlayerDestroy(World var1, BlockPos var2, IBlockState var3) {
      this.explode(☃, ☃, ☃, null);
   }

   public void explode(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4) {
      if (!☃.isRemote) {
         if (☃.getValue(EXPLODE)) {
            EntityTNTPrimed ☃ = new EntityTNTPrimed(☃, ☃.getX() + 0.5F, ☃.getY(), ☃.getZ() + 0.5F, ☃);
            ☃.spawnEntity(☃);
            ☃.playSound(null, ☃.posX, ☃.posY, ☃.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
         }
      }
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      ItemStack ☃ = ☃.getHeldItem(☃);
      if (!☃.isEmpty() && (☃.getItem() == Items.FLINT_AND_STEEL || ☃.getItem() == Items.FIRE_CHARGE)) {
         this.explode(☃, ☃, ☃.withProperty(EXPLODE, true), ☃);
         ☃.setBlockState(☃, Blocks.AIR.getDefaultState(), 11);
         if (☃.getItem() == Items.FLINT_AND_STEEL) {
            ☃.damageItem(1, ☃);
         } else if (!☃.capabilities.isCreativeMode) {
            ☃.shrink(1);
         }

         return true;
      } else {
         return super.onBlockActivated(☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃, ☃);
      }
   }

   @Override
   public void onEntityCollision(World var1, BlockPos var2, IBlockState var3, Entity var4) {
      if (!☃.isRemote && ☃ instanceof EntityArrow) {
         EntityArrow ☃ = (EntityArrow)☃;
         if (☃.isBurning()) {
            this.explode(
               ☃, ☃, ☃.getBlockState(☃).withProperty(EXPLODE, true), ☃.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase)☃.shootingEntity : null
            );
            ☃.setBlockToAir(☃);
         }
      }
   }

   @Override
   public boolean canDropFromExplosion(Explosion var1) {
      return false;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(EXPLODE, (☃ & 1) > 0);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(EXPLODE) ? 1 : 0;
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, EXPLODE);
   }
}
