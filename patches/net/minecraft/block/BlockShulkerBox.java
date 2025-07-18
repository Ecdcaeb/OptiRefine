package net.minecraft.block;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockShulkerBox extends BlockContainer {
   public static final PropertyEnum<EnumFacing> FACING = PropertyDirection.create("facing");
   private final EnumDyeColor color;

   public BlockShulkerBox(EnumDyeColor var1) {
      super(Material.ROCK, MapColor.AIR);
      this.color = ☃;
      this.setCreativeTab(CreativeTabs.DECORATIONS);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
   }

   @Override
   public TileEntity createNewTileEntity(World var1, int var2) {
      return new TileEntityShulkerBox(this.color);
   }

   @Override
   public boolean isOpaqueCube(IBlockState var1) {
      return false;
   }

   @Override
   public boolean causesSuffocation(IBlockState var1) {
      return true;
   }

   @Override
   public boolean isFullCube(IBlockState var1) {
      return false;
   }

   @Override
   public boolean hasCustomBreakingProgress(IBlockState var1) {
      return true;
   }

   @Override
   public EnumBlockRenderType getRenderType(IBlockState var1) {
      return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      if (☃.isRemote) {
         return true;
      } else if (☃.isSpectator()) {
         return true;
      } else {
         TileEntity ☃ = ☃.getTileEntity(☃);
         if (☃ instanceof TileEntityShulkerBox) {
            EnumFacing ☃x = ☃.getValue(FACING);
            boolean ☃xx;
            if (((TileEntityShulkerBox)☃).getAnimationStatus() == TileEntityShulkerBox.AnimationStatus.CLOSED) {
               AxisAlignedBB ☃xxx = FULL_BLOCK_AABB.expand(0.5F * ☃x.getXOffset(), 0.5F * ☃x.getYOffset(), 0.5F * ☃x.getZOffset())
                  .contract(☃x.getXOffset(), ☃x.getYOffset(), ☃x.getZOffset());
               ☃xx = !☃.collidesWithAnyBlock(☃xxx.offset(☃.offset(☃x)));
            } else {
               ☃xx = true;
            }

            if (☃xx) {
               ☃.addStat(StatList.OPEN_SHULKER_BOX);
               ☃.displayGUIChest((IInventory)☃);
            }

            return true;
         } else {
            return false;
         }
      }
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return this.getDefaultState().withProperty(FACING, ☃);
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, FACING);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(FACING).getIndex();
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      EnumFacing ☃ = EnumFacing.byIndex(☃);
      return this.getDefaultState().withProperty(FACING, ☃);
   }

   @Override
   public void onBlockHarvested(World var1, BlockPos var2, IBlockState var3, EntityPlayer var4) {
      if (☃.getTileEntity(☃) instanceof TileEntityShulkerBox) {
         TileEntityShulkerBox ☃ = (TileEntityShulkerBox)☃.getTileEntity(☃);
         ☃.setDestroyedByCreativePlayer(☃.capabilities.isCreativeMode);
         ☃.fillWithLoot(☃);
      }
   }

   @Override
   public void dropBlockAsItemWithChance(World var1, BlockPos var2, IBlockState var3, float var4, int var5) {
   }

   @Override
   public void onBlockPlacedBy(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4, ItemStack var5) {
      if (☃.hasDisplayName()) {
         TileEntity ☃ = ☃.getTileEntity(☃);
         if (☃ instanceof TileEntityShulkerBox) {
            ((TileEntityShulkerBox)☃).setCustomName(☃.getDisplayName());
         }
      }
   }

   @Override
   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      TileEntity ☃ = ☃.getTileEntity(☃);
      if (☃ instanceof TileEntityShulkerBox) {
         TileEntityShulkerBox ☃x = (TileEntityShulkerBox)☃;
         if (!☃x.isCleared() && ☃x.shouldDrop()) {
            ItemStack ☃xx = new ItemStack(Item.getItemFromBlock(this));
            NBTTagCompound ☃xxx = new NBTTagCompound();
            NBTTagCompound ☃xxxx = new NBTTagCompound();
            ☃xxx.setTag("BlockEntityTag", ((TileEntityShulkerBox)☃).saveToNbt(☃xxxx));
            ☃xx.setTagCompound(☃xxx);
            if (☃x.hasCustomName()) {
               ☃xx.setStackDisplayName(☃x.getName());
               ☃x.setCustomName("");
            }

            spawnAsEntity(☃, ☃, ☃xx);
         }

         ☃.updateComparatorOutputLevel(☃, ☃.getBlock());
      }

      super.breakBlock(☃, ☃, ☃);
   }

   @Override
   public void addInformation(ItemStack var1, @Nullable World var2, List<String> var3, ITooltipFlag var4) {
      super.addInformation(☃, ☃, ☃, ☃);
      NBTTagCompound ☃ = ☃.getTagCompound();
      if (☃ != null && ☃.hasKey("BlockEntityTag", 10)) {
         NBTTagCompound ☃x = ☃.getCompoundTag("BlockEntityTag");
         if (☃x.hasKey("LootTable", 8)) {
            ☃.add("???????");
         }

         if (☃x.hasKey("Items", 9)) {
            NonNullList<ItemStack> ☃xx = NonNullList.withSize(27, ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(☃x, ☃xx);
            int ☃xxx = 0;
            int ☃xxxx = 0;

            for (ItemStack ☃xxxxx : ☃xx) {
               if (!☃xxxxx.isEmpty()) {
                  ☃xxxx++;
                  if (☃xxx <= 4) {
                     ☃xxx++;
                     ☃.add(String.format("%s x%d", ☃xxxxx.getDisplayName(), ☃xxxxx.getCount()));
                  }
               }
            }

            if (☃xxxx - ☃xxx > 0) {
               ☃.add(String.format(TextFormatting.ITALIC + I18n.translateToLocal("container.shulkerBox.more"), ☃xxxx - ☃xxx));
            }
         }
      }
   }

   @Override
   public EnumPushReaction getPushReaction(IBlockState var1) {
      return EnumPushReaction.DESTROY;
   }

   @Override
   public AxisAlignedBB getBoundingBox(IBlockState var1, IBlockAccess var2, BlockPos var3) {
      TileEntity ☃ = ☃.getTileEntity(☃);
      return ☃ instanceof TileEntityShulkerBox ? ((TileEntityShulkerBox)☃).getBoundingBox(☃) : FULL_BLOCK_AABB;
   }

   @Override
   public boolean hasComparatorInputOverride(IBlockState var1) {
      return true;
   }

   @Override
   public int getComparatorInputOverride(IBlockState var1, World var2, BlockPos var3) {
      return Container.calcRedstoneFromInventory((IInventory)☃.getTileEntity(☃));
   }

   @Override
   public ItemStack getItem(World var1, BlockPos var2, IBlockState var3) {
      ItemStack ☃ = super.getItem(☃, ☃, ☃);
      TileEntityShulkerBox ☃x = (TileEntityShulkerBox)☃.getTileEntity(☃);
      NBTTagCompound ☃xx = ☃x.saveToNbt(new NBTTagCompound());
      if (!☃xx.isEmpty()) {
         ☃.setTagInfo("BlockEntityTag", ☃xx);
      }

      return ☃;
   }

   public static EnumDyeColor getColorFromItem(Item var0) {
      return getColorFromBlock(Block.getBlockFromItem(☃));
   }

   public static EnumDyeColor getColorFromBlock(Block var0) {
      return ☃ instanceof BlockShulkerBox ? ((BlockShulkerBox)☃).getColor() : EnumDyeColor.PURPLE;
   }

   public static Block getBlockByColor(EnumDyeColor var0) {
      switch (☃) {
         case WHITE:
            return Blocks.WHITE_SHULKER_BOX;
         case ORANGE:
            return Blocks.ORANGE_SHULKER_BOX;
         case MAGENTA:
            return Blocks.MAGENTA_SHULKER_BOX;
         case LIGHT_BLUE:
            return Blocks.LIGHT_BLUE_SHULKER_BOX;
         case YELLOW:
            return Blocks.YELLOW_SHULKER_BOX;
         case LIME:
            return Blocks.LIME_SHULKER_BOX;
         case PINK:
            return Blocks.PINK_SHULKER_BOX;
         case GRAY:
            return Blocks.GRAY_SHULKER_BOX;
         case SILVER:
            return Blocks.SILVER_SHULKER_BOX;
         case CYAN:
            return Blocks.CYAN_SHULKER_BOX;
         case PURPLE:
         default:
            return Blocks.PURPLE_SHULKER_BOX;
         case BLUE:
            return Blocks.BLUE_SHULKER_BOX;
         case BROWN:
            return Blocks.BROWN_SHULKER_BOX;
         case GREEN:
            return Blocks.GREEN_SHULKER_BOX;
         case RED:
            return Blocks.RED_SHULKER_BOX;
         case BLACK:
            return Blocks.BLACK_SHULKER_BOX;
      }
   }

   public EnumDyeColor getColor() {
      return this.color;
   }

   public static ItemStack getColoredItemStack(EnumDyeColor var0) {
      return new ItemStack(getBlockByColor(☃));
   }

   @Override
   public IBlockState withRotation(IBlockState var1, Rotation var2) {
      return ☃.withProperty(FACING, ☃.rotate(☃.getValue(FACING)));
   }

   @Override
   public IBlockState withMirror(IBlockState var1, Mirror var2) {
      return ☃.withRotation(☃.toRotation(☃.getValue(FACING)));
   }

   @Override
   public BlockFaceShape getBlockFaceShape(IBlockAccess var1, IBlockState var2, BlockPos var3, EnumFacing var4) {
      ☃ = this.getActualState(☃, ☃, ☃);
      EnumFacing ☃ = ☃.getValue(FACING);
      TileEntityShulkerBox.AnimationStatus ☃x = ((TileEntityShulkerBox)☃.getTileEntity(☃)).getAnimationStatus();
      return ☃x != TileEntityShulkerBox.AnimationStatus.CLOSED && (☃x != TileEntityShulkerBox.AnimationStatus.OPENED || ☃ != ☃.getOpposite() && ☃ != ☃)
         ? BlockFaceShape.UNDEFINED
         : BlockFaceShape.SOLID;
   }
}
