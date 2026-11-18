package mods.Hileb.optirefine.validation;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.optifine.override.ChunkCacheOF;

final class ChunkCacheContracts {
    private ChunkCacheContracts() {}

    static void verifyLightFloor(Minecraft mc) {
        BlockPos pos = new BlockPos(mc.player.posX, 128, mc.player.posZ);
        BlockPos from = pos.add(-1, -1, -1);
        BlockPos to = pos.add(16, 16, 16);
        ChunkCache vanilla = new ChunkCache(mc.world, from, to, 1);
        ChunkCacheOF cached = new ChunkCacheOF(vanilla, from, to, 1);
        int dark = vanilla.getCombinedLight(pos, 0);
        int bright = vanilla.getCombinedLight(pos, 15);
        if (dark == bright) throw new AssertionError("Light floor fixture has no brightness difference");

        cached.renderStart();
        try {
            if (cached.getCombinedLight(pos, 0) != dark || cached.getCombinedLight(pos, 15) != bright) {
                throw new AssertionError("A cached default light value masked the requested light floor");
            }
            // The backing cache can still answer a query beyond this section's padded box.
            BlockPos outside = to.up(2);
            if (cached.getCombinedLight(outside, 0) != vanilla.getCombinedLight(outside, 0)
                    || cached.getBlockState(outside) != vanilla.getBlockState(outside)) {
                throw new AssertionError("A query outside the padded section did not fall back to the backing cache");
            }
        } finally {
            cached.renderFinish();
        }
        cached.renderStart();
        try {
            if (cached.getCombinedLight(pos, 15) != bright || cached.getCombinedLight(pos, 0) != dark) {
                throw new AssertionError("A nonzero light floor contaminated the cached default light value");
            }
        } finally {
            cached.renderFinish();
        }
    }
}
