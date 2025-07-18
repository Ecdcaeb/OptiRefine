package net.minecraft.block.state;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBlockBehaviors {
   boolean onBlockEventReceived(World var1, BlockPos var2, int var3, int var4);

   void neighborChanged(World var1, BlockPos var2, Block var3, BlockPos var4);
}
