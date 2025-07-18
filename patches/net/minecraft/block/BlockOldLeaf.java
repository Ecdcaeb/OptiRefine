package net.minecraft.block;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockOldLeaf extends BlockLeaves {
   public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create(
      "variant", BlockPlanks.EnumType.class, new Predicate<BlockPlanks.EnumType>() {
         public boolean apply(@Nullable BlockPlanks.EnumType var1) {
            return ☃.getMetadata() < 4;
         }
      }
   );

   public BlockOldLeaf() {
      this.setDefaultState(
         this.blockState.getBaseState().withProperty(VARIANT, BlockPlanks.EnumType.OAK).withProperty(CHECK_DECAY, true).withProperty(DECAYABLE, true)
      );
   }

   @Override
   protected void dropApple(World var1, BlockPos var2, IBlockState var3, int var4) {
      if (☃.getValue(VARIANT) == BlockPlanks.EnumType.OAK && ☃.rand.nextInt(☃) == 0) {
         spawnAsEntity(☃, ☃, new ItemStack(Items.APPLE));
      }
   }

   @Override
   protected int getSaplingDropChance(IBlockState var1) {
      return ☃.getValue(VARIANT) == BlockPlanks.EnumType.JUNGLE ? 40 : super.getSaplingDropChance(☃);
   }

   @Override
   public void getSubBlocks(CreativeTabs var1, NonNullList<ItemStack> var2) {
      ☃.add(new ItemStack(this, 1, BlockPlanks.EnumType.OAK.getMetadata()));
      ☃.add(new ItemStack(this, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
      ☃.add(new ItemStack(this, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
      ☃.add(new ItemStack(this, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
   }

   @Override
   protected ItemStack getSilkTouchDrop(IBlockState var1) {
      return new ItemStack(Item.getItemFromBlock(this), 1, ☃.getValue(VARIANT).getMetadata());
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(VARIANT, this.getWoodType(☃)).withProperty(DECAYABLE, (☃ & 4) == 0).withProperty(CHECK_DECAY, (☃ & 8) > 0);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      int ☃ = 0;
      ☃ |= ☃.getValue(VARIANT).getMetadata();
      if (!☃.getValue(DECAYABLE)) {
         ☃ |= 4;
      }

      if (☃.getValue(CHECK_DECAY)) {
         ☃ |= 8;
      }

      return ☃;
   }

   @Override
   public BlockPlanks.EnumType getWoodType(int var1) {
      return BlockPlanks.EnumType.byMetadata((☃ & 3) % 4);
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, VARIANT, CHECK_DECAY, DECAYABLE);
   }

   @Override
   public int damageDropped(IBlockState var1) {
      return ☃.getValue(VARIANT).getMetadata();
   }

   @Override
   public void harvestBlock(World var1, EntityPlayer var2, BlockPos var3, IBlockState var4, @Nullable TileEntity var5, ItemStack var6) {
      if (!☃.isRemote && ☃.getItem() == Items.SHEARS) {
         ☃.addStat(StatList.getBlockStats(this));
         spawnAsEntity(☃, ☃, new ItemStack(Item.getItemFromBlock(this), 1, ☃.getValue(VARIANT).getMetadata()));
      } else {
         super.harvestBlock(☃, ☃, ☃, ☃, ☃, ☃);
      }
   }
}
