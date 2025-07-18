package net.minecraft.client.tutorial;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public interface ITutorialStep {
   default void onStop() {
   }

   default void update() {
   }

   default void handleMovement(MovementInput var1) {
   }

   default void handleMouse(MouseHelper var1) {
   }

   default void onMouseHover(WorldClient var1, RayTraceResult var2) {
   }

   default void onHitBlock(WorldClient var1, BlockPos var2, IBlockState var3, float var4) {
   }

   default void openInventory() {
   }

   default void handleSetSlot(ItemStack var1) {
   }
}
