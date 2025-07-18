package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockJukebox extends BlockContainer {
   public static final PropertyBool HAS_RECORD = PropertyBool.create("has_record");

   public static void registerFixesJukebox(DataFixer var0) {
      ☃.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackData(BlockJukebox.TileEntityJukebox.class, "RecordItem"));
   }

   protected BlockJukebox() {
      super(Material.WOOD, MapColor.DIRT);
      this.setDefaultState(this.blockState.getBaseState().withProperty(HAS_RECORD, false));
      this.setCreativeTab(CreativeTabs.DECORATIONS);
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      if (☃.getValue(HAS_RECORD)) {
         this.dropRecord(☃, ☃, ☃);
         ☃ = ☃.withProperty(HAS_RECORD, false);
         ☃.setBlockState(☃, ☃, 2);
         return true;
      } else {
         return false;
      }
   }

   public void insertRecord(World var1, BlockPos var2, IBlockState var3, ItemStack var4) {
      TileEntity ☃ = ☃.getTileEntity(☃);
      if (☃ instanceof BlockJukebox.TileEntityJukebox) {
         ((BlockJukebox.TileEntityJukebox)☃).setRecord(☃.copy());
         ☃.setBlockState(☃, ☃.withProperty(HAS_RECORD, true), 2);
      }
   }

   private void dropRecord(World var1, BlockPos var2, IBlockState var3) {
      if (!☃.isRemote) {
         TileEntity ☃ = ☃.getTileEntity(☃);
         if (☃ instanceof BlockJukebox.TileEntityJukebox) {
            BlockJukebox.TileEntityJukebox ☃x = (BlockJukebox.TileEntityJukebox)☃;
            ItemStack ☃xx = ☃x.getRecord();
            if (!☃xx.isEmpty()) {
               ☃.playEvent(1010, ☃, 0);
               ☃.playRecord(☃, null);
               ☃x.setRecord(ItemStack.EMPTY);
               float ☃xxx = 0.7F;
               double ☃xxxx = ☃.rand.nextFloat() * 0.7F + 0.15F;
               double ☃xxxxx = ☃.rand.nextFloat() * 0.7F + 0.060000002F + 0.6;
               double ☃xxxxxx = ☃.rand.nextFloat() * 0.7F + 0.15F;
               ItemStack ☃xxxxxxx = ☃xx.copy();
               EntityItem ☃xxxxxxxx = new EntityItem(☃, ☃.getX() + ☃xxxx, ☃.getY() + ☃xxxxx, ☃.getZ() + ☃xxxxxx, ☃xxxxxxx);
               ☃xxxxxxxx.setDefaultPickupDelay();
               ☃.spawnEntity(☃xxxxxxxx);
            }
         }
      }
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      this.dropRecord(☃, ☃, ☃);
      super.breakBlock(☃, ☃, ☃);
   }

   @Override
   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
      if (!☃.isRemote) {
         super.dropBlockAsItemWithChance(☃, ☃, ☃, ☃, 0);
      }
   }

   @Override
   public TileEntity createNewTileEntity(World var1, int var2) {
      return new BlockJukebox.TileEntityJukebox();
   }

   @Override
   public boolean hasComparatorInputOverride(IBlockState var1) {
      return true;
   }

   @Override
   public int getComparatorInputOverride(IBlockState var1, World var2, BlockPos var3) {
      TileEntity ☃ = ☃.getTileEntity(☃);
      if (☃ instanceof BlockJukebox.TileEntityJukebox) {
         ItemStack ☃x = ((BlockJukebox.TileEntityJukebox)☃).getRecord();
         if (!☃x.isEmpty()) {
            return Item.getIdFromItem(☃x.getItem()) + 1 - Item.getIdFromItem(Items.RECORD_13);
         }
      }

      return 0;
   }

   @Override
   public EnumBlockRenderType getRenderType(IBlockState var1) {
      return EnumBlockRenderType.MODEL;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(HAS_RECORD, ☃ > 0);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(HAS_RECORD) ? 1 : 0;
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, HAS_RECORD);
   }

   public static class TileEntityJukebox extends TileEntity {
      private ItemStack record = ItemStack.EMPTY;

      @Override
      public void readFromNBT(NBTTagCompound var1) {
         super.readFromNBT(☃);
         if (☃.hasKey("RecordItem", 10)) {
            this.setRecord(new ItemStack(☃.getCompoundTag("RecordItem")));
         } else if (☃.getInteger("Record") > 0) {
            this.setRecord(new ItemStack(Item.getItemById(☃.getInteger("Record"))));
         }
      }

      @Override
      public NBTTagCompound writeToNBT(NBTTagCompound var1) {
         super.writeToNBT(☃);
         if (!this.getRecord().isEmpty()) {
            ☃.setTag("RecordItem", this.getRecord().writeToNBT(new NBTTagCompound()));
         }

         return ☃;
      }

      public ItemStack getRecord() {
         return this.record;
      }

      public void setRecord(ItemStack var1) {
         this.record = ☃;
         this.markDirty();
      }
   }
}
