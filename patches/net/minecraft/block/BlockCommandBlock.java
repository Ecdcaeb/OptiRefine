package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlockCommandBlock extends BlockContainer {
   private static final Logger LOGGER = LogManager.getLogger();
   public static final PropertyDirection FACING = BlockDirectional.FACING;
   public static final PropertyBool CONDITIONAL = PropertyBool.create("conditional");

   public BlockCommandBlock(MapColor var1) {
      super(Material.IRON, ☃);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(CONDITIONAL, false));
   }

   @Override
   public TileEntity createNewTileEntity(World var1, int var2) {
      TileEntityCommandBlock ☃ = new TileEntityCommandBlock();
      ☃.setAuto(this == Blocks.CHAIN_COMMAND_BLOCK);
      return ☃;
   }

   @Override
   public void neighborChanged(IBlockState var1, World var2, BlockPos var3, Block var4, BlockPos var5) {
      if (!☃.isRemote) {
         TileEntity ☃ = ☃.getTileEntity(☃);
         if (☃ instanceof TileEntityCommandBlock) {
            TileEntityCommandBlock ☃x = (TileEntityCommandBlock)☃;
            boolean ☃xx = ☃.isBlockPowered(☃);
            boolean ☃xxx = ☃x.isPowered();
            ☃x.setPowered(☃xx);
            if (!☃xxx && !☃x.isAuto() && ☃x.getMode() != TileEntityCommandBlock.Mode.SEQUENCE) {
               if (☃xx) {
                  ☃x.setConditionMet();
                  ☃.scheduleUpdate(☃, this, this.tickRate(☃));
               }
            }
         }
      }
   }

   @Override
   public void updateTick(World var1, BlockPos var2, IBlockState var3, Random var4) {
      if (!☃.isRemote) {
         TileEntity ☃ = ☃.getTileEntity(☃);
         if (☃ instanceof TileEntityCommandBlock) {
            TileEntityCommandBlock ☃x = (TileEntityCommandBlock)☃;
            CommandBlockBaseLogic ☃xx = ☃x.getCommandBlockLogic();
            boolean ☃xxx = !StringUtils.isNullOrEmpty(☃xx.getCommand());
            TileEntityCommandBlock.Mode ☃xxxx = ☃x.getMode();
            boolean ☃xxxxx = ☃x.isConditionMet();
            if (☃xxxx == TileEntityCommandBlock.Mode.AUTO) {
               ☃x.setConditionMet();
               if (☃xxxxx) {
                  this.execute(☃, ☃, ☃, ☃xx, ☃xxx);
               } else if (☃x.isConditional()) {
                  ☃xx.setSuccessCount(0);
               }

               if (☃x.isPowered() || ☃x.isAuto()) {
                  ☃.scheduleUpdate(☃, this, this.tickRate(☃));
               }
            } else if (☃xxxx == TileEntityCommandBlock.Mode.REDSTONE) {
               if (☃xxxxx) {
                  this.execute(☃, ☃, ☃, ☃xx, ☃xxx);
               } else if (☃x.isConditional()) {
                  ☃xx.setSuccessCount(0);
               }
            }

            ☃.updateComparatorOutputLevel(☃, this);
         }
      }
   }

   private void execute(IBlockState var1, World var2, BlockPos var3, CommandBlockBaseLogic var4, boolean var5) {
      if (☃) {
         ☃.trigger(☃);
      } else {
         ☃.setSuccessCount(0);
      }

      executeChain(☃, ☃, ☃.getValue(FACING));
   }

   @Override
   public int tickRate(World var1) {
      return 1;
   }

   @Override
   public boolean onBlockActivated(
      World var1, BlockPos var2, IBlockState var3, EntityPlayer var4, EnumHand var5, EnumFacing var6, float var7, float var8, float var9
   ) {
      TileEntity ☃ = ☃.getTileEntity(☃);
      if (☃ instanceof TileEntityCommandBlock && ☃.canUseCommandBlock()) {
         ☃.displayGuiCommandBlock((TileEntityCommandBlock)☃);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean hasComparatorInputOverride(IBlockState var1) {
      return true;
   }

   @Override
   public int getComparatorInputOverride(IBlockState var1, World var2, BlockPos var3) {
      TileEntity ☃ = ☃.getTileEntity(☃);
      return ☃ instanceof TileEntityCommandBlock ? ((TileEntityCommandBlock)☃).getCommandBlockLogic().getSuccessCount() : 0;
   }

   @Override
   public void onBlockPlacedBy(World var1, BlockPos var2, IBlockState var3, EntityLivingBase var4, ItemStack var5) {
      TileEntity ☃ = ☃.getTileEntity(☃);
      if (☃ instanceof TileEntityCommandBlock) {
         TileEntityCommandBlock ☃x = (TileEntityCommandBlock)☃;
         CommandBlockBaseLogic ☃xx = ☃x.getCommandBlockLogic();
         if (☃.hasDisplayName()) {
            ☃xx.setName(☃.getDisplayName());
         }

         if (!☃.isRemote) {
            NBTTagCompound ☃xxx = ☃.getTagCompound();
            if (☃xxx == null || !☃xxx.hasKey("BlockEntityTag", 10)) {
               ☃xx.setTrackOutput(☃.getGameRules().getBoolean("sendCommandFeedback"));
               ☃x.setAuto(this == Blocks.CHAIN_COMMAND_BLOCK);
            }

            if (☃x.getMode() == TileEntityCommandBlock.Mode.SEQUENCE) {
               boolean ☃xxxx = ☃.isBlockPowered(☃);
               ☃x.setPowered(☃xxxx);
            }
         }
      }
   }

   @Override
   public int quantityDropped(Random var1) {
      return 0;
   }

   @Override
   public EnumBlockRenderType getRenderType(IBlockState var1) {
      return EnumBlockRenderType.MODEL;
   }

   @Override
   public IBlockState getStateFromMeta(int var1) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex(☃ & 7)).withProperty(CONDITIONAL, (☃ & 8) != 0);
   }

   @Override
   public int getMetaFromState(IBlockState var1) {
      return ☃.getValue(FACING).getIndex() | (☃.getValue(CONDITIONAL) ? 8 : 0);
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
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, FACING, CONDITIONAL);
   }

   @Override
   public IBlockState getStateForPlacement(World var1, BlockPos var2, EnumFacing var3, float var4, float var5, float var6, int var7, EntityLivingBase var8) {
      return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(☃, ☃)).withProperty(CONDITIONAL, false);
   }

   private static void executeChain(World var0, BlockPos var1, EnumFacing var2) {
      BlockPos.MutableBlockPos ☃ = new BlockPos.MutableBlockPos(☃);
      GameRules ☃x = ☃.getGameRules();
      int ☃xx = ☃x.getInt("maxCommandChainLength");

      while (☃xx-- > 0) {
         ☃.move(☃);
         IBlockState ☃xxx = ☃.getBlockState(☃);
         Block ☃xxxx = ☃xxx.getBlock();
         if (☃xxxx != Blocks.CHAIN_COMMAND_BLOCK) {
            break;
         }

         TileEntity ☃xxxxx = ☃.getTileEntity(☃);
         if (!(☃xxxxx instanceof TileEntityCommandBlock)) {
            break;
         }

         TileEntityCommandBlock ☃xxxxxx = (TileEntityCommandBlock)☃xxxxx;
         if (☃xxxxxx.getMode() != TileEntityCommandBlock.Mode.SEQUENCE) {
            break;
         }

         if (☃xxxxxx.isPowered() || ☃xxxxxx.isAuto()) {
            CommandBlockBaseLogic ☃xxxxxxx = ☃xxxxxx.getCommandBlockLogic();
            if (☃xxxxxx.setConditionMet()) {
               if (!☃xxxxxxx.trigger(☃)) {
                  break;
               }

               ☃.updateComparatorOutputLevel(☃, ☃xxxx);
            } else if (☃xxxxxx.isConditional()) {
               ☃xxxxxxx.setSuccessCount(0);
            }
         }

         ☃ = ☃xxx.getValue(FACING);
      }

      if (☃xx <= 0) {
         int ☃xxxxxxx = Math.max(☃x.getInt("maxCommandChainLength"), 0);
         LOGGER.warn("Commandblock chain tried to execure more than " + ☃xxxxxxx + " steps!");
      }
   }
}
