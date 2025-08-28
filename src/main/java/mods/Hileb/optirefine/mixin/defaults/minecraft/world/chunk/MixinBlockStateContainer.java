package mods.Hileb.optirefine.mixin.defaults.minecraft.world.chunk;

import mods.Hileb.optirefine.library.common.utils.Checked;
import net.minecraft.world.chunk.BlockStateContainer;
import org.spongepowered.asm.mixin.Mixin;

@Checked
@SuppressWarnings("unused")
@Mixin(BlockStateContainer.class)
public abstract class MixinBlockStateContainer{

}
/*
--- net/minecraft/world/chunk/BlockStateContainer.java	Tue Aug 19 14:59:42 2025
+++ net/minecraft/world/chunk/BlockStateContainer.java	Tue Aug 19 14:59:58 2025
@@ -118,13 +118,13 @@
       for (int var4 = 0; var4 < 4096; var4++) {
          int var5 = var4 & 15;
          int var6 = var4 >> 8 & 15;
          int var7 = var4 >> 4 & 15;
          int var8 = var3 == null ? 0 : var3.get(var5, var6, var7);
          int var9 = var8 << 12 | (var1[var4] & 255) << 4 | var2.get(var5, var6, var7);
-         this.set(var4, Block.BLOCK_STATE_IDS.getByValue(var9));
+         this.set(var4, (IBlockState)Block.BLOCK_STATE_IDS.getByValue(var9));
       }
    }

    public int getSerializedSize() {
       return 1 + this.palette.getSerializedSize() + PacketBuffer.getVarIntSize(this.storage.size()) + this.storage.getBackingLongArray().length * 8;
    }
 */
